package org.smartregister.stock.management.intent;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.smartregister.stock.management.application.Application;
import org.smartregister.stock.openlmis.OpenLMISLibrary;
import org.smartregister.stock.openlmis.intent.service.OpenLMISStockSyncIntentService;
import org.smartregister.stock.openlmis.repository.StockRepository;
import org.smartregister.stock.openlmis.util.Utils;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.smartregister.stock.management.util.ServiceUtils.startService;
import static org.smartregister.stock.management.util.ServiceUtils.stopService;

public class OpenLMISStockSyncIntentServiceTest extends BaseSyncIntentServiceTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    private StockRepository repository = OpenLMISLibrary.getInstance().getStockRepository();

    @Before
    @Override
    public void setUp() {
        super.setUp();
        startService(Application.getInstance().getApplicationContext(), OpenLMISStockSyncIntentService.class);
    }

    @After
    public void tearDown() {
        stopService(Application.getInstance().getApplicationContext(), OpenLMISStockSyncIntentService.class);
        repository.getReadableDatabase().close();
    }

    @Test
    public void testOpenLMISStocksAreSyncedDownwardAndSaved() {
        mockStatic(Utils.class);
        when(Utils.makeGetRequest(anyString())).thenReturn(responseString());
        assertEquals(1, repository.findUniqueStock("trade_item_id", "debit", "provider_id", "1", "1", "to_from").size());
        assertEquals(1, repository.findUniqueStock("trade_item_id_1", "debit", "provider_id_1", "1", "1", "to_from").size());
        assertEquals(1, repository.findUniqueStock("trade_item_id_2", "debit", "provider_id_2", "1", "1", "to_from").size());
    }

    private String responseString() {
        return "{\n" +
                "    \"stocks\": [\n" +
                "        {\n" +
                "            \"identifier\": 1,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536064794036,\n" +
                "            \"dateCreated\": \"2018-09-04T15:39:54.039+03:00\",\n" +
                "            \"serverVersion\": 1536064794036,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"c7a7ac8e-fcd3-4cdd-952d-84ed594e2778\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 2,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536064794038,\n" +
                "            \"dateCreated\": \"2018-09-04T15:39:54.287+03:00\",\n" +
                "            \"serverVersion\": 1536064794038,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"49e75fb1-e69d-49ea-9f5f-e459845a7397\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 3,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536064794038,\n" +
                "            \"dateCreated\": \"2018-09-04T15:39:54.395+03:00\",\n" +
                "            \"serverVersion\": 1536064794038,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"9d35a82f-de67-4e04-8e36-769e96fef814\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
    }
}
