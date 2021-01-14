package org.smartregister.stock.configuration;

import junit.framework.TestCase;

import org.json.JSONArray;
import org.junit.Before;
import org.smartregister.stock.util.Constants;

import java.util.HashMap;
import java.util.Map;

public class StockSyncConfigurationTest extends TestCase {

    private StockSyncConfiguration stockSyncConfiguration;

    @Before
    public void setUp() {
        stockSyncConfiguration = new StockSyncConfiguration();
    }

    public void testStockSyncRequestBody() {
        String[] locationsArr = new String[]{"2"};

        JSONArray jsonArray = new JSONArray();

        for (String s : locationsArr) {
            jsonArray.put(s);
        }

        Map<String, Object> syncParams = new HashMap<>();
        syncParams.put(Constants.StockResponseKey.SERVER_VERSION, 5);
        syncParams.put(Constants.StockResponseKey.LOCATIONS, jsonArray);

        assertEquals("{\"serverVersion\":5,\"locations\":[\"2\"]}", stockSyncConfiguration.stockSyncRequestBody(syncParams));
    }
}