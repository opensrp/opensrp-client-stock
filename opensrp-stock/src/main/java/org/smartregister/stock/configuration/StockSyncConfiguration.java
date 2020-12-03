package org.smartregister.stock.configuration;

import org.smartregister.stock.helper.StockSyncServiceHelper;
import org.smartregister.util.Utils;

public class StockSyncConfiguration {

    public String getStockSyncParams() {
        return String.format("&providerid=%s", Utils.getAllSharedPreferences().fetchRegisteredANM());
    }

    public boolean canPushStockToServer() {
        return true;
    }

    public boolean hasActions() {
        return true;
    }

    public boolean shouldFetchStockTypeImages() {
        return false;
    }

    public StockSyncServiceHelper getStockSyncIntentServiceHelper(){
        return new StockSyncServiceHelper(this);
    }

    public boolean useDefaultStockExistenceCheck() {
        return true;
    }
}
