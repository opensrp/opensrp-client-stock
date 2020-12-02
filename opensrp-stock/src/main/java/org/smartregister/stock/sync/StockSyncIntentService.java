package org.smartregister.stock.sync;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.CoreLibrary;
import org.smartregister.domain.Response;
import org.smartregister.service.ActionService;
import org.smartregister.service.HTTPAgent;
import org.smartregister.stock.R;
import org.smartregister.stock.StockLibrary;
import org.smartregister.stock.configuration.StockSyncConfiguration;
import org.smartregister.stock.domain.Stock;
import org.smartregister.stock.domain.StockResponse;
import org.smartregister.stock.repository.StockRepository;
import org.smartregister.stock.util.GsonUtil;
import org.smartregister.stock.util.StockSyncIntentServiceHelper;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

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
    private StockSyncIntentServiceHelper stockSyncIntentServiceHelper;

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
        stockSyncIntentServiceHelper = stockSyncConfiguration.getStockSyncIntentServiceHelper();
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
            String timeStampString = String.valueOf(timestamp);
            String uri = getSyncUrl(baseUrl, timeStampString);
            Response<String> response = httpAgent.fetch(uri);
            if (response.isFailure()) {
                logError("Stock pull failed.");
                return;
            }
            String jsonPayload = response.payload();
            List<Stock> stockItems = getStockFromPayload(jsonPayload);
            Long highestTimestamp = getHighestTimestampFromStockPayLoad(stockItems);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putLong(LAST_STOCK_SYNC, highestTimestamp);
            editor.commit();
            if (stockItems.isEmpty()) {
                return;
            } else {
                stockSyncIntentServiceHelper.batchInsertStocks(stockItems);
            }
        }
    }

    protected String getSyncUrl(String baseUrl, String timeStampString) {
        return MessageFormat.format("{0}/{1}?serverVersion={2}{3}",
                baseUrl,
                STOCK_SYNC_PATH,
                timeStampString,
                stockSyncConfiguration.getStockSyncParams()
        );
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
        try {
            StockResponse stockResponse = GsonUtil.getGson().fromJson(jsonPayload, new TypeToken<StockResponse>() {
            }.getType());
            return stockResponse.getStocks();
        } catch (Exception e) {
            Timber.e(e);
        }
        return new ArrayList<>();
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
