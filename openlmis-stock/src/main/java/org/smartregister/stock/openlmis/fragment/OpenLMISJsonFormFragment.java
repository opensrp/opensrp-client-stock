package org.smartregister.stock.openlmis.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.presenters.JsonFormFragmentPresenter;
import com.vijay.jsonwizard.utils.ValidationStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.interactor.OpenLMISJsonFormInteractor;
import org.smartregister.stock.openlmis.presenter.OpenLMISJsonFormFragmentPresenter;
import org.smartregister.stock.openlmis.widget.LotFactory;
import org.smartregister.stock.util.Constants;

import static org.smartregister.stock.openlmis.util.OpenLMISConstants.JsonForm.NEXT;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.JsonForm.NEXT_ENABLED;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.JsonForm.NEXT_LABEL;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.JsonForm.NEXT_TYPE;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.JsonForm.NO_PADDING;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.JsonForm.PREVIOUS;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.JsonForm.PREVIOUS_LABEL;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.JsonForm.SUBMIT;
import static org.smartregister.stock.openlmis.widget.LotFactory.NON_LOT_FIELD;

/**
 * Created by samuelgithengi on 8/16/18.
 */
public class OpenLMISJsonFormFragment extends JsonFormFragment {
    public static final String TAG = "OpenLMISJSonFragment";

    private BottomNavigationListener navigationListener = new BottomNavigationListener();
    private Button previousButton;
    private Button nextButton;
    private TextView informationTextView;
    private String stepName;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.openlmis_native_form_fragment_json_wizard, null);
        this.mMainView = rootView.findViewById(R.id.main_layout);
        this.mScrollView = rootView.findViewById(R.id.scroll_view);
        previousButton = rootView.findViewById(R.id.previous_button);
        nextButton = rootView.findViewById(R.id.next_button);
        nextButton.setEnabled(false);
        informationTextView = rootView.findViewById(R.id.information_textView);
        setupCustomToolbar();
        rootView.findViewById(R.id.previous_button).setOnClickListener(navigationListener);
        rootView.findViewById(R.id.next_button).setOnClickListener(navigationListener);
        if (getArguments() != null) {
            stepName = getArguments().getString(Constants.STEPNAME);
            if (getStep(stepName).optBoolean(NO_PADDING))
                mMainView.setPadding(0, 0, 0, 0);
        }

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeBottomNavigation();
    }

    private void setupCustomToolbar() {
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_close);
        setUpBackButton();
    }

    @Override
    protected JsonFormFragmentPresenter createPresenter() {
        return new OpenLMISJsonFormFragmentPresenter(this, OpenLMISJsonFormInteractor.getInstance());
    }

    public static OpenLMISJsonFormFragment getFormFragment(String stepName) {
        OpenLMISJsonFormFragment jsonFormFragment = new OpenLMISJsonFormFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.STEPNAME, stepName);
        jsonFormFragment.setArguments(bundle);
        return jsonFormFragment;
    }

    protected void initializeBottomNavigation() {
        JSONObject step = getStep(stepName);
        if (step.has(PREVIOUS)) {
            previousButton.setVisibility(View.VISIBLE);
            if (step.has(PREVIOUS_LABEL))
                previousButton.setText(step.optString(PREVIOUS_LABEL));
        }
        if (step.has(NEXT)) {
            nextButton.setVisibility(View.VISIBLE);
        } else if (step.optString(NEXT_TYPE).equalsIgnoreCase(SUBMIT)) {
            nextButton.setTag(R.id.submit, true);
            nextButton.setVisibility(View.VISIBLE);
            if (step.optBoolean(NEXT_ENABLED)) {
                nextButton.setEnabled(true);
                nextButton.setTextColor(getContext().getResources().getColor(R.color.white));
            }
        }
        if (step.has(NEXT_LABEL))
            nextButton.setText(step.optString(NEXT_LABEL));

        try {
            boolean isNonLot = step.getJSONArray("fields").getJSONObject(0).getBoolean(NON_LOT_FIELD);
            if (isNonLot) {
                informationTextView.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }

    private class BottomNavigationListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.next_button) {
                Object isSubmit = v.getTag(R.id.submit);
                if (isSubmit != null && Boolean.valueOf(isSubmit.toString()))
                    save(true);
                else
                    next();
            } else if (v.getId() == R.id.previous_button) {
                assert getFragmentManager() != null;
                getFragmentManager().popBackStack();
            }
        }
    }

    public void validateActivateNext(boolean isLotEnabled) {
        if (!isVisible())//form fragment is initializing
            return;
        ValidationStatus validationStatus = null;
        for (View dataView : getJsonApi().getFormDataViews()) {

            validationStatus = getPresenter().validate(this, dataView, false, isLotEnabled);
            if (!validationStatus.isValid()) {
                break;
            }
        }
        if (validationStatus != null && validationStatus.isValid()) {
            nextButton.setEnabled(true);
            nextButton.setTextColor(getContext().getResources().getColor(R.color.white));
        } else {
            nextButton.setEnabled(false);
            nextButton.setTextColor(getContext().getResources().getColor(R.color.next_button_disabled));
        }
    }

    public void writeValue(String stepName, String key, String s, String
            openMrsEntityParent, String openMrsEntity, String openMrsEntityId, boolean isLotEnabled) {
        super.writeValue(stepName, key, s, openMrsEntityParent, openMrsEntity, openMrsEntityId);
        validateActivateNext(isLotEnabled);
    }

    @Override
    public void writeValue(String stepName, String prentKey, String childObjectKey, String
            childKey, String value, String openMrsEntityParent, String openMrsEntity, String
                                   openMrsEntityId) {
        super.writeValue(stepName, prentKey, childObjectKey, childKey, value, openMrsEntityParent, openMrsEntity, openMrsEntityId);
        validateActivateNext(true);
    }

    public void setBottomNavigationText(String text) {
        informationTextView.setText(text);
    }

    private OpenLMISJsonFormFragmentPresenter getPresenter() {
        return (OpenLMISJsonFormFragmentPresenter) presenter;
    }
}
