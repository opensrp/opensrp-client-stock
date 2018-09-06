package org.smartregister.stock.management.intent;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.smartregister.stock.management.application.Application;
import org.smartregister.stock.openlmis.OpenLMISLibrary;
import org.smartregister.stock.openlmis.intent.DispensableSyncIntentService;
import org.smartregister.stock.openlmis.repository.openlmis.DispensableRepository;
import org.smartregister.stock.openlmis.util.Utils;

import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.smartregister.stock.management.util.ServiceUtils.startService;
import static org.smartregister.stock.management.util.ServiceUtils.stopService;

public class DispensableSyncIntentServiceTest extends  BaseSyncIntentServiceTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    private DispensableRepository repository = OpenLMISLibrary.getInstance().getDispensableRepository();

    @Before
    @Override
    public void setUp() {
        super.setUp();
        startService(Application.getInstance().getApplicationContext(), DispensableSyncIntentService.class);
    }

    @After
    public void tearDown() {
        stopService(Application.getInstance().getApplicationContext(), DispensableSyncIntentService.class);
        repository.getReadableDatabase().close();
    }

    @Test
    public void testDispensablesAreSyncedAndSaved() {
        mockStatic(Utils.class);
        when(Utils.makeGetRequest(anyString())).thenReturn(responseString());
        assertEquals(1, repository.findDispensables("identifier", "dispensing_unit", "size_code", "route_of_administration").size());
        assertEquals(1, repository.findDispensables("identifier_1", "dispensing_unit_1", "size_code_1", "route_of_administration_1").size());
        assertEquals(1, repository.findDispensables("identifier_2", "dispensing_unit_2", "size_code_2", "route_of_administration_2").size());
    }

    private String responseString() {
        return "[{\n" +
                "    \"id\": \"identifier\",\n" +
                "    \"serverVersion\": 829120707254405,\n" +
                "    \"keyDispensingUnit\": \"dispensing_unit\",\n" +
                "    \"keySizeCode\": \"size_code\",\n" +
                "    \"keyRouteOfAdministration\": \"route_of_administration\"\n" +
                "}, {\n" +
                "    \"id\": \"identifier_1\",\n" +
                "    \"serverVersion\": 829120891794093,\n" +
                "    \"keyDispensingUnit\": \"dispensing_unit_1\",\n" +
                "    \"keySizeCode\": \"size_code_1\",\n" +
                "    \"keyRouteOfAdministration\": \"route_of_administration_1\"\n" +
                "}]";
    }
}
