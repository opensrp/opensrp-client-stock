package org.smartregister.stock.openlmis.domain.openlmis;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class StockCardLineItemReason {

    @JsonProperty
    private String id;

    @JsonProperty
    private String name;

    @JsonProperty
    private String description;

    @JsonProperty
    private String reasonType;

    @JsonProperty
    private String reasonCategory;

    @JsonProperty
    private Boolean isFreeTextAllowed;

    @JsonProperty
    private List<String> tags;

    public StockCardLineItemReason(String id, String name, String description, String reasonType, String reasonCategory, Boolean isFreeTextAllowed) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.reasonType = reasonType;
        this.reasonCategory = reasonCategory;
        this.isFreeTextAllowed = isFreeTextAllowed;
    }

    public String getReasonType() {
        return reasonType;
    }

    public void setReasonType(String reasonType) {
        this.reasonType = reasonType;
    }

    public String getReasonCategory() {
        return reasonCategory;
    }

    public void setReasonCategory(String reasonCategory) {
        this.reasonCategory = reasonCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getFreeTextAllowed() {
        return isFreeTextAllowed;
    }

    public void setFreeTextAllowed(Boolean freeTextAllowed) {
        isFreeTextAllowed = freeTextAllowed;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
