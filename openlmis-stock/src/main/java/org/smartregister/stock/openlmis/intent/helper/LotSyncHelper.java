package org.smartregister.stock.openlmis.intent.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.smartregister.service.ActionService;
import org.smartregister.service.HTTPAgent;
import org.smartregister.stock.openlmis.OpenLMISLibrary;
import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.domain.openlmis.Lot;
import org.smartregister.stock.openlmis.repository.openlmis.LotRepository;

import java.text.MessageFormat;
import java.util.List;

import static org.smartregister.stock.openlmis.util.Utils.BASE_URL;
import static org.smartregister.stock.openlmis.util.Utils.PREV_SYNC_SERVER_VERSION;
import static org.smartregister.stock.openlmis.util.Utils.makeGetRequest;
import static org.smartregister.util.Log.logError;
import static org.smartregister.util.Log.logInfo;

public class LotSyncHelper implements BaseSyncHelper {

    private Context context;
    private HTTPAgent httpAgent;
    private ActionService actionService;
    private static final String LOT_SYNC_URL = "rest/lots/sync";

    public LotSyncHelper(Context context, ActionService actionService, HTTPAgent httpAgent) {
        this.context = context;
        this.actionService = actionService;
        this.httpAgent = httpAgent;
    }

    @Override
    public void processIntent() {
        String response = pullFromServer();
        if (response == null) {
            return;
        }
        saveResponse(response, PreferenceManager.getDefaultSharedPreferences(context));
    }

    private String pullFromServer() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String baseUrl = OpenLMISLibrary.getInstance().getContext().configuration().dristhiBaseURL();
        if (baseUrl.endsWith(context.getString(R.string.url_separator))) {
            baseUrl = baseUrl.substring(0, baseUrl.lastIndexOf(context.getString(R.string.url_separator)));
        }
        long timestamp = preferences.getLong(PREV_SYNC_SERVER_VERSION, 0);
        String timestampStr = String.valueOf(timestamp);
        String uri = MessageFormat.format("{0}/{1}?sync_server_version={2}",
                BASE_URL,
                LOT_SYNC_URL,
                timestampStr
        );
        // TODO: make baseUrl configurable
        String jsonPayload = null;
        try {
            jsonPayload = makeGetRequest(uri);
            if (jsonPayload == null) {
                logError("Lots pull failed.");
            }
            logInfo("Lots pulled successfully!");
        } catch (Exception e) {
            logError(e.getMessage());
            return jsonPayload;
        }
        return jsonPayload;
    }

    @Override
    public void saveResponse(String jsonPayload, SharedPreferences preferences) {

        // store lots
        Long highestTimeStamp = 0L;
        List<Lot> lots = new Gson().fromJson(jsonPayload, new TypeToken<List<Lot>>(){}.getType());
        LotRepository repository = OpenLMISLibrary.getInstance().getLotRepository();
        for (Lot lot : lots) {
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
}
