package org.smartregister.stock.openlmis.widget.helper;

/**
 * Created by samuelgithengi on 8/24/18.
 */
public class LotDto {

    private String lotId;

    private String lotStatus;

    private int quantity;

    public LotDto(String lotId) {
        this.lotId = lotId;
    }

    public String getLotId() {
        return lotId;
    }

    public void setLotId(String lotId) {
        this.lotId = lotId;
    }

    public String getLotStatus() {
        return lotStatus;
    }

    public void setLotStatus(String lotStatus) {
        this.lotStatus = lotStatus;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof LotDto))
            return false;
        LotDto other = (LotDto) obj;
        return lotId.equals(other.lotId);
    }
}
