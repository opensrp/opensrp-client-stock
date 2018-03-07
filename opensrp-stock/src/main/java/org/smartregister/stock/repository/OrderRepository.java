package org.smartregister.stock.repository;

import android.content.ContentValues;
import android.support.annotation.NonNull;
import android.util.Log;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.smartregister.repository.BaseRepository;
import org.smartregister.repository.Repository;
import org.smartregister.stock.domain.Order;
import org.smartregister.stock.util.Constants;

import java.util.ArrayList;
import java.util.List;

import static org.smartregister.stock.util.Constants.Order.*;

/**
 * Created by Ephraim Kigamba - ekigamba@ona.io on 07/03/2018.
 * Created by Vincent Karuri - vincentmkaruri@gmail.com on 07/03/2018.
 */

public class OrderRepository extends BaseRepository {
    private static final String TAG = OrderRepository.class.getName();
    private static final String ORDER_TABLE = "orders";
    private String[] ORDER_TABLE_COLUMNS = {Constants.Order.ID, Constants.Order.REVISION, Constants.Order.TYPE,
            Constants.Order.DATE_CREATED, Constants.Order.DATE_EDITED, Constants.Order.SERVER_VERSION,
            Constants.Order.LOCATION_ID, Constants.Order.PROVIDER_ID, DATE_CREATED_BY_CLIENT, SYNCED};
    private static final String CREATE_ORDER_TABLE_QUERY = "CREATE TABLE orders(" +
            "id VARCHAR PRIMARY KEY," +
            "revision VARCHAR," +
            "type VARCHAR NOT NULL," +
            "date_created BIGINT," +
            "date_edited BIGINT" +
            "server_version BIGINT," +
            "location_id VARCHAR NOT NULL," +
            "provider_id VARCHAR NOT NULL," +
            "date_created_by_client BIGINT NOT NULL," +
            "synced TINYINT NOT NULL)";

    public OrderRepository(Repository repository) {
        super(repository);
    }

    public static void createTable(SQLiteDatabase database) {
        database.execSQL(CREATE_ORDER_TABLE_QUERY);
    }

    public void addOrder(@NonNull Order order) {
        try {
            SQLiteDatabase database = getWritableDatabase();
            database.insert(ORDER_TABLE, null, createValuesForOrder(order));
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private ContentValues createValuesForOrder(Order order) {
        ContentValues values = new ContentValues();
        values.put(ID, order.getId());
        values.put(REVISION, order.getRevision());
        values.put(TYPE, order.getType());
        values.put(DATE_CREATED, order.getDateCreated());
        values.put(DATE_EDITED, order.getDateEdited());
        values.put(SERVER_VERSION, order.getServerVersion());
        values.put(LOCATION_ID, order.getLocationId());
        values.put(PROVIDER_ID, order.getProviderId());
        values.put(DATE_CREATED_BY_CLIENT, order.getDateCreatedByClient());
        values.put(SYNCED, order.isSynced());
        return values;
    }

    private Order getOrderById(String id) {
        Cursor cursor = getReadableDatabase().query(ORDER_TABLE, ORDER_TABLE_COLUMNS,
                ID  + " = ?", new String[]{id}, null, null, null, "1");
        List<Order> orders = readOrders(cursor);
        if (orders.size() != 0) {
            return orders.get(0);
        }

        return null;
    }

    private List<Order> getAllOrders() {
        Cursor cursor = getReadableDatabase().rawQuery("select * from " + ORDER_TABLE + " order by date_created_by_client", null);
        List<Order> orders = readOrders(cursor);
        return orders;
    }

    private List<Order> readOrders(Cursor cursor) {
        List<Order> orders = new ArrayList<>();

        try {
            if (cursor != null && cursor.getCount() > 0  && cursor.moveToFirst()) {
                while(!cursor.isAfterLast()) {
                    orders.add(new Order (
                            cursor.getString(cursor.getColumnIndex(ID)),
                            cursor.getString(cursor.getColumnIndex(REVISION)),
                            cursor.getString(cursor.getColumnIndex(TYPE)),
                            cursor.getLong(cursor.getColumnIndex(DATE_CREATED)),
                            cursor.getLong(cursor.getColumnIndex(DATE_EDITED)),
                            cursor.getLong(cursor.getColumnIndex(SERVER_VERSION)),
                            cursor.getString(cursor.getColumnIndex(LOCATION_ID)),
                            cursor.getString(cursor.getColumnIndex(PROVIDER_ID)),
                            cursor.getLong(cursor.getColumnIndex(DATE_CREATED_BY_CLIENT)),
                            (cursor.getInt(cursor.getColumnIndex(SYNCED)) == 1)
                    ));
                    cursor.moveToNext();
                }
            }
        } catch(Exception e) {
            Log.e(TAG, e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return  orders;
    }
}
