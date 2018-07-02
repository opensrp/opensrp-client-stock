package org.smartregister.stock.management.repository;

import android.util.Log;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.smartregister.repository.BaseRepository;
import org.smartregister.repository.Repository;
import org.smartregister.stock.management.domain.Dispensable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import static org.smartregister.stock.management.util.Utils.INSERT_OR_REPLACE;

public class DispensableRepository extends BaseRepository {

    public static final String TAG = DispensableRepository.class.getName();
    public static final String DISPENSABLE_TABLE = "dispensables";
    public static final String ID = "id";
    public static final String KEY_DISPENSING_UNIT = "key_dispensing_unit";
    public static final String KEY_SIZE_CODE = "key_size_code";
    public static final String KEY_ROUTE_OF_ADMINISTRATION = "key_route_of_administration";
    public static final String[] DISPENSABLE_TABLE_COLUMNS = {ID, KEY_DISPENSING_UNIT, KEY_SIZE_CODE, KEY_ROUTE_OF_ADMINISTRATION};

    public static final String CREATE_DISPENSABLE_TABLE =

            "CREATE TABLE " + DISPENSABLE_TABLE
            + "("
                    + ID + " VARCHAR NOT NULL,"
                    + KEY_DISPENSING_UNIT + " VARCHAR,"
                    + KEY_SIZE_CODE + " VARCHAR,"
                    + KEY_ROUTE_OF_ADMINISTRATION + " VARCHAR"
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

    public List<Dispensable> findDispensables(String id, String code, String name, String active) {

        List<Dispensable> dispensables = new ArrayList<>();
        Cursor cursor = null;
        try {
            String query = ID + "=?" + " AND " + KEY_DISPENSING_UNIT  + "=?" + " AND " + KEY_SIZE_CODE + "=?" + " AND " + KEY_ROUTE_OF_ADMINISTRATION + "=?";
            String[] selectionArgs = new String[]{id, code, name, active};
            cursor = getReadableDatabase().query(DISPENSABLE_TABLE, DISPENSABLE_TABLE_COLUMNS, query, selectionArgs, null, null, null);
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
                cursor.getString(cursor.getColumnIndex(KEY_DISPENSING_UNIT)),
                cursor.getString(cursor.getColumnIndex(KEY_SIZE_CODE)),
                cursor.getString(cursor.getColumnIndex(KEY_ROUTE_OF_ADMINISTRATION))
        );
    }
    
    private String formatTableValues(Dispensable dispensable) {

        String values = "";
        values += dispensable.getId().toString() + ",";
        values += dispensable.getAttributes().get(KEY_DISPENSING_UNIT) + ",";
        values += dispensable.getAttributes().get(KEY_SIZE_CODE) + ",";
        values += dispensable.getAttributes().get(KEY_ROUTE_OF_ADMINISTRATION) + ",";
        values += dispensable.getDateUpdated();

        return values;
    }
}
