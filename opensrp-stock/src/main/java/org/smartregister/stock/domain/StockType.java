package org.smartregister.stock.domain;

import java.io.Serializable;

/**
 * Created by samuelgithengi on 2/6/18.
 */

public class StockType implements Serializable {

    private Long id;
    private int quantity;
    private String name;
    private String openmrs_parent_entity_id;
    private String openmrs_date_concept_id;
    private String openmrs_quantity_concept_id;

    public StockType(Long id, int quantity, String name, String openmrs_parent_entity_id, String openmrs_date_concept_id, String openmrs_quantity_concept_id) {
        this.id = id;
        this.quantity = quantity;
        this.name = name;
        this.openmrs_parent_entity_id = openmrs_parent_entity_id;
        this.openmrs_date_concept_id = openmrs_date_concept_id;
        this.openmrs_quantity_concept_id = openmrs_quantity_concept_id;
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

    public String getOpenmrs_parent_entity_id() {
        return openmrs_parent_entity_id;
    }

    public void setOpenmrs_parent_entity_id(String openmrs_parent_entity_id) {
        this.openmrs_parent_entity_id = openmrs_parent_entity_id;
    }

    public String getOpenmrs_quantity_concept_id() {
        return openmrs_quantity_concept_id;
    }

    public void setOpenmrs_quantity_concept_id(String openmrs_quantity_concept_id) {
        this.openmrs_quantity_concept_id = openmrs_quantity_concept_id;
    }

    public String getOpenmrs_date_concept_id() {
        return openmrs_date_concept_id;
    }

    public void setOpenmrs_date_concept_id(String openmrs_date_concept_id) {
        this.openmrs_date_concept_id = openmrs_date_concept_id;
    }
}
