package org.smartregister.stock.sample.activity;

import android.support.v4.widget.DrawerLayout;

import org.smartregister.stock.activity.StockActivity;
import org.smartregister.stock.activity.StockControlActivity;

public class SampleStockActivity extends StockActivity {

    @Override
    protected String getLoggedInUserInitials() {
        return "RW";
    }

    @Override
    protected DrawerLayout getDrawer() {
        return null;
    }

    @Override
    protected Class getControlActivity() {
        return StockControlActivity.class;
    }
}
