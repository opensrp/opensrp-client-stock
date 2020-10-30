package org.smartregister.stock.openlmis.domain;

import androidx.annotation.NonNull;

/**
 * Created by samuelgithengi on 9/26/18.
 */
public class StockTake implements Comparable<StockTake> {

    private String programId;

    private String commodityTypeId;

    private String tradeItemId;

    private String lotId;

    private String status;

    private int quantity;

    private String reason;

    private String reasonId;

    private long lastUpdated;

    private boolean noChange;

    private boolean valid;

    private boolean displayStatus;


    public StockTake(String programId, String commodityTypeId, String tradeItemId, String lotId) {
        this.programId = programId;
        this.commodityTypeId = commodityTypeId;
        this.tradeItemId = tradeItemId;
        this.lotId = lotId;
    }

    public String getProgramId() {
        return programId;
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }

    public String getCommodityTypeId() {
        return commodityTypeId;
    }

    public void setCommodityTypeId(String commodityTypeId) {
        this.commodityTypeId = commodityTypeId;
    }

    public String getTradeItemId() {
        return tradeItemId;
    }

    public void setTradeItemId(String tradeItemId) {
        this.tradeItemId = tradeItemId;
    }

    public String getLotId() {
        return lotId;
    }

    public void setLotId(String lotId) {
        this.lotId = lotId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReasonId() {
        return reasonId;
    }

    public void setReasonId(String reasonId) {
        this.reasonId = reasonId;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public boolean isNoChange() {
        return noChange;
    }

    public void setNoChange(boolean noChange) {
        this.noChange = noChange;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public boolean isDisplayStatus() {
        return displayStatus;
    }

    public void setDisplayStatus(boolean displayStatus) {
        this.displayStatus = displayStatus;
    }

    @Override
    public int compareTo(@NonNull StockTake other) {
        return Long.valueOf(other.getLastUpdated()).compareTo(getLastUpdated());
    }

    @Override
    public int hashCode() {
        if (lotId == null)
            return (programId + commodityTypeId + tradeItemId).hashCode();
        else
            return (programId + commodityTypeId + tradeItemId + lotId).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof StockTake))
            return false;
        StockTake other = (StockTake) obj;
        if (lotId != null) {
            return other.getProgramId().equals(programId) &&
                    other.getCommodityTypeId().equals(commodityTypeId) &&
                    other.getTradeItemId().equals(tradeItemId) &&
                    other.getLotId().equals(lotId);
        } else {
            return other.getProgramId().equals(programId) &&
                    other.getTradeItemId().equals(tradeItemId) &&
                    other.getTradeItemId().equals(tradeItemId);
        }
    }
}
