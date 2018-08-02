package org.smartregister.stock.openlmis.domain;

/**
 * Created by samuelgithengi on 26/7/18.
 */
public class Stock extends org.smartregister.stock.domain.Stock {

    private String lotId;

    private String lotCode;

    public Stock(Long id, String transactionType, String providerid, int value, Long dateCreated,
                 String toFrom, String syncStatus, Long dateUpdated, String tradeItemId) {
        super(id, transactionType, providerid, value, dateCreated, toFrom, syncStatus, dateUpdated,
                tradeItemId);
    }

    public String getLotId() {
        return lotId;
    }

    public void setLotId(String lotId) {
        this.lotId = lotId;
    }

    public String getLotCode() {
        return lotCode;
    }

    public void setLotCode(String lotCode) {
        this.lotCode = lotCode;
    }
}