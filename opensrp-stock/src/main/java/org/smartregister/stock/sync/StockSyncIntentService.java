package org.smartregister.stock.sync;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.AllConstants;
import org.smartregister.CoreLibrary;
import org.smartregister.domain.Response;
import org.smartregister.repository.BaseRepository;
import org.smartregister.service.ActionService;
import org.smartregister.service.HTTPAgent;
import org.smartregister.stock.R;
import org.smartregister.stock.StockLibrary;
import org.smartregister.stock.configuration.StockSyncConfiguration;
import org.smartregister.stock.domain.Stock;
import org.smartregister.stock.helper.StockSyncServiceHelper;
import org.smartregister.stock.repository.StockRepository;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

import static org.smartregister.stock.repository.StockRepository.DELIVERY_DATE;
import static org.smartregister.stock.util.Constants.StockResponseKey.ACCOUNTABILITY_END_DATE;
import static org.smartregister.stock.util.Constants.StockResponseKey.CUSTOM_PROPERTIES;
import static org.smartregister.stock.util.Constants.StockResponseKey.DONOR;
import static org.smartregister.stock.util.Constants.StockResponseKey.ID;
import static org.smartregister.stock.util.Constants.StockResponseKey.IDENTIFIER;
import static org.smartregister.stock.util.Constants.StockResponseKey.LOCATION_ID;
import static org.smartregister.stock.util.Constants.StockResponseKey.SERIAL_NUMBER;
import static org.smartregister.stock.util.Constants.StockResponseKey.SERVER_VERSION;
import static org.smartregister.stock.util.Constants.StockResponseKey.TYPE;
import static org.smartregister.stock.util.Constants.StockResponseKey.VERSION;
import static org.smartregister.util.Log.logError;

/**
 * Created by samuelgithengi on 2/16/18.
 */

public class StockSyncIntentService extends IntentService {

    private static final String TAG = "StockSyncIntentService";

    private static final String STOCK_ADD_PATH = "rest/stockresource/add/";
    private static final String STOCK_SYNC_PATH = "rest/stockresource/sync/";

    private Context context;
    private HTTPAgent httpAgent;
    private ActionService actionService;
    private StockSyncConfiguration stockSyncConfiguration;
    private StockSyncServiceHelper stockSyncServiceHelper;

    public StockSyncIntentService() {
        super(TAG);
    }

    public StockSyncIntentService(String name) {
        super(name);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        context = getBaseContext();
        httpAgent = StockLibrary.getInstance().getContext().getHttpAgent();
        actionService = StockLibrary.getInstance().getContext().actionService();
        stockSyncConfiguration = StockLibrary.getInstance().getStockSyncConfiguration();
        stockSyncServiceHelper = stockSyncConfiguration.getStockSyncIntentServiceHelper();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // push
        if (stockSyncConfiguration.canPushStockToServer()) {
            pushStockToServer();
        }

        // pull
        pullStockFromServer();

        if (stockSyncConfiguration.hasActions()) {
            actionService.fetchNewActions();
        }
    }

    @NotNull
    public String getFormattedBaseUrl() {
        String baseUrl = CoreLibrary.getInstance().context().configuration().dristhiBaseURL();
        String endString = "/";
        if (baseUrl.endsWith(endString)) {
            baseUrl = baseUrl.substring(0, baseUrl.lastIndexOf(endString));
        }
        return baseUrl;
    }

    private void pullStockFromServer() {
        final String LAST_STOCK_SYNC = "last_stock_sync";
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String baseUrl = getFormattedBaseUrl();

        while (true) {
            long timestamp = preferences.getLong(LAST_STOCK_SYNC, 0);
            if (timestamp > 0) {
                timestamp += 1;
            }
            String timeStampString = String.valueOf(timestamp);
            String url = getSyncUrl(baseUrl, timeStampString);
            Map<String, Object> syncParams = new HashMap<>();
            syncParams.put(AllConstants.SERVER_VERSION, timeStampString);
            Response<String> response = stockSyncConfiguration.syncStockByPost() ? httpAgent.postWithJsonResponse(url, stockSyncConfiguration.stockSyncRequestBody(syncParams)) : httpAgent.fetch(url);
            if (response.isFailure()) {
                logError("Stock pull failed.");
                return;
            }
            String jsonPayload = response.payload();
            List<Stock> stockItems = getStockFromPayload(jsonPayload);
            if (stockItems == null || stockItems.isEmpty()) {
                return;
            }
            Long highestTimestamp = getHighestTimestampFromStockPayLoad(stockItems);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putLong(LAST_STOCK_SYNC, highestTimestamp);
            editor.commit();
            stockSyncServiceHelper.batchInsertStocks(stockItems);
        }
    }

    protected String getSyncUrl(String baseUrl, String timeStampString) {
        String url = "";
        if (stockSyncConfiguration.syncStockByPost()) {
            url = MessageFormat.format("{0}/{1}",
                    baseUrl,
                    STOCK_SYNC_PATH
            );
        } else {
            url = MessageFormat.format("{0}/{1}?serverVersion={2}{3}",
                    baseUrl,
                    STOCK_SYNC_PATH,
                    timeStampString,
                    stockSyncConfiguration.getStockSyncParams()
            );
        }
        return url;
    }

    private Long getHighestTimestampFromStockPayLoad(List<Stock> stocks) {
        Long toReturn = 0L;
        try {
            for (Stock stock : stocks) {
                if (stock.getServerVersion() > toReturn) {
                    toReturn = stock.getServerVersion();
                }
            }
        } catch (Exception e) {
            Timber.e(e);
        }
        return toReturn;
    }

    public List<Stock> getStockFromPayload(String jsonPayload) {
        return createStockResponse(jsonPayload);
    }

    private List<Stock> createStockResponse(String jsonPayload) {
        List<Stock> stocks = new ArrayList<>();
        try {
            JSONObject stockJsonObject = new JSONObject(jsonPayload);
            if (stockJsonObject.has(context.getString(R.string.stocks_key))) {
                JSONArray stockArray = stockJsonObject.getJSONArray(context.getString(R.string.stocks_key));
                for (int i = 0; i < stockArray.length(); i++) {
                    JSONObject stockObject = stockArray.getJSONObject(i);
                    Stock stock = new Stock(null,
                            stockObject.optString(context.getString(R.string.transaction_type_key)),
                            stockObject.optString(context.getString(R.string.providerid_key)),
                            stockObject.optInt(context.getString(R.string.value_key)),
                            stockObject.optLong(context.getString(R.string.date_created_key)),
                            stockObject.optString(context.getString(R.string.to_from_key)),
                            BaseRepository.TYPE_Synced,
                            stockObject.optLong(context.getString(R.string.date_updated_key)),
                            stockObject.optString(context.getString(R.string.stock_type_id_key)));
                    stock.setIdentifier(stockObject.optString(IDENTIFIER));
                    stock.setLocationId(stockObject.optString(LOCATION_ID));
                    stock.setStockId(stockObject.optString(ID));
                    stock.setCustomProperties(stockObject.optString(CUSTOM_PROPERTIES));
                    stock.setServerVersion(stockObject.optLong(SERVER_VERSION));
                    stock.setVersion(stockObject.optLong(VERSION));
                    stock.setType(stockObject.optString(TYPE));
                    stock.setDonor(stockObject.optString(DONOR));
                    stock.setDeliveryDate(stockObject.optString(DELIVERY_DATE));
                    stock.setAccountabilityEndDate(stockObject.optString(ACCOUNTABILITY_END_DATE));
                    stock.setSerialNumber(stockObject.optString(SERIAL_NUMBER));
                    stocks.add(stock);
                }
            }
        } catch (JSONException e) {
            Timber.e(e);
        }
        return stocks;
    }

    private void pushStockToServer() {
        boolean keepSyncing = true;
        int limit = 50;

        try {
            while (keepSyncing) {
                StockRepository stockRepository = StockLibrary.getInstance().getStockRepository();
                ArrayList<Stock> stocks = (ArrayList<Stock>) stockRepository.findUnSyncedWithLimit(limit);
                JSONArray stocksarray = createJsonArrayFromStockArray(stocks);
                if (stocks.isEmpty()) {
                    return;
                }

                String baseUrl = getFormattedBaseUrl();

                // create request body
                JSONObject request = new JSONObject();
                request.put(context.getString(R.string.stocks_key), stocksarray);

                String jsonPayload = request.toString();
                Response<String> response = httpAgent.post(
                        MessageFormat.format("{0}/{1}",
                                baseUrl,
                                STOCK_ADD_PATH),
                        jsonPayload);
                if (response.isFailure()) {
                    Timber.e("Stocks sync failed.");
                    return;
                }
                stockRepository.markEventsAsSynced(stocks);
                Timber.i("Stocks synced successfully.");
            }
        } catch (JSONException e) {
            Timber.e(e);
        }
    }

    private JSONArray createJsonArrayFromStockArray(ArrayList<Stock> stocks) {
        JSONArray array = new JSONArray();
        for (int i = 0; i < stocks.size(); i++) {
            JSONObject stock = new JSONObject();
            try {
                stock.put("identifier", stocks.get(i).getId());
                stock.put(context.getString(R.string.stock_type_id_key), stocks.get(i).getStockTypeId());
                stock.put(context.getString(R.string.transaction_type_key), stocks.get(i).getTransactionType());
                stock.put(context.getString(R.string.providerid_key), stocks.get(i).getProviderid());
                stock.put(context.getString(R.string.date_created_key), stocks.get(i).getDateCreated());
                stock.put(context.getString(R.string.value_key), stocks.get(i).getValue());
                stock.put(context.getString(R.string.to_from_key), stocks.get(i).getToFrom());
                stock.put(context.getString(R.string.date_updated_key), stocks.get(i).getUpdatedAt());
                array.put(stock);
            } catch (JSONException e) {
                Timber.e(e);
            }
        }
        return array;
    }
}
