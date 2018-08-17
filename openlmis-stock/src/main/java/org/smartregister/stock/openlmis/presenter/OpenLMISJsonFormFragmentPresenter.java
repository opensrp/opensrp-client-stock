package org.smartregister.stock.openlmis.presenter;

import android.widget.LinearLayout;

import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interactors.JsonFormInteractor;
import com.vijay.jsonwizard.presenters.JsonFormFragmentPresenter;
import com.vijay.jsonwizard.utils.ValidationStatus;

import org.smartregister.stock.openlmis.fragment.OpenLMISJsonFormFragment;

/**
 * Created by samuelgithengi on 8/17/18.
 */
public class OpenLMISJsonFormFragmentPresenter extends JsonFormFragmentPresenter {

    public OpenLMISJsonFormFragmentPresenter(JsonFormFragment formFragment, JsonFormInteractor jsonFormInteractor) {
        super(formFragment, jsonFormInteractor);
    }

    @Override
    public void onNextClick(LinearLayout mainView) {
        ValidationStatus validationStatus = this.writeValuesAndValidate(mainView);
        if (validationStatus.isValid()) {
            JsonFormFragment next = OpenLMISJsonFormFragment.getFormFragment(mStepDetails.optString("next"));
            getView().hideKeyBoard();
            getView().transactThis(next);
        } else {
            validationStatus.requestAttention();
            getView().showToast(validationStatus.getErrorMessage());
        }
    }
}
