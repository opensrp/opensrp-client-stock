package org.smartregister.stock.openlmis.presenter;

import android.view.View;

import org.smartregister.stock.openlmis.domain.Stock;
import org.smartregister.stock.openlmis.domain.TradeItem;
import org.smartregister.stock.openlmis.domain.openlmis.Lot;
import org.smartregister.stock.openlmis.dto.TradeItemDto;
import org.smartregister.stock.openlmis.interactor.StockDetailsInteractor;
import org.smartregister.stock.openlmis.view.contract.StockDetailsView;
import org.smartregister.stock.openlmis.wrapper.StockWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    public List<Stock> getStockByTradeItem(String tradeItemId) {
        List<Stock> stockList = stockDetailsInteractor.getStockByTradeItem(tradeItemId);

        if (!stockList.isEmpty())
            stockDetailsView.showTransactionsHeader();
        return stockList;
    }

    public List<StockWrapper> populateLotNamesAndBalance(TradeItemDto tradeItem, List<Stock> stockTransactions) {
        List<StockWrapper> stockWrapperList = new ArrayList<>();
        Map<String, String> lotName = stockDetailsInteractor.findLotNames(tradeItem.getId());
        int stockCounter = 0;
        for (Stock stock : stockTransactions) {
            stockWrapperList.add(new StockWrapper(stock, lotName.get(stock.getLotId()),
                    tradeItem.getTotalStock() - stockCounter));
            stockCounter += stock.getValue();
        }
        stockWrapperList.get(0).setStockBalance(stockCounter);
        return stockWrapperList;
    }

    public void collapseExpandClicked(int visibility) {
        if (visibility == View.GONE) {
            stockDetailsView.expandLots();
        } else if (visibility == View.VISIBLE) {
            stockDetailsView.collapseLots();
        }
    }
}
