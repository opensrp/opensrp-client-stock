package org.smartregister.stock.management.intent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.smartregister.stock.management.application.Application;
import org.smartregister.stock.openlmis.OpenLMISLibrary;
import org.smartregister.stock.openlmis.intent.DispensableSyncIntentService;
import org.smartregister.stock.openlmis.repository.openlmis.DispensableRepository;

import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.smartregister.stock.management.util.ServiceUtils.startService;
import static org.smartregister.stock.management.util.ServiceUtils.stopService;

public class DispensableSyncIntentServiceTest {

    DispensableRepository repository = OpenLMISLibrary.getInstance().getDispensableRepository();

    @Before
    public void setup() {
        startService(Application.getInstance().getApplicationContext(), DispensableSyncIntentService.class);
    }

    @After
    public void tearDown() {
        stopService(Application.getInstance().getApplicationContext(), DispensableSyncIntentService.class);
    }

    @Test
    public void testDispensablesAreSyncedAndSaved() {
        // this assumes that a lot with an id value of "id" has been posted to the lots endpoint
        try {
            TimeUnit.SECONDS.sleep(5);
            assertEquals(1, repository.findDispensables("identifier", "dispensing_unit", "size_code", "route_of_administration").size());
            assertEquals(1, repository.findDispensables("identifier_1", "dispensing_unit_1", "size_code_1", "route_of_administration_1").size());
            assertEquals(1, repository.findDispensables("identifier_2", "dispensing_unit_2", "size_code_2", "route_of_administration_2").size());
        } catch (InterruptedException e) {
            fail("Waiting for the worker thread took too long.");
        }
    }
}
