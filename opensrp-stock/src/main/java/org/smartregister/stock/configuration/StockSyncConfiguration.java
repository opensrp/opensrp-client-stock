package org.smartregister.stock.configuration;

import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.stock.helper.StockSyncServiceHelper;
import org.smartregister.util.Utils;

import java.util.Map;

import timber.log.Timber;

public class StockSyncConfiguration {

    /**
     * Adds query parameter during stock sync, Default includes the providerid
     *
     * @return String
     */
    public String getStockSyncParams() {
        return String.format("&providerid=%s", Utils.getAllSharedPreferences().fetchRegisteredANM());
    }

    /**
     * If enabled unsynced stocks will be pushed to the server, Default true
     *
     * @return boolean
     */
    public boolean canPushStockToServer() {
        return true;
    }

    /**
     * if enabled it will get actions from the server, Default true
     *
     * @return boolean
     */
    public boolean hasActions() {
        return true;
    }

    /**
     * if enabled it will fetch images belonging to the stockType/Products, Default false
     *
     * @return boolean
     */
    public boolean shouldFetchStockTypeImages() {
        return false;
    }

    /**
     * use this to override the {@link StockSyncServiceHelper}
     *
     * @return StockSyncServiceHelper
     */
    public StockSyncServiceHelper getStockSyncIntentServiceHelper() {
        return new StockSyncServiceHelper(this);
    }

    /**
     * if enabled it will use the default method to check for uniqueness of stock
     *
     * @return boolean
     */
    public boolean useDefaultStockExistenceCheck() {
        return true;
    }

    /**
     * if enabled it will use POST to sync stock
     *
     * @return
     */
    public boolean syncStockByPost() {
        return false;
    }

    /**
     * if specified it populates the stock requestBody
     *
     * @param syncParams
     * @return
     */
    public String stockSyncRequestBody(Map<String, String> syncParams) {
        JSONObject jsonObjectRequestBody = new JSONObject();
        try {
            for (Map.Entry<String, String> entry : syncParams.entrySet()) {
                jsonObjectRequestBody.put(entry.getKey(), entry.getValue());
            }
        } catch (JSONException e) {
            Timber.e(e);
        }
        return jsonObjectRequestBody.toString();
    }
}
