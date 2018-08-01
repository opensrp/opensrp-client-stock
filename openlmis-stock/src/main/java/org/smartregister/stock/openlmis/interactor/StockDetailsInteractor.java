package org.smartregister.stock.openlmis.interactor;

import org.smartregister.stock.openlmis.OpenLMISLibrary;
import org.smartregister.stock.openlmis.domain.TradeItem;
import org.smartregister.stock.openlmis.domain.openlmis.Lot;
import org.smartregister.stock.openlmis.repository.StockRepository;
import org.smartregister.stock.openlmis.repository.TradeItemRepository;
import org.smartregister.stock.openlmis.repository.openlmis.LotRepository;

import java.util.List;
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

    public int getTotalStockByLot(UUID lotId) {
        return stockRepository.getTotalStockByLot(lotId.toString());
    }

    public List<Lot> findLotsByTradeItem(String tradeItemId) {
        return lotRepository.findLotsByTradeItem(tradeItemId);
    }

    public TradeItem findTradeItem(String tradeItemId) {
        return tradeItemRepository.getTradeItemById(tradeItemId);
    }
}
