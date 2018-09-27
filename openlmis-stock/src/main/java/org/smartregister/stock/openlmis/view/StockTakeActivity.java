package org.smartregister.stock.openlmis.view;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.adapter.StockTakeCommodityTypeAdapter;
import org.smartregister.stock.openlmis.presenter.StockTakePresenter;
import org.smartregister.stock.openlmis.view.contract.StockTakeView;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static org.smartregister.stock.openlmis.repository.StockRepository.PROGRAM_ID;


/**
 * Created by samuelgithengi on 9/18/18.
 */
public class StockTakeActivity extends BaseActivity implements StockTakeView {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mma MMM d, yyyy", Locale.getDefault());

    private StockTakePresenter stockTakePresenter;

    private TextView tradeItemsChangedTextView;

    private TextView lastChangedTextView;

    private int totalTradeItems = 0;

    private int tradeItemsAdjusted = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_close);
        }
        tradeItemsChangedTextView = findViewById(R.id.items_changed);
        lastChangedTextView = findViewById(R.id.last_changed);

        String programID = getIntent().getStringExtra(PROGRAM_ID);
        stockTakePresenter = new StockTakePresenter(this);
        StockTakeCommodityTypeAdapter commodityTypeAdapter = new StockTakeCommodityTypeAdapter(stockTakePresenter, programID);
        RecyclerView recyclerView = findViewById(R.id.commodityTypeRecyclerView);
        recyclerView.setAdapter(commodityTypeAdapter);
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_stock_take;
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

}
