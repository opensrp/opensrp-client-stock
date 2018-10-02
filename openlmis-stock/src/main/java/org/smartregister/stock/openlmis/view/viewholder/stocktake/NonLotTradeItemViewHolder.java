package org.smartregister.stock.openlmis.view.viewholder.stocktake;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.domain.StockTake;
import org.smartregister.stock.openlmis.listener.StockTakeListener;
import org.smartregister.stock.openlmis.presenter.StockTakePresenter;

/**
 * Created by samuelgithengi on 10/2/18.
 */
public class NonLotTradeItemViewHolder extends RecyclerView.ViewHolder implements StockTakeListener {

    private StockTakePresenter stockTakePresenter;


    private TextView tradeItemTextView;

    public NonLotTradeItemViewHolder(@NonNull View itemView) {
        super(itemView);
        tradeItemTextView = itemView.findViewById(R.id.trade_item);

    }

    public void setStockTakePresenter(StockTakePresenter stockTakePresenter) {
        this.stockTakePresenter = stockTakePresenter;
    }


    public void setTradeItemName(String tradeItemName) {
        tradeItemTextView.setText(tradeItemName);
    }

    @Override
    public void registerStockTake(StockTake stockTake) {

    }
}
