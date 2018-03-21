package org.smartregister.stock.repository;

import android.content.ContentValues;
import android.support.annotation.NonNull;
import android.util.Log;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.smartregister.repository.BaseRepository;
import org.smartregister.repository.Repository;
import org.smartregister.stock.StockLibrary;
import org.smartregister.stock.domain.Order;
import org.smartregister.stock.domain.OrderShipment;
import org.smartregister.stock.domain.Shipment;
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
    private String[] ORDER_TABLE_COLUMNS = {ID, REVISION, FORM_SUBMISSION_ID, TYPE, DATE_CREATED, DATE_EDITED, SERVER_VERSION,
            LOCATION_ID, PROVIDER_ID, DATE_CREATED_BY_CLIENT, SYNCED};
    private static final String CREATE_ORDER_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS " + ORDER_TABLE + "(" +
            "id VARCHAR UNIQUE," +
            "revision VARCHAR," +
            "form_submission_id VARCHAR PRIMARY KEY," +
            "type VARCHAR NOT NULL," +
            "date_created BIGINT," +
            "date_edited BIGINT," +
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

    public void addOrUpdateOrder(@NonNull Order order) {
        try {
            Order existingOrder = getOrderByFormSubmissionId(order.getFormSubmissionId());

            if (existingOrder == null) {
                SQLiteDatabase database = getWritableDatabase();
                database.insert(ORDER_TABLE, null, createValuesForOrder(order));
            } else {
                updateOrder(order);
            }
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    private ContentValues createValuesForOrder(Order order) {
        ContentValues values = new ContentValues();
        values.put(ID, order.getId());
        values.put(REVISION, order.getRevision());
        values.put(FORM_SUBMISSION_ID, order.getFormSubmissionId());
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

    public Order getOrderByFormSubmissionId(String formSubmissionId) {
        Cursor cursor = getReadableDatabase().query(ORDER_TABLE, ORDER_TABLE_COLUMNS,
                FORM_SUBMISSION_ID  + " = ?", new String[]{formSubmissionId}, null, null, null, "1");
        List<Order> orders = readOrders(cursor);
        if (orders.size() != 0) {
            return orders.get(0);
        }

        return null;
    }

    public List<Order> getAllOrders() {
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + ORDER_TABLE + " ORDER BY date_created_by_client", null);
        List<Order> orders = readOrders(cursor);
        return orders;
    }

    public List<OrderShipment> getAllOrdersWithShipments() {
        Cursor cursor = getReadableDatabase().rawQuery("SELECT " + ORDER_TABLE + ".*, "
                + ShipmentRepository.SHIPMENT_TABLE + ".* FROM " + ORDER_TABLE + " LEFT JOIN "
                + ShipmentRepository.SHIPMENT_TABLE + " ON " + ORDER_TABLE + "." + ID
                + " = " + ShipmentRepository.SHIPMENT_TABLE + "." + Constants.Shipment.ORDER_CODE, null);

        return readOrderShipments(cursor);
    }

    public List<Order> getAllUnSyncedOrders(){
        List<Order> orders;
        Cursor cursor = getReadableDatabase().query(ORDER_TABLE, ORDER_TABLE_COLUMNS,
                SYNCED + " = 0", null, null, null, null);
        orders = readOrders(cursor);
        return orders;
    }

    public void setOrderStatusToSynced(List<Order> orders) {
        for (Order order : orders) {
            order.setSynced(true);
            updateOrder(order);
        }
    }

    private void updateOrder(Order order) {
        getReadableDatabase().update(ORDER_TABLE, createValuesForOrder(order), FORM_SUBMISSION_ID + " = ?",
                new String[]{order.getFormSubmissionId()});
    }

    private List<Order> readOrders(Cursor cursor) {
        List<Order> orders = new ArrayList<>();

        try {
            if (cursor != null && cursor.getCount() > 0  && cursor.moveToFirst()) {
                while(!cursor.isAfterLast()) {
                    Order currOrder = getOrderAtRow(cursor);

                    orders.add(currOrder);
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

    public List<OrderShipment> readOrderShipments(Cursor cursor) {
        List<OrderShipment> orderShipmentsList = new ArrayList<>();
        ShipmentRepository shipmentRepository = StockLibrary.getInstance().getShipmentRepository();

        try {
            if (cursor != null && cursor.getCount() > 0  && cursor.moveToFirst()) {
                while(!cursor.isAfterLast()) {
                    Order currOrder = getOrderAtRow(cursor);

                    Shipment shipment = shipmentRepository.getShipmentAtRow(cursor);

                    OrderShipment orderShipment = new OrderShipment();
                    orderShipment.setOrder(currOrder);
                    orderShipment.setShipment(shipment);

                    cursor.moveToNext();

                    orderShipmentsList.add(orderShipment);
                }
            }
        } catch(Exception e) {
            Log.e(TAG, e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return  orderShipmentsList;
    }

    private Order getOrderAtRow(@NonNull Cursor cursor) {
        Order currOrder = new Order();
        currOrder.setId(cursor.getString(cursor.getColumnIndex(ID)));
        currOrder.setRevision(cursor.getString(cursor.getColumnIndex(REVISION)));
        currOrder.setFormSubmissionId(cursor.getString(cursor.getColumnIndex(FORM_SUBMISSION_ID)));
        currOrder.setType(cursor.getString(cursor.getColumnIndex(TYPE)));
        currOrder.setDateCreated(cursor.getLong(cursor.getColumnIndex(DATE_CREATED)));
        currOrder.setDateEdited(cursor.getLong(cursor.getColumnIndex(DATE_EDITED)));
        currOrder.setServerVersion(cursor.getLong(cursor.getColumnIndex(SERVER_VERSION)));
        currOrder.setLocationId(cursor.getString(cursor.getColumnIndex(LOCATION_ID)));
        currOrder.setProviderId(cursor.getString(cursor.getColumnIndex(PROVIDER_ID)));
        currOrder.setDateCreatedByClient(cursor.getLong(cursor.getColumnIndex(DATE_CREATED_BY_CLIENT)));
        currOrder.setSynced(cursor.getInt(cursor.getColumnIndex(SYNCED)) == 1);

        return currOrder;
    }
}
