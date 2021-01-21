package org.smartregister.stock.helper;

import org.smartregister.stock.StockLibrary;
import org.smartregister.stock.configuration.StockSyncConfiguration;
import org.smartregister.stock.domain.Stock;
import org.smartregister.stock.repository.StockRepository;

import java.util.List;

public class StockSyncServiceHelper {

    private StockRepository stockRepository;

    private StockSyncConfiguration stockSyncConfiguration;

    public StockSyncServiceHelper(StockSyncConfiguration stockSyncConfiguration) {
        this.stockSyncConfiguration = stockSyncConfiguration;
    }

    public void batchInsertStocks(List<Stock> stocks) {
        if (stockSyncConfiguration.useDefaultStockExistenceCheck()) {
            for (Stock fromServer : stocks) {
                List<Stock> existingStock = getStockRepository().findUniqueStock(fromServer.getStockTypeId(), fromServer.getTransactionType(), fromServer.getProviderid(),
                        String.valueOf(fromServer.getValue()), String.valueOf(fromServer.getDateCreated()), fromServer.getToFrom());
                if (!existingStock.isEmpty()) {
                    for (Stock stock : existingStock) {
                        fromServer.setId(stock.getId());
                    }
                }
                getStockRepository().add(fromServer);
            }
        } else {
            getStockRepository()
                    .batchInsertStock(stocks);
        }
    }

    public StockRepository getStockRepository() {
        if (stockRepository == null) {
            stockRepository = StockLibrary.getInstance().getStockRepository();
        }
        return stockRepository;
    }
}
