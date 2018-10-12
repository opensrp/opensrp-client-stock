package org.smartregister.stock.openlmis.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.smartregister.stock.openlmis.OpenLMISLibrary;
import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.adapter.ListCommodityTypeAdapter;
import org.smartregister.stock.openlmis.domain.openlmis.Program;
import org.smartregister.stock.openlmis.dto.TradeItemDto;
import org.smartregister.stock.openlmis.presenter.StockListPresenter;
import org.smartregister.stock.openlmis.receiver.OpenLMISAlarmReceiver;
import org.smartregister.stock.openlmis.receiver.SyncStatusBroadcastReceiver;
import org.smartregister.stock.openlmis.util.OpenLMISConstants;
import org.smartregister.stock.openlmis.util.TestDataUtils;
import org.smartregister.stock.openlmis.view.contract.StockListView;

import java.util.List;

import static org.smartregister.stock.openlmis.repository.StockRepository.PROGRAM_ID;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.REFRESH_STOCK_ON_HAND;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.SERVICE_TYPE_NAME;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.ServiceType.SYNC_OPENLMIS_METADATA;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.ServiceType.SYNC_STOCK;
import static org.smartregister.stock.openlmis.util.Utils.sendSyncCompleteBroadCast;

public class StockListActivity extends BaseActivity implements StockListView, View.OnClickListener
        , SyncStatusBroadcastReceiver.SyncStatusListener {

    private StockListPresenter stockListPresenter;

    private ListCommodityTypeAdapter adapter;

    private ArrayAdapter<Program> programsAdapter;

    public final static int STOCK_LIST = 2340;

    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stockListPresenter = new StockListPresenter(this);

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
                adapter.setProgramId(program.getId());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                adapter.setProgramId(null);
            }
        });

        if (programsAdapter.getCount() > 0)
            adapter.setProgramId(programsAdapter.getItem(0).getId());

        findViewById(R.id.expandAll).setOnClickListener(this);

        findViewById(R.id.collapseAll).setOnClickListener(this);

        SyncStatusBroadcastReceiver.getInstance().addSyncStatusListener(this);

        searchView = findViewById(R.id.searchStock);
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
            // send openlmis metadata sync broadcast
            Intent intent = new Intent(getApplicationContext(), OpenLMISAlarmReceiver.class);
            intent.putExtra(SERVICE_TYPE_NAME, SYNC_OPENLMIS_METADATA);
            sendBroadcast(intent);
            // send stock sync broadcast
            intent = new Intent(getApplicationContext(), OpenLMISAlarmReceiver.class);
            intent.putExtra(SERVICE_TYPE_NAME, SYNC_STOCK);
            sendBroadcast(intent);
            // send sync completed broadcast
            sendSyncCompleteBroadCast(getApplication());
            return true;
        } else if (item.getTitle().toString().equalsIgnoreCase("Logout")) {
            OpenLMISLibrary.getInstance().getApplication().getInstance().logoutCurrentUser();
        }
        return super.onOptionsItemSelected(item);
    }

    public int getLayoutView() {
        return R.layout.activity_stock_list;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (searchView != null) {
            searchView.clearFocus();
        }
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
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.stock_take)
                    startBulkActivity(StockTakeActivity.class);
                return true;
            }
        });
        popupMenu.show();
    }

    private void startBulkActivity(Class<? extends AppCompatActivity> activity) {
        Intent intent = new Intent(getApplicationContext(), activity);
        intent.putExtra(PROGRAM_ID, adapter.getProgramId());
        startActivityForResult(intent, STOCK_LIST);
    }

    @Override
    public void startStockDetails(TradeItemDto tradeItemDto) {
        Intent intent = new Intent(getApplicationContext(), StockDetailsActivity.class);
        intent.putExtra(OpenLMISConstants.TRADE_ITEM, tradeItemDto);
        startActivityForResult(intent, STOCK_LIST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == STOCK_LIST && resultCode == RESULT_OK &&
                data != null && data.getBooleanExtra(REFRESH_STOCK_ON_HAND, false))
            adapter.refresh();
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
