package org.smartregister.stock.openlmis.domain;

import java.util.UUID;

public class TradeItemClassification extends BaseEntity {

    private TradeItem tradeItem;
    private String classificationSystem;
    private String classificationId;
    private Long dateUpdated;

    public TradeItemClassification(UUID id, TradeItem tradeItem, String classificationSystem, String classificationId, Long dateUpdated) {
        this.id = id;
        this.tradeItem = tradeItem;
        this.classificationSystem = classificationSystem;
        this.classificationId = classificationId;
        this.dateUpdated = dateUpdated;
    }

    /**
     * Constructs a classification for a trade item.
     * @param tradeItem the trade item this classification belongs to
     * @param classificationSystem the classification system
     * @param classificationId the id of the classification system
     */
    public TradeItemClassification(TradeItem tradeItem, String classificationSystem,
                            String classificationId) {
        this.tradeItem = tradeItem;
        this.classificationSystem = classificationSystem;
        this.classificationId = classificationId;
    }


    public TradeItem getTradeItem() {
        return tradeItem;
    }

    public void setTradeItem(TradeItem tradeItem) {
        this.tradeItem = tradeItem;
    }

    public String getClassificationSystem() {
        return classificationSystem;
    }

    public void setClassificationSystem(String classificationSystem) {
        this.classificationSystem = classificationSystem;
    }

    public String getClassificationId() {
        return classificationId;
    }

    public void setClassificationId(String classificationId) {
        this.classificationId = classificationId;
    }

    public Long getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Long dateUpdated) {
        this.dateUpdated = dateUpdated;
    }
}
