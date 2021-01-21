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
}
