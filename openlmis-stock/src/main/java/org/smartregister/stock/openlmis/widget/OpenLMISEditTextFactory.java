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

import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interfaces.CommonListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.stock.openlmis.OpenLMISLibrary;
import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.domain.openlmis.Reason;
import org.smartregister.stock.openlmis.widget.customviews.CustomTextInputEditText;

import java.util.List;

import static com.vijay.jsonwizard.constants.JsonFormConstants.EDIT_TEXT;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.ADJUSTMENT;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.DEBIT;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.JsonForm.ISSUE_REASONS;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.JsonForm.IS_SPINNABLE;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.JsonForm.LIST_OPTIONS;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.JsonForm.POPULATE_VALUES;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.JsonForm.STOCK_BALANCE;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.PROGRAM_ID;

public class OpenLMISEditTextFactory extends EditTextFactory {

    private JSONArray listOptions;

    @Override
    public List<View> getViewsFromJson(String stepName, Context context, JsonFormFragment formFragment, JSONObject jsonObject, CommonListener listener) throws Exception {
        String populateValues = jsonObject.optString(POPULATE_VALUES);

        if (ISSUE_REASONS.equals(populateValues)) {
            String programId = jsonObject.optString(PROGRAM_ID);

            List<Reason> validReasons = OpenLMISLibrary.getInstance().
                    getReasonRepository().findReasons(null, programId, DEBIT,
                    ADJUSTMENT);
            listOptions = convertReasonsToJsonArray(validReasons);
        } else if (jsonObject.has("list_options")) {
            listOptions = jsonObject.getJSONArray(LIST_OPTIONS);
        }
        super.setMainLayout(getLayout());
        super.setEditTextId(R.id.openlmis_edit_text);

        List<View> views = super.getViewsFromJson(stepName, context, formFragment, jsonObject, listener);

        RelativeLayout rootLayout = (RelativeLayout) views.get(0);

        if (jsonObject.optBoolean(IS_SPINNABLE)) {
            CustomTextInputEditText dropDown = (CustomTextInputEditText) ((TextInputLayout) rootLayout.findViewById(R.id.openlmis_edit_text_parent)).getEditText();
            Drawable spinner = context.getResources().getDrawable(R.drawable.abc_spinner_mtrl_am_alpha);
            spinner.setColorFilter(Color.parseColor("#9A9A9A"), PorterDuff.Mode.SRC_ATOP);
            dropDown.setCompoundDrawablesWithIntrinsicBounds(null, null, spinner, null);
            dropDown.setFocusable(false);
            populateDropdownOptions(context, dropDown);
        }
        if (!jsonObject.optBoolean("use_vvm", true)) {
            rootLayout.findViewById(R.id.openlmis_edit_text_parent).setVisibility(View.GONE);
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
                String nodeValue = (String) editText.getTag(com.vijay.jsonwizard.R.id.node_value);
                formFragment.writeValue(stepName, key, nodeValue == null ? s.toString() : nodeValue, openMrsEntityParent, openMrsEntity, openMrsEntityId, false);
            }

            @Override
            public void afterTextChanged(Editable s) {//do nothing
            }
        });

        if (jsonObject.has(STOCK_BALANCE)) {
            editText.setTag(R.id.stock_balance, jsonObject.getInt(STOCK_BALANCE));
        }
    }

    @VisibleForTesting
    protected void populateDropdownOptions(final Context context, final CustomTextInputEditText editText) {
        final PopupMenu popupMenu = new PopupMenu(context, editText);
        for (int i = 0; i < listOptions.length(); i++) {
            try {
                JSONObject jsonObject = listOptions.getJSONObject(i);
                MenuItem menuItem = popupMenu.getMenu().add(jsonObject.getString(JsonFormConstants.TEXT));
                View actionView = new View(context);
                actionView.setTag(com.vijay.jsonwizard.R.id.node_value, jsonObject.getString(JsonFormConstants.KEY));
                menuItem.setActionView(actionView);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        editText.setTag(com.vijay.jsonwizard.R.id.node_value, menuItem.getActionView().getTag(com.vijay.jsonwizard.R.id.node_value));
                        editText.setText(menuItem.getTitle());
                        return true;
                    }
                });
            }

        });
    }

    protected int getLayout() {
        return R.layout.openlmis_native_form_edit_text;
    }

    private JSONArray convertReasonsToJsonArray(List<Reason> validReasons) throws JSONException {
        JSONArray options = new JSONArray();
        for (Reason reason : validReasons) {
            JSONObject option = new JSONObject();
            option.put(JsonFormConstants.KEY, reason.getId());
            option.put(JsonFormConstants.TEXT, reason.getStockCardLineItemReason().getName());
            options.put(option);
        }
        return options;
    }
}
