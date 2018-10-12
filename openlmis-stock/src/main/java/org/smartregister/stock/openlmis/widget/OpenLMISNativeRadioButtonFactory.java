package org.smartregister.stock.openlmis.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.rey.material.util.ViewUtil;
import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interfaces.CommonListener;
import com.vijay.jsonwizard.interfaces.FormWidgetFactory;
import com.vijay.jsonwizard.interfaces.JsonApi;
import com.vijay.jsonwizard.views.CustomTextView;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.stock.openlmis.OpenLMISLibrary;
import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.domain.openlmis.Reason;
import org.smartregister.stock.openlmis.domain.openlmis.ValidSourceDestination;
import org.smartregister.stock.openlmis.util.OpenLMISConstants;

import java.util.ArrayList;
import java.util.List;

import static com.vijay.jsonwizard.utils.FormUtils.FONT_BOLD_PATH;
import static com.vijay.jsonwizard.utils.FormUtils.MATCH_PARENT;
import static com.vijay.jsonwizard.utils.FormUtils.WRAP_CONTENT;
import static com.vijay.jsonwizard.utils.FormUtils.getLayoutParams;
import static com.vijay.jsonwizard.utils.FormUtils.getTextViewWith;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.ADJUSTMENT;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.CREDIT;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.DEBIT;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.JsonForm.ALL_REASONS;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.JsonForm.ISSUE_DESTINATIONS;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.JsonForm.ISSUE_REASONS;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.JsonForm.POPULATE_VALUES;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.JsonForm.REASON_TYPE;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.JsonForm.RECEIVE_REASONS;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.JsonForm.RECEIVE_SOURCES;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.PROGRAM_ID;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.TRANSFER;

/**
 * Created by samuelgithengi on 10/4/18.
 */
public class OpenLMISNativeRadioButtonFactory implements FormWidgetFactory {

    @Override
    public List<View> getViewsFromJson(String stepName, Context context, JsonFormFragment formFragment, JSONObject jsonObject, CommonListener listener) throws Exception {
        populateValues(jsonObject);

        String openMrsEntityParent = jsonObject.getString("openmrs_entity_parent");
        String openMrsEntity = jsonObject.getString("openmrs_entity");
        String openMrsEntityId = jsonObject.getString("openmrs_entity_id");
        String relevance = jsonObject.optString("relevance");

        boolean readOnly = false;
        if (jsonObject.has(JsonFormConstants.READ_ONLY)) {
            readOnly = jsonObject.getBoolean(JsonFormConstants.READ_ONLY);
        }

        List<View> views = new ArrayList<>(1);

        JSONArray canvasIds = new JSONArray();

        String label = jsonObject.optString("label");
        if (!label.isEmpty()) {
            CustomTextView textView = getTextViewWith(context, 27, label, jsonObject.getString(JsonFormConstants.KEY),
                    jsonObject.getString("type"), openMrsEntityParent, openMrsEntity, openMrsEntityId,
                    relevance,
                    getLayoutParams(MATCH_PARENT, WRAP_CONTENT, 0, 0, 0, 0), FONT_BOLD_PATH);
            canvasIds.put(textView.getId());
            textView.setEnabled(!readOnly);
            views.add(textView);
        }

        JSONArray options = jsonObject.getJSONArray(JsonFormConstants.OPTIONS_FIELD_NAME);
        ArrayList<RadioButton> radioButtons = new ArrayList<>();
        RadioGroup radioGroup = new RadioGroup(context);
        for (int i = 0; i < options.length(); i++) {
            JSONObject item = options.getJSONObject(i);
            RadioButton radioButton = (RadioButton) LayoutInflater.from(context).inflate(R.layout.item_radio_button,
                    null);
            radioButton.setId(ViewUtil.generateViewId());
            radioButton.setText(item.getString(JsonFormConstants.TEXT));
            radioButton.setTag(R.id.key, jsonObject.getString(JsonFormConstants.KEY));
            radioButton.setTag(R.id.openmrs_entity_parent, openMrsEntityParent);
            radioButton.setTag(R.id.openmrs_entity, openMrsEntity);
            radioButton.setTag(R.id.openmrs_entity_id, openMrsEntityId);
            radioButton.setTag(R.id.type, jsonObject.getString(JsonFormConstants.TYPE));
            radioButton.setTag(R.id.reason_type, item.optString(OpenLMISConstants.JsonForm.REASON_TYPE));
            radioButton.setTag(R.id.childKey, item.getString(JsonFormConstants.KEY));
            radioButton.setTag(R.id.address, stepName + ":" + jsonObject.getString(JsonFormConstants.KEY));
            radioButton.setOnCheckedChangeListener(listener);
            if (!TextUtils.isEmpty(jsonObject.optString(JsonFormConstants.VALUE))
                    && jsonObject.optString(JsonFormConstants.VALUE).equals(item.getString(JsonFormConstants.KEY))) {
                radioButton.setChecked(true);
            }
            radioButton.setEnabled(!readOnly);
            radioButton.setFocusable(!readOnly);

            ((JsonApi) context).addFormDataView(radioButton);

            canvasIds.put(radioButton.getId());
            radioButtons.add(radioButton);
            radioGroup.addView(radioButton);


            if (relevance != null) {
                radioButton.setTag(R.id.relevance, relevance);
                ((JsonApi) context).addSkipLogicView(radioButton);
            }
        }
        radioGroup.setLayoutParams(getLayoutParams(MATCH_PARENT, WRAP_CONTENT, 0, 0, 0, (int) context
                .getResources().getDimension(R.dimen.extra_bottom_margin)));
        views.add(radioGroup);

        for (RadioButton radioButton : radioButtons) {
            radioButton.setTag(R.id.canvas_ids, canvasIds.toString());
        }

        return views;
    }

    private void populateValues(JSONObject jsonObject) throws JSONException {
        String populateValues = jsonObject.optString(POPULATE_VALUES);
        if (StringUtils.isBlank(populateValues)) {
            return;
        }
        String facilityType = OpenLMISLibrary.getInstance().getFacilityTypeUuid();
        String programId = jsonObject.optString(PROGRAM_ID);
        if (RECEIVE_SOURCES.equals(populateValues) || ISSUE_DESTINATIONS.equals(populateValues)) {
            List<ValidSourceDestination> validFacilities = OpenLMISLibrary.getInstance().
                    getValidSourceDestinationRepository().findValidSourceDestinations(null,
                    programId, facilityType, null, null, RECEIVE_SOURCES.equals(populateValues));
            jsonObject.put(JsonFormConstants.OPTIONS_FIELD_NAME, convertToJsonArray(validFacilities));
            if (StringUtils.isBlank(jsonObject.optString(JsonFormConstants.VALUE)) && !validFacilities.isEmpty())
                jsonObject.put(JsonFormConstants.VALUE, validFacilities.get(0).getOpenlmisUuid());
        } else if (RECEIVE_REASONS.equals(populateValues) || ISSUE_REASONS.equals(populateValues) ||
                ALL_REASONS.equals(populateValues)) {
            List<Reason> validReasons = OpenLMISLibrary.getInstance().
                    getReasonRepository().findReasons(null, programId,
                    ALL_REASONS.equals(populateValues) ? null : RECEIVE_REASONS.equals(populateValues) ? CREDIT : DEBIT,
                    ALL_REASONS.equals(populateValues) ? ADJUSTMENT : TRANSFER);
            jsonObject.put(JsonFormConstants.OPTIONS_FIELD_NAME, convertReasonsToJsonArray(validReasons));
            if (StringUtils.isBlank(jsonObject.optString(JsonFormConstants.VALUE)) && !validReasons.isEmpty())
                jsonObject.put(JsonFormConstants.VALUE, validReasons.get(0).getId());
        }
    }

    private JSONArray convertToJsonArray(List<ValidSourceDestination> validFacilities) throws JSONException {
        JSONArray options = new JSONArray();
        for (ValidSourceDestination validSourceDestination : validFacilities) {
            JSONObject option = new JSONObject();
            option.put(JsonFormConstants.KEY, validSourceDestination.getOpenlmisUuid());
            option.put(JsonFormConstants.TEXT, validSourceDestination.getFacilityName());
            options.put(option);
        }
        return options;

    }

    private JSONArray convertReasonsToJsonArray(List<Reason> validReasons) throws JSONException {
        JSONArray options = new JSONArray();
        for (Reason reason : validReasons) {
            JSONObject option = new JSONObject();
            option.put(JsonFormConstants.KEY, reason.getId());
            option.put(JsonFormConstants.TEXT, reason.getStockCardLineItemReason().getName());
            option.put(REASON_TYPE, reason.getStockCardLineItemReason().getReasonType());
            options.put(option);
        }
        return options;
    }
}
