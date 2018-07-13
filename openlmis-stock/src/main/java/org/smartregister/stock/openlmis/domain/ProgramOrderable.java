package org.smartregister.stock.openlmis.domain;

import java.util.Objects;
import java.util.UUID;

public class ProgramOrderable extends BaseEntity {

    private Program program;
    private Orderable orderable;
    private Integer dosesPerPatient;
    private boolean active;
    private boolean fullSupply;
    private Long dateUpdated;

    public ProgramOrderable(UUID id, Program program, Orderable orderable, Integer dosesPerPatient, boolean active, boolean fullSupply, long dateUpdated) {
        this.id = id;
        this.program = program;
        this.orderable = orderable;
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
        return this.program.equals(program);
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

        return Objects.equals(program, otherProgProduct.program)
                && Objects.equals(orderable, otherProgProduct.orderable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(program, orderable);
    }

    public boolean isActive() {
        return active;
    }

    public Program getProgram() {
        return program;
    }

    public Orderable getOrderable() {
        return orderable;
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

    public void setProgram(Program program) {
        this.program = program;
    }

    public void setOrderable(Orderable orderable) {
        this.orderable = orderable;
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
