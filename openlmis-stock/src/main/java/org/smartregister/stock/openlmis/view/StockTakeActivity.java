package org.smartregister.stock.openlmis.view;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.adapter.StockTakeCommodityTypeAdapter;
import org.smartregister.stock.openlmis.presenter.StockTakePresenter;

import static org.smartregister.stock.openlmis.repository.StockRepository.PROGRAM_ID;


/**
 * Created by samuelgithengi on 9/18/18.
 */
public class StockTakeActivity extends BaseActivity {

    private StockTakePresenter stockTakePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_close);
        }
        String programID = getIntent().getStringExtra(PROGRAM_ID);
        stockTakePresenter = new StockTakePresenter();
        StockTakeCommodityTypeAdapter commodityTypeAdapter = new StockTakeCommodityTypeAdapter(stockTakePresenter, programID);
        RecyclerView recyclerView = findViewById(R.id.commodityTypeRecyclerView);
        recyclerView.setAdapter(commodityTypeAdapter);

    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_stock_take;
    }
}
