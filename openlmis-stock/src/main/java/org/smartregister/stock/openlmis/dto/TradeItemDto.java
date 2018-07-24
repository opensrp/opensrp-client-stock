package org.smartregister.stock.openlmis.dto;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.Date;

public class TradeItemDto implements Parcelable {

    private String name;

    private String dispensable;

    private Long lastUpdated;

    public TradeItemDto(@NonNull String name, @NonNull String dispensable, @NonNull Long lastUpdated) {
        this.name = name;
        this.dispensable = dispensable;
        this.lastUpdated = lastUpdated;
    }

    public TradeItemDto(Parcel in) {
        String[] data = new String[3];

        in.readStringArray(data);
        name = data[0];
        dispensable = data[1];
        lastUpdated = Long.valueOf(data[2]);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{name,
                dispensable, lastUpdated.toString()});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public TradeItemDto createFromParcel(Parcel in) {
            return new TradeItemDto(in);
        }

        public TradeItemDto[] newArray(int size) {
            return new TradeItemDto[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getDispensable() {
        return dispensable;
    }

    public Long getLastUpdated() {
        return lastUpdated;
    }
}
