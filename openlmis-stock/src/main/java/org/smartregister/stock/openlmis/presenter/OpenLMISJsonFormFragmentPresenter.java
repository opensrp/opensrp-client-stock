package org.smartregister.stock.openlmis.presenter;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interactors.JsonFormInteractor;
import com.vijay.jsonwizard.presenters.JsonFormFragmentPresenter;
import com.vijay.jsonwizard.utils.ValidationStatus;
import com.vijay.jsonwizard.validators.edittext.MaxNumericValidator;
import com.vijay.jsonwizard.views.JsonFormFragmentView;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.fragment.OpenLMISJsonFormFragment;
import org.smartregister.stock.openlmis.widget.LotFactory;
import org.smartregister.stock.openlmis.widget.OpenLMISDatePickerFactory;
import org.smartregister.stock.openlmis.widget.OpenLMISEditTextFactory;
import org.smartregister.stock.openlmis.widget.customviews.CustomTextInputEditText;

import java.util.List;

import static com.vijay.jsonwizard.constants.JsonFormConstants.DATE_PICKER;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.DEBIT;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.JsonForm.ADJUSTED_QUANTITY;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.LOT_WIDGET;

/**
 * Created by samuelgithengi on 8/17/18.
 */
public class OpenLMISJsonFormFragmentPresenter extends JsonFormFragmentPresenter {

    private final OpenLMISJsonFormFragment formFragment;


    public OpenLMISJsonFormFragmentPresenter(OpenLMISJsonFormFragment formFragment, JsonFormInteractor jsonFormInteractor) {
        super(formFragment, jsonFormInteractor);
        this.formFragment = formFragment;
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
        } else if (validationStatus.isValid() && childAt instanceof CustomTextInputEditText
                && childAt.getTag(com.vijay.jsonwizard.R.id.type).equals(DATE_PICKER)) {
            validationStatus = OpenLMISDatePickerFactory.validate(formFragmentView, (CustomTextInputEditText) childAt);
        } else if (validationStatus.isValid() && childAt instanceof CustomTextInputEditText
                && childAt.getTag(com.vijay.jsonwizard.R.id.type).equals(JsonFormConstants.EDIT_TEXT)) {
            validationStatus = OpenLMISEditTextFactory.validate(formFragmentView, (CustomTextInputEditText) childAt);
        }
        return validationStatus;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if (compoundButton instanceof android.widget.RadioButton && isChecked &&
                StringUtils.isNotBlank(compoundButton.getTag(R.id.reason_type).toString())) {
            List<View> views = formFragment.getJsonApi().getFormDataViews();
            for (View view : views) {
                if (ADJUSTED_QUANTITY.equals(view.getTag(R.id.key))) {
                    CustomTextInputEditText editText = (CustomTextInputEditText) view;
                    if (DEBIT.equals(compoundButton.getTag(R.id.reason_type))) {
                        int stockOnHand = Integer.valueOf(editText.getTag(R.id.stock_balance).toString());
                        editText.addValidator(new MaxNumericValidator(formFragment.getContext().getString(R.string.negative_adjustment, stockOnHand),
                                stockOnHand));
                    } else {
                        editText.removeMaxValidators();
                    }
                    break;
                }
            }
        }
        super.onCheckedChanged(compoundButton, isChecked);
    }
}
