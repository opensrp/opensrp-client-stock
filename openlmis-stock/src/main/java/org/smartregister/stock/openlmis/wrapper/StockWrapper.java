package org.smartregister.stock.openlmis.wrapper;

import org.smartregister.stock.openlmis.domain.Stock;

/**
 * Created by samuelgithengi on 8/2/18.
 */
public class StockWrapper {

    private Stock stock;

    private String lotCode;

    private int stockBalance;

    public StockWrapper(Stock stock, String lotCode, int stockBalance) {
        this.stock = stock;
        this.lotCode = lotCode;
        this.stockBalance = stockBalance;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public String getLotCode() {
        return lotCode;
    }

    public void setLotCode(String lotCode) {
        this.lotCode = lotCode;
    }

    public int getStockBalance() {
        return stockBalance;
    }

    public void setStockBalance(int stockBalance) {
        this.stockBalance = stockBalance;
    }
}
