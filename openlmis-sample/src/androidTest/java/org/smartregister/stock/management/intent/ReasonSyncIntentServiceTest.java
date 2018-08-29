package org.smartregister.stock.management.intent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.smartregister.stock.management.application.Application;
import org.smartregister.stock.openlmis.OpenLMISLibrary;
import org.smartregister.stock.openlmis.intent.ReasonSyncIntentService;
import org.smartregister.stock.openlmis.repository.openlmis.ReasonRepository;

import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.smartregister.stock.management.util.ServiceUtils.startService;
import static org.smartregister.stock.management.util.ServiceUtils.stopService;
import static org.smartregister.stock.openlmis.util.Utils.DATABASE_NAME;

public class ReasonSyncIntentServiceTest extends BaseSyncIntentServiceTest {

    ReasonRepository repository = OpenLMISLibrary.getInstance().getReasonRepository();

    @Before
    @Override
    public void setUp() {
        super.setUp();
        startService(Application.getInstance().getApplicationContext(), ReasonSyncIntentService.class);
    }

    @After
    public void tearDown() {
        stopService(Application.getInstance().getApplicationContext(), ReasonSyncIntentService.class);
        repository.getReadableDatabase().close();
    }

    @Test
    public void testReasonsAreSyncedAndSaved() {
        // this assumes that a reason with an id value of "id" has been posted to the reasons endpoint
        try {
            TimeUnit.SECONDS.sleep(5);
            assertEquals(1, repository.findReasons("id", "name", "program_id").size());
            assertEquals(1, repository.findReasons("id_1", "name", "program_id_1").size());
            assertEquals(1, repository.findReasons("id_2", "name", "program_id_2").size());
        } catch (InterruptedException e) {
            fail("Waiting for the worker thread took too long.");
        }
    }
}
