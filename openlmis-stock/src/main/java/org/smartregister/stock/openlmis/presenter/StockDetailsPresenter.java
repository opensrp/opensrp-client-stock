package org.smartregister.stock.openlmis.presenter;

import org.smartregister.stock.openlmis.domain.TradeItem;
import org.smartregister.stock.openlmis.domain.openlmis.Lot;
import org.smartregister.stock.openlmis.interactor.StockDetailsInteractor;
import org.smartregister.stock.openlmis.view.contract.StockDetailsView;

import java.util.List;
import java.util.UUID;

public class StockDetailsPresenter {

    private StockDetailsInteractor stockDetailsInteractor;

    private StockDetailsView stockDetailsView;


    public StockDetailsPresenter(StockDetailsView stockDetailsView) {
        this.stockDetailsView = stockDetailsView;
        stockDetailsInteractor = new StockDetailsInteractor();

    }

    public int getTotalStockByLot(UUID lotId) {
        return stockDetailsInteractor.getTotalStockByLot(lotId);
    }

    public List<Lot> findLotsByTradeItem(String tradeItemId) {
        List<Lot> lots = stockDetailsInteractor.findLotsByTradeItem(tradeItemId);
        if (!lots.isEmpty())
            stockDetailsView.showLotsHeader();
        return lots;
    }

    public TradeItem findTradeItem(String tradeItemId) {
        return stockDetailsInteractor.findTradeItem(tradeItemId);
    }
}
