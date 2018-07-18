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
import org.smartregister.stock.openlmis.adapter.ListCommodityTypeAdapter;
import org.smartregister.stock.openlmis.presenter.StockListPresenter;
import org.smartregister.stock.openlmis.view.contract.StockListView;

public class StockListActivity extends AppCompatActivity implements StockListView, View.OnClickListener {

    private RecyclerView mRecyclerView;

    private FloatingActionButton mfFloatingActionButton;

    private Toolbar toolbar;

    private StockListPresenter stockListPresenter;

    private Spinner programsFilter;

    private ListCommodityTypeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_list);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        stockListPresenter = new StockListPresenter(this);

        mfFloatingActionButton = findViewById(R.id.stockAction);
        mfFloatingActionButton.setOnClickListener(this);

        mRecyclerView = findViewById(R.id.commodityTypeRecyclerView);

        adapter = new ListCommodityTypeAdapter(stockListPresenter, this);
        mRecyclerView.setAdapter(adapter);
        stockListPresenter.setCommodityTypeAdapter(adapter);

        programsFilter = findViewById(R.id.filterPrograms);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, stockListPresenter.getPrograms());

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        programsFilter.setAdapter(dataAdapter);

        findViewById(R.id.expandAll).setOnClickListener(this);

        findViewById(R.id.collapseAll).setOnClickListener(this);

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

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.stockAction)
            stockListPresenter.stockActionClicked(view);
        else if (view.getId() == R.id.expandAll)
            stockListPresenter.expandAllClicked();
        else if (view.getId() == R.id.collapseAll)
            stockListPresenter.collapseAllClicked();
    }
}
