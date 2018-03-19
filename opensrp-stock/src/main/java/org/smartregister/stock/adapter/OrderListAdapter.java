package org.smartregister.stock.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.smartregister.stock.domain.OrderShipment;
import org.smartregister.stock.provider.OrderRowSmartClientsProvider;

import java.util.List;

/**
 * Created by Ephraim Kigamba - ekigamba@ona.io on 08/03/2018.
 */

public class OrderListAdapter extends BaseAdapter {

    private List<OrderShipment> orderShipmentList;
    private Context context;
    private OrderRowSmartClientsProvider orderRowSmartClientsProvider;

    public OrderListAdapter(@NonNull Context context, @NonNull List<OrderShipment> orderShipmentList, @NonNull OrderRowSmartClientsProvider orderRowSmartClientsProvider) {
        this.context = context;
        this.orderShipmentList = orderShipmentList;
        this.orderRowSmartClientsProvider = orderRowSmartClientsProvider;
    }

    @Override
    public int getCount() {
        return orderShipmentList.size();
    }

    @Override
    public Object getItem(int position) {
        return orderShipmentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OrderShipment orderShipment = (OrderShipment) getItem(position);
        return orderRowSmartClientsProvider.getView(convertView, orderShipment);
    }
}
