package org.smartregister.stock.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.smartregister.stock.R;
import org.smartregister.stock.domain.OrderShipment;
import org.smartregister.stock.util.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Ephraim Kigamba - ekigamba@ona.io on 08/03/2018.
 */

public class OrderListAdapter extends BaseAdapter {

    private List<OrderShipment> orderShipmentList;
    private Context context;

    public OrderListAdapter(Context context, @NonNull List<OrderShipment> orderShipmentList) {
        this.orderShipmentList = orderShipmentList;
        this.context = context;
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
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.order_item_row, null);
        }

        TextView orderDate = (TextView) convertView.findViewById(R.id.tv_orderItemRow_orderDate);
        TextView orderStatus = (TextView) convertView.findViewById(R.id.tv_orderItemRow_orderStatus);

        OrderShipment orderShipment = (OrderShipment) getItem(position);
        orderDate.setText(getFriendlyDate(orderShipment.getOrder().getDateCreatedByClient()));

        if (orderShipment.getShipment() != null) {
            orderStatus.setText(getOrderStatus(orderShipment.getShipment().getShipmentAcceptStatus()));
        } else {
            orderStatus.setText("Not Shipped");
        }

        return convertView;
    }

    private String getFriendlyDate(long longDate) {
        Date date = new Date(longDate);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d MMM YYYY");
        return simpleDateFormat.format(date);
    }

    private String getOrderStatus(String acceptStatus) {

        if (acceptStatus.equals(Constants.Shipment.ACCEPT_STATUS_FULLY_ACCEPTED)) {
            return "Accepted";
        }

        if (acceptStatus.equals(Constants.Shipment.ACCEPT_STATUS_PARTIALLY_ACCEPTED)) {
            return "Part. Accepted";
        }

        if (acceptStatus.equals(Constants.Shipment.ACCEPT_STATUS_REJECTED)) {
            return "Rejected";
        }

        if (acceptStatus.equals(Constants.Shipment.ACCEPT_STATUS_NO_ACTION)) {
            return "Shipped";
        }

        return "Awaiting Shipment";
    }
}
