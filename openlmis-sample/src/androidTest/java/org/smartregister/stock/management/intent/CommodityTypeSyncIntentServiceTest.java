package org.smartregister.stock.management.intent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.smartregister.stock.management.application.Application;
import org.smartregister.stock.openlmis.OpenLMISLibrary;
import org.smartregister.stock.openlmis.intent.CommodityTypeSyncIntentService;
import org.smartregister.stock.openlmis.repository.openlmis.CommodityTypeRepository;

import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.smartregister.stock.management.util.ServiceUtils.startService;
import static org.smartregister.stock.management.util.ServiceUtils.stopService;

public class CommodityTypeSyncIntentServiceTest {

    CommodityTypeRepository repository = OpenLMISLibrary.getInstance().getCommodityTypeRepository();

    @Before
    public void setup() {
        startService(Application.getInstance().getApplicationContext(), CommodityTypeSyncIntentService.class);
    }

    @After
    public void tearDown() {
        stopService(Application.getInstance().getApplicationContext(), CommodityTypeSyncIntentService.class);
    }

    @Test
    public void testCommodityTypesAreSyncedAndSaved() {
        // this assumes that a commodityType with an id value of "id" has been posted to the commodityTypes endpoint
        try {
            TimeUnit.SECONDS.sleep(5);
            assertEquals(1, repository.findCommodityTypes("identifier", "commodity_name", "parent_id", "classification_system", "classification_id").size());
            assertEquals(1, repository.findCommodityTypes("identifier_1", "commodity_name_1", "parent_id_1", "classification_system_1", "classification_id_1").size());
            assertEquals(1, repository.findCommodityTypes("identifier_2", "commodity_name_2", "parent_id_2", "classification_system_2", "classification_id_2").size());
        } catch (InterruptedException e) {
            fail("Waiting for the worker thread took too long.");
        }
    }
}
