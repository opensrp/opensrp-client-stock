package org.smartregister.stock.domain;

import java.io.Serializable;
import java.util.List;

public class StockResponse implements Serializable {

    private List<Stock> stocks;

    public List<Stock> getStocks() {
        return stocks;
    }
}
