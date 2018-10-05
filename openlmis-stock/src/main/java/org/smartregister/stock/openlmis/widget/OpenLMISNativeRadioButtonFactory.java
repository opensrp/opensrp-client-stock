package org.smartregister.stock.openlmis.widget;

import android.content.Context;
import android.view.View;

import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interfaces.CommonListener;
import com.vijay.jsonwizard.widgets.NativeRadioButtonFactory;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.stock.openlmis.OpenLMISLibrary;
import org.smartregister.stock.openlmis.domain.openlmis.Reason;
import org.smartregister.stock.openlmis.domain.openlmis.ValidSourceDestination;

import java.util.List;

import static org.smartregister.stock.openlmis.util.OpenLMISConstants.CREDIT;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.DEBIT;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.JsonForm.ISSUE_DESTINATIONS;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.JsonForm.ISSUE_REASONS;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.JsonForm.POPULATE_VALUES;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.JsonForm.RECEIVE_REASONS;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.JsonForm.RECEIVE_SOURCES;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.PROGRAM_ID;

/**
 * Created by samuelgithengi on 10/4/18.
 */
public class OpenLMISNativeRadioButtonFactory extends NativeRadioButtonFactory {

    @Override
    public List<View> getViewsFromJson(String stepName, Context context, JsonFormFragment formFragment, JSONObject jsonObject, CommonListener listener) throws Exception {
        populateValues(jsonObject);
        return super.getViewsFromJson(stepName, context, formFragment, jsonObject, listener);

    }

    private void populateValues(JSONObject jsonObject) throws JSONException {
        String populateValues = jsonObject.optString(POPULATE_VALUES);
        if (StringUtils.isBlank(populateValues)) {
            return;
        }
        String facilityId = OpenLMISLibrary.getInstance().getOpenlmisUuid();
        String facilityType = OpenLMISLibrary.getInstance().getFacilityTypeUuid();
        String programId = jsonObject.optString(PROGRAM_ID);
        if (RECEIVE_SOURCES.equals(populateValues) || ISSUE_DESTINATIONS.equals(populateValues)) {
            List<ValidSourceDestination> validFacilities = OpenLMISLibrary.getInstance().
                    getValidSourceDestinationRepository().findValidSourceDestinations(null,
                    programId, facilityType, null, facilityId, RECEIVE_SOURCES.equals(populateValues));
            jsonObject.put(JsonFormConstants.OPTIONS_FIELD_NAME, convertToJsonArray(validFacilities));
            jsonObject.put(JsonFormConstants.VALUE, validFacilities.get(0).getOpenlmisUuid());
        } else if (RECEIVE_REASONS.equals(populateValues) || ISSUE_REASONS.equals(populateValues)) {
            List<Reason> validReasons = OpenLMISLibrary.getInstance().
                    getReasonRepository().findReasons(null, null, programId,
                    RECEIVE_REASONS.equals(populateValues) ? CREDIT : DEBIT);
            jsonObject.put(JsonFormConstants.OPTIONS_FIELD_NAME, convertReasonsToJsonArray(validReasons));
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
            options.put(option);
        }
        return options;
    }
}
