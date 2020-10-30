package org.smartregister.stock.openlmis.view.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import org.smartregister.stock.openlmis.R;

/**
 * Created by samuelgithengi on 8/27/18.
 */
public class ReviewViewHolder extends ViewHolder {

    private TextView tradeItemTextView;
    private TextView lotCodeTextView;
    private TextView quantityTextView;
    private TextView statusTextView;

    public ReviewViewHolder(@NonNull View itemView) {
        super(itemView);
        tradeItemTextView = itemView.findViewById(R.id.trade_item);
        lotCodeTextView = itemView.findViewById(R.id.lot_code);
        quantityTextView = itemView.findViewById(R.id.lot_quantity);
        statusTextView = itemView.findViewById(R.id.lot_status);
    }

    public TextView getTradeItemTextView() {
        return tradeItemTextView;
    }

    public TextView getLotCodeTextView() {
        return lotCodeTextView;
    }

    public TextView getQuantityTextView() {
        return quantityTextView;
    }

    public TextView getStatusTextView() {
        return statusTextView;
    }
}
