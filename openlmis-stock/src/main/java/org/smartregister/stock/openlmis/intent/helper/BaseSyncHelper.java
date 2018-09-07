package org.smartregister.stock.openlmis.intent.helper;

import android.content.SharedPreferences;

public interface BaseSyncHelper {
    void processIntent();
    void saveResponse(String response, SharedPreferences preferences);
}
