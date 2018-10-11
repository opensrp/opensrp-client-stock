package org.smartregister.stock.openlmis.widget.customviews;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.util.AttributeSet;
import android.widget.EditText;

import com.rengwuxian.materialedittext.validation.METValidator;
import com.rengwuxian.materialedittext.validation.RegexpValidator;
import com.vijay.jsonwizard.validators.edittext.MaxNumericValidator;
import com.vijay.jsonwizard.validators.edittext.RequiredValidator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by samuelgithengi on 8/29/18.
 */
public class CustomTextInputEditText extends TextInputEditText {

    private List<METValidator> validators = new ArrayList<>();

    public CustomTextInputEditText(Context context) {
        super(context);
    }

    public CustomTextInputEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomTextInputEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void addValidator(RequiredValidator requiredValidator) {
        validators.add(requiredValidator);
    }

    public void addValidator(RegexpValidator regexpValidator) {
        validators.add(regexpValidator);
    }

    /**
     * Adds a new validator to the View's list of validators
     * <p/>
     * This will be checked with the others in {@link #validate()}
     *
     * @param validator Validator to add
     * @return This instance, for easy chaining
     */
    public EditText addValidator(METValidator validator) {
        if (validators == null) {
            this.validators = new ArrayList<>();
        }
        this.validators.add(validator);
        return this;
    }

    public void removeMaxValidators() {
        if (validators != null) {
            List<METValidator> maxvalidators = new ArrayList<>();
            for (METValidator validator : validators) {
                if (validator instanceof MaxNumericValidator)
                    maxvalidators.add(validator);
            }
            validators.removeAll(maxvalidators);
        }

    }

    public boolean validate() {
        if (validators == null || validators.isEmpty()) {
            return true;
        }

        CharSequence text = getText();
        boolean isEmpty = text.length() == 0;

        boolean isValid = true;
        for (METValidator validator : validators) {
            //noinspection ConstantConditions
            isValid = isValid && validator.isValid(text, isEmpty);
            if (!isValid) {
                setError(validator.getErrorMessage());
                break;
            }
        }
        if (isValid) {
            setError(null);
        }

        postInvalidate();
        return isValid;
    }
}
