package org.smartregister.stock.openlmis.util;

public interface OpenLMISConstants {

    String tradeItem = "tradeItem";

    int EXPIRING_MONTHS_WARNING = 3;

    String TRADE_ITEM = "TRADE_ITEM";

    String LOT_WIDGET = "lot";

    String REVIEW_WIDGET = "review";

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

    interface JsonForm {

        String TRADE_ITEM = "[trade_item]";

        String TRADE_ITEM_ID = "[trade_item_id]";

        String NET_CONTENT = "[net_content]";

        String DISPENSING_UNIT = "[dispensing_unit]";

        String PREVIOUS = "previous";

        String PREVIOUS_LABEL = "previous_label";

        String NEXT = "next";

        String NEXT_LABEL = "next_label";

        String NEXT_TYPE = "next_type";

        String NEXT_ENABLED = "next_enabled";

        String SUBMIT = "submit";

        String NO_PADDING = "no_padding";
    }

    interface Forms {
        String INDIVIDUAL_ISSUED_FORM = "individual_issued_form";

        String INDIVIDUAL_RECEIVED_FORM = "individual_received_form";
        
        String INDIVIDUAL_ADJUST_FORM = "individual_adjustment_form";
    }

    interface ServiceType {
        int SYNC_OPENLMIS_METADATA = 1;
        int SYNC_STOCK = 2;
    }
}
