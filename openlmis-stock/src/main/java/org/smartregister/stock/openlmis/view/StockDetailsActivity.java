package org.smartregister.stock.openlmis.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.activity.OpenLMISJsonForm;
import org.smartregister.stock.openlmis.adapter.LotAdapter;
import org.smartregister.stock.openlmis.adapter.StockTransactionAdapter;
import org.smartregister.stock.openlmis.dto.TradeItemDto;
import org.smartregister.stock.openlmis.presenter.StockDetailsPresenter;
import org.smartregister.stock.openlmis.util.OpenLMISConstants;
import org.smartregister.stock.openlmis.view.contract.StockDetailsView;
import org.smartregister.util.FormUtils;
import org.smartregister.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.smartregister.stock.openlmis.util.OpenLMISConstants.Forms.INDIVIDUAL_ADJUST_FORM;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.Forms.INDIVIDUAL_ISSUED_FORM;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.Forms.INDIVIDUAL_NON_LOT_ISSUE_FORM;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.Forms.INDIVIDUAL_NON_LOT_RECEIPT_FORM;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.Forms.INDIVIDUAL_RECEIVED_FORM;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.Forms.NON_LOT_INDIVIDUAL_ADJUST_FORM;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.JsonForm.DISPENSING_UNIT;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.JsonForm.NET_CONTENT;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.JsonForm.PROGRAM_ID;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.JsonForm.STOCK_ON_HAND;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.JsonForm.TRADE_ITEM;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.JsonForm.TRADE_ITEM_ID;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.JsonForm.USE_VVM;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.REFRESH_STOCK_ON_HAND;

public class StockDetailsActivity extends BaseActivity implements StockDetailsView, View.OnClickListener {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mma dd MMM, yyyy");
    private static final int REQUEST_CODE_GET_JSON = 3432;

    private StockDetailsPresenter stockDetailsPresenter;

    private RecyclerView lotsRecyclerView;

    private LinearLayout lotsHeader;

    private ImageView collapseExpandButton;

    private TradeItemDto tradeItemDto;

    private RecyclerView transactionsRecyclerView;

    private TextView dosesTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tradeItemDto = getIntent().getParcelableExtra(OpenLMISConstants.TRADE_ITEM);

        stockDetailsPresenter = new StockDetailsPresenter(this, tradeItemDto.getProgramId());

        toolbar.setTitle(getString(R.string.stock_details_title, tradeItemDto.getName()));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView itemNameTextView = findViewById(R.id.itemNameTextView);
        itemNameTextView.setText(tradeItemDto.getName());

        dosesTextView = findViewById(R.id.doseTextView);
        dosesTextView.setText(getString(R.string.stock_balance, tradeItemDto.getTotalStock(),
                tradeItemDto.getDispensingUnit(), tradeItemDto.getNetContent() * tradeItemDto.getTotalStock(),
                tradeItemDto.getRouteOfAdministration()));

        TextView lastUpdatedTextView = findViewById(R.id.lastUpdatedTextView);
        Date lastUpdated = new Date(tradeItemDto.getLastUpdated());
        lastUpdatedTextView.setText(getString(R.string.stock_last_updated,
                simpleDateFormat.format(lastUpdated)));

        TextView lots = findViewById(R.id.number_of_lots);
        lots.setText(getString(R.string.lots_details, tradeItemDto.getNumberOfLots()));

        lotsHeader = findViewById(R.id.lot_header);
        collapseExpandButton = findViewById(R.id.collapseExpandButton);


        if (tradeItemDto.isHasLots()) {
            lotsRecyclerView = findViewById(R.id.lotsRecyclerView);
            lotsRecyclerView.setAdapter(new LotAdapter(tradeItemDto, stockDetailsPresenter));
        } else {
            findViewById(R.id.list_lots_card_view).setVisibility(View.GONE);
            findViewById(R.id.transactions_lot_code).setVisibility(View.GONE);
        }

        transactionsRecyclerView = findViewById(R.id.transactionsRecyclerView);
        transactionsRecyclerView.setAdapter(new StockTransactionAdapter(tradeItemDto, stockDetailsPresenter));

        collapseExpandButton.setOnClickListener(this);
        findViewById(R.id.number_of_lots).setOnClickListener(this);
        findViewById(R.id.issued).setOnClickListener(this);
        findViewById(R.id.received).setOnClickListener(this);
        findViewById(R.id.loss_adj).setOnClickListener(this);
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_stock_details;
    }

    @Override
    public void showLotsHeader() {
        lotsHeader.setVisibility(View.VISIBLE);
    }

    @Override
    public void showTransactionsHeader() {
        findViewById(R.id.transactions_header).setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.collapseExpandButton || view.getId() == R.id.number_of_lots) {
            stockDetailsPresenter.collapseExpandClicked(lotsRecyclerView.getVisibility());
        } else if (view.getId() == R.id.issued) {
            if (tradeItemDto.isHasLots())
                startJsonForm(INDIVIDUAL_ISSUED_FORM);
            else
                startJsonForm(INDIVIDUAL_NON_LOT_ISSUE_FORM);
        } else if (view.getId() == R.id.received) {
            if (tradeItemDto.isHasLots())
                startJsonForm(INDIVIDUAL_RECEIVED_FORM);
            else
                startJsonForm(INDIVIDUAL_NON_LOT_RECEIPT_FORM);
        } else if (view.getId() == R.id.loss_adj) {
            if (tradeItemDto.isHasLots())
                startJsonForm(INDIVIDUAL_ADJUST_FORM);
            else
                startJsonForm(NON_LOT_INDIVIDUAL_ADJUST_FORM);
        }
    }

    @Override
    public void collapseLots() {
        lotsHeader.setVisibility(View.GONE);
        lotsRecyclerView.setVisibility(View.GONE);
        collapseExpandButton.setImageResource(R.drawable.ic_keyboard_arrow_down);
    }

    @Override
    public void expandLots() {
        lotsHeader.setVisibility(View.VISIBLE);
        lotsRecyclerView.setVisibility(View.VISIBLE);
        collapseExpandButton.setImageResource(R.drawable.ic_keyboard_arrow_up);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void refreshStockDetails(int totalStockAdjustment) {
        tradeItemDto.setTotalStock(tradeItemDto.getTotalStock() + totalStockAdjustment);
        if (tradeItemDto.isHasLots())
            lotsRecyclerView.setAdapter(new LotAdapter(tradeItemDto, stockDetailsPresenter));
        transactionsRecyclerView.setAdapter(new StockTransactionAdapter(tradeItemDto, stockDetailsPresenter));
        dosesTextView.setText(getString(R.string.stock_balance, tradeItemDto.getTotalStock(),
                tradeItemDto.getDispensingUnit(), tradeItemDto.getNetContent() * tradeItemDto.getTotalStock(),
                tradeItemDto.getRouteOfAdministration()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            String jsonString = data.getStringExtra("json");
            android.util.Log.d("JSONResult", jsonString);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            AllSharedPreferences allSharedPreferences = new AllSharedPreferences(preferences);
            stockDetailsPresenter.processFormJsonResult(jsonString, allSharedPreferences.fetchRegisteredANM());
        }
    }

    private void startJsonForm(String formName) {
        Intent intent = new Intent(getApplicationContext(), OpenLMISJsonForm.class);
        try {
            JSONObject form = FormUtils.getInstance(getApplicationContext()).getFormJson(formName);
            String formMetadata = form.toString().replace(TRADE_ITEM, tradeItemDto.getName());
            formMetadata = formMetadata.replace(TRADE_ITEM_ID, tradeItemDto.getId());
            formMetadata = formMetadata.replace(NET_CONTENT, tradeItemDto.getNetContent().toString());
            formMetadata = formMetadata.replace(DISPENSING_UNIT, tradeItemDto.getDispensingUnit());
            formMetadata = formMetadata.replace(STOCK_ON_HAND, tradeItemDto.getTotalStock().toString());
            formMetadata = formMetadata.replace(PROGRAM_ID, tradeItemDto.getProgramId());
            formMetadata = formMetadata.replace(USE_VVM, tradeItemDto.isUseVVM().toString());
            formMetadata = formMetadata.replace(DISPENSING_UNIT, tradeItemDto.getDispensingUnit());
            intent.putExtra("json", formMetadata);
            startActivityForResult(intent, REQUEST_CODE_GET_JSON);
        } catch (Exception e) {
            Log.logDebug(e.getMessage());
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        setResult(RESULT_OK, new Intent().putExtra(REFRESH_STOCK_ON_HAND, true));
        finish();
        return true;
    }


}
