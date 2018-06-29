package org.smartregister.stock.management.domain;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Orderable extends BaseEntity {

    public static final String TRADE_ITEM = "tradeItem";
    public static final String COMMODITY_TYPE = "commodityType";

    private Code productCode;
    private String fullProductCode;
    private long netContent;
    private long packRoundingThreshold;
    private boolean roundToZero;
    private List<ProgramOrderable> programOrderables;
    private Map<String, String> identifiers;
    private Map<String, String> extraData;
    private Dispensable dispensable;

    /**
     * Get the association to a {@link Program}.
     * @param program the Program this product is (maybe) in.
     * @return the association to the given {@link Program}, or null if this product is not in the
     *        given program or is marked inactive.
     */
    public ProgramOrderable getProgramOrderable(Program program) {
        for (ProgramOrderable programOrderable : programOrderables) {
            if (programOrderable.isForProgram(program) && programOrderable.isActive()) {
                return programOrderable;
            }
        }

        return null;
    }

    /**
     * Returns the number of packs to order. For this Orderable given a desired number of
     * dispensing units, will return the number of packs that should be ordered.
     * @param dispensingUnits # of dispensing units we'd like to order for
     * @return the number of packs that should be ordered.
     */
    public long packsToOrder(long dispensingUnits) {
        if (dispensingUnits <= 0 || netContent == 0) {
            return 0;
        }

        long packsToOrder = dispensingUnits / netContent;
        long remainderQuantity = dispensingUnits % netContent;

        if (remainderQuantity > 0 && remainderQuantity > packRoundingThreshold) {
            packsToOrder += 1;
        }

        if (packsToOrder == 0 && !roundToZero) {
            packsToOrder = 1;
        }

        return packsToOrder;
    }

    public String getTradeItemIdentifier() {
        return identifiers.get(TRADE_ITEM);
    }

    public String getCommodityTypeIdentifier() {
        return identifiers.get(COMMODITY_TYPE);
    }

    public boolean hasDispensable(Dispensable dispensable) {
        return this.dispensable.equals(dispensable);
    }

    /**
     * Determines equality based on product codes.
     * @param object another Orderable, ideally.
     * @return true if the two are semantically equal.  False otherwise.
     */
    @Override
    public final boolean equals(Object object) {
        return null != object
                && object instanceof Orderable
                && Objects.equals(productCode, ((Orderable) object).productCode);
    }

    @Override
    public final int hashCode() {
        return Objects.hashCode(productCode);
    }
}
