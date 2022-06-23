package org.smartregister.stock.openlmis.intent.helper;

import android.content.Context;
import android.content.SharedPreferences;

import static org.smartregister.stock.openlmis.util.Utils.sendSyncCompleteBroadCast;

import org.smartregister.CoreLibrary;

public abstract class BaseSyncHelper {

    protected Context context;

    protected abstract String pullFromServer(String url);

    protected abstract boolean saveResponse(String response, SharedPreferences preferences);

    public void processIntent(String url) {
        String response = pullFromServer(url);
        if (response == null) {
            return;
        }
        boolean isEmptyResponse = saveResponse(response, CoreLibrary.getInstance().context().allSharedPreferences().getPreferences());
        if (!isEmptyResponse) {
            sendSyncCompleteBroadCast(context);
        }
    }
}
