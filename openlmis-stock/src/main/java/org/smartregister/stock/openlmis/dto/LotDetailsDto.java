package org.smartregister.stock.openlmis.dto;

/**
 * Created by samuelgithengi on 9/7/18.
 */
public class LotDetailsDto {

    private String lotId;

    private long minimumExpiryDate;

    private int totalStock;


    public LotDetailsDto(String lotId, long minimumExpiryDate, int totalStock) {
        this.lotId = lotId;
        this.minimumExpiryDate = minimumExpiryDate;
        this.totalStock = totalStock;
    }

    public String getLotId() {
        return lotId;
    }

    public void setLotId(String lotId) {
        this.lotId = lotId;
    }

    public long getMinimumExpiryDate() {
        return minimumExpiryDate;
    }

    public void setMinimumExpiryDate(long minimumExpiryDate) {
        this.minimumExpiryDate = minimumExpiryDate;
    }

    public int getTotalStock() {
        return totalStock;
    }

    public void setTotalStock(int totalStock) {
        this.totalStock = totalStock;
    }
}
