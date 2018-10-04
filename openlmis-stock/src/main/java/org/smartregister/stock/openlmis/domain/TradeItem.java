package org.smartregister.stock.openlmis.domain;

import org.smartregister.stock.openlmis.domain.openlmis.Dispensable;

/**
 * Created by samuelgithengi on 26/7/18.
 */
public class TradeItem {

    private String id;

    private String commodityTypeId;

    private String name;

    private Long dateUpdated;

    private Long netContent;

    private Dispensable dispensable;

    private boolean useVvm;

    private boolean hasLots;

    public String getId() {
        return id;

    }

    public void setId(String id) {
        this.id = id;
    }


    public String getCommodityTypeId() {
        return commodityTypeId;
    }

    public TradeItem(String id) {
        this.id = id;
    }


    public void setCommodityTypeId(String commodityTypeId) {
        this.commodityTypeId = commodityTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Long dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public Long getNetContent() {
        return netContent;
    }

    public void setNetContent(Long netContent) {
        this.netContent = netContent;
    }

    public Dispensable getDispensable() {
        return dispensable;
    }

    public void setDispensable(Dispensable dispensable) {
        this.dispensable = dispensable;
    }

    public boolean isUseVvm() {
        return useVvm;
    }

    public void setUseVvm(boolean useVvm) {
        this.useVvm = useVvm;
    }

    public boolean isHasLots() {
        return hasLots;
    }

    public void setHasLots(boolean hasLots) {
        this.hasLots = hasLots;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof TradeItem))
            return false;
        TradeItem other = (TradeItem) obj;
        return other.getId().equals(getId());
    }
}
