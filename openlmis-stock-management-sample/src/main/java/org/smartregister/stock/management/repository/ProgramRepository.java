package org.smartregister.stock.management.repository;

import android.content.ContentValues;
import android.util.Log;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.smartregister.repository.BaseRepository;
import org.smartregister.repository.Repository;
import org.smartregister.stock.management.domain.Code;
import org.smartregister.stock.management.domain.Program;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import static org.smartregister.stock.management.util.Utils.INSERT_OR_REPLACE;

public class ProgramRepository extends BaseRepository {

    public static final String TAG = ProgramRepository.class.getName();
    public static final String PROGRAM_TABLE = "programs";
    public static final String ID = "id";
    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String ACTIVE = "active";
    public static final String PERIODS_SKIPPABLE = "periods_skippable";
    public static final String SKIP_AUTHORIZATION = "skip_authorization";
    public static final String SHOW_NON_FULL_SUPPLY_TAB = "show_non_full_supply_tab";
    public static final String ENABLE_DATE_PHYSICAL_STOCK_COUNT_COMPLETED = "enable_date_physical_stock_count_completed";
    public static final String DATE_UPDATED = "date_updated";
    public static final String[] PROGRAM_TABLE_COLUMNS = {ID, CODE, NAME, DESCRIPTION, ACTIVE, PERIODS_SKIPPABLE,
            SKIP_AUTHORIZATION, SHOW_NON_FULL_SUPPLY_TAB, ENABLE_DATE_PHYSICAL_STOCK_COUNT_COMPLETED, DATE_UPDATED};

    public static final String CREATE_PROGRAM_TABLE =

            "CREATE TABLE " + PROGRAM_TABLE
            + "("
                + ID + " VARCHAR NOT NULL,"
                + CODE + " VARCHAR NOT NULL,"
                + NAME + " VARCHAR NOT NULL,"
                + DESCRIPTION + " VARCHAR,"
                + ACTIVE + " TINYINT,"
                + PERIODS_SKIPPABLE + " TINYINT,"
                + SKIP_AUTHORIZATION + " TINYINT,"
                + SHOW_NON_FULL_SUPPLY_TAB + " TINYINT,"
                + ENABLE_DATE_PHYSICAL_STOCK_COUNT_COMPLETED + " TINYINT,"
                + DATE_UPDATED + " INTEGER"
            + ")";



    public ProgramRepository(Repository repository) { super(repository); }

    public static void createTable(SQLiteDatabase database) {
        database.execSQL(CREATE_PROGRAM_TABLE);
    }

    public void addOrUpdate(Program program) {

        if (program == null) {
            return;
        }

        if (program.getDateUpdated() == null) {
            program.setDateUpdated(Calendar.getInstance().getTimeInMillis());
        }

        try {
            SQLiteDatabase database = getWritableDatabase();

            String sql = String.format(INSERT_OR_REPLACE, PROGRAM_TABLE);
            sql += "(" + formatTableValues(program) + ")";
            database.execSQL(sql);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    private String formatTableValues(Program program) {

        String values = "";
        values += program.getId().toString() + ",";
        values += program.getCode().toString() + ",";
        values += program.getName() + ",";
        values += program.getDescription() + ",";
        values += program.getActive() + ",";
        values += program.getPeriodsSkippable() + ",";
        values += program.getSkipAuthorization() + ",";
        values += program.getShowNonFullSupplyTab() + ",";
        values += program.getEnableDatePhysicalStockCountCompleted();

        return values;
    }

    private List<Program> findStock(String id, String code, String name, String active) {

        List<Program> programs = new ArrayList<>();
        Cursor cursor = null;
        try {
            String query = ID + "=?" + " AND " + CODE  + "=?" + " AND " + NAME + "=?" + " AND " + ACTIVE + "=?";
            String[] values = new String[]{id, code, name, active};
            cursor = getReadableDatabase().query(PROGRAM_TABLE, PROGRAM_TABLE_COLUMNS, query, values, null, null, null);
            programs = readPrograms(cursor);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return programs;
    }

    private List<Program> readPrograms(Cursor cursor) {

        List<Program> programs = new ArrayList<>();

        try {
            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {

                }
            }
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return programs;
    }

    private Program createProgramFromCursor(Cursor cursor) {

        return new Program(
                UUID.fromString(cursor.getString(cursor.getColumnIndex(ID))),
                new Code(cursor.getString(cursor.getColumnIndex(CODE))),
                cursor.getString(cursor.getColumnIndex(NAME)),
                cursor.getString(cursor.getColumnIndex(DESCRIPTION)),
                convertIntToBoolean(cursor.getInt(cursor.getColumnIndex(ACTIVE))),
                convertIntToBoolean(cursor.getInt(cursor.getColumnIndex(PERIODS_SKIPPABLE))),
                convertIntToBoolean(cursor.getInt(cursor.getColumnIndex(SKIP_AUTHORIZATION))),
                convertIntToBoolean(cursor.getInt(cursor.getColumnIndex(SHOW_NON_FULL_SUPPLY_TAB))),
                convertIntToBoolean(cursor.getInt(cursor.getColumnIndex(ENABLE_DATE_PHYSICAL_STOCK_COUNT_COMPLETED))),
                cursor.getLong(cursor.getColumnIndex(DATE_UPDATED))
        );
    }

    private Boolean convertIntToBoolean(int i) {
        return i > 0;
    }

    private ContentValues createContentValues(Program program) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, program.getId().toString());
        contentValues.put(CODE, program.getCode().toString());
        contentValues.put(NAME, program.getName());
        contentValues.put(DESCRIPTION, program.getDescription());
        contentValues.put(ACTIVE, program.getActive());
        contentValues.put(PERIODS_SKIPPABLE, program.getPeriodsSkippable());
        contentValues.put(SKIP_AUTHORIZATION, program.getSkipAuthorization());
        contentValues.put(SHOW_NON_FULL_SUPPLY_TAB, program.getShowNonFullSupplyTab());
        contentValues.put(ENABLE_DATE_PHYSICAL_STOCK_COUNT_COMPLETED, program.getEnableDatePhysicalStockCountCompleted());

        return contentValues;
    }
}
