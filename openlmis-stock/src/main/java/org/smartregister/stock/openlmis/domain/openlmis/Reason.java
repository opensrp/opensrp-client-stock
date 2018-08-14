package org.smartregister.stock.openlmis.domain.openlmis;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Reason extends BaseEntity {

    @JsonProperty
    private String name;
    @JsonProperty
    private Program program;
    @JsonProperty
    private String description;
    @JsonProperty
    private Boolean additive;
    @JsonProperty
    private Long dateUpdated;

    public Reason(String id, String name, Program program, String description, Boolean additive) {
        this.id = id;
        this.name = name;
        this.program = program;
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

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
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
