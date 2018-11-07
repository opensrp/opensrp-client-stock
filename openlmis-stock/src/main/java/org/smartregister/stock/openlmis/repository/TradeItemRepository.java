package org.smartregister.stock.openlmis.repository;

import android.content.ContentValues;
import android.text.TextUtils;
import android.util.Log;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.joda.time.LocalDate;
import org.smartregister.repository.BaseRepository;
import org.smartregister.repository.Repository;
import org.smartregister.stock.openlmis.domain.TradeItem;
import org.smartregister.stock.openlmis.domain.openlmis.Dispensable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.smartregister.stock.openlmis.repository.openlmis.LotRepository.EXPIRATION_DATE;
import static org.smartregister.stock.openlmis.repository.openlmis.LotRepository.LOT_TABLE;
import static org.smartregister.stock.openlmis.repository.openlmis.LotRepository.TRADE_ITEM_ID;

import static org.smartregister.stock.openlmis.util.Utils.convertIntToBoolean;

/**
 * Created by samuelgithengi on 26/7/18.
 */
public class TradeItemRepository extends BaseRepository {

    private static final String TAG = TradeItemRepository.class.getName();

    public static final String TRADE_ITEM_TABLE = "trade_item_register";

    public static final String ID = "_id";

    public static final String COMMODITY_TYPE_ID = "commodity_type_id";

    private static final String NAME = "name";

    private static final String DATE_UPDATED = "date_updated";

    private static final String NET_CONTENT = "net_content";

    private static final String DISPENSING_UNIT = "dispensing_unit";

    private static final String DISPENSING_SIZE = "dispensing_size";

    private static final String DISPENSING_ADMINISTRATION = "dispensing_administration";

    private static final String USE_VVM = "use_vvm";

    public static final String HAS_LOTS = "has_lots";

    private static final String CREATE_TRADE_ITEM_TABLE = "CREATE TABLE " + TRADE_ITEM_TABLE +
            "(" + ID + " VARCHAR NOT NULL PRIMARY KEY," +
            COMMODITY_TYPE_ID + " VARCHAR ," +
            NAME + " VARCHAR, " +
            DATE_UPDATED + " INTEGER, " +
            NET_CONTENT + " INTEGER, " +
            DISPENSING_UNIT + " VARCHAR, " +
            DISPENSING_SIZE + " VARCHAR, " +
            DISPENSING_ADMINISTRATION + " VARCHAR," +
            USE_VVM + " INTEGER," +
            HAS_LOTS + " INTEGER)";

    private static final String CREATE_TRADE_ITEM_INDEX = "CREATE INDEX "
            + TRADE_ITEM_TABLE + "_INDEX ON "
            + TRADE_ITEM_TABLE + "(" + COMMODITY_TYPE_ID + "," + ID + ")";

    public TradeItemRepository(Repository repository) {
        super(repository);
    }

    public static void createTable(SQLiteDatabase database) {
        database.execSQL(CREATE_TRADE_ITEM_TABLE);
        database.execSQL(CREATE_TRADE_ITEM_INDEX);
    }

    public void addOrUpdate(TradeItem tradeItem) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COMMODITY_TYPE_ID, tradeItem.getCommodityTypeId());
        contentValues.put(NAME, tradeItem.getName());
        contentValues.put(DATE_UPDATED, tradeItem.getDateUpdated());
        contentValues.put(NET_CONTENT, tradeItem.getNetContent());
        contentValues.put(HAS_LOTS, tradeItem.isHasLots());
        contentValues.put(USE_VVM, tradeItem.isUseVvm());
        if (tradeItem.getDispensable() != null) {
            contentValues.put(DISPENSING_UNIT, tradeItem.getDispensable().getKeyDispensingUnit());
            contentValues.put(DISPENSING_SIZE, tradeItem.getDispensable().getKeySizeCode());
            contentValues.put(DISPENSING_ADMINISTRATION, tradeItem.getDispensable().getKeyRouteOfAdministration());
        }
        if (tradeItemExists(tradeItem.getId())) {
            getWritableDatabase().update(TRADE_ITEM_TABLE, contentValues, ID + "=?", new String[]{tradeItem.getId()});
        } else {
            contentValues.put(ID, tradeItem.getId());
            getWritableDatabase().insert(TRADE_ITEM_TABLE, null, contentValues);
        }
    }

    public boolean tradeItemExists(String tradeItemId) {
        String query = String.format("SELECT 1 FROM %s WHERE %s=?", TRADE_ITEM_TABLE, ID);
        Cursor cursor = null;
        try {
            cursor = getReadableDatabase().rawQuery(query, new String[]{tradeItemId});
            return cursor.moveToFirst();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return false;
    }

    public List<TradeItem> getTradeItemByCommodityType(String commodityTypeId) {

        if (commodityTypeId == null) {
            return new ArrayList<>();
        }
        String query = String.format("SELECT * FROM %s WHERE %s=? AND %s IS NOT NULL", TRADE_ITEM_TABLE, COMMODITY_TYPE_ID, NAME);
        Cursor cursor = null;
        List<TradeItem> tradeItems = new ArrayList<>();
        try {
            cursor = getReadableDatabase().rawQuery(query, new String[]{commodityTypeId});
            while (cursor.moveToNext()) {
                tradeItems.add(createTradeItem(cursor));
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return tradeItems;
    }

    public TradeItem getTradeItemById(String tradeItemId) {

        if (tradeItemId == null) {
            return null;
        }
        String query = String.format("SELECT * FROM %s WHERE %s=?", TRADE_ITEM_TABLE, ID);
        Cursor cursor = null;
        try {
            cursor = getReadableDatabase().rawQuery(query, new String[]{tradeItemId});
            if (cursor.moveToFirst()) {
                return createTradeItem(cursor);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    public List<TradeItem> getTradeItemByIds(Set<String> tradeItemIds) {
        List<TradeItem> tradeItems = new ArrayList<>();
        if (tradeItemIds == null)
            return tradeItems;
        tradeItemIds.remove(null);
        if (tradeItemIds.isEmpty())
            return tradeItems;
        int len = tradeItemIds.size();
        String query = String.format("SELECT * FROM %s WHERE %s IS NOT NULL AND %s IN (%s)",
                TRADE_ITEM_TABLE, NAME, ID, TextUtils.join(",", Collections.nCopies(len, "?")));
        Cursor cursor = null;
        try {
            cursor = getReadableDatabase().rawQuery(query, tradeItemIds.toArray(new String[len]));
            while (cursor.moveToNext()) {
                tradeItems.add(createTradeItem(cursor));
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return tradeItems;
    }

    public List<TradeItem> findTradeItemsWithActiveLotsByCommodityType(String commodityTypeId) {
        if (commodityTypeId == null) {
            return new ArrayList<>();
        }
        String query = String.format("SELECT DISTINCT t.* FROM %s t JOIN %s l on t.%s=l.%s" +
                        " WHERE t.%s = ? AND l.%s >=? AND %s=1",
                TRADE_ITEM_TABLE, LOT_TABLE, ID, TRADE_ITEM_ID, COMMODITY_TYPE_ID, EXPIRATION_DATE, HAS_LOTS);
        Cursor cursor = null;
        List<TradeItem> tradeItems = new ArrayList<>();
        try {
            cursor = getReadableDatabase().rawQuery(query, new String[]{commodityTypeId
                    , String.valueOf(new LocalDate().toDate().getTime())});
            while (cursor.moveToNext()) {
                tradeItems.add(createTradeItem(cursor));
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return tradeItems;
    }

    public List<TradeItem> findActiveTradeItemsWithoutLotsByCommodityType(String commodityTypeId) {
        if (commodityTypeId == null) {
            return new ArrayList<>();
        }
        String query = String.format("SELECT DISTINCT t.* FROM %s t " +
                        " WHERE t.%s = ? AND t.%s =0 ",
                TRADE_ITEM_TABLE, COMMODITY_TYPE_ID, HAS_LOTS);
        Cursor cursor = null;
        List<TradeItem> tradeItems = new ArrayList<>();
        try {
            cursor = getReadableDatabase().rawQuery(query, new String[]{commodityTypeId});
            while (cursor.moveToNext()) {
                tradeItems.add(createTradeItem(cursor));
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return tradeItems;
    }

    public List<TradeItem> findTradeItemsWithActiveLotsByTradeItemIds(Set<String> tradeItemIds) {
        List<TradeItem> tradeItems = new ArrayList<>();
        if (tradeItemIds == null || tradeItemIds.isEmpty()) {
            return tradeItems;
        }
        int len = tradeItemIds.size();
        String query = String.format("SELECT DISTINCT t.* FROM %s t JOIN %s l on t.%s=l.%s" +
                        " WHERE t.%s IN (%s) AND l.%s >=?",
                TRADE_ITEM_TABLE, LOT_TABLE, ID, TRADE_ITEM_ID, ID,
                TextUtils.join(",", Collections.nCopies(len, "?")), EXPIRATION_DATE);
        Cursor cursor = null;
        try {
            String[] params = tradeItemIds.toArray(new String[len + 1]);
            params[len] = String.valueOf(new LocalDate().toDate().getTime());
            cursor = getReadableDatabase().rawQuery(query, params);
            while (cursor.moveToNext()) {
                tradeItems.add(createTradeItem(cursor));
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return tradeItems;
    }

    public int findNumberOfTradeItems(Set<String> commodityTypeIds) {
        if (commodityTypeIds == null || commodityTypeIds.isEmpty()) {
            return 0;
        }
        int len = commodityTypeIds.size();
        String query = String.format("SELECT COUNT(DISTINCT t.%s) FROM %s t LEFT JOIN %s l on t.%s=l.%s" +
                        " WHERE t.%s  IN (%s) AND ( t.%s = 0 OR l.%s >=? )",
                ID, TRADE_ITEM_TABLE, LOT_TABLE, ID, TRADE_ITEM_ID, COMMODITY_TYPE_ID,
                TextUtils.join(",", Collections.nCopies(len, "?")), HAS_LOTS, EXPIRATION_DATE);
        Cursor cursor = null;
        try {
            String[] params = commodityTypeIds.toArray(new String[len + 1]);
            params[len] = String.valueOf(new LocalDate().toDate().getTime());
            cursor = getReadableDatabase().rawQuery(query, params);
            if (cursor.moveToFirst()) {
                return cursor.getInt(0);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return 0;
    }

    private TradeItem createTradeItem(Cursor cursor) {
        TradeItem tradeItem = new TradeItem(cursor.getString(cursor.getColumnIndex(ID)));
        tradeItem.setCommodityTypeId(cursor.getString(cursor.getColumnIndex(COMMODITY_TYPE_ID)));
        tradeItem.setName(cursor.getString(cursor.getColumnIndex(NAME)));
        tradeItem.setDateUpdated(cursor.getLong(cursor.getColumnIndex(DATE_UPDATED)));
        tradeItem.setNetContent(cursor.getLong(cursor.getColumnIndex(NET_CONTENT)));
        tradeItem.setDispensable(new Dispensable(null,
                cursor.getString(cursor.getColumnIndex(DISPENSING_UNIT)),
                cursor.getString(cursor.getColumnIndex(DISPENSING_SIZE)),
                cursor.getString(cursor.getColumnIndex(DISPENSING_ADMINISTRATION))));
        tradeItem.setHasLots(convertIntToBoolean(cursor.getInt(cursor.getColumnIndex(HAS_LOTS))));
        tradeItem.setUseVvm(convertIntToBoolean(cursor.getInt(cursor.getColumnIndex(USE_VVM))));
        return tradeItem;
    }

}
