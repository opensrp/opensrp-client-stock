package org.smartregister.stock.openlmis.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.widgets.DatePickerFactory;

import org.json.JSONObject;
import org.smartregister.stock.openlmis.R;

import static org.smartregister.stock.util.Constants.BACKGROUND;
import static org.smartregister.stock.util.Constants.UNDERLINE_COLOR;

/**
 * Created by samuelgithengi on 8/16/18.
 */
public class OpenLMISDatePickerFactory extends DatePickerFactory {

    @Override
    protected void attachJson(String stepName, Context context, JsonFormFragment formFragment,
                              final JSONObject jsonObject, final MaterialEditText editText, TextView duration) {
        super.attachJson(stepName, context, formFragment, jsonObject, editText, duration);
        editText.setFloatingLabelText(jsonObject.optString("hint"));
        String background = jsonObject.optString(BACKGROUND);
        if (!background.isEmpty())
            editText.setBackgroundColor(Color.parseColor(background));

        String underlineColor = jsonObject.optString(UNDERLINE_COLOR);
        if (!underlineColor.isEmpty())
            editText.setUnderlineColor(Color.parseColor(underlineColor));
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    editText.setFloatingLabel(MaterialEditText.FLOATING_LABEL_NONE);
                } else {
                    editText.setFloatingLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    protected int getLayout() {
        return R.layout.openlmis_native_form_item_date_picker;
    }


}
