package org.smartregister.stock.openlmis.interactor;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import org.smartregister.CoreLibrary;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.repository.BaseRepository;
import org.smartregister.stock.openlmis.OpenLMISLibrary;
import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.domain.Stock;
import org.smartregister.stock.openlmis.domain.StockTake;
import org.smartregister.stock.openlmis.domain.TradeItem;
import org.smartregister.stock.openlmis.domain.openlmis.CommodityType;
import org.smartregister.stock.openlmis.domain.openlmis.Lot;
import org.smartregister.stock.openlmis.domain.openlmis.Reason;
import org.smartregister.stock.openlmis.repository.SearchRepository;
import org.smartregister.stock.openlmis.repository.StockRepository;
import org.smartregister.stock.openlmis.repository.StockTakeRepository;
import org.smartregister.stock.openlmis.repository.TradeItemRepository;
import org.smartregister.stock.openlmis.repository.openlmis.CommodityTypeRepository;
import org.smartregister.stock.openlmis.repository.openlmis.LotRepository;
import org.smartregister.stock.openlmis.repository.openlmis.ProgramOrderableRepository;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.smartregister.stock.domain.Stock.loss_adjustment;
import static org.smartregister.stock.domain.Stock.stock_take;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.ADJUSTMENT;

/**
 * Created by samuelgithengi on 9/20/18.
 */
public class StockTakeInteractor extends StockListBaseInteractor {

    private static final String TAG = "StockTakeInteractor";

    private LotRepository lotRepository;

    private StockTakeRepository stockTakeRepository;

    private StockRepository stockRepository;

    public StockTakeInteractor() {
        this(OpenLMISLibrary.getInstance().getCommodityTypeRepository(),
                OpenLMISLibrary.getInstance().getProgramOrderableRepository(),
                OpenLMISLibrary.getInstance().getTradeItemRegisterRepository(),
                OpenLMISLibrary.getInstance().getLotRepository(),
                OpenLMISLibrary.getInstance().getStockTakeRepository(),
                OpenLMISLibrary.getInstance().getStockRepository(),
                OpenLMISLibrary.getInstance().getSearchRepository());
    }

    private StockTakeInteractor(CommodityTypeRepository commodityTypeRepository,
                                ProgramOrderableRepository programOrderableRepository,
                                TradeItemRepository tradeItemRepository,
                                LotRepository lotRepository,
                                StockTakeRepository stockTakeRepository,
                                StockRepository stockRepository,
                                SearchRepository searchRepository) {
        super(commodityTypeRepository, programOrderableRepository, tradeItemRepository, searchRepository);
        this.lotRepository = lotRepository;
        this.stockTakeRepository = stockTakeRepository;
        this.stockRepository = stockRepository;
    }

    public List<Lot> findLotsByTradeItem(String tradeItemId) {
        return lotRepository.findLotsByTradeItem(tradeItemId);
    }

    public List<CommodityType> findActiveCommodityTypes(Set<String> commodityTypeIds) {
        List<CommodityType> commodityTypes = commodityTypeRepository.findCommodityTypesWithActiveLotsByIds(commodityTypeIds);
        commodityTypes.addAll(commodityTypeRepository.findActiveCommodityTypesWithoutLotsByIds(commodityTypeIds));
        return commodityTypes;
    }

    public List<TradeItem> findActiveTradeItems(String commodityTypeId) {
        List<TradeItem> tradeItems = tradeItemRepository.findTradeItemsWithActiveLotsByCommodityType(commodityTypeId);
        tradeItems.addAll(tradeItemRepository.findActiveTradeItemsWithoutLotsByCommodityType(commodityTypeId));
        return tradeItems;
    }

    public List<Reason> findAdjustReasons(String programId) {
        return OpenLMISLibrary.getInstance().getReasonRepository().findReasons(null, programId, null, ADJUSTMENT);
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

    public boolean completeStockTake(String programId, Set<String> adjustedTradeItems, String provider, Context context) {
        try {
            Set<StockTake> stockTakeSet = stockTakeRepository.getStockTakeListByTradeItemIds(programId, adjustedTradeItems);
            for (StockTake stockTake : stockTakeSet) {
                Stock stock = new Stock(null, stock_take,
                        provider, stockTake.isNoChange() ? 0 : stockTake.getQuantity(),
                        stockTake.getLastUpdated(),
                        stockTake.isNoChange() ? context.getString(R.string.physical_inventory) : stockTake.getReasonId(),
                        BaseRepository.TYPE_Unsynced,
                        System.currentTimeMillis(), stockTake.getTradeItemId());
                stock.setProgramId(stockTake.getProgramId());
                stock.setLotId(stockTake.getLotId());
                stock.setVvmStatus(stockTake.getStatus());
                stock.setReason(stockTake.getReasonId());
                stock.setOrderableId(getOrderableId(stockTake.getTradeItemId()));
                stock.setFacilityId(OpenLMISLibrary.getInstance().getOpenlmisUuid());


                AllSharedPreferences sharedPreferences = CoreLibrary.getInstance().context().allSharedPreferences();
                stock.setLocationId(sharedPreferences.fetchDefaultLocalityId(sharedPreferences.fetchRegisteredANM()));
                stock.setTeam(sharedPreferences.fetchDefaultTeam(sharedPreferences.fetchRegisteredANM()));
                stock.setTeamId(sharedPreferences.fetchDefaultTeamId(sharedPreferences.fetchRegisteredANM()));

                stockRepository.addOrUpdate(stock);
            }
            return stockTakeRepository.deleteStockTake(programId, adjustedTradeItems) == stockTakeSet.size();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return false;
        }
    }

    public List<TradeItem> findTradeItemsWithActiveLotsByTradeItemIds(Set<String> tradeItemIds) {
        return tradeItemRepository.findTradeItemsWithActiveLotsByTradeItemIds(tradeItemIds);
    }

    public String getOrderableId(String tradeItemId) {
        return OpenLMISLibrary.getInstance().getOrderableRepository().findOrderableIdByTradeItemId(tradeItemId);
    }
}
