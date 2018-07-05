package org.smartregister.stock.management.repository;

import android.util.Log;
import android.util.Pair;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.smartregister.repository.BaseRepository;
import org.smartregister.repository.Repository;
import org.smartregister.stock.management.domain.Dispensable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import static org.smartregister.stock.management.domain.Dispensable.KEY_DISPENSING_UNIT;
import static org.smartregister.stock.management.domain.Dispensable.KEY_ROUTE_OF_ADMINISTRATION;
import static org.smartregister.stock.management.domain.Dispensable.KEY_SIZE_CODE;
import static org.smartregister.stock.management.util.Utils.INSERT_OR_REPLACE;

public class DispensableRepository extends BaseRepository {

    public static final String TAG = DispensableRepository.class.getName();
    public static final String DISPENSABLE_TABLE = "dispensables";
    public static final String ID = "id";
    public static final String DISPENSING_UNIT = "dispensing_unit";
    public static final String SIZE_CODE = "size_code";
    public static final String ROUTE_OF_ADMINISTRATION = "route_of_administration";
    public static final String DATE_UPDATED = "date_updated";
    public static final String[] DISPENSABLE_TABLE_COLUMNS = {ID, DISPENSING_UNIT, SIZE_CODE, ROUTE_OF_ADMINISTRATION, DATE_UPDATED};
    private static final String[] SELECT_TABLE_COLUMNS = {ID, DISPENSING_UNIT, SIZE_CODE, ROUTE_OF_ADMINISTRATION};

    public static final String CREATE_DISPENSABLE_TABLE =

            "CREATE TABLE " + DISPENSABLE_TABLE
            + "("
                    + ID + " VARCHAR NOT NULL PRIMARY KEY,"
                    + DISPENSING_UNIT + " VARCHAR,"
                    + SIZE_CODE + " VARCHAR,"
                    + ROUTE_OF_ADMINISTRATION + " VARCHAR,"
                    + DATE_UPDATED + " INTEGER"
            + ")";

    public DispensableRepository(Repository repository) { super(repository); }

    public static void createTable(SQLiteDatabase database) {
        database.execSQL(CREATE_DISPENSABLE_TABLE);
    }

    public void addOrUpdate(Dispensable dispensable) {

        if (dispensable == null) {
            return;
        }

        if (dispensable.getDateUpdated() == null) {
            dispensable.setDateUpdated(Calendar.getInstance().getTimeInMillis());
        }

        try {
            SQLiteDatabase database = getWritableDatabase();

            String query = String.format(INSERT_OR_REPLACE, DISPENSABLE_TABLE);
            query += "(" + formatTableValues(dispensable) + ")";
            database.execSQL(query);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    public List<Dispensable> findDispensables(String id, String dispensingUnit, String sizeCode, String routeOfAdministration) {

        List<Dispensable> dispensables = new ArrayList<>();
        Cursor cursor = null;
        try {
            String[] selectionArgs = new String[]{id, dispensingUnit, sizeCode, routeOfAdministration};
            Pair<String, String[]> query= createQuery(selectionArgs);

            String querySelectString =  query.first;
            selectionArgs = query.second;

            cursor = getReadableDatabase().query(DISPENSABLE_TABLE, DISPENSABLE_TABLE_COLUMNS, querySelectString, selectionArgs, null, null, null);
            dispensables = readDispensablesFromCursor(cursor);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return dispensables;
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


    private List<Dispensable> readDispensablesFromCursor(Cursor cursor) {

        List<Dispensable> dispensables = new ArrayList<>();
        try {
            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    dispensables.add(createDispensableFromCursor(cursor));
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
        return dispensables;
    }

    private Dispensable createDispensableFromCursor(Cursor cursor) {

        return new Dispensable(
                UUID.fromString(cursor.getString(cursor.getColumnIndex(ID))),
                cursor.getString(cursor.getColumnIndex(DISPENSING_UNIT)),
                cursor.getString(cursor.getColumnIndex(SIZE_CODE)),
                cursor.getString(cursor.getColumnIndex(ROUTE_OF_ADMINISTRATION))
        );
    }
    
    private String formatTableValues(Dispensable dispensable) {

        String values = "";
        values += "'" + dispensable.getId().toString() + "'" + ",";
        values += "'" + dispensable.getAttributes().get(KEY_DISPENSING_UNIT) + "'" +  ",";
        values += "'" + dispensable.getAttributes().get(KEY_SIZE_CODE) + "'" +  ",";
        values += "'" + dispensable.getAttributes().get(KEY_ROUTE_OF_ADMINISTRATION) + "'" +  ",";
        values += dispensable.getDateUpdated();

        return values;
    }
}
