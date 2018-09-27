package org.smartregister.stock.openlmis.interactor;

import android.util.Pair;

import org.smartregister.stock.openlmis.OpenLMISLibrary;
import org.smartregister.stock.openlmis.domain.StockTake;
import org.smartregister.stock.openlmis.domain.TradeItem;
import org.smartregister.stock.openlmis.domain.openlmis.CommodityType;
import org.smartregister.stock.openlmis.domain.openlmis.Lot;
import org.smartregister.stock.openlmis.domain.openlmis.Reason;
import org.smartregister.stock.openlmis.domain.openlmis.StockCardLineItemReason;
import org.smartregister.stock.openlmis.repository.StockRepository;
import org.smartregister.stock.openlmis.repository.StockTakeRepository;
import org.smartregister.stock.openlmis.repository.TradeItemRepository;
import org.smartregister.stock.openlmis.repository.openlmis.CommodityTypeRepository;
import org.smartregister.stock.openlmis.repository.openlmis.LotRepository;
import org.smartregister.stock.openlmis.repository.openlmis.ProgramOrderableRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Created by samuelgithengi on 9/20/18.
 */
public class StockTakeInteractor extends StockListBaseInteractor {

    private LotRepository lotRepository;

    private StockTakeRepository stockTakeRepository;

    private StockRepository stockRepository;

    public StockTakeInteractor() {
        this(OpenLMISLibrary.getInstance().getCommodityTypeRepository(),
                OpenLMISLibrary.getInstance().getProgramOrderableRepository(),
                OpenLMISLibrary.getInstance().getTradeItemRegisterRepository(),
                OpenLMISLibrary.getInstance().getLotRepository(),
                OpenLMISLibrary.getInstance().getStockTakeRepository(),
                OpenLMISLibrary.getInstance().getStockRepository());
    }

    private StockTakeInteractor(CommodityTypeRepository commodityTypeRepository,
                                ProgramOrderableRepository programOrderableRepository,
                                TradeItemRepository tradeItemRepository,
                                LotRepository lotRepository,
                                StockTakeRepository stockTakeRepository,
                                StockRepository stockRepository) {
        super(commodityTypeRepository, programOrderableRepository, tradeItemRepository);
        this.lotRepository = lotRepository;
        this.stockTakeRepository = stockTakeRepository;
        this.stockRepository = stockRepository;
    }

    public List<Lot> findLotsByTradeItem(String tradeItemId) {
        return lotRepository.findLotsByTradeItem(tradeItemId);
    }

    public List<CommodityType> findCommodityTypesWithActiveLots(Set<String> commodityTypeIds) {
        return commodityTypeRepository.findCommodityTypesWithActiveLotsByIds(commodityTypeIds);
    }

    public List<TradeItem> findTradeItemsActiveLots(String commodityTypeId) {
        return tradeItemRepository.findTradeItemsWithActiveLotsByCommodityType(commodityTypeId);
    }

    public List<Reason> findAdjustReasons(String programId) {
        List<Reason> reasons = new ArrayList<>();
        reasons.add(new Reason(UUID.randomUUID().toString(), programId, "", new StockCardLineItemReason("Transfer In", "Trasfer", "CREDIT", "Adjust", true)));
        reasons.add(new Reason(UUID.randomUUID().toString(), programId, "", new StockCardLineItemReason("Damaged", "Damaged", "DEBIT", "Adjust", true)));
        reasons.add(new Reason(UUID.randomUUID().toString(), programId, "", new StockCardLineItemReason("Expired", "Expired", "DEBIT", "Adjust", true)));
        return reasons;
    }

    public Set<StockTake> findStockTakeList(String programId, String tradeItemId) {
        return stockTakeRepository.getStockTakeList(programId, tradeItemId);
    }

    public boolean saveStockTake(Set<StockTake> stockTakeSet) {
        for (StockTake stockTake : stockTakeSet)
            stockTakeRepository.addOrUpdate(stockTake);
        return true;
    }

    public Map<String, Integer> findStockBalanceByTradeItemIds(String programId, List<String> tradeItemIds) {
        return stockRepository.findStockByTradeItemIds(programId, tradeItemIds);
    }

    public Map<String, Integer> findStockBalanceByLotsIds(String programId, List<String> lotIds) {
        return stockRepository.findStockByLotIds(programId, lotIds);
    }

    public int findNumberOfTradeItems(Set<String> commodityTypeIds) {
        return tradeItemRepository.findNumberOfTradeItems(commodityTypeIds);
    }

    public Pair<Set<String>, Long> findTradeItemsIdsAdjusted(String programId, Set<String> commodityTypeIds) {
        return stockTakeRepository.findTradeItemsIdsAdjusted(programId, commodityTypeIds);
    }
}
