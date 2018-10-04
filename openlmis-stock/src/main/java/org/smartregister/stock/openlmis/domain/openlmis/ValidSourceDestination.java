package org.smartregister.stock.openlmis.domain.openlmis;

public class ValidSourceDestination extends BaseEntity {

    private String programUuid;
    private String facilityTypeUuid;
    private String facilityName;
    private String openlmisUuid;
    private Long dateUpdated;
    private boolean isSource;

    public ValidSourceDestination() {}

    public ValidSourceDestination(int id, String programUuid, String facilityTypeUuid, String facilityName, String openlmisUuid, boolean isSource) {
        this.id = String.valueOf(id);
        this.programUuid = programUuid;
        this.facilityTypeUuid = facilityTypeUuid;
        this.facilityName = facilityName;
        this.openlmisUuid = openlmisUuid;
        this.isSource = isSource;
    }

    public String getProgramUuid() {
        return programUuid;
    }

    public void setProgramUuid(String programUuid) {
        this.programUuid = programUuid;
    }

    public String getFacilityTypeUuid() {
        return facilityTypeUuid;
    }

    public void setFacilityTypeUuid(String facilityTypeUuid) {
        this.facilityTypeUuid = facilityTypeUuid;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public String getOpenlmisUuid() {
        return openlmisUuid;
    }

    public void setOpenlmisUuid(String openlmisUuid) {
        this.openlmisUuid = openlmisUuid;
    }

    public boolean isSource() {
        return isSource;
    }

    public void setSource(boolean source) {
        isSource = source;
    }

    public Long getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(long dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public void setDateUpdated(Long dateUpdated) {
        this.dateUpdated = dateUpdated;
    }
}
