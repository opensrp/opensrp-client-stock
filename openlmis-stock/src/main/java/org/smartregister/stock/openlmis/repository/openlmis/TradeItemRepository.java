package org.smartregister.stock.openlmis.repository.openlmis;

import android.util.Log;
import android.util.Pair;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.repository.BaseRepository;
import org.smartregister.repository.Repository;
import org.smartregister.stock.openlmis.domain.openlmis.Gtin;
import org.smartregister.stock.openlmis.domain.openlmis.TradeItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.smartregister.stock.openlmis.util.Utils.INSERT_OR_REPLACE;
import static org.smartregister.stock.openlmis.util.Utils.createQuery;

public class TradeItemRepository extends BaseRepository {

    public static final String TAG = TradeItemRepository.class.getName();
    public static final String TRADE_ITEM_TABLE = "trade_items";
    public static final String ID = "id";
    public static final String GTIN = "gtin";
    public static final String MANUFACTURER_OF_TRADE_ITEM = "manufacturer_of_trade_item";
    public static final String DATE_UPDATED = "date_updated";
    public static final String[] TRADE_ITEM_TABLE_COLUMNS = new String[]{ID, GTIN, MANUFACTURER_OF_TRADE_ITEM, DATE_UPDATED};
    private static final String[] SELECT_TABLE_COLUMNS = new String[]{ID, GTIN, MANUFACTURER_OF_TRADE_ITEM};

    public static final String CREATE_TRADE_ITEM_TABLE =

            "CREATE TABLE " + TRADE_ITEM_TABLE
            + "("
                    + ID + " VARCHAR NOT NULL PRIMARY KEY,"
                    + GTIN + " VARCHAR,"
                    + MANUFACTURER_OF_TRADE_ITEM + " VARCHAR NOT NULL,"
                    + DATE_UPDATED + " INTEGER"
            + ")";

    public TradeItemRepository(Repository repository) { super(repository); }

    public static void createTable(SQLiteDatabase database) {
        database.execSQL(CREATE_TRADE_ITEM_TABLE);
    }

    public void addOrUpdate(TradeItem tradeItem) {

        if (tradeItem == null) {
            return;
        }

        if (tradeItem.getDateUpdated() == null) {
            tradeItem.setDateUpdated(Calendar.getInstance().getTimeInMillis());
        }

        try {
            SQLiteDatabase database = getWritableDatabase();
            String query = String.format(INSERT_OR_REPLACE, TRADE_ITEM_TABLE);
            query += "(" + StringUtils.repeat("?", ",", TRADE_ITEM_TABLE_COLUMNS.length) + ")";
            database.execSQL(query, createQueryValues(tradeItem));
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    public List<TradeItem> findTradeItems(String id, String gtin, String manufacturerOfTradeItem) {

        List<TradeItem> tradeItems = new ArrayList<>();
        Cursor cursor = null;
        try {
            String[] selectionArgs = new String[]{id, gtin, manufacturerOfTradeItem};
            Pair<String, String[]> query = createQuery(selectionArgs, SELECT_TABLE_COLUMNS);

            String querySelectString =  query.first;
            selectionArgs = query.second;

            cursor = getReadableDatabase().query(TRADE_ITEM_TABLE, TRADE_ITEM_TABLE_COLUMNS, querySelectString, selectionArgs, null, null, null);
            tradeItems = readTradeItems(cursor);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return tradeItems;
    }


    private List<TradeItem> readTradeItems(Cursor cursor) {

        List<TradeItem> tradeItems = new ArrayList<>();
        try {
            if (cursor != null && cursor.getCount() != 0 && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    tradeItems.add(createTradeItem(cursor));
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return tradeItems;
    }

    private TradeItem createTradeItem(Cursor cursor) {

        try {
            return new TradeItem(
                    cursor.getString(cursor.getColumnIndex(ID)),
                    new Gtin(cursor.getString(cursor.getColumnIndex(GTIN))),
                    cursor.getString(cursor.getColumnIndex(MANUFACTURER_OF_TRADE_ITEM)),
                    cursor.getLong(cursor.getColumnIndex(DATE_UPDATED))
            );
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
        return  null;
    }

    private Object[] createQueryValues(TradeItem tradeItem) {

        Object[] values = new Object[] {
            tradeItem.getId().toString(),
            tradeItem.getGtin() == null ? null : tradeItem.getGtin().toString(),
            tradeItem.getManufacturerOfTradeItem(),
            tradeItem.getDateUpdated(),
        };
        return values;
    }
}
