package org.smartregister.stock.openlmis.intent.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.smartregister.CoreLibrary;
import org.smartregister.service.ActionService;
import org.smartregister.service.HTTPAgent;
import org.smartregister.stock.openlmis.OpenLMISLibrary;
import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.domain.openlmis.FacilityProgram;
import org.smartregister.stock.openlmis.domain.openlmis.Program;
import org.smartregister.stock.openlmis.repository.openlmis.ProgramRepository;

import java.text.MessageFormat;
import java.util.List;

import static org.smartregister.stock.openlmis.util.OpenLMISConstants.PREV_SYNC_SERVER_VERSION_PROGRAM;
import static org.smartregister.stock.openlmis.util.Utils.BASE_URL;
import static org.smartregister.stock.openlmis.util.Utils.makeGetRequest;
import static org.smartregister.util.Log.logError;
import static org.smartregister.util.Log.logInfo;

public class ProgramSyncHelper extends BaseSyncHelper {

    private HTTPAgent httpAgent;
    private ActionService actionService;
    private ProgramRepository repository;

    public ProgramSyncHelper(Context context, ActionService actionService, HTTPAgent httpAgent) {
        this.repository = OpenLMISLibrary.getInstance().getProgramRepository();
        this.context = context;
        this.httpAgent = httpAgent;
        this.actionService = actionService;
    }

    @Override
    protected String pullFromServer(String url) {

        SharedPreferences preferences = CoreLibrary.getInstance().context().allSharedPreferences().getPreferences();
        String baseUrl = OpenLMISLibrary.getInstance().getContext().configuration().dristhiBaseURL();
        if (baseUrl.endsWith(context.getString(R.string.url_separator))) {
            baseUrl = baseUrl.substring(0, baseUrl.lastIndexOf(context.getString(R.string.url_separator)));
        }
        long timestamp = preferences.getLong(PREV_SYNC_SERVER_VERSION_PROGRAM, 0);
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
                logError("Programs pull failed.");
            }
            logInfo("Programs successfully pulled!");
            // store programs
        } catch (Exception e) {
            logError(e.getMessage());
            return jsonPayload;
        }
        return jsonPayload;
    }

    @Override
    public boolean saveResponse(String jsonPayload, SharedPreferences preferences) {

        Long highestTimeStamp = 0L;
        List<FacilityProgram> facilityPrograms = new Gson().fromJson(jsonPayload, new TypeToken<List<FacilityProgram>>(){}.getType());
        boolean isEmptyResponse = true;
        for (FacilityProgram facilityProgram : facilityPrograms) {
            List<Program> supportedPrograms = facilityProgram.getSupportedPrograms();
            if (supportedPrograms == null) { continue; }
            for (Program program : supportedPrograms) {
                if (program != null) {
                    isEmptyResponse = false;
                    repository.addOrUpdate(program);
                    if (program.getServerVersion() > highestTimeStamp) {
                        highestTimeStamp = program.getServerVersion();
                    }
                }
            }
        }
        // save highest server version
        if (!isEmptyResponse) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putLong(PREV_SYNC_SERVER_VERSION_PROGRAM, highestTimeStamp + 1);
            editor.apply();
        }
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

    public ProgramRepository getRepository() {
        return repository;
    }

    public void setRepository(ProgramRepository repository) {
        this.repository = repository;
    }
}
