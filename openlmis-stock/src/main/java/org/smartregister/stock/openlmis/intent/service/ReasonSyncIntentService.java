package org.smartregister.stock.openlmis.intent.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import org.smartregister.service.ActionService;
import org.smartregister.service.HTTPAgent;
import org.smartregister.stock.openlmis.OpenLMISLibrary;
import org.smartregister.stock.openlmis.domain.openlmis.Program;
import org.smartregister.stock.openlmis.intent.helper.ReasonSyncHelper;
import org.smartregister.stock.util.NetworkUtils;

import java.util.List;

import static org.smartregister.stock.openlmis.util.OpenLMISConstants.FACILITY_TYPE_UUID;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.PROGRAM_ID;

public class ReasonSyncIntentService extends IntentService implements SyncIntentService {

    private static final String REASON_SYNC_URL = "rest/reasons/sync";

    private Context context;
    private ReasonSyncHelper syncHelper;

    public ReasonSyncIntentService() {
        super("ReasonSyncIntentService");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ActionService actionService = OpenLMISLibrary.getInstance().getContext().actionService();
        HTTPAgent httpAgent = OpenLMISLibrary.getInstance().getContext().getHttpAgent();
        this.context = getBaseContext();
        this.syncHelper = new ReasonSyncHelper(context, actionService, httpAgent);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        String facilityTypeUuid = OpenLMISLibrary.getInstance().getFacilityTypeUuid();;
        if (NetworkUtils.isNetworkAvailable(context)) {
            // assumes eventual consistency where all programs are synced and reasons for all programs can be fetched
            List<Program> programs = OpenLMISLibrary.getInstance().getProgramRepository().findAllPrograms();
            if (programs != null) {
                for (Program program : programs) {
                    if (facilityTypeUuid != null) {
                        pullFromServer(REASON_SYNC_URL + "?" + FACILITY_TYPE_UUID + "=" + facilityTypeUuid + "&" + PROGRAM_ID + "=" + program.getId());
                    } else {
                        pullFromServer(REASON_SYNC_URL + "?");
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void pullFromServer(String url) {
        syncHelper.processIntent(url);
    }
}
