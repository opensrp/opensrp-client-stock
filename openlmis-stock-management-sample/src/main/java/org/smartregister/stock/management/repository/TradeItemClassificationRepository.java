package org.smartregister.stock.management.repository;

import android.util.Log;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.smartregister.repository.BaseRepository;
import org.smartregister.repository.Repository;
import org.smartregister.stock.management.domain.TradeItem;
import org.smartregister.stock.management.domain.TradeItemClassification;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import static org.smartregister.stock.management.util.Utils.INSERT_OR_REPLACE;

public class TradeItemClassificationRepository extends BaseRepository {

    public static final String TAG = TradeItemClassificationRepository.class.getName();
    public static final String TRADE_ITEM_CLASSIFICATION_TABLE = "trade_item_classificaton_table";
    public static final String ID = "id";
    public static final String TRADE_ITEM = "trade_item";
    public static final String CLASSIFICATION_SYSTEM = "classification_system";
    public static final String CLASSIFICATION_ID = "classification_id";
    public static final String DATE_UPDATED = "date_updated";
    public static final String[] TRADE_ITEM_CLASSIFICATION_TABLE_COLUMNS = {ID, TRADE_ITEM, CLASSIFICATION_SYSTEM, CLASSIFICATION_ID};

    public static final String CREATE_TRADE_ITEM_CLASSIFICATION_TABLE =

            "CREATE TABLE " + TRADE_ITEM_CLASSIFICATION_TABLE
            + "("
                    + ID + " VARCHAR NOT NULL,"
                    + TRADE_ITEM + " VARCHAR NOT NULL,"
                    + CLASSIFICATION_SYSTEM + " VARCHAR,"
                    + CLASSIFICATION_ID + " VARCHAR,"
                    + DATE_UPDATED + " VARCHAR"
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
            query += "(" + formatTableValues(tradeItemClassification) + ")";
            database.execSQL(query);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    public List<TradeItemClassification> findTradeItemClassifications(String id, String tradeItem, String classificationSystem, String classificationId) {

        List<TradeItemClassification> tradeItemClassifications = new ArrayList<>();
        Cursor cursor = null;
        try {
            String query = ID + "=?" + " AND " + TRADE_ITEM + "=?" + " AND " + CLASSIFICATION_SYSTEM + "=?" + " AND " + CLASSIFICATION_ID + "=?";
            String[] selectionArgs = new String[]{id, tradeItem, classificationSystem, classificationId};
            cursor = getReadableDatabase().query(TRADE_ITEM_CLASSIFICATION_TABLE, TRADE_ITEM_CLASSIFICATION_TABLE_COLUMNS, query, selectionArgs, null, null, null);
            tradeItemClassifications = readTradeItemClassificationsFromCursor(cursor);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return tradeItemClassifications;
    }

    private List<TradeItemClassification> readTradeItemClassificationsFromCursor(Cursor cursor) {

        List<TradeItemClassification> tradeItemClassifications = new ArrayList<>();
        try {
            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    tradeItemClassifications.add(createTradeItemClassificationFromCursor(cursor));
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

    private TradeItemClassification createTradeItemClassificationFromCursor(Cursor cursor) {

        return new TradeItemClassification(
                UUID.fromString(cursor.getString(cursor.getColumnIndex(ID))),
                new TradeItem(UUID.fromString(cursor.getString(cursor.getColumnIndex(TRADE_ITEM)))),
                cursor.getString(cursor.getColumnIndex(CLASSIFICATION_SYSTEM)),
                cursor.getString(cursor.getColumnIndex(CLASSIFICATION_ID)),
                cursor.getLong(cursor.getColumnIndex(DATE_UPDATED))
        );
    }
    
    private String formatTableValues(TradeItemClassification tradeItemClassification) {

        String values = "";
        values += ID + tradeItemClassification.getId().toString() + ",";
        values += TRADE_ITEM + tradeItemClassification.getTradeItem().getId().toString() + ",";
        values += CLASSIFICATION_SYSTEM + tradeItemClassification.getClassificationSystem() + ",";
        values += CLASSIFICATION_ID + tradeItemClassification.getClassificationId() + ",";
        values += DATE_UPDATED + tradeItemClassification.getDateUpdated();

        return values;
    }
}
