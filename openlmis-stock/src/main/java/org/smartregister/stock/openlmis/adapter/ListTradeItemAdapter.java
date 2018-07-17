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

import java.util.List;
import java.util.Random;

/**
 * Created by samuelgithengi on 7/17/18.
 */
public class ListTradeItemAdapter extends RecyclerView.Adapter<TradeItemViewHolder> {

    List<TradeItem> tradeItems;
    private Context context;

    public ListTradeItemAdapter(List<TradeItem> tradeItems, Context context) {
        this.tradeItems = tradeItems;
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
        TradeItem tradeItem = tradeItems.get(position);
        holder.getNameTextView().setText(tradeItem.getManufacturerOfTradeItem());
        Random random = new Random();
        holder.getLotsTextView().setText(context.getString(R.string.lot_formatter, random.nextInt(100)));
        holder.getDispensableTextView().setText(context.getString(R.string.dispensable_formatter, random.nextInt(100), "vials"));
        holder.getExpiringTextView().setVisibility(random.nextInt(4) == 1 ? View.VISIBLE : View.INVISIBLE);

    }

    @Override
    public int getItemCount() {
        return tradeItems.size();
    }
}
