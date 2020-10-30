package org.smartregister.stock.activity;

import android.os.Build;
import android.os.Bundle;
import android.widget.GridView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.navigation.NavigationView;

import org.smartregister.stock.R;
import org.smartregister.stock.StockLibrary;
import org.smartregister.stock.adapter.StockGridAdapter;
import org.smartregister.stock.domain.StockType;

import java.util.ArrayList;

/**
 * Created by raihan on 5/23/17.
 */
public abstract class StockActivity extends AppCompatActivity {
    private GridView stockGrid;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stockGrid = (GridView) findViewById(R.id.stockgrid);
    }

    protected abstract String getLoggedInUserInitials();

    protected abstract NavigationView getNavigationView();

    protected abstract NavigationView.OnNavigationItemSelectedListener getNavigationViewListener();

    protected abstract int getNavigationViewWith();

    protected abstract Class getControlActivity();

    @SuppressWarnings("unchecked")
    private void refreshAdapter() {
        ArrayList<StockType> allStockTypes = (ArrayList) StockLibrary.getInstance().getStockTypeRepository().getAllStockTypes(null);
        StockType[] stockTypes = allStockTypes.toArray(new StockType[allStockTypes.size()]);
        StockGridAdapter adapter = new StockGridAdapter(this, stockTypes, getControlActivity());
        stockGrid.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshAdapter();
    }
}
