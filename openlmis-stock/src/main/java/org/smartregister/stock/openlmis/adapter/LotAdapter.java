package org.smartregister.stock.openlmis.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.domain.TradeItem;
import org.smartregister.stock.openlmis.domain.openlmis.Lot;
import org.smartregister.stock.openlmis.presenter.StockDetailsPresenter;
import org.smartregister.stock.openlmis.view.viewholder.LotViewHolder;

import java.util.List;

/**
 * Created by samuelgithengi on 8/1/18.
 */
public class LotAdapter extends RecyclerView.Adapter<LotViewHolder> {

    static String DATE_FORMAT = "dd-MM-yyyy";

    private Context context;
    private List<Lot> lots;

    private TradeItem tradeItem;

    private StockDetailsPresenter stockDetailsPresenter;

    public LotAdapter(String tradeItemId, StockDetailsPresenter stockDetailsPresenter) {
        this.stockDetailsPresenter = stockDetailsPresenter;
        lots = stockDetailsPresenter.findLotsByTradeItem(tradeItemId);
        tradeItem = stockDetailsPresenter.findTradeItem(tradeItemId);
    }

    @NonNull
    @Override
    public LotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.lot_row, parent, false);
        return new LotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LotViewHolder holder, int position) {
        Lot lot = lots.get(position);
        holder.getLotCodeTextView().setText(context.getString(R.string.stock_on_hand_and_expiry,
                lot.getLotCode(), lot.getExpirationDate().toString(DATE_FORMAT)));
        holder.getStockOnHandTextView().setText(context.getString(R.string.dispensable_formatter,
                stockDetailsPresenter.getTotalStockByLot(lot.getId()),
                tradeItem.getDispensable().getKeyDispensingUnit()));
        holder.getStatusTextView().setText(context.getString(R.string.VMM, 1));

    }

    @Override
    public int getItemCount() {
        return lots.size();
    }
}
