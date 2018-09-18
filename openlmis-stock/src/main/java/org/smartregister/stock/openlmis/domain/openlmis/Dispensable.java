package org.smartregister.stock.openlmis.domain.openlmis;

public class Dispensable extends BaseEntity {


    private String keyDispensingUnit;
    private String keySizeCode;
    private String keyRouteOfAdministration;
    private Long dateUpdated;

    public Dispensable(String id) {
        this.id = id;
    }

    public Dispensable(String id, String keyDispensingUnit, String keySizeCode, String keyRouteOfAdministration) {
        this.id = id;
        this.keyDispensingUnit = keyDispensingUnit;
        this.keySizeCode = keySizeCode;
        this.keyRouteOfAdministration = keyRouteOfAdministration;
    }

    public String getKeyDispensingUnit() {
        return keyDispensingUnit;
    }

    public void setKeyDispensingUnit(String keyDispensingUnit) {
        this.keyDispensingUnit = keyDispensingUnit;
    }

    public String getKeySizeCode() {
        return keySizeCode;
    }

    public void setKeySizeCode(String keySizeCode) {
        this.keySizeCode = keySizeCode;
    }

    public String getKeyRouteOfAdministration() {
        return keyRouteOfAdministration;
    }

    public void setKeyRouteOfAdministration(String keyRouteOfAdministration) {
        this.keyRouteOfAdministration = keyRouteOfAdministration;
    }

    public Long getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Long dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

}
