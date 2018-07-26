package org.smartregister.stock.openlmis.domain;

import org.smartregister.stock.openlmis.domain.openlmis.Dispensable;

/**
 * Created by samuelgithengi on 26/7/18.
 */
public class TradeItem {

    private String id;

    private String commodityTypeId;

    private String name;

    private long dateUpdated;

    private long netContent;

    private Dispensable dispensable;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCommodityTypeId() {
        return commodityTypeId;
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

    public long getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(long dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public long getNetContent() {
        return netContent;
    }

    public void setNetContent(long netContent) {
        this.netContent = netContent;
    }

    public Dispensable getDispensable() {
        return dispensable;
    }

    public void setDispensable(Dispensable dispensable) {
        this.dispensable = dispensable;
    }
}
