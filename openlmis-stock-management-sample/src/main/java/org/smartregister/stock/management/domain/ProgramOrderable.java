package org.smartregister.stock.management.domain;

import java.util.Objects;

public class ProgramOrderable extends BaseEntity {

    private Program program;
    private Orderable product;
    private Integer dosesPerPatient;
    private boolean active;
    private boolean fullSupply;

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
                && Objects.equals(product, otherProgProduct.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(program, product);
    }

    public boolean isActive() {
        return active;
    }
}
