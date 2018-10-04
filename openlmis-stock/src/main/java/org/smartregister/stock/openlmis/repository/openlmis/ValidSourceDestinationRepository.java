package org.smartregister.stock.openlmis.repository.openlmis;

import android.util.Log;
import android.util.Pair;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.repository.BaseRepository;
import org.smartregister.repository.Repository;
import org.smartregister.stock.openlmis.domain.openlmis.ValidSourceDestination;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.smartregister.stock.openlmis.util.Utils.INSERT_OR_REPLACE;
import static org.smartregister.stock.openlmis.util.Utils.convertBooleanToInt;
import static org.smartregister.stock.openlmis.util.Utils.convertIntToBoolean;
import static org.smartregister.stock.openlmis.util.Utils.createQuery;

public class ValidSourceDestinationRepository extends BaseRepository {

    public static final String TAG = ValidSourceDestinationRepository.class.getName();
    public static final String ID = "id";
    public static final String PROGRAM_UUID = "program_uuid";
    public static final String FACILITY_TYPE_UUID = "facility_type_uuid";
    public static final String FACILITY_NAME = "facility_name";
    public static final String OPENLMIS_UUID = "openlmis_uuid";
    public static final String IS_SOURCE = "is_source";
    public static final String DATE_UPDATED = "date_updated";

    public static final String VALID_SOURCE_DESTINATION_TABLE = "valid_sources_and_destinations";
    public static final String[] VALID_SOURCE_DESTINATION_TABLE_COLUMNS = {ID, PROGRAM_UUID, FACILITY_TYPE_UUID, FACILITY_NAME, OPENLMIS_UUID, IS_SOURCE, DATE_UPDATED};
    public static final String[] SELECT_TABLE_COLUMNS = {ID, PROGRAM_UUID, FACILITY_TYPE_UUID, FACILITY_NAME, OPENLMIS_UUID, IS_SOURCE};

    public static final String CREATE_VALID_SOURCE_DESTINATION_TABLE =

            "CREATE TABLE " +  VALID_SOURCE_DESTINATION_TABLE
                + "("
                    +  ID + " VARCHAR NOT NULL PRIMARY KEY,"
                    +  PROGRAM_UUID + " VARCHAR NOT NULL,"
                    +  FACILITY_TYPE_UUID + " VARCHAR NOT NULL,"
                    +  FACILITY_NAME + " VARCHAR NOT NULL,"
                    +  OPENLMIS_UUID + " VARCHAR NOT NULL,"
                    + IS_SOURCE + " INTEGER NOT NULL,"
                    +  DATE_UPDATED + " INTEGER" +
                ")";

    public ValidSourceDestinationRepository(Repository repository) {
        super(repository);
    }

    public static void createTable(SQLiteDatabase database) {
        database.execSQL(CREATE_VALID_SOURCE_DESTINATION_TABLE);
    }

    public void addOrUpdate(ValidSourceDestination validSourceDestination) {

        if (validSourceDestination == null) {
            return;
        }

        if (validSourceDestination.getDateUpdated() == null) {
            validSourceDestination.setDateUpdated(Calendar.getInstance().getTimeInMillis());
        }

        try {
            SQLiteDatabase database = getWritableDatabase();

            String query = String.format(INSERT_OR_REPLACE, VALID_SOURCE_DESTINATION_TABLE);
            query += "(" + StringUtils.repeat("?", ",", VALID_SOURCE_DESTINATION_TABLE_COLUMNS.length) + ")";
            database.execSQL(query, createQueryValues(validSourceDestination));
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    public List<ValidSourceDestination> findValidSourceDestinations(String id, String programUuid, String facilityTypeUuid, String facilityName, String openlmisUuid, String isSource) {

        List<ValidSourceDestination> validSourceDestinations = new ArrayList<>();
        Cursor cursor = null;
        try {

            String[] selectionArgs = new String[]{id, programUuid, facilityTypeUuid, facilityName, openlmisUuid, isSource};
            Pair<String, String[]> query= createQuery(selectionArgs, SELECT_TABLE_COLUMNS);

            String querySelectString = query.first;
            selectionArgs = query.second;

            cursor = getReadableDatabase().query(VALID_SOURCE_DESTINATION_TABLE, VALID_SOURCE_DESTINATION_TABLE_COLUMNS, querySelectString, selectionArgs, null, null, null);
            validSourceDestinations = readValidSourceDestinations(cursor);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return validSourceDestinations;
    }

    public ValidSourceDestination findValidSourceDestination(String id) {

        ValidSourceDestination validSourceDestination = null;
        Cursor cursor = null;
        try {
            String[] selectionArgs = new String[]{id};
            Pair<String, String[]> query= createQuery(selectionArgs, SELECT_TABLE_COLUMNS);

            String querySelectString =  query.first;
            selectionArgs = query.second;

            cursor = getReadableDatabase().query(VALID_SOURCE_DESTINATION_TABLE, VALID_SOURCE_DESTINATION_TABLE_COLUMNS, querySelectString, selectionArgs, null, null, null);
            List<ValidSourceDestination> validSourceDestinations = readValidSourceDestinations(cursor);
            if (validSourceDestinations.size() > 0) {
                validSourceDestination = validSourceDestinations.get(0);
            }
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return validSourceDestination;
    }

    public List<ValidSourceDestination> findAllValidSourceDestinations() {

        List<ValidSourceDestination> validSourceDestinations = new ArrayList<>();
        Cursor cursor = null;
        try {

            String query = "SELECT * FROM " + VALID_SOURCE_DESTINATION_TABLE;
            cursor = getReadableDatabase().rawQuery(query, null);
            validSourceDestinations = readValidSourceDestinations(cursor);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return validSourceDestinations;
    }

    private List<ValidSourceDestination> readValidSourceDestinations(Cursor cursor) {

        List<ValidSourceDestination> validSourceDestinations = new ArrayList<>();
        try {
            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    validSourceDestinations.add(createValidSourceDestination(cursor));
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
        return validSourceDestinations;
    }

    private ValidSourceDestination createValidSourceDestination(Cursor cursor) {

        return new ValidSourceDestination(
                cursor.getInt(cursor.getColumnIndex(ID)),
                cursor.getString(cursor.getColumnIndex(PROGRAM_UUID)),
                cursor.getString(cursor.getColumnIndex(FACILITY_TYPE_UUID)),
                cursor.getString(cursor.getColumnIndex(FACILITY_NAME)),
                cursor.getString(cursor.getColumnIndex(OPENLMIS_UUID)),
                convertIntToBoolean(cursor.getInt(cursor.getColumnIndex(IS_SOURCE)))
        );
    }

    private Object[] createQueryValues(ValidSourceDestination validSourceDestination) {

        Object[] values = new Object[]{
                validSourceDestination.getId().toString(),
                validSourceDestination.getProgramUuid(),
                validSourceDestination.getFacilityTypeUuid(),
                validSourceDestination.getFacilityName(),
                validSourceDestination.getOpenlmisUuid(),
                convertBooleanToInt(validSourceDestination.isSource()),
                validSourceDestination.getDateUpdated()
        };
        return values;
    }
}
