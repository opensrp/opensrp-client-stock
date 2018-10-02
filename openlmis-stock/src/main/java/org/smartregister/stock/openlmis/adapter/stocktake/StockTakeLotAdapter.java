package org.smartregister.stock.openlmis.adapter.stocktake;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.domain.StockTake;
import org.smartregister.stock.openlmis.domain.openlmis.Lot;
import org.smartregister.stock.openlmis.domain.openlmis.Reason;
import org.smartregister.stock.openlmis.listener.StockTakeListener;
import org.smartregister.stock.openlmis.presenter.StockTakePresenter;
import org.smartregister.stock.openlmis.view.viewholder.stocktake.StockTakeLotViewHolder;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by samuelgithengi on 9/21/18.
 */
public class StockTakeLotAdapter extends RecyclerView.Adapter<StockTakeLotViewHolder> {

    private StockTakeListener stockTakeListener;

    private List<Lot> lots;

    private List<Reason> adjustReasons;

    private String commodityTypeId;

    private Set<StockTake> stockTakeList;

    private String programId;

    private String tradeItemId;

    private Map<String, Integer> stockBalances;


    public StockTakeLotAdapter(StockTakePresenter stockTakePresenter, String programId,
                               String commodityTypeId, String tradeItemId, Set<StockTake> stockTakeList,
                               StockTakeListener stockTakeListener) {
        this.commodityTypeId = commodityTypeId;
        this.stockTakeList = stockTakeList;
        this.stockTakeListener = stockTakeListener;
        this.programId = programId;
        this.tradeItemId = tradeItemId;
        lots = stockTakePresenter.findLotsByTradeItem(tradeItemId);
        adjustReasons = stockTakePresenter.findAdjustReasons(programId);
        stockBalances = stockTakePresenter.findStockBalanceByLots(programId, lots);
    }


    @NonNull
    @Override
    public StockTakeLotViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(
                R.layout.stock_take_lot_row, viewGroup, false);
        return new StockTakeLotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StockTakeLotViewHolder stockTakeLotViewHolder, int position) {
        stockTakeLotViewHolder.setStockAdjustReasons(adjustReasons);
        stockTakeLotViewHolder.setStockTakeListener(stockTakeListener);
        Lot lot = lots.get(position);
        int stockOnHand = 0;
        if (stockBalances.containsKey(lot.getId()))
            stockOnHand = stockBalances.get(lot.getId());
        stockTakeLotViewHolder.setStockOnHand(stockOnHand);
        for (StockTake stockTake : stockTakeList) {
            if (lot.getId().equals(stockTake.getLotId())) {
                stockTakeLotViewHolder.setStockTake(stockTake);
                stockTakeLotViewHolder.setDifference(stockTake.getQuantity());
                stockTakeLotViewHolder.setStatus(stockTake.getStatus());
                stockTakeLotViewHolder.setReason(stockTake.getReasonId());
                stockTakeLotViewHolder.setPhysicalCount(stockOnHand + stockTake.getQuantity());
                stockTakeLotViewHolder.activateNoChange(stockTake.isNoChange());
                validateStockTake(stockOnHand, stockTake);
                stockTakeListener.registerStockTake(stockTake);
                break;
            }
        }
        if (stockTakeLotViewHolder.getStockTake() == null) {
            StockTake stockTake = new StockTake(programId, commodityTypeId, tradeItemId, lot.getId());
            stockTake.setLastUpdated(System.currentTimeMillis());
            stockTakeLotViewHolder.setStockTake(stockTake);
            stockTakeLotViewHolder.setPhysicalCount(stockOnHand);
            stockTakeLotViewHolder.setStatus(lot.getLotStatus());
        }
        stockTakeLotViewHolder.setLot(lot);
    }

    @Override
    public int getItemCount() {
        return lots.size();
    }

    private void validateStockTake(int stockOnHand, StockTake stockTake) {
        if (stockTake.isNoChange()) {
            stockTake.setValid(true);
        } else if (stockTake.getQuantity() - stockOnHand < 0 || stockTake.getQuantity() == 0 ||
                StringUtils.isBlank(stockTake.getReasonId()) ||
                StringUtils.isBlank(stockTake.getStatus())) {
            stockTake.setValid(false);
        } else {
            stockTake.setValid(true);
        }
    }
}