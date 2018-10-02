package org.smartregister.stock.openlmis.view.viewholder.stocktake;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.smartregister.stock.openlmis.R;

/**
 * Created by samuelgithengi on 9/20/18.
 */
public class StockTakeCommodityTypeViewHolder extends RecyclerView.ViewHolder {

    private TextView commodityType;

    private RecyclerView tradeItemRecyclerView;

    public StockTakeCommodityTypeViewHolder(@NonNull View itemView) {
        super(itemView);
        commodityType = itemView.findViewById(R.id.commodityTypeTextView);
        tradeItemRecyclerView = itemView.findViewById(R.id.tradeItemsRecyclerView);
    }

    public void setCommodityTypeName(String commodityTypeName) {
        commodityType.setText(commodityTypeName);
    }

    public RecyclerView getTradeItemRecyclerView() {
        return tradeItemRecyclerView;
    }
}
