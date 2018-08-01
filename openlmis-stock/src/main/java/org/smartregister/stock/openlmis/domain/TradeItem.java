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

    private int totalStock;

    private int numberOfLots;

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

    public int getTotalStock() {
        return totalStock;
    }

    public void setTotalStock(int totalStock) {
        this.totalStock = totalStock;
    }

    public int getNumberOfLots() {
        return numberOfLots;
    }

    public void setNumberOfLots(int numberOfLots) {
        this.numberOfLots = numberOfLots;
    }
}
