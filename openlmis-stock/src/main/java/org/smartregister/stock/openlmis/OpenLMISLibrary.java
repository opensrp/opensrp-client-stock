package org.smartregister.stock.openlmis;

import android.content.SharedPreferences;

import org.smartregister.Context;
import org.smartregister.CoreLibrary;
import org.smartregister.repository.DrishtiRepository;
import org.smartregister.repository.Repository;
import org.smartregister.repository.SettingsRepository;
import org.smartregister.stock.openlmis.repository.SearchRepository;
import org.smartregister.stock.openlmis.repository.StockRepository;
import org.smartregister.stock.openlmis.repository.StockTakeRepository;
import org.smartregister.stock.openlmis.repository.openlmis.CommodityTypeRepository;
import org.smartregister.stock.openlmis.repository.openlmis.DispensableRepository;
import org.smartregister.stock.openlmis.repository.openlmis.LotRepository;
import org.smartregister.stock.openlmis.repository.openlmis.OrderableRepository;
import org.smartregister.stock.openlmis.repository.openlmis.ProgramOrderableRepository;
import org.smartregister.stock.openlmis.repository.openlmis.ProgramRepository;
import org.smartregister.stock.openlmis.repository.openlmis.ReasonRepository;
import org.smartregister.stock.openlmis.repository.openlmis.TradeItemClassificationRepository;
import org.smartregister.stock.openlmis.repository.openlmis.TradeItemRepository;
import org.smartregister.view.activity.DrishtiApplication;
import org.smartregister.stock.openlmis.repository.openlmis.ValidSourceDestinationRepository;

import static org.smartregister.stock.openlmis.util.OpenLMISConstants.FACILITY_TYPE_UUID;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.OPENLMIS_UUID;


/**
 * Created by samuelgithengi on 7/10/18.
 */
public class OpenLMISLibrary {

    private static OpenLMISLibrary instance;
    private Context context;
    private Repository repository;
    private LotRepository lotRepository;
    private TradeItemRepository tradeItemRepository;
    private org.smartregister.stock.openlmis.repository.TradeItemRepository tradeItemRegisterRepository;
    private ProgramRepository programRepository;
    private ProgramOrderableRepository programOrderableRepository;
    private ReasonRepository reasonRepository;
    private CommodityTypeRepository commodityTypeRepository;
    private OrderableRepository orderableRepository;
    private DispensableRepository dispensableRepository;
    private TradeItemClassificationRepository tradeItemClassificationRepository;
    private StockRepository stockRepository;
    private SettingsRepository settingsRepository;
    private SearchRepository searchRepository;
    private DrishtiApplication application;
    private ValidSourceDestinationRepository validSourceDestinationRepository;
    private StockTakeRepository stockTakeRepository;
    private static SharedPreferences preferences;

    public OpenLMISLibrary(Context context, Repository repository) {
        this.context = context;
        this.repository = repository;
    }

    public static void init(Context context, Repository repository) {
        if (instance == null) {
            instance = new OpenLMISLibrary(context, repository);
        }
    }

    public static OpenLMISLibrary getInstance() {
        if (instance == null) {
            throw new IllegalStateException(" Instance does not exist!!! Call " + OpenLMISLibrary.class.getName() + ".init method in the onCreate method of your Application class ");
        }
        return instance;
    }

    public Context getContext() {
        return context;
    }

    public Repository getRepository() {
        return repository;
    }

    public LotRepository getLotRepository() {
        if (lotRepository == null) {
            lotRepository = new LotRepository(getRepository());
        }
        return lotRepository;
    }

    public TradeItemRepository getTradeItemRepository() {
        if (tradeItemRepository == null) {
            tradeItemRepository = new TradeItemRepository(getRepository());
        }
        return tradeItemRepository;
    }

    public org.smartregister.stock.openlmis.repository.TradeItemRepository getTradeItemRegisterRepository() {
        if (tradeItemRegisterRepository == null) {
            tradeItemRegisterRepository = new org.smartregister.stock.openlmis.repository.TradeItemRepository(getRepository());
        }
        return tradeItemRegisterRepository;
    }

    public ProgramRepository getProgramRepository() {
        if (programRepository == null) {
            programRepository = new ProgramRepository(getRepository());
        }
        return programRepository;
    }

    public ProgramOrderableRepository getProgramOrderableRepository() {
        if (programOrderableRepository == null) {
            programOrderableRepository = new ProgramOrderableRepository(getRepository());
        }
        return programOrderableRepository;
    }

    public ReasonRepository getReasonRepository() {
        if (reasonRepository == null) {
            reasonRepository = new ReasonRepository(getRepository());
        }
        return reasonRepository;
    }

    public CommodityTypeRepository getCommodityTypeRepository() {
        if (commodityTypeRepository == null) {
            commodityTypeRepository = new CommodityTypeRepository(getRepository());
        }
        return commodityTypeRepository;
    }

    public OrderableRepository getOrderableRepository() {
        if (orderableRepository == null) {
            orderableRepository = new OrderableRepository(getRepository());
        }
        return orderableRepository;
    }

    public DispensableRepository getDispensableRepository() {
        if (dispensableRepository == null) {
            dispensableRepository = new DispensableRepository(getRepository());
        }
        return dispensableRepository;
    }

    public TradeItemClassificationRepository getTradeItemClassificationRepository() {
        if (tradeItemClassificationRepository == null) {
            tradeItemClassificationRepository = new TradeItemClassificationRepository(getRepository());
        }
        return tradeItemClassificationRepository;
    }

    public StockRepository getStockRepository() {
        if (stockRepository == null) {
            stockRepository = new StockRepository(getRepository());
        }
        return stockRepository;
    }

    public ValidSourceDestinationRepository getValidSourceDestinationRepository() {
        if (validSourceDestinationRepository == null) {
            validSourceDestinationRepository = new ValidSourceDestinationRepository(getRepository());
        }
        return validSourceDestinationRepository;
    }

    public SettingsRepository getSettingsRepository() {
        if (settingsRepository == null) {
            for (DrishtiRepository repository : getContext().sharedRepositories()) {
                if (repository instanceof SettingsRepository) {
                    settingsRepository = (SettingsRepository) repository;
                    return settingsRepository;
                }
            }
        }
        return settingsRepository;
    }

    public SearchRepository getSearchRepository() {
        if (searchRepository == null) {
            searchRepository = new SearchRepository(getRepository());
        }
        return searchRepository;
    }


    public DrishtiApplication getApplication() {
        return application;
    }

    public void setApplication(DrishtiApplication application) {
        this.application = application;
    }

    public String getFacilityTypeUuid() {
        if (preferences == null) {
            preferences = CoreLibrary.getInstance().context().allSharedPreferences().getPreferences();
        }
        return preferences.getString(FACILITY_TYPE_UUID, null);
    }

    public String getOpenlmisUuid() {
        if (preferences == null) {
            preferences = CoreLibrary.getInstance().context().allSharedPreferences().getPreferences();
        }
        return preferences.getString(OPENLMIS_UUID , null);
    }

    public void setFacilityTypeUuid(String facilityTypeUuid) {
        if (preferences == null) {
            preferences = CoreLibrary.getInstance().context().allSharedPreferences().getPreferences();
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(FACILITY_TYPE_UUID, facilityTypeUuid);
        editor.apply();
    }

    public void setOpenlmisUuid(String openlmisUuid) {
        if (preferences == null) {
            preferences = CoreLibrary.getInstance().context().allSharedPreferences().getPreferences();
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(OPENLMIS_UUID, openlmisUuid);
        editor.apply();
    }

    public StockTakeRepository getStockTakeRepository() {
        if (stockTakeRepository == null) {
            stockTakeRepository = new StockTakeRepository(getRepository());
        }
        return stockTakeRepository;
    }
}
