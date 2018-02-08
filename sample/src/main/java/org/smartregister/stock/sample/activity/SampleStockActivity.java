package org.smartregister.stock.sample.activity;

import android.os.Bundle;

import org.smartregister.stock.activity.StockActivity;

public class SampleStockActivity extends StockActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected String getLoggedInUserInitials() {
        return "RW";
    }
}
