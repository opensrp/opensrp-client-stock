package org.smartregister.stock.openlmis.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.adapter.ListCommodityTypeAdapter;
import org.smartregister.stock.openlmis.presenter.StockListPresenter;
import org.smartregister.stock.openlmis.receiver.SyncStatusBroadcastReceiver;
import org.smartregister.stock.openlmis.util.TestDataUtils;
import org.smartregister.stock.openlmis.view.contract.StockListView;

public class StockListActivity extends AppCompatActivity implements StockListView, View.OnClickListener
        , SyncStatusBroadcastReceiver.SyncStatusListener {

    private StockListPresenter stockListPresenter;

    private ListCommodityTypeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        stockListPresenter = new StockListPresenter(this);

        //populateTestData();

        FloatingActionButton mfFloatingActionButton = findViewById(R.id.stockAction);
        mfFloatingActionButton.setOnClickListener(this);

        RecyclerView mRecyclerView = findViewById(R.id.commodityTypeRecyclerView);

        final ListCommodityTypeAdapter adapter = new ListCommodityTypeAdapter(stockListPresenter, this);

        mRecyclerView.setAdapter(adapter);
        stockListPresenter.setCommodityTypeAdapter(adapter);

        Spinner programsFilter = findViewById(R.id.filterPrograms);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, stockListPresenter.getPrograms());

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        programsFilter.setAdapter(dataAdapter);

        findViewById(R.id.expandAll).setOnClickListener(this);

        findViewById(R.id.collapseAll).setOnClickListener(this);

        SyncStatusBroadcastReceiver.getInstance().addSyncStatusListener(this);

        SearchView searchView = findViewById(R.id.searchStock);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.filterCommodityTypes(s);
                return false;
            }
        });
    }

    public void populateTestData() {
        SharedPreferences sharedPreferences = getSharedPreferences("TestDataUtils", Context.MODE_PRIVATE);
        if (!sharedPreferences.getBoolean("testDataPopulated", false)) {
            TestDataUtils.getInstance().populateTestData();
            sharedPreferences.edit().putBoolean("testDataPopulated", true).apply();
        }
    }

    @Override
    public void showStockActionMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.inflate(R.menu.floating_stock_menu);
        popupMenu.show();
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.stockAction)
            stockListPresenter.stockActionClicked(view);
        else if (view.getId() == R.id.expandAll)
            stockListPresenter.expandAllClicked();
        else if (view.getId() == R.id.collapseAll)
            stockListPresenter.collapseAllClicked();
    }

    @Override
    public void onSyncComplete() {
        adapter.refresh();
    }

    @Override
    protected void onDestroy() {
        SyncStatusBroadcastReceiver.getInstance().removeSyncStatusListener(this);
        super.onDestroy();
    }
}
