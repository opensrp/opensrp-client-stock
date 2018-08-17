package org.smartregister.stock.openlmis.activity;

import com.vijay.jsonwizard.activities.JsonFormActivity;
import com.vijay.jsonwizard.constants.JsonFormConstants;

import org.smartregister.stock.fragment.StockJsonFormFragment;
import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.fragment.OpenLMISJsonFormFragment;

/**
 * Created by samuelgithengi on 8/15/18.
 */
public class OpenLMISJsonForm extends JsonFormActivity {

    private OpenLMISJsonFormFragment stockJsonFormFragment;

    @Override
    public void initializeFormFragment() {
        stockJsonFormFragment = OpenLMISJsonFormFragment.getFormFragment(JsonFormConstants.FIRST_STEP_NAME);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, stockJsonFormFragment).commit();
    }

}
