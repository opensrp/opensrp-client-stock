package org.smartregister.stock.openlmis.domain.openlmis;

import java.util.HashMap;
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
    private String commodityTypeId;
    private String tradeItemId;
    private String dispensableId;
    private String fullProductName;
    private Map<String, String> extraData;
    private Long dateUpdated;

    public Orderable(String id) {
        this.id = id;
        this.extraData = new HashMap<>();
    }

    public Orderable(String id, String fullProductCode, String fullProductName, long netContent, long packRoundingThreshold,
                     boolean roundToZero, String dispensableId, String tradeItemId, String commodityTypeId) {

        this.id = id;
        this.fullProductCode = fullProductCode;
        this.fullProductName = fullProductName;
        this.netContent = netContent;
        this.packRoundingThreshold = packRoundingThreshold;
        this.roundToZero = roundToZero;
        this.dispensableId = dispensableId;
        this.tradeItemId = tradeItemId;
        this.commodityTypeId = commodityTypeId;
        this.extraData = new HashMap<>();
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

    public boolean hasDispensable(Dispensable dispensable) {
        return this.dispensableId.equals(dispensable);
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

    public String getDispensableId() {
        return dispensableId;
    }

    public String getCommodityTypeId() {
        return commodityTypeId;
    }

    public void setCommodityTypeId(String commodityTypeId) {
        this.commodityTypeId = commodityTypeId;
    }

    public String getTradeItemId() {
        return tradeItemId;
    }

    public void setTradeItemId(String tradeItemId) {
        this.tradeItemId = tradeItemId;
    }

    public void setDispensableId(String dispensableId) {
        this.dispensableId = dispensableId;
    }

    public String getFullProductName() {
        return fullProductName;
    }

    public void setFullProductName(String fullProductName) {
        this.fullProductName = fullProductName;
    }

    public Map<String, String> getExtraData() {
        return extraData;
    }

    public void setExtraData(Map<String, String> extraData) {
        this.extraData = extraData;
    }
}
