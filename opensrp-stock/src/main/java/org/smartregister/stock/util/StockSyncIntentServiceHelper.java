package org.smartregister.stock.util;

import org.smartregister.stock.StockLibrary;
import org.smartregister.stock.domain.Stock;
import org.smartregister.stock.repository.StockRepository;

import java.util.List;

public class StockSyncIntentServiceHelper {

    private StockRepository stockRepository;

    public StockSyncIntentServiceHelper() {
    }

    public void batchInsertStocks(List<Stock> stocks) {
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
    }

    public StockRepository getStockRepository() {
        if (stockRepository == null) {
            stockRepository = StockLibrary.getInstance().getStockRepository();
        }
        return stockRepository;
    }
}
