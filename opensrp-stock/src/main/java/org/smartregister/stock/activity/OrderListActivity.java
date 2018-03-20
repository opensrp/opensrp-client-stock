package org.smartregister.stock.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.TypedValue;
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
import org.smartregister.stock.provider.OrderRowSmartClientsProvider;
import org.smartregister.stock.repository.OrderRepository;
import org.smartregister.stock.sync.OrdersSyncIntentService;

import java.util.List;
import java.util.UUID;

import static org.smartregister.util.Log.logError;

public class OrderListActivity extends BasicOrderActivity {

    @Override
    protected void onResume() {
        super.onResume();
        fetchOrdersShipmentsFromDbAndRender();
    }

    private void fetchOrdersShipmentsFromDbAndRender() {
        OrderRepository orderRepository = StockLibrary.getInstance().getOrderRepository();
        final List<OrderShipment> orderShipmentList = orderRepository.getAllOrdersWithShipments();

        OrderRowSmartClientsProvider orderRowSmartClientsProvider = new OrderRowSmartClientsProvider(this);
        OrderListAdapter orderListAdapter = new OrderListAdapter(orderShipmentList, orderRowSmartClientsProvider);

        ListView ordersListView = (ListView) findViewById(R.id.lv_orders_ordersList);
        ordersListView.setAdapter(orderListAdapter);

        ordersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OrderShipment orderShipment = orderShipmentList.get(position);

                if (!orderShipment.getOrderStatus(OrderListActivity.this).equals(getString(R.string.ordered))) {
                    Intent intent = new Intent(OrderListActivity.this, OrderDetailsActivity.class);

                    Gson gson = new Gson();
                    String orderShipmentJSONString = gson.toJson(orderShipmentList.get(position));

                    intent.putExtra(OrderDetailsActivity.ORDER_SHIPMENT_JSON, orderShipmentJSONString);
                    startActivity(intent);
                }
            }
        });
    }

    private void showCreateOrderConfirmationDialog(String facilityName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(R.string.new_order_dialog_title)
                .setMessage(getString(R.string.new_order_dialog_message) + facilityName)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        createNewOrder();
                        fetchOrdersShipmentsFromDbAndRender();
                        startOrdersSyncService();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // This should just dismiss the dialog which it already does
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

    private void startOrdersSyncService() {
        startService(new Intent(this, OrdersSyncIntentService.class));
    }

    protected String getLoggedInUserInitials() {
        try {
            AllSharedPreferences allSharedPreferences = new AllSharedPreferences(PreferenceManager.getDefaultSharedPreferences(this));

            String preferredName = allSharedPreferences.getANMPreferredName(
                    allSharedPreferences.fetchRegisteredANM());
            if (!TextUtils.isEmpty(preferredName)) {
                String[] initialsArray = preferredName.split(" ");
                String initials = "";
                if (initialsArray.length > 0) {
                    initials = initialsArray[0].substring(0, 1);
                    if (initialsArray.length > 1) {
                        initials = initials + initialsArray[1].substring(0, 1);
                    }
                }

                return initials.toUpperCase();
            }

        } catch (Exception e) {
            logError("Error on initView : Getting Preferences: Getting Initials");
        }

        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_order_list;
    }

    @Override
    protected int getStatusBarColor() {
        return getColorPrimaryDark();
    }

    @Override
    protected void afterOnCreate() {
        Button newOrderBtn = (Button) findViewById(R.id.btn_order_newOrder);
        newOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateOrderConfirmationDialog(getFacilityName());
            }
        });
    }

    private String getFacilityName() {
        AllSharedPreferences allSharedPreferences = new AllSharedPreferences(PreferenceManager.getDefaultSharedPreferences(this));
        return allSharedPreferences.fetchCurrentLocality();
    }

    private int getColorPrimaryDark() {
        TypedValue typedValue = new TypedValue();

        TypedArray typedArray = obtainStyledAttributes(typedValue.data, new int[] { R.attr.colorPrimaryDark });
        int color = typedArray.getColor(0, 0);

        typedArray.recycle();

        return color;
    }
}
