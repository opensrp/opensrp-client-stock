package org.smartregister.stock.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import androidx.annotation.NonNull;

import net.sqlcipher.SQLException;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteException;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.smartregister.domain.StockAndProductDetails;
import org.smartregister.repository.BaseRepository;
import org.smartregister.stock.StockLibrary;
import org.smartregister.stock.domain.Stock;
import org.smartregister.stock.domain.StockType;
import org.smartregister.util.DatabaseMigrationUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import timber.log.Timber;

/**
 * Created by samuelgithengi on 2/6/18.
 */

public class StockRepository extends BaseRepository {

    private static final String TAG = StockRepository.class.getCanonicalName();

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
    public static final String IDENTIFIER = "identifier";
    public static final String CUSTOM_PROPERTIES = "custom_properties";
    public static final String STOCK_ID = "stock_id";
    public static final String SERIAL_NUMBER = "serial_number";
    public static final String DELIVERY_DATE = "delivery_date";
    public static final String ACCOUNTABILITY_END_DATE = "accountability_end_date";
    public static final String TYPE = "type";
    public static final String DONOR = "donor";
    public static final String VERSION = "version";
    public static final String SERVER_VERSION = "server_version";

    private static final String STOCK_SQL = "CREATE TABLE " + STOCK_TABLE_NAME + " (" +
            ID_COLUMN + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            IDENTIFIER + " VARCHAR NULL," +
            STOCK_TYPE_ID + " VARCHAR NOT NULL," +
            TRANSACTION_TYPE + " VARCHAR NULL," +
            PROVIDER_ID + " VARCHAR NOT NULL," +
            VALUE + " INTEGER," +
            DATE_CREATED + " DATETIME NOT NULL," +
            TO_FROM + " VARCHAR NULL," +
            SYNC_STATUS + " VARCHAR," +
            LOCATION_ID + " VARCHAR NULL," +
            STOCK_ID + " VARCHAR NULL," +
            CUSTOM_PROPERTIES + " VARCHAR NULL," +
            SERIAL_NUMBER + " VARCHAR NULL," +
            DELIVERY_DATE + " VARCHAR NULL," +
            ACCOUNTABILITY_END_DATE + " VARCHAR NULL," +
            TYPE + " VARCHAR NULL," +
            DONOR + " VARCHAR NULL," +
            VERSION + " INTEGER," +
            SERVER_VERSION + " INTEGER," +
            DATE_UPDATED + " INTEGER NULL)";

    public static final String[] STOCK_TABLE_COLUMNS = {ID_COLUMN, STOCK_TYPE_ID, TRANSACTION_TYPE, PROVIDER_ID, VALUE,
            DATE_CREATED, TO_FROM, SYNC_STATUS, DATE_UPDATED, LOCATION_ID, IDENTIFIER, CUSTOM_PROPERTIES, STOCK_ID,
            SERVER_VERSION, VERSION, DONOR, TYPE, ACCOUNTABILITY_END_DATE, SERIAL_NUMBER, DELIVERY_DATE};

    public static final String TYPE_UNSYNCED = "Unsynced";

    private static final String TYPE_SYNCED = "Synced";

    public static void createTable(SQLiteDatabase database) {
        database.execSQL(STOCK_SQL);
    }

    public void batchInsertStock(@NonNull List<Stock> stocks) {
        Set<String> stockIdsFromResponse = getStockIdsFromResponse(stocks);
        Set<String> stockIds = populateStockIds(stockIdsFromResponse);
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.beginTransaction();
        for (Stock stock : stocks) {
            stock.setSyncStatus(TYPE_SYNCED);
            stock.setDateUpdated(System.currentTimeMillis());
            ContentValues contentValues = createValuesFor(stock);
            if (!stockIds.contains(stock.getStockId())) {
                sqLiteDatabase.insert(STOCK_TABLE_NAME, null, contentValues);
            } else {
                String idSelection = STOCK_ID + " = ?";
                contentValues.remove(ID_COLUMN);
                sqLiteDatabase.update(STOCK_TABLE_NAME, contentValues, idSelection, new String[]{stock.getStockId()});
            }
        }
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
    }

    private Set<String> getStockIdsFromResponse(@NonNull List<Stock> stocks) {
        Set<String> stockIds = new HashSet<>();
        for (Stock stock : stocks) {
            String stockId = stock.getStockId();
            if (StringUtils.isNotBlank(stockId)) {
                stockIds.add(stockId);
            }
        }
        return stockIds;
    }

    private Set<String> populateStockIds(@NonNull Set<String> stockIds) {
        Set<String> tempStockIds = new HashSet<>();
        String query = "SELECT " + STOCK_ID + " FROM " + STOCK_TABLE_NAME +
                " WHERE " + STOCK_ID + " IN ( " + StringUtils.repeat("?", ", ", stockIds.size()) + ")";
        try (Cursor mCursor = getReadableDatabase().rawQuery(query, stockIds.toArray(new String[0]))) {
            if (mCursor != null) {
                while (mCursor.moveToNext()) {
                    tempStockIds.add(mCursor.getString(0));
                }
            }
        } catch (SQLException e) {
            Timber.e(e);
        }
        return tempStockIds;
    }

    public void add(Stock stock) {
        if (stock == null) {
            return;
        }

        try {
            if (StringUtils.isBlank(stock.getSyncStatus())) {
                stock.setSyncStatus(TYPE_UNSYNCED);
            }

            if (stock.getDateUpdated() == null) {
                stock.setDateUpdated(Calendar.getInstance().getTimeInMillis());
            }

            SQLiteDatabase database = getWritableDatabase();
            if (stock.getId() == null || "-1".equals(stock.getId())) {
                stock.setId(String.valueOf(database.insert(STOCK_TABLE_NAME, null, createValuesFor(stock))));
            } else {
                //mark the stock as unsynced for processing as an updated stock
                String idSelection = ID_COLUMN + " = ?";
                database.update(STOCK_TABLE_NAME, createValuesFor(stock), idSelection, new String[]{stock.getId()});
            }
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    private ContentValues createValuesFor(Stock stock) {
        ContentValues values = new ContentValues();
        values.put(ID_COLUMN, (stock.getId() != null && "-1".equals(stock.getId())) ? null : stock.getId());
        values.put(STOCK_TYPE_ID, String.valueOf(stock.getStockTypeId() == null ? stock.getIdentifier() : "0"));
        values.put(TRANSACTION_TYPE, stock.getTransactionType());
        values.put(PROVIDER_ID, stock.getProviderid());
        values.put(VALUE, stock.getValue());
        values.put(DATE_CREATED, stock.getDateCreated() != null ? stock.getDateCreated().getMillis() : System.currentTimeMillis());
        values.put(TO_FROM, stock.getToFrom());
        values.put(SYNC_STATUS, stock.getSyncStatus());
        values.put(DATE_UPDATED, stock.getDateUpdated() != null ? stock.getDateUpdated() : System.currentTimeMillis());
        values.put(IDENTIFIER, stock.getIdentifier());
        values.put(LOCATION_ID, stock.getLocationId());
        values.put(STOCK_ID, stock.getStockId());
        values.put(CUSTOM_PROPERTIES, String.valueOf(stock.getCustomProperties()));
        values.put(SERVER_VERSION, stock.getServerVersion());
        values.put(VERSION, stock.getVersion());
        values.put(DONOR, stock.getDonor());
        values.put(ACCOUNTABILITY_END_DATE, stock.getAccountabilityEndDate() != null ? stock.getAccountabilityEndDate().getTime() : null);
        values.put(SERIAL_NUMBER, stock.getSerialNumber());
        values.put(TYPE, stock.getType());
        values.put(DELIVERY_DATE, stock.getDeliveryDate() != null ? stock.getDeliveryDate().getTime() : null);
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
            Timber.e(e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return stocks;
    }

    public List<Stock> findUniqueStock(String stock_type_id, String transaction_type, String providerid, String value, String date_created, String to_from) {
        List<Stock> stocks = new ArrayList<>();
        try (Cursor cursor = getReadableDatabase().query(STOCK_TABLE_NAME, STOCK_TABLE_COLUMNS, STOCK_TYPE_ID + " = ? AND " + TRANSACTION_TYPE + " = ? AND " + PROVIDER_ID + " = ? AND " + VALUE + " = ? AND " + DATE_CREATED + " = ? AND " + TO_FROM + " = ?", new String[]{stock_type_id, transaction_type, providerid, value, date_created, to_from}, null, null, null, null)) {
            stocks = readAllstocks(cursor);

        } catch (Exception e) {
            Timber.e(e);
        }
        return stocks;
    }

    public Stock readAllStockforCursorAdapter(Cursor cursor) {
        Stock stock = new Stock(cursor.getString(cursor.getColumnIndex(ID_COLUMN)),
                cursor.getString(cursor.getColumnIndex(TRANSACTION_TYPE)),
                cursor.getString(cursor.getColumnIndex(PROVIDER_ID)),
                cursor.getInt(cursor.getColumnIndex(VALUE)),
                cursor.getLong(cursor.getColumnIndex(DATE_CREATED)),
                cursor.getString(cursor.getColumnIndex(TO_FROM)),
                cursor.getString(cursor.getColumnIndex(SYNC_STATUS)),
                cursor.getLong(cursor.getColumnIndex(DATE_UPDATED)),
                cursor.getString(cursor.getColumnIndex(STOCK_TYPE_ID)));
        stock.setIdentifier(cursor.getString(cursor.getColumnIndex(IDENTIFIER)));
        stock.setLocationId(cursor.getString(cursor.getColumnIndex(LOCATION_ID)));
        stock.setStockId(cursor.getString(cursor.getColumnIndex(STOCK_ID)));
        stock.setCustomProperties(cursor.getString(cursor.getColumnIndex(CUSTOM_PROPERTIES)));
        stock.setServerVersion(cursor.getLong(cursor.getColumnIndex(SERVER_VERSION)));
        stock.setVersion(cursor.getLong(cursor.getColumnIndex(VERSION)));
        stock.setType(cursor.getString(cursor.getColumnIndex(TYPE)));
        stock.setDonor(cursor.getString(cursor.getColumnIndex(DONOR)));
        stock.setDeliveryDate(cursor.getString(cursor.getColumnIndex(DELIVERY_DATE)));
        stock.setAccountabilityEndDate(cursor.getString(cursor.getColumnIndex(ACCOUNTABILITY_END_DATE)));
        stock.setSerialNumber(cursor.getString(cursor.getColumnIndex(SERIAL_NUMBER)));
        return stock;
    }

    private List<Stock> readAllstocks(Cursor cursor) {
        List<Stock> stocks = new ArrayList<>();
        try {
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    Stock stock = readAllStockforCursorAdapter(cursor);
                    stocks.add(stock);
                }
            }
        } catch (Exception e) {
            Timber.e(e);
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

    public StockAndProductDetails findStockWithStockTypeByStockId(String stockId) {
        StockAndProductDetails stockAndProductDetails = null;
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = "SELECT * FROM " + STOCK_TABLE_NAME
                + " LEFT JOIN " + StockTypeRepository.STOCK_TYPE_TABLE_NAME + " ON " + STOCK_TABLE_NAME + "." + IDENTIFIER + " = " + StockTypeRepository.STOCK_TYPE_TABLE_NAME + "." + StockTypeRepository.UNIQUE_ID
                + " WHERE " + STOCK_TABLE_NAME + "." + STOCK_ID + " = ? limit 1";

        try (Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{stockId})) {
            if (cursor != null && cursor.moveToNext()) {
                stockAndProductDetails = readStockAndProductDetails(cursor);
            }
        } catch (SQLiteException e) {
            Timber.e(e);
        }
        return stockAndProductDetails;
    }

    private StockAndProductDetails readStockAndProductDetails(Cursor cursor) {
        Stock stock = readAllStockforCursorAdapter(cursor);
        StockType stockType = StockLibrary.getInstance().getStockTypeRepository().readStockType(cursor);
        return new StockAndProductDetails(stock, stockType);
    }

    public static void migrateFromOldStockRepository(SQLiteDatabase database, String oldTableName) {
        database.execSQL("ALTER TABLE " + oldTableName + " RENAME TO old_" + oldTableName);
        createTable(database);
        String sql = "INSERT INTO " + STOCK_TABLE_NAME + " SELECT * FROM old_" + oldTableName;
        database.execSQL(sql);
        database.execSQL("DROP TABLE IF EXISTS old_" + oldTableName);
    }

    public static void migrateAddInventoryColumns(@NonNull SQLiteDatabase database) {
        DatabaseMigrationUtils.addColumnIfNotExists(database, STOCK_TABLE_NAME, IDENTIFIER, "VARCHAR");
        DatabaseMigrationUtils.addColumnIfNotExists(database, STOCK_TABLE_NAME, LOCATION_ID, "VARCHAR");
        DatabaseMigrationUtils.addColumnIfNotExists(database, STOCK_TABLE_NAME, CUSTOM_PROPERTIES, "VARCHAR");
        DatabaseMigrationUtils.addColumnIfNotExists(database, STOCK_TABLE_NAME, STOCK_ID, "VARCHAR");

        DatabaseMigrationUtils.addColumnIfNotExists(database, STOCK_TABLE_NAME, SERVER_VERSION, "INTEGER");
        DatabaseMigrationUtils.addColumnIfNotExists(database, STOCK_TABLE_NAME, SERIAL_NUMBER, "VARCHAR");
        DatabaseMigrationUtils.addColumnIfNotExists(database, STOCK_TABLE_NAME, VERSION, "INTEGER");
        DatabaseMigrationUtils.addColumnIfNotExists(database, STOCK_TABLE_NAME, TYPE, "VARCHAR");
        DatabaseMigrationUtils.addColumnIfNotExists(database, STOCK_TABLE_NAME, DONOR, "VARCHAR");
        DatabaseMigrationUtils.addColumnIfNotExists(database, STOCK_TABLE_NAME, DELIVERY_DATE, "VARCHAR");
        DatabaseMigrationUtils.addColumnIfNotExists(database, STOCK_TABLE_NAME, ACCOUNTABILITY_END_DATE, "VARCHAR");

        DatabaseMigrationUtils.addIndexIfNotExists(database, STOCK_TABLE_NAME, IDENTIFIER);
        DatabaseMigrationUtils.addIndexIfNotExists(database, STOCK_TABLE_NAME, LOCATION_ID);
    }
}
