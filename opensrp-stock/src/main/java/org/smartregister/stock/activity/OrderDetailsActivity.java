package org.smartregister.stock.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;

import org.smartregister.stock.R;
import org.smartregister.stock.domain.OrderShipment;
import org.smartregister.stock.domain.ShipmentLineItem;
import org.smartregister.stock.fragment.OrderDetailsFragment;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderDetailsActivity extends AppCompatActivity {

    public static final String ORDER_SHIPMENT_JSON = "ORDER SHIPMENT JSON";

    // TODO: Rename and change types of parameters
    private String orderShipmentJSON;
    private OrderShipment orderShipment;

    private OrderDetailsFragment.OnFragmentInteractionListener mListener;

    private TextView orderCodeTv;
    private TextView orderedDateTv;
    private TextView shippedDateTv;
    private TextView supplyingFacilityNameTv;
    private TextView receivingFacilityNameTv;
    private TextView processingPeriodStartTv;
    private TextView processingPeriodEndTv;

    private TableLayout stockItemsTl;

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        context = this;

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            orderShipmentJSON = bundle.getString(ORDER_SHIPMENT_JSON);
            orderShipment = new Gson()
                    .fromJson(orderShipmentJSON, OrderShipment.class);

            setTitle("Stock Control > Orders > " + orderShipment.getOrder().getId());

            initialiseViews();
            populateViews();
        }
    }

    private void initialiseViews() {
        orderCodeTv = (TextView) findViewById(R.id.tv_orderDetails_orderCode);
        orderedDateTv = (TextView) findViewById(R.id.tv_orderDetails_orderedDate);
        shippedDateTv = (TextView) findViewById(R.id.tv_orderDetails_shippedDate);
        supplyingFacilityNameTv = (TextView) findViewById(R.id.tv_orderDetails_supplyingFacilityName);
        receivingFacilityNameTv = (TextView) findViewById(R.id.tv_orderDetails_receivingFacilityName);
        processingPeriodStartTv = (TextView) findViewById(R.id.tv_orderDetails_processingPeriodStartDate);
        processingPeriodEndTv = (TextView) findViewById(R.id.tv_orderDetails_processingPeriodEndDate);
        stockItemsTl = (TableLayout) findViewById(R.id.tl_orderDetails_tableLayout);
    }

    private void populateViews() {
        orderCodeTv.setText(orderShipment.getOrder().getId());
        orderedDateTv.setText("Ordered: " + getUserFriendlyDate(orderShipment.getOrder().getDateCreatedByClient()));

        if (orderShipment.getShipment() != null) {
            shippedDateTv.setText("Shipped: " + getUserFriendlyDate(orderShipment.getShipment().getServerVersion()));
            supplyingFacilityNameTv.setText("From: " + orderShipment.getShipment().getSupplyingFacilityName());
            receivingFacilityNameTv.setText("To: " + orderShipment.getShipment().getReceivingFacilityName());
            processingPeriodStartTv.setText("Processing Period Start Date: " + getUserFriendlyDate(orderShipment.getShipment().getProcessingPeriodStartDate()));
            processingPeriodEndTv.setText("Processing Period End Date: " + getUserFriendlyDate(orderShipment.getShipment().getProcessingPeriodEndDate()));

            ShipmentLineItem[] shipmentLineItems = orderShipment.getShipment().getShipmentLineItems();

            for(ShipmentLineItem shipmentLineItem: shipmentLineItems) {
                //Todo: Add colouring
                stockItemsTl.addView(createTableRow(shipmentLineItem));
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

    private TableRow createTableRow(ShipmentLineItem shipmentLineItem) {
        TableRow tableRow = new TableRow(context);
        tableRow.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        tableRow.addView(createTextViewCell(shipmentLineItem.getAntigenType()));
        tableRow.addView(createTextViewCell(shipmentLineItem.getOrderedQuantity() + ""));
        tableRow.addView(createTextViewCell(shipmentLineItem.getShippedQuantity() + ""));
        tableRow.addView(createTextViewCell(shipmentLineItem.getNumberOfDoses() + ""));

        return tableRow;
    }

    private TextView createTextViewCell(String text) {
        TextView cellTv = new TextView(context);
        cellTv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        cellTv.setText(text);

        return cellTv;
    }

    private String getUserFriendlyDate(long dateLong) {
        return getUserFriendlyDate(new Date(dateLong));
    }

    private String getUserFriendlyDate(Date date) {
        return dateFormat.format(date);
    }

}
