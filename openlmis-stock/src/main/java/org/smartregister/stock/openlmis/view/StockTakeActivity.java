package org.smartregister.stock.openlmis.view;

import android.os.Bundle;

import org.smartregister.stock.openlmis.R;

import static org.smartregister.stock.openlmis.repository.StockRepository.PROGRAM_ID;


/**
 * Created by samuelgithengi on 9/18/18.
 */
public class StockTakeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_close);
        }
        String programID = getIntent().getStringExtra(PROGRAM_ID);
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_stock_take;
    }
}
