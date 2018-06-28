package org.smartregister.stock.management.domain;

import java.util.UUID;

public class BaseEntity {

    protected UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
