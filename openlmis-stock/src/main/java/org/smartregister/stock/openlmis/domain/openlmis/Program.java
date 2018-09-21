package org.smartregister.stock.openlmis.domain.openlmis;

public class Program extends BaseEntity {

    private Code code;
    private String name;
    private String description;
    private Boolean active;
    private Boolean periodsSkippable;
    private Boolean skipAuthorization;
    private Boolean showNonFullSupplyTab;
    private Boolean enableDatePhysicalStockCountCompleted;
    private Long dateUpdated;

    public Program() {
        code = null;
    }

    public Program(String id) {
        this.id = id;
    }

    public Program(String id, Code code, String name, String description, Boolean active) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
        this.active = active;
    }

    /**
     * Equal by a Program's code.
     *
     * @param other the other Program
     * @return true if the two Program's ids are equal.
     */
    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof Program)) {
            return false;
        }

        Program otherProgram = (Program) other;
        return otherProgram.id.equals(id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public Code getCode() {
        return code;
    }

    public void setCode(Code code) {
        this.code = code;
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getPeriodsSkippable() {
        return periodsSkippable;
    }

    public void setPeriodsSkippable(Boolean periodsSkippable) {
        this.periodsSkippable = periodsSkippable;
    }

    public Boolean getSkipAuthorization() {
        return skipAuthorization;
    }

    public void setSkipAuthorization(Boolean skipAuthorization) {
        this.skipAuthorization = skipAuthorization;
    }

    public Boolean getShowNonFullSupplyTab() {
        return showNonFullSupplyTab;
    }

    public void setShowNonFullSupplyTab(Boolean showNonFullSupplyTab) {
        this.showNonFullSupplyTab = showNonFullSupplyTab;
    }

    public Boolean getEnableDatePhysicalStockCountCompleted() {
        return enableDatePhysicalStockCountCompleted;
    }

    public void setEnableDatePhysicalStockCountCompleted(Boolean enableDatePhysicalStockCountCompleted) {
        this.enableDatePhysicalStockCountCompleted = enableDatePhysicalStockCountCompleted;
    }

    public Long getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Long dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    @Override
    public String toString() {
        return name;
    }
}


