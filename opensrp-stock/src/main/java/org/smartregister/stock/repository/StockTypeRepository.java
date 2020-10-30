package org.smartregister.stock.repository;

/**
 * Created by samuelgithengi on 2/6/18.
 */

import android.content.ContentValues;
import android.database.Cursor;

import net.sqlcipher.database.SQLiteDatabase;

import org.smartregister.repository.BaseRepository;
import org.smartregister.repository.Repository;
import org.smartregister.stock.domain.StockType;

import java.util.ArrayList;
import java.util.List;

import static org.smartregister.stock.repository.StockRepository.STOCK_TYPE_ID;

public class StockTypeRepository extends BaseRepository {

    private static final String STOCK_TYPE_SQL = "CREATE TABLE stock_types (_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,quantity INTEGER,name VARCHAR NOT NULL,openmrs_parent_entity_id VARCHAR NULL,openmrs_date_concept_id VARCHAR NULL,openmrs_quantity_concept_id VARCHAR)";
    public static final String STOCK_TYPE_TABLE_NAME = "stock_types";
    public static final String ID_COLUMN = "_id";
    public static final String QUANTITY = "quantity";
    public static final String NAME = "name";
    public static final String OPENMRS_PARENT_ENTITIY_ID = "openmrs_parent_entity_id";
    public static final String OPENMRS_DATE_CONCEPT_ID = "openmrs_date_concept_id";
    public static final String OPENMRS_QUANTITY_CONCEPT_ID = "openmrs_quantity_concept_id";

    public static final String[] STOCK_Type_TABLE_COLUMNS = {ID_COLUMN, QUANTITY, NAME, OPENMRS_PARENT_ENTITIY_ID, OPENMRS_DATE_CONCEPT_ID, OPENMRS_QUANTITY_CONCEPT_ID};

    public StockTypeRepository(Repository repository) {
        super();
    }

    public static void createTable(SQLiteDatabase database) {
        database.execSQL(STOCK_TYPE_SQL);
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
            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    stocks.add(
                            new StockType(cursor.getLong(cursor.getColumnIndex(ID_COLUMN)),
                                    cursor.getInt(cursor.getColumnIndex(QUANTITY)),
                                    cursor.getString(cursor.getColumnIndex(NAME)),
                                    cursor.getString(cursor.getColumnIndex(OPENMRS_PARENT_ENTITIY_ID)),
                                    cursor.getString(cursor.getColumnIndex(OPENMRS_DATE_CONCEPT_ID)),
                                    cursor.getString(cursor.getColumnIndex(OPENMRS_QUANTITY_CONCEPT_ID))
                            )
                    );

                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {

        } finally {
            cursor.close();
        }
        return stocks;
    }

    private ContentValues createValuesFor(StockType stockType) {
        ContentValues values = new ContentValues();
        values.put(ID_COLUMN, stockType.getId());
        values.put(NAME, stockType.getName());
        values.put(QUANTITY, stockType.getQuantity());
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
}
