package org.smartregister.stock.openlmis.view.viewholder;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import org.smartregister.stock.openlmis.R;

/**
 * Created by samuelgithengi on 7/13/18.
 */
public class StockListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private Context context;

    private TextView commodityTypeTextView;

    private TextView doseTextView;

    private ImageView collapseExpandButton;

    private FrameLayout tradeItemsView;

    public StockListViewHolder(View itemView) {
        super(itemView);
        this.context = itemView.getContext();
        commodityTypeTextView = itemView.findViewById(R.id.commodityTypeTextView);
        doseTextView = itemView.findViewById(R.id.doseTextView);
        collapseExpandButton = itemView.findViewById(R.id.collapseExpandButton);
        tradeItemsView = itemView.findViewById(R.id.tradeItemsView);
        collapseExpandButton.setOnClickListener(this);
        itemView.findViewById(R.id.commodityTypeMore).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.collapseExpandButton) {
            if (tradeItemsView.getVisibility() == View.VISIBLE) {
                tradeItemsView.setVisibility(View.GONE);
                collapseExpandButton.setImageResource(R.drawable.ic_keyboard_arrow_down);
            } else {
                tradeItemsView.setVisibility(View.VISIBLE);
                collapseExpandButton.setImageResource(R.drawable.ic_keyboard_arrow_up);
            }

        } else if (view.getId() == R.id.commodityTypeMore) {
            PopupMenu popupMenu = new PopupMenu(context, view, Gravity.NO_GRAVITY, R.attr.actionOverflowMenuStyle, 0);
            popupMenu.inflate(R.menu.commodity_type_menu);
            popupMenu.show();
        }

    }

    public TextView getCommodityTypeTextView() {
        return commodityTypeTextView;
    }

    public TextView getDoseTextView() {
        return doseTextView;
    }
}
