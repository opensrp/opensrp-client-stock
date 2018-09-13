package org.smartregister.stock.openlmis.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.domain.TradeItem;
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

    public ListTradeItemAdapter(List<TradeItemWrapper> tradeItems, String programId, Context context) {
        this.tradeItems = tradeItems;
        this.programId = programId;
        this.context = context;
    }

    @NonNull
    @Override
    public TradeItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.trade_item_row, parent, false);
        return new TradeItemViewHolder(itemView);
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
        holder.getLotsTextView().setText(context.getString(R.string.lot_formatter, tradeItemWrapper.getNumberOfLots()));
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
