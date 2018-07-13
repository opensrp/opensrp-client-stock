package org.smartregister.stock.openlmis.domain;

import android.util.Log;

import java.util.List;
import java.util.UUID;

public class CommodityType extends BaseEntity {

    public static final String TAG = CommodityType.class.getName();
    private String name;
    private CommodityType parent;
    private String classificationSystem;
    private String classificationId;
    private Long dateUpdated;
    private String parentId;
    private List<CommodityType> children;

    public CommodityType(UUID id, String name, String parentId, String classificationSystem, String classificationId, Long dateUpdated) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.classificationSystem = classificationSystem;
        this.classificationId = classificationId;
        this.dateUpdated = dateUpdated;
    }

    public CommodityType(UUID id, String name, CommodityType parent, String classificationSystem, String classificationId, Long dateUpdated) {
        this.id = id;
        this.name = name;
        this.parent = parent;
        this.classificationSystem = classificationSystem;
        this.classificationId = classificationId;
        this.dateUpdated = dateUpdated;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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
                throw new Exception("ParentIsDescendant");
            }
            child.validateIsNotDescendant(commodityType);
        }
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public List<CommodityType> getChildren() {
        return children;
    }

    public void setChildren(List<CommodityType> children) {
        this.children = children;
    }
}
