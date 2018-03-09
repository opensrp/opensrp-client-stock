package org.smartregister.stock.domain;

import java.util.Date;

/**
 * Created by Ephraim Kigamba - ekigamba@ona.io on 07/03/2018.
 */

public class Shipment {

    private String orderCode;
    private Date orderedDate;
    private String receivingFacilityCode;
    private String receivingFacilityName;
    private String supplyingFacilityCode;
    private String supplyingFacilityName;
    private Date processingPeriodStartDate;
    private Date processingPeriodEndDate;
    private String shipmentAcceptStatus;
    private boolean synced;
    private ShipmentLineItem[] shipmentLineItems;

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Date getOrderedDate() {
        return orderedDate;
    }

    public void setOrderedDate(Date orderedDate) {
        this.orderedDate = orderedDate;
    }

    public String getReceivingFacilityCode() {
        return receivingFacilityCode;
    }

    public void setReceivingFacilityCode(String receivingFacilityCode) {
        this.receivingFacilityCode = receivingFacilityCode;
    }

    public String getReceivingFacilityName() {
        return receivingFacilityName;
    }

    public void setReceivingFacilityName(String receivingFacilityName) {
        this.receivingFacilityName = receivingFacilityName;
    }

    public String getSupplyingFacilityCode() {
        return supplyingFacilityCode;
    }

    public void setSupplyingFacilityCode(String supplyingFacilityCode) {
        this.supplyingFacilityCode = supplyingFacilityCode;
    }

    public String getSupplyingFacilityName() {
        return supplyingFacilityName;
    }

    public void setSupplyingFacilityName(String supplyingFacilityName) {
        this.supplyingFacilityName = supplyingFacilityName;
    }

    public Date getProcessingPeriodStartDate() {
        return processingPeriodStartDate;
    }

    public void setProcessingPeriodStartDate(Date processingPeriodStartDate) {
        this.processingPeriodStartDate = processingPeriodStartDate;
    }

    public Date getProcessingPeriodEndDate() {
        return processingPeriodEndDate;
    }

    public void setProcessingPeriodEndDate(Date processingPeriodEndDate) {
        this.processingPeriodEndDate = processingPeriodEndDate;
    }

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }

    public String getShipmentAcceptStatus() {
        return shipmentAcceptStatus;
    }

    public void setShipmentAcceptStatus(String shipmentAcceptStatus) {
        this.shipmentAcceptStatus = shipmentAcceptStatus;
    }

    public ShipmentLineItem[] getShipmentLineItems() {
        return shipmentLineItems;
    }

    public void setShipmentLineItems(ShipmentLineItem[] shipmentLineItems) {
        this.shipmentLineItems = shipmentLineItems;
    }
}
