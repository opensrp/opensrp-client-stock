package org.smartregister.stock.openlmis.intent;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.smartregister.domain.Response;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.service.HTTPAgent;
import org.smartregister.stock.StockLibrary;
import org.smartregister.stock.openlmis.OpenLMISLibrary;
import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.domain.openlmis.Lot;
import org.smartregister.stock.openlmis.repository.openlmis.LotRepository;
import org.smartregister.stock.util.NetworkUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import static org.smartregister.util.Log.logError;
import static org.smartregister.util.Log.logInfo;

public class LotSyncIntentService extends IntentService implements SyncIntentService  {

    private static final String LOT_SYNC_URL = "rest/lots/sync";
    private Context context;
    private HTTPAgent httpAgent;

    public LotSyncIntentService() {
        super("LotSyncIntentService");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        context = getBaseContext();
        httpAgent = StockLibrary.getInstance().getContext().getHttpAgent();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {

        if (OpenLMISLibrary.getInstance().getContext().IsUserLoggedOut()) {
            logInfo("Not updating from server as user is not logged in.");
            return;
        }
        if (NetworkUtils.isNetworkAvailable(context)) {
            pullFromServer();
        }
    }

    @Override
    public void pullFromServer() {

        final String PREV_SYNC_SERVER_VERSION = "prev_sync_server_version";
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        AllSharedPreferences allSharedPreferences = new AllSharedPreferences(preferences);
        String baseUrl = OpenLMISLibrary.getInstance().getContext().configuration().dristhiBaseURL();

        if (baseUrl.endsWith(context.getString(R.string.url_separator))) {
            baseUrl = baseUrl.substring(0, baseUrl.lastIndexOf(context.getString(R.string.url_separator)));
        }

        long timestamp = preferences.getLong(PREV_SYNC_SERVER_VERSION, 0);
        String timeStampString = String.valueOf(timestamp);
        String uri = MessageFormat.format("{0}/{1}?serverVersion={3}",
                baseUrl,
                LOT_SYNC_URL,
                timeStampString
        );

        Response<String> response = httpAgent.fetch(uri);
        if (response.isFailure()) {
            logError("Stock pull failed.");
            return;
        }

        String jsonPayload = response.payload();
        List<Lot> lots = getLotsFromPayload(jsonPayload);

        // store lots
        Long highestTimeStamp = 0L;
        LotRepository repository = OpenLMISLibrary.getInstance().getLotRepository();
        for (int i = 0; i < lots.size(); i++) {
            Lot lot = lots.get(i);
            repository.addOrUpdate(lot);
            if (lot.getServerVersion() > highestTimeStamp) {
                highestTimeStamp = lot.getServerVersion();
            }
        }

        // save highest server version
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(PREV_SYNC_SERVER_VERSION, highestTimeStamp);
        editor.commit();
    }

    private List<Lot> getLotsFromPayload(String jsonPayload) {

        List<Lot> lots = new ArrayList<>();
        try {
            return new Gson().fromJson(jsonPayload, new TypeToken<List<Lot>>(){}.getType());
        } catch (Exception e) {
            Log.e(getClass().getCanonicalName(), e.getMessage());
        }
        return lots;
    }
}
