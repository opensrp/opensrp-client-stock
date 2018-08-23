package org.smartregister.stock.openlmis.util;

public interface OpenLMISConstants {

    String TRADE_ITEM = "TRADE_ITEM";

    String LOT_WIDGET = "lot";

    interface JsonForm {

        String TRADE_ITEM = "[trade_item]";

        String NET_CONTENT = "[net_content]";

        String PREVIOUS = "previous";

        String NEXT = "next";

        String BACKGROUND = "background";

        String UNDERLINE_COLOR = "underline_color";

        String VALUE = "value";
    }

    interface Forms {
        String INDIVIDUAL_ISSUED_FORM = "individual_issued_form";

        String INDIVIDUAL_RECEIVED_FORM = "individual_received_form";
    }
}
