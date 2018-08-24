package org.smartregister.stock.openlmis.intent;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpClientConnection;
import org.smartregister.domain.Response;
import org.smartregister.service.ActionService;
import org.smartregister.service.HTTPAgent;
import org.smartregister.stock.openlmis.OpenLMISLibrary;
import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.domain.openlmis.TradeItem;
import org.smartregister.stock.openlmis.repository.openlmis.TradeItemRepository;
import org.smartregister.stock.util.NetworkUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.List;

import static org.smartregister.stock.openlmis.util.Utils.BASE_URL;
import static org.smartregister.stock.openlmis.util.Utils.PREV_SYNC_SERVER_VERSION;
import static org.smartregister.stock.openlmis.util.Utils.makeGetRequest;
import static org.smartregister.util.Log.logError;

public class TradeItemSyncIntentService extends IntentService implements SyncIntentService {

    private static final String TRADE_ITEM_SYNC_URL = "rest/trade-items/sync";
    private Context context;
    private HTTPAgent httpAgent;
    private ActionService actionService;

    public TradeItemSyncIntentService() {
        super("TradeItemSyncIntentService");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        context = getBaseContext();
        actionService = OpenLMISLibrary.getInstance().getContext().actionService();
        httpAgent = OpenLMISLibrary.getInstance().getContext().getHttpAgent();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        if (NetworkUtils.isNetworkAvailable(context)) {
            pullFromServer();
        }
    }

    @Override
    public void pullFromServer() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String baseUrl = OpenLMISLibrary.getInstance().getContext().configuration().dristhiBaseURL();
        if (baseUrl.endsWith(context.getString(R.string.url_separator))) {
            baseUrl = baseUrl.substring(0, baseUrl.lastIndexOf(context.getString(R.string.url_separator)));
        }
        long timestamp = preferences.getLong(PREV_SYNC_SERVER_VERSION, 0);
        String uri = MessageFormat.format("{0}/{1}?sync_server_version={2}",
                BASE_URL,
                TRADE_ITEM_SYNC_URL,
                timestamp
        );
        // TODO: make baseUrl configurable
        while (true) {
            try {
                String jsonPayload = makeGetRequest(uri);
                if (jsonPayload == null) {
                    logError("TradeItemClassifications pull failed.");
                    return;
                }
                // store tradeItems
                Long highestTimeStamp = 0L;
                List<TradeItem> tradeItems = new Gson().fromJson(jsonPayload, new TypeToken<List<TradeItem>>(){}.getType());
                TradeItemRepository repository = OpenLMISLibrary.getInstance().getTradeItemRepository();
                for (TradeItem tradeItem : tradeItems) {
                    repository.addOrUpdate(tradeItem);
                    if (tradeItem.getServerVersion() > highestTimeStamp) {
                        highestTimeStamp = tradeItem.getServerVersion();
                    }
                }
                // save highest server version
                SharedPreferences.Editor editor = preferences.edit();
                editor.putLong(PREV_SYNC_SERVER_VERSION, highestTimeStamp);
                editor.commit();
            } catch (Exception e) {
                logError(e.getMessage());
            }
        }
    }
}
