package org.smartregister.stock.openlmis.view.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.listener.StockTakeListener;
import org.smartregister.stock.openlmis.widget.helper.LotDto;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by samuelgithengi on 9/20/18.
 */
public class StockTakeTradeItemViewHolder extends RecyclerView.ViewHolder implements StockTakeListener {

    private TextView tradeItemTextView;

    private RecyclerView lotsRecyclerView;

    private Button saveButton;

    private Set<LotDto> lotDtos = new HashSet<>();

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


    private void enableSave() {
        saveButton.setEnabled(true);
        saveButton.setTextColor(saveButton.getResources().getColor(R.color.light_blue));
    }

    private void disableSave() {
        saveButton.setEnabled(false);
        saveButton.setTextColor(saveButton.getResources().getColor(R.color.save_disabled));
    }

    @Override
    public void registerLotDetails(LotDto lotDto) {
        lotDtos.add(lotDto);
        boolean isValid = true;
        for (LotDto lot : lotDtos) {
            if (!lot.isValid()) {
                disableSave();
                isValid = false;
                break;
            }
        }
        if (isValid)
            enableSave();

    }
}
