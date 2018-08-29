package org.smartregister.stock.openlmis.widget.customviews;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.util.AttributeSet;

import com.rengwuxian.materialedittext.validation.METValidator;
import com.rengwuxian.materialedittext.validation.RegexpValidator;
import com.vijay.jsonwizard.validators.edittext.RequiredValidator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by samuelgithengi on 8/29/18.
 */
public class CustomTextInputEditText extends TextInputEditText {

    List<METValidator> validators = new ArrayList<>();

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
