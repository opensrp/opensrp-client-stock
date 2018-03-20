package org.smartregister.stock.repository;

import android.content.ContentValues;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.smartregister.repository.BaseRepository;
import org.smartregister.repository.Repository;
import org.smartregister.stock.StockLibrary;
import org.smartregister.stock.domain.Shipment;
import org.smartregister.stock.domain.ShipmentLineItem;
import org.smartregister.stock.util.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ephraim Kigamba - ekigamba@ona.io on 07/03/2018.
 */

public class ShipmentRepository extends BaseRepository {

    private static final String TAG = ShipmentRepository.class.getName();
    public static final String SHIPMENT_TABLE = "shipments";
    private static final String SHIPMENT_SQL = "CREATE TABLE IF NOT EXISTS " + SHIPMENT_TABLE +
            "(order_code VARCHAR PRIMARY KEY," +
            "ordered_date DATE," +
            "openlmis_order_code VARCHAR NOT NULL," +
            "receiving_facility_code VARCHAR NOT NULL," +
            "receiving_facility_name VARCHAR NOT NULL," +
            "supplying_facility_code VARCHAR NOT NULL," +
            "supplying_facility_name VARCHAR NOT NULL," +
            "processing_period_start_date DATE," +
            "processing_period_end_date DATE," +
            "shipment_accept_status VARCHAR," +
            "server_version BIGINT NOT NULL," +
            "synced TINYINT NOT NULL)";

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public ShipmentRepository(Repository repository) {
        super(repository);
    }

    public static void createTable(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SHIPMENT_SQL);
    }

    public void addShipment(@NonNull Shipment shipment) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        if (!isShipmentAdded(shipment)) {
            sqLiteDatabase.insert(SHIPMENT_TABLE, null, createValuesForShipment(shipment));

            ShipmentLineItemRepository shipmentLineItemRepository = StockLibrary.getInstance().getShipmentLineItemRepository();
            for (ShipmentLineItem shipmentLineItem : shipment.getShipmentLineItems()) {
                shipmentLineItemRepository.addShipmentLineItem(shipmentLineItem);
            }
        }
    }

    public void updateShipment(@NonNull Shipment shipment) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        sqLiteDatabase.update(SHIPMENT_TABLE, createValuesForShipment(shipment), Constants.Shipment.ORDER_CODE
                + " = ?", new String[]{shipment.getOrderCode()});
    }

    public boolean isShipmentAdded(Shipment shipment) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT " + Constants.Shipment.ORDER_CODE + " FROM " + SHIPMENT_TABLE + " WHERE "
                + Constants.Shipment.ORDER_CODE + " = '" + shipment.getOrderCode() + "' LIMIT 1", null);

        if (cursor != null) {
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                cursor.close();
                return true;
            }
            cursor.close();
        }

        return false;
    }

    private ContentValues createValuesForShipment(Shipment shipment) {
        ContentValues shipmentContentValues = new ContentValues();
        shipmentContentValues.put(Constants.Shipment.ORDER_CODE, shipment.getOrderCode());
        shipmentContentValues.put(Constants.Shipment.OPENLMIS_ORDER_CODE, shipment.getOpenlmisOrderCode());
        shipmentContentValues.put(Constants.Shipment.ORDERED_DATE, getSQLiteFriendlyDateString(shipment.getOrderedDate()));
        shipmentContentValues.put(Constants.Shipment.RECEIVING_FACILITY_CODE, shipment.getReceivingFacilityCode());
        shipmentContentValues.put(Constants.Shipment.RECEIVING_FACILITY_NAME, shipment.getReceivingFacilityName());
        shipmentContentValues.put(Constants.Shipment.SUPPLYING_FACILITY_CODE, shipment.getSupplyingFacilityCode());
        shipmentContentValues.put(Constants.Shipment.SUPPLYING_FACILITY_NAME, shipment.getSupplyingFacilityName());
        shipmentContentValues.put(Constants.Shipment.PROCESSING_PERIOD_START_DATE, getSQLiteFriendlyDateString(shipment.getProcessingPeriodStartDate()));
        shipmentContentValues.put(Constants.Shipment.PROCESSING_PERIOD_END_DATE, getSQLiteFriendlyDateString(shipment.getProcessingPeriodEndDate()));
        shipmentContentValues.put(Constants.Shipment.SHIPMENT_ACCEPT_STATUS, shipment.getShipmentAcceptStatus());
        shipmentContentValues.put(Constants.Shipment.SERVER_VERSION, shipment.getServerVersion());
        shipmentContentValues.put(Constants.Shipment.SYNCED, shipment.isSynced());

        return shipmentContentValues;
    }

    public Shipment getShipmentAtRow(@NonNull Cursor cursor) {
        String orderCode = cursor.getString(cursor.getColumnIndex(Constants.Shipment.ORDER_CODE));

        if (orderCode != null) {
            return new Shipment(
                    orderCode,
                    cursor.getString(cursor.getColumnIndex(Constants.Shipment.OPENLMIS_ORDER_CODE)),
                    getDateFromSQLiteFriendlyDateString(cursor.getString(cursor.getColumnIndex(Constants.Shipment.ORDERED_DATE))),
                    cursor.getString(cursor.getColumnIndex(Constants.Shipment.RECEIVING_FACILITY_CODE)),
                    cursor.getString(cursor.getColumnIndex(Constants.Shipment.RECEIVING_FACILITY_NAME)),
                    cursor.getString(cursor.getColumnIndex(Constants.Shipment.SUPPLYING_FACILITY_CODE)),
                    cursor.getString(cursor.getColumnIndex(Constants.Shipment.SUPPLYING_FACILITY_NAME)),
                    getDateFromSQLiteFriendlyDateString(cursor.getString(cursor.getColumnIndex(Constants.Shipment.PROCESSING_PERIOD_START_DATE))),
                    getDateFromSQLiteFriendlyDateString(cursor.getString(cursor.getColumnIndex(Constants.Shipment.PROCESSING_PERIOD_END_DATE))),
                    cursor.getString(cursor.getColumnIndex(Constants.Shipment.SHIPMENT_ACCEPT_STATUS)),
                    cursor.getLong(cursor.getColumnIndex(Constants.Shipment.SERVER_VERSION)),
                    (cursor.getInt(cursor.getColumnIndex(Constants.Shipment.SYNCED)) == 1)
            );
        } else {
            return null;
        }
    }

    private String getSQLiteFriendlyDateString(@Nullable Date date) {
        if (date == null) {
            return null;
        }

        return dateFormat.format(date);
    }

    private Date getDateFromSQLiteFriendlyDateString(@Nullable String dateString) {
        if (dateString == null)  {
            return null;
        }

        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
        return null;
    }
}
