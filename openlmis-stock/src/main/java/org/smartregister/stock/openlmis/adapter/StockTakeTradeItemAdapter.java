package org.smartregister.stock.openlmis.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.domain.TradeItem;
import org.smartregister.stock.openlmis.presenter.StockTakePresenter;
import org.smartregister.stock.openlmis.view.viewholder.StockTakeTradeItemViewHolder;

import java.util.List;

/**
 * Created by samuelgithengi on 9/20/18.
 */
public class StockTakeTradeItemAdapter extends RecyclerView.Adapter<StockTakeTradeItemViewHolder> {

    private StockTakePresenter stockTakePresenter;

    private List<TradeItem> tradeItems;

    private String programId;

    public StockTakeTradeItemAdapter(StockTakePresenter stockTakePresenter, String programId, String commodityTypeId) {
        this.stockTakePresenter = stockTakePresenter;
        this.programId = programId;
        tradeItems = stockTakePresenter.findTradeItemsWithActiveLots(commodityTypeId);
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
        stockTakeTradeItemViewHolder.setTradeItemName(tradeItem.getName());
        StockTakeLotAdapter adapter = new StockTakeLotAdapter(stockTakePresenter, programId, tradeItem.getId());
        stockTakeTradeItemViewHolder.getLotsRecyclerView().setAdapter(adapter);

    }

    @Override
    public int getItemCount() {
        return tradeItems.size();
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }
}
