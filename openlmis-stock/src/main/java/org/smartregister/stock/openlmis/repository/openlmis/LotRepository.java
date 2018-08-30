package org.smartregister.stock.openlmis.repository.openlmis;

import android.content.ContentValues;
import android.util.Log;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.joda.time.LocalDate;
import org.smartregister.repository.BaseRepository;
import org.smartregister.repository.Repository;
import org.smartregister.stock.openlmis.domain.openlmis.Lot;
import org.smartregister.stock.openlmis.domain.openlmis.TradeItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.smartregister.stock.openlmis.repository.StockRepository.LOT_ID;
import static org.smartregister.stock.repository.StockRepository.STOCK_TYPE_ID;
import static org.smartregister.stock.repository.StockRepository.VALUE;
import static org.smartregister.stock.repository.StockRepository.stock_TABLE_NAME;

/**
 * Created by samuelgithengi on 26/7/18.
 */
public class LotRepository extends BaseRepository {
    public static final String ID = "_id";
    private static final String LOT_CODE = "lot_code";
    public static final String EXPIRATION_DATE = "expiration_date";
    private static final String MANUFACTURE_DATE = "manufacture_date";
    public static final String TRADE_ITEM_ID = "trade_item_id";
    private static final String ACTIVE = "active";
    private static final String LOT_STATUS = "lot_status";
    public static final String LOT_TABLE = "lots";
    private static final String TAG = LotRepository.class.getName();

    private static final String CREATE_LOT_TABLE = "CREATE TABLE " + LOT_TABLE +
            "(" + ID + " VARCHAR NOT NULL PRIMARY KEY," + LOT_CODE + " VARCHAR NOT NULL," +
            EXPIRATION_DATE + " INTEGER NOT NULL," + MANUFACTURE_DATE + " INTEGER NOT NULL," +
            TRADE_ITEM_ID + " VARCHAR NOT NULL," + ACTIVE + " TINYINT," + LOT_STATUS + " VARCHAR);";

    public LotRepository(Repository repository) {
        super(repository);
    }

    public static void createTable(SQLiteDatabase database) {
        database.execSQL(CREATE_LOT_TABLE);
    }

    private boolean lotExists(String id) {
        String query = String.format("SELECT 1 FROM %s WHERE %s=?", LOT_TABLE, ID);
        Cursor cursor = null;
        try {
            cursor = getReadableDatabase().rawQuery(query, new String[]{id});
            return cursor.moveToFirst();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return false;
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    public void addOrUpdate(Lot lot) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(LOT_CODE, lot.getLotCode());
        contentValues.put(EXPIRATION_DATE, lot.getExpirationDate().toDate().getTime());
        contentValues.put(MANUFACTURE_DATE, lot.getManufactureDate().toDate().getTime());
        contentValues.put(TRADE_ITEM_ID, lot.getTradeItem().getId().toString());
        contentValues.put(ACTIVE, lot.isActive());
        contentValues.put(LOT_STATUS, lot.getLotStatus());
        if (lotExists(lot.getId().toString())) {
            getWritableDatabase().update(LOT_TABLE, contentValues, ID + "=?", new String[]{lot.getId().toString()});
        } else {
            contentValues.put(ID, lot.getId().toString());
            getWritableDatabase().insert(LOT_TABLE, null, contentValues);
        }

    }

    public List<Lot> findLotsByTradeItem(String tradeItemId) {
        return findLotsByTradeItem(tradeItemId, false);
    }


    public List<Lot> findLotsByTradeItem(String tradeItemId, boolean filterWithoutStock) {
        String query;
        if (filterWithoutStock)
            query = String.format("SELECT * FROM %s WHERE %s IN " +
                            "(SELECT %s FROM %s  WHERE %s=? AND %s > ? GROUP BY %s having SUM(%s) >0 )" +
                            "ORDER BY %s, %s desc",
                    LOT_TABLE, ID, LOT_ID, stock_TABLE_NAME, STOCK_TYPE_ID, EXPIRATION_DATE, LOT_ID,
                    VALUE, EXPIRATION_DATE, LOT_STATUS);
        else
            query = String.format("SELECT * FROM %s WHERE %s=? AND %s > ? " +
                            "ORDER BY %s, %s desc",
                    LOT_TABLE, TRADE_ITEM_ID, EXPIRATION_DATE,
                    EXPIRATION_DATE, LOT_STATUS);
        Log.d(TAG, query);
        Cursor cursor = null;
        List<Lot> lots = new ArrayList<>();
        try {
            cursor = getReadableDatabase().rawQuery(query, new String[]{tradeItemId, String.valueOf(new LocalDate().toDate().getTime())});
            while (cursor.moveToNext()) {
                lots.add(createLot(cursor));
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return lots;
    }

    public Lot findLotById(String lotId) {

        String query = String.format("SELECT * FROM %s WHERE %s=?",
                LOT_TABLE, ID);
        Cursor cursor = null;
        try {
            cursor = getReadableDatabase().rawQuery(query, new String[]{lotId});
            if (cursor.moveToFirst())
                return createLot(cursor);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public int getNumberOfLotsByTradeItem(String tradeItemId) {
        String query = String.format("SELECT count(*) FROM %s WHERE %s=?", LOT_TABLE, TRADE_ITEM_ID);
        Cursor cursor = null;
        try {
            cursor = getReadableDatabase().rawQuery(query, new String[]{tradeItemId});
            if (cursor.moveToFirst()) {
                return cursor.getInt(0);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return 0;
    }

    public Map<String, String> findLotNames(String tradeItemId) {

        String query = String.format("SELECT DISTINCT %s,%s FROM %s WHERE %s = ?",
                ID, LOT_CODE, LOT_TABLE, TRADE_ITEM_ID);
        Log.d(TAG, query);
        Cursor cursor = null;
        Map<String, String> lots = new HashMap<>();
        try {
            cursor = getReadableDatabase().rawQuery(query, new String[]{tradeItemId});
            while (cursor.moveToNext()) {
                lots.put(cursor.getString(0), cursor.getString(1));
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return lots;

    }

    public Map<String, Integer> getStockByLot(String tradeItemId) {
        String query = String.format("SELECT %s, sum(%s) FROM %s WHERE %s=? GROUP BY %s", LOT_ID, VALUE,
                stock_TABLE_NAME, STOCK_TYPE_ID, LOT_ID);
        Cursor cursor = null;
        Map<String, Integer> lots = new HashMap<>();
        try {
            cursor = getReadableDatabase().rawQuery(query, new String[]{tradeItemId});
            while (cursor.moveToNext()) {
                lots.put(cursor.getString(0), cursor.getInt(1));
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return lots;
    }


    private Lot createLot(Cursor cursor) {
        Lot lot = new Lot(UUID.fromString(cursor.getString(cursor.getColumnIndex(ID))),
                cursor.getString(cursor.getColumnIndex(LOT_CODE)),
                new LocalDate(cursor.getLong(cursor.getColumnIndex(EXPIRATION_DATE))),
                new LocalDate(cursor.getLong(cursor.getColumnIndex(MANUFACTURE_DATE))),
                new TradeItem(UUID.fromString(cursor.getString(cursor.getColumnIndex(TRADE_ITEM_ID)))),
                cursor.getInt(cursor.getColumnIndex(ACTIVE)) > 0);
        lot.setLotStatus(cursor.getString(cursor.getColumnIndex(LOT_STATUS)));
        return lot;
    }
}
