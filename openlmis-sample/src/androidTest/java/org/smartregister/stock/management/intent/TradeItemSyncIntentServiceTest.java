package org.smartregister.stock.management.intent;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.smartregister.stock.management.application.Application;
import org.smartregister.stock.openlmis.OpenLMISLibrary;
import org.smartregister.stock.openlmis.intent.service.TradeItemSyncIntentService;
import org.smartregister.stock.openlmis.repository.openlmis.TradeItemRepository;
import org.smartregister.stock.openlmis.util.Utils;

import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.smartregister.stock.management.util.ServiceUtils.startService;
import static org.smartregister.stock.management.util.ServiceUtils.stopService;

public class TradeItemSyncIntentServiceTest extends BaseSyncIntentServiceTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    private TradeItemRepository repository = OpenLMISLibrary.getInstance().getTradeItemRepository();

    @Before
    @Override
    public void setUp() {
        super.setUp();
        startService(Application.getInstance().getApplicationContext(), TradeItemSyncIntentService.class);
    }

    @After
    public void tearDown() {
        stopService(Application.getInstance().getApplicationContext(), TradeItemSyncIntentService.class);
        repository.getReadableDatabase().close();
    }

    @Test
    public void testTradeItemsAreSyncedAndSaved() {
        mockStatic(Utils.class);
        when(Utils.makeGetRequest(anyString())).thenReturn(responseString());
        assertEquals(1, repository.findTradeItems("identifier_1", "859245888", "manufactuter_1").size());
        assertEquals(1, repository.findTradeItems("identifier_2", "859245888", "manufactuter_2").size());
        assertEquals(1, repository.findTradeItems("identifier_3", "859245888", "manufactuter_3").size());
    }

    private String responseString() {
        return "[{\n" +
                "    \"uuid\": \"identifier\",\n" +
                "    \"gtin\": {\n" +
                "        \"gtin\": \"859245888\"\n" +
                "    },\n" +
                "    \"manufacturerOfTradeItem\": \"manufactuter\",\n" +
                "    \"classifications\": [{\n" +
                "        \"uuid\": \"identifier_1\",\n" +
                "        \"tradeItem\": null,\n" +
                "        \"classificationSystem\": \"classification_system\",\n" +
                "        \"classificationId\": \"classification_id\"\n" +
                "    }, {\n" +
                "        \"uuid\": \"identifier_2\",\n" +
                "        \"tradeItem\": null,\n" +
                "        \"classificationSystem\": \"classification_system\",\n" +
                "        \"classificationId\": \"classification_id\"\n" +
                "    }]\n" +
                "}, {\n" +
                "    \"uuid\": \"identifier_1\",\n" +
                "    \"gtin\": {\n" +
                "        \"gtin\": \"859245888\"\n" +
                "    },\n" +
                "    \"manufacturerOfTradeItem\": \"manufactuter_1\",\n" +
                "    \"classifications\": [{\n" +
                "        \"uuid\": \"identifier_1\",\n" +
                "        \"tradeItem\": null,\n" +
                "        \"classificationSystem\": \"classification_system\",\n" +
                "        \"classificationId\": \"classification_id\"\n" +
                "    }, {\n" +
                "        \"uuid\": \"identifier_2\",\n" +
                "        \"tradeItem\": null,\n" +
                "        \"classificationSystem\": \"classification_system\",\n" +
                "        \"classificationId\": \"classification_id\"\n" +
                "    }]\n" +
                "}]";
    }
}
