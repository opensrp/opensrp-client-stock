package org.smartregister.stock.domain;

import java.io.Serializable;

/**
 * Created by samuelgithengi on 2/6/18.
 */

public class StockType implements Serializable {

    private Long id;
    private int quantity;
    private String name;
    private String openmrsParentEntityId;
    private String openmrsDateConceptId;
    private String openmrsQuantityConceptId;

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
}
