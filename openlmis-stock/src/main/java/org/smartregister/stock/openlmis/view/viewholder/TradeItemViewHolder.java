package org.smartregister.stock.openlmis.view.viewholder;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.smartregister.stock.openlmis.R;

/**
 * Created by samuelgithengi on 7/17/18.
 */
public class TradeItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView nameTextView;
    private TextView lotsTextView;
    private TextView expiringTextView;
    private TextView dispensableTextView;


    public TradeItemViewHolder(View itemView) {
        super(itemView);
        nameTextView = itemView.findViewById(R.id.nameTextView);
        lotsTextView = itemView.findViewById(R.id.lotsTextView);
        expiringTextView = itemView.findViewById(R.id.expiringTextView);
        dispensableTextView = itemView.findViewById(R.id.dispensableTextView);
        itemView.findViewById(R.id.tradeItemsMore).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tradeItemsMore) {
            Snackbar.make(view, "Opening Details ", Snackbar.LENGTH_LONG).show();
        }
    }

    public TextView getNameTextView() {
        return nameTextView;
    }

    public TextView getLotsTextView() {
        return lotsTextView;
    }

    public TextView getExpiringTextView() {
        return expiringTextView;
    }

    public TextView getDispensableTextView() {
        return dispensableTextView;
    }
}
