package org.smartregister.stock.widget;

import com.vijay.jsonwizard.widgets.DatePickerFactory;

import org.smartregister.stock.R;

/**
 * Created by Ephraim Kigamba - ekigamba@ona.io on 12/04/2018.
 */

public class StockDatePickerFactory extends DatePickerFactory {

    @Override
    protected int getLayout() {
        return R.layout.stock_native_form_item_date_picker;
    }
}
