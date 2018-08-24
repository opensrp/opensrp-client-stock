package org.smartregister.stock.management.intent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.smartregister.stock.management.application.Application;
import org.smartregister.stock.openlmis.OpenLMISLibrary;
import org.smartregister.stock.openlmis.intent.ProgramSyncIntentService;
import org.smartregister.stock.openlmis.repository.openlmis.ProgramRepository;

import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.smartregister.stock.management.util.ServiceUtils.startService;
import static org.smartregister.stock.management.util.ServiceUtils.stopService;
import static org.smartregister.stock.openlmis.util.Utils.DATABASE_NAME;

public class ProgramSyncIntentServiceTest extends BaseSyncIntentServiceTest {

    ProgramRepository repository = OpenLMISLibrary.getInstance().getProgramRepository();

    @Before
    @Override
    public void setUp() {
        super.setUp();
        startService(Application.getInstance().getApplicationContext(), ProgramSyncIntentService.class);
    }

    @After
    public void tearDown() {
        stopService(Application.getInstance().getApplicationContext(), ProgramSyncIntentService.class);
        repository.getReadableDatabase().close();
    }

    @Test
    public void testProgramsAreSyncedAndSaved() {
        // this assumes that a program with an id value of "id" has been posted to the programs endpoint
        try {
            TimeUnit.SECONDS.sleep(5);
            assertEquals(1, repository.findPrograms("identifier", "code", "program_name", "1").size());
            assertEquals(1, repository.findPrograms("identifier_1", "code_1", "program_name_1", "1").size());
            assertEquals(1, repository.findPrograms("identifier_2", "code_2", "program_name_2", "1").size());
        } catch (InterruptedException e) {
            fail("Waiting for the worker thread took too long.");
        }
    }
}
