package org.smartregister.stock.openlmis.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.domain.openlmis.Lot;
import org.smartregister.stock.openlmis.domain.openlmis.Reason;
import org.smartregister.stock.openlmis.listener.SaveStateListener;
import org.smartregister.stock.openlmis.presenter.StockTakePresenter;
import org.smartregister.stock.openlmis.view.viewholder.StockTakeLotViewHolder;

import java.util.List;

/**
 * Created by samuelgithengi on 9/21/18.
 */
public class StockTakeLotAdapter extends RecyclerView.Adapter<StockTakeLotViewHolder> {

    private SaveStateListener saveStateListener;

    private List<Lot> lots;

    private List<Reason> adjustReasons;


    public StockTakeLotAdapter(StockTakePresenter stockTakePresenter, String programId, String tradeItemId, SaveStateListener saveStateListener) {
        this.saveStateListener = saveStateListener;
        lots = stockTakePresenter.findLotsByTradeItem(tradeItemId);
        adjustReasons = stockTakePresenter.findAdjustReasons(programId);
    }

    @NonNull
    @Override
    public StockTakeLotViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(
                R.layout.stock_take_lot_row, viewGroup, false);
        return new StockTakeLotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StockTakeLotViewHolder stockTakeLotViewHolder, int position) {
        stockTakeLotViewHolder.setStockAdjustReasons(adjustReasons);
        stockTakeLotViewHolder.setSaveStateListener(saveStateListener);

        Lot lot = lots.get(position);
        stockTakeLotViewHolder.setLotCodeAndExpiry(lot.getLotCode(), lot.getExpirationDate());
        stockTakeLotViewHolder.setStockOnHand(10);
        stockTakeLotViewHolder.setPhysicalCount(10);
        stockTakeLotViewHolder.setStatus(lot.getLotStatus());
    }

    @Override
    public int getItemCount() {
        return lots.size();
    }
}