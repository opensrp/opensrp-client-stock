package org.smartregister.stock.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;

import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.stock.R;
import org.smartregister.stock.StockLibrary;
import org.smartregister.stock.adapter.OrderListAdapter;
import org.smartregister.stock.domain.Order;
import org.smartregister.stock.domain.OrderShipment;
import org.smartregister.stock.repository.OrderRepository;

import java.util.List;
import java.util.UUID;

public class OrderListActivity extends AppCompatActivity {

    public static final String LOCATION_ID = "";
    public static final String PROVIDER_ID = "";
    public static final String ANM_NAME = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        OrderRepository orderRepository = StockLibrary.getInstance().getOrderRepository();
        final List<OrderShipment> orderShipmentList = orderRepository.getAllOrdersWithShipments();
        OrderListAdapter orderListAdapter = new OrderListAdapter(this, orderShipmentList);

        ListView ordersListView = (ListView) findViewById(R.id.lv_orders_ordersList);
        ordersListView.setAdapter(orderListAdapter);

        ordersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(OrderListActivity.this, OrderDetailsActivity.class);

                Gson gson = new Gson();
                String orderShipmentJSONString = gson.toJson(orderShipmentList.get(position));

                intent.putExtra(OrderDetailsActivity.ORDER_SHIPMENT_JSON, orderShipmentJSONString);
                startActivity(intent);
            }
        });

        Button newOrderBtn = (Button) findViewById(R.id.btn_order_newOrder);
        newOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateOrderConfirmationDialog("Sample Facility");
            }
        });

    }

    private void showCreateOrderConfirmationDialog(String facilityName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Would you like to make a new Order?")
                .setMessage("Please confirm that you would like to order stock based on Ideal Stock Amounts for " + facilityName)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        createNewOrder();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void createNewOrder() {
        AllSharedPreferences allSharedPreferences = new AllSharedPreferences(PreferenceManager.getDefaultSharedPreferences(this));
        String providerId = allSharedPreferences.fetchRegisteredANM();
        String locationId = allSharedPreferences.fetchDefaultLocalityId(providerId);

        Order order = new Order();
        order.setId(UUID.randomUUID().toString());
        order.setLocationId(locationId);
        order.setProviderId(providerId);
        order.setDateCreatedByClient(System.currentTimeMillis());
        order.setSynced(false);

        OrderRepository orderRepository = StockLibrary.getInstance().getOrderRepository();
        orderRepository.addOrUpdateOrder(order);
    }
}
