package org.smartregister.stock;

import org.smartregister.Context;
import org.smartregister.repository.Repository;
import org.smartregister.stock.configuration.StockSyncConfiguration;
import org.smartregister.stock.repository.StockExternalRepository;
import org.smartregister.stock.repository.StockRepository;
import org.smartregister.stock.repository.StockTypeRepository;
import org.smartregister.util.AppExecutors;

/**
 * Created by ndegwamartin on 05/02/2018.
 */

public class StockLibrary {
    private static StockLibrary instance;
    private static Context context;
    private Repository repository;
    private StockRepository stockRepository;
    private StockTypeRepository stockTypeRepository;
    private StockExternalRepository stockExternalRepository;
    private AppExecutors appExecutors;
    private StockSyncConfiguration stockSyncConfiguration;

    public static void init(Context context_, Repository repository) {
        init(context_, repository, null);
    }

    public static void init(Context context_, Repository repository, StockExternalRepository stockExternalRepository) {
        init(context_, repository, stockExternalRepository, null, new StockSyncConfiguration());
    }

    public static void init(Context context_, Repository repository, StockExternalRepository stockExternalRepository,
                            AppExecutors appExecutors, StockSyncConfiguration stockSyncConfiguration) {
        if (instance == null) {
            context = context_;
            instance = new StockLibrary(context, repository, stockExternalRepository, appExecutors, stockSyncConfiguration);
        }
    }

    public static StockLibrary getInstance() {
        if (instance == null) {
            throw new IllegalStateException(" Instance does not exist!!! Call " + StockLibrary.class.getName() + ".init method in the onCreate method of your Application class ");
        }
        return instance;
    }

    private StockLibrary(Context context, Repository repository, StockExternalRepository stockExternalRepository, AppExecutors appExecutors, StockSyncConfiguration stockSyncConfiguration) {
        this.context = context;
        this.repository = repository;
        this.stockExternalRepository = stockExternalRepository;
        this.appExecutors = appExecutors == null ? getAppExecutors() : appExecutors;
        this.stockSyncConfiguration = stockSyncConfiguration;
    }

    public Context getContext() {
        return context;
    }

    public Repository getRepository() {
        return repository;
    }

    public StockRepository getStockRepository() {
        if (stockRepository == null) {
            stockRepository = new StockRepository();
        }
        return stockRepository;
    }

    public StockTypeRepository getStockTypeRepository() {
        if (stockTypeRepository == null) {
            stockTypeRepository = new StockTypeRepository();
        }
        return stockTypeRepository;
    }

    public StockExternalRepository getStockExternalRepository() {
        return stockExternalRepository;
    }

    public AppExecutors getAppExecutors() {
        if (appExecutors == null) {
            return appExecutors = new AppExecutors();
        }
        return appExecutors;
    }

    public StockSyncConfiguration getStockSyncConfiguration() {
        if (stockSyncConfiguration == null) {
            stockSyncConfiguration = new StockSyncConfiguration();
        }
        return stockSyncConfiguration;
    }
}
