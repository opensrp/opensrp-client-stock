package org.smartregister.stock.management.application;

import org.smartregister.Context;
import org.smartregister.CoreLibrary;
import org.smartregister.repository.Repository;
import org.smartregister.stock.openlmis.OpenLMISLibrary;
import org.smartregister.stock.openlmis.domain.openlmis.Program;
import org.smartregister.stock.openlmis.intent.CommodityTypeSyncIntentService;
import org.smartregister.stock.openlmis.intent.DispensableSyncIntentService;
import org.smartregister.stock.openlmis.intent.LotSyncIntentService;
import org.smartregister.stock.openlmis.intent.OpenLMISStockSyncIntentService;
import org.smartregister.stock.openlmis.intent.OrderableSyncIntentService;
import org.smartregister.stock.openlmis.intent.ProgramOrderableSyncIntentService;
import org.smartregister.stock.openlmis.intent.ProgramSyncIntentService;
import org.smartregister.stock.openlmis.intent.ReasonSyncIntentService;
import org.smartregister.stock.openlmis.intent.TradeItemClassificationSyncIntentService;
import org.smartregister.stock.openlmis.intent.TradeItemSyncIntentService;
import org.smartregister.stock.openlmis.repository.StockManagementRepository;
import org.smartregister.view.activity.DrishtiApplication;

import static org.smartregister.util.Log.logError;

public class Application extends DrishtiApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        context = Context.getInstance();
        context.updateApplicationContext(getApplicationContext());
        CoreLibrary.init(context);

        //Initialize OpenLMISLibrary
        OpenLMISLibrary.init(context, getRepository());

        // Initialize sync intent services
        org.smartregister.stock.management.util.ServiceUtils.startService(getInstance().getApplicationContext(), CommodityTypeSyncIntentService.class);
        org.smartregister.stock.management.util.ServiceUtils.startService(getInstance().getApplicationContext(), DispensableSyncIntentService.class);
        org.smartregister.stock.management.util.ServiceUtils.startService(getInstance().getApplicationContext(), LotSyncIntentService.class);
        org.smartregister.stock.management.util.ServiceUtils.startService(getInstance().getApplicationContext(), OrderableSyncIntentService.class);
        org.smartregister.stock.management.util.ServiceUtils.startService(getInstance().getApplicationContext(), ProgramOrderableSyncIntentService.class);
        org.smartregister.stock.management.util.ServiceUtils.startService(getInstance().getApplicationContext(), ReasonSyncIntentService.class);
        org.smartregister.stock.management.util.ServiceUtils.startService(getInstance().getApplicationContext(), TradeItemClassificationSyncIntentService.class);
        org.smartregister.stock.management.util.ServiceUtils.startService(getInstance().getApplicationContext(), TradeItemSyncIntentService.class);
        org.smartregister.stock.management.util.ServiceUtils.startService(getInstance().getApplicationContext(), ProgramSyncIntentService.class);
        org.smartregister.stock.management.util.ServiceUtils.startService(getInstance().getApplicationContext(), OpenLMISStockSyncIntentService.class);
    }


    public static synchronized Application getInstance() {
        return (Application) mInstance;
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


}
