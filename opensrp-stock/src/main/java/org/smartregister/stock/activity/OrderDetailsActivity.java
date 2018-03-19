package org.smartregister.stock.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;

import org.smartregister.stock.R;
import org.smartregister.stock.StockLibrary;
import org.smartregister.stock.domain.OrderShipment;
import org.smartregister.stock.domain.Shipment;
import org.smartregister.stock.domain.ShipmentLineItem;
import org.smartregister.stock.domain.Stock;
import org.smartregister.stock.domain.StockType;
import org.smartregister.stock.repository.ShipmentRepository;
import org.smartregister.stock.repository.StockRepository;
import org.smartregister.stock.repository.StockTypeRepository;
import org.smartregister.stock.util.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class OrderDetailsActivity extends BasicOrderActivity {

    public static final String ORDER_SHIPMENT_JSON = "ORDER SHIPMENT JSON";

    private String orderShipmentJSON;
    private OrderShipment orderShipment;

    private TextView orderedDateTv;
    private TextView shippedDateTv;
    private TextView supplyingFacilityNameTv;
    private TextView receivingFacilityNameTv;
    private TextView processingPeriodStartTv;
    private TextView processingPeriodEndTv;
    private Button receiveBtn;
    private TableLayout stockItemsTl;

    private int[] STRIPPED_TABLE_ROW_COLORS = new int[] {
            R.color.shipment_line_item_table_row_0,
            R.color.shipment_line_item_table_row_1
    };

    private static final int TEXT_SIZE = 22;
    private static final float CELL_LAYOUT_WEIGHT = 1.0f;
    private static final int CELL_MARGIN_IN_DP = 10;

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private HashMap<String, StockType> stockTypeHashMap;

    @Override
    protected int getContentView() {
        return R.layout.activity_order_details;
    }

    @Override
    protected int getStatusBarColor() {
        return Color.parseColor("#276C91");
    }

    @Override
    protected void afterOnCreate() {
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            orderShipmentJSON = bundle.getString(ORDER_SHIPMENT_JSON);
            orderShipment = new Gson()
                    .fromJson(orderShipmentJSON, OrderShipment.class);
            String orderCode = orderShipment.getOrder().getId();

            initialiseViews();

            if (orderShipment.getShipment() != null) {
                ShipmentLineItem[] shipmentLineItems = StockLibrary.getInstance().getShipmentLineItemRepository()
                        .getShipmentLineItemsForShipment(orderCode);
                Shipment shipment = orderShipment.getShipment();
                shipment.setShipmentLineItems(shipmentLineItems);

                if (shipment.getShipmentAcceptStatus() == Constants.Shipment.ACCEPT_STATUS_NO_ACTION) {
                    receiveBtn.setVisibility(View.VISIBLE);
                    receiveBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showReceiveDialog();
                        }
                    });
                }
            }

            ((TextView) findViewById(R.id.title)).setText(getString(R.string.stock_title) + " > " + getString(R.string.orders) + " > " + orderCode);
            populateViews();
        }
    }

    private void initialiseViews() {
        orderedDateTv = (TextView) findViewById(R.id.tv_orderDetails_orderedDate);
        shippedDateTv = (TextView) findViewById(R.id.tv_orderDetails_shippedDate);
        supplyingFacilityNameTv = (TextView) findViewById(R.id.tv_orderDetails_supplyingFacilityName);
        receivingFacilityNameTv = (TextView) findViewById(R.id.tv_orderDetails_receivingFacilityName);
        processingPeriodStartTv = (TextView) findViewById(R.id.tv_orderDetails_processingPeriodStartDate);
        processingPeriodEndTv = (TextView) findViewById(R.id.tv_orderDetails_processingPeriodEndDate);
        stockItemsTl = (TableLayout) findViewById(R.id.tl_orderDetails_tableLayout);
        receiveBtn = (Button) findViewById(R.id.btn_orderDetails_receiveShipment);
    }

    private void populateViews() {
        orderedDateTv.setText(getString(R.string.ordered) + ": " + getUserFriendlyDate(orderShipment.getOrder().getDateCreatedByClient()));

        if (orderShipment.getShipment() != null) {
            shippedDateTv.setText(getString(R.string.shipped) + ": " + getUserFriendlyDate(orderShipment.getShipment().getServerVersion()));
            supplyingFacilityNameTv.setText(getString(R.string.from) + ": " + orderShipment.getShipment().getSupplyingFacilityName());
            receivingFacilityNameTv.setText(getString(R.string.to) + ": " + orderShipment.getShipment().getReceivingFacilityName());
            processingPeriodStartTv.setText(getString(R.string.processing_period_start_date) + ": "
                    + getUserFriendlyDate(orderShipment.getShipment().getProcessingPeriodStartDate()));
            processingPeriodEndTv.setText(getString(R.string.processing_period_end_date) + ": " + getUserFriendlyDate(
                    orderShipment.getShipment().getProcessingPeriodEndDate()
            ));

            ShipmentLineItem[] shipmentLineItems = orderShipment.getShipment().getShipmentLineItems();

            int index = 0;
            for(ShipmentLineItem shipmentLineItem: shipmentLineItems) {
                stockItemsTl.addView(
                        createTableRow(index, shipmentLineItem),
                        new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                );
                index++;
            }
        } else {
            shippedDateTv.setVisibility(View.GONE);
            supplyingFacilityNameTv.setVisibility(View.GONE);
            receivingFacilityNameTv.setVisibility(View.GONE);
            processingPeriodStartTv.setVisibility(View.GONE);
            processingPeriodEndTv.setVisibility(View.GONE);
            stockItemsTl.setVisibility(View.GONE);
        }
    }

    private TableRow createTableRow(int index, ShipmentLineItem shipmentLineItem) {
        TableRow tableRow = new TableRow(context);
        tableRow.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        tableRow.addView(createTextViewCell(shipmentLineItem.getAntigenType()));
        tableRow.addView(createTextViewCell(String.valueOf(shipmentLineItem.getOrderedQuantity())));
        tableRow.addView(createTextViewCell(String.valueOf(shipmentLineItem.getShippedQuantity())));
        tableRow.addView(createTextViewCell(String.valueOf((shipmentLineItem.getNumberOfDoses() * shipmentLineItem.getShippedQuantity()))));

        tableRow.setBackgroundColor(getResources().getColor(getRowColor(index)));

        return tableRow;
    }

    private void showReceiveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(R.string.receive_shipment_dialog_title)
                .setMessage(R.string.receive_shipment_dialog_message)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        applyNewShipment(orderShipment);
                        disableReceiveButton();
                        finish();
                    }
                })
                .setNegativeButton(R.string.reject, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        rejectShipment(orderShipment);
                        disableReceiveButton();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void disableReceiveButton() {
        receiveBtn.setVisibility(View.GONE);
    }

    private int getRowColor(int index) {
        return STRIPPED_TABLE_ROW_COLORS[index%2];
    }

    private TextView createTextViewCell(String text) {
        TextView cellTv = new TextView(context);
        cellTv.setTextSize(TEXT_SIZE);
        cellTv.setTextColor(Color.BLACK);
        cellTv.setGravity(Gravity.CENTER);

        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, CELL_LAYOUT_WEIGHT);

        // Converting DP to PX
        int marginInPX = (int) ((getResources().getDisplayMetrics().density) * CELL_MARGIN_IN_DP);
        layoutParams.setMargins(marginInPX, marginInPX, marginInPX, marginInPX);

        cellTv.setLayoutParams(layoutParams);
        cellTv.setText(text);

        return cellTv;
    }

    private String getUserFriendlyDate(long dateLong) {
        return getUserFriendlyDate(new Date(dateLong));
    }

    private String getUserFriendlyDate(@Nullable Date date) {
        if (date == null) {
            return "";
        }

        return dateFormat.format(date);
    }

    private void rejectShipment(OrderShipment orderShipment) {
        ShipmentRepository shipmentRepository = StockLibrary.getInstance().getShipmentRepository();

        Shipment shipment = orderShipment.getShipment();
        shipment.setShipmentAcceptStatus(Constants.Shipment.ACCEPT_STATUS_REJECTED);
        shipmentRepository.updateShipment(shipment);
    }

    private void applyNewShipment(OrderShipment orderShipment) {
        acceptShippedQuantities(orderShipment);

        Shipment shipment = orderShipment.getShipment();
        StockRepository stockRepository = StockLibrary.getInstance().getStockRepository();
        ShipmentLineItem[] shipmentLineItems = shipment.getShipmentLineItems();

        String providerId = StockLibrary.getInstance().getContext().allSharedPreferences().fetchRegisteredANM();
        long timeNow = new Date().getTime();

        for(ShipmentLineItem shipmentLineItem: shipmentLineItems) {
            StockType stockType = getStockType(shipmentLineItem.getAntigenType());
            Stock stock = new Stock(
                    null,
                    Stock.received,
                    providerId,
                    shipmentLineItem.getAcceptedQuantity(),
                    timeNow,
                    "shipment",
                    StockRepository.TYPE_Unsynced,
                    timeNow,
                    String.valueOf(stockType.getId()));

            stockRepository.add(stock);
        }

        shipment.setShipmentAcceptStatus(Constants.Shipment.ACCEPT_STATUS_FULLY_ACCEPTED);
        shipment.setSynced(false);

        ShipmentRepository shipmentRepository = StockLibrary.getInstance().getShipmentRepository();
        shipmentRepository.updateShipment(shipment);
    }

    private void acceptShippedQuantities(OrderShipment orderShipment) {
        ShipmentLineItem[] shipmentLineItems = orderShipment.getShipment().getShipmentLineItems();

        for (ShipmentLineItem shipmentLineItem: shipmentLineItems) {
            shipmentLineItem.setAcceptedQuantity(shipmentLineItem.getShippedQuantity());
        }
    }

    private StockType getStockType(String antigenType) {
        if (stockTypeHashMap == null) {
            stockTypeHashMap = new HashMap<>();

            StockTypeRepository stockTypeRepository = StockLibrary.getInstance().getStockTypeRepository();
            List<StockType> stockTypeList = stockTypeRepository.getAllStockTypes(null);
            int size = stockTypeList.size();

            for(int i = 0; i < size; i++) {
                StockType stockType = stockTypeList.get(i);
                stockTypeHashMap.put(stockType.getName(), stockType);
            }
        }

        return stockTypeHashMap.get(antigenType);
    }

}
