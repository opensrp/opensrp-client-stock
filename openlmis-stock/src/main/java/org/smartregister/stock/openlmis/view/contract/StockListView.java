package org.smartregister.stock.openlmis.view.contract;

import android.view.View;

import org.smartregister.stock.openlmis.dto.TradeItemDto;

/**
 * Created by samuelgithengi on 7/16/18.
 */
public interface StockListView {

    void showStockActionMenu(View view);

    void startStockDetails(TradeItemDto tradeItemDto);
}
