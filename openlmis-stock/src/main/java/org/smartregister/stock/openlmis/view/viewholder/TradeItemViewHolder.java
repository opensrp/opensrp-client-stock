package org.smartregister.stock.openlmis.view.viewholder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.domain.TradeItem;
import org.smartregister.stock.openlmis.dto.TradeItemDto;
import org.smartregister.stock.openlmis.util.OpenLMISConstants;
import org.smartregister.stock.openlmis.view.StockDetailsActivity;

/**
 * Created by samuelgithengi on 7/17/18.
 */
public class TradeItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private Context context;
    private TextView nameTextView;
    private TextView lotsTextView;
    private TextView expiringTextView;
    private TextView dispensableTextView;

    private TradeItem tradeItem;


    public TradeItemViewHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        nameTextView = itemView.findViewById(R.id.nameTextView);
        lotsTextView = itemView.findViewById(R.id.lotsTextView);
        expiringTextView = itemView.findViewById(R.id.expiringTextView);
        dispensableTextView = itemView.findViewById(R.id.dispensableTextView);
        itemView.findViewById(R.id.tradeItemsMore).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tradeItemsMore) {
            Intent intent = new Intent(context, StockDetailsActivity.class);
            TradeItemDto tradeItemDto = new TradeItemDto(tradeItem.getId(),
                    tradeItem.getName(), dispensableTextView.getText().toString(),
                    tradeItem.getDateUpdated(), tradeItem.getNumberOfLots(), tradeItem.getDispensable().getKeyDispensingUnit());
            intent.putExtra(OpenLMISConstants.tradeItem, tradeItemDto);
            context.startActivity(intent);
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

    public void setTradeItem(TradeItem tradeItem) {
        this.tradeItem = tradeItem;
    }
}
