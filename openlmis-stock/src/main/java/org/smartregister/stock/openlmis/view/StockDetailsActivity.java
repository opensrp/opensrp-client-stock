package org.smartregister.stock.openlmis.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.util.OpenLMISConstants;

public class StockDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        String tradeItem=getIntent().getStringExtra(OpenLMISConstants.tradeItem);
        toolbar.setTitle(getString(R.string.stock_details_title,tradeItem));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}
