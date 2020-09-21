package org.smartregister.stock.widget;

import android.content.Context;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.widgets.DatePickerFactory;

import org.json.JSONObject;
import org.smartregister.stock.R;

/**
 * Created by Ephraim Kigamba - ekigamba@ona.io on 12/04/2018.
 */

public class StockDatePickerFactory extends DatePickerFactory {

    @Override
    protected void attachLayout(String stepName, Context context, JsonFormFragment formFragment, JSONObject jsonObject, MaterialEditText editText, TextView duration) {
        super.attachLayout(stepName, context, formFragment, jsonObject, editText, duration);
//        editText.setHintTextColor(context.getResources().getColor(R.color.text_black));
    }

    @Override
    protected int getLayout() {
        return R.layout.stock_native_form_item_date_picker;
    }
}
