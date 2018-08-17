package org.smartregister.stock.openlmis.fragment;

import android.os.Bundle;

import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.presenters.JsonFormFragmentPresenter;

import org.smartregister.stock.openlmis.interactor.OpenLMISJsonFormInteractor;
import org.smartregister.stock.util.Constants;

/**
 * Created by samuelgithengi on 8/16/18.
 */
public class OpenLMISJsonFormFragment extends JsonFormFragment {

    @Override
    protected JsonFormFragmentPresenter createPresenter() {
        return new JsonFormFragmentPresenter(this, OpenLMISJsonFormInteractor.getInstance());
    }

    public static OpenLMISJsonFormFragment getFormFragment(String stepName) {
        OpenLMISJsonFormFragment jsonFormFragment = new OpenLMISJsonFormFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.STEPNAME, stepName);
        jsonFormFragment.setArguments(bundle);
        return jsonFormFragment;
    }
}
