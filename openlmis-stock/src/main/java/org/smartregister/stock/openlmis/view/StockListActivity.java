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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.adapter.ListCommodityTypeAdapter;
import org.smartregister.stock.openlmis.domain.openlmis.Program;
import org.smartregister.stock.openlmis.presenter.StockListPresenter;
import org.smartregister.stock.openlmis.receiver.SyncStatusBroadcastReceiver;
import org.smartregister.stock.openlmis.util.TestDataUtils;
import org.smartregister.stock.openlmis.view.contract.StockListView;

import java.util.List;

public class StockListActivity extends AppCompatActivity implements StockListView, View.OnClickListener
        , SyncStatusBroadcastReceiver.SyncStatusListener {

    private StockListPresenter stockListPresenter;

    private ListCommodityTypeAdapter adapter;

    private ArrayAdapter<Program> programsAdapter;

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

        adapter = new ListCommodityTypeAdapter(stockListPresenter, this);

        mRecyclerView.setAdapter(adapter);
        stockListPresenter.setCommodityTypeAdapter(adapter);

        Spinner programsFilter = findViewById(R.id.filterPrograms);

        programsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, stockListPresenter.getPrograms());

        programsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        programsFilter.setAdapter(programsAdapter);

        programsFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Program program = programsAdapter.getItem(position);
                adapter.setProgramId(program.getId().toString());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                adapter.setProgramId(null);
            }
        });

        if (programsAdapter.getCount() > 0)
            adapter.setProgramId(programsAdapter.getItem(0).getId().toString());

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Sync");
        menu.add("Logout");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().toString().equalsIgnoreCase("Sync")) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        List<Program> programs = stockListPresenter.getPrograms();
        if (programs.size() != programsAdapter.getCount()) {
            programsAdapter.clear();
            programsAdapter.addAll(programs);
            //if the current selected program was removed the remove selection and refresh recycler view
            if (!programs.contains(new Program(adapter.getProgramId()))) {
                adapter.setProgramId(null);
                programsAdapter.remove(new Program(adapter.getProgramId()));
            }
        } else
            adapter.refresh();
    }

    @Override
    protected void onDestroy() {
        SyncStatusBroadcastReceiver.getInstance().removeSyncStatusListener(this);
        super.onDestroy();
    }
}
