package org.smartregister.stock.management.repository;

import android.util.Log;
import android.util.Pair;

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
import static org.smartregister.stock.management.util.Utils.convertBooleanToInt;
import static org.smartregister.stock.management.util.Utils.convertIntToBoolean;
import static org.smartregister.stock.management.util.Utils.createQuery;

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
    public static final String[] SELECT_TABLE_COLUMNS = {ID, CODE, NAME, ACTIVE};

    public static final String CREATE_PROGRAM_TABLE =

            "CREATE TABLE " + PROGRAM_TABLE
            + "("
                + ID + " VARCHAR NOT NULL PRIMARY KEY,"
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

            String query = String.format(INSERT_OR_REPLACE, PROGRAM_TABLE);
            query += "(" + createQueryValues(program) + ")";
            database.execSQL(query);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    public List<Program> findPrograms(String id, String code, String name, String active) {

        List<Program> programs = new ArrayList<>();
        Cursor cursor = null;
        try {
            String[] selectionArgs = new String[]{id, code, name, active};
            Pair<String, String[]> query= createQuery(selectionArgs, SELECT_TABLE_COLUMNS);

            String querySelectString =  query.first;
            selectionArgs = query.second;

            cursor = getReadableDatabase().query(PROGRAM_TABLE, PROGRAM_TABLE_COLUMNS, querySelectString, selectionArgs, null, null, null);
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
                    programs.add(createProgram(cursor));
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
        return programs;
    }

    private Program createProgram(Cursor cursor) {

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

    private String createQueryValues(Program program) {

        String values = "";
        values += "'" + program.getId().toString() + "'" + ",";
        values += "'" + program.getCode().toString() + "'"  + ",";
        values += "'" + program.getName() + "'"  + ",";
        values += "'" + program.getDescription() + "'"  + ",";
        values += convertBooleanToInt(program.getActive()) + ",";
        values += convertBooleanToInt(program.getPeriodsSkippable()) + ",";
        values += convertBooleanToInt(program.getSkipAuthorization()) + ",";
        values += convertBooleanToInt(program.getShowNonFullSupplyTab()) + ",";
        values += convertBooleanToInt(program.getEnableDatePhysicalStockCountCompleted()) + ",";
        values += program.getDateUpdated();

        return values;
    }
}
