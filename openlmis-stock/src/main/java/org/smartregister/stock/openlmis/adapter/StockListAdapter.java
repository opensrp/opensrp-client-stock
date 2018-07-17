package org.smartregister.stock.openlmis.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.domain.CommodityType;
import org.smartregister.stock.openlmis.view.viewholder.StockListViewHolder;

import java.util.List;

/**
 * Created by samuelgithengi on 7/13/18.
 */
public class StockListAdapter extends RecyclerView.Adapter<StockListViewHolder> {

    private List<CommodityType> commodityTypes;

    private Context context;

    public StockListAdapter(List<CommodityType> commodityTypes, Context context) {
        this.commodityTypes = commodityTypes;
        this.context = context;
    }

    @NonNull
    @Override
    public StockListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.commodity_type_row, parent, false);
        return new StockListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StockListViewHolder holder, int position) {
        CommodityType commodityType = commodityTypes.get(position);
        holder.getCommodityTypeTextView().setText(context.getString(R.string.commodity_type_formatter, commodityType.getName(), 3));
        holder.getDoseTextView().setText(context.getString(R.string.dose_formatter, 1300));

    }

    @Override
    public int getItemCount() {
        return commodityTypes.size();
    }
}
