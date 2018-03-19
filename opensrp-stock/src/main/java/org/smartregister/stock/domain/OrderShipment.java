package org.smartregister.stock.domain;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.smartregister.stock.R;
import org.smartregister.stock.util.Constants;

/**
 * Created by Ephraim Kigamba - ekigamba@ona.io on 08/03/2018.
 */

public class OrderShipment {

    private Shipment shipment;
    private Order order;

    public Shipment getShipment() {
        return shipment;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getOrderStatus(@NonNull Context context) {
        String acceptStatus;

        if (shipment != null) {
            acceptStatus = shipment.getShipmentAcceptStatus();
        } else {
            return context.getString(R.string.ordered);
        }

        if (acceptStatus == Constants.Shipment.ACCEPT_STATUS_NO_ACTION) {
            return context.getString(R.string.shipped);
        }

        if (acceptStatus.equals(Constants.Shipment.ACCEPT_STATUS_FULLY_ACCEPTED)) {
            return context.getString(R.string.accepted);
        }

        if (acceptStatus.equals(Constants.Shipment.ACCEPT_STATUS_PARTIALLY_ACCEPTED)) {
            return context.getString(R.string.part_accepted);
        }

        if (acceptStatus.equals(Constants.Shipment.ACCEPT_STATUS_REJECTED)) {
            return context.getString(R.string.rejected);
        }

        return "";
    }

    public int getStatusResource() {
        if (shipment == null) {
            return R.drawable.ordered_shipment_status;
        }

        String acceptStatus = shipment.getShipmentAcceptStatus();

        if (acceptStatus == Constants.Shipment.ACCEPT_STATUS_NO_ACTION) {
            return R.drawable.shipped_shipment_status;
        }

        if (acceptStatus.equals(Constants.Shipment.ACCEPT_STATUS_FULLY_ACCEPTED)) {
            return R.drawable.accepted_shipment_status;
        }

        if (acceptStatus.equals(Constants.Shipment.ACCEPT_STATUS_PARTIALLY_ACCEPTED)) {
            return R.drawable.part_accepted_shipment_status;
        }

        if (acceptStatus.equals(Constants.Shipment.ACCEPT_STATUS_REJECTED)) {
            return R.drawable.rejected_shipment_status;
        }

        return R.drawable.ordered_shipment_status;
    }
}
