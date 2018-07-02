package org.smartregister.stock.management.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Dispensable extends BaseEntity {

    public static final String KEY_DISPENSING_UNIT = "dispensingUnit";
    public static final String KEY_SIZE_CODE = "sizeCode";
    public static final String KEY_ROUTE_OF_ADMINISTRATION = "routeOfAdministration";

    protected Map<String, String> attributes;
    private Long dateUpdated;

    protected Dispensable() {
        attributes = new HashMap<>();
    }

    public Dispensable(UUID id, String keyDispensingUnit, String keySizeCode, String keyRouteOfAdministration) {
        this.id = id;
        attributes = new HashMap<>();
        attributes.put(KEY_DISPENSING_UNIT, keyDispensingUnit);
        attributes.put(KEY_SIZE_CODE, keySizeCode);
        attributes.put(keyRouteOfAdministration, KEY_ROUTE_OF_ADMINISTRATION);
    }

    // public abstract boolean equals(Object object);

    // TODO: this has to have some extra logic to test equality
//    public int hashCode() {
//        return 1;
//    }

    // public abstract String toString();

    public Long getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Long dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }
}
