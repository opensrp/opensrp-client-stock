package org.smartregister.stock.domain;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by samuelgithengi on 2/6/18.
 */
public class Stock implements Serializable {
    @SerializedName("_id")
    private Long id;
    private String stockTypeId;
    @SerializedName("transaction_type")
    private String transactionType;
    private String providerid;
    private int value;
    @SerializedName("date_created")
    private Long dateCreated;
    @SerializedName("to_from")
    private String toFrom;
    private String syncStatus;
    @SerializedName("date_updated")
    private Long dateUpdated;
    private String locationId;
    private String childLocationId;
    private String team;
    private String teamId;
    private String identifier;
    private Object customProperties;
    @SerializedName("id")
    private String stockId;
    private String serialNumber;
    private String deliveryDate;
    private String accountabilityEndDate;
    private String type;
    private String donor;
    private Long version;
    private Long serverVersion;

    public static final String issued = "issued";
    public static final String received = "received";
    public static final String loss_adjustment = "loss_adjustment";
    public static final String stock_take = "stock_take";

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

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getChildLocationId() {
        return childLocationId;
    }

    public void setChildLocationId(String childLocationId) {
        this.childLocationId = childLocationId;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public Long getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Long dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public static String getIssued() {
        return issued;
    }

    public static String getReceived() {
        return received;
    }

    public static String getLossAdjusment() {
        return loss_adjustment;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Object getCustomProperties() {
        return customProperties;
    }

    public void setCustomProperties(Object customProperties) {
        this.customProperties = customProperties;
    }

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getAccountabilityEndDate() {
        return accountabilityEndDate;
    }

    public void setAccountabilityEndDate(String accountabilityEndDate) {
        this.accountabilityEndDate = accountabilityEndDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDonor() {
        return donor;
    }

    public void setDonor(String donor) {
        this.donor = donor;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Long getServerVersion() {
        return serverVersion;
    }

    public void setServerVersion(Long serverVersion) {
        this.serverVersion = serverVersion;
    }
}
