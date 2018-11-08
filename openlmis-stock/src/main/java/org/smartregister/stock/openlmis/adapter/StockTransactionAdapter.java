package org.smartregister.stock.openlmis.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.domain.Stock;
import org.smartregister.stock.openlmis.dto.TradeItemDto;
import org.smartregister.stock.openlmis.presenter.StockDetailsPresenter;
import org.smartregister.stock.openlmis.view.viewholder.StockTransactionsViewHolder;
import org.smartregister.stock.openlmis.wrapper.StockWrapper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.smartregister.stock.domain.Stock.issued;
import static org.smartregister.stock.domain.Stock.loss_adjustment;
import static org.smartregister.stock.domain.Stock.received;

/**
 * Created by samuelgithengi on 8/1/18.
 */
public class StockTransactionAdapter extends RecyclerView.Adapter<StockTransactionsViewHolder> {

    protected static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

    private List<StockWrapper> stockTransactions;

    private boolean hasLots;

    public StockTransactionAdapter(TradeItemDto tradeItem, StockDetailsPresenter stockDetailsPresenter) {
        this.hasLots = tradeItem.isHasLots();
        stockTransactions = stockDetailsPresenter.populateLotNamesAndBalance(tradeItem,
                stockDetailsPresenter.findStockByTradeItem(tradeItem.getId()));
    }

    @NonNull
    @Override
    public StockTransactionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transactions_row, parent, false);
        return new StockTransactionsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StockTransactionsViewHolder holder, int position) {
        StockWrapper stockWrapper = stockTransactions.get(position);
        Stock stock = stockWrapper.getStock();
        holder.getDateTextView().setText(dateFormatter.format(new Date(stock.getDateCreated())));
        holder.getToFromTextView().setText(stockWrapper.getFacility());
        holder.getLotCodeTextView().setText(stockWrapper.getLotCode());
        holder.getBalanceTextView().setText(String.valueOf(stockWrapper.getStockBalance()));
        if (stock.getTransactionType().equals(received)) {
            holder.getReceivedTextView().setText(String.valueOf(stock.getValue()));
            holder.getIssuedTextView().setText("");
            holder.getAdjustmentTextView().setText("");
        } else if (stock.getTransactionType().equals(issued)) {
            holder.getIssuedTextView().setText(String.valueOf(Math.abs(stock.getValue())));
            holder.getReceivedTextView().setText("");
            holder.getAdjustmentTextView().setText("");
        } else if (stock.getTransactionType().equals(loss_adjustment)) {
            holder.getAdjustmentTextView().setText(String.valueOf(stock.getValue()));
            holder.getReceivedTextView().setText("");
            holder.getIssuedTextView().setText("");
            if (StringUtils.isNotBlank(stockWrapper.getReason())) {
                holder.getToFromTextView().setText(stockWrapper.getReason());
            } else {
                holder.getToFromTextView().setText(stockWrapper.getStock().getToFrom());
            }
        }

        if (!hasLots)
            holder.getLotCodeTextView().setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return stockTransactions.size();
    }
}
