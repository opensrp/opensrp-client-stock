package org.smartregister.stock.openlmis.view.viewholder;

import android.content.Context;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.listener.ExpandCollapseListener;

/**
 * Created by samuelgithengi on 7/13/18.
 */
public class CommodityTypeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
        , ExpandCollapseListener {

    private Context context;

    private TextView commodityTypeTextView;

    private TextView doseTextView;

    private ImageView collapseExpandButton;

    private ImageView commodityTypeMore;

    private RecyclerView tradeItemsRecyclerView;

    public CommodityTypeViewHolder(View itemView) {
        super(itemView);
        this.context = itemView.getContext();
        commodityTypeTextView = itemView.findViewById(R.id.commodityTypeTextView);
        doseTextView = itemView.findViewById(R.id.doseTextView);
        collapseExpandButton = itemView.findViewById(R.id.collapseExpandButton);
        tradeItemsRecyclerView = itemView.findViewById(R.id.tradeItemsView);
        commodityTypeMore = itemView.findViewById(R.id.commodityTypeMore);
        collapseExpandButton.setOnClickListener(this);
        itemView.findViewById(R.id.commodityTypeMore).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.collapseExpandButton) {
            if (tradeItemsRecyclerView.getVisibility() == View.VISIBLE) {
                collapseView();
            } else {
                expandView();
            }
        } else if (view.getId() == R.id.commodityTypeMore) {
            showActionsMenu(view);
        }

    }

    protected void showActionsMenu(View view) {
        Context wrapper = new ContextThemeWrapper(context, R.style.PopupOverlay_Light);
        PopupMenu stockActionsMenu = new PopupMenu(wrapper, view, Gravity.NO_GRAVITY, R.attr.actionOverflowMenuStyle, 0);
        stockActionsMenu.inflate(R.menu.commodity_type_menu);
        stockActionsMenu.show();
    }

    public TextView getCommodityTypeTextView() {
        return commodityTypeTextView;
    }

    public TextView getDoseTextView() {
        return doseTextView;
    }

    public RecyclerView getTradeItemsRecyclerView() {
        return tradeItemsRecyclerView;
    }

    public ImageView getCollapseExpandButton() {
        return collapseExpandButton;
    }

    public ImageView getCommodityTypeMore() {
        return commodityTypeMore;
    }

    @Override
    public void expandView() {
        tradeItemsRecyclerView.setVisibility(View.VISIBLE);
        collapseExpandButton.setImageResource(R.drawable.ic_keyboard_arrow_up);

    }

    @Override
    public void collapseView() {
        tradeItemsRecyclerView.setVisibility(View.GONE);
        collapseExpandButton.setImageResource(R.drawable.ic_keyboard_arrow_down);
    }
}
