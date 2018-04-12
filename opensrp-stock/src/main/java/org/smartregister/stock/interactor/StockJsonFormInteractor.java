package org.smartregister.stock.interactor;

import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.interactors.JsonFormInteractor;

import org.smartregister.stock.widget.StockDatePickerFactory;
import org.smartregister.stock.widget.StockEditTextFactory;

/**
 * Created by samuelgithengi on 2/26/18.
 */

public class StockJsonFormInteractor extends JsonFormInteractor {

    private static final JsonFormInteractor INSTANCE = new StockJsonFormInteractor();

    private StockJsonFormInteractor() {
        super();
    }

    @Override
    protected void registerWidgets() {
        super.registerWidgets();
        map.put(JsonFormConstants.EDIT_TEXT, new StockEditTextFactory());
        map.put(JsonFormConstants.DATE_PICKER, new StockDatePickerFactory());
    }

    public static JsonFormInteractor getInstance() {
        return INSTANCE;
    }
}
