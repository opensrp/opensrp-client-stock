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
import static org.smartregister.stock.openlmis.util.Utils.DATABASE_NAME;

@RunWith(JUnit4ClassRunner.class)
public class LotSyncIntentServiceTest extends BaseSyncIntentServiceTest {

    LotRepository repository = OpenLMISLibrary.getInstance().getLotRepository();

    @Before
    @Override
    public void setUp() {
        super.setUp();
        startService(Application.getInstance().getApplicationContext(), LotSyncIntentService.class);
    }

    @After
    public void tearDown() {
        stopService(Application.getInstance().getApplicationContext(), LotSyncIntentService.class);
        repository.getReadableDatabase().close();
    }

    @Test
    public void testLotsAreSyncedAndSaved() {
        // this assumes that a lot with an id value of "id" has been posted to the lots endpoint
        try {
            TimeUnit.SECONDS.sleep(5);
            assertNotNull(repository.findLotById("id"));
            assertNotNull(repository.findLotById("id_1"));
            assertNotNull(repository.findLotById("id_2"));
        } catch (InterruptedException e) {
            fail("Waiting for the worker thread took too long.");
        }
    }
}