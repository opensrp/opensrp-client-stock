package org.smartregister.stock.openlmis.intent.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import org.smartregister.service.ActionService;
import org.smartregister.service.HTTPAgent;
import org.smartregister.stock.openlmis.OpenLMISLibrary;
import org.smartregister.stock.openlmis.intent.helper.ProgramSyncHelper;
import org.smartregister.stock.util.NetworkUtils;

import static org.smartregister.stock.openlmis.util.OpenLMISConstants.FACILITY_TYPE_UUID;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.OPENLMIS_UUID;

public class ProgramSyncIntentService extends IntentService implements SyncIntentService {

    private static final String PROGRAM_SYNC_URL = "rest/facility-programs/sync";

    private Context context;
    private ProgramSyncHelper syncHelper;

    public ProgramSyncIntentService() {
        super("ProgramSyncIntentService");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ActionService actionService = OpenLMISLibrary.getInstance().getContext().actionService();
        HTTPAgent httpAgent = OpenLMISLibrary.getInstance().getContext().getHttpAgent();
        this.context = getBaseContext();
        this.syncHelper = new ProgramSyncHelper(context, actionService, httpAgent);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {

        String facilityTypeUuid = OpenLMISLibrary.getInstance().getFacilityTypeUuid();
        String openlmisUuid = OpenLMISLibrary.getInstance().getOpenlmisUuid();
        if (NetworkUtils.isNetworkAvailable(context)) {
            if (facilityTypeUuid != null && openlmisUuid != null) {
                pullFromServer( PROGRAM_SYNC_URL + "?" + FACILITY_TYPE_UUID + "=" + facilityTypeUuid + "&" + OPENLMIS_UUID +  "=" + openlmisUuid);
            } else if (facilityTypeUuid == null && openlmisUuid == null) {
                pullFromServer(PROGRAM_SYNC_URL + "?");
            }
        }
    }

    @Override
    public void pullFromServer(String url) {
        syncHelper.processIntent(url);
    }
}

