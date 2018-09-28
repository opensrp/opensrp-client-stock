package org.smartregister.stock.openlmis.intent.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import org.smartregister.service.ActionService;
import org.smartregister.service.HTTPAgent;
import org.smartregister.stock.openlmis.OpenLMISLibrary;
import org.smartregister.stock.openlmis.intent.helper.ValidSourceDestinationSyncHelper;
import org.smartregister.stock.util.NetworkUtils;

import static org.smartregister.stock.openlmis.util.OpenLMISConstants.FACILITY_TYPE_UUID;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.OPENLMIS_UUID;

public class ValidSourceDestinationSyncIntentService extends IntentService {

    private static final String VALID_SOURCE_SYNC_URL = "rest/valid-sources/sync";
    private static final String VALID_DESTINATION_SYNC_URL = "rest/valid-destinations/sync";

    private Context context;
    private ValidSourceDestinationSyncHelper validSourceSyncHelper;
    private ValidSourceDestinationSyncHelper validDestinationSyncHelper;
    private String facilityTypeUuid;
    private String openlmisUuid;

    public ValidSourceDestinationSyncIntentService() {
        super("ValidSourceDestinationSyncIntentService");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ActionService actionService = OpenLMISLibrary.getInstance().getContext().actionService();
        HTTPAgent httpAgent = OpenLMISLibrary.getInstance().getContext().getHttpAgent();
        this.context = getBaseContext();
        this.validSourceSyncHelper = new ValidSourceDestinationSyncHelper(context, actionService, httpAgent, true);
        this.validDestinationSyncHelper = new ValidSourceDestinationSyncHelper(context, actionService, httpAgent, false);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        facilityTypeUuid = workIntent.getStringExtra(FACILITY_TYPE_UUID);
        openlmisUuid = workIntent.getStringExtra(OPENLMIS_UUID);
        if (NetworkUtils.isNetworkAvailable(context)) {
            if (facilityTypeUuid != null && openlmisUuid != null) {
                pullValidDestinationsFromServer(VALID_DESTINATION_SYNC_URL + "?" + FACILITY_TYPE_UUID + "=" + facilityTypeUuid + "&" + OPENLMIS_UUID +  "=" + openlmisUuid);
                pullValidSourcesFromServer(VALID_SOURCE_SYNC_URL + "?" + FACILITY_TYPE_UUID + "=" + facilityTypeUuid + "&" + OPENLMIS_UUID +  "=" + openlmisUuid);
            }
            pullValidDestinationsFromServer(VALID_DESTINATION_SYNC_URL);
            pullValidSourcesFromServer(VALID_SOURCE_SYNC_URL);
        }
    }

    public void pullValidSourcesFromServer(String url) {
        validSourceSyncHelper.processIntent(url);
    }

    public void pullValidDestinationsFromServer(String url) {
        validDestinationSyncHelper.processIntent(url);
    }
}
