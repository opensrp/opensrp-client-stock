package org.smartregister.stock.management.repository;

import android.util.Log;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.smartregister.repository.BaseRepository;
import org.smartregister.repository.Repository;
import org.smartregister.stock.management.domain.Orderable;
import org.smartregister.stock.management.domain.Program;
import org.smartregister.stock.management.domain.ProgramOrderable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import static org.smartregister.stock.management.util.Utils.INSERT_OR_REPLACE;
import static org.smartregister.stock.management.util.Utils.convertBooleanToInt;
import static org.smartregister.stock.management.util.Utils.convertIntToBoolean;

public class ProgramOrderableRepository extends BaseRepository {

    public static final String TAG = ProgramOrderableRepository.class.getName();
    public static final String PROGRAM_ORDERABLE_TABLE = "program_orderables";
    public static final String ID = "id";
    public static final String PROGRAM = "program";
    public static final String ORDERABLE = "orderable";
    public static final String DOSES_PER_PATIENT = "doses_per_patient";
    public static final String ACTIVE = "active";
    public static final String FULL_SUPPLY = "full_supply";
    public static final String[] PROGRAM_ORDERABLE_TABLE_COLUMNS = {ID, PROGRAM, ORDERABLE, DOSES_PER_PATIENT, ACTIVE, FULL_SUPPLY};

    public static final String CREATE_PROGRAM_ORDERABLE_TABLE =

            "CREATE TABLE " + PROGRAM_ORDERABLE_TABLE
            + "("
                    + ID + " VARCHAR NOT NULL,"
                    + PROGRAM + " VARCHAR NOT NULL,"
                    + ORDERABLE + " VARCHAR NOT NULL,"
                    + DOSES_PER_PATIENT + " INTEGER,"
                    + ACTIVE + " TINYINT,"
                    + FULL_SUPPLY + " TINYINT"
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
            query += "(" + formatTableValues(program) + ")";
            database.execSQL(query);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    public List<ProgramOrderable> findProgramOrderables(String id, String program, String orderable, String dosesPerPatient, String active, String fullSupply) {

        List<ProgramOrderable> programs = new ArrayList<>();
        Cursor cursor = null;
        try {
            String query = ID + "=?" + " AND " + PROGRAM + "=?" + " AND " + ORDERABLE + "=?" + " AND " + DOSES_PER_PATIENT + "=?" + " AND " +  ACTIVE + "=?" + " AND " + FULL_SUPPLY + "=?";
            String[] selectionArgs = new String[]{id, program, orderable, dosesPerPatient, active, fullSupply};
            cursor = getReadableDatabase().query(PROGRAM_ORDERABLE_TABLE, PROGRAM_ORDERABLE_TABLE_COLUMNS, query, selectionArgs, null, null, null);
            programs = readProgramOrderablesFromCursor(cursor);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return programs;
    }

    private List<ProgramOrderable> readProgramOrderablesFromCursor(Cursor cursor) {

        List<ProgramOrderable> programsOrderables = new ArrayList<>();
        try {
            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    programsOrderables.add(createProgramOrderableFromCursor(cursor));
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

    private ProgramOrderable createProgramOrderableFromCursor(Cursor cursor) {

        return new ProgramOrderable(
                UUID.fromString(cursor.getString(cursor.getColumnIndex(ID))),
                new Program(cursor.getString(cursor.getColumnIndex(PROGRAM))),
                new Orderable(UUID.fromString(cursor.getString(cursor.getColumnIndex(ORDERABLE)))),
                cursor.getInt(cursor.getColumnIndex(DOSES_PER_PATIENT)),
                convertIntToBoolean(cursor.getInt(cursor.getColumnIndex(ACTIVE))),
                convertIntToBoolean(cursor.getInt(cursor.getColumnIndex(FULL_SUPPLY)))
        );
    }

    private String formatTableValues(ProgramOrderable programOrderable) {

        String values = "";
        values += programOrderable.getId().toString() + ",";
        values += programOrderable.getProgram().getCode().toString() + ",";
        values += programOrderable.getOrderable().getId().toString() + ",";
        values += programOrderable.getDosesPerPatient() + ",";
        values += convertBooleanToInt(programOrderable.isActive()) + ",";
        values += convertBooleanToInt(programOrderable.isFullSupply()) + ",";
        values += programOrderable.getDateUpdated();

        return values;
    }
}
