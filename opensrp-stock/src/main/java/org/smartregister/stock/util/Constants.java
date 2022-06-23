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
    public static final String MEDIA_URL = "multimedia/media";
    public static final String PRODUCT_IMAGE = "product_image";
    public static final String LAST_STOCK_TYPE_SYNC = "last_stock_type_sync";

    public interface StockResponseKey {
        String ID = "id";
        String CUSTOM_PROPERTIES = "customProperties";
        String SERVER_VERSION = "serverVersion";
        String TYPE = "type";
        String VERSION = "version";
        String DONOR = "donor";
        String ACCOUNTABILITY_END_DATE = "accountabilityEndDate";
        String SERIAL_NUMBER = "serialNumber";
        String IDENTIFIER = "identifier";
        String LOCATION_ID = "locationId";
        String LOCATIONS = "locations";
    }

    public static class FormKey {
        public static final String DOSES_WASTED_NOTE = "Doses_Wasted_Note";
        public static final String CHILDREN_VACCINATED_COUNT = "Children_Vaccinated_Count";
        public static final String VIALS_ISSUED_COUNT = "Vials_Issued_Count";
        public static final String DOSES_WASTED = "Doses_wasted";
        public static final String VIALS_BALANCE = "Vials_Balance";
        public static final String BALANCE = "Balance";
    }

    public static class AppProperties {
        public static final String USE_ONLY_DOSES_FOR_CALCULATION = "use.only.doses.for.calculation";
    }
}