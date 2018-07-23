package org.smartregister.stock.openlmis.presenter;

import org.smartregister.stock.openlmis.domain.TradeItem;
import org.smartregister.stock.openlmis.interactor.StockDetailsInteractor;

public class StockDetailsPresenter {

    private StockDetailsInteractor stockDetailsInteractor;


    public StockDetailsPresenter() {
        stockDetailsInteractor = new StockDetailsInteractor();

    }

}
