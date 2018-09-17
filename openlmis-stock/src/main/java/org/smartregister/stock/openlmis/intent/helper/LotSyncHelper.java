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

import static org.smartregister.stock.openlmis.util.OpenLMISConstants.PREV_SYNC_SERVER_VERSION_LOT;
import static org.smartregister.stock.openlmis.util.Utils.BASE_URL;
import static org.smartregister.stock.openlmis.util.Utils.makeGetRequest;
import static org.smartregister.util.Log.logError;
import static org.smartregister.util.Log.logInfo;

public class LotSyncHelper extends BaseSyncHelper {

    private HTTPAgent httpAgent;
    private ActionService actionService;
    private LotRepository repository;
    private static final String LOT_SYNC_URL = "rest/lots/sync";

    public LotSyncHelper(Context context, ActionService actionService, HTTPAgent httpAgent) {
        this.repository = OpenLMISLibrary.getInstance().getLotRepository();
        this.context = context;
        this.actionService = actionService;
        this.httpAgent = httpAgent;
    }

    protected String pullFromServer() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String baseUrl = OpenLMISLibrary.getInstance().getContext().configuration().dristhiBaseURL();
        if (baseUrl.endsWith(context.getString(R.string.url_separator))) {
            baseUrl = baseUrl.substring(0, baseUrl.lastIndexOf(context.getString(R.string.url_separator)));
        }
        long timestamp = preferences.getLong(PREV_SYNC_SERVER_VERSION_LOT, 0);
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
    public boolean saveResponse(String jsonPayload, SharedPreferences preferences) {

        // store lots
        Long highestTimeStamp = 0L;
        List<Lot> lots = new Gson().fromJson(jsonPayload, new TypeToken<List<Lot>>(){}.getType());
        boolean isEmptyResponse = true;
        for (Lot lot : lots) {
            isEmptyResponse = false;
            repository.addOrUpdate(lot);
            if (lot.getServerVersion() > highestTimeStamp) {
                highestTimeStamp = lot.getServerVersion();
            }
        }
        // save highest server version
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(PREV_SYNC_SERVER_VERSION_LOT, highestTimeStamp);
        editor.commit();

        return isEmptyResponse;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public HTTPAgent getHttpAgent() {
        return httpAgent;
    }

    public void setHttpAgent(HTTPAgent httpAgent) {
        this.httpAgent = httpAgent;
    }

    public ActionService getActionService() {
        return actionService;
    }

    public void setActionService(ActionService actionService) {
        this.actionService = actionService;
    }

    public LotRepository getRepository() {
        return repository;
    }

    public void setRepository(LotRepository repository) {
        this.repository = repository;
    }
}
