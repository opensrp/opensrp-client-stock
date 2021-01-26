package org.smartregister.stock.repository;

/**
 * Created by samuelgithengi on 2/6/18.
 */

import android.content.ContentValues;
import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import net.sqlcipher.SQLException;
import net.sqlcipher.database.SQLiteDatabase;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.repository.BaseRepository;
import org.smartregister.stock.domain.StockType;
import org.smartregister.util.DatabaseMigrationUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import timber.log.Timber;

import static org.smartregister.stock.repository.StockRepository.STOCK_TYPE_ID;

public class StockTypeRepository extends BaseRepository {

    public static final String STOCK_TYPE_TABLE_NAME = "stock_types";
    public static final String ID_COLUMN = "_id";
    public static final String UNIQUE_ID = "unique_id";
    public static final String QUANTITY = "quantity";
    public static final String NAME = "name";
    public static final String MATERIAL_NUMBER = "material_number";
    public static final String IS_ATTRACTIVE_ITEM = "is_attractive_item";
    public static final String AVAILABILITY = "availability";
    public static final String CONDITION = "condition";
    public static final String APPROPRIATE_USAGE = "appropriate_usage";
    public static final String ACCOUNTABILITY_PERIOD = "accountability_period";
    public static final String OPENMRS_PARENT_ENTITIY_ID = "openmrs_parent_entity_id";
    public static final String OPENMRS_DATE_CONCEPT_ID = "openmrs_date_concept_id";
    public static final String OPENMRS_QUANTITY_CONCEPT_ID = "openmrs_quantity_concept_id";
    public static final String PHOTO_URL = "photo_url";
    public static final String PHOTO_FILE_LOCATION = "photo_file_location";
    public static final String SERVER_VERSION = "server_version";


    private static final String STOCK_TYPE_SQL = "CREATE TABLE stock_types (" +
            ID_COLUMN + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            UNIQUE_ID + " INTEGER," +
            QUANTITY + " INTEGER," +
            NAME + " VARCHAR NOT NULL," +
            MATERIAL_NUMBER + " VARCHAR NULL," +
            IS_ATTRACTIVE_ITEM + " VARCHAR NULL," +
            AVAILABILITY + " VARCHAR NULL," +
            CONDITION + " VARCHAR NULL," +
            APPROPRIATE_USAGE + " VARCHAR NULL," +
            ACCOUNTABILITY_PERIOD + " VARCHAR NULL," +
            PHOTO_URL + " VARCHAR NULL," +
            PHOTO_FILE_LOCATION + " VARCHAR NULL," +
            SERVER_VERSION + " INTEGER NULL," +
            OPENMRS_PARENT_ENTITIY_ID + " VARCHAR NULL," +
            OPENMRS_DATE_CONCEPT_ID + " VARCHAR NULL," +
            OPENMRS_QUANTITY_CONCEPT_ID + " VARCHAR)";

    public static final String[] STOCK_Type_TABLE_COLUMNS = {ID_COLUMN, UNIQUE_ID, QUANTITY, NAME, MATERIAL_NUMBER, IS_ATTRACTIVE_ITEM,
            AVAILABILITY, CONDITION, APPROPRIATE_USAGE, ACCOUNTABILITY_PERIOD, SERVER_VERSION, OPENMRS_PARENT_ENTITIY_ID, OPENMRS_DATE_CONCEPT_ID,
            OPENMRS_QUANTITY_CONCEPT_ID, PHOTO_URL, PHOTO_FILE_LOCATION};

    public static void createTable(SQLiteDatabase database) {
        database.execSQL(STOCK_TYPE_SQL);
    }

    public void batchInsertStockTypes(@Nullable List<StockType> stockTypes) {
        if (stockTypes != null) {
            Set<Long> stockTypeIdsFromResponse = getStockTypeUniqueIdsFromResponse(stockTypes);
            Set<Long> stockTypeIds = populateStockTypeUniqueIds(stockTypeIdsFromResponse);
            SQLiteDatabase sqLiteDatabase = getWritableDatabase();
            sqLiteDatabase.beginTransaction();
            for (StockType stockType : stockTypes) {
                ContentValues contentValues = createValuesFor(stockType);
                if (!stockTypeIds.contains(stockType.getUniqueId())) {
                    sqLiteDatabase.insert(STOCK_TYPE_TABLE_NAME, null, contentValues);
                } else {
                    String idSelection = UNIQUE_ID + " = ?";
                    contentValues.remove(ID_COLUMN);
                    sqLiteDatabase.update(STOCK_TYPE_TABLE_NAME, contentValues, idSelection, new String[]{stockType.getUniqueId().toString()});
                }
            }
            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();
        }
    }

    private Set<Long> getStockTypeUniqueIdsFromResponse(@NonNull List<StockType> stockTypes) {
        Set<Long> stockTypeUniqueIds = new HashSet<>();
        for (StockType stockType : stockTypes) {
            Long stockTypeUniqueId = stockType.getUniqueId();
            if (stockTypeUniqueId != null) {
                stockTypeUniqueIds.add(stockTypeUniqueId);
            }
        }
        return stockTypeUniqueIds;
    }

    protected Set<Long> populateStockTypeUniqueIds(@NonNull Set<Long> stockTypeUniqueIds) {
        Set<Long> tempStockTypeUniqueIds = new HashSet<>();
        String query = "SELECT " + UNIQUE_ID + " FROM " + STOCK_TYPE_TABLE_NAME +
                " WHERE " + UNIQUE_ID + " IN ( " + StringUtils.repeat("?", ", ", stockTypeUniqueIds.size()) + ")";
        try (Cursor mCursor = getReadableDatabase().rawQuery(query, stockTypeUniqueIds.toArray(new Long[0]))) {
            if (mCursor != null) {
                while (mCursor.moveToNext()) {
                    tempStockTypeUniqueIds.add(mCursor.getLong(mCursor.getColumnIndex(UNIQUE_ID)));
                }
            }
        } catch (SQLException e) {
            Timber.e(e);
        }
        return tempStockTypeUniqueIds;
    }

    public void add(StockType stockType, SQLiteDatabase database_) {
        SQLiteDatabase database = database_;
        if (stockType == null) {
            return;
        }

        if (database == null) {
            database = getWritableDatabase();
        }

        if (stockType.getId() == null) {
            stockType.setId(database.insert(STOCK_TYPE_TABLE_NAME, null, createValuesFor(stockType)));
        } else {
            //mark the stock as unsynced for processing as an updated event
            String idSelection = ID_COLUMN + " = ?";
            database.update(STOCK_TYPE_TABLE_NAME, createValuesFor(stockType), idSelection, new String[]{stockType.getId().toString()});
        }
    }

    public List<StockType> findIDByName(String Name) {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(STOCK_TYPE_TABLE_NAME, STOCK_Type_TABLE_COLUMNS, NAME + " = ? ", new String[]{Name}, null, null, null, null);
        return readAllStock(cursor);
    }

    public List<StockType> getAllStockTypes(SQLiteDatabase database_) {
        SQLiteDatabase database = database_;
        if (database == null) {
            database = getReadableDatabase();
        }

        Cursor cursor = database.query(STOCK_TYPE_TABLE_NAME, STOCK_Type_TABLE_COLUMNS, null,
                null, null, null, null, null);
        return readAllStock(cursor);
    }

    public List<StockType> getAllStockTypes() {
        return getAllStockTypes(getReadableDatabase());
    }

    public List<StockType> findAllWithUnDownloadedPhoto() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(STOCK_TYPE_TABLE_NAME, STOCK_Type_TABLE_COLUMNS, PHOTO_URL + " IS NOT NULL AND " + PHOTO_FILE_LOCATION + " IS NULL",
                null, null, null, null);
        return readAllStock(cursor);
    }

    public int getDosesPerVial(String name) {
        int dosesperstock = 1;
        ArrayList<StockType> stockTypes = (ArrayList<StockType>) findIDByName(name);
        for (int i = 0; i < stockTypes.size(); i++) {
            dosesperstock = stockTypes.get(0).getQuantity();
        }
        return dosesperstock;
    }

    private List<StockType> readAllStock(Cursor cursor) {
        List<StockType> stocks = new ArrayList<StockType>();

        try {
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    StockType stockType = readStockType(cursor);
                    stocks.add(stockType);
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

    public StockType readStockType(Cursor cursor) {
        StockType stockType = new StockType(cursor.getLong(cursor.getColumnIndex(ID_COLUMN)),
                cursor.getInt(cursor.getColumnIndex(QUANTITY)),
                cursor.getString(cursor.getColumnIndex(NAME)),
                cursor.getString(cursor.getColumnIndex(OPENMRS_PARENT_ENTITIY_ID)),
                cursor.getString(cursor.getColumnIndex(OPENMRS_DATE_CONCEPT_ID)),
                cursor.getString(cursor.getColumnIndex(OPENMRS_QUANTITY_CONCEPT_ID))
        );
        stockType.setProductName(cursor.getString(cursor.getColumnIndex(NAME)));
        stockType.setAccountabilityPeriod(cursor.getInt(cursor.getColumnIndex(ACCOUNTABILITY_PERIOD)));
        stockType.setAppropriateUsage(cursor.getString(cursor.getColumnIndex(APPROPRIATE_USAGE)));
        stockType.setAvailability(cursor.getString(cursor.getColumnIndex(AVAILABILITY)));
        stockType.setCondition(cursor.getString(cursor.getColumnIndex(CONDITION)));
        stockType.setIsAttractiveItem(Boolean.valueOf(cursor.getString(cursor.getColumnIndex(IS_ATTRACTIVE_ITEM))));
        stockType.setMaterialNumber(cursor.getString(cursor.getColumnIndex(MATERIAL_NUMBER)));
        stockType.setUniqueId(cursor.getLong(cursor.getColumnIndex(UNIQUE_ID)));
        stockType.setPhotoURL(cursor.getString(cursor.getColumnIndex(PHOTO_URL)));
        stockType.setPhotoFileLocation(cursor.getString(cursor.getColumnIndex(PHOTO_FILE_LOCATION)));
        stockType.setServerVersion(cursor.getLong(cursor.getColumnIndex(SERVER_VERSION)));
        return stockType;
    }

    public void updatePhotoLocation(Long id, String location) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL("UPDATE " + STOCK_TYPE_TABLE_NAME + " SET " + PHOTO_FILE_LOCATION + " = ? " +
                "WHERE " + ID_COLUMN + " = ?", new String[]{location, id.toString()});
    }

    private ContentValues createValuesFor(@NonNull StockType stockType) {
        ContentValues values = new ContentValues();
        values.put(ID_COLUMN, stockType.getId());
        values.put(NAME, StringUtils.isBlank(stockType.getName()) ? stockType.getProductName() : stockType.getName());
        values.put(QUANTITY, stockType.getQuantity());
        values.put(ACCOUNTABILITY_PERIOD, stockType.getAccountabilityPeriod());
        values.put(APPROPRIATE_USAGE, stockType.getAppropriateUsage());
        values.put(AVAILABILITY, stockType.getAvailability());
        values.put(IS_ATTRACTIVE_ITEM, stockType.getIsAttractiveItem());
        values.put(CONDITION, stockType.getCondition());
        values.put(MATERIAL_NUMBER, stockType.getMaterialNumber());
        values.put(UNIQUE_ID, stockType.getUniqueId());
        values.put(PHOTO_URL, StringUtils.isNotBlank(stockType.getPhotoURL()) ? stockType.getPhotoURL() : null);
        values.put(SERVER_VERSION, stockType.getServerVersion());
        values.put(OPENMRS_DATE_CONCEPT_ID, stockType.getOpenmrsDateConceptId());
        values.put(OPENMRS_QUANTITY_CONCEPT_ID, stockType.getOpenmrsQuantityConceptId());
        values.put(OPENMRS_PARENT_ENTITIY_ID, stockType.getOpenmrsParentEntityId());
        return values;
    }

    public int getBalanceFromNameAndDate(String Name, Long updatedat) {
        SQLiteDatabase database = getReadableDatabase();
        List<StockType> allStockTypes = findIDByName(Name);
        String id_for_stock = "";
        if (allStockTypes.size() > 0) {
            id_for_stock = "" + allStockTypes.get(0).getId();
        }
        Cursor c = database.rawQuery("Select sum(value) from Stock Where date_created <=" + updatedat + " and " + STOCK_TYPE_ID + " = " + id_for_stock, null);
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

    public static void migrationAddServerVersionColumn(@NonNull SQLiteDatabase
                                                               sqLiteDatabase) {
        DatabaseMigrationUtils.addColumnIfNotExists(sqLiteDatabase, STOCK_TYPE_TABLE_NAME, SERVER_VERSION, "INTEGER");
    }

    public static void migrationAdditionalProductProperties(@NonNull SQLiteDatabase
                                                                    sqLiteDatabase) {
        DatabaseMigrationUtils.addColumnIfNotExists(sqLiteDatabase, STOCK_TYPE_TABLE_NAME, MATERIAL_NUMBER, "VARCHAR");
        DatabaseMigrationUtils.addColumnIfNotExists(sqLiteDatabase, STOCK_TYPE_TABLE_NAME, APPROPRIATE_USAGE, "VARCHAR");
        DatabaseMigrationUtils.addColumnIfNotExists(sqLiteDatabase, STOCK_TYPE_TABLE_NAME, IS_ATTRACTIVE_ITEM, "VARCHAR");
        DatabaseMigrationUtils.addColumnIfNotExists(sqLiteDatabase, STOCK_TYPE_TABLE_NAME, CONDITION, "VARCHAR");
        DatabaseMigrationUtils.addColumnIfNotExists(sqLiteDatabase, STOCK_TYPE_TABLE_NAME, PHOTO_FILE_LOCATION, "VARCHAR");
        DatabaseMigrationUtils.addColumnIfNotExists(sqLiteDatabase, STOCK_TYPE_TABLE_NAME, UNIQUE_ID, "INTEGER");
        DatabaseMigrationUtils.addColumnIfNotExists(sqLiteDatabase, STOCK_TYPE_TABLE_NAME, ACCOUNTABILITY_PERIOD, "VARCHAR");
        DatabaseMigrationUtils.addColumnIfNotExists(sqLiteDatabase, STOCK_TYPE_TABLE_NAME, PHOTO_URL, "VARCHAR");

        DatabaseMigrationUtils.addIndexIfNotExists(sqLiteDatabase, STOCK_TYPE_TABLE_NAME, UNIQUE_ID);
    }
}
