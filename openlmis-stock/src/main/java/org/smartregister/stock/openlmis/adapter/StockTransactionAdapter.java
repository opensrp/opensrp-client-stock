package org.smartregister.stock.openlmis.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.domain.Stock;
import org.smartregister.stock.openlmis.presenter.StockDetailsPresenter;
import org.smartregister.stock.openlmis.view.viewholder.StockTransactionsViewHolder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.smartregister.stock.domain.Stock.issued;
import static org.smartregister.stock.domain.Stock.loss_adjustment;
import static org.smartregister.stock.domain.Stock.received;

/**
 * Created by samuelgithengi on 8/1/18.
 */
public class StockTransactionAdapter extends RecyclerView.Adapter<StockTransactionsViewHolder> {

    static SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");

    private List<Stock> stockTransactions;

    private StockDetailsPresenter stockDetailsPresenter;

    private int balance = 0;

    public StockTransactionAdapter(String tradeItemId, StockDetailsPresenter stockDetailsPresenter) {
        this.stockDetailsPresenter = stockDetailsPresenter;
        stockTransactions = stockDetailsPresenter.getStockByTradeItem(tradeItemId);
        stockTransactions = stockDetailsPresenter.populateLotNames(tradeItemId, stockTransactions);
    }

    @NonNull
    @Override
    public StockTransactionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transactions_row, parent, false);
        return new StockTransactionsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StockTransactionsViewHolder holder, int position) {
        Stock stock = stockTransactions.get(position);
        holder.getDateTextView().setText(dateFormatter.format(new Date(stock.getDateCreated())));
        holder.getToFromTextView().setText(stock.getToFrom());
        holder.getLotCodeTextView().setText(stock.getLotCode());
        if (stock.getTransactionType().equals(received)) {
            holder.getReceivedTextView().setText(String.valueOf(stock.getValue()));
        } else if (stock.getTransactionType().equals(issued)) {
            holder.getIssuedTextView().setText(String.valueOf(Math.abs(stock.getValue())));
        } else if (stock.getTransactionType().equals(loss_adjustment)) {
            holder.getAdjustmentTextView().setText(String.valueOf(stock.getValue()));
        }
        holder.getBalanceTextView().setText(String.valueOf(balance));
    }

    @Override
    public int getItemCount() {
        return stockTransactions.size();
    }
}
