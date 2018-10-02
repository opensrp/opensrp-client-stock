package org.smartregister.stock.openlmis.view.viewholder.stocktake;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.domain.openlmis.Lot;

import static org.smartregister.stock.openlmis.adapter.LotAdapter.DATE_FORMAT;

/**
 * Created by samuelgithengi on 9/21/18.
 */
public class StockTakeLotViewHolder extends BaseStockTakeViewHolder {

    private TextView lotCodeAndExpiryTextView;


    public StockTakeLotViewHolder(@NonNull View itemView) {
        super(itemView);
        lotCodeAndExpiryTextView = itemView.findViewById(R.id.lot_code);
    }

    public void setLot(Lot lot) {
        lotCodeAndExpiryTextView.setText(context.getString(R.string.stock_take_lot,
                lot.getLotCode(), new DateTime(lot.getExpirationDate()).toString(DATE_FORMAT)));
        stockTake.setLotId(lot.getId());
    }

}