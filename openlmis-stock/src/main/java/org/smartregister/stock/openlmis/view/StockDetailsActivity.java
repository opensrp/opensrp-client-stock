package org.smartregister.stock.openlmis.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.dto.TradeItemDto;
import org.smartregister.stock.openlmis.util.OpenLMISConstants;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StockDetailsActivity extends AppCompatActivity {

    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mma dd MMM, yyyy");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_details);
        TradeItemDto tradeItemName = getIntent().getParcelableExtra(OpenLMISConstants.tradeItem);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.stock_details_title, tradeItemName.getName()));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView itemNameTextView = findViewById(R.id.itemNameTextView);
        itemNameTextView.setText(tradeItemName.getName());

        TextView dosesTextView = findViewById(R.id.doseTextView);
        dosesTextView.setText(tradeItemName.getDispensable());

        TextView lastUpdatedTextView = findViewById(R.id.lastUpdatedTextView);
        Date lastUpdated = new Date(tradeItemName.getLastUpdated());
        lastUpdatedTextView.setText(simpleDateFormat.format(lastUpdated));

    }
}
