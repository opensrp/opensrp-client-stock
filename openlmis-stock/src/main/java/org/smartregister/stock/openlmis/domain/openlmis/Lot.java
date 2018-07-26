package org.smartregister.stock.openlmis.domain.openlmis;

import org.joda.time.LocalDate;

import java.util.UUID;

/**
 * Created by samuelgithengi on 25/7/18.
 */
public class Lot extends BaseEntity {

    public Lot(UUID id, String lotCode, LocalDate expirationDate, LocalDate manufactureDate, TradeItem tradeItem, boolean active) {
        this.id = id;
        this.lotCode = lotCode;
        this.expirationDate = expirationDate;
        this.manufactureDate = manufactureDate;
        this.tradeItem = tradeItem;
        this.active = active;
    }

    private String lotCode;

    private LocalDate expirationDate;

    private LocalDate manufactureDate;

    private TradeItem tradeItem;

    private boolean active;

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

    public TradeItem getTradeItem() {
        return tradeItem;
    }

    public void setTradeItem(TradeItem tradeItem) {
        this.tradeItem = tradeItem;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
