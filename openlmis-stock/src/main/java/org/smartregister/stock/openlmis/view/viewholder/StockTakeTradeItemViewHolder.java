package org.smartregister.stock.openlmis.view.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.adapter.StockTakeLotAdapter;
import org.smartregister.stock.openlmis.domain.StockTake;
import org.smartregister.stock.openlmis.listener.StockTakeListener;
import org.smartregister.stock.openlmis.presenter.StockTakePresenter;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by samuelgithengi on 9/20/18.
 */
public class StockTakeTradeItemViewHolder extends RecyclerView.ViewHolder implements StockTakeListener {

    private StockTakePresenter stockTakePresenter;

    private TextView tradeItemTextView;

    private RecyclerView lotsRecyclerView;

    private Button saveButton;

    private LinearLayout pendingStockTake;

    private RelativeLayout completedStockTake;

    private TextView completedTradeItem;

    private TextView adjustment;

    private Set<StockTake> stockTakeSet = new HashSet<>();

    private int stockOnhand;

    private String dispensingUnit;

    private String tradeItemId;

    public StockTakeTradeItemViewHolder(@NonNull View itemView) {
        super(itemView);
        pendingStockTake = itemView.findViewById(R.id.pending_stock_take);
        completedStockTake = itemView.findViewById(R.id.completed_stock_take);
        tradeItemTextView = itemView.findViewById(R.id.trade_item);
        lotsRecyclerView = itemView.findViewById(R.id.lotsRecyclerView);
        saveButton = itemView.findViewById(R.id.save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stockTakePresenter.saveStockTake(stockTakeSet)) {
                    stockTakeCompleted();
                    stockTakePresenter.updateAdjustedTradeItems(stockTakeSet);
                }
            }
        });

        completedTradeItem = itemView.findViewById(R.id.completed_trade_item);
        adjustment = itemView.findViewById(R.id.adjustment);
        itemView.findViewById(R.id.edit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayLots();
            }
        });
    }

    public void setStockTakePresenter(StockTakePresenter stockTakePresenter) {
        this.stockTakePresenter = stockTakePresenter;
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
        stockTakePresenter.registerStockTake(tradeItemId, this.stockTakeSet);
    }

    private void disableSave() {
        saveButton.setEnabled(false);
        saveButton.setTextColor(saveButton.getResources().getColor(R.color.save_disabled));
        stockTakePresenter.unregisterStockTake(tradeItemId);
    }

    @Override
    public void registerStockTake(StockTake stockTake) {
        stockTakeSet.add(stockTake);
        boolean isValid = true;
        for (StockTake stockTake1 : stockTakeSet) {
            if (!stockTake1.isValid()) {
                disableSave();
                isValid = false;
                break;
            }
        }
        if (isValid)
            enableSave();
        stockTakePresenter.registerStockTake(tradeItemId, stockTakeSet);
    }


    public void stockTakeCompleted() {
        pendingStockTake.setVisibility(View.GONE);
        completedStockTake.setVisibility(View.VISIBLE);
        completedTradeItem.setText(tradeItemTextView.getText());
        int totalAdjustment = 0;
        for (StockTake stockTake : stockTakeSet)
            totalAdjustment += stockTake.getQuantity();
        if (totalAdjustment != 0) {
            String totalAdjustmentFormatted = totalAdjustment > 0 ? "+" + totalAdjustment : "" + totalAdjustment;
            adjustment.setText(adjustment.getContext().getString(R.string.stock_take_adjustment,
                    stockOnhand + totalAdjustment, dispensingUnit, totalAdjustmentFormatted));
        } else
            adjustment.setText(adjustment.getContext().getString(R.string.stock_take_no_adjustment,
                    stockOnhand + totalAdjustment, dispensingUnit));
    }

    private void displayLots() {
        pendingStockTake.setVisibility(View.VISIBLE);
        completedStockTake.setVisibility(View.GONE);
        StockTake stockTake = stockTakeSet.iterator().next();
        StockTakeLotAdapter adapter = new StockTakeLotAdapter(stockTakePresenter, stockTake.getProgramId(), stockTake.getCommodityTypeId(),
                stockTake.getTradeItemId(), stockTakeSet, this);
        getLotsRecyclerView().setAdapter(adapter);
    }

    public void setStockOnhand(int stockOnhand) {
        this.stockOnhand = stockOnhand;
    }

    public void setDispensingUnit(String dispensingUnit) {
        this.dispensingUnit = dispensingUnit;
    }

    public void setStockTakeSet(Set<StockTake> stockTakeSet) {
        this.stockTakeSet = stockTakeSet;
    }

    public void setTradeItemId(String tradeItemId) {
        this.tradeItemId = tradeItemId;
    }
}
