package org.smartregister.stock.openlmis.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.smartregister.stock.openlmis.OpenLMISLibrary;

public class OnBootReceiver extends BroadcastReceiver {

    private final Intent intent = new Intent();
    private final String TAG = OnBootReceiver.class.getCanonicalName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Log.d(TAG, "Boot completed intent received in OpenLMIS onReceive");
            intent.putExtra(Intent.ACTION_BOOT_COMPLETED, true);
            this.restartAlarms(context);
        }
    }

    private void restartAlarms(Context context) {
        OpenLMISLibrary.getInstance().getApplication().onCreate();
        Log.d(TAG, "Alarms restarted in OpenLMIS");
    }
}
