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

    public static class Shipment {

        public static final String ORDER_CODE = "order_code";
        public static final String ORDERED_DATE = "ordered_date";
        public static final String RECEIVING_FACILITY_CODE = "receiving_facility_code";
        public static final String RECEIVING_FACILITY_NAME = "receiving_facility_name";
        public static final String SUPPLYING_FACILITY_CODE = "supplying_facility_code";
        public static final String SUPPLYING_FACILITY_NAME = "supplying_facility_name";
        public static final String PROCESSING_PERIOD_START_DATE = "processing_period_start_date";
        public static final String PROCESSING_PERIOD_END_DATE = "processing_period_end_date";
        public static final String SHIPMENT_ACCEPT_STATUS = "shipment_accept_status";
        public static final String SYNCED = "synced";
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
