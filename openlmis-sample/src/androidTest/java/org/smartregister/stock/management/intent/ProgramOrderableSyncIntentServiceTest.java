package org.smartregister.stock.management.intent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.smartregister.stock.management.application.Application;
import org.smartregister.stock.openlmis.OpenLMISLibrary;
import org.smartregister.stock.openlmis.intent.ProgramOrderableSyncIntentService;
import org.smartregister.stock.openlmis.repository.openlmis.ProgramOrderableRepository;

import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.smartregister.stock.management.util.ServiceUtils.startService;
import static org.smartregister.stock.management.util.ServiceUtils.stopService;

public class ProgramOrderableSyncIntentServiceTest {

    ProgramOrderableRepository repository = OpenLMISLibrary.getInstance().getProgramOrderableRepository();

    @Before
    public void setup() {
        startService(Application.getInstance().getApplicationContext(), ProgramOrderableSyncIntentService.class);
    }

    @After
    public void tearDown() {
        stopService(Application.getInstance().getApplicationContext(), ProgramOrderableSyncIntentService.class);
    }

    @Test
    public void testProgramOrderablesAreSyncedAndSaved() {
        // this assumes that a lot with an id value of "id" has been posted to the lots endpoint
        try {
            TimeUnit.SECONDS.sleep(5);
            assertEquals(1, repository.findProgramOrderables("id", "program_id", "orderable_id", "5", "1", "1").size());
            assertEquals(1, repository.findProgramOrderables("id_1", "program_id_1", "orderable_id_1", "5", "1", "1").size());
            assertEquals(1, repository.findProgramOrderables("id_2", "program_id_2", "orderable_id_2", "5", "1", "1").size());
        } catch (InterruptedException e) {
            fail("Waiting for the worker thread took too long.");
        }
    }
}
