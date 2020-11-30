package org.smartregister.stock.repository;

/**
 * Created by samuelgithengi on 2/6/18.
 */

import android.content.ContentValues;
import android.database.Cursor;

import androidx.annotation.NonNull;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteException;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.repository.BaseRepository;
import org.smartregister.stock.domain.StockType;
import org.smartregister.util.DatabaseMigrationUtils;

import java.util.ArrayList;
import java.util.List;

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
            OPENMRS_PARENT_ENTITIY_ID + " VARCHAR NULL," +
            OPENMRS_DATE_CONCEPT_ID + " VARCHAR NULL," +
            OPENMRS_QUANTITY_CONCEPT_ID + " VARCHAR)";

    public static final String[] STOCK_Type_TABLE_COLUMNS = {ID_COLUMN, UNIQUE_ID, QUANTITY, NAME, MATERIAL_NUMBER, IS_ATTRACTIVE_ITEM,
            AVAILABILITY, CONDITION, APPROPRIATE_USAGE, ACCOUNTABILITY_PERIOD, OPENMRS_PARENT_ENTITIY_ID, OPENMRS_DATE_CONCEPT_ID, OPENMRS_QUANTITY_CONCEPT_ID};

    public static void createTable(SQLiteDatabase database) {
        database.execSQL(STOCK_TYPE_SQL);
    }

    public void add(StockType stockType, SQLiteDatabase database) {
        add(stockType, database, null);
    }

    public void add(StockType stockType, SQLiteDatabase database_, String uniqueColumn) {
        SQLiteDatabase database = database_;
        if (stockType == null) {
            return;
        }

        if (database == null) {
            database = getWritableDatabase();
        }

        if (StringUtils.isBlank(uniqueColumn) || ID_COLUMN.equals(uniqueColumn)) {
            if (stockType.getId() == null) {
                stockType.setId(database.insert(STOCK_TYPE_TABLE_NAME, null, createValuesFor(stockType)));
            } else {
                //mark the stock as unsynced for processing as an updated event
                String idSelection = ID_COLUMN + " = ?";
                database.update(STOCK_TYPE_TABLE_NAME, createValuesFor(stockType), idSelection, new String[]{stockType.getId().toString()});
            }
        } else {
            if (checkIfStockTypeExist(String.valueOf(stockType.getUniqueId()))) {
                String idSelection = UNIQUE_ID + " = ?";
                ContentValues contentValues = createValuesFor(stockType);
                contentValues.remove(ID_COLUMN);
                database.update(STOCK_TYPE_TABLE_NAME, contentValues, idSelection, new String[]{stockType.getUniqueId().toString()});
            } else {
                database.insert(STOCK_TYPE_TABLE_NAME, null, createValuesFor(stockType));
            }
        }
    }

    public boolean checkIfStockTypeExist(String id) {
        if (StringUtils.isNotBlank(id)) {
            SQLiteDatabase sqLiteDatabase = getReadableDatabase();
            try (Cursor cursor = sqLiteDatabase.query(STOCK_TYPE_TABLE_NAME, new String[]{UNIQUE_ID}, UNIQUE_ID + " =? ", new String[]{id}, null, null, null, "1")) {
                if (cursor != null) {
                    return cursor.getCount() > 0;
                }
            } catch (SQLiteException e) {
                Timber.e(e);
            }
        }
        return false;
    }

    public List<StockType> findIDByName(String Name) {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(STOCK_TYPE_TABLE_NAME, STOCK_Type_TABLE_COLUMNS, this.NAME + " = ? ", new String[]{Name}, null, null, null, null);
        return readAllStock(cursor);
    }

    public List<StockType> getAllStockTypes(SQLiteDatabase database_) {
        SQLiteDatabase database = database_;
        if (database == null) {
            database = getReadableDatabase();
        }

        Cursor cursor = database.query(STOCK_TYPE_TABLE_NAME, STOCK_Type_TABLE_COLUMNS, null, null, null, null, null, null);
        return readAllStock(cursor);
    }

    public List<StockType> getAllStockTypes() {
        return getAllStockTypes(getReadableDatabase());
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
                    StockType stockType = new StockType(cursor.getLong(cursor.getColumnIndex(ID_COLUMN)),
                            cursor.getInt(cursor.getColumnIndex(QUANTITY)),
                            cursor.getString(cursor.getColumnIndex(NAME)),
                            cursor.getString(cursor.getColumnIndex(OPENMRS_PARENT_ENTITIY_ID)),
                            cursor.getString(cursor.getColumnIndex(OPENMRS_DATE_CONCEPT_ID)),
                            cursor.getString(cursor.getColumnIndex(OPENMRS_QUANTITY_CONCEPT_ID))
                    );
                    stockType.setAccountabilityPeriod(cursor.getString(cursor.getColumnIndex(ACCOUNTABILITY_PERIOD)));
                    stockType.setAppropriateUsage(cursor.getString(cursor.getColumnIndex(APPROPRIATE_USAGE)));
                    stockType.setAvailability(cursor.getString(cursor.getColumnIndex(AVAILABILITY)));
                    stockType.setCondition(cursor.getString(cursor.getColumnIndex(CONDITION)));
                    stockType.setIsAttractiveItem(cursor.getString(cursor.getColumnIndex(IS_ATTRACTIVE_ITEM)));
                    stockType.setMaterialNumber(cursor.getString(cursor.getColumnIndex(MATERIAL_NUMBER)));
                    stockType.setUniqueId(cursor.getLong(cursor.getColumnIndex(UNIQUE_ID)));
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

    private ContentValues createValuesFor(@NonNull StockType stockType) {
        ContentValues values = new ContentValues();
        values.put(ID_COLUMN, stockType.getId());
        values.put(NAME, stockType.getName());
        values.put(QUANTITY, stockType.getQuantity());
        values.put(ACCOUNTABILITY_PERIOD, stockType.getAccountabilityPeriod());
        values.put(APPROPRIATE_USAGE, stockType.getAppropriateUsage());
        values.put(AVAILABILITY, stockType.getAvailability());
        values.put(IS_ATTRACTIVE_ITEM, stockType.getIsAttractiveItem());
        values.put(CONDITION, stockType.getCondition());
        values.put(MATERIAL_NUMBER, stockType.getMaterialNumber());
        values.put(UNIQUE_ID, stockType.getUniqueId());
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

    public static void migrationAdditionalProductProperties(@NonNull SQLiteDatabase
                                                                    sqLiteDatabase) {
        DatabaseMigrationUtils.addColumnIfNotExists(sqLiteDatabase, STOCK_TYPE_TABLE_NAME, MATERIAL_NUMBER, "VARCHAR");
        DatabaseMigrationUtils.addColumnIfNotExists(sqLiteDatabase, STOCK_TYPE_TABLE_NAME, APPROPRIATE_USAGE, "VARCHAR");
        DatabaseMigrationUtils.addColumnIfNotExists(sqLiteDatabase, STOCK_TYPE_TABLE_NAME, IS_ATTRACTIVE_ITEM, "VARCHAR");
        DatabaseMigrationUtils.addColumnIfNotExists(sqLiteDatabase, STOCK_TYPE_TABLE_NAME, CONDITION, "VARCHAR");
        DatabaseMigrationUtils.addColumnIfNotExists(sqLiteDatabase, STOCK_TYPE_TABLE_NAME, UNIQUE_ID, "INTEGER");
        DatabaseMigrationUtils.addColumnIfNotExists(sqLiteDatabase, STOCK_TYPE_TABLE_NAME, ACCOUNTABILITY_PERIOD, "VARCHAR");
        DatabaseMigrationUtils.addIndexIfNotExists(sqLiteDatabase, STOCK_TYPE_TABLE_NAME, UNIQUE_ID);
    }
}
