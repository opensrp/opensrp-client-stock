package org.smartregister.stock.openlmis.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interfaces.CommonListener;
import com.vijay.jsonwizard.utils.ValidationStatus;
import com.vijay.jsonwizard.views.JsonFormFragmentView;

import org.json.JSONObject;
import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.widget.customviews.CustomTextInputEditText;

import java.util.List;

import static com.vijay.jsonwizard.constants.JsonFormConstants.DATE_PICKER;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.JsonForm.IS_NON_LOT;

/**
 * Created by samuelgithengi on 8/16/18.
 */
public class OpenLMISDatePickerFactory extends DatePickerFactory {

    private boolean isLotEnabled = true;

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
                formFragment.writeValue(stepName, key, s.toString(), openMrsEntityParent,
                        openMrsEntity, openMrsEntityId);

            }

            @Override
            public void afterTextChanged(Editable s) {//do nothing
            }
        });
    }

    @Override
    public List<View> getViewsFromJson(String stepName, Context context, JsonFormFragment formFragment, JSONObject jsonObject, CommonListener listener) {
        List<View> views = super.getViewsFromJson(stepName, context, formFragment, jsonObject, listener);
        if (jsonObject.optBoolean(IS_NON_LOT)) {
            isLotEnabled = false;
        }
        return views;
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
