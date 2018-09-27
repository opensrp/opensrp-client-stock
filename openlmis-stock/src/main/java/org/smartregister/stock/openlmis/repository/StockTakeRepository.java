package org.smartregister.stock.openlmis.repository;

import android.content.ContentValues;
import android.util.Log;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.repository.BaseRepository;
import org.smartregister.repository.Repository;
import org.smartregister.stock.openlmis.domain.StockTake;

import java.util.HashSet;
import java.util.Set;

import static org.smartregister.stock.openlmis.repository.StockRepository.LOT_ID;
import static org.smartregister.stock.openlmis.repository.StockRepository.PROGRAM_ID;
import static org.smartregister.stock.openlmis.repository.StockRepository.REASON;
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

    private static final String CREATE_STOCK_TAKE_TABLE = "CREATE TABLE " + STOCK_TAKE_TABLE +
            " (" + PROGRAM_ID + " VARCHAR  NOT NULL, " +
            TRADE_ITEM_ID + " VARCHAR NOT NULL, " +
            LOT_ID + " VARCHAR  NOT NULL, " +
            REASON + " VARCHAR, " +
            STATUS + " VARCHAR, " +
            VALUE + " INTEGER NOT NULL, " +
            LAST_UPDATED + " INTEGER NOT NULL)";

    private static final String CREATE_PROGRAM_TRADE_ITEM_INDEX = "CREATE INDEX "
            + STOCK_TAKE_TABLE + "_INDEX ON "
            + STOCK_TAKE_TABLE + "(" + PROGRAM_ID + "," + TRADE_ITEM_ID + ")";

    public StockTakeRepository(Repository repository) {
        super(repository);
    }

    public static void createTable(SQLiteDatabase database) {
        database.execSQL(CREATE_STOCK_TAKE_TABLE);
        database.execSQL(CREATE_PROGRAM_TRADE_ITEM_INDEX);
    }

    public void addOrUpdate(StockTake stockTake) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PROGRAM_ID, stockTake.getProgramId());
        contentValues.put(TRADE_ITEM_ID, stockTake.getTradeItemId());
        contentValues.put(LOT_ID, stockTake.getLotId());
        contentValues.put(REASON, stockTake.getReasonId());
        contentValues.put(STATUS, stockTake.getStatus());
        contentValues.put(VALUE, stockTake.getQuantity());
        contentValues.put(LAST_UPDATED, System.currentTimeMillis());
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

    private boolean exists(StockTake stockTake) {
        boolean hasLots = StringUtils.isNotBlank(stockTake.getLotId());
        String query;
        Cursor cursor = null;
        try {
            if (hasLots) {
                query = String.format("SELECT 1 FROM %s WHERE %s=? AND %s=? AND %s=?", STOCK_TAKE_TABLE, PROGRAM_ID, TRADE_ITEM_ID, LOT_ID);
                cursor = getReadableDatabase().rawQuery(query, new String[]{stockTake.getProgramId(), stockTake.getTradeItemId()});
            } else {
                query = String.format("SELECT 1 FROM %s WHERE %s=? AND %s=?", STOCK_TAKE_TABLE, PROGRAM_ID, TRADE_ITEM_ID);
                cursor = getReadableDatabase().rawQuery(query, new String[]{stockTake.getProgramId(), stockTake.getTradeItemId()});
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

    private StockTake createStockTake(Cursor cursor) {
        StockTake stockTake = new StockTake(
                cursor.getString(cursor.getColumnIndex(PROGRAM_ID)),
                cursor.getString(cursor.getColumnIndex(TRADE_ITEM_ID)),
                cursor.getString(cursor.getColumnIndex(LOT_ID)));
        stockTake.setStatus(cursor.getString(cursor.getColumnIndex(STATUS)));
        stockTake.setReasonId(cursor.getString(cursor.getColumnIndex(REASON)));
        stockTake.setQuantity(cursor.getInt(cursor.getColumnIndex(VALUE)));
        stockTake.setLastUpdated(cursor.getInt(cursor.getColumnIndex(LAST_UPDATED)));
        return stockTake;
    }

}
