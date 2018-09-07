package org.smartregister.stock.management.intent;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.smartregister.stock.management.application.Application;
import org.smartregister.stock.openlmis.OpenLMISLibrary;
import org.smartregister.stock.openlmis.intent.service.ProgramOrderableSyncIntentService;
import org.smartregister.stock.openlmis.repository.openlmis.ProgramOrderableRepository;
import org.smartregister.stock.openlmis.util.Utils;

import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.smartregister.stock.management.util.ServiceUtils.startService;
import static org.smartregister.stock.management.util.ServiceUtils.stopService;

public class ProgramOrderableSyncIntentServiceTest extends BaseSyncIntentServiceTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    private ProgramOrderableRepository repository = OpenLMISLibrary.getInstance().getProgramOrderableRepository();

    @Before
    @Override
    public void setUp() {
        super.setUp();
        startService(Application.getInstance().getApplicationContext(), ProgramOrderableSyncIntentService.class);
    }

    @After
    public void tearDown() {
        stopService(Application.getInstance().getApplicationContext(), ProgramOrderableSyncIntentService.class);
        repository.getReadableDatabase().close();
    }

    @Test
    public void testProgramOrderablesAreSyncedAndSaved() {
        mockStatic(Utils.class);
        when(Utils.makeGetRequest(anyString())).thenReturn(responseString());
        assertEquals(1, repository.findProgramOrderables("id", "program_id", "orderable_id", "5", "1", "1").size());
        assertEquals(1, repository.findProgramOrderables("id_1", "program_id_1", "orderable_id_1", "5", "1", "1").size());
        assertEquals(1, repository.findProgramOrderables("id_2", "program_id_2", "orderable_id_2", "5", "1", "1").size());
    }

    private String responseString() {
        return "[{\n" +
                "\t\"id\": \"id\",\n" +
                "\t\"programId\": \"program_id\",\n" +
                "\t\"orderableId\": \"orderable_id\",\n" +
                "\t\"dosesPerPatient\": 5,\n" +
                "\t\"active\": true,\n" +
                "\t\"fullSupply\": true,\n" +
                "\t\"serverVersion\": null,\n" +
                "\t\"dateDeleted\": null\n" +
                "}, {\n" +
                "\t\"id\": \"id_1\",\n" +
                "\t\"programId\": \"program_id_1\",\n" +
                "\t\"orderableId\": \"orderable_id_1\",\n" +
                "\t\"dosesPerPatient\": 5,\n" +
                "\t\"active\": true,\n" +
                "\t\"fullSupply\": true,\n" +
                "\t\"serverVersion\": null,\n" +
                "\t\"dateDeleted\": null\n" +
                "}]";
    }
}
