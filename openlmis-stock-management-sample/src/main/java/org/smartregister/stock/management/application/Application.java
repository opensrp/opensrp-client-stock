package org.smartregister.stock.management.application;

import org.smartregister.Context;
import org.smartregister.CoreLibrary;
import org.smartregister.repository.Repository;
import org.smartregister.stock.openlmis.OpenLMISLibrary;
import org.smartregister.stock.openlmis.repository.StockManagementRepository;
import org.smartregister.view.activity.DrishtiApplication;

import static org.smartregister.util.Log.logError;

public class Application extends DrishtiApplication {

    private static android.content.Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        context = Context.getInstance();
        context.updateApplicationContext(getApplicationContext());
        CoreLibrary.init(context);

        //Initialize OpenLMISLibrary
        OpenLMISLibrary.init(context, getRepository());
    }


    public static synchronized Application getInstance() {
        return (Application) mInstance;
    }

    @Override
    public Repository getRepository() {
        try {
            if (repository == null) {
                repository = new StockManagementRepository(getAppContext(), context);
            }
        } catch (UnsatisfiedLinkError e) {
            logError("Error on getRepository: " + e);
        }
        return repository;
    }

    @Override
    public void logoutCurrentUser() {

    }

    public android.content.Context getAppContext() {
        if (applicationContext == null) {
            applicationContext = getInstance().getApplicationContext();
        }
        return applicationContext;
    }

    public static void setAppContext(android.content.Context context) {
        applicationContext = context;
    }
}
