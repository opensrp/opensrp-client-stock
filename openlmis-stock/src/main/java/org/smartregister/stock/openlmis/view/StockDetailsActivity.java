package org.smartregister.stock.openlmis.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.adapter.LotAdapter;
import org.smartregister.stock.openlmis.dto.TradeItemDto;
import org.smartregister.stock.openlmis.presenter.StockDetailsPresenter;
import org.smartregister.stock.openlmis.util.OpenLMISConstants;
import org.smartregister.stock.openlmis.view.contract.StockDetailsView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StockDetailsActivity extends AppCompatActivity implements StockDetailsView {

    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mma dd MMM, yyyy");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_details);
        TradeItemDto tradeItemDto = getIntent().getParcelableExtra(OpenLMISConstants.tradeItem);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.stock_details_title, tradeItemDto.getName()));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView itemNameTextView = findViewById(R.id.itemNameTextView);
        itemNameTextView.setText(tradeItemDto.getName());

        TextView dosesTextView = findViewById(R.id.doseTextView);
        dosesTextView.setText(tradeItemDto.getDispensable());

        TextView lastUpdatedTextView = findViewById(R.id.lastUpdatedTextView);
        Date lastUpdated = new Date(tradeItemDto.getLastUpdated());
        lastUpdatedTextView.setText(getString(R.string.stock_last_updated,
                simpleDateFormat.format(lastUpdated)));

        StockDetailsPresenter stockDetailsPresenter = new StockDetailsPresenter(this);

        RecyclerView lotsRecyclerView = findViewById(R.id.lotsRecyclerView);
        lotsRecyclerView.setAdapter(new LotAdapter(tradeItemDto.getId(), stockDetailsPresenter));

    }

    @Override
    public void showLotsHeader() {
        findViewById(R.id.lot_header).setVisibility(View.VISIBLE);
    }

    @Override
    public void showTransactionsHeader() {

    }
}
