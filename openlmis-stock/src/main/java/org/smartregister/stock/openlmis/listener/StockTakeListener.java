package org.smartregister.stock.openlmis.listener;

import org.smartregister.stock.openlmis.widget.helper.LotDto;

/**
 * Created by samuelgithengi on 9/25/18.
 */
public interface StockTakeListener {

    void registerLotDetails(LotDto lotDto);

}
