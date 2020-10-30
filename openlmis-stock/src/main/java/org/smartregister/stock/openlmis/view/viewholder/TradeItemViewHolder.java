package org.smartregister.stock.openlmis.view.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.domain.TradeItem;
import org.smartregister.stock.openlmis.dto.TradeItemDto;
import org.smartregister.stock.openlmis.presenter.StockListPresenter;
import org.smartregister.stock.openlmis.wrapper.TradeItemWrapper;

/**
 * Created by samuelgithengi on 7/17/18.
 */
public class TradeItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final StockListPresenter stockListPresenter;

    private Context context;
    private TextView nameTextView;
    private TextView lotsTextView;
    private TextView expiringTextView;
    private TextView dispensableTextView;

    private TradeItemWrapper tradeItemWrapper;

    private String programId;


    public TradeItemViewHolder(StockListPresenter stockListPresenter, View itemView) {
        super(itemView);
        this.stockListPresenter = stockListPresenter;
        context = itemView.getContext();
        nameTextView = itemView.findViewById(R.id.nameTextView);
        lotsTextView = itemView.findViewById(R.id.lotsTextView);
        expiringTextView = itemView.findViewById(R.id.expiringTextView);
        dispensableTextView = itemView.findViewById(R.id.dispensableTextView);
        itemView.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        TradeItem tradeItem = tradeItemWrapper.getTradeItem();
        TradeItemDto tradeItemDto = new TradeItemDto(tradeItem.getId(),
                tradeItem.getName(), tradeItemWrapper.getTotalStock(),
                tradeItem.getDateUpdated(), tradeItemWrapper.getNumberOfLots(),
                tradeItem.getDispensable().getKeyDispensingUnit(), tradeItem.getNetContent(),
                programId, tradeItem.isHasLots(), tradeItem.isUseVvm(),
                tradeItem.getDispensable().getKeyRouteOfAdministration());
        stockListPresenter.startStockDetailsActivity(tradeItemDto);
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

    public void setTradeItemWrapper(TradeItemWrapper tradeItemWrapper) {
        this.tradeItemWrapper = tradeItemWrapper;
    }

    public Context getContext() {
        return context;
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }
}
