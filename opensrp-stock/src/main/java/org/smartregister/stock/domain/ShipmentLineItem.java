package org.smartregister.stock.domain;

/**
 * Created by Ephraim Kigamba - ekigamba@ona.io on 07/03/2018.
 */

public class ShipmentLineItem {

    private int id;
    private String shipmentOrderCode;
    private String antigenType;
    private int orderedQuantity;
    private int shippedQuantity;
    private int numberOfDoses;
    private int acceptedQuantity;

    public ShipmentLineItem(int id, String shipmentOrderCode, String antigenType, int orderedQuantity,
                            int shippedQuantity, int numberOfDoses, int acceptedQuantity) {
        this.id = id;
        this.shipmentOrderCode = shipmentOrderCode;
        this.antigenType = antigenType;
        this.orderedQuantity = orderedQuantity;
        this.shippedQuantity = shippedQuantity;
        this.numberOfDoses = numberOfDoses;
        this.acceptedQuantity = acceptedQuantity;
    }

    public ShipmentLineItem() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShipmentOrderCode() {
        return shipmentOrderCode;
    }

    public void setShipmentOrderCode(String shipmentOrderCode) {
        this.shipmentOrderCode = shipmentOrderCode;
    }

    public String getAntigenType() {
        return antigenType;
    }

    public void setAntigenType(String antigenType) {
        this.antigenType = antigenType;
    }

    public int getOrderedQuantity() {
        return orderedQuantity;
    }

    public void setOrderedQuantity(int orderedQuantity) {
        this.orderedQuantity = orderedQuantity;
    }

    public int getShippedQuantity() {
        return shippedQuantity;
    }

    public void setShippedQuantity(int shippedQuantity) {
        this.shippedQuantity = shippedQuantity;
    }

    public int getNumberOfDoses() {
        return numberOfDoses;
    }

    public void setNumberOfDoses(int numberOfDoses) {
        this.numberOfDoses = numberOfDoses;
    }

    public int getAcceptedQuantity() {
        return acceptedQuantity;
    }

    public void setAcceptedQuantity(int acceptedQuantity) {
        this.acceptedQuantity = acceptedQuantity;
    }
}
