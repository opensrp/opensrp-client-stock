package org.smartregister.stock.openlmis.view.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.smartregister.stock.openlmis.R;

/**
 * Created by samuelgithengi on 8/1/18.
 */
public class LotViewHolder extends RecyclerView.ViewHolder {

    private TextView lotCodeTextView;

    private TextView stockOnHandTextView;

    private TextView statusTextView;

    public LotViewHolder(View itemView) {
        super(itemView);
        lotCodeTextView=itemView.findViewById(R.id.lot_code);
        stockOnHandTextView=itemView.findViewById(R.id.stock_on_hand);
        statusTextView=itemView.findViewById(R.id.lot_status);
    }

    public TextView getLotCodeTextView() {
        return lotCodeTextView;
    }

    public TextView getStockOnHandTextView() {
        return stockOnHandTextView;
    }

    public TextView getStatusTextView() {
        return statusTextView;
    }
}
