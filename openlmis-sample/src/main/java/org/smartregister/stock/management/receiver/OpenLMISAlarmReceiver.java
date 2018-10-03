package org.smartregister.stock.management.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.smartregister.stock.openlmis.OpenLMISLibrary;
import org.smartregister.stock.openlmis.intent.service.CommodityTypeSyncIntentService;
import org.smartregister.stock.openlmis.intent.service.DispensableSyncIntentService;
import org.smartregister.stock.openlmis.intent.service.LotSyncIntentService;
import org.smartregister.stock.openlmis.intent.service.OpenLMISStockSyncIntentService;
import org.smartregister.stock.openlmis.intent.service.OrderableSyncIntentService;
import org.smartregister.stock.openlmis.intent.service.ProgramOrderableSyncIntentService;
import org.smartregister.stock.openlmis.intent.service.ProgramSyncIntentService;
import org.smartregister.stock.openlmis.intent.service.ReasonSyncIntentService;
import org.smartregister.stock.openlmis.intent.service.TradeItemClassificationSyncIntentService;
import org.smartregister.stock.openlmis.intent.service.TradeItemSyncIntentService;
import org.smartregister.stock.openlmis.intent.service.ValidSourceDestinationSyncIntentService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.smartregister.stock.management.util.ServiceUtils.startService;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.FACILITY_TYPE_UUID;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.OPENLMIS_UUID;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.PROGRAM_ID;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.ServiceType.SYNC_OPENLMIS_METADATA;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.ServiceType.SYNC_STOCK;

public class OpenLMISAlarmReceiver extends BroadcastReceiver {

    private static final String TAG = OpenLMISAlarmReceiver.class.getCanonicalName();
    private static final String SERVICE_ACTION_NAME = "org.smartregister.path.action.START_SERVICE_ACTION";
    private static final String SERVICE_TYPE_NAME = "serviceType";
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void onReceive(Context context, Intent alarmIntent) {
        int serviceType = alarmIntent.getIntExtra(SERVICE_TYPE_NAME, 0);
        Intent serviceIntent = null;
        switch (serviceType) {
            case SYNC_OPENLMIS_METADATA:
                startService(context, CommodityTypeSyncIntentService.class);
                startService(context, DispensableSyncIntentService.class);
                startService(context, LotSyncIntentService.class);
                startService(context, OrderableSyncIntentService.class);
                startService(context, ProgramOrderableSyncIntentService.class);
                startService(context, ProgramSyncIntentService.class);
                startService(context, TradeItemClassificationSyncIntentService.class);
                startService(context, TradeItemSyncIntentService.class);
                startService(context, ReasonSyncIntentService.class);
                startService(context, ProgramSyncIntentService.class);
                startService(context, ValidSourceDestinationSyncIntentService.class);

                Log.i(TAG, "Started OpenLMIS metadata sync service at: " + DATE_FORMATTER.format(new Date()));
                break;
            case SYNC_STOCK:
                startService(context, OpenLMISStockSyncIntentService.class);
                Log.i(TAG, "Started Stock sync service at: " + DATE_FORMATTER.format(new Date()));
                break;
            default:
                break;
        }
        if (serviceIntent != null) {
            context.startService(serviceIntent);
        }
    }

    /**
     * @param context
     * @param triggerIteration in minutes
     * @param taskType a constant from OpenLMISConstants denoting the service type
     */
    public static void setAlarm(Context context, long triggerIteration, int taskType) {
        try {
            AlarmManager alarmManager;
            PendingIntent alarmIntent;

            long triggerAt;
            long triggerInterval;
            if (context == null) {
                throw new Exception("Unable to schedule service without app context");
            }

            // Otherwise schedule based on normal interval
            triggerInterval = TimeUnit.MINUTES.toMillis(triggerIteration);
            // set trigger time to be current device time + the interval (frequency). Probably randomize this a bit so that services not launch at exactly the same time
            triggerAt = System.currentTimeMillis() + triggerInterval;

            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent alarmReceiverIntent = new Intent(context, OpenLMISAlarmReceiver.class);

            alarmReceiverIntent.setAction(SERVICE_ACTION_NAME + taskType);
            alarmReceiverIntent.putExtra(SERVICE_TYPE_NAME, taskType);
            alarmIntent = PendingIntent.getBroadcast(context, 0, alarmReceiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            try {
                alarmManager.cancel(alarmIntent);
            } catch (Exception e) {
                org.smartregister.util.Log.logError(TAG, e.getMessage());
            }
            //Elapsed real time uses the "time since system boot" as a reference, and real time clock uses UTC (wall clock) time
            alarmManager.setRepeating(AlarmManager.RTC, triggerAt, triggerInterval, alarmIntent);
        } catch (Exception e) {
            org.smartregister.util.Log.logError(TAG, "Error in setting service Alarm " + e.getMessage());
        }
    }
}
