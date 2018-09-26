package org.smartregister.stock.openlmis.widget.helper;

/**
 * Created by samuelgithengi on 8/24/18.
 */
public class LotDto {

    private String lotId;

    private String lotStatus;

    private int quantity;

    private String lotCodeAndExpiry;

    private String reason;

    private boolean valid;

    public LotDto(String lotId) {
        this.lotId = lotId;
    }

    public LotDto(String lotId, int quantity, String lotStatus, String lotCodeAndExpiry) {
        this.lotId = lotId;
        this.quantity = quantity;
        this.lotStatus = lotStatus;
        this.lotCodeAndExpiry = lotCodeAndExpiry;
    }

    public LotDto() {
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

    public String getLotCodeAndExpiry() {
        return lotCodeAndExpiry;
    }

    public void setLotCodeAndExpiry(String lotCodeAndExpiry) {
        this.lotCodeAndExpiry = lotCodeAndExpiry;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof LotDto))
            return false;
        LotDto other = (LotDto) obj;
        return lotId.equals(other.lotId);
    }
}
