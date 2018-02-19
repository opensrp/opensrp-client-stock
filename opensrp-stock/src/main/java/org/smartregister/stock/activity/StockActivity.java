package org.smartregister.stock.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        Toolbar toolbar = (Toolbar) findViewById(R.id.location_switching_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //return to previous activity
                finish();
            }
        });

        TextView nameInitials = (TextView) findViewById(R.id.name_inits);
        nameInitials.setText(getLoggedInUserInitials());

        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        final NavigationView drawer = getNavigationView();
        if (drawer != null) {
            DrawerLayout.LayoutParams lp = new DrawerLayout.LayoutParams(
                    getNavigationViewWith(), LinearLayout.LayoutParams.MATCH_PARENT);
            lp.gravity = Gravity.START;
            drawerLayout.addView(drawer, lp);
            final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawerLayout.setDrawerListener(toggle);
            toggle.syncState();

            drawer.setNavigationItemSelectedListener(getNavigationViewListener());
        }
        nameInitials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer != null && !drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.openDrawer(GravityCompat.START);
                } else
                    finish();
            }
        });
        setSupportActionBar(toolbar);
        toolbar.setTitle("Stock Control");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        stockGrid = (GridView) findViewById(R.id.stockgrid);

    }

    protected abstract String getLoggedInUserInitials();

    protected abstract NavigationView getNavigationView();

    protected abstract NavigationView.OnNavigationItemSelectedListener getNavigationViewListener();

    protected abstract int getNavigationViewWith();

    protected abstract Class getControlActivity();

    @SuppressWarnings("unchecked")
    private void refreshadapter() {
        ArrayList<StockType> allStockTypes = (ArrayList) StockLibrary.getInstance().getStockTypeRepository().getAllStockTypes(null);
        StockType[] stockTypes = allStockTypes.toArray(new StockType[allStockTypes.size()]);
        StockGridAdapter adapter = new StockGridAdapter(this, stockTypes, getControlActivity());
        stockGrid.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshadapter();
    }

}
