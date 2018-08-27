package org.smartregister.stock.openlmis.presenter;

import android.view.View;
import android.widget.LinearLayout;

import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interactors.JsonFormInteractor;
import com.vijay.jsonwizard.presenters.JsonFormFragmentPresenter;
import com.vijay.jsonwizard.utils.ValidationStatus;
import com.vijay.jsonwizard.views.JsonFormFragmentView;

import org.smartregister.stock.openlmis.fragment.OpenLMISJsonFormFragment;
import org.smartregister.stock.openlmis.widget.LotFactory;

import static org.smartregister.stock.openlmis.util.OpenLMISConstants.LOT_WIDGET;

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

    public static ValidationStatus validate(JsonFormFragmentView formFragmentView, View childAt, boolean requestFocus) {
        ValidationStatus validationStatus = JsonFormFragmentPresenter.validate(formFragmentView, childAt, requestFocus);
        if (validationStatus.isValid() && childAt instanceof LinearLayout
                && childAt.getTag(com.vijay.jsonwizard.R.id.type).equals(LOT_WIDGET)) {
            validationStatus = LotFactory.validate(formFragmentView, (LinearLayout) childAt);
        }
        return validationStatus;
    }
}
