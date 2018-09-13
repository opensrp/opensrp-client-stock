package org.smartregister.stock.openlmis.util;

public interface OpenLMISConstants {

    String tradeItem="tradeItem";

    int EXPIRING_MONTHS_WARNING = 3;

    String TRADE_ITEM = "TRADE_ITEM";

    String LOT_WIDGET = "lot";

    String REVIEW_WIDGET = "review";

    String SYNC_COMPLETE_INTENT_ACTION = "org.smartregister.stock.openlmis.SYNC_COMPLETE_NOTIFICATION";

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

        String INDIVIDUAL_ADJUST_FORM = "individual_adjust_form";
    }

    public static final class ServiceType {
        public static final int SYNC_OPENLMIS_METADATA = 1;
        public static final int SYNC_STOCK = 2;
        String INDIVIDUAL_ADJUST_FORM = "individual_adjustment_form";
    }
}
