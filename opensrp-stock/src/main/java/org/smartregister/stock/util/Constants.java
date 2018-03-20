package org.smartregister.stock.util;

/**
 * Created by samuelgithengi on 2/7/18.
 */

public class Constants {
    public static final String STOCK_TYPE_JSON_FILE = "stock_type.json";
    public static final String VACCINES_JSON_FILE = "vaccines.json";
    public static final String ARG_STOCK_TYPE = "stock_type";
    public static final String STEPNAME = "stepName";

    public static final String NUMBER_PICKER = "number_picker";

    public static class Order {

        public static final String ID = "id";
        public static final String REVISION = "revision";
        public static final String TYPE = "type";
        public static final String DATE_CREATED = "date_created";
        public static final String DATE_EDITED = "date_edited";
        public static final String SERVER_VERSION = "server_version";
        public static final String LOCATION_ID = "location_id";
        public static final String PROVIDER_ID = "provider_id";
        public static final String DATE_CREATED_BY_CLIENT = "date_created_by_client";
        public static final String SYNCED = "synced";
    }
   
    public static class Shipment {

        public static final String ORDER_CODE = "order_code";
        public static final String OPENLMIS_ORDER_CODE = "openlmis_order_code";
        public static final String ORDERED_DATE = "ordered_date";
        public static final String RECEIVING_FACILITY_CODE = "receiving_facility_code";
        public static final String RECEIVING_FACILITY_NAME = "receiving_facility_name";
        public static final String SUPPLYING_FACILITY_CODE = "supplying_facility_code";
        public static final String SUPPLYING_FACILITY_NAME = "supplying_facility_name";
        public static final String PROCESSING_PERIOD_START_DATE = "processing_period_start_date";
        public static final String PROCESSING_PERIOD_END_DATE = "processing_period_end_date";
        public static final String SHIPMENT_ACCEPT_STATUS = "shipment_accept_status";
        public static final String SERVER_VERSION = "server_version";
        public static final String SYNCED = "synced";

        public static final String ACCEPT_STATUS_REJECTED = "REJECTED";
        public static final String ACCEPT_STATUS_NO_ACTION = null;

        // This means that all ShipmentLineItems in this shipment were accepted
        public static final String ACCEPT_STATUS_FULLY_ACCEPTED = "FULLY_ACCEPTED";
        // This means that only some ShipmentLineItems in this shipment were accepted
        public static final String ACCEPT_STATUS_PARTIALLY_ACCEPTED = "PARTIALLY_ACCEPTED";
    }

    public static class ShipmentLineItem {

        public static final String ID = "id";
        public static final String SHIPMENT_ORDER_CODE = "shipment_order_code";
        public static final String ANTIGEN_TYPE = "antigen_type";
        public static final String ORDERED_QUANTITY = "ordered_quantity";
        public static final String SHIPPED_QUANTITY = "shipped_quantity";
        public static final String NUMBER_OF_DOSES = "number_of_doses";
        public static final String ACCEPTED_QUANTITY = "accepted_quantity";
    }
}
