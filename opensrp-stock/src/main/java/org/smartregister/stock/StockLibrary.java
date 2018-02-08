package org.smartregister.stock;

import org.smartregister.Context;
import org.smartregister.commonregistry.CommonFtsObject;
import org.smartregister.repository.Repository;
import org.smartregister.stock.repository.StockRepository;
import org.smartregister.stock.repository.StockTypeRepository;

/**
 * Created by ndegwamartin on 05/02/2018.
 */

public class StockLibrary {
    private static StockLibrary instance;
    private static Context context;
    private static CommonFtsObject commonFtsObject;
    private Repository repository;
    private StockRepository stockRepository;
    private StockTypeRepository stockTypeRepository;

    public static void init(Context context_, Repository repository) {
        if (instance == null) {
            context = context_;
            instance = new StockLibrary(context, repository);
        }
    }

    public static StockLibrary getInstance() {
        if (instance == null) {
            throw new IllegalStateException(" Instance does not exist!!! Call " + StockLibrary.class.getName() + ".init method in the onCreate method of your Application class ");
        }
        return instance;
    }

    private StockLibrary(Context context, Repository repository) {
        this.context = context;
        this.repository = repository;
    }

    public Context getContext() {
        return context;
    }

    public Repository getRepository() {
        return repository;
    }

    public StockRepository getStockRepository() {
        if (stockRepository == null) {
            stockRepository = new StockRepository(getRepository());
        }
        return stockRepository;
    }

    public StockTypeRepository getStockTypeRepository() {
        if (stockTypeRepository == null) {
            stockTypeRepository = new StockTypeRepository(getRepository());
        }
        return stockTypeRepository;
    }

}
