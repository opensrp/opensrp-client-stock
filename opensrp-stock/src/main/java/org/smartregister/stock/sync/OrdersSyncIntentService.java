package org.smartregister.stock.sync;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

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
        JsonArray ordersJSONArray = createJSONArrayFromOrders(ordersList);

        String baseUrl = StockLibrary.getInstance().getContext().configuration().dristhiBaseURL();
        if (baseUrl.endsWith(context.getString(R.string.url_separator))) {
            baseUrl = baseUrl.substring(0, baseUrl.lastIndexOf(context.getString(R.string.url_separator)));
        }

        JsonObject jsonPayload = new JsonObject();

        jsonPayload.add("orders", ordersJSONArray);
        String fullUrl = MessageFormat.format("{0}/{1}", baseUrl, ADD_ORDERS_URL);

        Response<String> response = httpAgent.post(fullUrl, jsonPayload.toString());

        if (response.isFailure()) {
            Log.e(TAG, "Server error occured trying to push orders");
            return;
        }

        orderRepository.setOrderStatusToSynced(ordersList);

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

            Gson gson = new Gson();
            ArrayList<Order> ordersList = gson.fromJson(ordersJSONArray.toString(), new TypeToken<ArrayList<Order>>() {}.getType());

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


    private JsonArray createJSONArrayFromOrders(List<Order> orders) {
        Gson gson = new Gson();
        return (JsonArray) gson.toJsonTree(orders, new TypeToken<List<Order>>() {}.getType());
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
