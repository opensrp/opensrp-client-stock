package org.smartregister.stock.openlmis;

import org.smartregister.Context;
import org.smartregister.repository.Repository;
import org.smartregister.stock.openlmis.repository.StockRepository;
import org.smartregister.stock.openlmis.repository.openlmis.CommodityTypeRepository;
import org.smartregister.stock.openlmis.repository.openlmis.DispensableRepository;
import org.smartregister.stock.openlmis.repository.openlmis.LotRepository;
import org.smartregister.stock.openlmis.repository.openlmis.OrderableRepository;
import org.smartregister.stock.openlmis.repository.openlmis.ProgramOrderableRepository;
import org.smartregister.stock.openlmis.repository.openlmis.ProgramRepository;
import org.smartregister.stock.openlmis.repository.openlmis.ReasonRepository;
import org.smartregister.stock.openlmis.repository.openlmis.TradeItemClassificationRepository;
import org.smartregister.stock.openlmis.repository.openlmis.TradeItemRepository;

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
            return new LotRepository(getRepository());
        }
        return lotRepository;
    }

    public TradeItemRepository getTradeItemRepository() {
        if (tradeItemRepository == null)   {
            return new TradeItemRepository(getRepository());
        }
        return tradeItemRepository;
    }

    public org.smartregister.stock.openlmis.repository.TradeItemRepository getTradeItemRegisterRepository() {
        if (tradeItemRegisterRepository == null)   {
            return new org.smartregister.stock.openlmis.repository.TradeItemRepository(getRepository());
        }
        return tradeItemRegisterRepository;
    }

    public ProgramRepository getProgramRepository() {
        if (programRepository == null) {
            return new ProgramRepository(getRepository());
        }
        return programRepository;
    }

    public ProgramOrderableRepository getProgramOrderableRepository() {
        if (programOrderableRepository == null) {
            return new ProgramOrderableRepository(getRepository());
        }
        return programOrderableRepository;
    }

    public ReasonRepository getReasonRepository() {
        if (reasonRepository == null) {
            return new ReasonRepository(getRepository());
        }
        return reasonRepository;
    }

    public CommodityTypeRepository getCommodityTypeRepository() {
        if (commodityTypeRepository == null) {
            return new CommodityTypeRepository(getRepository());
        }
        return commodityTypeRepository;
    }

    public OrderableRepository getOrderableRepository() {
        if (orderableRepository == null) {
           return new OrderableRepository(getRepository());
        }
        return orderableRepository;
    }

    public DispensableRepository getDispensableRepository() {
        if (dispensableRepository == null) {
           return new DispensableRepository(getRepository());
        }
        return dispensableRepository;
    }

    public TradeItemClassificationRepository getTradeItemClassificationRepository() {
        if (tradeItemClassificationRepository == null) {
            return new TradeItemClassificationRepository(getRepository());
        }
        return tradeItemClassificationRepository;
    }

    public StockRepository getStockRepository() {
        if (stockRepository == null) {
            return new StockRepository(getRepository());
        }
        return stockRepository;
    }
}
