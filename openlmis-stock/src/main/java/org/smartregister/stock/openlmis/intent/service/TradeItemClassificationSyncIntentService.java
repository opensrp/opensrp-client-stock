package org.smartregister.stock.openlmis.intent.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import org.smartregister.service.ActionService;
import org.smartregister.service.HTTPAgent;
import org.smartregister.stock.openlmis.OpenLMISLibrary;
import org.smartregister.stock.openlmis.intent.helper.TradeItemClassificationSyncHelper;
import org.smartregister.stock.util.NetworkUtils;

public class TradeItemClassificationSyncIntentService extends IntentService implements SyncIntentService {

    private static final String TRADE_ITEM_CLASSIFICATION_SYNC_URL = "rest/trade-item-classifications/sync";

    private Context context;
    private TradeItemClassificationSyncHelper syncHelper;

    public TradeItemClassificationSyncIntentService() {
        super("TradeItemClassificationSyncIntentService");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ActionService actionService = OpenLMISLibrary.getInstance().getContext().actionService();
        HTTPAgent httpAgent = OpenLMISLibrary.getInstance().getContext().getHttpAgent();
        this.context = getBaseContext();
        this.syncHelper = new TradeItemClassificationSyncHelper(context, actionService, httpAgent);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        if (NetworkUtils.isNetworkAvailable(context)) {
            pullFromServer(TRADE_ITEM_CLASSIFICATION_SYNC_URL);
        }
    }

    @Override
    public void pullFromServer(String url) {
        syncHelper.processIntent(url);
    }
}
