package org.smartregister.stock.openlmis.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.utils.ValidationStatus;
import com.vijay.jsonwizard.views.JsonFormFragmentView;

import org.json.JSONObject;
import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.widget.customviews.CustomTextInputEditText;

import static com.vijay.jsonwizard.constants.JsonFormConstants.DATE_PICKER;

/**
 * Created by samuelgithengi on 8/16/18.
 */
public class OpenLMISDatePickerFactory extends DatePickerFactory {

    @Override
    protected void attachJson(final String stepName, Context context, final JsonFormFragment formFragment,
                              final JSONObject jsonObject, final CustomTextInputEditText editText, TextView duration) {
        super.attachJson(stepName, context, formFragment, jsonObject, editText, duration);
        editText.setTag(com.vijay.jsonwizard.R.id.type, DATE_PICKER);
        editText.setHint(jsonObject.optString("hint"));
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {//do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String key = (String) editText.getTag(com.vijay.jsonwizard.R.id.key);
                String openMrsEntityParent = (String) editText.getTag(com.vijay.jsonwizard.R.id.openmrs_entity_parent);
                String openMrsEntity = (String) editText.getTag(com.vijay.jsonwizard.R.id.openmrs_entity);
                String openMrsEntityId = (String) editText.getTag(com.vijay.jsonwizard.R.id.openmrs_entity_id);
                formFragment.writeValue(stepName, key, s.toString(), openMrsEntityParent, openMrsEntity, openMrsEntityId, false);
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

    public static ValidationStatus validate(JsonFormFragmentView formFragmentView,
                                            CustomTextInputEditText editText) {
        if (editText.isEnabled()) {
            boolean validate = editText.validate();
            if (!validate) {
                return new ValidationStatus(false, editText.getError().toString(), formFragmentView, editText);
            }
        }
        return new ValidationStatus(true, null, formFragmentView, editText);
    }


}
