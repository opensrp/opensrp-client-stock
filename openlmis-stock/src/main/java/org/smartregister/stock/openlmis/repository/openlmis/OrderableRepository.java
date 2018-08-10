package org.smartregister.stock.openlmis.repository.openlmis;

import android.util.Log;
import android.util.Pair;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.repository.BaseRepository;
import org.smartregister.repository.Repository;
import org.smartregister.stock.openlmis.domain.openlmis.Code;
import org.smartregister.stock.openlmis.domain.openlmis.Dispensable;
import org.smartregister.stock.openlmis.domain.openlmis.Orderable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.smartregister.stock.openlmis.util.Utils.INSERT_OR_REPLACE;
import static org.smartregister.stock.openlmis.util.Utils.convertBooleanToInt;
import static org.smartregister.stock.openlmis.util.Utils.convertIntToBoolean;
import static org.smartregister.stock.openlmis.util.Utils.createQuery;


public class OrderableRepository extends BaseRepository {

    public static final String TAG = BaseRepository.class.getName();
    public static final String ORDERABLE_TABLE = "orderables";
    public static final String ID = "id";
    public static final String CODE = "code";
    public static final String FULL_PRODUCT_CODE = "full_product_code";
    public static final String NET_CONTENT = "net_content";
    public static final String PACK_ROUNDING_THRESHOLD = "pack_rounding_threshold";
    public static final String ROUND_TO_ZERO = "round_to_zero";
    public static final String DISPENSABLE = "dispensable";
    public static final String TRADE_ITEM_ID = "trade_item_id";
    public static final String COMMODITY_TYPE_ID = "commodity_type_id";
    public static final String DATE_UPDATED = "date_updated";
    public static final String[] ORDERABLE_TABLE_COLUMNS = {ID, CODE, FULL_PRODUCT_CODE, NET_CONTENT, 
            PACK_ROUNDING_THRESHOLD, ROUND_TO_ZERO, DISPENSABLE, TRADE_ITEM_ID, COMMODITY_TYPE_ID, DATE_UPDATED};
    public static final String[] SELECT_TABLE_COLUMNS = {ID, CODE, FULL_PRODUCT_CODE, NET_CONTENT, DISPENSABLE, TRADE_ITEM_ID, COMMODITY_TYPE_ID};
    
    public static final String CREATE_ORDERABLE_TABLE =

            "CREATE TABLE " + ORDERABLE_TABLE
             + "("
                    + ID + " VARCHAR NOT NULL PRIMARY KEY,"
                    + CODE + " VARCHAR NOT NULL,"
                    + FULL_PRODUCT_CODE + " VARCHAR NOT NULL,"
                    + NET_CONTENT + " INTEGER,"
                    + PACK_ROUNDING_THRESHOLD + " INTEGER,"
                    + ROUND_TO_ZERO + " TINYINT,"
                    + DISPENSABLE + " INTEGER,"
                    + TRADE_ITEM_ID + " INTEGER,"
                    + COMMODITY_TYPE_ID + " INTEGER,"
                    + DATE_UPDATED + " INTEGER"
             + ")";
    
    public OrderableRepository(Repository repository) { super(repository); }

    public static void createTable(SQLiteDatabase database) {
        database.execSQL(CREATE_ORDERABLE_TABLE);
    }

    public void addOrUpdate(Orderable orderable) {

        if (orderable == null) {
            return;
        }

        if (orderable.getDateUpdated() == null) {
            orderable.setDateUpdated(Calendar.getInstance().getTimeInMillis());
        }

        try {
            SQLiteDatabase database = getWritableDatabase();

            String query = String.format(INSERT_OR_REPLACE, ORDERABLE_TABLE);
            query += "(" + StringUtils.repeat("?", ",", ORDERABLE_TABLE_COLUMNS.length) + ")";
            database.execSQL(query, createQueryValues(orderable));
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    public List<Orderable> findOrderables(String id, String code, String fullProductCode, String netContent, String dispensable, String tradeItemId, String commodityTypeId) {

        List<Orderable> orderables = new ArrayList<>();
        Cursor cursor = null;
        try {
            String[] selectionArgs = new String[]{id, code, fullProductCode, netContent, dispensable, tradeItemId, commodityTypeId};
            Pair<String, String[]> query= createQuery(selectionArgs, SELECT_TABLE_COLUMNS);

            String querySelectString =  query.first;
            selectionArgs = query.second;

            cursor = getReadableDatabase().query(ORDERABLE_TABLE, ORDERABLE_TABLE_COLUMNS, querySelectString, selectionArgs, null, null, null);
            orderables = readOrderables(cursor);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return orderables;
    }

    private List<Orderable> readOrderables(Cursor cursor) {

        List<Orderable> orderables = new ArrayList<>();
        try {
            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    orderables.add(createOrderable(cursor));
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
        return orderables;
    }

    private Orderable createOrderable(Cursor cursor) {

        return new Orderable(
            cursor.getString(cursor.getColumnIndex(ID)),
            new Code(cursor.getString(cursor.getColumnIndex(CODE))), 
            cursor.getString(cursor.getColumnIndex(FULL_PRODUCT_CODE)),
            cursor.getLong(cursor.getColumnIndex(NET_CONTENT)),
            cursor.getLong(cursor.getColumnIndex(PACK_ROUNDING_THRESHOLD)),
            convertIntToBoolean(cursor.getInt(cursor.getColumnIndex(ROUND_TO_ZERO))),
            new Dispensable(cursor.getString(cursor.getColumnIndex(DISPENSABLE))),
            cursor.getString(cursor.getColumnIndex(TRADE_ITEM_ID)),
            cursor.getString(cursor.getColumnIndex(COMMODITY_TYPE_ID))
        );
    }

    private Object[] createQueryValues(Orderable orderable) {

        Object[] values = new Object[]{
            orderable.getId().toString(),
            orderable.getProductCode().toString(),
            orderable.getFullProductCode(),
            orderable.getNetContent(),
            orderable.getPackRoundingThreshold(),
            convertBooleanToInt(orderable.isRoundToZero()),
            orderable.getDispensable().getId().toString(),
            orderable.getTradeItemIdentifier(),
            orderable.getCommodityTypeIdentifier(),
            orderable.getDateUpdated()
        };
        return values;
    }
}
