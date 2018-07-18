package org.smartregister.stock.openlmis.presenter;

import android.view.View;

import org.smartregister.stock.openlmis.adapter.ListCommodityTypeAdapter;
import org.smartregister.stock.openlmis.domain.CommodityType;
import org.smartregister.stock.openlmis.domain.TradeItem;
import org.smartregister.stock.openlmis.interactor.StockListInteractor;
import org.smartregister.stock.openlmis.view.contract.StockListView;

import java.util.List;

/**
 * Created by samuelgithengi on 7/13/18.
 */
public class StockListPresenter {

    private StockListInteractor stockListInteractor;

    private StockListView stockListView;

    private ListCommodityTypeAdapter commodityTypeAdapter;

    public StockListPresenter(StockListView stockListView) {
        this.stockListView = stockListView;
        stockListInteractor = new StockListInteractor();
    }

    public void setCommodityTypeAdapter(ListCommodityTypeAdapter commodityTypeAdapter) {
        this.commodityTypeAdapter = commodityTypeAdapter;
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

    public void expandAllClicked() {
        commodityTypeAdapter.expandAllViews();
    }

    public void collapseAllClicked() {
        commodityTypeAdapter.collapseAllViews();
    }

    public List<TradeItem> getTradeItems(CommodityType commodityType) {
        return stockListInteractor.getTradeItems(commodityType);
    }


}
