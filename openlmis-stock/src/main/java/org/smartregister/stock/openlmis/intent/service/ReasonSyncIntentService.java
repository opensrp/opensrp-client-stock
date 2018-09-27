package org.smartregister.stock.openlmis.intent.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import org.smartregister.service.ActionService;
import org.smartregister.service.HTTPAgent;
import org.smartregister.stock.openlmis.OpenLMISLibrary;
import org.smartregister.stock.openlmis.intent.helper.ReasonSyncHelper;
import org.smartregister.stock.util.NetworkUtils;

import static org.smartregister.stock.openlmis.util.OpenLMISConstants.FACILITY_TYPE_UUID;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.PROGRAM_ID;

public class ReasonSyncIntentService extends IntentService implements SyncIntentService {

    private static final String REASON_SYNC_URL = "rest/reasons/sync";

    private Context context;
    private ReasonSyncHelper syncHelper;
    private String facilityTypeUuid;
    private String programId;

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
        facilityTypeUuid = workIntent.getStringExtra(FACILITY_TYPE_UUID);
        programId = workIntent.getStringExtra(PROGRAM_ID);
        if (NetworkUtils.isNetworkAvailable(context)) {
            if (facilityTypeUuid != null && programId != null) {
                pullFromServer(REASON_SYNC_URL + "?" + FACILITY_TYPE_UUID + "=" + facilityTypeUuid + "&" + PROGRAM_ID +  "=" + programId);
            }
            pullFromServer(REASON_SYNC_URL);
        }
    }

    @Override
    public void pullFromServer(String url) {
        syncHelper.processIntent(url);
    }
}
