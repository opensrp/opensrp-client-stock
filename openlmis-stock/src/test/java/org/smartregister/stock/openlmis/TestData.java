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
}
