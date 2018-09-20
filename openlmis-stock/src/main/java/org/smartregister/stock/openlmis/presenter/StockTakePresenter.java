package org.smartregister.stock.openlmis.presenter;

import org.smartregister.stock.openlmis.interactor.StockListBaseInteractor;
import org.smartregister.stock.openlmis.interactor.StockTakeInteractor;

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
}
