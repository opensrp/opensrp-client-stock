package org.smartregister.stock.openlmis.dto;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class TradeItemDto implements Parcelable {

    private String id;

    private String name;

    private String dispensable;

    private Long lastUpdated;

    private Integer numberOfLots;

    private String dispensingUnit;

    private Integer totalStock;

    public TradeItemDto(@NonNull String id, @NonNull String name, @NonNull String dispensable,
                        @NonNull Long lastUpdated, Integer numberOfLots, @NonNull String dispensingUnit, @NonNull Integer totalStock) {
        this.id = id;
        this.name = name;
        this.dispensable = dispensable;
        this.lastUpdated = lastUpdated;
        this.numberOfLots = numberOfLots == null ? 0 : numberOfLots;
        this.dispensingUnit = dispensingUnit;
        this.totalStock = totalStock;
    }

    public TradeItemDto(Parcel in) {
        String[] data = new String[7];

        in.readStringArray(data);
        id = data[0];
        name = data[1];
        dispensable = data[2];
        lastUpdated = Long.valueOf(data[3]);
        numberOfLots = Integer.valueOf(data[4]);
        dispensingUnit = data[5];
        totalStock = Integer.valueOf(data[6]);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{id, name,
                dispensable, lastUpdated.toString(), numberOfLots.toString(), dispensingUnit, totalStock.toString()});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public TradeItemDto createFromParcel(Parcel in) {
            return new TradeItemDto(in);
        }

        public TradeItemDto[] newArray(int size) {
            return new TradeItemDto[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDispensable() {
        return dispensable;
    }

    public Long getLastUpdated() {
        return lastUpdated;
    }

    public Integer getNumberOfLots() {
        return numberOfLots;
    }

    public String getDispensingUnit() {
        return dispensingUnit;
    }

    public Integer getTotalStock() {
        return totalStock;
    }
}
