package org.smartregister.stock.openlmis.view.contract;

import java.util.Date;

/**
 * Created by samuelgithengi on 9/27/18.
 */
public interface StockTakeView extends BaseView {

    void updateTotalTradeItems(int totalTradeItems);

    void updateTradeItemsAdjusted(int tradeItemsAdjusted, Date lastChanged);

    void activateSubmit();

    void onStockTakeCompleted();
}
