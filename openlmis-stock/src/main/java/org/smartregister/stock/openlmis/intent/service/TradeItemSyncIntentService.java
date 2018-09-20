package org.smartregister.stock.openlmis.intent.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import org.smartregister.service.ActionService;
import org.smartregister.service.HTTPAgent;
import org.smartregister.stock.openlmis.OpenLMISLibrary;
import org.smartregister.stock.openlmis.intent.helper.TradeItemSyncHelper;
import org.smartregister.stock.util.NetworkUtils;

public class TradeItemSyncIntentService extends IntentService implements SyncIntentService {

    private Context context;
    private TradeItemSyncHelper syncHelper;

    public TradeItemSyncIntentService() {
        super("TradeItemSyncIntentService");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        ActionService actionService = OpenLMISLibrary.getInstance().getContext().actionService();
        HTTPAgent httpAgent = OpenLMISLibrary.getInstance().getContext().getHttpAgent();
        this.context = getBaseContext();
        this.syncHelper = new TradeItemSyncHelper(context, actionService, httpAgent);

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
