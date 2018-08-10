package org.smartregister.stock.management.intent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;
import org.smartregister.stock.management.application.Application;
import org.smartregister.stock.openlmis.OpenLMISLibrary;
import org.smartregister.stock.openlmis.intent.LotSyncIntentService;
import org.smartregister.stock.openlmis.repository.openlmis.LotRepository;

import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.fail;
import static org.smartregister.stock.management.util.ServiceUtils.startService;
import static org.smartregister.stock.management.util.ServiceUtils.stopService;

@RunWith(JUnit4ClassRunner.class)
public class LotSyncIntentServiceTest {

    LotRepository repository = OpenLMISLibrary.getInstance().getLotRepository();

    @Before
    public void setup() {
        startService(Application.getInstance().getApplicationContext(), LotSyncIntentService.class);
    }

    @After
    public void tearDown() {
        stopService(Application.getInstance().getApplicationContext(), LotSyncIntentService.class);
    }

    @Test
    public void testLotsAreSyncedAndSaved() {
        try {
            TimeUnit.SECONDS.sleep(5);
            assertNotNull(repository.findLotById("id"));
        } catch (InterruptedException e) {
            fail("Waiting for the worker thread took too long.");
        }
    }
}