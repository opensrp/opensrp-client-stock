package org.smartregister.stock.openlmis.adapter.stocktake;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.domain.StockTake;
import org.smartregister.stock.openlmis.domain.TradeItem;
import org.smartregister.stock.openlmis.presenter.StockTakePresenter;
import org.smartregister.stock.openlmis.view.viewholder.stocktake.NonLotTradeItemViewHolder;
import org.smartregister.stock.openlmis.view.viewholder.stocktake.StockTakeTradeItemViewHolder;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by samuelgithengi on 9/20/18.
 */
public class StockTakeTradeItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "StockTakeTradeItemAdapt";

    private StockTakePresenter stockTakePresenter;

    private List<TradeItem> tradeItems;

    private String programId;

    private String commodityTypeId;

    private Map<String, Integer> stockBalances;

    public StockTakeTradeItemAdapter(StockTakePresenter stockTakePresenter, String programId, String commodityTypeId, Set<String> tradeItemIds) {
        this.stockTakePresenter = stockTakePresenter;
        this.programId = programId;
        this.commodityTypeId = commodityTypeId;
        if (tradeItemIds == null)
            tradeItems = stockTakePresenter.findTradeItemsWithActiveLots(commodityTypeId);
        else
            tradeItems = stockTakePresenter.findTradeItemsWithActiveLotsByTradeItemIds(tradeItemIds);
        stockBalances = stockTakePresenter.findStockBalanceByTradeItemIds(programId, tradeItems);
    }


    @Override
    public int getItemViewType(int position) {
        if (tradeItems.get(position).isHasLots()) {
            return ViewType.LOT_MANAGED.getValue();
        } else {
            return ViewType.NON_LOT_MANGED.getValue();
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        if (ViewType.LOT_MANAGED.getValue() == viewType) {
            View view = LayoutInflater.from(context).inflate(
                    R.layout.stock_take_trade_item_row, viewGroup, false);
            return new StockTakeTradeItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(
                    R.layout.stock_take_trade_item_none_lot_row, viewGroup, false);
            return new NonLotTradeItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder stockTakeTradeItemViewHolder, int position) {
        TradeItem tradeItem = tradeItems.get(position);
        if (tradeItem.isHasLots())
            bindLotManaged((StockTakeTradeItemViewHolder) stockTakeTradeItemViewHolder, tradeItem);
        else
            bindNotLotManaged((NonLotTradeItemViewHolder) stockTakeTradeItemViewHolder, tradeItem);
    }


    private void bindLotManaged(StockTakeTradeItemViewHolder stockTakeTradeItemViewHolder, TradeItem tradeItem) {
        Set<StockTake> stockTakeSet = stockTakePresenter.findStockTakeList(programId, tradeItem.getId());
        stockTakeTradeItemViewHolder.setTradeItemName(tradeItem.getName());
        stockTakeTradeItemViewHolder.setTradeItemId(tradeItem.getId());
        stockTakeTradeItemViewHolder.setStockTakePresenter(stockTakePresenter);
        stockTakeTradeItemViewHolder.setDispensingUnit(tradeItem.getDispensable().getKeyDispensingUnit());
        if (stockBalances.containsKey(tradeItem.getId()))
            stockTakeTradeItemViewHolder.setStockOnHand(stockBalances.get(tradeItem.getId()));
        else
            stockTakeTradeItemViewHolder.setStockOnHand(0);
        if (!stockTakePresenter.getAdjustedTradeItems().contains(tradeItem.getId())) {
            StockTakeLotAdapter adapter = new StockTakeLotAdapter(stockTakePresenter, programId, commodityTypeId,
                    tradeItem.getId(), stockTakeSet, stockTakeTradeItemViewHolder, tradeItem.isUseVvm());
            stockTakeTradeItemViewHolder.getLotsRecyclerView().setAdapter(adapter);
        } else {
            stockTakeTradeItemViewHolder.setStockTakeSet(stockTakeSet);
            stockTakeTradeItemViewHolder.stockTakeCompleted();
            stockTakePresenter.registerStockTake(tradeItem.getId(), stockTakeSet);
        }
    }

    private void bindNotLotManaged(NonLotTradeItemViewHolder stockTakeTradeItemViewHolder, TradeItem tradeItem) {
        stockTakeTradeItemViewHolder.setStockAdjustReasons(stockTakePresenter.findAdjustReasons());
        stockTakeTradeItemViewHolder.setStockTakeListener(stockTakeTradeItemViewHolder);
        stockTakeTradeItemViewHolder.setTradeItemName(tradeItem.getName());
        stockTakeTradeItemViewHolder.setTradeItemId(tradeItem.getId());
        stockTakeTradeItemViewHolder.setStockTakePresenter(stockTakePresenter);
        stockTakeTradeItemViewHolder.setDispensingUnit(tradeItem.getDispensable().getKeyDispensingUnit());
        if (!tradeItem.isUseVvm())
            stockTakeTradeItemViewHolder.hideVVMStatus();
        int stockOnHand = 0;
        if (stockBalances.containsKey(tradeItem.getId()))
            stockOnHand = stockBalances.get(tradeItem.getId());

        stockTakeTradeItemViewHolder.setStockOnHand(stockOnHand);

        Set<StockTake> stockTakeSet = stockTakePresenter.findStockTakeList(programId, tradeItem.getId());

        if (!stockTakeSet.isEmpty()) {
            if (stockTakeSet.size() > 1)
                Log.w(TAG, String.format("Multiple stock take for Non-Lot Managed Trade Item %s %s ",
                        tradeItem.getId(), tradeItem.getName()));
            StockTake stockTake = stockTakeSet.iterator().next();
            stockTakeTradeItemViewHolder.setStockTake(stockTake);
            stockTakeTradeItemViewHolder.setDifference(stockTake.getQuantity());
            stockTakeTradeItemViewHolder.setStatus(stockTake.getStatus());
            stockTakeTradeItemViewHolder.setReason(stockTake.getReason());
            stockTakeTradeItemViewHolder.setPhysicalCount(stockOnHand + stockTake.getQuantity());
            stockTakeTradeItemViewHolder.activateNoChange(stockTake.isNoChange());
            stockTakeTradeItemViewHolder.registerStockTake(stockTake);
            if (stockTakePresenter.getAdjustedTradeItems().contains(tradeItem.getId()))
                stockTakeTradeItemViewHolder.stockTakeCompleted();
        } else {
            StockTake stockTake = new StockTake(programId, commodityTypeId, tradeItem.getId(), null);
            stockTake.setDisplayStatus(tradeItem.isUseVvm());
            stockTake.setLastUpdated(System.currentTimeMillis());
            stockTakeTradeItemViewHolder.setStockTake(stockTake);
            stockTakeTradeItemViewHolder.setPhysicalCount(stockOnHand);
        }
    }

    @Override
    public int getItemCount() {
        return tradeItems.size();
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }

    enum ViewType {
        LOT_MANAGED(1), NON_LOT_MANGED(2);

        private int value;

        ViewType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
