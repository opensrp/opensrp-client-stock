package org.smartregister.stock.management.intent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.smartregister.stock.management.application.Application;
import org.smartregister.stock.openlmis.OpenLMISLibrary;
import org.smartregister.stock.openlmis.intent.TradeItemClassificationSyncIntentService;
import org.smartregister.stock.openlmis.repository.openlmis.TradeItemClassificationRepository;

import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.smartregister.stock.management.util.ServiceUtils.startService;
import static org.smartregister.stock.management.util.ServiceUtils.stopService;
import static org.smartregister.stock.openlmis.util.Utils.DATABASE_NAME;

public class TradeItemClassificationSyncIntentServiceTest extends BaseSyncIntentServiceTest {

        TradeItemClassificationRepository repository = OpenLMISLibrary.getInstance().getTradeItemClassificationRepository();

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
            // this assumes that a lot with an id value of "id" has been posted to the lots endpoint
            try {
                TimeUnit.SECONDS.sleep(5);
                assertEquals(1, repository.findTradeItemClassifications("identifier", "trade_item", "classification_system", "classification_id").size());
                assertEquals(1, repository.findTradeItemClassifications("identifier_1", "trade_item_1", "classification_system_1", "classification_id_1").size());
                assertEquals(1, repository.findTradeItemClassifications("identifier_2", "trade_item_2", "classification_system_2", "classification_id_2").size());
            } catch (InterruptedException e) {
                fail("Waiting for the worker thread took too long.");
            }
        }
}
