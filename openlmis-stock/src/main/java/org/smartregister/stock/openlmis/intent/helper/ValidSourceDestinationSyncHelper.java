package org.smartregister.stock.openlmis.intent.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.smartregister.service.ActionService;
import org.smartregister.service.HTTPAgent;
import org.smartregister.stock.openlmis.OpenLMISLibrary;
import org.smartregister.stock.openlmis.domain.openlmis.ValidSourceDestination;
import org.smartregister.stock.openlmis.repository.openlmis.ValidSourceDestinationRepository;

import java.text.MessageFormat;
import java.util.List;

import static org.smartregister.stock.openlmis.util.OpenLMISConstants.PREV_SYNC_SERVER_VERSION_VALID_DESTINATION;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.PREV_SYNC_SERVER_VERSION_VALID_SOURCE;
import static org.smartregister.stock.openlmis.util.Utils.BASE_URL;
import static org.smartregister.stock.openlmis.util.Utils.makeGetRequest;
import static org.smartregister.util.Log.logError;
import static org.smartregister.util.Log.logInfo;

public class ValidSourceDestinationSyncHelper extends BaseSyncHelper {

    private ActionService actionService;
    private HTTPAgent httpAgent;
    private ValidSourceDestinationRepository repository;
    private boolean isSource;

    public ValidSourceDestinationSyncHelper(Context context, ActionService actionService, HTTPAgent httpAgent, boolean isSource) {
        this.repository = OpenLMISLibrary.getInstance().getValidSourceDestinationRepository();
        this.context = context;
        this.actionService = actionService;
        this.httpAgent = httpAgent;
        this.isSource = isSource;
    }

    @Override
    protected String pullFromServer(String url) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String baseUrl = OpenLMISLibrary.getInstance().getContext().configuration().dristhiBaseURL();
        if (baseUrl.endsWith(context.getString(org.smartregister.stock.openlmis.R.string.url_separator))) {
            baseUrl = baseUrl.substring(0, baseUrl.lastIndexOf(context.getString(org.smartregister.stock.openlmis.R.string.url_separator)));
        }

        long timestamp;
        if (isSource) {
            timestamp = preferences.getLong(PREV_SYNC_SERVER_VERSION_VALID_SOURCE, 0);
        } else {
            timestamp = preferences.getLong(PREV_SYNC_SERVER_VERSION_VALID_DESTINATION, 0);
        }
        String timestampStr = String.valueOf(timestamp);
        String uri = MessageFormat.format("{0}/{1}&sync_server_version={2}",
                BASE_URL,
                url,
                timestampStr
        );
        // TODO: make baseUrl configurable
        String jsonPayload = null;
        try {
            jsonPayload = makeGetRequest(uri);
            if (jsonPayload == null) {
                logError("ValidSourceDestinations pull failed.");
            }
            logInfo("ValidSourceDestinations successfully pulled!");
        } catch (Exception e) {
            logError(e.getMessage());
            return jsonPayload;
        }
        return jsonPayload;
    }

    @Override
    public boolean saveResponse(String jsonPayload, SharedPreferences preferences) {

        // store validSourceDestinations
        Long highestTimeStamp = 0L;
        List<ValidSourceDestination> validSourceDestinations = new Gson().fromJson(jsonPayload, new TypeToken<List<ValidSourceDestination>>(){}.getType());
        boolean isEmptyResponse = true;
        for (ValidSourceDestination validSourceDestination : validSourceDestinations) {
            isEmptyResponse = false;
            if (isSource) {
                validSourceDestination.setSource(isSource);
            }
            repository.addOrUpdate(validSourceDestination);
            if (validSourceDestination.getServerVersion() > highestTimeStamp) {
                highestTimeStamp = validSourceDestination.getServerVersion();
            }
        }
        // save highest server version
        if (!isEmptyResponse) {
            SharedPreferences.Editor editor = preferences.edit();
            if (isSource) {
                editor.putLong(PREV_SYNC_SERVER_VERSION_VALID_SOURCE, highestTimeStamp + 1);
            } else {
                editor.putLong(PREV_SYNC_SERVER_VERSION_VALID_DESTINATION, highestTimeStamp + 1);
            }
            editor.commit();
        }
        return isEmptyResponse;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ActionService getActionService() {
        return actionService;
    }

    public void setActionService(ActionService actionService) {
        this.actionService = actionService;
    }

    public HTTPAgent getHttpAgent() {
        return httpAgent;
    }

    public void setHttpAgent(HTTPAgent httpAgent) {
        this.httpAgent = httpAgent;
    }

    public ValidSourceDestinationRepository getRepository() {
        return repository;
    }

    public void setRepository(ValidSourceDestinationRepository repository) {
        this.repository = repository;
    }
}
