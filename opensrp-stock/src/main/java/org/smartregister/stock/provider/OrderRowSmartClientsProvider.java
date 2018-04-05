package org.smartregister.stock.provider;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import org.smartregister.stock.R;
import org.smartregister.stock.domain.OrderShipment;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ephraim Kigamba - ekigamba@ona.io on 16/03/2018.
 */

public class OrderRowSmartClientsProvider {

    private LayoutInflater layoutInflater;
    private Context context;

    public OrderRowSmartClientsProvider(@NonNull Context context) {
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
    }

    public View getView(View view, @NonNull OrderShipment orderShipment, boolean isEnabled) {
        View convertView = view;
        if (convertView == null) {
            convertView = inflateLayoutForCursorAdapter();
        }

        TextView orderDate = (TextView) convertView.findViewById(R.id.tv_orderItemRow_orderDate);
        TextView orderStatus = (TextView) convertView.findViewById(R.id.tv_orderItemRow_orderStatus);

        orderDate.setText(getFriendlyDate(orderShipment.getOrder().getDateCreatedByClient()));

        orderStatus.setTextColor(Color.WHITE);

        orderStatus.setText(orderShipment.getOrderStatus(context));
        orderStatus.setBackgroundResource(orderShipment.getStatusResource());

        if (isEnabled) {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.transparent));
        } else {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.disabled_color));
        }

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
