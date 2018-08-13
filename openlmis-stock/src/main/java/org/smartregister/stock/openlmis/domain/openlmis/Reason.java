package org.smartregister.stock.openlmis.domain.openlmis;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Reason extends BaseEntity {

    @JsonProperty
    private String name;
    @JsonProperty
    private String programId;
    @JsonProperty
    private String description;
    @JsonProperty
    private Boolean additive;
    @JsonProperty
    private Long dateUpdated;

    public Reason(String id, String name, String programId, String description, Boolean additive) {
        this.id = id;
        this.name = name;
        this.programId = programId;
        this.description = description;
        this.additive = additive;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProgram() {
        return programId;
    }

    public void setProgram(String programId) {
        this.programId = programId;
    }

    public Boolean getAdditive() {
        return additive;
    }

    public void setAdditive(Boolean additive) {
        this.additive = additive;
    }

    public Long getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Long dateUpdated) {
        this.dateUpdated = dateUpdated;
    }
}
