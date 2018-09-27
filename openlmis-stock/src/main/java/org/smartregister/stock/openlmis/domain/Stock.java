package org.smartregister.stock.openlmis.domain;

/**
 * Created by samuelgithengi on 26/7/18.
 */
public class Stock extends org.smartregister.stock.domain.Stock {

    private String lotId;

    private String programId;

    private String reason;

    private String vvmStatus;

    public Stock(Long id, String transactionType, String providerid, int value, Long dateCreated,
                 String toFrom, String syncStatus, Long dateUpdated, String tradeItemId) {
        super(id, transactionType, providerid, value, dateCreated, toFrom, syncStatus, dateUpdated,
                tradeItemId);
    }

    public String getLotId() {
        return lotId;
    }

    public void setLotId(String lotId) {
        this.lotId = lotId;
    }

    public String getProgramId() {
        return programId;
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getvvmStatus() {
        return vvmStatus;
    }

    public void setvvmStatus(String status) {
        this.vvmStatus = status;
    }
}
