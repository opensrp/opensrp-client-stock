package org.smartregister.stock.openlmis.domain.openlmis;

import java.util.Objects;

public class ProgramOrderable extends BaseEntity {

    private String programId;
    private String orderableId;
    private Integer dosesPerPatient;
    private boolean active;
    private boolean fullSupply;
    private Long dateUpdated;

    public ProgramOrderable(String id, String programId, String orderableId, Integer dosesPerPatient, boolean active, boolean fullSupply, long dateUpdated) {
        this.id = id;
        this.programId = programId;
        this.orderableId = orderableId;
        this.dosesPerPatient = dosesPerPatient;
        this.active = active;
        this.fullSupply = fullSupply;
        this.dateUpdated = dateUpdated;
    }

    /**
     * Returns true if this association is for given Program.
     * @param program the {@link Program} to ask about
     * @return true if this association is for the given Program, false otherwise.
     */
    public boolean isForProgram(Program program) {
        return this.programId.equals(program);
    }

    /**
     * Equal if both represent association between same Program and Product.  e.g. Ibuprofen in the
     * Essential Meds Program is always the same association regardless of the other properties.
     * @param other the other ProgramOrderable
     * @return true if for same Program-Orderable association, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (Objects.isNull(other) || !(other instanceof ProgramOrderable)) {
            return false;
        }

        ProgramOrderable otherProgProduct = (ProgramOrderable) other;

        return Objects.equals(getProgramId(), otherProgProduct.getProgramId())
                && Objects.equals(getOrderableId(), otherProgProduct.orderableId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(programId, orderableId);
    }

    public boolean isActive() {
        return active;
    }

    public String getProgramId() {
        return programId;
    }

    public String getOrderableId() {
        return orderableId;
    }

    public Integer getDosesPerPatient() {
        return dosesPerPatient;
    }

    public boolean isFullSupply() {
        return fullSupply;
    }

    public Long getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Long dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public void setProgram(String programId) {
        this.programId = programId;
    }

    public void setOrderableId(String orderableId) {
        this.orderableId = orderableId;
    }

    public void setDosesPerPatient(Integer dosesPerPatient) {
        this.dosesPerPatient = dosesPerPatient;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setFullSupply(boolean fullSupply) {
        this.fullSupply = fullSupply;
    }
}
