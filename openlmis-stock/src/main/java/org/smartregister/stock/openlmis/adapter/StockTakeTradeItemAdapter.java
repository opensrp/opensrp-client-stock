package org.smartregister.stock.openlmis.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.domain.StockTake;
import org.smartregister.stock.openlmis.domain.TradeItem;
import org.smartregister.stock.openlmis.presenter.StockTakePresenter;
import org.smartregister.stock.openlmis.view.viewholder.StockTakeTradeItemViewHolder;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by samuelgithengi on 9/20/18.
 */
public class StockTakeTradeItemAdapter extends RecyclerView.Adapter<StockTakeTradeItemViewHolder> {

    private StockTakePresenter stockTakePresenter;

    private List<TradeItem> tradeItems;

    private String programId;

    private String commodityTypeId;
    private Set<String> tradeItemIds;

    private Map<String, Integer> stockBalances;

    public StockTakeTradeItemAdapter(StockTakePresenter stockTakePresenter, String programId, String commodityTypeId, Set<String> tradeItemIds) {
        this.stockTakePresenter = stockTakePresenter;
        this.programId = programId;
        this.commodityTypeId = commodityTypeId;
        this.tradeItemIds = tradeItemIds;
        if (tradeItemIds == null)
            tradeItems = stockTakePresenter.findTradeItemsWithActiveLots(commodityTypeId);
        else
            tradeItems = stockTakePresenter.findTradeItemsWithActiveLotsByTradeItemIds(tradeItemIds);
        stockBalances = stockTakePresenter.findStockBalanceByTradeItemIds(programId, tradeItems);
    }

    @NonNull
    @Override
    public StockTakeTradeItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(
                R.layout.stock_take_trade_item_row, viewGroup, false);
        return new StockTakeTradeItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StockTakeTradeItemViewHolder stockTakeTradeItemViewHolder, int position) {
        TradeItem tradeItem = tradeItems.get(position);
        Set<StockTake> stockTakeSet = stockTakePresenter.findStockTakeList(programId, tradeItem.getId());
        stockTakeTradeItemViewHolder.setTradeItemName(tradeItem.getName());
        stockTakeTradeItemViewHolder.setStockTakePresenter(stockTakePresenter);
        stockTakeTradeItemViewHolder.setDispensingUnit(tradeItem.getDispensable().getKeyDispensingUnit());
        if (stockBalances.containsKey(tradeItem.getId()))
            stockTakeTradeItemViewHolder.setStockOnhand(stockBalances.get(tradeItem.getId()));
        else
            stockTakeTradeItemViewHolder.setStockOnhand(0);
        if (stockTakeSet.isEmpty()) {
            StockTakeLotAdapter adapter = new StockTakeLotAdapter(stockTakePresenter, programId, commodityTypeId,
                    tradeItem.getId(), stockTakeSet, stockTakeTradeItemViewHolder);
            stockTakeTradeItemViewHolder.getLotsRecyclerView().setAdapter(adapter);
        } else {
            stockTakeTradeItemViewHolder.setStockTakeSet(stockTakeSet);
            stockTakeTradeItemViewHolder.stockTakeCompleted();
            stockTakePresenter.registerStockTake(stockTakeSet);
        }
    }

    @Override
    public int getItemCount() {
        return tradeItems.size();
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }
}
