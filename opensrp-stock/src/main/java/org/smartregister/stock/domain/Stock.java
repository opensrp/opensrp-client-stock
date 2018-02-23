package org.smartregister.stock.domain;

/**
 * Created by samuelgithengi on 2/6/18.
 */
public class Stock {
    private Long id;
    private String stockTypeId;
    private String transactionType;
    private String providerid;
    private int value;
    private Long dateCreated;
    private String toFrom;
    private String syncStatus;
    private Long dateUpdated;

    public static final String issued = "issued";
    public static final String received = "received";
    public static final String loss_adjustment = "loss_adjustment";

    public Stock(Long id, String transactionType, String providerid, int value, Long dateCreated, String toFrom, String syncStatus, Long dateUpdated, String stockTypeId) {
        this.id = id;
        this.transactionType = transactionType;
        this.providerid = providerid;
        this.value = value;
        this.dateCreated = dateCreated;
        this.toFrom = toFrom;
        this.syncStatus = syncStatus;
        this.dateUpdated = dateUpdated;
        this.stockTypeId = stockTypeId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStockTypeId() {
        return stockTypeId;
    }

    public void setStockTypeId(String stockTypeId) {
        this.stockTypeId = stockTypeId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getToFrom() {
        return toFrom;
    }

    public void setToFrom(String toFrom) {
        this.toFrom = toFrom;
    }

    public Long getUpdatedAt() {
        return dateUpdated;
    }

    public void setUpdatedAt(Long dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public String getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(String sync_status) {
        this.syncStatus = sync_status;
    }

    public String getProviderid() {
        return providerid;
    }

    public void setProviderid(String providerid) {
        this.providerid = providerid;
    }
}
