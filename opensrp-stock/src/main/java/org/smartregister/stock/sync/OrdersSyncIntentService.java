package org.smartregister.stock.sync;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.stock.R;
import org.smartregister.stock.StockLibrary;
import org.smartregister.stock.domain.Order;
import org.smartregister.stock.repository.OrderRepository;
import org.smartregister.stock.util.NetworkUtils;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.smartregister.util.Log.logInfo;

/**
 * Created by Ephraim Kigamba - ekigamba@ona.io on 08/03/2018.
 */

public class OrdersSyncIntentService extends IntentService {

    private Context context;
    private static final String TAG = OrdersSyncIntentService.class.getName();

    private static final String ADD_ORDERS_URL = "rest/stockresource/order/add";
    private static final String GET_ORDERS_URL = "rest/stockresource/order/getOrders";

    public static final String LAST_ORDER_SERVER_VERSION_PREFERENCE = "LAST ORDER SERVER VERSION";
    private RequestQueue requestQueue;

    public OrdersSyncIntentService() {
        super(OrdersSyncIntentService.class.getName());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        context = getBaseContext();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        requestQueue = Volley.newRequestQueue(context);
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
        final OrderRepository orderRepository = StockLibrary.getInstance().getOrderRepository();

        final List<Order> ordersList = orderRepository.getAllUnSyncedOrders();
        JsonArray ordersJSONArray = createJSONArrayFromOrders(ordersList);

        String baseUrl = StockLibrary.getInstance().getContext().configuration().dristhiBaseURL();
        if (baseUrl.endsWith(context.getString(R.string.url_separator))) {
            baseUrl = baseUrl.substring(0, baseUrl.lastIndexOf(context.getString(R.string.url_separator)));
        }

        JsonObject jsonPayload = new JsonObject();

        jsonPayload.add("orders", ordersJSONArray);
        String fullUrl = MessageFormat.format("{0}/{1}", baseUrl, ADD_ORDERS_URL);
        Log.i(TAG, fullUrl);
        Log.i(TAG, jsonPayload.toString());

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, fullUrl, jsonPayload.toString(), new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    Log.i(TAG, response.toString());
                    orderRepository.setOrderStatusToSynced(ordersList);
                } else {
                    Log.i(TAG, "Push orders: The response was empty. This is okay");
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, Log.getStackTraceString(error));
            }

        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return authHeaders();
            }

            @Override
            protected com.android.volley.Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    String json = new String(
                            response.data,
                            "UTF-8"
                    );

                    if (json.length() == 0) {
                        return com.android.volley.Response.success(
                                null,
                                HttpHeaderParser.parseCacheHeaders(response)
                        );
                    } else {
                        return super.parseNetworkResponse(response);
                    }
                } catch (UnsupportedEncodingException e) {
                    Log.e(TAG, Log.getStackTraceString(e));
                    return com.android.volley.Response.error(new ParseError(e));
                }
            }
        };
        requestQueue.add(stringRequest);
    }

    private void pullOrdersFromServer() {
        final OrderRepository orderRepository = StockLibrary.getInstance().getOrderRepository();

        String baseUrl = StockLibrary.getInstance().getContext().configuration().dristhiBaseURL();
        if (baseUrl.endsWith(context.getString(R.string.url_separator))) {
            baseUrl = baseUrl.substring(0, baseUrl.lastIndexOf(context.getString(R.string.url_separator)));
        }

        final long lastServerVersion = getLastOrderServerVersion();

        AllSharedPreferences allSharedPreferences = new AllSharedPreferences(PreferenceManager.getDefaultSharedPreferences(context));
        String locationId = allSharedPreferences.fetchDefaultLocalityId(allSharedPreferences.fetchRegisteredANM());

        String fullUrl = MessageFormat.format("{0}/{1}?serverVersion={2}&locationId={3}", baseUrl, GET_ORDERS_URL, String.valueOf(lastServerVersion), locationId);
        Log.i(TAG, fullUrl);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, fullUrl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!TextUtils.isEmpty(response)) {
                    Log.i(TAG, response);
                    processResponsePayload(orderRepository, lastServerVersion, response);
                } else {
                    Log.e(TAG, "Pull orders: The response was empty. Something is wrong!!");
                }

                startShipmentSyncIntentService();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, Log.getStackTraceString(error));
                startShipmentSyncIntentService();
            }

        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return authHeaders();
            }
        };
        requestQueue.add(stringRequest);
    }

    private void processResponsePayload(OrderRepository orderRepository, long lastServerVersion, String responsePayload) {
        try {
            JSONObject jsonPayload = new JSONObject(responsePayload);
            JSONArray ordersJSONArray = jsonPayload.getJSONArray("orders");

            ArrayList<Order> ordersList = convertOrdersJSONArrayToList(ordersJSONArray);

            for(Order order: ordersList) {
                if (order.getServerVersion() > lastServerVersion) {
                    lastServerVersion = order.getServerVersion();
                }

                order.setSynced(true);
                orderRepository.addOrUpdateOrder(order);
            }

            setLastOrderServerVersion(lastServerVersion);
        } catch (JSONException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    private ArrayList<Order> convertOrdersJSONArrayToList(JSONArray jsonArray) {
        ArrayList<Order> orderArrayList = new ArrayList<>();

        int size = jsonArray.length();
        for(int i = 0; i < size; i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                Order order = new Order();
                order.setId(jsonObject.getString("id"));
                order.setSynced(true);
                order.setType("Order");
                order.setDateCreatedByClient(jsonObject.getLong("dateCreatedByClient"));
                order.setDateCreated(getDateFromString(jsonObject.getString("dateCreated")));
                order.setDateEdited(getDateFromString(jsonObject.getString("dateEdited")));
                order.setProviderId(jsonObject.getString("providerId"));
                order.setLocationId(jsonObject.getString("locationId"));
                order.setRevision(jsonObject.getString("revision"));
                order.setServerVersion(jsonObject.getLong("serverVersion"));

                orderArrayList.add(order);
            } catch (JSONException e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }
        }

        return orderArrayList;
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

    private long getDateFromString(String jsonDateFormat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
            Date date = simpleDateFormat.parse(jsonDateFormat);
            return date.getTime();
        } catch (ParseException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }

        return 0;
    }

    private void startShipmentSyncIntentService() {
        startService(new Intent(this, ShipmentSyncIntentService.class));
    }

    private Map<String, String> authHeaders() {
        final String USERNAME = StockLibrary.getInstance().getContext().allSharedPreferences().fetchRegisteredANM();
        final String PASSWORD = StockLibrary.getInstance().getContext().allSettings().fetchANMPassword();

        Map<String, String> headers = new HashMap<>();
        // add headers <key,value>
        String credentials = USERNAME + ":" + PASSWORD;
        String auth = "Basic "
                + Base64.encodeToString(credentials.getBytes(),
                Base64.NO_WRAP);
        headers.put("Authorization", auth);
        return headers;
    }

}
