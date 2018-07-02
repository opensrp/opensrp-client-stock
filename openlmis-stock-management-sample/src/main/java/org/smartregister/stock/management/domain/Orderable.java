package org.smartregister.stock.management.domain;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Orderable extends BaseEntity {

    public static final String TRADE_ITEM = "tradeItem";
    public static final String COMMODITY_TYPE = "commodityType";

    private Code productCode;
    private String fullProductCode;
    private long netContent;
    private long packRoundingThreshold;
    private boolean roundToZero;
    private Dispensable dispensable;
    private Long dateUpdated;
    private List<ProgramOrderable> programOrderables;
    private Map<String, String> identifiers;
    private Map<String, String> extraData;

    public Orderable(UUID id) {
        this.id = id;
    }

    public Orderable(UUID id, Code productCode, String fullProductCode, long netContent, long packRoundingThreshold, boolean roundToZero, String tradeItemId, String commodityTypeId) {
        this.id = id;
        this.productCode = productCode;
        this.fullProductCode = fullProductCode;
        this.netContent = netContent;
        this.packRoundingThreshold = packRoundingThreshold;
        this.roundToZero = roundToZero;
        identifiers.put(TRADE_ITEM, tradeItemId);
        identifiers.put(COMMODITY_TYPE, commodityTypeId);
    }

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

    public Long getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Long dateUpdated) {
        this.dateUpdated = dateUpdated;
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

    public Code getProductCode() {
        return productCode;
    }

    public void setProductCode(Code productCode) {
        this.productCode = productCode;
    }

    public String getFullProductCode() {
        return fullProductCode;
    }

    public void setFullProductCode(String fullProductCode) {
        this.fullProductCode = fullProductCode;
    }

    public long getNetContent() {
        return netContent;
    }

    public void setNetContent(long netContent) {
        this.netContent = netContent;
    }

    public long getPackRoundingThreshold() {
        return packRoundingThreshold;
    }

    public void setPackRoundingThreshold(long packRoundingThreshold) {
        this.packRoundingThreshold = packRoundingThreshold;
    }

    public boolean isRoundToZero() {
        return roundToZero;
    }

    public void setRoundToZero(boolean roundToZero) {
        this.roundToZero = roundToZero;
    }

    public Dispensable getDispensable() {
        return dispensable;
    }

    public void setDispensable(Dispensable dispensable) {
        this.dispensable = dispensable;
    }

    public List<ProgramOrderable> getProgramOrderables() {
        return programOrderables;
    }

    public void setProgramOrderables(List<ProgramOrderable> programOrderables) {
        this.programOrderables = programOrderables;
    }

    public Map<String, String> getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(Map<String, String> identifiers) {
        this.identifiers = identifiers;
    }

    public Map<String, String> getExtraData() {
        return extraData;
    }

    public void setExtraData(Map<String, String> extraData) {
        this.extraData = extraData;
    }
}
