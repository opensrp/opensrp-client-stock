package org.smartregister.stock.openlmis.view.viewholder;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.smartregister.stock.openlmis.R;

/**
 * Created by samuelgithengi on 9/21/18.
 */
public class StockTakeLotViewHolder extends RecyclerView.ViewHolder {

    private TextView lotCodeAndExpiryTextView;

    private TextInputEditText stockOnHandTextView;

    private TextInputEditText physicalCountTextView;

    private TextInputEditText statusTextView;

    public StockTakeLotViewHolder(@NonNull View itemView) {
        super(itemView);
        lotCodeAndExpiryTextView = itemView.findViewById(R.id.lot_code);
        stockOnHandTextView = itemView.findViewById(R.id.stock_on_hand_textview);
        physicalCountTextView = itemView.findViewById(R.id.quantity_textview);
        statusTextView = itemView.findViewById(R.id.status_textview);
    }

    public void setLotCodeAndExpiry(String lotCodeAndExpiry) {
        lotCodeAndExpiryTextView.setText(lotCodeAndExpiry);
    }

    public void setStockOnHand(int stockOnHand) {
        stockOnHandTextView.setText(String.valueOf(stockOnHand));
    }

    public void setPhysicalCount(int physicalCount) {
        physicalCountTextView.setText(String.valueOf(physicalCount));
    }

    public void setStatus(String status) {
        statusTextView.setText(status);
    }
}