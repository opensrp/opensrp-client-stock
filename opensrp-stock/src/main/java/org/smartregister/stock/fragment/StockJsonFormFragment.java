package org.smartregister.stock.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vijay.jsonwizard.fragments.JsonFormFragment;

import org.smartregister.stock.R;
import org.smartregister.stock.util.Constants;

/**
 * Created by samuelgithengi on 2/8/18.
 */

public class StockJsonFormFragment extends JsonFormFragment {

    public static StockJsonFormFragment getFormFragment(String stepName) {
        StockJsonFormFragment jsonFormFragment = new StockJsonFormFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.STEPNAME, stepName);
        jsonFormFragment.setArguments(bundle);
        return jsonFormFragment;
    }


    public void getLabelViewFromTag(String labelText, String toDisplay) {
        updateRelevantTextView(getMainView(), toDisplay, labelText);
    }

    private void updateRelevantTextView(LinearLayout mMainView, String textString, String currentKey) {
        if (mMainView != null) {
            int childCount = mMainView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View view = mMainView.getChildAt(i);
                if (view instanceof TextView) {
                    TextView textView = (TextView) view;
                    String key = (String) textView.getTag(R.id.key);
                    if (key.equals(currentKey)) {
                        textView.setText(textString);
                    }
                }
            }
        }
    }

    public String getRelevantTextViewString(String currentKey) {
        String toreturn = "";
        if (getMainView() != null) {
            int childCount = getMainView().getChildCount();
            for (int i = 0; i < childCount; i++) {
                View view = getMainView().getChildAt(i);
                if (view instanceof TextView) {
                    TextView textView = (TextView) view;
                    String key = (String) textView.getTag(R.id.key);
                    if (key.equals(currentKey)) {
                        toreturn = textView.getText().toString();
                    }
                }
            }
        }
        return toreturn;
    }
}
