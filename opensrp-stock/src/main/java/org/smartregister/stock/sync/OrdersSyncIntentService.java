package org.smartregister.stock.sync;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.domain.Response;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.service.HTTPAgent;
import org.smartregister.stock.R;
import org.smartregister.stock.StockLibrary;
import org.smartregister.stock.domain.Order;
import org.smartregister.stock.domain.Shipment;
import org.smartregister.stock.repository.OrderRepository;
import org.smartregister.stock.repository.ShipmentRepository;
import org.smartregister.stock.util.Constants;
import org.smartregister.stock.util.NetworkUtils;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.prefs.Preferences;

import static org.smartregister.util.Log.logInfo;

/**
 * Created by Ephraim Kigamba - ekigamba@ona.io on 08/03/2018.
 */

public class OrdersSyncIntentService extends IntentService {

    private Context context;
    private HTTPAgent httpAgent;
    private static final String TAG = OrdersSyncIntentService.class.getName();

    private static final String ADD_ORDERS_URL = "rest/stockresource/order/add";
    private static final String GET_ORDERS_URL = "rest/stockresource/order/getOrders";

    public static final String LAST_ORDER_SERVER_VERSION_PREFERENCE = "LAST ORDER SERVER VERSION";

    public OrdersSyncIntentService() {
        super(OrdersSyncIntentService.class.getName());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        context = getBaseContext();
        httpAgent = StockLibrary.getInstance().getContext().getHttpAgent();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (StockLibrary.getInstance().getContext().IsUserLoggedOut()) {
            logInfo("Not updating from server since user is not logged in.");
            return;
        }

        if (NetworkUtils.isNetworkAvailable(context)) {
            // Push orders to the server
            pushOrdersToServer();
            // Pull orders from server
            pullOrdersFromServer();
        }
    }

    private void pushOrdersToServer() {
        OrderRepository orderRepository = StockLibrary.getInstance().getOrderRepository();

        List<Order> ordersList = orderRepository.getAllUnSyncedOrders();
        JSONArray ordersJSONArray = createJSONArrayFromOrders(ordersList);

        String baseUrl = StockLibrary.getInstance().getContext().configuration().dristhiBaseURL();
        if (baseUrl.endsWith(context.getString(R.string.url_separator))) {
            baseUrl = baseUrl.substring(0, baseUrl.lastIndexOf(context.getString(R.string.url_separator)));
        }

        JSONObject jsonPayload = new JSONObject();
        try {
            jsonPayload.put("orders", ordersJSONArray);
            String fullUrl = MessageFormat.format("{0}/{1}", baseUrl, ADD_ORDERS_URL);

            Response<String> response = httpAgent.post(fullUrl, jsonPayload.toString());

            if (response.isFailure()) {
                Log.e(TAG, "Server error occured trying to push orders");
                return;
            }

            orderRepository.setOrderStatusToSynced(ordersList);
        } catch (JSONException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    private void pullOrdersFromServer() {
        OrderRepository orderRepository = StockLibrary.getInstance().getOrderRepository();

        String baseUrl = StockLibrary.getInstance().getContext().configuration().dristhiBaseURL();
        if (baseUrl.endsWith(context.getString(R.string.url_separator))) {
            baseUrl = baseUrl.substring(0, baseUrl.lastIndexOf(context.getString(R.string.url_separator)));
        }

        long lastServerVersion = getLastOrderServerVersion();

        AllSharedPreferences allSharedPreferences = new AllSharedPreferences(PreferenceManager.getDefaultSharedPreferences(context));
        String locationId = allSharedPreferences.fetchDefaultLocalityId(allSharedPreferences.fetchRegisteredANM());

        String fullUrl = MessageFormat.format("{0}/{1}?serverVersion={2}&locationId={3}", baseUrl, GET_ORDERS_URL, lastServerVersion, locationId);
        Response<String> response = httpAgent.fetch(fullUrl);

        if (response.isFailure()) {
            Log.e(TAG, "Server error occured trying to pull orders from the server");
            return;
        }

        String responsePayload = response.payload();
        try {
            JSONObject jsonPayload = new JSONObject(responsePayload);
            JSONArray ordersJSONArray = jsonPayload.getJSONArray("orders");

            ArrayList<Order> ordersList = createOrdersListFromJSONArray(ordersJSONArray);

            for(Order order: ordersList) {
                if (order.getServerVersion() > lastServerVersion) {
                    lastServerVersion = order.getServerVersion();
                }

                orderRepository.addOrUpdateOrder(order);
            }

            setLastOrderServerVersion(lastServerVersion);
        } catch (JSONException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }

    }


    private JSONArray createJSONArrayFromOrders(List<Order> orders) {
        JSONArray jsonArray = new JSONArray();

        try {
            for (Order order : orders) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(Constants.Order.ID, order.getId());
                jsonObject.put(Constants.Order.REVISION, order.getRevision());
                jsonObject.put(Constants.Order.TYPE, order.getType());
                jsonObject.put(Constants.Order.DATE_CREATED, order.getDateCreated());
                jsonObject.put(Constants.Order.DATE_EDITED, order.getDateEdited());
                jsonObject.put(Constants.Order.SERVER_VERSION, order.getServerVersion());
                jsonObject.put(Constants.Order.LOCATION_ID, order.getLocationId());
                jsonObject.put(Constants.Order.PROVIDER_ID, order.getProviderId());
                jsonObject.put(Constants.Order.DATE_CREATED_BY_CLIENT, order.getDateCreatedByClient());

                jsonArray.put(jsonObject);
            }
        } catch (JSONException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }

        return jsonArray;
    }

    private ArrayList<Order> createOrdersListFromJSONArray(JSONArray jsonArray) {
        ArrayList<Order> ordersList = new ArrayList<>();
        int count = jsonArray.length();

        for(int i = 0; i < count; i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Order order = createOrderFromJSONObject(jsonObject);
                ordersList.add(order);
            } catch (JSONException e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }
        }

        return ordersList;
    }

    private Order createOrderFromJSONObject(JSONObject jsonObject) throws JSONException {
        Order order = new Order();
        order.setId(jsonObject.getString(Constants.Order.ID));
        order.setRevision(jsonObject.getString(Constants.Order.REVISION));
        order.setType(jsonObject.getString(Constants.Order.TYPE));
        order.setDateCreated(jsonObject.getLong(Constants.Order.DATE_CREATED));
        order.setDateEdited(jsonObject.getLong(Constants.Order.DATE_EDITED));
        order.setServerVersion(jsonObject.getLong(Constants.Order.SERVER_VERSION));
        order.setLocationId(jsonObject.getString(Constants.Order.LOCATION_ID));
        order.setProviderId(jsonObject.getString(Constants.Order.PROVIDER_ID));
        order.setDateCreatedByClient(jsonObject.getLong(Constants.Order.DATE_CREATED_BY_CLIENT));

        return order;
    }

    private long getLastOrderServerVersion() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getLong(LAST_ORDER_SERVER_VERSION_PREFERENCE, 0);
    }

    private void setLastOrderServerVersion(long lastOrderServerVersion) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(LAST_ORDER_SERVER_VERSION_PREFERENCE, lastOrderServerVersion);
        editor.commit();
    }
}
