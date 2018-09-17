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
import org.smartregister.stock.openlmis.domain.openlmis.Dispensable;
import org.smartregister.stock.openlmis.repository.openlmis.DispensableRepository;
import org.smartregister.stock.openlmis.util.SynchronizedUpdater;

import java.text.MessageFormat;
import java.util.List;

import static org.smartregister.stock.openlmis.util.OpenLMISConstants.PREV_SYNC_SERVER_VERSION_DISPENSABLE;
import static org.smartregister.stock.openlmis.util.Utils.BASE_URL;
import static org.smartregister.stock.openlmis.util.Utils.makeGetRequest;
import static org.smartregister.util.Log.logError;
import static org.smartregister.util.Log.logInfo;

public class DispensableSyncHelper extends BaseSyncHelper {

    private static final String DISPENSABLE_SYNC_URL = "rest/dispensables/sync";
    private HTTPAgent httpAgent;
    private ActionService actionService;
    private DispensableRepository repository;

    public DispensableSyncHelper(Context context, ActionService actionService, HTTPAgent httpAgent) {
        this.repository = OpenLMISLibrary.getInstance().getDispensableRepository();
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
        long timestamp = preferences.getLong(PREV_SYNC_SERVER_VERSION_DISPENSABLE, 0);
        String timestampStr = String.valueOf(timestamp);
        String uri = MessageFormat.format("{0}/{1}?sync_server_version={2}",
                BASE_URL,
                DISPENSABLE_SYNC_URL,
                timestampStr
        );
        // TODO: make baseUrl configurable
        String jsonPayload = null;
        try {
            jsonPayload = makeGetRequest(uri);
            if (jsonPayload == null) {
                logError("Dispensables pull failed.");
            }
            logInfo("Dispensables pulled succesfully!");
        } catch (Exception e) {
            logError(e.getMessage());
            return jsonPayload;
        }
        return jsonPayload;
    }

    @Override
    public boolean saveResponse(String jsonPayload, SharedPreferences preferences) {

        // store dispensables
        Long highestTimeStamp = 0L;
        List<Dispensable> dispensables = new Gson().fromJson(jsonPayload, new TypeToken<List<Dispensable>>(){}.getType());
        boolean isEmptyResponse = true;
        for (Dispensable dispensable : dispensables) {
            isEmptyResponse = false;
            SynchronizedUpdater.getInstance().updateInfo(dispensable);
            if (dispensable.getServerVersion() > highestTimeStamp) {
                highestTimeStamp = dispensable.getServerVersion();
            }
            repository.addOrUpdate(dispensable);
        }
        // save highest server version
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(PREV_SYNC_SERVER_VERSION_DISPENSABLE, highestTimeStamp);
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

    public DispensableRepository getRepository() {
        return repository;
    }

    public void setRepository(DispensableRepository repository) {
        this.repository = repository;
    }
}
