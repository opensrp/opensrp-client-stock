package org.smartregister.stock.management;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import org.smartregister.repository.BaseRepository;
import org.smartregister.repository.Repository;
import org.smartregister.stock.management.domain.Program;

public class ProgramRepository extends BaseRepository {

    public static final String TAG = ProgramRepository.class.getName();
    public static final String PROGRAM_TABLE_NAME = "programs";
    public static final String ID = "id";
    public static final String CODE = "code";
    public static final String DESCRIPTION = "description";
    public static final String ACTIVE = "active";
    public static final String PERIODS_SKIPPABLE = "periods_skippable";
    public static final String SKIP_AUTHORIZATION = "skip_authorization";
    public static final String SHOW_NON_FULL_SUPPLY_TAB = "show_non_full_supply_tab";
    public static final String ENABLE_DATE_PHYSICAL_STOCK_COUNT_COMPLETED = "enable_date_physical_stock_count_completed";
    public static final String[] PROGRAM_TABLE_COLUMNS = {ID, CODE, DESCRIPTION, ACTIVE, PERIODS_SKIPPABLE,
            SKIP_AUTHORIZATION, SHOW_NON_FULL_SUPPLY_TAB, ENABLE_DATE_PHYSICAL_STOCK_COUNT_COMPLETED};

    public static final String CREATE_PROGRAM_TABLE =

            "CREATE TABLE " + PROGRAM_TABLE_NAME
            + "("
                + ID + " VARCHAR NOT NULL,"
                + CODE + " VARCHAR NOT NULL,"
                + DESCRIPTION + " VARCHAR,"
                + ACTIVE + " TINYINT,"
                + PERIODS_SKIPPABLE + " TINYINT,"
                + SKIP_AUTHORIZATION + " TINYINT,"
                + SHOW_NON_FULL_SUPPLY_TAB + " TINYINT,"
                + ENABLE_DATE_PHYSICAL_STOCK_COUNT_COMPLETED + " TINYINT"
            + ")";

    public ProgramRepository(Repository repository) {super(repository);}

    public static void createTable(SQLiteDatabase database) {
        database.execSQL(CREATE_PROGRAM_TABLE);
    }

    private ContentValues createContentValues(Program program) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, program.getId().toString());
        contentValues.put(CODE, program.getCode().toString());
        contentValues.put(DESCRIPTION, program.getDescription());
        contentValues.put(ACTIVE, program.getActive());
        contentValues.put(PERIODS_SKIPPABLE, program.getPeriodsSkippable());
        contentValues.put(SKIP_AUTHORIZATION, program.getSkipAuthorization());
        contentValues.put(SHOW_NON_FULL_SUPPLY_TAB, program.getShowNonFullSupplyTab());
        contentValues.put(ENABLE_DATE_PHYSICAL_STOCK_COUNT_COMPLETED, program.getEnableDatePhysicalStockCountCompleted());

        return contentValues;
    }
}
