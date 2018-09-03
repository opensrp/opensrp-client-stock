package org.smartregister.stock.management.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.smartregister.stock.management.application.Application;

public class OnBootReceiver extends BroadcastReceiver {

    private final Intent intent = new Intent();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            intent.putExtra(Intent.ACTION_BOOT_COMPLETED, true);
            this.restartAlarms(context);
        }
    }

    private void restartAlarms(Context context) {
        Application.setAlarms(context);
    }
}
