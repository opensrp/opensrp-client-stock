package org.smartregister.stock.openlmis.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import org.smartregister.stock.openlmis.OpenLMISLibrary;

import java.util.List;

public class ServiceUtils {

    public static boolean isServiceRunning(Class<?> serviceClass) {
        final ActivityManager activityManager = (ActivityManager) OpenLMISLibrary.getInstance().getApplication().getInstance().getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);

        for (ActivityManager.RunningServiceInfo service : services) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static void startService(Context context, Class<?> serviceClass) {
        if (context == null || serviceClass == null) {
            return;
        }
        if (!isServiceRunning(serviceClass)) {
            Intent intent = new Intent(context, serviceClass);
            OpenLMISLibrary.getInstance().getApplication().getInstance().startService(intent);
        }
    }

    public static void stopService(Context context, Class<?> serviceClass) {

        if (context == null || serviceClass == null) {
            return;
        }
        if (isServiceRunning(serviceClass)) {
            Intent intent = new Intent(context, serviceClass);
            OpenLMISLibrary.getInstance().getApplication().stopService(intent);
        }
    }
}
