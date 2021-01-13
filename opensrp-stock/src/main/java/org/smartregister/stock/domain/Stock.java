package org.smartregister.stock.domain;

import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.smartregister.stock.util.GsonUtil;
import org.smartregister.stock.util.StockUtils;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by samuelgithengi on 2/6/18.
 */
public class Stock extends org.smartregister.domain.Stock implements Serializable {
    private String stockTypeId;
    private String syncStatus;
    private String childLocationId;
    private String team;
    private String teamId;
    private String stockId;

    public static final String issued = "issued";
    public static final String received = "received";
    public static final String loss_adjustment = "loss_adjustment";
    public static final String stock_take = "stock_take";

    public Stock(String id, String transactionType, String providerid, int value, Long dateCreated, String toFrom, String syncStatus, Long dateUpdated, String stockTypeId) {
        setId(id);
        setTransactionType(transactionType);
        setProviderid(providerid);
        setValue(value);
        setDateCreated(dateCreated);
        setToFrom(toFrom);
        this.syncStatus = syncStatus;
        setDateUpdated(dateUpdated);
        this.stockTypeId = stockTypeId;
    }

    public String getStockTypeId() {
        return stockTypeId;
    }

    public void setStockTypeId(String stockTypeId) {
        this.stockTypeId = stockTypeId;
    }

    public String getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(String sync_status) {
        this.syncStatus = sync_status;
    }

    public String getChildLocationId() {
        return childLocationId;
    }

    public void setChildLocationId(String childLocationId) {
        this.childLocationId = childLocationId;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public Long getUpdatedAt() {
        return getDateUpdated();
    }

    public static String getIssued() {
        return issued;
    }

    public static String getReceived() {
        return received;
    }

    public static String getLossAdjusment() {
        return loss_adjustment;
    }

    public void setDeliveryDate(String deliveryDate) {
        DateTime dateTime = StockUtils.getDateTimeFromString(deliveryDate);
        if (dateTime != null) {
            setDeliveryDate(dateTime.toDate());
        }
    }

    public void setAccountabilityEndDate(String accountabilityEndDate) {
        DateTime dateTime = StockUtils.getDateTimeFromString(accountabilityEndDate);
        if (dateTime != null) {
            setAccountabilityEndDate(dateTime.toDate());
        }
    }

    public void setDateCreated(Long dateCreated) {
        DateTime dateTime = StockUtils.getDateTimeFromString(String.valueOf(dateCreated));
        if (dateTime != null) {
            setDateCreated(dateTime);
        }
    }

    public void setCustomProperties(String customProperties) {
        if (StringUtils.isNotBlank(customProperties)) {
            Map<String, String> propertiesMap = GsonUtil.getGsonWithTimeTypeConverter().fromJson(customProperties, new TypeToken<Map<String, String>>() {
            }.getType());
            setCustomProperties(propertiesMap);
        }
    }

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    @Override
    public String getId() {
        String id = super.getId();
        if ("-1".equals(id)) {
            return null;
        } else {
            return id;
        }
    }

    @Override
    public void setId(String s) {
        if (s == null) {
            super.setId("-1");
        } else {
            super.setId(s);
        }
    }
}
