package org.smartregister.stock.management.intent;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.smartregister.stock.management.application.Application;
import org.smartregister.stock.openlmis.OpenLMISLibrary;
import org.smartregister.stock.openlmis.intent.ReasonSyncIntentService;
import org.smartregister.stock.openlmis.repository.openlmis.ReasonRepository;
import org.smartregister.stock.openlmis.util.Utils;

import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.smartregister.stock.management.util.ServiceUtils.startService;
import static org.smartregister.stock.management.util.ServiceUtils.stopService;

public class ReasonSyncIntentServiceTest extends BaseSyncIntentServiceTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    private ReasonRepository repository = OpenLMISLibrary.getInstance().getReasonRepository();

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
        mockStatic(Utils.class);
        when(Utils.makeGetRequest(anyString())).thenReturn(responseString());
        assertEquals(1, repository.findReasons("id", "name", "program_id").size());
        assertEquals(1, repository.findReasons("id_1", "name", "program_id_1").size());
        assertEquals(1, repository.findReasons("id_2", "name", "program_id_2").size());
    }

    private String responseString() {
        return "[\n" +
                "    {\n" +
                "        \"id\": \"id\",\n" +
                "        \"serverVersion\": 1105930338227499,\n" +
                "        \"programId\": \"program_id\",\n" +
                "        \"facilityType\": \"facility_type\",\n" +
                "        \"stockCardLineItemReason\": {\n" +
                "            \"name\": \"name\",\n" +
                "            \"description\": \"description\",\n" +
                "            \"reasonType\": \"reason_type\",\n" +
                "            \"reasonCategory\": \"reason_category\",\n" +
                "            \"isFreeTextAllowed\": true,\n" +
                "            \"tags\": null\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\": \"id_1\",\n" +
                "        \"serverVersion\": 1105930547449814,\n" +
                "        \"programId\": \"program_id_1\",\n" +
                "        \"facilityType\": \"facility_type_1\",\n" +
                "        \"stockCardLineItemReason\": {\n" +
                "            \"name\": \"name\",\n" +
                "            \"description\": \"description\",\n" +
                "            \"reasonType\": \"reason_type\",\n" +
                "            \"reasonCategory\": \"reason_category\",\n" +
                "            \"isFreeTextAllowed\": true,\n" +
                "            \"tags\": null\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\": \"id_2\",\n" +
                "        \"serverVersion\": 1105930597527204,\n" +
                "        \"programId\": \"program_id_2\",\n" +
                "        \"facilityType\": \"facility_type_2\",\n" +
                "        \"stockCardLineItemReason\": {\n" +
                "            \"name\": \"name\",\n" +
                "            \"description\": \"description\",\n" +
                "            \"reasonType\": \"reason_type\",\n" +
                "            \"reasonCategory\": \"reason_category\",\n" +
                "            \"isFreeTextAllowed\": true,\n" +
                "            \"tags\": null\n" +
                "        }\n" +
                "    }\n" +
                "]";
    }
}
