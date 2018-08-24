package org.smartregister.stock.openlmis.repository.openlmis;

import android.util.Log;
import android.util.Pair;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.repository.BaseRepository;
import org.smartregister.repository.Repository;
import org.smartregister.stock.openlmis.domain.openlmis.Dispensable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.smartregister.stock.openlmis.util.Utils.INSERT_OR_REPLACE;
import static org.smartregister.stock.openlmis.util.Utils.createQuery;

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

    public DispensableRepository(Repository repository) {
        super(repository);
    }

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
            query += "(" + StringUtils.repeat("?", ",", DISPENSABLE_TABLE_COLUMNS.length) + ")";
            database.execSQL(query, createQueryValues(dispensable));
            database.close();
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    public List<Dispensable> findDispensables(String id, String dispensingUnit, String sizeCode, String routeOfAdministration) {

        List<Dispensable> dispensables = new ArrayList<>();
        Cursor cursor = null;
        try {
            String[] selectionArgs = new String[]{id, dispensingUnit, sizeCode, routeOfAdministration};
            Pair<String, String[]> query = createQuery(selectionArgs, SELECT_TABLE_COLUMNS);

            String querySelectString = query.first;
            selectionArgs = query.second;

            cursor = getReadableDatabase().query(DISPENSABLE_TABLE, DISPENSABLE_TABLE_COLUMNS, querySelectString, selectionArgs, null, null, null);
            dispensables = readDispensables(cursor);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return dispensables;
    }

    public Dispensable findDispensable(String id) {

        Dispensable dispensable = null;
        Cursor cursor = null;
        try {
            String[] selectionArgs = new String[]{id};
            Pair<String, String[]> query = createQuery(selectionArgs, SELECT_TABLE_COLUMNS);

            String querySelectString = query.first;
            selectionArgs = query.second;

            cursor = getReadableDatabase().query(DISPENSABLE_TABLE, DISPENSABLE_TABLE_COLUMNS, querySelectString, selectionArgs, null, null, null);
            List<Dispensable> dispensables = readDispensables(cursor);
            if (dispensables.size() > 0) {
                dispensable = dispensables.get(0);
            }
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return dispensable;
    }

    private List<Dispensable> readDispensables(Cursor cursor) {

        List<Dispensable> dispensables = new ArrayList<>();
        try {
            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    dispensables.add(createDispensable(cursor));
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

    private Dispensable createDispensable(Cursor cursor) {

        return new Dispensable(
                cursor.getString(cursor.getColumnIndex(ID)),
                cursor.getString(cursor.getColumnIndex(DISPENSING_UNIT)),
                cursor.getString(cursor.getColumnIndex(SIZE_CODE)),
                cursor.getString(cursor.getColumnIndex(ROUTE_OF_ADMINISTRATION))
        );
    }

    private Object[] createQueryValues(Dispensable dispensable) {

        Object[] values = new Object[]{
                dispensable.getId().toString(),
                dispensable.getKeyDispensingUnit(),
                dispensable.getKeySizeCode(),
                dispensable.getKeyRouteOfAdministration(),
                dispensable.getDateUpdated()
        };
        return values;
    }
}
