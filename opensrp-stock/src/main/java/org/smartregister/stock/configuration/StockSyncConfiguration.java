package org.smartregister.stock.configuration;

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
}
