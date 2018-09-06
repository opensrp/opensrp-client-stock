package org.smartregister.stock.management.intent;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.smartregister.stock.management.application.Application;
import org.smartregister.stock.openlmis.OpenLMISLibrary;
import org.smartregister.stock.openlmis.intent.TradeItemClassificationSyncIntentService;
import org.smartregister.stock.openlmis.repository.openlmis.TradeItemClassificationRepository;
import org.smartregister.stock.openlmis.util.Utils;

import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.smartregister.stock.management.util.ServiceUtils.startService;
import static org.smartregister.stock.management.util.ServiceUtils.stopService;

public class TradeItemClassificationSyncIntentServiceTest extends BaseSyncIntentServiceTest {

        @Rule
        public MockitoRule rule = MockitoJUnit.rule();

        private TradeItemClassificationRepository repository = OpenLMISLibrary.getInstance().getTradeItemClassificationRepository();

        @Before
        @Override
        public void setUp() {
            super.setUp();
            startService(Application.getInstance().getApplicationContext(), TradeItemClassificationSyncIntentService.class);
        }

        @After
        public void tearDown() {
            stopService(Application.getInstance().getApplicationContext(), TradeItemClassificationSyncIntentService.class);
            repository.getReadableDatabase().close();
        }

        @Test
        public void testTradeItemClassificationsAreSyncedAndSaved() {
            mockStatic(Utils.class);
            when(Utils.makeGetRequest(anyString())).thenReturn(responseString());
            assertEquals(1, repository.findTradeItemClassifications("identifier", "trade_item", "classification_system", "classification_id").size());
            assertEquals(1, repository.findTradeItemClassifications("identifier_1", "trade_item_1", "classification_system_1", "classification_id_1").size());
            assertEquals(1, repository.findTradeItemClassifications("identifier_2", "trade_item_2", "classification_system_2", "classification_id_2").size());
        }

        private String responseString() {
            return "[{\n" +
                    "    \"id\": \"identifier\",\n" +
                    "    \"serverVersion\": 833805170125239,\n" +
                    "    \"tradeItem\": {\n" +
                    "        \"id\": \"trade_item\",\n" +
                    "        \"serverVersion\": 0,\n" +
                    "        \"gtin\": null,\n" +
                    "        \"manufacturerOfTradeItem\": null,\n" +
                    "        \"classifications\": null\n" +
                    "    },\n" +
                    "    \"classificationSystem\": \"classification_system\",\n" +
                    "    \"classificationId\": \"classification_id\"\n" +
                    "}, {\n" +
                    "    \"id\": \"identifier_1\",\n" +
                    "    \"serverVersion\": 833805353780491,\n" +
                    "    \"tradeItem\": {\n" +
                    "        \"id\": \"trade_item_1\",\n" +
                    "        \"serverVersion\": 0,\n" +
                    "        \"gtin\": null,\n" +
                    "        \"manufacturerOfTradeItem\": null,\n" +
                    "        \"classifications\": null\n" +
                    "    },\n" +
                    "    \"classificationSystem\": \"classification_system_1\",\n" +
                    "    \"classificationId\": \"classification_id_1\"\n" +
                    "}]";
        }
}
