package org.smartregister.stock.openlmis.repository;

import android.content.ContentValues;
import android.text.TextUtils;
import android.util.Log;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.smartregister.repository.BaseRepository;
import org.smartregister.repository.Repository;
import org.smartregister.stock.openlmis.domain.TradeItem;
import org.smartregister.stock.openlmis.domain.openlmis.Dispensable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

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

    private static final String CREATE_TRADE_ITEM_TABLE = "CREATE TABLE " + TRADE_ITEM_TABLE +
            "(" + ID + " VARCHAR NOT NULL PRIMARY KEY," +
            COMMODITY_TYPE_ID + " VARCHAR ," +
            NAME + " VARCHAR, " +
            DATE_UPDATED + " INTEGER, " +
            NET_CONTENT + " INTEGER, " +
            DISPENSING_UNIT + " VARCHAR, " +
            DISPENSING_SIZE + " VARCHAR, " +
            DISPENSING_ADMINISTRATION + " VARCHAR)";

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
        tradeItemIds.remove(null);
        if (tradeItemIds.isEmpty())
            return tradeItems;
        int len = tradeItemIds.size();
        String query = String.format("SELECT * FROM %s WHERE %s IN (%s)", TRADE_ITEM_TABLE, ID,
                TextUtils.join(",", Collections.nCopies(len, "?")));
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
        return tradeItem;
    }

}
