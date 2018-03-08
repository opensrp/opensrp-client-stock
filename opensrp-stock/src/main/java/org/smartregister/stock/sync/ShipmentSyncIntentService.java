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
import org.smartregister.stock.domain.Shipment;
import org.smartregister.stock.repository.ShipmentRepository;
import org.smartregister.stock.util.Constants;
import org.smartregister.stock.util.NetworkUtils;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.smartregister.util.Log.logInfo;

/**
 * Created by Ephraim Kigamba - ekigamba@ona.io on 08/03/2018.
 */

public class ShipmentSyncIntentService extends IntentService {
    private Context context;
    private HTTPAgent httpAgent;
    private static final String TAG = ShipmentSyncIntentService.class.getName();
    private static final String GET_SHIPMENTS_URL = "rest/stockresource/shipment/getShipments";
    public static final String LAST_SHIPMENT_SERVER_VERSION_PREFERENCE = "LAST SHIPMENT SERVER VERSION";

    public ShipmentSyncIntentService(){
        super(ShipmentSyncIntentService.class.getName());
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
            // Pull shipments from server
            pullShipmentsFromServer();
        }
    }

    private void pullShipmentsFromServer() {
        ShipmentRepository shipmentRepository = StockLibrary.getInstance().getShipmentRepository();

        String baseUrl = StockLibrary.getInstance().getContext().configuration().dristhiBaseURL();
        if (baseUrl.endsWith(context.getString(R.string.url_separator))) {
            baseUrl = baseUrl.substring(0, baseUrl.lastIndexOf(context.getString(R.string.url_separator)));
        }

        long lastServerVersion = getLastShipmentServerVersion();
        AllSharedPreferences allSharedPreferences = new AllSharedPreferences(PreferenceManager.getDefaultSharedPreferences(context));
        String locationId = allSharedPreferences.fetchDefaultLocalityId(allSharedPreferences.fetchRegisteredANM());

        String fullUrl = MessageFormat.format("{0}/{1}?serverVersion={2}&receivingFacility.code={3}", baseUrl, GET_SHIPMENTS_URL, lastServerVersion, locationId);

        Response<String> response = httpAgent.fetch(fullUrl);
        if (response.isFailure()) {
            Log.e(TAG, "Server error occured trying to fetch Shipments");
            return;
        }

        String stringPayload = response.payload();
        try {
            JSONObject jsonObject = new JSONObject(stringPayload);
            JSONArray shipmentsJSONArray = jsonObject.getJSONArray("shipments");
            List<Shipment> shipments = getShipmentFromJSONArray(shipmentsJSONArray);
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

    private ArrayList<Shipment> getShipmentFromJSONArray(JSONArray shipmentsJSONArray) {
        ArrayList<Shipment> shipments = new ArrayList<>();
        for (int i = 0; i < shipmentsJSONArray.length(); i++) {
            try {
                JSONObject shipmentJSONObject = shipmentsJSONArray.getJSONObject(i);
                Shipment shipment = new Shipment(
                        shipmentJSONObject.getString(Constants.Shipment.ORDER_CODE),
                        getDateFromJsonFormat(shipmentJSONObject.getString(Constants.Shipment.ORDERED_DATE)),
                        shipmentJSONObject.getString(Constants.Shipment.RECEIVING_FACILITY_CODE),
                        shipmentJSONObject.getString(Constants.Shipment.RECEIVING_FACILITY_NAME),
                        shipmentJSONObject.getString(Constants.Shipment.SUPPLYING_FACILITY_CODE),
                        shipmentJSONObject.getString(Constants.Shipment.SUPPLYING_FACILITY_NAME),
                        getDateFromJsonFormat(shipmentJSONObject.getString(Constants.Shipment.PROCESSING_PERIOD_START_DATE)),
                        getDateFromJsonFormat(shipmentJSONObject.getString(Constants.Shipment.PROCESSING_PERIOD_END_DATE)),
                        shipmentJSONObject.getString(Constants.Shipment.SHIPMENT_ACCEPT_STATUS),
                        shipmentJSONObject.getLong(Constants.Shipment.SERVER_VERSION),
                        shipmentJSONObject.getBoolean(Constants.Shipment.SYNCED)
                );
                shipments.add(shipment);
            } catch (JSONException e) {
                Log.e(TAG, Log.getStackTraceString(e));

            }
        }
        return  shipments;
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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
            return simpleDateFormat.parse(dateStringInJson);
        } catch (ParseException e) {
            Log.e(TAG, Log.getStackTraceString(e));
            return null;
        }
    }
}
