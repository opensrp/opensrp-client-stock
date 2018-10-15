package org.smartregister.stock.openlmis.repository;

import android.content.ContentValues;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.repository.BaseRepository;
import org.smartregister.repository.Repository;
import org.smartregister.stock.openlmis.domain.StockTake;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.smartregister.stock.openlmis.repository.StockRepository.LOT_ID;
import static org.smartregister.stock.openlmis.repository.StockRepository.PROGRAM_ID;
import static org.smartregister.stock.openlmis.repository.StockRepository.REASON;
import static org.smartregister.stock.openlmis.repository.TradeItemRepository.COMMODITY_TYPE_ID;
import static org.smartregister.stock.openlmis.widget.LotFactory.TRADE_ITEM_ID;
import static org.smartregister.stock.repository.StockRepository.VALUE;

/**
 * Created by samuelgithengi on 9/26/18.
 */
public class StockTakeRepository extends BaseRepository {

    private static final String TAG = "StockTakeRepository";

    private static final String STOCK_TAKE_TABLE = "stock_take";

    private static final String STATUS = "status";

    private static final String LAST_UPDATED = "last_updated";

    private static final String NO_CHANGE = "no_change";

    private static final String DISPLAY_STATUS = "display_status";

    private static final String CREATE_STOCK_TAKE_TABLE = "CREATE TABLE " + STOCK_TAKE_TABLE +
            " (" + PROGRAM_ID + " VARCHAR  NOT NULL, " +
            COMMODITY_TYPE_ID + " VARCHAR NOT NULL, " +
            TRADE_ITEM_ID + " VARCHAR NOT NULL, " +
            LOT_ID + " VARCHAR  NULL, " +
            REASON + " VARCHAR, " +
            STATUS + " VARCHAR, " +
            VALUE + " INTEGER NOT NULL, " +
            NO_CHANGE + " INTEGER NOT NULL, " +
            DISPLAY_STATUS + " INTEGER NOT NULL, " +
            LAST_UPDATED + " INTEGER NOT NULL)";

    private static final String CREATE_PROGRAM_TRADE_ITEM_INDEX = "CREATE INDEX "
            + STOCK_TAKE_TABLE + "_TM_INDEX ON "
            + STOCK_TAKE_TABLE + "(" + PROGRAM_ID + "," + TRADE_ITEM_ID + ")";

    private static final String CREATE_PROGRAM_COMMODITY_TYPE_INDEX = "CREATE INDEX "
            + STOCK_TAKE_TABLE + "_CT_INDEX ON "
            + STOCK_TAKE_TABLE + "(" + PROGRAM_ID + "," + COMMODITY_TYPE_ID + ")";

    public StockTakeRepository(Repository repository) {
        super(repository);
    }

    public static void createTable(SQLiteDatabase database) {
        database.execSQL(CREATE_STOCK_TAKE_TABLE);
        database.execSQL(CREATE_PROGRAM_TRADE_ITEM_INDEX);
        database.execSQL(CREATE_PROGRAM_COMMODITY_TYPE_INDEX);
    }

    public void addOrUpdate(StockTake stockTake) {
        stockTake.setLastUpdated(System.currentTimeMillis());
        ContentValues contentValues = new ContentValues();
        contentValues.put(PROGRAM_ID, stockTake.getProgramId());
        contentValues.put(COMMODITY_TYPE_ID, stockTake.getCommodityTypeId());
        contentValues.put(TRADE_ITEM_ID, stockTake.getTradeItemId());
        contentValues.put(LOT_ID, stockTake.getLotId());
        contentValues.put(REASON, stockTake.getReasonId());
        contentValues.put(STATUS, stockTake.getStatus());
        contentValues.put(VALUE, stockTake.getQuantity());
        contentValues.put(LAST_UPDATED, stockTake.getLastUpdated());
        contentValues.put(NO_CHANGE, stockTake.isNoChange());
        contentValues.put(DISPLAY_STATUS, stockTake.isDisplayStatus());
        if (exists(stockTake)) {
            if (StringUtils.isBlank(stockTake.getLotId()))
                getWritableDatabase().update(STOCK_TAKE_TABLE, contentValues, String.format("%s=? AND %s=?",
                        PROGRAM_ID, TRADE_ITEM_ID), new String[]{stockTake.getProgramId(), stockTake.getTradeItemId()});
            else
                getWritableDatabase().update(STOCK_TAKE_TABLE, contentValues, String.format("%s=? AND %s=? AND %s=?",
                        PROGRAM_ID, TRADE_ITEM_ID, LOT_ID),
                        new String[]{stockTake.getProgramId(), stockTake.getTradeItemId(), stockTake.getLotId()});

        } else {
            getWritableDatabase().insert(STOCK_TAKE_TABLE, null, contentValues);
        }
    }

    public Set<StockTake> getStockTakeList(String programId, String tradeItemId) {
        Set<StockTake> stockTakeList = new HashSet<>();
        if (tradeItemId == null) {
            return stockTakeList;
        }
        String query = String.format("SELECT * FROM %s WHERE %s=? AND %s=?", STOCK_TAKE_TABLE, PROGRAM_ID, TRADE_ITEM_ID);
        Cursor cursor = null;
        try {
            cursor = getReadableDatabase().rawQuery(query, new String[]{programId, tradeItemId});
            while (cursor.moveToNext()) {
                stockTakeList.add(createStockTake(cursor));
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return stockTakeList;
    }

    public Set<StockTake> getStockTakeListByTradeItemIds(String programId, Set<String> tradeItemIds) {
        Set<StockTake> stockTakeSet = new HashSet<>();
        if (tradeItemIds == null) {
            return stockTakeSet;
        }
        int len = tradeItemIds.size();
        String query = String.format("SELECT * FROM %s WHERE %s IN (%s) AND %s=?", STOCK_TAKE_TABLE,
                TRADE_ITEM_ID, TextUtils.join(",", Collections.nCopies(len, "?")), PROGRAM_ID);
        Cursor cursor = null;
        try {
            String[] params = tradeItemIds.toArray(new String[len + 1]);
            params[len] = programId;
            cursor = getReadableDatabase().rawQuery(query, params);
            while (cursor.moveToNext()) {
                stockTakeSet.add(createStockTake(cursor));
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return stockTakeSet;
    }

    private boolean exists(StockTake stockTake) {
        boolean hasLots = StringUtils.isNotBlank(stockTake.getLotId());
        String query;
        Cursor cursor = null;
        try {
            if (hasLots) {
                query = String.format("SELECT 1 FROM %s WHERE %s=? AND %s=? AND %s=?",
                        STOCK_TAKE_TABLE, PROGRAM_ID, TRADE_ITEM_ID, LOT_ID);
                cursor = getReadableDatabase().rawQuery(query, new String[]{stockTake.getProgramId(),
                        stockTake.getTradeItemId(), stockTake.getLotId()});
            } else {
                query = String.format("SELECT 1 FROM %s WHERE %s=? AND %s=?", STOCK_TAKE_TABLE,
                        PROGRAM_ID, TRADE_ITEM_ID);
                cursor = getReadableDatabase().rawQuery(query, new String[]{stockTake.getProgramId(),
                        stockTake.getTradeItemId()});
            }
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

    public Pair<Set<String>, Long> findTradeItemsIdsAdjusted(String programId, Set<String> commodityTypeIds) {
        Set<String> ids = new HashSet<>();
        if (commodityTypeIds == null || commodityTypeIds.isEmpty()) {
            return new Pair<>(ids, null);
        }
        int len = commodityTypeIds.size();
        String query = String.format("SELECT %s FROM %s " +
                        " WHERE %s  IN (%s) AND %s =? ",
                TRADE_ITEM_ID, STOCK_TAKE_TABLE, COMMODITY_TYPE_ID,
                TextUtils.join(",", Collections.nCopies(len, "?")), PROGRAM_ID);
        Cursor cursor = null;
        try {
            String[] params = commodityTypeIds.toArray(new String[len + 1]);
            params[len] = programId;
            cursor = getReadableDatabase().rawQuery(query, params);
            while (cursor.moveToNext()) {
                ids.add(cursor.getString(0));
            }
            return new Pair<>(ids, findLastUpdated(params));
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return new Pair<>(ids, null);
    }

    private Long findLastUpdated(String[] params) {
        String query = String.format("SELECT MAX(%s) FROM %s " +
                        " WHERE %s  IN (%s) AND %s =? ",
                LAST_UPDATED, STOCK_TAKE_TABLE, COMMODITY_TYPE_ID,
                TextUtils.join(",", Collections.nCopies(params.length - 1, "?")), PROGRAM_ID);
        Cursor cursor = null;
        try {
            cursor = getReadableDatabase().rawQuery(query, params);
            if (cursor.moveToFirst()) {
                return cursor.getLong(0);
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

    public int deleteStockTake(String programId, Set<String> adjustedTradeItems) {
        int len = adjustedTradeItems.size();
        String[] params = adjustedTradeItems.toArray(new String[len + 1]);
        params[len] = programId;
        String whereClause = String.format("%s IN (%s) AND %s =?", TRADE_ITEM_ID,
                TextUtils.join(",", Collections.nCopies(len, "?")), PROGRAM_ID);
        return getWritableDatabase().delete(STOCK_TAKE_TABLE, whereClause, params);
    }

    private StockTake createStockTake(Cursor cursor) {
        StockTake stockTake = new StockTake(
                cursor.getString(cursor.getColumnIndex(PROGRAM_ID)),
                cursor.getString(cursor.getColumnIndex(COMMODITY_TYPE_ID)),
                cursor.getString(cursor.getColumnIndex(TRADE_ITEM_ID)),
                cursor.getString(cursor.getColumnIndex(LOT_ID)));
        stockTake.setStatus(cursor.getString(cursor.getColumnIndex(STATUS)));
        stockTake.setReasonId(cursor.getString(cursor.getColumnIndex(REASON)));
        stockTake.setQuantity(cursor.getInt(cursor.getColumnIndex(VALUE)));
        stockTake.setLastUpdated(cursor.getLong(cursor.getColumnIndex(LAST_UPDATED)));
        stockTake.setNoChange(cursor.getInt(cursor.getColumnIndex(NO_CHANGE)) > 0);
        stockTake.setDisplayStatus(cursor.getInt(cursor.getColumnIndex(DISPLAY_STATUS)) > 0);
        return stockTake;
    }


}
