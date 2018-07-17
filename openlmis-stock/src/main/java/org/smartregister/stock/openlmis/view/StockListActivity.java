package org.smartregister.stock.openlmis.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.adapter.StockListAdapter;
import org.smartregister.stock.openlmis.presenter.StockListPresenter;
import org.smartregister.stock.openlmis.view.contract.StockListView;

public class StockListActivity extends AppCompatActivity implements StockListView {

    private RecyclerView mRecyclerView;

    private FloatingActionButton mfFloatingActionButton;

    private Toolbar toolbar;

    private StockListPresenter stockListPresenter;

    private Spinner programsFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_list);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        stockListPresenter = new StockListPresenter(this);

        mfFloatingActionButton = findViewById(R.id.stockAction);
        mfFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stockListPresenter.stockActionClicked(view);
            }
        });

        mRecyclerView = findViewById(R.id.commodityTypeRecyclerView);

        mRecyclerView.setAdapter(new StockListAdapter(stockListPresenter.getCommodityTypes()));

        programsFilter = findViewById(R.id.filterPrograms);
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, stockListPresenter.getPrograms());

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        programsFilter.setAdapter(dataAdapter);
    }

    @Override
    public void showStockActionMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.inflate(R.menu.floating_stock_menu);
        popupMenu.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.stock_menu, menu);
        return true;
    }

}
