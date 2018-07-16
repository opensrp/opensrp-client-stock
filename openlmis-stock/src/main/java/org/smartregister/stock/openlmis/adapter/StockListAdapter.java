package org.smartregister.stock.openlmis.adapter;

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

    public StockListAdapter(List<CommodityType> commodityTypes) {
        this.commodityTypes = commodityTypes;
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
        holder.getCommodityTypeTextView().setText(commodityType.getName());

    }

    @Override
    public int getItemCount() {
        return commodityTypes.size();
    }
}
