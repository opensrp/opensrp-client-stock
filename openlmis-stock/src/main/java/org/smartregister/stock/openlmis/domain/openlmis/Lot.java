package org.smartregister.stock.openlmis.domain.openlmis;

import org.joda.time.LocalDate;

import java.util.UUID;

/**
 * Created by samuelgithengi on 25/7/18.
 */
public class Lot extends BaseEntity {

    private String lotCode;

    private LocalDate expirationDate;

    private LocalDate manufactureDate;

    private TradeItem tradeItemId;

    private boolean active;

    private Long serverVersion;

    private Long dateDeleted;

    public Lot(UUID id, String lotCode, LocalDate expirationDate, LocalDate manufactureDate, TradeItem tradeItemId, boolean active) {
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

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public LocalDate getManufactureDate() {
        return manufactureDate;
    }

    public void setManufactureDate(LocalDate manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    public TradeItem getTradeItemId() {
        return tradeItemId;
    }

    public void setTradeItemId(TradeItem tradeItemId) {
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
