package org.smartregister.stock.openlmis;

/**
 * Created by samuelgithengi on 8/31/18.
 */
public class TestData {

    public static final String EDIT_TEXT_JSON = "{\n" +
            "        \"key\": \"Last_Name\",\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"person\",\n" +
            "        \"openmrs_entity_id\": \"last_name\",\n" +
            "        \"type\": \"edit_text\",\n" +
            "        \"hint\": \"Last name *\",\n" +
            "        \"edit_type\": \"name\",\n" +
            "        \"v_required\": {\n" +
            "          \"value\": \"true\",\n" +
            "          \"err\": \"Please enter the last name\"\n" +
            "        }" +
            "      }";

    public static final String ISSUE_FORM_JSON = "{\n" +
            "  \"count\": \"3\",\n" +
            "  \"encounter_type\": \"\",\n" +
            "  \"entity_id\": \"\",\n" +
            "  \"metadata\": {\n" +
            "    \"start\": {\n" +
            "      \"openmrs_entity_parent\": \"\",\n" +
            "      \"openmrs_entity\": \"concept\",\n" +
            "      \"openmrs_data_type\": \"start\",\n" +
            "      \"openmrs_entity_id\": \"163137AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"\n" +
            "    },\n" +
            "    \"end\": {\n" +
            "      \"openmrs_entity_parent\": \"\",\n" +
            "      \"openmrs_entity\": \"concept\",\n" +
            "      \"openmrs_data_type\": \"end\",\n" +
            "      \"openmrs_entity_id\": \"163138AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"\n" +
            "    },\n" +
            "    \"today\": {\n" +
            "      \"openmrs_entity_parent\": \"\",\n" +
            "      \"openmrs_entity\": \"encounter\",\n" +
            "      \"openmrs_entity_id\": \"encounter_date\"\n" +
            "    },\n" +
            "    \"deviceid\": {\n" +
            "      \"openmrs_entity_parent\": \"\",\n" +
            "      \"openmrs_entity\": \"concept\",\n" +
            "      \"openmrs_data_type\": \"deviceid\",\n" +
            "      \"openmrs_entity_id\": \"163149AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"\n" +
            "    },\n" +
            "    \"subscriberid\": {\n" +
            "      \"openmrs_entity_parent\": \"\",\n" +
            "      \"openmrs_entity\": \"concept\",\n" +
            "      \"openmrs_data_type\": \"subscriberid\",\n" +
            "      \"openmrs_entity_id\": \"163150AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"\n" +
            "    },\n" +
            "    \"simserial\": {\n" +
            "      \"openmrs_entity_parent\": \"\",\n" +
            "      \"openmrs_entity\": \"concept\",\n" +
            "      \"openmrs_data_type\": \"simserial\",\n" +
            "      \"openmrs_entity_id\": \"163151AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"\n" +
            "    },\n" +
            "    \"phonenumber\": {\n" +
            "      \"openmrs_entity_parent\": \"\",\n" +
            "      \"openmrs_entity\": \"concept\",\n" +
            "      \"openmrs_data_type\": \"phonenumber\",\n" +
            "      \"openmrs_entity_id\": \"163152AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"\n" +
            "    },\n" +
            "    \"encounter_location\": \"\"\n" +
            "  },\n" +
            "  \"step1\": {\n" +
            "    \"title\": \"Issue [trade_item]\",\n" +
            "    \"next\": \"step2\",\n" +
            "    \"bottom_navigation\": \"true\",\n" +
            "    \"fields\": [\n" +
            "      {\n" +
            "        \"key\": \"Date_Stock_Issued\",\n" +
            "        \"type\": \"date_picker\",\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\",\n" +
            "        \"hint\": \"Date\",\n" +
            "        \"expanded\": false,\n" +
            "        \"min_date\": \"today-1m\",\n" +
            "        \"max_date\": \"today\",\n" +
            "        \"background\": \"#EAEAEA\",\n" +
            "        \"underline_color\": \"#EAEAEA\",\n" +
            "        \"v_required\": {\n" +
            "          \"value\": \"true\",\n" +
            "          \"err\": \"Enter the Stock Issue date\"\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"labelIssuedTo\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text\": \"Issued to *\",\n" +
            "        \"top_margin\": \"10dp\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"Issued_Stock_To\",\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\",\n" +
            "        \"openmrs_data_type\": \"select one\",\n" +
            "        \"type\": \"native_radio\",\n" +
            "        \"options\": [\n" +
            "          {\n" +
            "            \"key\": \"Comfort Health Clinic\",\n" +
            "            \"text\": \"Comfort Health Clinic\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"key\": \"Balaka District Warehouse\",\n" +
            "            \"text\": \"Balaka District Warehouse\"\n" +
            "          }\n" +
            "        ],\n" +
            "        \"value\": \"Comfort Health Clinic\",\n" +
            "        \"v_required\": {\n" +
            "          \"value\": true,\n" +
            "          \"err\": \"Please select where stock was issued\"\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"labelReason\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text\": \"Reason *\",\n" +
            "        \"top_margin\": \"10dp\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"Issued_Stock_Reason\",\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\",\n" +
            "        \"openmrs_data_type\": \"select one\",\n" +
            "        \"type\": \"native_radio\",\n" +
            "        \"options\": [\n" +
            "          {\n" +
            "            \"key\": \"Consumed\",\n" +
            "            \"text\": \"Consumed\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"key\": \"Transfer Out\",\n" +
            "            \"text\": \"Transfer Out\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"key\": \"Damage\",\n" +
            "            \"text\": \"Damage\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"key\": \"Other\",\n" +
            "            \"text\": \"Other\"\n" +
            "          }\n" +
            "        ],\n" +
            "        \"value\": \"Consumed\",\n" +
            "        \"v_required\": {\n" +
            "          \"value\": true,\n" +
            "          \"err\": \"Please select reason stock was issued\"\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"Issued_Stock_Reason_Other\",\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\",\n" +
            "        \"type\": \"edit_text\",\n" +
            "        \"hint\": \"Please specify: *\",\n" +
            "        \"edit_type\": \"name\",\n" +
            "        \"v_required\": {\n" +
            "          \"value\": true,\n" +
            "          \"err\": \"Please specify reason stock was issued\"\n" +
            "        },\n" +
            "        \"relevance\": {\n" +
            "          \"step1:Issued_Stock_Reason\": {\n" +
            "            \"type\": \"string\",\n" +
            "            \"ex\": \"equalTo(., \\\"Other\\\")\"\n" +
            "          }\n" +
            "        }\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"step2\": {\n" +
            "    \"title\": \"Issue [trade_item]\",\n" +
            "    \"previous\": \"step1\",\n" +
            "    \"next\": \"step3\",\n" +
            "    \"next_label\": \"Review\",\n" +
            "    \"bottom_navigation\": \"true\",\n" +
            "    \"fields\": [\n" +
            "      {\n" +
            "        \"key\": \"stockLots\",\n" +
            "        \"type\": \"lot\",\n" +
            "        \"trade_item\": \"[trade_item]\",\n" +
            "        \"trade_item_id\": \"[trade_item_id]\",\n" +
            "        \"dispensing_unit\": \"[dispensing_unit]\",\n" +
            "        \"net_content\": \"12\",\n" +
            "        \"is_stock_issue\": \"true\",\n" +
            "        \"lot_status\": [\n" +
            "          \"VVM1\",\n" +
            "          \"VVM2\"\n" +
            "        ]\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"step3\": {\n" +
            "    \"title\": \"Review\",\n" +
            "    \"previous\": \"step2\",\n" +
            "    \"bottom_navigation\": \"true\",\n" +
            "    \"next_type\": \"submit\",\n" +
            "    \"next_label\": \"Submit\",\n" +
            "    \"next_enabled\": \"true\",\n" +
            "    \"no_padding\": true,\n" +
            "    \"fields\": [\n" +
            "      {\n" +
            "        \"key\": \"stockReview\",\n" +
            "        \"type\": \"review\",\n" +
            "        \"trade_item\": \"[trade_item]\",\n" +
            "        \"trade_item_id\": \"[trade_item_id]\",\n" +
            "        \"dispensing_unit\": \"[dispensing_unit]\",\n" +
            "        \"net_content\": \"12\",\n" +
            "        \"review_type\": \"Review Your Issue\",\n" +
            "        \"date\": \"Date_Stock_Issued\",\n" +
            "        \"facility\": \"Issued_Stock_To\",\n" +
            "        \"reason\": \"Issued_Stock_Reason\"\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "}";

    public static final String RECEIVE_JSON_FORM_DATA = "{\"count\":\"3\",\"encounter_type\":\"\",\"entity_id\":\"\",\"metadata\":{\"start\":{\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"concept\",\"openmrs_data_type\":\"start\",\"openmrs_entity_id\":\"163137AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"},\"end\":{\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"concept\",\"openmrs_data_type\":\"end\",\"openmrs_entity_id\":\"163138AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"},\"today\":{\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"encounter\",\"openmrs_entity_id\":\"encounter_date\"},\"deviceid\":{\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"concept\",\"openmrs_data_type\":\"deviceid\",\"openmrs_entity_id\":\"163149AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"},\"subscriberid\":{\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"concept\",\"openmrs_data_type\":\"subscriberid\",\"openmrs_entity_id\":\"163150AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"},\"simserial\":{\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"concept\",\"openmrs_data_type\":\"simserial\",\"openmrs_entity_id\":\"163151AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"},\"phonenumber\":{\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"concept\",\"openmrs_data_type\":\"phonenumber\",\"openmrs_entity_id\":\"163152AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"},\"encounter_location\":\"\"},\"step1\":{\"title\":\"Receive GSK BCG 20\",\"next\":\"step2\",\"bottom_navigation\":\"true\",\"fields\":[{\"key\":\"Date_Stock_Received\",\"type\":\"date_picker\",\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"\",\"openmrs_entity_id\":\"\",\"hint\":\"Date\",\"expanded\":false,\"min_date\":\"today-1m\",\"max_date\":\"today\",\"background\":\"#EAEAEA\",\"underline_color\":\"#EAEAEA\",\"v_required\":{\"value\":\"true\",\"err\":\"Enter the Stock Receipt date\"},\"value\":\"03-09-2018\"},{\"key\":\"labelRecieveFrom\",\"type\":\"label\",\"text\":\"Receive from *\",\"top_margin\":\"10dp\"},{\"key\":\"Receive_Stock_From\",\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"\",\"openmrs_entity_id\":\"\",\"openmrs_data_type\":\"select one\",\"type\":\"native_radio\",\"options\":[{\"key\":\"Comfort Health Clinic\",\"text\":\"Comfort Health Clinic\"},{\"key\":\"Balaka District Warehouse\",\"text\":\"Balaka District Warehouse\"}],\"value\":\"Balaka District Warehouse\",\"v_required\":{\"value\":true,\"err\":\"Please select where stock was received from\"}},{\"key\":\"labelReason\",\"type\":\"label\",\"text\":\"Reason *\",\"top_margin\":\"10dp\"},{\"key\":\"Receive_Stock_Reason\",\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"\",\"openmrs_entity_id\":\"\",\"openmrs_data_type\":\"select one\",\"type\":\"native_radio\",\"options\":[{\"key\":\"Receipts\",\"text\":\"Receipts\"},{\"key\":\"Transfer In\",\"text\":\"Transfer In\"},{\"key\":\"Beginning Balance Excess\",\"text\":\"Beginning Balance Excess\"},{\"key\":\"Other\",\"text\":\"Other\"}],\"value\":\"Receipts\",\"v_required\":{\"value\":true,\"err\":\"Please select reason stock was received\"}},{\"key\":\"Receive_Stock_Reason_Other\",\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"\",\"openmrs_entity_id\":\"\",\"type\":\"edit_text\",\"hint\":\"Please specify: *\",\"edit_type\":\"name\",\"v_required\":{\"value\":true,\"err\":\"Please specify reason stock was received\"},\"relevance\":{\"step1:Receive_Stock_Reason\":{\"type\":\"string\",\"ex\":\"equalTo(., \\\"Other\\\")\"}},\"value\":\"\"}]},\"step2\":{\"title\":\"Receive GSK BCG 20\",\"previous\":\"step1\",\"next\":\"step3\",\"next_label\":\"Review\",\"bottom_navigation\":\"true\",\"fields\":[{\"key\":\"stockLots\",\"type\":\"lot\",\"trade_item\":\"GSK BCG 20\",\"trade_item_id\":\"0cf8b2f4-3e1b-4d35-b839-5a4268ef03d6\",\"dispensing_unit\":\"vials\",\"net_content\":\"4\",\"lot_status\":[\"VVM1\",\"VVM2\"],\"value\":\"[{\\\"lotCodeAndExpiry\\\":\\\"LC1265 Exp. 21-09-2018\\\",\\\"lotId\\\":\\\"7c6d239f-0bbc-4cab-b218-888d8be89d24\\\",\\\"lotStatus\\\":\\\"VVM1\\\",\\\"quantity\\\":12},{\\\"lotCodeAndExpiry\\\":\\\"LC8063 Exp. 06-10-2018\\\",\\\"lotId\\\":\\\"9da34cac-4753-4763-a749-10741cdcce33\\\",\\\"lotStatus\\\":\\\"VVM2\\\",\\\"quantity\\\":3}]\",\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"\",\"openmrs_entity_id\":\"\"}]},\"step3\":{\"title\":\"Review\",\"previous\":\"step2\",\"bottom_navigation\":\"true\",\"next_type\":\"submit\",\"next_label\":\"Submit\",\"no_padding\":true,\"fields\":[{\"key\":\"stockReview\",\"type\":\"review\",\"trade_item\":\"GSK BCG 20\",\"trade_item_id\":\"0cf8b2f4-3e1b-4d35-b839-5a4268ef03d6\",\"dispensing_unit\":\"vials\",\"net_content\":\"4\",\"review_type\":\"Review Your Receipt\",\"date\":\"Date_Stock_Received\",\"facility\":\"Receive_Stock_From\",\"reason\":\"Receive_Stock_Reason\"}]}}";

    public static final String ISSUE_JSON_FORM_DATA = "{\"count\":\"3\",\"encounter_type\":\"\",\"entity_id\":\"\",\"metadata\":{\"start\":{\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"concept\",\"openmrs_data_type\":\"start\",\"openmrs_entity_id\":\"163137AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"},\"end\":{\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"concept\",\"openmrs_data_type\":\"end\",\"openmrs_entity_id\":\"163138AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"},\"today\":{\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"encounter\",\"openmrs_entity_id\":\"encounter_date\"},\"deviceid\":{\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"concept\",\"openmrs_data_type\":\"deviceid\",\"openmrs_entity_id\":\"163149AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"},\"subscriberid\":{\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"concept\",\"openmrs_data_type\":\"subscriberid\",\"openmrs_entity_id\":\"163150AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"},\"simserial\":{\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"concept\",\"openmrs_data_type\":\"simserial\",\"openmrs_entity_id\":\"163151AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"},\"phonenumber\":{\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"concept\",\"openmrs_data_type\":\"phonenumber\",\"openmrs_entity_id\":\"163152AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"},\"encounter_location\":\"\"},\"step1\":{\"title\":\"Issue GSK BCG 20\",\"next\":\"step2\",\"bottom_navigation\":\"true\",\"fields\":[{\"key\":\"Date_Stock_Issued\",\"type\":\"date_picker\",\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"\",\"openmrs_entity_id\":\"\",\"hint\":\"Date\",\"expanded\":false,\"min_date\":\"today-1m\",\"max_date\":\"today\",\"background\":\"#EAEAEA\",\"underline_color\":\"#EAEAEA\",\"v_required\":{\"value\":\"true\",\"err\":\"Enter the Stock Issue date\"},\"value\":\"03-09-2018\"},{\"key\":\"labelIssuedTo\",\"type\":\"label\",\"text\":\"Issued to *\",\"top_margin\":\"10dp\"},{\"key\":\"Issued_Stock_To\",\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"\",\"openmrs_entity_id\":\"\",\"openmrs_data_type\":\"select one\",\"type\":\"native_radio\",\"options\":[{\"key\":\"Comfort Health Clinic\",\"text\":\"Comfort Health Clinic\"},{\"key\":\"Balaka District Warehouse\",\"text\":\"Balaka District Warehouse\"}],\"value\":\"Comfort Health Clinic\",\"v_required\":{\"value\":true,\"err\":\"Please select where stock was issued\"}},{\"key\":\"labelReason\",\"type\":\"label\",\"text\":\"Reason *\",\"top_margin\":\"10dp\"},{\"key\":\"Issued_Stock_Reason\",\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"\",\"openmrs_entity_id\":\"\",\"openmrs_data_type\":\"select one\",\"type\":\"native_radio\",\"options\":[{\"key\":\"Consumed\",\"text\":\"Consumed\"},{\"key\":\"Transfer Out\",\"text\":\"Transfer Out\"},{\"key\":\"Damage\",\"text\":\"Damage\"},{\"key\":\"Other\",\"text\":\"Other\"}],\"value\":\"Consumed\",\"v_required\":{\"value\":true,\"err\":\"Please select reason stock was issued\"}},{\"key\":\"Issued_Stock_Reason_Other\",\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"\",\"openmrs_entity_id\":\"\",\"type\":\"edit_text\",\"hint\":\"Please specify: *\",\"edit_type\":\"name\",\"v_required\":{\"value\":true,\"err\":\"Please specify reason stock was issued\"},\"relevance\":{\"step1:Issued_Stock_Reason\":{\"type\":\"string\",\"ex\":\"equalTo(., \\\"Other\\\")\"}},\"value\":\"\"}]},\"step2\":{\"title\":\"Issue GSK BCG 20\",\"previous\":\"step1\",\"next\":\"step3\",\"next_label\":\"Review\",\"bottom_navigation\":\"true\",\"fields\":[{\"key\":\"stockLots\",\"type\":\"lot\",\"trade_item\":\"GSK BCG 20\",\"trade_item_id\":\"0cf8b2f4-3e1b-4d35-b839-5a4268ef03d6\",\"dispensing_unit\":\"vials\",\"net_content\":\"4\",\"is_stock_issue\":\"true\",\"lot_status\":[\"VVM1\",\"VVM2\"],\"value\":\"[{\\\"lotCodeAndExpiry\\\":\\\"LC1265 Exp. 21-09-2018\\\",\\\"lotId\\\":\\\"7c6d239f-0bbc-4cab-b218-888d8be89d24\\\",\\\"lotStatus\\\":\\\"VVM2\\\",\\\"quantity\\\":7}]\",\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"\",\"openmrs_entity_id\":\"\"}]},\"step3\":{\"title\":\"Review\",\"previous\":\"step2\",\"bottom_navigation\":\"true\",\"next_type\":\"submit\",\"next_label\":\"Submit\",\"no_padding\":true,\"fields\":[{\"key\":\"stockReview\",\"type\":\"review\",\"trade_item\":\"GSK BCG 20\",\"trade_item_id\":\"0cf8b2f4-3e1b-4d35-b839-5a4268ef03d6\",\"dispensing_unit\":\"vials\",\"net_content\":\"4\",\"review_type\":\"Review Your Issue\",\"date\":\"Date_Stock_Issued\",\"facility\":\"Issued_Stock_To\",\"reason\":\"Issued_Stock_Reason\"}]}}\n";

    public static final String LOT_WIDGET_JSON = "{\n" +
            "        \"key\": \"stockLots\",\n" +
            "        \"type\": \"lot\",\n" +
            "        \"trade_item\": \"GSK BCG 20\",\n" +
            "        \"trade_item_id\": \"0cf8b2f4-3e1b-4d35-b839-5a4268ef03d6\",\n" +
            "        \"dispensing_unit\": \"vials\",\n" +
            "        \"net_content\": \"4\",\n" +
            "        \"lot_status\": [\n" +
            "          \"VVM1\",\n" +
            "          \"VVM2\"\n" +
            "        ],\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\"\n" +
            "      }";


    public static final String LOT_WIDGET_TWO_EXISTING_JSON = "{\n" +
            "        \"key\": \"stockLots\",\n" +
            "        \"type\": \"lot\",\n" +
            "        \"trade_item\": \"GSK BCG 20\",\n" +
            "        \"trade_item_id\": \"0cf8b2f4-3e1b-4d35-b839-5a4268ef03d6\",\n" +
            "        \"dispensing_unit\": \"vials\",\n" +
            "        \"net_content\": \"4\",\n" +
            "        \"lot_status\": [\n" +
            "          \"VVM1\",\n" +
            "          \"VVM2\"\n" +
            "        ],\n" +
            "        \"value\": [\n" +
            "          {\n" +
            "            \"lotCodeAndExpiry\": \"LC1265 Exp. 21-09-2018\",\n" +
            "            \"lotId\": \"7c6d239f-0bbc-4cab-b218-888d8be89d24\",\n" +
            "            \"lotStatus\": \"VVM1\",\n" +
            "            \"quantity\": 12\n" +
            "          },\n" +
            "          {\n" +
            "            \"lotCodeAndExpiry\": \"LC8063 Exp. 06-10-2018\",\n" +
            "            \"lotId\": \"9da34cac-4753-4763-a749-10741cdcce33\",\n" +
            "            \"lotStatus\": \"VVM2\",\n" +
            "            \"quantity\": 3\n" +
            "          }\n" +
            "        ],\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\"\n" +
            "      }";

    public static final String LOT_WIDGET_ONE_EXISTING_JSON = "{\n" +
            "        \"key\": \"stockLots\",\n" +
            "        \"type\": \"lot\",\n" +
            "        \"trade_item\": \"GSK BCG 20\",\n" +
            "        \"trade_item_id\": \"0cf8b2f4-3e1b-4d35-b839-5a4268ef03d6\",\n" +
            "        \"dispensing_unit\": \"vials\",\n" +
            "        \"net_content\": \"4\",\n" +
            "        \"lot_status\": [\n" +
            "          \"VVM1\",\n" +
            "          \"VVM2\"\n" +
            "        ],\n" +
            "        \"value\": [\n" +
            "          {\n" +
            "            \"lotCodeAndExpiry\": \"LC1265 Exp. 21-09-2018\",\n" +
            "            \"lotId\": \"7c6d239f-0bbc-4cab-b218-888d8be89d24\",\n" +
            "            \"lotStatus\": \"VVM1\",\n" +
            "            \"quantity\": 12\n" +
            "          }\n" +
            "        ],\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\"\n" +
            "      }";

    public static final String REVIEW_WIDGET_JSON = "{\"key\":\"stockReview\",\"type\":\"review\",\"trade_item\":\"GSK BCG 20\",\"trade_item_id\":\"0cf8b2f4-3e1b-4d35-b839-5a4268ef03d6\",\"dispensing_unit\":\"vials\",\"net_content\":\"4\",\"review_type\":\"Review Your Receipt\",\"date\":\"Date_Stock_Received\",\"facility\":\"Receive_Stock_From\",\"reason\":\"Receive_Stock_Reason\"}]}";

    public static final String ADJUST_WIDGET_JSON = "{\"key\":\"stockLots\",\"type\":\"lot\",\"trade_item\":\"Intervax BCG 20\",\"trade_item_id\":\"0cf8b2f4-3e1b-4d35-b839-5a4268ef03d6\",\"dispensing_unit\":\"vials\",\"net_content\":\"6\",\"is_stock_adjustment\":\"true\",\"lot_status\":[\"VVM1\",\"VVM2\"]}";
    public static final String ADJUST_WIDGET_FORM_DATA = "{\"count\":\"1\",\"encounter_type\":\"\",\"entity_id\":\"\",\"metadata\":{\"start\":{\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"concept\",\"openmrs_data_type\":\"start\",\"openmrs_entity_id\":\"163137AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"},\"end\":{\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"concept\",\"openmrs_data_type\":\"end\",\"openmrs_entity_id\":\"163138AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"},\"today\":{\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"encounter\",\"openmrs_entity_id\":\"encounter_date\"},\"deviceid\":{\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"concept\",\"openmrs_data_type\":\"deviceid\",\"openmrs_entity_id\":\"163149AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"},\"subscriberid\":{\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"concept\",\"openmrs_data_type\":\"subscriberid\",\"openmrs_entity_id\":\"163150AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"},\"simserial\":{\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"concept\",\"openmrs_data_type\":\"simserial\",\"openmrs_entity_id\":\"163151AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"},\"phonenumber\":{\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"concept\",\"openmrs_data_type\":\"phonenumber\",\"openmrs_entity_id\":\"163152AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"},\"encounter_location\":\"\"},\"step1\":{\"title\":\"Intervax BCG 20 loss\\/adjustment\",\"next_type\":\"submit\",\"next_label\":\"Save\",\"bottom_navigation\":\"true\",\"no_padding\":true,\"fields\":[{\"key\":\"stockLots\",\"type\":\"lot\",\"trade_item\":\"Intervax BCG 20\",\"trade_item_id\":\"894a3458-6c8c-4a51-8278-9167d6c9422d\",\"dispensing_unit\":\"vials\",\"net_content\":\"6\",\"is_stock_adjustment\":\"true\",\"lot_status\":[\"VVM1\",\"VVM2\"],\"value\":\"[{\\\"lotCodeAndExpiry\\\":\\\"LC2833 Exp. 21-03-2019\\\",\\\"lotId\\\":\\\"fa06cf2c-2511-4680-86a9-129575910bf6\\\",\\\"lotStatus\\\":\\\"VVM1\\\",\\\"quantity\\\":-2,\\\"reason\\\":\\\"Transferred\\\"}]\",\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"\",\"openmrs_entity_id\":\"\"}]}}";
}
