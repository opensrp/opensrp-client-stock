package org.smartregister.stock.openlmis.wrapper;

import org.smartregister.stock.openlmis.domain.TradeItem;

/**
 * Created by samuelgithengi on 8/2/18.
 */
public class TradeItemWrapper {

    private TradeItem tradeItem;

    private int totalStock;

    private int numberOfLots;

    private boolean hasLotExpiring;

    public TradeItemWrapper(TradeItem tradeItem) {
        this.tradeItem = tradeItem;
    }

    public TradeItem getTradeItem() {
        return tradeItem;
    }

    public void setTradeItem(TradeItem tradeItem) {
        this.tradeItem = tradeItem;
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

    public boolean isHasLotExpiring() {
        return hasLotExpiring;
    }

    public void setHasLotExpiring(boolean hasLotExpiring) {
        this.hasLotExpiring = hasLotExpiring;
    }
}
