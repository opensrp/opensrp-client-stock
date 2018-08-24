package org.smartregister.stock.openlmis.repository.openlmis;

import android.util.Log;
import android.util.Pair;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.repository.BaseRepository;
import org.smartregister.repository.Repository;
import org.smartregister.stock.openlmis.domain.openlmis.TradeItem;
import org.smartregister.stock.openlmis.domain.openlmis.TradeItemClassification;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.smartregister.stock.openlmis.util.Utils.INSERT_OR_REPLACE;
import static org.smartregister.stock.openlmis.util.Utils.createQuery;

public class TradeItemClassificationRepository extends BaseRepository {

    public static final String TAG = TradeItemClassificationRepository.class.getName();
    public static final String TRADE_ITEM_CLASSIFICATION_TABLE = "trade_item_classifications";
    public static final String ID = "id";
    public static final String TRADE_ITEM = "trade_item";
    public static final String CLASSIFICATION_SYSTEM = "classification_system";
    public static final String CLASSIFICATION_ID = "classification_id";
    public static final String DATE_UPDATED = "date_updated";
    public static final String[] TRADE_ITEM_CLASSIFICATION_TABLE_COLUMNS = {ID, TRADE_ITEM, CLASSIFICATION_SYSTEM, CLASSIFICATION_ID, DATE_UPDATED};
    public static final String[] SELECT_TABLE_COLUMNS = {ID, TRADE_ITEM, CLASSIFICATION_SYSTEM, CLASSIFICATION_ID};

    public static final String CREATE_TRADE_ITEM_CLASSIFICATION_TABLE =

            "CREATE TABLE " + TRADE_ITEM_CLASSIFICATION_TABLE
            + "("
                    + ID + " VARCHAR NOT NULL PRIMARY KEY,"
                    + TRADE_ITEM + " VARCHAR NOT NULL,"
                    + CLASSIFICATION_SYSTEM + " VARCHAR,"
                    + CLASSIFICATION_ID + " VARCHAR,"
                    + DATE_UPDATED + " INTEGER"
            + ")";

    public TradeItemClassificationRepository(Repository repository) { super(repository); }

    public static void createTable(SQLiteDatabase database) {
        database.execSQL(CREATE_TRADE_ITEM_CLASSIFICATION_TABLE);
    }

    public void addOrUpdate(TradeItemClassification tradeItemClassification) {

        if (tradeItemClassification == null) {
            return;
        }

        if (tradeItemClassification.getDateUpdated() == null) {
            tradeItemClassification.setDateUpdated(Calendar.getInstance().getTimeInMillis());
        }

        try {
            SQLiteDatabase database = getWritableDatabase();

            String query = String.format(INSERT_OR_REPLACE, TRADE_ITEM_CLASSIFICATION_TABLE);
            query += "(" + StringUtils.repeat("?", ",", TRADE_ITEM_CLASSIFICATION_TABLE_COLUMNS.length) + ")";
            database.execSQL(query, createQueryValues(tradeItemClassification));
            database.close();
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    public List<TradeItemClassification> findTradeItemClassifications(String id, String tradeItem, String classificationSystem, String classificationId) {

        List<TradeItemClassification> tradeItemClassifications = new ArrayList<>();
        Cursor cursor = null;
        try {
            String[] selectionArgs = new String[]{id, tradeItem, classificationSystem, classificationId};
            Pair<String, String[]> query= createQuery(selectionArgs, SELECT_TABLE_COLUMNS);

            String querySelectString = query.first;
            selectionArgs = query.second;

            cursor = getReadableDatabase().query(TRADE_ITEM_CLASSIFICATION_TABLE, TRADE_ITEM_CLASSIFICATION_TABLE_COLUMNS, querySelectString, selectionArgs, null, null, null);
            tradeItemClassifications = readTradeItemClassifications(cursor);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return tradeItemClassifications;
    }

    private List<TradeItemClassification> readTradeItemClassifications(Cursor cursor) {

        List<TradeItemClassification> tradeItemClassifications = new ArrayList<>();
        try {
            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    tradeItemClassifications.add(createTradeItemClassification(cursor));
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
        return tradeItemClassifications;
    }

    private TradeItemClassification createTradeItemClassification(Cursor cursor) {

        return new TradeItemClassification(
                cursor.getString(cursor.getColumnIndex(ID)),
                new TradeItem(cursor.getString(cursor.getColumnIndex(TRADE_ITEM))),
                cursor.getString(cursor.getColumnIndex(CLASSIFICATION_SYSTEM)),
                cursor.getString(cursor.getColumnIndex(CLASSIFICATION_ID)),
                cursor.getLong(cursor.getColumnIndex(DATE_UPDATED))
        );
    }
    
    private Object[] createQueryValues(TradeItemClassification tradeItemClassification) {

        Object[] values = new Object[] {
            tradeItemClassification.getId().toString(),
            tradeItemClassification.getTradeItem().getId().toString(),
            tradeItemClassification.getClassificationSystem(),
            tradeItemClassification.getClassificationId(),
            tradeItemClassification.getDateUpdated()
        };
        return values;
    }
}
