package org.smartregister.stock.openlmis.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.domain.openlmis.CommodityType;
import org.smartregister.stock.openlmis.listener.ExpandCollapseListener;
import org.smartregister.stock.openlmis.presenter.StockListPresenter;
import org.smartregister.stock.openlmis.view.viewholder.CommodityTypeViewHolder;
import org.smartregister.stock.openlmis.wrapper.TradeItemWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by samuelgithengi on 7/13/18.
 */
public class ListCommodityTypeAdapter extends RecyclerView.Adapter<CommodityTypeViewHolder> {

    private final StockListPresenter stockListPresenter;
    private List<CommodityType> commodityTypes;

    private List<ExpandCollapseListener> expandCollapseListeners = new ArrayList<>();

    private Map<String, Set<String>> searchedIds;

    private Map<String, Set<String>> programIds;

    private Context context;

    private String programId;

    public ListCommodityTypeAdapter(StockListPresenter stockListPresenter, Context context) {
        this.stockListPresenter = stockListPresenter;
        this.commodityTypes = new ArrayList<>();
        this.context = context;

    }

    public void filterCommodityTypes(String searchPhrase) {
        if (StringUtils.isBlank(searchPhrase)) {
            if (programIds != null) {
                commodityTypes = stockListPresenter.findCommodityTypesByIds(programIds.keySet());
            }
            searchedIds = null;
            notifyDataSetChanged();
            collapseAllViews();
        } else {
            searchedIds = stockListPresenter.filterValidPrograms(programIds,
                    stockListPresenter.searchIds(searchPhrase));
            commodityTypes = stockListPresenter.findCommodityTypesByIds(searchedIds.keySet());
            notifyDataSetChanged();
            expandAllViews();
        }
    }

    @NonNull
    @Override
    public CommodityTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.commodity_type_row, parent, false);
        CommodityTypeViewHolder holder = new CommodityTypeViewHolder(itemView);
        expandCollapseListeners.add(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommodityTypeViewHolder holder, int position) {
        CommodityType commodityType = commodityTypes.get(position);
        List<TradeItemWrapper> tradeItems;
        if (searchedIds != null) {
            tradeItems = stockListPresenter.findTradeItemsByIds(searchedIds.get(commodityType.getId()));
        } else if (programIds != null) {
            tradeItems = stockListPresenter.findTradeItemsByIds(programIds.get(commodityType.getId()));
        } else {
            tradeItems = stockListPresenter.getTradeItems(commodityType);
        }
        int totalDoses = 0;
        for (TradeItemWrapper tradeItem : tradeItems)
            totalDoses += tradeItem.getTotalStock() * tradeItem.getTradeItem().getNetContent();
        holder.getCommodityTypeTextView().setText(context.getString(R.string.commodity_type_formatter,
                commodityType.getName(), tradeItems.size()));
        holder.getDoseTextView().setText(context.getString(R.string.dose_formatter, totalDoses));
        holder.getTradeItemsRecyclerView().setAdapter(new ListTradeItemAdapter(tradeItems, programId, context));
    }

    @Override
    public int getItemCount() {
        return commodityTypes.size();
    }

    public void expandAllViews() {
        for (ExpandCollapseListener listener : expandCollapseListeners)
            listener.expandView();
    }

    public void collapseAllViews() {
        for (ExpandCollapseListener listener : expandCollapseListeners)
            listener.collapseView();
    }

    public void registerExpandCollapseListeners(ExpandCollapseListener listener) {
        expandCollapseListeners.add(listener);
    }

    public void setProgramId(String programId) {
        this.programId = programId;
        if (programId == null)
            commodityTypes = new ArrayList<>();
        else {
            programIds = stockListPresenter.searchIdsByPrograms(programId);
            commodityTypes = stockListPresenter.findCommodityTypesByIds(programIds.keySet());
        }
    }

    public String getProgramId() {
        return programId;
    }

    public void refresh() {
        if (programId != null)
            setProgramId(programId);
        notifyDataSetChanged();
    }
}
