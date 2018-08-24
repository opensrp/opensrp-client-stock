package org.smartregister.stock.management.intent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.smartregister.stock.management.application.Application;
import org.smartregister.stock.openlmis.OpenLMISLibrary;
import org.smartregister.stock.openlmis.intent.OrderableSyncIntentService;
import org.smartregister.stock.openlmis.repository.TradeItemRepository;
import org.smartregister.stock.openlmis.repository.openlmis.OrderableRepository;

import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.smartregister.stock.management.util.ServiceUtils.startService;
import static org.smartregister.stock.management.util.ServiceUtils.stopService;
import static org.smartregister.stock.openlmis.util.Utils.DATABASE_NAME;

public class OrderableSyncIntentServiceTest extends BaseSyncIntentServiceTest {


    OrderableRepository orderableRepository = OpenLMISLibrary.getInstance().getOrderableRepository();
    TradeItemRepository tradeItemRegisterRepository = OpenLMISLibrary.getInstance().getTradeItemRegisterRepository();

    @Before
    @Override
    public void setUp() {
        super.setUp();
        startService(Application.getInstance().getApplicationContext(), OrderableSyncIntentService.class);
    }

    @After
    public void tearDown() {
        stopService(Application.getInstance().getApplicationContext(), OrderableSyncIntentService.class);
    }

    @Test
    public void testOrderablesAreSyncedAndSaved() {
        // this assumes that a lot with an id value of "id" has been posted to the lots endpoint
        try {
            TimeUnit.SECONDS.sleep(5);
            // assert orderable table is populated
            assertNotNull(orderableRepository.findOrderable("id"));
            assertNotNull(orderableRepository.findOrderable("id_1"));
            assertNotNull(orderableRepository.findOrderable("id_2"));
            // assert TradeItem register table is populated
            assertTrue(tradeItemRegisterRepository.tradeItemExists("trade_item"));
            assertTrue(tradeItemRegisterRepository.tradeItemExists("trade_item_1"));
            assertTrue(tradeItemRegisterRepository.tradeItemExists("trade_item_2"));
        } catch (InterruptedException e) {
            fail("Waiting for the worker thread took too long.");
        }
    }
}
