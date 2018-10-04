package org.smartregister.stock.openlmis.domain.openlmis;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Reason extends BaseEntity {

    @JsonProperty
    private String programId;
    @JsonProperty
    private String facilityTypeUuid;
    @JsonProperty
    private StockCardLineItemReason stockCardLineItemReason;
    @JsonProperty
    private Long dateUpdated;

    public Reason(String id, String programId, String facilityTypeUuid, StockCardLineItemReason stockCardLineItemReason) {
        this.id = id;
        this.programId = programId;
        this.facilityTypeUuid = facilityTypeUuid;
        this.stockCardLineItemReason = stockCardLineItemReason;
    }

    public String getProgramId() {
        return programId;
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }

    public String getFacilityTypeUuid() {
        return facilityTypeUuid;
    }

    public void setFacilityTypeUuid(String facilityTypeUuid) {
        this.facilityTypeUuid = facilityTypeUuid;
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
