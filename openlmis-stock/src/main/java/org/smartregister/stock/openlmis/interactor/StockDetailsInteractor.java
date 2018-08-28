package org.smartregister.stock.openlmis.interactor;


import android.support.annotation.VisibleForTesting;

import org.smartregister.stock.openlmis.OpenLMISLibrary;
import org.smartregister.stock.openlmis.domain.Stock;
import org.smartregister.stock.openlmis.domain.TradeItem;
import org.smartregister.stock.openlmis.domain.openlmis.Lot;
import org.smartregister.stock.openlmis.repository.StockRepository;
import org.smartregister.stock.openlmis.repository.TradeItemRepository;
import org.smartregister.stock.openlmis.repository.openlmis.LotRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class StockDetailsInteractor {

    private StockRepository stockRepository;

    private LotRepository lotRepository;

    private TradeItemRepository tradeItemRepository;

    public StockDetailsInteractor() {
        stockRepository = new StockRepository(OpenLMISLibrary.getInstance().getRepository());
        lotRepository = new LotRepository(OpenLMISLibrary.getInstance().getRepository());
        tradeItemRepository = new TradeItemRepository(OpenLMISLibrary.getInstance().getRepository());
    }

    @VisibleForTesting
    protected StockDetailsInteractor(StockRepository stockRepository, LotRepository lotRepository, TradeItemRepository tradeItemRepository) {
        this.stockRepository = stockRepository;
        this.lotRepository = lotRepository;
        this.tradeItemRepository = tradeItemRepository;
    }

    public int getTotalStockByLot(UUID lotId) {
        return stockRepository.getTotalStockByLot(lotId.toString());
    }

    public List<Lot> findLotsByTradeItem(String tradeItemId) {
        return lotRepository.findLotsByTradeItem(tradeItemId);
    }

    public TradeItem findTradeItem(String tradeItemId) {
        return tradeItemRepository.getTradeItemById(tradeItemId);
    }

    public List<Stock> getStockByTradeItem(String tradeItemId) {
        return stockRepository.getStockByTradeItem(tradeItemId);
    }

    public Map<String, String> findLotNames(String tradeItemId) {
        return lotRepository.findLotNames(tradeItemId);
    }

    public void addStock(Stock stock) {
        stockRepository.addOrUpdate(stock);
    }
}
