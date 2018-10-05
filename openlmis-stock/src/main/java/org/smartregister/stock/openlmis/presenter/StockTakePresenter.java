package org.smartregister.stock.openlmis.presenter;

import android.util.Pair;

import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.domain.StockTake;
import org.smartregister.stock.openlmis.domain.TradeItem;
import org.smartregister.stock.openlmis.domain.openlmis.CommodityType;
import org.smartregister.stock.openlmis.domain.openlmis.Lot;
import org.smartregister.stock.openlmis.domain.openlmis.Reason;
import org.smartregister.stock.openlmis.interactor.StockListBaseInteractor;
import org.smartregister.stock.openlmis.interactor.StockTakeInteractor;
import org.smartregister.stock.openlmis.util.AppExecutors;
import org.smartregister.stock.openlmis.view.contract.StockTakeView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by samuelgithengi on 9/20/18.
 */
public class StockTakePresenter extends StockListBasePresenter {

    private StockTakeInteractor stockTakeInteractor;

    private StockTakeView stockTakeView;

    private Set<String> adjustedTradeItems;

    private List<Reason> adjustReasons;

    private int totalTradeItems;

    private String programId;

    private Map<String, Set<StockTake>> stockTakeMap = new HashMap<>();

    private AppExecutors appExecutors;

    public StockTakePresenter(StockTakeView stockTakeView) {
        this.stockTakeView = stockTakeView;
        stockTakeInteractor = new StockTakeInteractor();
        appExecutors = new AppExecutors();
    }

    @Override
    protected StockListBaseInteractor getStockListInteractor() {
        return stockTakeInteractor;
    }


    public void iniatializePresenter(String programId, Set<String> commodityTypeIds) {
        this.programId = programId;
        totalTradeItems = stockTakeInteractor.findNumberOfTradeItems(commodityTypeIds);
        Pair<Set<String>, Long> tradeItemsAdjusted = stockTakeInteractor.findTradeItemsIdsAdjusted(programId, commodityTypeIds);
        adjustedTradeItems = tradeItemsAdjusted.first;
        adjustReasons = stockTakeInteractor.findAdjustReasons(programId);
        stockTakeView.updateTotalTradeItems(totalTradeItems);
        stockTakeView.updateTradeItemsAdjusted(adjustedTradeItems.size(),
                tradeItemsAdjusted.second == null || tradeItemsAdjusted.second == 0 ? null : new Date(tradeItemsAdjusted.second));
        enableSubmit();
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

    public List<Reason> findAdjustReasons() {
        return adjustReasons;
    }

    public Set<StockTake> findStockTakeList(String programId, String tradeItemId) {
        stockTakeMap.put(tradeItemId, getStockTakeList(programId, tradeItemId));
        return stockTakeMap.get(tradeItemId);
    }

    public Set<StockTake> getStockTakeList(String programId, String tradeItemId) {
        if (stockTakeMap.containsKey(tradeItemId))
            stockTakeMap.get(tradeItemId).addAll(stockTakeInteractor.findStockTakeList(programId, tradeItemId));
        else
            stockTakeMap.put(tradeItemId, stockTakeInteractor.findStockTakeList(programId, tradeItemId));
        return stockTakeMap.get(tradeItemId);
    }

    public boolean saveStockTake(Set<StockTake> stockTakeSet) {
        return stockTakeInteractor.saveStockTake(stockTakeSet);
    }

    public Map<String, Integer> findStockBalanceByTradeItemIds(String programId, List<TradeItem> tradeItems) {
        List<String> tradeItemIds = new ArrayList<>();
        for (TradeItem tradeItem : tradeItems)
            tradeItemIds.add(tradeItem.getId());
        return stockTakeInteractor.findStockBalanceByTradeItemIds(programId, tradeItemIds);
    }

    public Map<String, Integer> findStockBalanceByLots(String programId, List<Lot> lots) {
        List<String> lotIds = new ArrayList<>();
        for (Lot lot : lots)
            lotIds.add(lot.getId());
        return stockTakeInteractor.findStockBalanceByLotsIds(programId, lotIds);
    }

    public void updateAdjustedTradeItems(Set<StockTake> stockTakes) {
        for (StockTake stockTake : stockTakes)
            adjustedTradeItems.add(stockTake.getTradeItemId());
        stockTakeView.updateTradeItemsAdjusted(adjustedTradeItems.size(), new Date(stockTakes.iterator().next().getLastUpdated()));
        enableSubmit();
    }

    private void enableSubmit() {
        if (adjustedTradeItems.size() == totalTradeItems)
            stockTakeView.onActivateSubmit();
    }

    public void completeStockTake(final String provider) {
        stockTakeView.showProgressDialog(stockTakeView.getContext().getString(R.string.stock_take),
                stockTakeView.getContext().getString(R.string.save_in_progress));
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final boolean processed = stockTakeInteractor.completeStockTake(programId,
                        adjustedTradeItems, provider, stockTakeView.getContext());
                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        stockTakeView.hideProgressDialog();
                        if (processed) {
                            stockTakeView.onExitStockTake(true);
                        }
                    }
                });
            }
        });
    }

    public void registerStockTake(String tradeItemId, Set<StockTake> stockTakeSet) {
        stockTakeMap.put(tradeItemId, stockTakeSet);
    }

    public void unregisterStockTake(String tradeItemId) {
        stockTakeMap.remove(tradeItemId);
    }

    public void saveStockTakeDraft() {
        stockTakeView.showProgressDialog(stockTakeView.getContext().getString(R.string.stock_take),
                stockTakeView.getContext().getString(R.string.save_in_progress));
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                Set<StockTake> stockTakeSet = new HashSet<>();
                for (String tradeItemId : stockTakeMap.keySet()) {
                    boolean isValid = true;
                    for (StockTake stocktake : stockTakeMap.get(tradeItemId)) {
                        if (!stocktake.isValid()) {
                            isValid = false;
                            break;
                        }
                    }
                    if (isValid) {
                        stockTakeSet.addAll(stockTakeMap.get(tradeItemId));
                    }
                }
                final boolean processed = stockTakeInteractor.saveStockTake(stockTakeSet);
                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        stockTakeView.hideProgressDialog();
                        if (processed) {
                            stockTakeView.onExitStockTake(false);
                        }
                    }
                });
            }
        });

    }

    public List<TradeItem> findTradeItemsWithActiveLotsByTradeItemIds(Set<String> tradeItemIds) {
        return stockTakeInteractor.findTradeItemsWithActiveLotsByTradeItemIds(tradeItemIds);
    }

    public Set<String> getAdjustedTradeItems() {
        return adjustedTradeItems;
    }

}
