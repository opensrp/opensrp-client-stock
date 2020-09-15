package org.smartregister.stock.openlmis.interactor;


import androidx.annotation.VisibleForTesting;

import org.smartregister.stock.openlmis.OpenLMISLibrary;
import org.smartregister.stock.openlmis.domain.Stock;
import org.smartregister.stock.openlmis.domain.TradeItem;
import org.smartregister.stock.openlmis.domain.openlmis.Lot;
import org.smartregister.stock.openlmis.domain.openlmis.Reason;
import org.smartregister.stock.openlmis.domain.openlmis.ValidSourceDestination;
import org.smartregister.stock.openlmis.repository.StockRepository;
import org.smartregister.stock.openlmis.repository.TradeItemRepository;
import org.smartregister.stock.openlmis.repository.openlmis.LotRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StockDetailsInteractor {

    private StockRepository stockRepository;

    private LotRepository lotRepository;

    private TradeItemRepository tradeItemRepository;

    public StockDetailsInteractor() {
        this(new StockRepository(OpenLMISLibrary.getInstance().getRepository()), new LotRepository(OpenLMISLibrary.getInstance().getRepository()), new TradeItemRepository(OpenLMISLibrary.getInstance().getRepository()));
    }

    @VisibleForTesting
    protected StockDetailsInteractor(StockRepository stockRepository, LotRepository lotRepository, TradeItemRepository tradeItemRepository) {
        this.stockRepository = stockRepository;
        this.lotRepository = lotRepository;
        this.tradeItemRepository = tradeItemRepository;
    }

    public int getTotalStockByLot(String lotId) {
        return stockRepository.getTotalStockByLot(lotId);
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

    public void updateLotStatus(String lotId, String lotStatus) {
        lotRepository.updateLotStatus(lotId, lotStatus);
    }

    public Map<String, String> findReasonNames(String programId) {
        Map<String, String> reasonNames = new HashMap<>();
        List<Reason> reasons = OpenLMISLibrary.getInstance().getReasonRepository().findReasons(null, programId, null, null);
        for (Reason reason : reasons)
            reasonNames.put(reason.getId(), reason.getStockCardLineItemReason().getName());
        return reasonNames;
    }

    public Map<String, String> findFacilityNames(String programId) {
        Map<String, String> facilityNames = new HashMap<>();
        List<ValidSourceDestination> facilities = OpenLMISLibrary.getInstance().getValidSourceDestinationRepository().findValidSourceDestinations(null, programId, OpenLMISLibrary.getInstance().getFacilityTypeUuid(), null, null, null);
        for (ValidSourceDestination facility : facilities)
            facilityNames.put(facility.getOpenlmisUuid(), facility.getFacilityName());
        return facilityNames;
    }

    public String getOrderableId(String tradeItemId) {
        return OpenLMISLibrary.getInstance().getOrderableRepository().findOrderableIdByTradeItemId(tradeItemId);
    }

    public Reason findReasonById(String reasonId) {
        return OpenLMISLibrary.getInstance().getReasonRepository().findReasonById(reasonId);
    }
}
