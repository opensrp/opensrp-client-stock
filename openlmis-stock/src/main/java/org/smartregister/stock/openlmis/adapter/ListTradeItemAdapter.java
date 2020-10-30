package org.smartregister.stock.openlmis.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.domain.TradeItem;
import org.smartregister.stock.openlmis.presenter.StockListPresenter;
import org.smartregister.stock.openlmis.view.viewholder.TradeItemViewHolder;
import org.smartregister.stock.openlmis.wrapper.TradeItemWrapper;

import java.util.List;

/**
 * Created by samuelgithengi on 7/17/18.
 */
public class ListTradeItemAdapter extends RecyclerView.Adapter<TradeItemViewHolder> {

    private List<TradeItemWrapper> tradeItems;
    private String programId;
    private Context context;

    private final StockListPresenter stockListPresenter;

    public ListTradeItemAdapter(List<TradeItemWrapper> tradeItems, String programId, Context context,
                                StockListPresenter stockListPresenter) {
        this.tradeItems = tradeItems;
        this.programId = programId;
        this.context = context;
        this.stockListPresenter = stockListPresenter;
    }

    @NonNull
    @Override
    public TradeItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.trade_item_row, parent, false);
        return new TradeItemViewHolder(stockListPresenter, itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TradeItemViewHolder holder, int position) {
        TradeItemWrapper tradeItemWrapper = tradeItems.get(position);
        TradeItem tradeItem = tradeItemWrapper.getTradeItem();
        holder.setTradeItemWrapper(tradeItemWrapper);
        holder.setProgramId(programId);
        holder.getNameTextView().setText(tradeItem.getName());
        holder.getNameTextView().setTag(R.id.trade_item_updated_key, tradeItem.getDateUpdated());
        holder.getNameTextView().setTag(R.id.trade_item_id_key, tradeItem.getId());
        if (tradeItemWrapper.isHasLots())
            holder.getLotsTextView().setText(context.getString(R.string.lot_formatter, tradeItemWrapper.getNumberOfLots()));
        else {
            holder.getLotsTextView().measure(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
            int lotsHeight = holder.getLotsTextView().getMeasuredHeight() / 2;
            holder.getLotsTextView().setVisibility(View.GONE);
            int tradeItemPadding = Math.round(context.getResources().getDimension(R.dimen.list_stock_padding));
            holder.getNameTextView().setPadding(tradeItemPadding, 3 * tradeItemPadding / 2 + lotsHeight / 2,
                    tradeItemPadding, lotsHeight / 2 + 3 * tradeItemPadding / 2);
        }
        holder.getDispensableTextView().setText(context.getString(R.string.dispensable_formatter,
                tradeItemWrapper.getTotalStock(),
                tradeItem.getDispensable() == null ? null : tradeItem.getDispensable().getKeyDispensingUnit()));
        holder.getExpiringTextView().setVisibility(tradeItemWrapper.isHasLotExpiring() ? View.VISIBLE : View.INVISIBLE);

    }

    @Override
    public int getItemCount() {
        return tradeItems.size();
    }
}
