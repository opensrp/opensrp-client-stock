package org.smartregister.stock.openlmis.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.view.viewholder.ReviewViewHolder;
import org.smartregister.stock.openlmis.widget.helper.LotDto;

import java.util.List;

/**
 * Created by samuelgithengi on 8/27/18.
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewViewHolder> {

    private String tradeItem;
    private List<LotDto> lotDtoList;

    public ReviewAdapter(String tradeItem, List<LotDto> lotDtoList) {
        this.tradeItem = tradeItem;
        this.lotDtoList = lotDtoList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(
                R.layout.openlmis_native_form_item_review_row, viewGroup, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder reviewViewHolder, int position) {
        LotDto lot = lotDtoList.get(position);
        reviewViewHolder.getTradeItemTextView().setText(tradeItem);
        reviewViewHolder.getLotCodeTextView().setText(lot.getLotCodeAndExpiry());
        reviewViewHolder.getQuantityTextView().setText(String.valueOf(lot.getQuantity()));
        reviewViewHolder.getStatusTextView().setText(lot.getLotStatus());
    }

    @Override
    public int getItemCount() {
        return lotDtoList.size();
    }
}
