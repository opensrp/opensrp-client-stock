package org.smartregister.stock.openlmis.view.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.smartregister.stock.openlmis.R;

/**
 * Created by samuelgithengi on 8/1/18.
 */
public class StockTransactionsViewHolder extends RecyclerView.ViewHolder {

    private TextView dateTextView;

    private TextView ToFromTextView;

    private TextView lotCodeTextView;

    private TextView receivedTextView;

    private TextView issuedTextView;

    private TextView adjustmentTextView;

    private TextView balanceTextView;

    public StockTransactionsViewHolder(View itemView) {
        super(itemView);
        dateTextView = itemView.findViewById(R.id.date);
        ToFromTextView = itemView.findViewById(R.id.to_from);
        lotCodeTextView = itemView.findViewById(R.id.lot_code);
        receivedTextView = itemView.findViewById(R.id.received);
        issuedTextView = itemView.findViewById(R.id.issued);
        adjustmentTextView = itemView.findViewById(R.id.loss_adj);
        balanceTextView = itemView.findViewById(R.id.balance);
    }

    public TextView getDateTextView() {
        return dateTextView;
    }

    public TextView getToFromTextView() {
        return ToFromTextView;
    }

    public TextView getLotCodeTextView() {
        return lotCodeTextView;
    }

    public TextView getReceivedTextView() {
        return receivedTextView;
    }

    public TextView getIssuedTextView() {
        return issuedTextView;
    }

    public TextView getAdjustmentTextView() {
        return adjustmentTextView;
    }

    public TextView getBalanceTextView() {
        return balanceTextView;
    }
}
