package org.smartregister.stock.openlmis.util;

public interface OpenLMISConstants {

    String TRADE_ITEM = "TRADE_ITEM";

    String LOT_WIDGET = "lot";

    interface JsonForm {

        String TRADE_ITEM = "[trade_item]";

        String TRADE_ITEM_ID = "[trade_item_id]";

        String NET_CONTENT = "[net_content]";

        String DISPENSING_UNIT = "[dispensing_unit]";

        String PREVIOUS = "previous";

        String PREVIOUS_LABEL = "previous_label";

        String NEXT = "next";

        String NEXT_LABEL = "next_label";

    }

    interface Forms {
        String INDIVIDUAL_ISSUED_FORM = "individual_issued_form";

        String INDIVIDUAL_RECEIVED_FORM = "individual_received_form";
    }
}
