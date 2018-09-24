package org.smartregister.stock.openlmis.presenter;

import org.smartregister.stock.openlmis.domain.openlmis.CommodityType;
import org.smartregister.stock.openlmis.domain.openlmis.Lot;
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


    public List<CommodityType> findCommodityTypesWithActiveLotsByIds(Set<String> commodityTypeIds) {
        return stockTakeInteractor.findCommodityTypesWithActiveLotsByIds(commodityTypeIds);
    }

}
