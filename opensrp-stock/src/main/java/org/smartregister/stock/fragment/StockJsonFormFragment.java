package org.smartregister.stock.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.presenters.JsonFormFragmentPresenter;
import com.vijay.jsonwizard.views.CustomTextView;

import org.smartregister.stock.R;
import org.smartregister.stock.activity.StockJsonFormActivity;
import org.smartregister.stock.interactor.StockJsonFormInteractor;
import org.smartregister.stock.util.Constants;

import java.util.Iterator;
import java.util.List;

/**
 * Created by samuelgithengi on 2/8/18.
 */

public class StockJsonFormFragment extends JsonFormFragment {

    @Override
    protected JsonFormFragmentPresenter createPresenter() {
        return new JsonFormFragmentPresenter(this, StockJsonFormInteractor.getInstance());
    }

    public static StockJsonFormFragment getFormFragment(String stepName) {
        StockJsonFormFragment jsonFormFragment = new StockJsonFormFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.STEPNAME, stepName);
        jsonFormFragment.setArguments(bundle);
        return jsonFormFragment;
    }

    @Override
    public void addFormElements(List<View> views) {
        Iterator iterator = views.iterator();

        while (iterator.hasNext()) {
            View view = (View) iterator.next();
            getMainView().addView(view);

            // Make sure this TextView is not part of another widget -
            // i.e. It is a lone label explicity defined in JSON
            if (view instanceof CustomTextView && view.getTag(R.id.key) != null) {
                if (getJsonApi() instanceof StockJsonFormActivity) {
                    ((StockJsonFormActivity) getJsonApi()).addLabel(view);
                }
            }
        }

        getJsonApi().refreshHiddenViews(false);
        getJsonApi().resetFocus();
    }

    public void getLabelViewFromTag(String labelText, String toDisplay) {
        updateRelevantTextView(getMainView(), toDisplay, labelText);
    }

    private void updateRelevantTextView(LinearLayout mMainView, String textString, String currentKey) {
        if (mMainView != null) {
            int childCount = mMainView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View layout = mMainView.getChildAt(i);
                if (layout instanceof ViewGroup) {
                    int layoutCount = ((ViewGroup) layout).getChildCount();
                    for (int j = 0; j < layoutCount; j++) {
                        View view = ((ViewGroup) layout).getChildAt(j);
                        if (view instanceof TextView) {
                            TextView textView = (TextView) view;
                            String key = (String) textView.getTag(R.id.key);
                            if (key != null && key.equals(currentKey)) {
                                textView.setText(textString);
                                textView.setTextColor(1);
                            }
                        }
                    }
                }
            }
        }
    }
}
