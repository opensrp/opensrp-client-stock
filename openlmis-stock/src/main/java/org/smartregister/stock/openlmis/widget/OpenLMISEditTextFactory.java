package org.smartregister.stock.openlmis.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;

import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interfaces.CommonListener;

import org.json.JSONArray;
import org.json.JSONObject;
import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.widget.customviews.CustomTextInputEditText;

import java.util.List;

import static com.vijay.jsonwizard.constants.JsonFormConstants.EDIT_TEXT;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.JsonForm.LIST_OPTIONS;

public class OpenLMISEditTextFactory extends EditTextFactory {

    private JSONArray listOptions;

    @Override
    public List<View> getViewsFromJson(String stepName, Context context, JsonFormFragment formFragment, JSONObject jsonObject, CommonListener listener) throws Exception {
        if (jsonObject.has("list_options")) {
            listOptions = jsonObject.getJSONArray(LIST_OPTIONS);
        }
        super.setMainLayout(getLayout());
        super.setEditTextId(R.id.openlmis_edit_text);

        List<View> views = super.getViewsFromJson(stepName, context, formFragment, jsonObject, listener);

        RelativeLayout rootLayout =  (RelativeLayout) views.get(0);
        if (jsonObject.has("is_spinnable")) {
            boolean isSpinnable = (boolean) jsonObject.get("is_spinnable");
            if (isSpinnable) {
                CustomTextInputEditText dropDown = (CustomTextInputEditText) ((TextInputLayout) rootLayout.findViewById(R.id.openlmis_edit_text_parent)).getEditText();
                Drawable spinner = context.getResources().getDrawable(R.drawable.abc_spinner_mtrl_am_alpha);
                spinner.setColorFilter(Color.parseColor("#9A9A9A"), PorterDuff.Mode.SRC_ATOP);
                dropDown.setCompoundDrawablesWithIntrinsicBounds(null, null, spinner, null);
                dropDown.setFocusable(false);
                populateStatusOptions(context, dropDown);
            }
        }
        return views;
    }

    @Override
    protected void attachJson(final String stepName, Context context, final JsonFormFragment formFragment,
                              final JSONObject jsonObject, final CustomTextInputEditText editText) throws Exception {

        super.attachJson(stepName, context, formFragment, jsonObject, editText);
        editText.setTag(com.vijay.jsonwizard.R.id.type, EDIT_TEXT);
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

    @VisibleForTesting
    protected PopupMenu populateStatusOptions(final Context context, final CustomTextInputEditText editText) {
        // TODO: Make this dynamic and react to onclick correctly
        final PopupMenu popupMenu = new PopupMenu(context, editText);
        for (int i = 0; i < listOptions.length(); i++) {
            popupMenu.getMenu().add(listOptions.optString(i));
        }
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        editText.setText(menuItem.getTitle());
                        return true;
                    }
                });
            }

        });
        return popupMenu;
    }

    protected int getLayout() {
        return R.layout.openlmis_native_form_edit_text;
    }
}
