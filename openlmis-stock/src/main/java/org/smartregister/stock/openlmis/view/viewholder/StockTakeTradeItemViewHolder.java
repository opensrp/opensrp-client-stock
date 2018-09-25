package org.smartregister.stock.openlmis.view.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.listener.SaveStateListener;

/**
 * Created by samuelgithengi on 9/20/18.
 */
public class StockTakeTradeItemViewHolder extends RecyclerView.ViewHolder implements SaveStateListener {

    private TextView tradeItemTextView;

    private RecyclerView lotsRecyclerView;

    private Button saveButton;

    public StockTakeTradeItemViewHolder(@NonNull View itemView) {
        super(itemView);
        tradeItemTextView = itemView.findViewById(R.id.trade_item);
        lotsRecyclerView = itemView.findViewById(R.id.lotsRecyclerView);
        saveButton = itemView.findViewById(R.id.save);
    }

    public void setTradeItemName(String name) {
        tradeItemTextView.setText(name);
    }

    public RecyclerView getLotsRecyclerView() {
        return lotsRecyclerView;
    }


    @Override
    public void enableSave() {
        saveButton.setEnabled(true);
        saveButton.setTextColor(saveButton.getResources().getColor(R.color.light_blue));
    }

    @Override
    public void disableSave() {
        saveButton.setEnabled(false);
        saveButton.setTextColor(saveButton.getResources().getColor(R.color.save_disabled));
    }

}
