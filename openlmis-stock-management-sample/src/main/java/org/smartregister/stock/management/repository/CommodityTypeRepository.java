package org.smartregister.stock.management.repository;

import android.util.Log;
import android.util.Pair;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.smartregister.repository.BaseRepository;
import org.smartregister.repository.Repository;
import org.smartregister.stock.management.domain.CommodityType;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import static org.smartregister.stock.management.util.Utils.INSERT_OR_REPLACE;

public class CommodityTypeRepository extends BaseRepository {

    public static final String TAG = CommodityType.class.getName();
    public static final String COMMODITY_TYPE_TABLE = "commodity_types";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String PARENT = "parent";
    public static final String CLASSIFICATION_SYSTEM = "classification_system";
    public static final String CLASSIFICATION_ID = "classification_id";
    public static final String DATE_UPDATED = "date_updated";
    public static final String[] COMMODITY_TYPE_TABLE_COLUMNS = {ID, NAME, PARENT, CLASSIFICATION_SYSTEM, CLASSIFICATION_ID, DATE_UPDATED};
    public static final String[] SELECT_TABLE_COLUMNS = {ID, NAME, PARENT, CLASSIFICATION_SYSTEM, CLASSIFICATION_ID};

    public static final String CREATE_COMMODITY_TYPE_TABLE =

            "CREATE TABLE " + COMMODITY_TYPE_TABLE
            + "("
                    + ID + " VARCHAR NOT NULL PRIMARY KEY,"
                    + NAME + " VARCHAR NOT NULL,"
                    + PARENT + " VARCHAR,"
                    + CLASSIFICATION_SYSTEM + " VARCHAR,"
                    + CLASSIFICATION_ID + " VARCHAR,"
                    + DATE_UPDATED + " INTEGER"
            + ")";

    public CommodityTypeRepository(Repository repository) { super(repository); }

    public static void createTable(SQLiteDatabase database) {
        database.execSQL(CREATE_COMMODITY_TYPE_TABLE);
    }

    public void addOrUpdate(CommodityType commodityType) {

        if (commodityType == null) {
            return;
        }

        if (commodityType.getDateUpdated() == null) {
            commodityType.setDateUpdated(Calendar.getInstance().getTimeInMillis());
        }

        try {
            SQLiteDatabase database = getWritableDatabase();

            String query = String.format(INSERT_OR_REPLACE, COMMODITY_TYPE_TABLE);
            query += "(" + createQueryValues(commodityType) + ")";
            database.execSQL(query);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    public List<CommodityType> findCommodityTypes(String id, String name, String parent, String classificationSystem, String classificationId) {

        List<CommodityType> commodityTypes = new ArrayList<>();
        Cursor cursor = null;
        try {
            String[] selectionArgs = new String[]{id, name, parent, classificationSystem, classificationId};
            Pair<String, String[]> query= createQuery(selectionArgs);

            String querySelectString =  query.first;
            selectionArgs = query.second;

            cursor = getReadableDatabase().query(COMMODITY_TYPE_TABLE, COMMODITY_TYPE_TABLE_COLUMNS, querySelectString, selectionArgs, null, null, null);
            commodityTypes = readCommodityTypes(cursor);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return commodityTypes;
    }

    /**
     *
     * This method takes an array of {@param columnValues} and returns a {@code Pair} comprising of
     * the query string select statement and the query string arguments array.
     *
     * It assumes that {@param columnValues} is the same size as {@link SELECT_TABLE_COLUMNS} and
     * that select arguments are in the same order as {@link SELECT_TABLE_COLUMNS} column values.
     *
     * @param columnValues
     * @return
     */
    private Pair<String, String[]> createQuery(String[] columnValues) {

        String queryString = "";
        List<String> selectionArgs = new ArrayList<>();
        for (int i = 0; i < columnValues.length; i++) {
            if (columnValues[i] == null) {
                continue;
            }

            queryString += SELECT_TABLE_COLUMNS[i] + "=?";
            if (i != columnValues.length - 1) {
                queryString += " AND ";
            }
            selectionArgs.add(columnValues[i]);
        }

        String[] args = new String[selectionArgs.size()];
        args = selectionArgs.toArray(args);

        return new Pair<>(queryString, args);
    }

    private List<CommodityType> readCommodityTypes(Cursor cursor) {

        List<CommodityType> commodityTypes = new ArrayList<>();
        try {
            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    commodityTypes.add(createCommodityType(cursor));
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
        return commodityTypes;
    }

    private CommodityType createCommodityType(Cursor cursor) {

        return new CommodityType(
                UUID.fromString(cursor.getString(cursor.getColumnIndex(ID))),
                cursor.getString(cursor.getColumnIndex(NAME)),
                cursor.getString(cursor.getColumnIndex(PARENT)),
                cursor.getString(cursor.getColumnIndex(CLASSIFICATION_SYSTEM)),
                cursor.getString(cursor.getColumnIndex(CLASSIFICATION_ID)),
                cursor.getLong(cursor.getColumnIndex(DATE_UPDATED))
        );
    }

    private String createQueryValues(CommodityType commodityType) {

        String values = "";
        values += "'" + commodityType.getId().toString() + "'" + ",";
        values += "'" + commodityType.getName() + "'"  + ",";
        values += "'" + commodityType.getParentId() + "'"  + ",";
        values += "'" + commodityType.getClassificationSystem() + "'"  + ",";
        values += "'" + commodityType.getClassificationId() + "'"  + ",";
        values += commodityType.getDateUpdated();

        return values;
    }
}
