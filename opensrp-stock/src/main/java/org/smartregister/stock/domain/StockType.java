package org.smartregister.stock.domain;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by samuelgithengi on 2/6/18.
 */

public class StockType implements Serializable {

    private Long id;
    private int quantity;
    private Long uniqueId;
    @SerializedName("productName")
    private String name;
    private String materialNumber;
    private String condition;
    private String appropriateUsage;
    private String accountabilityPeriod;
    private String availability;
    private String isAttractiveItem;
    private String openmrsParentEntityId;
    private String openmrsDateConceptId;
    private String openmrsQuantityConceptId;

    public StockType() {
    }

    public StockType(Long id, int quantity, String name, String openmrsParentEntityId, String openmrsDateConceptId, String openmrsQuantityConceptId) {
        this.id = id;
        this.quantity = quantity;
        this.name = name;
        this.openmrsParentEntityId = openmrsParentEntityId;
        this.openmrsDateConceptId = openmrsDateConceptId;
        this.openmrsQuantityConceptId = openmrsQuantityConceptId;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOpenmrsParentEntityId() {
        return openmrsParentEntityId;
    }

    public void setOpenmrsParentEntityId(String openmrsParentEntityId) {
        this.openmrsParentEntityId = openmrsParentEntityId;
    }

    public String getOpenmrsQuantityConceptId() {
        return openmrsQuantityConceptId;
    }

    public void setOpenmrsQuantityConceptId(String openmrsQuantityConceptId) {
        this.openmrsQuantityConceptId = openmrsQuantityConceptId;
    }

    public String getOpenmrsDateConceptId() {
        return openmrsDateConceptId;
    }

    public void setOpenmrsDateConceptId(String openmrsDateConceptId) {
        this.openmrsDateConceptId = openmrsDateConceptId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMaterialNumber() {
        return materialNumber;
    }

    public void setMaterialNumber(String materialNumber) {
        this.materialNumber = materialNumber;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getAppropriateUsage() {
        return appropriateUsage;
    }

    public void setAppropriateUsage(String appropriateUsage) {
        this.appropriateUsage = appropriateUsage;
    }

    public String getAccountabilityPeriod() {
        return accountabilityPeriod;
    }

    public void setAccountabilityPeriod(String accountabilityPeriod) {
        this.accountabilityPeriod = accountabilityPeriod;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getIsAttractiveItem() {
        return isAttractiveItem;
    }

    public void setIsAttractiveItem(String isAttractiveItem) {
        this.isAttractiveItem = isAttractiveItem;
    }

    public Long getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(Long uniqueId) {
        this.uniqueId = uniqueId;
    }
}
