package org.smartregister.stock.openlmis.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.domain.openlmis.CommodityType;
import org.smartregister.stock.openlmis.presenter.StockTakePresenter;
import org.smartregister.stock.openlmis.view.viewholder.StockTakeCommodityTypeViewHolder;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by samuelgithengi on 9/20/18.
 */
public class StockTakeCommodityTypeAdapter extends RecyclerView.Adapter<StockTakeCommodityTypeViewHolder> {

    private StockTakePresenter stockTakePresenter;

    private List<CommodityType> commodityTypeList;

    public StockTakeCommodityTypeAdapter(StockTakePresenter stockTakePresenter, String programId) {
        this.stockTakePresenter = stockTakePresenter;
        Map<String, Set<String>> ids = stockTakePresenter.searchIdsByPrograms(programId);
        this.commodityTypeList = stockTakePresenter.findCommodityTypesByIds(ids.keySet());
    }

    @NonNull
    @Override
    public StockTakeCommodityTypeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(
                R.layout.stock_take_commodity_type_row, viewGroup, false);
        return new StockTakeCommodityTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StockTakeCommodityTypeViewHolder stockTakeCommodityTypeViewHolder, int position) {
        CommodityType commodityType = commodityTypeList.get(position);
        stockTakeCommodityTypeViewHolder.setCommodityTypeName(commodityType.getName());
        StockTakeTradeItemAdapter adapter = new StockTakeTradeItemAdapter(stockTakePresenter, commodityType.getId());
        stockTakeCommodityTypeViewHolder.getTradeItemRecyclerView().setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return commodityTypeList.size();
    }
}
