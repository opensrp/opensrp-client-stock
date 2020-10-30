package org.smartregister.stock.openlmis.repository;

import android.content.ContentValues;
import android.util.Log;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.smartregister.repository.BaseRepository;
import org.smartregister.repository.Repository;
import org.smartregister.stock.openlmis.domain.TradeItem;
import org.smartregister.stock.openlmis.domain.openlmis.CommodityType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by samuelgithengi on 9/12/18.
 */
public class SearchRepository extends BaseRepository {

    private static final String TAG = "CommodityTradeItemRepo";

    private static final String COMMODITY_TYPE_FTS_TABLE = "commodity_trade_item_search";

    private static final String COMMODITY_TYPE_ID = "commodity_type_id";
    private static final String PHRASE = "phrase";
    private static final String TRADE_ITEM_ID = "trade_item_id";
    private static final String CREATE_COMMODITY_TYPE_FTS_TABLE =
            "CREATE VIRTUAL TABLE " + COMMODITY_TYPE_FTS_TABLE
                    + " USING fts4(" + COMMODITY_TYPE_ID + "," + TRADE_ITEM_ID + "," + PHRASE + " TEXT)";


    public SearchRepository(Repository repository) {
        super();
    }

    public static void createTable(SQLiteDatabase database) {
        database.execSQL(CREATE_COMMODITY_TYPE_FTS_TABLE);
    }

    public void addOrUpdate(CommodityType commodityType, List<TradeItem> tradeItems) {
        if (commodityType == null)
            return;
        if (tradeItems == null || tradeItems.isEmpty()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(PHRASE, withSub(commodityType.getName()));
            if (exists(commodityType.getId().toString()))
                getWritableDatabase().update(COMMODITY_TYPE_FTS_TABLE, contentValues,
                        COMMODITY_TYPE_ID + "=? AND " + TRADE_ITEM_ID + " IS NULL",
                        new String[]{commodityType.getId().toString()});
            else {
                contentValues.put(COMMODITY_TYPE_ID, commodityType.getId().toString());
                getWritableDatabase().insert(COMMODITY_TYPE_FTS_TABLE, null, contentValues);
            }
        } else
            for (TradeItem tradeItem : tradeItems) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(PHRASE, withSub(commodityType.getName()).concat("|").concat(withSub(tradeItem.getName())));
                if (exists(commodityType.getId().toString(), tradeItem.getId()))
                    getWritableDatabase().update(COMMODITY_TYPE_FTS_TABLE, contentValues,
                            COMMODITY_TYPE_ID + "=? AND " + TRADE_ITEM_ID + "=?",
                            new String[]{commodityType.getId().toString(), tradeItem.getId()});
                else {
                    contentValues.put(COMMODITY_TYPE_ID, commodityType.getId().toString());
                    contentValues.put(TRADE_ITEM_ID, tradeItem.getId());
                    getWritableDatabase().insert(COMMODITY_TYPE_FTS_TABLE, null, contentValues);
                }
            }

    }

    private boolean exists(String commodityTypeId, String tradeItemId) {
        String query = String.format("SELECT 1 FROM %s WHERE %s=? AND %s=?",
                COMMODITY_TYPE_FTS_TABLE, COMMODITY_TYPE_ID, TRADE_ITEM_ID);
        Cursor cursor = null;
        try {
            cursor = getReadableDatabase().rawQuery(query, new String[]{commodityTypeId, tradeItemId});
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

    private boolean exists(String commodityTypeId) {
        String query = String.format("SELECT 1 FROM %s WHERE %s=?",
                COMMODITY_TYPE_FTS_TABLE, COMMODITY_TYPE_ID);
        Cursor cursor = null;
        try {
            cursor = getReadableDatabase().rawQuery(query, new String[]{commodityTypeId});
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


    private String withSub(String s) {
        String withSub = "";
        if (s == null || s.isEmpty()) {
            return withSub;
        }
        int length = s.length();

        for (int i = 0; i < length; i++) {
            withSub = withSub.concat(s.substring(i) + " ");
        }
        return withSub.trim();
    }

    public Map<String, Set<String>> searchIds(String phrase) {

        Cursor cursor = null;
        Map<String, Set<String>> ids = new HashMap<>();

        try {
            String query = String.format("SELECT * FROM %s WHERE %s MATCH ?", COMMODITY_TYPE_FTS_TABLE, PHRASE);
            cursor = getReadableDatabase().rawQuery(query, new String[]{phrase + "*"});
            while (cursor.moveToNext()) {
                String commodityType = cursor.getString(0);
                String tradeItem = cursor.getString(1);
                if (ids.containsKey(commodityType) && tradeItem != null) {
                    ids.get(commodityType).add(tradeItem);

                } else {
                    Set<String> tradeItems = new HashSet<>();
                    if (tradeItem != null)
                        tradeItems.add(tradeItem);
                    ids.put(commodityType, tradeItems);
                }
            }

        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return ids;
    }

}
