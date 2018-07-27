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
import java.util.List;
import java.util.UUID;

/**
 * Created by samuelgithengi on 26/7/18.
 */
public class LotRepository extends BaseRepository {
    private static final String ID = "_id";
    private static final String LOT_CODE = "lot_code";
    private static final String EXPIRATION_DATE = "expiration_date";
    private static final String MANUFACTURE_DATE = "manufactureDate";
    private static final String TRADE_ITEM_ID = "tradeItem";
    private static final String ACTIVE = "active";
    public static final String LOT_TABLE = "lots";
    private static final String TAG = LotRepository.class.getName();

    private static String CREATE_LOT_TABLE = "CREATE TABLE " + LOT_TABLE +
            "(" + ID + " VARCHAR NOT NULL PRIMARY KEY," + LOT_CODE + " VARCHAR NOT NULL," +
            EXPIRATION_DATE + " INTEGER NOT NULL," + MANUFACTURE_DATE + " INTEGER NOT NULL," +
            TRADE_ITEM_ID + " VARCHAR NOT NULL," + ACTIVE + " TINYINT);";

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
        if (lotExists(lot.getId().toString())) {
            getWritableDatabase().update(LOT_TABLE, contentValues, ID + "=?", new String[]{lot.getId().toString()});
        } else {
            contentValues.put(ID, lot.getId().toString());
            getWritableDatabase().insert(LOT_TABLE, null, contentValues);
        }

    }

    public List<Lot> findLotsByTradeItem(String tradeItemId) {
        String query = String.format("SELECT * FROM %s WHERE %s=?", LOT_TABLE, TRADE_ITEM_ID);
        Cursor cursor = null;
        List<Lot> lots = new ArrayList<>();
        try {
            cursor = getReadableDatabase().rawQuery(query, new String[]{tradeItemId});
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


    private Lot createLot(Cursor cursor) {
        Lot lot = new Lot(UUID.fromString(cursor.getString(cursor.getColumnIndex(ID))),
                cursor.getString(cursor.getColumnIndex(LOT_CODE)),
                new LocalDate(cursor.getLong(cursor.getColumnIndex(EXPIRATION_DATE))),
                new LocalDate(cursor.getLong(cursor.getColumnIndex(MANUFACTURE_DATE))),
                new TradeItem(UUID.fromString(cursor.getString(cursor.getColumnIndex(TRADE_ITEM_ID)))),
                cursor.getInt(cursor.getColumnIndex(ACTIVE)) > 0);
        return lot;
    }
}
