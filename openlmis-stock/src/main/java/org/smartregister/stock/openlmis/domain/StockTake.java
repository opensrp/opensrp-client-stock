package org.smartregister.stock.openlmis.domain;

/**
 * Created by samuelgithengi on 9/26/18.
 */
public class StockTake {

    private String programId;

    private String tradeItemId;

    private String lotId;

    private String status;

    private int quantity;

    private String reasonId;

    private long lastUpdated;

    private boolean valid;

    public StockTake(String programId, String tradeItemId) {
        this.programId = programId;
        this.tradeItemId = tradeItemId;
    }

    public StockTake(String programId, String tradeItemId, String lotId) {
        this(programId, tradeItemId);
        this.lotId = lotId;
    }

    public String getProgramId() {
        return programId;
    }

    public void setProgramId(String programId) {
        this.programId = programId;
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

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }


}
