package org.smartregister.stock.openlmis.intent.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import org.smartregister.service.ActionService;
import org.smartregister.service.HTTPAgent;
import org.smartregister.stock.openlmis.OpenLMISLibrary;
import org.smartregister.stock.openlmis.intent.helper.CommodityTypeSyncHelper;
import org.smartregister.stock.util.NetworkUtils;

public class CommodityTypeSyncIntentService extends IntentService implements SyncIntentService {

    public CommodityTypeSyncIntentService() {
        super("CommodityTypeSyncIntentService");
    }
    private CommodityTypeSyncHelper syncHelper;
    private Context context;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        ActionService actionService= OpenLMISLibrary.getInstance().getContext().actionService();
        HTTPAgent httpAgent = OpenLMISLibrary.getInstance().getContext().getHttpAgent();
        this.context = getBaseContext();
        this.syncHelper = new CommodityTypeSyncHelper(context, actionService, httpAgent);

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
        syncHelper.processIntent();
    }
}
