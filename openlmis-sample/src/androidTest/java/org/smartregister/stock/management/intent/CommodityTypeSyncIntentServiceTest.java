package org.smartregister.stock.management.intent;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.smartregister.stock.management.application.Application;
import org.smartregister.stock.openlmis.OpenLMISLibrary;
import org.smartregister.stock.openlmis.intent.CommodityTypeSyncIntentService;
import org.smartregister.stock.openlmis.repository.openlmis.CommodityTypeRepository;
import org.smartregister.stock.openlmis.util.Utils;

import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.smartregister.stock.management.util.ServiceUtils.startService;
import static org.smartregister.stock.management.util.ServiceUtils.stopService;
import static org.smartregister.stock.openlmis.util.Utils.DATABASE_NAME;

public class CommodityTypeSyncIntentServiceTest extends BaseSyncIntentServiceTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    private CommodityTypeRepository repository = OpenLMISLibrary.getInstance().getCommodityTypeRepository();

    @Before
    @Override
    public void setUp() {
        super.setUp();
        startService(Application.getInstance().getApplicationContext(), CommodityTypeSyncIntentService.class);
    }

    @After
    public void tearDown() {
        stopService(Application.getInstance().getApplicationContext(), CommodityTypeSyncIntentService.class);
        repository.getReadableDatabase().close();
    }

    @Test
    public void testCommodityTypesAreSyncedAndSaved() {
        mockStatic(Utils.class);
        when(Utils.makeGetRequest(anyString())).thenReturn(responseString());
        assertEquals(1, repository.findCommodityTypes("identifier", "commodity_name", "parent_id", "classification_system", "classification_id").size());
        assertEquals(1, repository.findCommodityTypes("identifier_1", "commodity_name_1", "parent_id_1", "classification_system_1", "classification_id_1").size());
        assertEquals(1, repository.findCommodityTypes("identifier_2", "commodity_name_2", "parent_id_2", "classification_system_2", "classification_id_2").size());
    }

    private String responseString() {
        return "[{\n" +
                "    \"uuid\": \"identifier\",\n" +
                "    \"name\": \"commodity_name\",\n" +
                "    \"parent\": {\n" +
                "        \"uuid\": \"parent_id\",\n" +
                "        \"name\": null,\n" +
                "        \"parent\": null,\n" +
                "        \"classificationSystem\": null,\n" +
                "        \"classificationId\": null,\n" +
                "        \"parentId\": null,\n" +
                "        \"children\": null,\n" +
                "        \"tradeItems\": null\n" +
                "    },\n" +
                "    \"classificationSystem\": \"classification_system\",\n" +
                "    \"classificationId\": \"classification_id\",\n" +
                "    \"parentId\": \"parent_id\",\n" +
                "    \"children\": [{\n" +
                "        \"uuid\": \"child_1\",\n" +
                "        \"name\": null,\n" +
                "        \"parent\": null,\n" +
                "        \"classificationSystem\": null,\n" +
                "        \"classificationId\": null,\n" +
                "        \"parentId\": null,\n" +
                "        \"children\": null,\n" +
                "        \"tradeItems\": null\n" +
                "    }, {\n" +
                "        \"uuid\": \"child_2\",\n" +
                "        \"name\": null,\n" +
                "        \"parent\": null,\n" +
                "        \"classificationSystem\": null,\n" +
                "        \"classificationId\": null,\n" +
                "        \"parentId\": null,\n" +
                "        \"children\": null,\n" +
                "        \"tradeItems\": null\n" +
                "    }],\n" +
                "    \"tradeItems\": [{\n" +
                "        \"uuid\": \"trade_item_1\",\n" +
                "        \"gtin\": null,\n" +
                "        \"manufacturerOfTradeItem\": null,\n" +
                "        \"classifications\": null\n" +
                "    }, {\n" +
                "        \"uuid\": \"trade_item_2\",\n" +
                "        \"gtin\": null,\n" +
                "        \"manufacturerOfTradeItem\": null,\n" +
                "        \"classifications\": null\n" +
                "    }]\n" +
                "}, {\n" +
                "    \"uuid\": \"identifier_1\",\n" +
                "    \"name\": \"commodity_name_1\",\n" +
                "    \"parent\": {\n" +
                "        \"uuid\": \"parent_id\",\n" +
                "        \"name\": null,\n" +
                "        \"parent\": null,\n" +
                "        \"classificationSystem\": null,\n" +
                "        \"classificationId\": null,\n" +
                "        \"parentId\": null,\n" +
                "        \"children\": null,\n" +
                "        \"tradeItems\": null\n" +
                "    },\n" +
                "    \"classificationSystem\": \"classification_system_1\",\n" +
                "    \"classificationId\": \"classification_id_1\",\n" +
                "    \"parentId\": \"parent_id_1\",\n" +
                "    \"children\": [{\n" +
                "        \"uuid\": \"child_1\",\n" +
                "        \"name\": null,\n" +
                "        \"parent\": null,\n" +
                "        \"classificationSystem\": null,\n" +
                "        \"classificationId\": null,\n" +
                "        \"parentId\": null,\n" +
                "        \"children\": null,\n" +
                "        \"tradeItems\": null\n" +
                "    }, {\n" +
                "        \"uuid\": \"child_2\",\n" +
                "        \"name\": null,\n" +
                "        \"parent\": null,\n" +
                "        \"classificationSystem\": null,\n" +
                "        \"classificationId\": null,\n" +
                "        \"parentId\": null,\n" +
                "        \"children\": null,\n" +
                "        \"tradeItems\": null\n" +
                "    }],\n" +
                "    \"tradeItems\": [{\n" +
                "        \"uuid\": \"trade_item_1\",\n" +
                "        \"gtin\": null,\n" +
                "        \"manufacturerOfTradeItem\": null,\n" +
                "        \"classifications\": null\n" +
                "    }, {\n" +
                "        \"uuid\": \"trade_item_2\",\n" +
                "        \"gtin\": null,\n" +
                "        \"manufacturerOfTradeItem\": null,\n" +
                "        \"classifications\": null\n" +
                "    }]\n" +
                "}]";
    }
}
