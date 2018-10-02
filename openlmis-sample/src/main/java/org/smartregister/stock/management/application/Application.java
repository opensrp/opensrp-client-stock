package org.smartregister.stock.management.application;

import org.smartregister.Context;
import org.smartregister.CoreLibrary;
import org.smartregister.repository.Repository;
import org.smartregister.stock.management.BuildConfig;
import org.smartregister.stock.management.receiver.OpenLMISAlarmReceiver;
import org.smartregister.stock.openlmis.OpenLMISLibrary;
import org.smartregister.stock.openlmis.receiver.SyncStatusBroadcastReceiver;
import org.smartregister.stock.openlmis.repository.StockManagementRepository;
import org.smartregister.view.activity.DrishtiApplication;

import static org.smartregister.stock.openlmis.util.OpenLMISConstants.ServiceType.SYNC_OPENLMIS_METADATA;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.ServiceType.SYNC_STOCK;
import static org.smartregister.util.Log.logError;

public class Application extends DrishtiApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        context = Context.getInstance();
        context.updateApplicationContext(this);
        CoreLibrary.init(context);
        OpenLMISLibrary.init(context, getRepository());  // Initialize OpenLMISLibrary
        setAlarms(getApplicationContext());
        SyncStatusBroadcastReceiver.init(this);
    }


    public static synchronized Application getInstance() {
        return (Application) mInstance;
    }

    public Context getContext() {
        return context;
    }

    @Override
    public Repository getRepository() {
        try {
            if (repository == null) {
                repository = new StockManagementRepository(getApplicationContext(), context);
            }
        } catch (UnsatisfiedLinkError e) {
            logError("Error on getRepository: " + e);
        }
        return repository;
    }

    @Override
    public void logoutCurrentUser() {

    }

    @Override
    public void onTerminate() {
        SyncStatusBroadcastReceiver.destroy(this);
        super.onTerminate();
    }

    public static void setAlarms(android.content.Context context) {
        OpenLMISAlarmReceiver.setAlarm(context, BuildConfig.OPENLMIS_METADATA_SYNC_INTERVAL_MIN, SYNC_OPENLMIS_METADATA);
        OpenLMISAlarmReceiver.setAlarm(context, BuildConfig.STOCK_SYNC_INTERVAL_MIN, SYNC_STOCK);
    }
}
