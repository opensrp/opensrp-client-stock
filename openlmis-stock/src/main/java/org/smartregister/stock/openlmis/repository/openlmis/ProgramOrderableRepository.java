package org.smartregister.stock.openlmis.repository.openlmis;

import android.util.Log;
import android.util.Pair;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.repository.BaseRepository;
import org.smartregister.repository.Repository;
import org.smartregister.stock.openlmis.domain.openlmis.Orderable;
import org.smartregister.stock.openlmis.domain.openlmis.Program;
import org.smartregister.stock.openlmis.domain.openlmis.ProgramOrderable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.smartregister.stock.openlmis.util.Utils.INSERT_OR_REPLACE;
import static org.smartregister.stock.openlmis.util.Utils.convertBooleanToInt;
import static org.smartregister.stock.openlmis.util.Utils.convertIntToBoolean;
import static org.smartregister.stock.openlmis.util.Utils.createQuery;

public class ProgramOrderableRepository extends BaseRepository {

    public static final String TAG = ProgramOrderableRepository.class.getName();
    public static final String PROGRAM_ORDERABLE_TABLE = "program_orderables";
    public static final String ID = "id";
    public static final String PROGRAM = "program";
    public static final String ORDERABLE = "orderable";
    public static final String DOSES_PER_PATIENT = "doses_per_patient";
    public static final String ACTIVE = "active";
    public static final String FULL_SUPPLY = "full_supply";
    public static final String DATE_UPDATED = "date_updated";
    public static final String[] PROGRAM_ORDERABLE_TABLE_COLUMNS = {ID, PROGRAM, ORDERABLE, DOSES_PER_PATIENT, ACTIVE, FULL_SUPPLY, DATE_UPDATED};
    public static final String[] SELECT_TABLE_COLUMNS = {ID, PROGRAM, ORDERABLE, DOSES_PER_PATIENT, ACTIVE, FULL_SUPPLY};

    public static final String CREATE_PROGRAM_ORDERABLE_TABLE =

            "CREATE TABLE " + PROGRAM_ORDERABLE_TABLE
            + "("
                    + ID + " VARCHAR NOT NULL PRIMARY KEY,"
                    + PROGRAM + " VARCHAR NOT NULL,"
                    + ORDERABLE + " VARCHAR NOT NULL,"
                    + DOSES_PER_PATIENT + " INTEGER,"
                    + ACTIVE + " TINYINT,"
                    + FULL_SUPPLY + " TINYINT,"
                    + DATE_UPDATED + " INTEGER"
            + ")";

    public ProgramOrderableRepository(Repository repository) { super(repository); }

    public static void createTable(SQLiteDatabase database) {
        database.execSQL(CREATE_PROGRAM_ORDERABLE_TABLE);
    }

    public void addOrUpdate(ProgramOrderable program) {

        if (program == null) {
            return;
        }

        if (program.getDateUpdated() == null) {
            program.setDateUpdated(Calendar.getInstance().getTimeInMillis());
        }

        try {
            SQLiteDatabase database = getWritableDatabase();

            String query = String.format(INSERT_OR_REPLACE, PROGRAM_ORDERABLE_TABLE);
            query += "(" + StringUtils.repeat("?", ",", PROGRAM_ORDERABLE_TABLE_COLUMNS.length) + ")";
            database.execSQL(query, createQueryValues(program));
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    public List<ProgramOrderable> findProgramOrderables(String id, String program, String orderable, String dosesPerPatient, String active, String fullSupply) {

        List<ProgramOrderable> programs = new ArrayList<>();
        Cursor cursor = null;
        try {
            String[] selectionArgs = new String[]{id, program, orderable, dosesPerPatient, active, fullSupply};
            Pair<String, String[]> query= createQuery(selectionArgs, SELECT_TABLE_COLUMNS);

            String querySelectString =  query.first;
            selectionArgs = query.second;

            cursor = getReadableDatabase().query(PROGRAM_ORDERABLE_TABLE, PROGRAM_ORDERABLE_TABLE_COLUMNS, querySelectString, selectionArgs, null, null, null);
            programs = readProgramOrderables(cursor);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return programs;
    }

    private List<ProgramOrderable> readProgramOrderables(Cursor cursor) {

        List<ProgramOrderable> programsOrderables = new ArrayList<>();
        try {
            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    programsOrderables.add(createProgramOrderable(cursor));
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
        return programsOrderables;
    }

    private ProgramOrderable createProgramOrderable(Cursor cursor) {

        return new ProgramOrderable(
                cursor.getString(cursor.getColumnIndex(ID)),
                cursor.getString(cursor.getColumnIndex(PROGRAM)),
                cursor.getString(cursor.getColumnIndex(ORDERABLE)),
                cursor.getInt(cursor.getColumnIndex(DOSES_PER_PATIENT)),
                convertIntToBoolean(cursor.getInt(cursor.getColumnIndex(ACTIVE))),
                convertIntToBoolean(cursor.getInt(cursor.getColumnIndex(FULL_SUPPLY))),
                cursor.getLong(cursor.getColumnIndex(DATE_UPDATED))
        );
    }

    private Object[] createQueryValues(ProgramOrderable programOrderable) {

        Object[] values = new Object[] {
            programOrderable.getId(),
            programOrderable.getProgramId(),
            programOrderable.getOrderableId(),
            programOrderable.getDosesPerPatient(),
            convertBooleanToInt(programOrderable.isActive()),
            convertBooleanToInt(programOrderable.isFullSupply()),
            programOrderable.getDateUpdated()
        };
        return values;
    }
}
