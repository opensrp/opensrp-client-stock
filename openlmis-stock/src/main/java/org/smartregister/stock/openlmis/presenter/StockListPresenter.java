package org.smartregister.stock.openlmis.presenter;

import android.view.View;

import org.smartregister.stock.openlmis.domain.CommodityType;
import org.smartregister.stock.openlmis.interactor.StockListInteractor;
import org.smartregister.stock.openlmis.view.contract.StockListView;

import java.util.List;

/**
 * Created by samuelgithengi on 7/13/18.
 */
public class StockListPresenter {

    private StockListInteractor stockListInteractor;

    private StockListView stockListView;

    public StockListPresenter(StockListView stockListView) {
        this.stockListView = stockListView;
        stockListInteractor = new StockListInteractor();
    }

    public List<String> getPrograms() {
        return stockListInteractor.getPrograms();
    }

    public List<CommodityType> getCommodityTypes() {
        return stockListInteractor.getCommodityTypes();
    }

    public void stockActionClicked(View view) {
        stockListView.showStockActionMenu(view);
    }
}
