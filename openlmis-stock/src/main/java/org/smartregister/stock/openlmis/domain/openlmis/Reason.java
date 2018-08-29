package org.smartregister.stock.openlmis.domain.openlmis;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Reason extends BaseEntity {

    @JsonProperty
    private String programId;
    @JsonProperty
    private String facilityType;
    @JsonProperty
    private StockCardLineItemReason stockCardLineItemReason;
    @JsonProperty
    private Long dateUpdated;

    public Reason(String id, String programId, String facilityType, StockCardLineItemReason stockCardLineItemReason) {
        this.id = id;
        this.programId = programId;
        this.facilityType = facilityType;
        this.stockCardLineItemReason = stockCardLineItemReason;
    }

    public String getProgramId() {
        return programId;
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }

    public String getFacilityType() {
        return facilityType;
    }

    public void setFacilityType(String facilityType) {
        this.facilityType = facilityType;
    }

    public StockCardLineItemReason getStockCardLineItemReason() {
        return stockCardLineItemReason;
    }

    public void setStockCardLineItemReason(StockCardLineItemReason stockCardLineItemReason) {
        this.stockCardLineItemReason = stockCardLineItemReason;
    }

    public Long getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Long dateUpdated) {
        this.dateUpdated = dateUpdated;
    }
}
