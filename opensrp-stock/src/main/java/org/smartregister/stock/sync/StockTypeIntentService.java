package org.smartregister.stock.sync;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

import org.smartregister.stock.helper.SyncStockTypeServiceHelper;

public class StockTypeIntentService extends IntentService {
    public static final String SYNC_URL = "/rest/product-catalogue";

    private static final String TAG = "StockTypeIntentService";
    private SyncStockTypeServiceHelper syncStockTypeServiceHelper;

    public StockTypeIntentService() {
        super(TAG);
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public StockTypeIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        syncStockTypeServiceHelper = new SyncStockTypeServiceHelper(getBaseContext());
        syncStockTypeServiceHelper.pullStockTypeFromServer();
    }
}
