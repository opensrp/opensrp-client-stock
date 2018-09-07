package org.smartregister.stock.management.intent;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.smartregister.stock.management.application.Application;
import org.smartregister.stock.openlmis.OpenLMISLibrary;
import org.smartregister.stock.openlmis.intent.service.ProgramSyncIntentService;
import org.smartregister.stock.openlmis.repository.openlmis.ProgramRepository;
import org.smartregister.stock.openlmis.util.Utils;

import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.smartregister.stock.management.util.ServiceUtils.startService;
import static org.smartregister.stock.management.util.ServiceUtils.stopService;

public class ProgramSyncIntentServiceTest extends BaseSyncIntentServiceTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    private ProgramRepository repository = OpenLMISLibrary.getInstance().getProgramRepository();

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
        mockStatic(Utils.class);
        when(Utils.makeGetRequest(anyString())).thenReturn(responseString());
        assertEquals(1, repository.findPrograms("identifier", "code", "program_name", "1").size());
        assertEquals(1, repository.findPrograms("identifier_1", "code_1", "program_name_1", "1").size());
        assertEquals(1, repository.findPrograms("identifier_2", "code_2", "program_name_2", "1").size());
    }

    private String responseString() {
        return "[{\n" +
                "\t\"id\": \"identifier\",\n" +
                "\t\"code\": {\n" +
                "\t\t\"code\": \"code\"\n" +
                "\t},\n" +
                "\t\"name\": \"program_name\",\n" +
                "\t\"description\": \"program_description\",\n" +
                "\t\"active\": true,\n" +
                "\t\"periodsSkippable\": false,\n" +
                "\t\"skipAuthorization\": true,\n" +
                "\t\"showNonFullSupplyTab\": false,\n" +
                "\t\"enableDatePhysicalStockCountCompleted\": true\n" +
                "}, {\n" +
                "\t\"id\": \"identifier_1\",\n" +
                "\t\"code\": {\n" +
                "\t\t\"code\": \"code_1\"\n" +
                "\t},\n" +
                "\t\"name\": \"program_name_1\",\n" +
                "\t\"description\": \"program_description_1\",\n" +
                "\t\"active\": true,\n" +
                "\t\"periodsSkippable\": false,\n" +
                "\t\"skipAuthorization\": true,\n" +
                "\t\"showNonFullSupplyTab\": true,\n" +
                "\t\"enableDatePhysicalStockCountCompleted\": true\n" +
                "}]";
    }
}
