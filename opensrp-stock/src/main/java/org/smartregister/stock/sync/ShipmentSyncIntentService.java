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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.stock.R;
import org.smartregister.stock.StockLibrary;
import org.smartregister.stock.domain.Shipment;
import org.smartregister.stock.domain.ShipmentLineItem;
import org.smartregister.stock.repository.ShipmentRepository;
import org.smartregister.stock.util.NetworkUtils;

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

public class ShipmentSyncIntentService extends IntentService {
    private Context context;
    private static final String TAG = ShipmentSyncIntentService.class.getName();
    private static final String GET_SHIPMENTS_URL = "rest/stockresource/shipment/getShipments";
    public static final String LAST_SHIPMENT_SERVER_VERSION_PREFERENCE = "LAST SHIPMENT SERVER VERSION";

    public ShipmentSyncIntentService(){
        super(ShipmentSyncIntentService.class.getName());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        context = getBaseContext();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (StockLibrary.getInstance().getContext().IsUserLoggedOut()) {
            logInfo("Not updating from server since user is not logged in.");
            return;
        }

        if (NetworkUtils.isNetworkAvailable(context)) {
            // Pull shipments from server
            pullShipmentsFromServer();
        }
    }

    private void pullShipmentsFromServer() {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        final ShipmentRepository shipmentRepository = StockLibrary.getInstance().getShipmentRepository();

        String baseUrl = StockLibrary.getInstance().getContext().configuration().dristhiBaseURL();
        if (baseUrl.endsWith(context.getString(R.string.url_separator))) {
            baseUrl = baseUrl.substring(0, baseUrl.lastIndexOf(context.getString(R.string.url_separator)));
        }

        final long lastServerVersion = getLastShipmentServerVersion();
        AllSharedPreferences allSharedPreferences = new AllSharedPreferences(PreferenceManager.getDefaultSharedPreferences(context));
        String locationId = allSharedPreferences.fetchDefaultLocalityId(allSharedPreferences.fetchRegisteredANM());

        String fullUrl = MessageFormat.format("{0}/{1}?serverVersion={2}&receivingFacility.code={3}", baseUrl, GET_SHIPMENTS_URL, String.valueOf(lastServerVersion), locationId);
        Log.i(TAG, fullUrl);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, fullUrl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!TextUtils.isEmpty(response)) {
                    Log.i(TAG, response);
                    processResponsePayload(response, lastServerVersion, shipmentRepository);
                } else {
                    Log.e(TAG, "Get shipments: The response was empty. Something is wrong");
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
        };
        requestQueue.add(stringRequest);
    }

    private void processResponsePayload(String stringPayload, long lastServerVersion, ShipmentRepository shipmentRepository) {
        try {
            JSONObject jsonObject = new JSONObject(stringPayload);
            JSONArray shipmentsJSONArray = jsonObject.getJSONArray("shipments");

            List<Shipment> shipments = getShipments(shipmentsJSONArray);

            for (Shipment shipment : shipments) {
                shipmentRepository.addShipment(shipment);
                if (lastServerVersion < shipment.getServerVersion()) {
                    lastServerVersion = shipment.getServerVersion();
                }
            }
            setLastShipmentServerVersion(lastServerVersion);
        } catch (JSONException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    private ArrayList<Shipment> getShipments(JSONArray jsonArray) throws JSONException {
        ArrayList<Shipment> shipmentsList = new ArrayList<>();
        int count = jsonArray.length();

        for(int i = 0; i < count; i++) {
            shipmentsList.add(getShipment(jsonArray.getJSONObject(i)));
        }

        return shipmentsList;
    }

    private Shipment getShipment(JSONObject jsonObject) throws JSONException {
        JSONObject supplyingFacility = jsonObject.getJSONObject("supplyingFacility");
        JSONObject receivingFacility = jsonObject.getJSONObject("receivingFacility");
        JSONObject processingPeriod = jsonObject.getJSONObject("processingPeriod");

        Shipment shipment = new Shipment();
        shipment.setOrderCode(jsonObject.getString("orderCode"));
        shipment.setOrderedDate(getDateFromJsonFormat(jsonObject.getString("orderedDate")));
        shipment.setServerVersion(jsonObject.getLong("serverVersion"));
        shipment.setSynced(true);
        shipment.setProcessingPeriodEndDate(getDateFromJsonFormat(processingPeriod.getString("endDate")));
        shipment.setProcessingPeriodStartDate(getDateFromJsonFormat(processingPeriod.getString("startDate")));
        shipment.setReceivingFacilityCode(receivingFacility.getString("code"));
        shipment.setReceivingFacilityName(receivingFacility.getString("name"));
        shipment.setSupplyingFacilityCode(supplyingFacility.getString("code"));
        shipment.setSupplyingFacilityName(supplyingFacility.getString("name"));

        JSONArray shipmentLineItemsJSONArray = jsonObject.getJSONArray("lineItems");
        int count = shipmentLineItemsJSONArray.length();

        ShipmentLineItem[] shipmentLineItems = new ShipmentLineItem[count];
        for(int i = 0; i < count; i++) {
            JSONObject shipmentLineItemJSONObject = shipmentLineItemsJSONArray.getJSONObject(i);
            shipmentLineItems[i] = getShipmentLineItemFromJSONObject(shipment, shipmentLineItemJSONObject);
        }
        shipment.setShipmentLineItems(shipmentLineItems);

        return shipment;
    }

    private ShipmentLineItem getShipmentLineItemFromJSONObject(Shipment shipment, JSONObject jsonObject) throws JSONException {
        ShipmentLineItem shipmentLineItem = new ShipmentLineItem();
        shipmentLineItem.setAntigenType(jsonObject.getString("antigenType"));
        shipmentLineItem.setNumberOfDoses(jsonObject.getInt("numDoses"));
        shipmentLineItem.setOrderedQuantity(jsonObject.getInt("orderedQuantity"));
        shipmentLineItem.setShippedQuantity(jsonObject.getInt("shippedQuantity"));
        shipmentLineItem.setShipmentOrderCode(shipment.getOrderCode());

        return shipmentLineItem;
    }

    private long getLastShipmentServerVersion() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getLong(LAST_SHIPMENT_SERVER_VERSION_PREFERENCE, 0);
    }

    private void setLastShipmentServerVersion(long lastShipmentServerVersion) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putLong(LAST_SHIPMENT_SERVER_VERSION_PREFERENCE, lastShipmentServerVersion);
        editor.commit();
    }

    private Date getDateFromJsonFormat(String dateStringInJson) {
        try {
            if (dateStringInJson.length() < 11) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                return simpleDateFormat.parse(dateStringInJson);
            } else {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                return simpleDateFormat.parse(dateStringInJson);
            }
        } catch (ParseException e) {
            Log.e(TAG, Log.getStackTraceString(e));
            return null;
        }
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
