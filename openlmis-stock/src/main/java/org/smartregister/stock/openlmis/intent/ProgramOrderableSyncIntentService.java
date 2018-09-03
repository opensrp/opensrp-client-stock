package org.smartregister.stock.openlmis.intent;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.smartregister.domain.Response;
import org.smartregister.service.ActionService;
import org.smartregister.service.HTTPAgent;
import org.smartregister.stock.openlmis.OpenLMISLibrary;
import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.domain.openlmis.ProgramOrderable;
import org.smartregister.stock.openlmis.repository.openlmis.ProgramOrderableRepository;
import org.smartregister.stock.util.NetworkUtils;

import java.text.MessageFormat;
import java.util.List;

import static org.smartregister.stock.openlmis.util.Utils.BASE_URL;
import static org.smartregister.stock.openlmis.util.Utils.makeGetRequest;
import static org.smartregister.util.Log.logError;
import static org.smartregister.util.Log.logInfo;

public class ProgramOrderableSyncIntentService extends IntentService implements SyncIntentService {

    private static final String LOT_SYNC_URL = "rest/program-orderables/sync";
    private Context context;
    private HTTPAgent httpAgent;
    private ActionService actionService;

    public ProgramOrderableSyncIntentService() {
        super("ProgramOrderableSyncIntentService");
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

        final String PREV_SYNC_SERVER_VERSION = "prev_sync_server_version";
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
        // make request
        try {
            String jsonPayload = makeGetRequest(uri);
            if (jsonPayload == null) {
                logError("ProgramOrderables pull failed.");
                return;
            }
            logInfo("ProgramOrderables successfully pulled!");
            // store programOrderables
            Long highestTimeStamp = 0L;
            List<ProgramOrderable> programOrderables = new Gson().fromJson(jsonPayload, new TypeToken<List<ProgramOrderable>>(){}.getType());
            ProgramOrderableRepository repository = OpenLMISLibrary.getInstance().getProgramOrderableRepository();
            for (ProgramOrderable programOrderable : programOrderables) {
                repository.addOrUpdate(programOrderable);
                if (programOrderable.getServerVersion() > highestTimeStamp) {
                    highestTimeStamp = programOrderable.getServerVersion();
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
