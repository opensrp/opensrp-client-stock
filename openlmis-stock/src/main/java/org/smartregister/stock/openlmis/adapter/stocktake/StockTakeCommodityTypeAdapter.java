package org.smartregister.stock.openlmis.adapter.stocktake;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.domain.openlmis.CommodityType;
import org.smartregister.stock.openlmis.presenter.StockTakePresenter;
import org.smartregister.stock.openlmis.view.viewholder.stocktake.StockTakeCommodityTypeViewHolder;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by samuelgithengi on 9/20/18.
 */
public class StockTakeCommodityTypeAdapter extends RecyclerView.Adapter<StockTakeCommodityTypeViewHolder> {

    private StockTakePresenter stockTakePresenter;

    private List<CommodityType> commodityTypeList;

    private String programId;

    private Map<String, Set<String>> programIds;

    private Map<String, Set<String>> searchedIds;

    public StockTakeCommodityTypeAdapter(StockTakePresenter stockTakePresenter, String programId) {
        this.stockTakePresenter = stockTakePresenter;
        this.programId = programId;
        programIds = stockTakePresenter.searchIdsByPrograms(programId);
        this.commodityTypeList = stockTakePresenter.findCommodityTypesWithActiveLots(programIds.keySet());
        stockTakePresenter.iniatializeBottomPanel(programId, programIds.keySet());
    }

    public void filterCommodityTypes(String searchPhrase) {
        if (StringUtils.isBlank(searchPhrase)) {
            if (programIds != null) {
                commodityTypeList = stockTakePresenter.findCommodityTypesByIds(programIds.keySet());
            }
            searchedIds = null;
            notifyDataSetChanged();
        } else {
            searchedIds = stockTakePresenter.filterValidPrograms(programIds,
                    stockTakePresenter.searchIds(searchPhrase));
            commodityTypeList = stockTakePresenter.findCommodityTypesWithActiveLots(searchedIds.keySet());
            notifyDataSetChanged();
        }
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
        StockTakeTradeItemAdapter adapter = new StockTakeTradeItemAdapter(stockTakePresenter, programId,
                commodityType.getId(), searchedIds == null ? null : searchedIds.get(commodityType.getId()));
        stockTakeCommodityTypeViewHolder.getTradeItemRecyclerView().setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return commodityTypeList.size();
    }
}
