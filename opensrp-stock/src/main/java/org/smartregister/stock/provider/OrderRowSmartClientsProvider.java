package org.smartregister.stock.provider;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import org.smartregister.stock.R;
import org.smartregister.stock.domain.OrderShipment;
import org.smartregister.stock.domain.Shipment;
import org.smartregister.stock.repository.OrderRepository;
import org.smartregister.stock.util.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ephraim Kigamba - ekigamba@ona.io on 16/03/2018.
 */

public class OrderRowSmartClientsProvider {

    private LayoutInflater layoutInflater;
    private OrderRepository orderRepository;
    private Context context;

    public OrderRowSmartClientsProvider(@NonNull Context context, @NonNull OrderRepository orderRepository) {
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.orderRepository = orderRepository;
        this.context = context;
    }

    public View getView(View convertView, @NonNull OrderShipment orderShipment) {
        if (convertView == null) {
            convertView = inflateLayoutForCursorAdapter();
        }

        TextView orderDate = (TextView) convertView.findViewById(R.id.tv_orderItemRow_orderDate);
        TextView orderStatus = (TextView) convertView.findViewById(R.id.tv_orderItemRow_orderStatus);

        orderDate.setText(getFriendlyDate(orderShipment.getOrder().getDateCreatedByClient()));

        orderStatus.setTextColor(Color.WHITE);

        orderStatus.setText(orderShipment.getOrderStatus(context));
        orderStatus.setBackgroundResource(orderShipment.getStatusResource());

        return convertView;
    }

    private View inflateLayoutForCursorAdapter() {
        return layoutInflater.inflate(R.layout.order_item_row, null);
    }

    private String getFriendlyDate(long longDate) {
        Date date = new Date(longDate);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d MMM yyyy");
        return simpleDateFormat.format(date);
    }
}
