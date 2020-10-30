package org.smartregister.stock.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import net.sqlcipher.database.SQLiteDatabase;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.smartregister.repository.BaseRepository;
import org.smartregister.repository.Repository;
import org.smartregister.stock.StockLibrary;
import org.smartregister.stock.domain.Stock;
import org.smartregister.stock.domain.StockType;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by samuelgithengi on 2/6/18.
 */

public class StockRepository extends BaseRepository {

    private static final String TAG = StockRepository.class.getCanonicalName();

    private static final String STOCK_SQL = "CREATE TABLE stocks (_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "stock_type_id VARCHAR NOT NULL," +
            "transaction_type VARCHAR NULL," +
            "providerid VARCHAR NOT NULL," +
            "value INTEGER," +
            "date_created DATETIME NOT NULL," +
            "to_from VARCHAR NULL," +
            "sync_status VARCHAR," +
            "date_updated INTEGER NULL)";
    public static final String STOCK_TABLE_NAME = "stocks";
    public static final String ID_COLUMN = "_id";
    public static final String STOCK_TYPE_ID = "stock_type_id";
    public static final String TRANSACTION_TYPE = "transaction_type";
    public static final String PROVIDER_ID = "providerid";
    public static final String VALUE = "value";
    public static final String DATE_CREATED = "date_created";
    public static final String TO_FROM = "to_from";
    public static final String SYNC_STATUS = "sync_status";
    public static final String DATE_UPDATED = "date_updated";
    public static final String LOCATION_ID = "location_id";
    public static final String CHILD_LOCATION_ID = "child_location_id";
    public static final String TEAM_NAME = "team_name";
    public static final String TEAM_ID = "team_id";
    public static final String[] STOCK_TABLE_COLUMNS = {ID_COLUMN, STOCK_TYPE_ID, TRANSACTION_TYPE, PROVIDER_ID, VALUE, DATE_CREATED, TO_FROM, SYNC_STATUS, DATE_UPDATED};

    public static final String TYPE_UNSYNCED = "Unsynced";
    private static final String TYPE_SYNCED = "Synced";

    public StockRepository(Repository repository) {
        super();
    }

    public static void createTable(SQLiteDatabase database) {
        database.execSQL(STOCK_SQL);
    }

    public void add(Stock stock) {
        if (stock == null) {
            return;
        }

        try {
            if (StringUtils.isBlank(stock.getSyncStatus())) {
                stock.setSyncStatus(TYPE_UNSYNCED);
            }

            if (stock.getUpdatedAt() == null) {
                stock.setUpdatedAt(Calendar.getInstance().getTimeInMillis());
            }

            SQLiteDatabase database = getWritableDatabase();
            if (stock.getId() == null) {
                stock.setId(database.insert(STOCK_TABLE_NAME, null, createValuesFor(stock)));
            } else {
                //mark the stock as unsynced for processing as an updated stock
                String idSelection = ID_COLUMN + " = ?";
                database.update(STOCK_TABLE_NAME, createValuesFor(stock), idSelection, new String[]{stock.getId().toString()});
            }
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    private ContentValues createValuesFor(Stock stock) {
        ContentValues values = new ContentValues();
        values.put(ID_COLUMN, stock.getId());
        values.put(STOCK_TYPE_ID, stock.getStockTypeId());
        values.put(TRANSACTION_TYPE, stock.getTransactionType());
        values.put(PROVIDER_ID, stock.getProviderid());
        values.put(VALUE, stock.getValue());
        values.put(DATE_CREATED, stock.getDateCreated() != null ? stock.getDateCreated() : null);
        values.put(TO_FROM, stock.getToFrom());
        values.put(SYNC_STATUS, stock.getSyncStatus());
        values.put(DATE_UPDATED, stock.getUpdatedAt() != null ? stock.getUpdatedAt() : null);
        return values;
    }

    public List<Stock> findUnSyncedBeforeTime(int hours) {
        List<Stock> stocks = new ArrayList<>();
        Cursor cursor = null;
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.HOUR_OF_DAY, -hours);

            Long time = calendar.getTimeInMillis();

            cursor = getReadableDatabase().query(STOCK_TABLE_NAME, STOCK_TABLE_COLUMNS, DATE_UPDATED + " < ? AND " + SYNC_STATUS + " = ?", new String[]{time.toString(), TYPE_UNSYNCED}, null, null, null, null);
            stocks = readAllstocks(cursor);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return stocks;
    }

    public List<Stock> findUnSyncedWithLimit(int limit) {
        List<Stock> stocks = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = getReadableDatabase().query(STOCK_TABLE_NAME, STOCK_TABLE_COLUMNS, SYNC_STATUS + " = ?", new String[]{TYPE_UNSYNCED}, null, null, null, "" + limit);
            stocks = readAllstocks(cursor);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return stocks;
    }

    public List<Stock> findUniqueStock(String stock_type_id, String transaction_type, String providerid, String value, String date_created, String to_from) {
        List<Stock> stocks = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = getReadableDatabase().query(STOCK_TABLE_NAME, STOCK_TABLE_COLUMNS, STOCK_TYPE_ID + " = ? AND " + TRANSACTION_TYPE + " = ? AND " + PROVIDER_ID + " = ? AND " + VALUE + " = ? AND " + DATE_CREATED + " = ? AND " + TO_FROM + " = ?", new String[]{stock_type_id, transaction_type, providerid, value, date_created, to_from}, null, null, null, null);
            stocks = readAllstocks(cursor);

        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return stocks;
    }

    public Stock readAllStockforCursorAdapter(Cursor cursor) {
        return new Stock(cursor.getLong(cursor.getColumnIndex(ID_COLUMN)),
                cursor.getString(cursor.getColumnIndex(TRANSACTION_TYPE)),
                cursor.getString(cursor.getColumnIndex(PROVIDER_ID)),
                cursor.getInt(cursor.getColumnIndex(VALUE)),
                cursor.getLong(cursor.getColumnIndex(DATE_CREATED)),
                cursor.getString(cursor.getColumnIndex(TO_FROM)),
                cursor.getString(cursor.getColumnIndex(SYNC_STATUS)),
                cursor.getLong(cursor.getColumnIndex(DATE_UPDATED)),
                cursor.getString(cursor.getColumnIndex(STOCK_TYPE_ID)));
    }

    private List<Stock> readAllstocks(Cursor cursor) {
        List<Stock> stocks = new ArrayList<>();

        try {
            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {

                    stocks.add(
                            new Stock(cursor.getLong(cursor.getColumnIndex(ID_COLUMN)),
                                    cursor.getString(cursor.getColumnIndex(TRANSACTION_TYPE)),
                                    cursor.getString(cursor.getColumnIndex(PROVIDER_ID)),
                                    cursor.getInt(cursor.getColumnIndex(VALUE)),
                                    cursor.getLong(cursor.getColumnIndex(DATE_CREATED)),
                                    cursor.getString(cursor.getColumnIndex(TO_FROM)),
                                    cursor.getString(cursor.getColumnIndex(SYNC_STATUS)),
                                    cursor.getLong(cursor.getColumnIndex(DATE_UPDATED)),
                                    cursor.getString(cursor.getColumnIndex(STOCK_TYPE_ID))
                            ));

                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            Log.e(getClass().getCanonicalName(), e.getMessage());
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return stocks;
    }

    public int getBalanceBefore(Stock stock) {
        SQLiteDatabase database = getReadableDatabase();
        Cursor c = database.rawQuery("Select sum(value) from stocks Where date_updated <" + stock.getUpdatedAt() + " and date_created <=" + new DateTime(stock.getDateCreated()).toDate().getTime() + " and " + STOCK_TYPE_ID + " = " + stock.getStockTypeId(), null);
        if (c.getCount() == 0) {
            c.close();
            return 0;
        } else {
            c.moveToFirst();
            if (c.getString(0) != null) {
                int toreturn = Integer.parseInt(c.getString(0));
                c.close();
                return toreturn;
            } else {
                c.close();
                return 0;
            }
        }
    }

    public int getBalanceBeforeCheck(Stock stock) {
        int sum = 0;
        SQLiteDatabase database = getReadableDatabase();

        Cursor c = database.rawQuery("Select sum(value) from stocks Where date_created = " + stock.getDateCreated() + " and date_updated <" + stock.getUpdatedAt() + " and " + STOCK_TYPE_ID + " = " + stock.getStockTypeId(), null);
        if (c.getCount() == 0) {
            sum = 0;
        } else {
            c.moveToFirst();
            if (c.getString(0) != null) {
                sum = Integer.parseInt(c.getString(0));
            } else {
                sum = 0;
            }
        }
        c.close();
        c = database.rawQuery("Select sum(value) from stocks Where date_created <" + stock.getDateCreated() + " and " + STOCK_TYPE_ID + " = " + stock.getStockTypeId(), null);
        if (c.getCount() == 0) {
            sum = sum + 0;
        } else {
            c.moveToFirst();
            if (c.getString(0) != null) {
                sum = sum + Integer.parseInt(c.getString(0));
            } else {
                sum = sum + 0;
            }
        }
        c.close();
        return sum;
    }

    public void markEventsAsSynced(ArrayList<Stock> stocks) {
        for (int i = 0; i < stocks.size(); i++) {
            Stock stockToAdd = stocks.get(i);
            stockToAdd.setSyncStatus(TYPE_SYNCED);
            add(stockToAdd);
        }
    }

    public int getBalanceFromNameAndDate(String name, Long updatedAt) {
        SQLiteDatabase database = getReadableDatabase();
        StockTypeRepository stockTypeRepository = StockLibrary.getInstance().getStockTypeRepository();
        ArrayList<StockType> stockTypes = (ArrayList) stockTypeRepository.findIDByName(name);
        String id = "";
        if (stockTypes.size() > 0) {
            id = "" + stockTypes.get(0).getId();
        }
        Cursor c = database.rawQuery("Select sum(value) from stocks Where date_created <=" + updatedAt + " and " + STOCK_TYPE_ID + " = " + id, null);
        if (c.getCount() == 0) {
            c.close();
            return 0;
        } else {
            c.moveToFirst();
            if (c.getString(0) != null) {
                int toreturn = Integer.parseInt(c.getString(0));
                c.close();
                return toreturn;
            } else {
                c.close();
                return 0;
            }
        }
    }

    public int getCurrentStockNumber(StockType stockType) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("Select sum(value) from stocks where " + StockRepository.DATE_CREATED + " <= " + new DateTime(System.currentTimeMillis()).toDate().getTime() + " and " + StockRepository.STOCK_TYPE_ID + " = " + stockType.getId(), null);
        String stockValue = "0";
        if (c.getCount() > 0) {
            c.moveToFirst();
            if (c.getString(0) != null && !StringUtils.isBlank(c.getString(0))) {
                stockValue = c.getString(0);
            }
            c.close();
        } else {
            c.close();
        }
        return Integer.parseInt(stockValue);
    }

    public static void migrateFromOldStockRepository(SQLiteDatabase database, String oldTableName) {
        database.execSQL("ALTER TABLE " + oldTableName + " RENAME TO old_" + oldTableName);
        createTable(database);
        String sql = "INSERT INTO " + STOCK_TABLE_NAME + " SELECT * FROM old_" + oldTableName;
        database.execSQL(sql);
        database.execSQL("DROP TABLE IF EXISTS old_" + oldTableName);
    }
}
