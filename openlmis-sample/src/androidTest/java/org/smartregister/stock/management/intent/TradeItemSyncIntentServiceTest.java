package org.smartregister.stock.management.intent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.smartregister.stock.management.application.Application;
import org.smartregister.stock.openlmis.OpenLMISLibrary;
import org.smartregister.stock.openlmis.intent.TradeItemSyncIntentService;
import org.smartregister.stock.openlmis.repository.openlmis.TradeItemRepository;

import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.smartregister.stock.management.util.ServiceUtils.startService;
import static org.smartregister.stock.management.util.ServiceUtils.stopService;
import static org.smartregister.stock.openlmis.util.Utils.DATABASE_NAME;

public class TradeItemSyncIntentServiceTest extends BaseSyncIntentServiceTest {

    TradeItemRepository repository = OpenLMISLibrary.getInstance().getTradeItemRepository();

    @Before
    @Override
    public void setUp() {
        super.setUp();
        startService(Application.getInstance().getApplicationContext(), TradeItemSyncIntentService.class);
    }

    @After
    public void tearDown() {
        stopService(Application.getInstance().getApplicationContext(), TradeItemSyncIntentService.class);
    }

    @Test
    public void testTradeItemsAreSyncedAndSaved() {
        // this assumes that a tradeItem with an id value of "id" has been posted to the tradeItems endpoint
        try {
            TimeUnit.SECONDS.sleep(5);
            assertEquals(1, repository.findTradeItems("identifier_1", "859245888", "manufactuter_1").size());
            assertEquals(1, repository.findTradeItems("identifier_2", "859245888", "manufactuter_2").size());
            assertEquals(1, repository.findTradeItems("identifier_3", "859245888", "manufactuter_3").size());
        } catch (InterruptedException e) {
            fail("Waiting for the worker thread took too long.");
        }
    }
}
