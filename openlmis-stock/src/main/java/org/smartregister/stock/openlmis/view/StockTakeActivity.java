package org.smartregister.stock.openlmis.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.adapter.StockTakeCommodityTypeAdapter;
import org.smartregister.stock.openlmis.presenter.StockTakePresenter;
import org.smartregister.stock.openlmis.view.contract.StockTakeView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.smartregister.stock.openlmis.repository.StockRepository.PROGRAM_ID;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.REFRESH_STOCK_ON_HAND;


/**
 * Created by samuelgithengi on 9/18/18.
 */
public class StockTakeActivity extends BaseActivity implements StockTakeView, View.OnClickListener {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mma MMM d, yyyy", Locale.getDefault());

    private StockTakePresenter stockTakePresenter;

    private TextView tradeItemsChangedTextView;

    private TextView lastChangedTextView;

    private Button submitButton;

    private int totalTradeItems = 0;

    private int tradeItemsAdjusted = 0;

    private SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_close);
        }
        tradeItemsChangedTextView = findViewById(R.id.items_changed);
        lastChangedTextView = findViewById(R.id.last_changed);
        submitButton = findViewById(R.id.submit_button);

        String programID = getIntent().getStringExtra(PROGRAM_ID);
        stockTakePresenter = new StockTakePresenter(this);
        final StockTakeCommodityTypeAdapter commodityTypeAdapter = new StockTakeCommodityTypeAdapter(stockTakePresenter, programID);
        RecyclerView recyclerView = findViewById(R.id.commodityTypeRecyclerView);
        recyclerView.setAdapter(commodityTypeAdapter);

        submitButton.setOnClickListener(this);
        findViewById(R.id.save_draft).setOnClickListener(this);

        searchView = findViewById(R.id.searchStock);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                commodityTypeAdapter.filterCommodityTypes(s);
                return false;
            }
        });
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_stock_take;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (searchView != null) {
            searchView.clearFocus();
        }
    }

    @Override
    public void updateTotalTradeItems(int totalTradeItems) {
        this.totalTradeItems = totalTradeItems;
        tradeItemsChangedTextView.setText(getString(R.string.adjustment_summary, tradeItemsAdjusted, totalTradeItems));
    }

    @Override
    public void updateTradeItemsAdjusted(int itemsAdjusted, Date lastChanged) {
        this.tradeItemsAdjusted = itemsAdjusted;
        tradeItemsChangedTextView.setText(getString(R.string.adjustment_summary, tradeItemsAdjusted, totalTradeItems));
        if (lastChanged != null) {
            String formattedDate = simpleDateFormat.format(lastChanged);
            formattedDate = formattedDate.replace("AM", "am").replace("PM.", "pm");
            lastChangedTextView.setText(getString(R.string.last_adjustment, formattedDate));
        }
    }

    @Override
    public void onActivateSubmit() {
        submitButton.setEnabled(true);
        submitButton.setTextColor(getResources().getColor(R.color.white));
    }

    @Override
    public void onExitStockTake(boolean refresh) {
        setResult(RESULT_OK, new Intent().putExtra(REFRESH_STOCK_ON_HAND, refresh));
        finish();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.submit_button) {
            stockTakePresenter.completeStockTake(allSharedPreferences.fetchRegisteredANM());
        } else if (view.getId() == R.id.save_draft) {
            stockTakePresenter.saveStockTakeDraft();
        }
    }
}
