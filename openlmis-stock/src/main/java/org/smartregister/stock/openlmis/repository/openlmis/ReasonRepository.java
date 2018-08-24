package org.smartregister.stock.openlmis.repository.openlmis;

import android.util.Log;
import android.util.Pair;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.repository.BaseRepository;
import org.smartregister.repository.Repository;
import org.smartregister.stock.openlmis.domain.openlmis.Program;
import org.smartregister.stock.openlmis.domain.openlmis.Reason;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.smartregister.stock.openlmis.util.Utils.INSERT_OR_REPLACE;
import static org.smartregister.stock.openlmis.util.Utils.convertIntToBoolean;
import static org.smartregister.stock.openlmis.util.Utils.createQuery;

public class ReasonRepository extends BaseRepository {

    public static final String TAG = BaseRepository.class.getName();
    public static final String REASON_TABLE = "reasons";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String PROGRAM_ID = "program_id";
    public static final String DESCRIPTION = "description";
    public static final String ADDITIVE = "additive";
    public static final String DATE_UPDATED = "date_updated";

    public static final String[] REASON_TABLE_COLUMNS = {ID, NAME, PROGRAM_ID, DESCRIPTION, ADDITIVE, DATE_UPDATED};
    public static final String[] SELECT_TABLE_COLUMNS = {ID, NAME, PROGRAM_ID};

    public static final String CREATE_ORDERABLE_TABLE =

            "CREATE TABLE " + REASON_TABLE
                    + "("
                    + ID + " VARCHAR NOT NULL PRIMARY KEY,"
                    + NAME + " VARCHAR NOT NULL,"
                    + PROGRAM_ID + " VARCHAR NOT NULL,"
                    + DESCRIPTION + " VARCHAR NOT NULL,"
                    + ADDITIVE + " TINYINT,"
                    + DATE_UPDATED + " INTEGER"
                    + ")";

    public ReasonRepository(Repository repository) { super(repository); }

    public static void createTable(SQLiteDatabase database) {
        database.execSQL(CREATE_ORDERABLE_TABLE);
    }

    public void addOrUpdate(Reason reason) {

        if (reason == null) {
            return;
        }

        if (reason.getDateUpdated() == null) {
            reason.setDateUpdated(Calendar.getInstance().getTimeInMillis());
        }

        try {
            SQLiteDatabase database = getWritableDatabase();

            String query = String.format(INSERT_OR_REPLACE, REASON_TABLE);
            query += "(" + StringUtils.repeat("?", ",", REASON_TABLE_COLUMNS.length) + ")";
            database.execSQL(query, createQueryValues(reason));
            database.close();
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    public List<Reason> findReasons(String id, String name, String programId) {

        List<Reason> reasons = new ArrayList<>();
        Cursor cursor = null;
        try {
            String[] selectionArgs = new String[]{id, name, programId};
            Pair<String, String[]> query= createQuery(selectionArgs, SELECT_TABLE_COLUMNS);

            String querySelectString =  query.first;
            selectionArgs = query.second;

            cursor = getReadableDatabase().query(REASON_TABLE, REASON_TABLE_COLUMNS, querySelectString, selectionArgs, null, null, null);
            reasons = readReasons(cursor);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return reasons;
    }

    private List<Reason> readReasons(Cursor cursor) {

        List<Reason> reasons = new ArrayList<>();
        try {
            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    reasons.add(createReason(cursor));
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
        return reasons;
    }

    private Reason createReason(Cursor cursor) {

        return new Reason(
                cursor.getString(cursor.getColumnIndex(ID)),
                cursor.getString(cursor.getColumnIndex(NAME)),
                new Program(cursor.getString(cursor.getColumnIndex(PROGRAM_ID))),
                cursor.getString(cursor.getColumnIndex(DESCRIPTION)),
                convertIntToBoolean(cursor.getInt(cursor.getColumnIndex(ADDITIVE)))
        );
    }

    private Object[] createQueryValues(Reason reason) {

        Object[] values = new Object[]{
                reason.getId(),
                reason.getName(),
                reason.getProgram().getId(),
                reason.getDescription(),
                reason.getAdditive(),
                reason.getDateUpdated()
        };
        return values;
    }
}
