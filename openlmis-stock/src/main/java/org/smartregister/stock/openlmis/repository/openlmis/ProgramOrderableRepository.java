package org.smartregister.stock.openlmis.repository.openlmis;

import android.util.Log;
import android.util.Pair;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.repository.BaseRepository;
import org.smartregister.repository.Repository;
import org.smartregister.stock.openlmis.domain.openlmis.ProgramOrderable;
import org.smartregister.stock.openlmis.repository.TradeItemRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.smartregister.stock.openlmis.repository.TradeItemRepository.COMMODITY_TYPE_ID;
import static org.smartregister.stock.openlmis.repository.TradeItemRepository.TRADE_ITEM_TABLE;
import static org.smartregister.stock.openlmis.repository.openlmis.OrderableRepository.ORDERABLE_TABLE;
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

    private static final String CREATE_PROGRAM_INDEX = "CREATE INDEX "
            + PROGRAM_ORDERABLE_TABLE + PROGRAM + "_INDEX ON "
            + PROGRAM_ORDERABLE_TABLE + "(" + PROGRAM + ")";

    public ProgramOrderableRepository(Repository repository) {
        super();
    }

    public static void createTable(SQLiteDatabase database) {
        database.execSQL(CREATE_PROGRAM_ORDERABLE_TABLE);
        database.execSQL(CREATE_PROGRAM_INDEX);
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
            Pair<String, String[]> query = createQuery(selectionArgs, SELECT_TABLE_COLUMNS);

            String querySelectString = query.first;
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

    public Map<String, Set<String>> searchIdsByPrograms(String programId) {
        Cursor cursor = null;
        Map<String, Set<String>> ids = new HashMap<>();
        String query = String.format("SELECT IFNULL(t.%s,o.%s),t.%s FROM %s p " +
                        " JOIN %s o on p.%s = o.%s" +
                        " LEFT JOIN %s t on t.%s=o.%s or t.%s=o.%s WHERE p.%s =? ",
                COMMODITY_TYPE_ID,OrderableRepository.COMMODITY_TYPE_ID, TradeItemRepository.ID,
                PROGRAM_ORDERABLE_TABLE,ORDERABLE_TABLE, ORDERABLE, OrderableRepository.ID,
                TRADE_ITEM_TABLE, COMMODITY_TYPE_ID, OrderableRepository.COMMODITY_TYPE_ID,
                TradeItemRepository.ID, OrderableRepository.TRADE_ITEM_ID, PROGRAM);
        try {
            cursor = getReadableDatabase().rawQuery(query, new String[]{programId});
            while (cursor.moveToNext()) {
                String commodityType = cursor.getString(0);
                String tradeItem = cursor.getString(1);
                if (ids.containsKey(commodityType)) {
                    ids.get(commodityType).add(tradeItem);

                } else {
                    Set<String> tradeItems = new HashSet<>();
                    tradeItems.add(tradeItem);
                    ids.put(commodityType, tradeItems);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return ids;
    }
}
