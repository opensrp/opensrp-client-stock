package org.smartregister.stock.openlmis.domain.openlmis;

import java.util.List;

public class FacilityProgram extends BaseEntity {

    private String openlmisUuid;
    private String facilityName;
    private String facilityTypeUuid;
    private List<Program> supportedPrograms;

    public FacilityProgram() {}

    public FacilityProgram(String openlmisUuid, String facilityName, String facilityTypeUuid) {
        this.openlmisUuid = openlmisUuid;
        this.facilityName = facilityName;
        this.facilityTypeUuid = facilityTypeUuid;
    }

    public String getOpenlmisUuid() {
        return openlmisUuid;
    }

    public void setOpenlmisUuid(String openlmisUuid) {
        this.openlmisUuid = openlmisUuid;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public String getFacilityTypeUuid() {
        return facilityTypeUuid;
    }

    public void setFacilityTypeUuid(String facilityTypeUuid) {
        this.facilityTypeUuid = facilityTypeUuid;
    }

    public List<Program> getSupportedPrograms() {
        return supportedPrograms;
    }

    public void setSupportedPrograms(List<Program> supportedPrograms) {
        this.supportedPrograms = supportedPrograms;
    }
}
