package org.smartregister.stock.openlmis.view.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.smartregister.stock.openlmis.R;

/**
 * Created by samuelgithengi on 9/20/18.
 */
public class StockTakeTradeItemViewHolder extends RecyclerView.ViewHolder {

    private TextView tradeItemTextView;

    private RecyclerView lotsRecyclerView;

    public StockTakeTradeItemViewHolder(@NonNull View itemView) {
        super(itemView);
        tradeItemTextView = itemView.findViewById(R.id.trade_item);
        lotsRecyclerView = itemView.findViewById(R.id.lotsRecyclerView);
    }

    public void setTradeItemName(String name) {
        tradeItemTextView.setText(name);
    }

    public RecyclerView getLotsRecyclerView() {
        return lotsRecyclerView;
    }

}
