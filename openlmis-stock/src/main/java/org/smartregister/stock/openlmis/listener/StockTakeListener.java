package org.smartregister.stock.openlmis.listener;

import org.smartregister.stock.openlmis.domain.StockTake;

/**
 * Created by samuelgithengi on 9/25/18.
 */
public interface StockTakeListener {

    void registerStockTake(StockTake stockTake);

}
