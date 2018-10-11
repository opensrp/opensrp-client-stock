package org.smartregister.stock.openlmis.util;

public interface OpenLMISConstants {

    String tradeItem = "tradeItem";

    int EXPIRING_MONTHS_WARNING = 3;

    String TRADE_ITEM = "TRADE_ITEM";

    String LOT_WIDGET = "lot";

    String REVIEW_WIDGET = "review";

    String REFRESH_STOCK_ON_HAND = "refresh_stock";

    String SYNC_COMPLETE_INTENT_ACTION = "org.smartregister.stock.openlmis.SYNC_COMPLETE_NOTIFICATION";

    String PREV_SYNC_SERVER_VERSION_LOT = "prev_sync_server_version_lot";

    String PREV_SYNC_SERVER_VERSION_COMMODITY_TYPE = "prev_sync_server_version_commodity_type";

    String PREV_SYNC_SERVER_VERSION_DISPENSABLE = "prev_sync_server_version_dispensable";

    String PREV_SYNC_SERVER_VERSION_STOCK = "prev_sync_server_version_stock";

    String PREV_SYNC_SERVER_VERSION_PROGRAM_ORDERABLE = "prev_sync_server_version_program_orderable";

    String PREV_SYNC_SERVER_VERSION_PROGRAM = "prev_sync_server_version_program";

    String PREV_SYNC_SERVER_VERSION_REASON = "prev_sync_server_version_reason";

    String PREV_SYNC_SERVER_VERSION_TRADE_ITEM = "prev_sync_server_version_trade_item";

    String PREV_SYNC_SERVER_VERSION_TRADE_ITEM_CLASSIFICATION = "prev_sync_server_version_trade_item_classification";

    String PREV_SYNC_SERVER_VERSION_ORDERABLE = "prev_sync_server_version_orderable";

    String IS_REMOTE_LOGIN = "is_remote_login";

    String SERVICE_TYPE_NAME = "serviceType";

    String SERVICE_ACTION_NAME = "org.smartregister.path.action.START_SERVICE_ACTION";

    String PREV_SYNC_SERVER_VERSION_VALID_SOURCE = "prev_sync_server_version_valid_source";

    String PREV_SYNC_SERVER_VERSION_VALID_DESTINATION = "prev_sync_server_version_valid_destination";

    String FACILITY_TYPE_UUID = "facility_type_uuid";

    String PROGRAM_ID = "program_id";

    String OPENLMIS_UUID = "openlmis_uuid";

    String DEBIT = "DEBIT";

    String CREDIT = "CREDIT";


    interface JsonForm {

        String TRADE_ITEM = "[trade_item]";

        String TRADE_ITEM_ID = "[trade_item_id]";

        String NET_CONTENT = "[net_content]";

        String DISPENSING_UNIT = "[dispensing_unit]";

        String PROGRAM_ID = "[program_id]";

        String USE_VVM = "[use_vvm]";

        String STOCK_ON_HAND = "[stock_on_hand]";

        String PREVIOUS = "previous";

        String PREVIOUS_LABEL = "previous_label";

        String NEXT = "next";

        String NEXT_LABEL = "next_label";

        String NEXT_TYPE = "next_type";

        String NEXT_ENABLED = "next_enabled";

        String SUBMIT = "submit";

        String NO_PADDING = "no_padding";

        String LIST_OPTIONS = "list_options";

        String IS_NON_LOT = "is_non_lot";

        String IS_SPINNABLE = "is_spinnable";

        String POPULATE_VALUES = "populate_values";

        String ISSUE_DESTINATIONS = "issue_destinations";

        String RECEIVE_SOURCES = "receive_sources";

        String ISSUE_REASONS = "issue_reasons";

        String RECEIVE_REASONS = "receive_reasons";

        String ALL_REASONS = "all_reasons";

        String REASON_TYPE = "reason_type";

        String STOCK_BALANCE = "stock_balance";

        String ADJUSTED_QUANTITY ="Adjusted_Quantity";
    }

    interface Forms {

        String INDIVIDUAL_ISSUED_FORM = "individual_issued_form";

        String INDIVIDUAL_RECEIVED_FORM = "individual_received_form";

        String INDIVIDUAL_ADJUST_FORM = "individual_adjustment_form";

        String NON_LOT_INDIVIDUAL_ADJUST_FORM = "non_lot_individual_adjustment_form";

        String INDIVIDUAL_NON_LOT_ISSUE_FORM = "non_lot_individual_issue_form";

        String INDIVIDUAL_NON_LOT_RECEIPT_FORM = "non_lot_individual_receipt_form";
    }

    interface StockStatus {
        String VVM1 = "VVM1";
        String VVM2 = "VVM2";
    }

    interface ServiceType {
        int SYNC_OPENLMIS_METADATA = 1;
        int SYNC_STOCK = 2;
    }
}
