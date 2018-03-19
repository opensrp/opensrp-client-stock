package org.smartregister.stock.repository;

import android.content.ContentValues;
import android.support.annotation.NonNull;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.smartregister.repository.BaseRepository;
import org.smartregister.repository.Repository;
import org.smartregister.stock.domain.ShipmentLineItem;
import org.smartregister.stock.util.Constants;

import java.util.ArrayList;

/**
 * Created by Ephraim Kigamba - ekigamba@ona.io on 08/03/2018.
 */

public class ShipmentLineItemRepository extends BaseRepository {

    public static final String SHIPMENT_LINE_ITEM_TABLE = "shipment_line_items";
    private static final String SHIPMENT_LINE_ITEM_SQL = "CREATE TABLE IF NOT EXISTS " + SHIPMENT_LINE_ITEM_TABLE +
            "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "shipment_order_code VARCHAR NOT NULL," +
            "antigen_type VARCHAR NOT NULL," +
            "ordered_quantity INTEGER NOT NULL," +
            "shipped_quantity INTEGER NOT NULL," +
            "number_of_doses INTEGER NOT NULL," +
            "accepted_quantity INTEGER NOT NULL," +
            "FOREIGN KEY (shipment_order_code) REFERENCES " + ShipmentRepository.SHIPMENT_TABLE + "(order_code))";

    private String[] SHIPMENT_LINE_ITEM_TABLE_COLUMNS = {
            Constants.ShipmentLineItem.ID,
            Constants.ShipmentLineItem.SHIPMENT_ORDER_CODE,
            Constants.ShipmentLineItem.ANTIGEN_TYPE,
            Constants.ShipmentLineItem.ORDERED_QUANTITY,
            Constants.ShipmentLineItem.SHIPPED_QUANTITY,
            Constants.ShipmentLineItem.ACCEPTED_QUANTITY,
            Constants.ShipmentLineItem.NUMBER_OF_DOSES
    };

    public ShipmentLineItemRepository(Repository repository) {
        super(repository);
    }

    public static void createTable(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SHIPMENT_LINE_ITEM_SQL);
    }

    public void addShipmentLineItem(@NonNull ShipmentLineItem shipmentLineItem) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        shipmentLineItem.setId((int) sqLiteDatabase.insert(SHIPMENT_LINE_ITEM_TABLE, null, createValuesForShipmentLineItem(shipmentLineItem)));
    }

    private ContentValues createValuesForShipmentLineItem(ShipmentLineItem shipmentLineItem) {
        ContentValues lineItemContentValues = new ContentValues();
        lineItemContentValues.put(Constants.ShipmentLineItem.SHIPMENT_ORDER_CODE, shipmentLineItem.getShipmentOrderCode());
        lineItemContentValues.put(Constants.ShipmentLineItem.ANTIGEN_TYPE, shipmentLineItem.getAntigenType());
        lineItemContentValues.put(Constants.ShipmentLineItem.ORDERED_QUANTITY, shipmentLineItem.getOrderedQuantity());
        lineItemContentValues.put(Constants.ShipmentLineItem.SHIPPED_QUANTITY, shipmentLineItem.getShippedQuantity());
        lineItemContentValues.put(Constants.ShipmentLineItem.NUMBER_OF_DOSES, shipmentLineItem.getNumberOfDoses());
        lineItemContentValues.put(Constants.ShipmentLineItem.ACCEPTED_QUANTITY, shipmentLineItem.getAcceptedQuantity());

        return lineItemContentValues;
    }

    public ShipmentLineItem[] getShipmentLineItemsForShipment(String orderCode) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(SHIPMENT_LINE_ITEM_TABLE, SHIPMENT_LINE_ITEM_TABLE_COLUMNS,
                Constants.ShipmentLineItem.SHIPMENT_ORDER_CODE + " = ?", new String[]{orderCode}, null, null, null);

        ArrayList<ShipmentLineItem> shipmentLineItemArrayList = getShipments(cursor);

        return shipmentLineItemArrayList.toArray(new ShipmentLineItem[shipmentLineItemArrayList.size()]);
    }

    private ArrayList<ShipmentLineItem> getShipments(Cursor cursor) {
        ArrayList<ShipmentLineItem> shipmentLineItemArrayList = new ArrayList<>();

        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            while(!cursor.isAfterLast()) {
                shipmentLineItemArrayList.add(getShipmentLineItemAtRow(cursor));
                cursor.moveToNext();
            }
        }

        if (cursor != null) {
            cursor.close();
        }

        return shipmentLineItemArrayList;
    }

    public ShipmentLineItem getShipmentLineItemAtRow(Cursor cursor) {
        ShipmentLineItem shipmentLineItem = new ShipmentLineItem();

        shipmentLineItem.setId(cursor.getInt(cursor.getColumnIndex(Constants.ShipmentLineItem.ID)));
        shipmentLineItem.setShippedQuantity(cursor.getInt(cursor.getColumnIndex(Constants.ShipmentLineItem.SHIPPED_QUANTITY)));
        shipmentLineItem.setOrderedQuantity(cursor.getInt(cursor.getColumnIndex(Constants.ShipmentLineItem.ORDERED_QUANTITY)));
        shipmentLineItem.setShipmentOrderCode(cursor.getString(cursor.getColumnIndex(Constants.ShipmentLineItem.SHIPMENT_ORDER_CODE)));
        shipmentLineItem.setNumberOfDoses(cursor.getInt(cursor.getColumnIndex(Constants.ShipmentLineItem.NUMBER_OF_DOSES)));
        shipmentLineItem.setAcceptedQuantity(cursor.getInt(cursor.getColumnIndex(Constants.ShipmentLineItem.ACCEPTED_QUANTITY)));
        shipmentLineItem.setAntigenType(cursor.getString(cursor.getColumnIndex(Constants.ShipmentLineItem.ANTIGEN_TYPE)));

        return shipmentLineItem;
    }
}
