package org.smartregister.stock.openlmis.view.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.smartregister.stock.openlmis.R;

/**
 * Created by samuelgithengi on 7/13/18.
 */
public class StockListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private Context context;

    private TextView commodityTypeTextView;

    private ImageView collapseExpandButton;

    public StockListViewHolder(View itemView) {
        super(itemView);
        this.context = itemView.getContext();
        commodityTypeTextView = itemView.findViewById(R.id.commodityTypeTextView);
        collapseExpandButton = itemView.findViewById(R.id.collapseExpandButton);
    }

    @Override
    public void onClick(View v) {

    }

    public TextView getCommodityTypeTextView() {
        return commodityTypeTextView;
    }
}
