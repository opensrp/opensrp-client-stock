package org.smartregister.stock.repository;

import android.content.ContentValues;
import android.support.annotation.NonNull;

import net.sqlcipher.database.SQLiteDatabase;

import org.smartregister.repository.BaseRepository;
import org.smartregister.repository.Repository;
import org.smartregister.stock.domain.ShipmentLineItem;
import org.smartregister.stock.util.Constants;

/**
 * Created by Ephraim Kigamba - ekigamba@ona.io on 08/03/2018.
 */

public class ShipmentLineItemRepository extends BaseRepository {

    public static final String SHIPMENT_LINE_ITEM_TABLE = "shipment_line_items";
    private static final String SHIPMENT_LINE_ITEM_SQL = "create table if not exists " + SHIPMENT_LINE_ITEM_TABLE +
            "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "shipment_order_code VARCHAR NOT NULL," +
            "antigen_type VARCHAR NOT NULL," +
            "ordered_quantity INTEGER NOT NULL," +
            "shipped_quantity INTEGER NOT NULL," +
            "number_of_doses INTEGER NOT NULL," +
            "accepted_quantity INTEGER NOT NULL," +
            "FOREIGN KEY (shipment_order_code) REFERENCES " + ShipmentRepository.SHIPMENT_TABLE + "(order_code))";

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
}
