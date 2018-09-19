package org.smartregister.stock.openlmis.domain.openlmis;

import android.util.Log;

import java.util.List;

public class CommodityType extends BaseEntity {

    public static final String TAG = CommodityType.class.getName();
    private String name;
    private CommodityType parent;
    private String classificationSystem;
    private String classificationId;
    private Long dateUpdated;
    private List<CommodityType> children;
    private List<TradeItem> tradeItems;

    public CommodityType(String id, String name, CommodityType parent, String classificationSystem, String classificationId, Long dateUpdated) {
        this.id = id;
        this.name = name;
        this.parent = parent;
        this.classificationSystem = classificationSystem;
        this.classificationId = classificationId;
        this.dateUpdated = dateUpdated;
    }

    public CommodityType(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CommodityType getParent() {
        return parent;
    }

    public void setParent(CommodityType parent) {
        this.parent = parent;
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

    /**
     * Validates and assigns a parent to this commodity type.
     * No cycles in the hierarchy are allowed.
     *
     * @param parent the parent to assign
     */
    public void assignParent(CommodityType parent) {
        try {
            validateIsNotDescendant(parent);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
        this.parent = parent;
        parent.children.add(this);
    }

    private void validateIsNotDescendant(CommodityType commodityType) throws Exception {
        for (CommodityType child : children) {
            if (child.equals(commodityType)) {
                throw new IllegalArgumentException("ParentIsDescendant");
            }
            child.validateIsNotDescendant(commodityType);
        }
    }

    public List<CommodityType> getChildren() {
        return children;
    }

    public void setChildren(List<CommodityType> children) {
        this.children = children;
    }

    public List<TradeItem> getTradeItems() {
        return tradeItems;
    }

    public void setTradeItems(List<TradeItem> tradeItems) {
        this.tradeItems = tradeItems;
    }
}
