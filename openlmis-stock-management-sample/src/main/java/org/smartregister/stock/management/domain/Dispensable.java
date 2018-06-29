package org.smartregister.stock.management.domain;

import java.util.HashMap;
import java.util.Map;

public abstract class Dispensable extends BaseEntity {

    public static final String KEY_DISPENSING_UNIT = "dispensingUnit";
    public static final String KEY_SIZE_CODE = "sizeCode";
    public static final String KEY_ROUTE_OF_ADMINISTRATION = "routeOfAdministration";

    protected Map<String, String> attributes;

    protected Dispensable() {
        attributes = new HashMap<>();
    }

    public abstract boolean equals(Object object);

    // TODO: this has to have some extra logic to test equality
    public int hashCode() {
        return 1;
    }

    public abstract String toString();
}
