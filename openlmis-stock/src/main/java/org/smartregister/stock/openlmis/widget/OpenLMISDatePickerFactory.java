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

import static org.smartregister.stock.openlmis.util.OpenLMISConstants.JsonForm.BACKGROUND;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.JsonForm.UNDERLINE_COLOR;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.JsonForm.VALUE;

/**
 * Created by samuelgithengi on 8/16/18.
 */
public class OpenLMISDatePickerFactory extends DatePickerFactory {

    @Override
    protected void attachJson(final String stepName, Context context, final JsonFormFragment formFragment,
                              final JSONObject jsonObject, final MaterialEditText editText, TextView duration) {
        super.attachJson(stepName, context, formFragment, jsonObject, editText, duration);
        editText.setFloatingLabelText(jsonObject.optString("hint"));
        if (!jsonObject.optString(VALUE).isEmpty()) {
            editText.setFloatingLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
            editText.setFloatingLabelAlwaysShown(true);
        }
        String background = jsonObject.optString(BACKGROUND);
        if (!background.isEmpty())
            editText.setBackgroundColor(Color.parseColor(background));

        String underlineColor = jsonObject.optString(UNDERLINE_COLOR);
        if (!underlineColor.isEmpty())
            editText.setUnderlineColor(Color.parseColor(underlineColor));
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {//do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    editText.setFloatingLabel(MaterialEditText.FLOATING_LABEL_NONE);
                } else {
                    editText.setFloatingLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
                }
                String key = (String) editText.getTag(com.vijay.jsonwizard.R.id.key);
                String openMrsEntityParent = (String) editText.getTag(com.vijay.jsonwizard.R.id.openmrs_entity_parent);
                String openMrsEntity = (String) editText.getTag(com.vijay.jsonwizard.R.id.openmrs_entity);
                String openMrsEntityId = (String) editText.getTag(com.vijay.jsonwizard.R.id.openmrs_entity_id);
                formFragment.writeValue(stepName, key, s.toString(), openMrsEntityParent,
                        openMrsEntity, openMrsEntityId);

            }

            @Override
            public void afterTextChanged(Editable s) {//do nothing
            }
        });
    }

    @Override
    protected int getLayout() {
        return R.layout.openlmis_native_form_item_date_picker;
    }


}
