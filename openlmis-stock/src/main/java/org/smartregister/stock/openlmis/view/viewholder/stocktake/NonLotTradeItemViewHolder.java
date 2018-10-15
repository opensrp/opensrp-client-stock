package org.smartregister.stock.openlmis.view.viewholder.stocktake;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.domain.StockTake;
import org.smartregister.stock.openlmis.listener.StockTakeListener;
import org.smartregister.stock.openlmis.presenter.StockTakePresenter;

import java.util.Collections;
import java.util.HashSet;

/**
 * Created by samuelgithengi on 10/2/18.
 */
public class NonLotTradeItemViewHolder extends BaseStockTakeViewHolder implements StockTakeListener {

    private StockTakePresenter stockTakePresenter;

    private TextView tradeItemTextView;

    private Button saveButton;

    private String dispensingUnit;

    private String tradeItemId;

    private View pendingStockTake;

    private View completedStockTake;

    private TextView completedTradeItem;

    private TextView adjustment;

    private View vvmStatus;

    public NonLotTradeItemViewHolder(@NonNull View itemView) {
        super(itemView);
        pendingStockTake = itemView.findViewById(R.id.pending_stock_take);
        completedStockTake = itemView.findViewById(R.id.completed_stock_take);

        tradeItemTextView = itemView.findViewById(R.id.trade_item);
        saveButton = itemView.findViewById(R.id.save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashSet<StockTake> stockTakeSet = new HashSet<>(Collections.singleton(stockTake));
                if (stockTakePresenter.saveStockTake(stockTakeSet)) {
                    stockTakeCompleted();
                    stockTakePresenter.updateAdjustedTradeItems(stockTakeSet);
                }
            }
        });

        completedTradeItem = itemView.findViewById(R.id.completed_trade_item);
        adjustment = itemView.findViewById(R.id.completed_adjustment);

        itemView.findViewById(R.id.edit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayForm();
            }
        });
        vvmStatus = itemView.findViewById(R.id.lot_status);

    }

    public void setTradeItemName(String tradeItemName) {
        tradeItemTextView.setText(tradeItemName);
    }

    public void setStockTakePresenter(StockTakePresenter stockTakePresenter) {
        this.stockTakePresenter = stockTakePresenter;
    }

    public void setDispensingUnit(String dispensingUnit) {
        this.dispensingUnit = dispensingUnit;
    }

    public void setTradeItemId(String tradeItemId) {
        this.tradeItemId = tradeItemId;
    }

    private void enableSave() {
        saveButton.setEnabled(true);
        saveButton.setTextColor(saveButton.getResources().getColor(R.color.light_blue));
        new HashSet<>();
        stockTakePresenter.registerStockTake(tradeItemId, new HashSet<>(Collections.singleton(stockTake)));
    }

    private void disableSave() {
        saveButton.setEnabled(false);
        saveButton.setTextColor(saveButton.getResources().getColor(R.color.save_disabled));
        stockTakePresenter.unregisterStockTake(tradeItemId);
    }

    @Override
    public void registerStockTake(StockTake stockTake) {
        if (stockTake.isValid())
            enableSave();
        else
            disableSave();
    }

    public void stockTakeCompleted() {
        pendingStockTake.setVisibility(View.GONE);
        completedStockTake.setVisibility(View.VISIBLE);
        completedTradeItem.setText(tradeItemTextView.getText());
        int totalAdjustment = stockTake.getQuantity();
        if (totalAdjustment != 0) {
            String totalAdjustmentFormatted = totalAdjustment > 0 ? "+" + totalAdjustment : "" + totalAdjustment;
            adjustment.setText(adjustment.getContext().getString(R.string.stock_take_adjustment,
                    stockOnHand + totalAdjustment, dispensingUnit, totalAdjustmentFormatted));
        } else
            adjustment.setText(adjustment.getContext().getString(R.string.stock_take_no_adjustment,
                    stockOnHand + totalAdjustment, dispensingUnit));
    }

    private void displayForm() {
        pendingStockTake.setVisibility(View.VISIBLE);
        completedStockTake.setVisibility(View.GONE);
    }

    public void hideVVMStatus() {
        vvmStatus.setVisibility(View.GONE);
    }
}
