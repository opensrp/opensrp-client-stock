package org.smartregister.stock.openlmis.presenter;

import org.smartregister.stock.openlmis.domain.StockTake;
import org.smartregister.stock.openlmis.domain.TradeItem;
import org.smartregister.stock.openlmis.domain.openlmis.CommodityType;
import org.smartregister.stock.openlmis.domain.openlmis.Lot;
import org.smartregister.stock.openlmis.domain.openlmis.Reason;
import org.smartregister.stock.openlmis.interactor.StockListBaseInteractor;
import org.smartregister.stock.openlmis.interactor.StockTakeInteractor;

import java.util.List;
import java.util.Set;

/**
 * Created by samuelgithengi on 9/20/18.
 */
public class StockTakePresenter extends StockListBasePresenter {

    private StockTakeInteractor stockTakeInteractor;

    public StockTakePresenter() {
        stockTakeInteractor = new StockTakeInteractor();
    }

    @Override
    protected StockListBaseInteractor getStockListInteractor() {
        return stockTakeInteractor;
    }

    public List<Lot> findLotsByTradeItem(String tradeItemId) {
        return stockTakeInteractor.findLotsByTradeItem(tradeItemId);
    }


    public List<CommodityType> findCommodityTypesWithActiveLots(Set<String> commodityTypeIds) {
        return stockTakeInteractor.findCommodityTypesWithActiveLots(commodityTypeIds);
    }

    public List<TradeItem> findTradeItemsWithActiveLots(String commodityTypeId) {
        return stockTakeInteractor.findTradeItemsActiveLots(commodityTypeId);
    }

    public List<Reason> findAdjustReasons(String programId) {
        return stockTakeInteractor.findAdjustReasons(programId);
    }

    public List<StockTake> findStockTakeList(String programId, String tradeItemId) {
        return stockTakeInteractor.findStockTakeList(programId, tradeItemId);
    }

    public boolean saveStockTake(Set<StockTake> stockTakeSet) {
        return stockTakeInteractor.saveStockTake(stockTakeSet);
    }
}
