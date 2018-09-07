package org.smartregister.stock.management.intent;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.smartregister.stock.management.application.Application;
import org.smartregister.stock.openlmis.OpenLMISLibrary;
import org.smartregister.stock.openlmis.intent.service.LotSyncIntentService;
import org.smartregister.stock.openlmis.repository.openlmis.LotRepository;
import org.smartregister.stock.openlmis.util.Utils;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.smartregister.stock.management.util.ServiceUtils.startService;
import static org.smartregister.stock.management.util.ServiceUtils.stopService;

@RunWith(JUnit4ClassRunner.class)
public class LotSyncIntentServiceTest extends BaseSyncIntentServiceTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    private LotRepository repository = OpenLMISLibrary.getInstance().getLotRepository();

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
        mockStatic(Utils.class);
        when(Utils.makeGetRequest(anyString())).thenReturn(responseString());
        assertNotNull(repository.findLotById("id"));
        assertNotNull(repository.findLotById("id_1"));
        assertNotNull(repository.findLotById("id_2"));
    }

    private String responseString() {
        return "[{\n" +
                "\t\"id\": \"id\",\n" +
                "\t\"lotCode\": \"lot_code\",\n" +
                "\t\"expirationDate\": 359518697150953,\n" +
                "\t\"manufuctureDate\": 359518697131441,\n" +
                "\t\"tradeItemId\": \"trade_item\",\n" +
                "\t\"active\": false,\n" +
                "\t\"serverVersion\": 359518805941410,\n" +
                "\t\"dateDeleted\": null\n" +
                "}, {\n" +
                "\t\"id\": \"id_1\",\n" +
                "\t\"lotCode\": \"lot_code_2\",\n" +
                "\t\"expirationDate\": 359518816405516,\n" +
                "\t\"manufuctureDate\": 359518816435396,\n" +
                "\t\"tradeItemId\": \"trade_item_id_2\",\n" +
                "\t\"active\": true,\n" +
                "\t\"serverVersion\": 359518868003348,\n" +
                "\t\"dateDeleted\": null\n" +
                "}]";
    }
}