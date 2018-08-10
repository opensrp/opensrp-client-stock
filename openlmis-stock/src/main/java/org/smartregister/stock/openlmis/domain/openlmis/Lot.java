package org.smartregister.stock.openlmis.domain.openlmis;

import org.joda.time.LocalDate;

/**
 * Created by samuelgithengi on 25/7/18.
 */
public class Lot extends BaseEntity {

    private String lotCode;

    private Long expirationDate;

    private Long manufactureDate;

    private String tradeItemId;

    private boolean active;

    private Long serverVersion;

    private Long dateDeleted;

    public Lot(String id, String lotCode, Long expirationDate, Long manufactureDate, String tradeItemId, boolean active) {
        this.id = id;
        this.lotCode = lotCode;
        this.expirationDate = expirationDate;
        this.manufactureDate = manufactureDate;
        this.tradeItemId = tradeItemId;
        this.active = active;
    }

    public String getLotCode() {
        return lotCode;
    }

    public void setLotCode(String lotCode) {
        this.lotCode = lotCode;
    }

    public Long getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Long expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Long getManufactureDate() {
        return manufactureDate;
    }

    public void setManufactureDate(Long manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    public String getTradeItemId() {
        return tradeItemId;
    }

    public void setTradeItemId(String tradeItemId) {
        this.tradeItemId = tradeItemId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Long getServerVersion() {
        return serverVersion;
    }

    public void setServerVersion(Long serverVersion) {
        this.serverVersion = serverVersion;
    }

    public Long getDateDeleted() {
        return dateDeleted;
    }

    public void setDateDeleted(Long dateDeleted) {
        this.dateDeleted = dateDeleted;
    }
}
