package org.smartregister.stock.openlmis.domain.openlmis;

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
