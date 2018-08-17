package org.smartregister.stock.openlmis.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.presenters.JsonFormFragmentPresenter;

import org.json.JSONObject;
import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.interactor.OpenLMISJsonFormInteractor;
import org.smartregister.stock.openlmis.presenter.OpenLMISJsonFormFragmentPresenter;
import org.smartregister.stock.util.Constants;

import static org.smartregister.stock.openlmis.util.OpenLMISConstants.JsonForm.NEXT;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.JsonForm.PREVIOUS;

/**
 * Created by samuelgithengi on 8/16/18.
 */
public class OpenLMISJsonFormFragment extends JsonFormFragment {
    public static final String TAG = "OpenLMISJSonFragment";

    private BottomNavigationListener navigationListener = new BottomNavigationListener();
    private TextView informationTextView;
    private Button previousButton;
    private Button nextButton;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.openlmis_native_form_fragment_json_wizard, (ViewGroup) null);
        this.mMainView = rootView.findViewById(R.id.main_layout);
        this.mScrollView = rootView.findViewById(R.id.scroll_view);
        informationTextView = rootView.findViewById(R.id.information_textView);
        previousButton = rootView.findViewById(R.id.previous_button);
        nextButton = rootView.findViewById(R.id.next_button);
        setupCustomToolbar();
        rootView.findViewById(R.id.previous_button).setOnClickListener(navigationListener);
        rootView.findViewById(R.id.next_button).setOnClickListener(navigationListener);
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

    private void initializeBottomNavigation() {
        String stepName = getArguments().getString(Constants.STEPNAME);
        JSONObject step = getStep(stepName);
        if (step.has(PREVIOUS)) {
            previousButton.setVisibility(View.VISIBLE);
        }
        if (step.has(NEXT)) {
            nextButton.setVisibility(View.VISIBLE);
        }
    }

    private class BottomNavigationListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.next_button) {
                next();
            } else if (v.getId() == R.id.previous_button) {
                assert getFragmentManager() != null;
                getFragmentManager().popBackStack();
            }
        }
    }
}
