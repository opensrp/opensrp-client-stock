package org.smartregister.stock.management.intent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.smartregister.stock.management.application.Application;
import org.smartregister.stock.openlmis.OpenLMISLibrary;
import org.smartregister.stock.openlmis.intent.OpenLMISStockSyncIntentService;
import org.smartregister.stock.openlmis.repository.StockRepository;

import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.smartregister.stock.management.util.ServiceUtils.startService;
import static org.smartregister.stock.management.util.ServiceUtils.stopService;

public class OpenLMISStockSyncIntentServiceTest extends BaseSyncIntentServiceTest {

    StockRepository repository = OpenLMISLibrary.getInstance().getStockRepository();

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
    public void testOpenLMISStocksAreSyncedAndSaved() {
        // this assumes that stock with corresponding id value has been posted to the stock endpoint
        try {
            TimeUnit.SECONDS.sleep(5);
            assertEquals(1, repository.findUniqueStock("trade_item_id", "debit", "provider_id", "1", "1", "to_from").size());
            assertEquals(1, repository.findUniqueStock("trade_item_id_1", "debit", "provider_id_1", "1", "1", "to_from").size());
            assertEquals(1, repository.findUniqueStock("trade_item_id_2", "debit", "provider_id_2", "1", "1", "to_from").size());
        } catch (InterruptedException e) {
            fail("Waiting for the worker thread took too long.");
        }
    }
}
