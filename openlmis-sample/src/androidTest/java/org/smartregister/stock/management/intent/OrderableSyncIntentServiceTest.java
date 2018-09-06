package org.smartregister.stock.management.intent;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.smartregister.stock.management.application.Application;
import org.smartregister.stock.openlmis.OpenLMISLibrary;
import org.smartregister.stock.openlmis.intent.OrderableSyncIntentService;
import org.smartregister.stock.openlmis.repository.TradeItemRepository;
import org.smartregister.stock.openlmis.repository.openlmis.OrderableRepository;
import org.smartregister.stock.openlmis.util.Utils;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.smartregister.stock.management.util.ServiceUtils.startService;
import static org.smartregister.stock.management.util.ServiceUtils.stopService;

public class OrderableSyncIntentServiceTest extends BaseSyncIntentServiceTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    private OrderableRepository orderableRepository = OpenLMISLibrary.getInstance().getOrderableRepository();
    private TradeItemRepository tradeItemRegisterRepository = OpenLMISLibrary.getInstance().getTradeItemRegisterRepository();

    @Before
    @Override
    public void setUp() {
        super.setUp();
        startService(Application.getInstance().getApplicationContext(), OrderableSyncIntentService.class);
    }

    @After
    public void tearDown() {
        stopService(Application.getInstance().getApplicationContext(), OrderableSyncIntentService.class);
        orderableRepository.getReadableDatabase().close();
        tradeItemRegisterRepository.getReadableDatabase().close();
    }

    @Test
    public void testOrderablesAreSyncedAndSaved() {
        mockStatic(Utils.class);
        when(Utils.makeGetRequest(anyString())).thenReturn(responseString());
        assertNotNull(orderableRepository.findOrderable("id"));
        assertNotNull(orderableRepository.findOrderable("id_1"));
        assertNotNull(orderableRepository.findOrderable("id_2"));
        // assert TradeItem register table is populated
        assertTrue(tradeItemRegisterRepository.tradeItemExists("trade_item"));
        assertTrue(tradeItemRegisterRepository.tradeItemExists("trade_item_1"));
        assertTrue(tradeItemRegisterRepository.tradeItemExists("trade_item_2"));
    }

    private String responseString() {
        return "[{\n" +
                "\t\"id\": \"id\",\n" +
                "\t\"code\": \"code\",\n" +
                "\t\"fullProductCode\": \"full_product_code\",\n" +
                "\t\"netContent\": 18,\n" +
                "\t\"packRoundingThreshold\": 2,\n" +
                "\t\"roundToZero\": true,\n" +
                "\t\"dispensable\": 12,\n" +
                "\t\"tradeItemId\": \"trade_item\",\n" +
                "\t\"commodityTypeId\": \"commodity_type_id\",\n" +
                "\t\"serverVersion\": 322932727702854,\n" +
                "\t\"dateDeleted\": null\n" +
                "}, {\n" +
                "\t\"id\": \"id_1\",\n" +
                "\t\"code\": \"code_1\",\n" +
                "\t\"fullProductCode\": \"full_product_code_1\",\n" +
                "\t\"netContent\": 12,\n" +
                "\t\"packRoundingThreshold\": 3,\n" +
                "\t\"roundToZero\": true,\n" +
                "\t\"dispensable\": 18,\n" +
                "\t\"tradeItemId\": \"trade_item_1\",\n" +
                "\t\"commodityTypeId\": \"commodity_type_id_1\",\n" +
                "\t\"serverVersion\": 322932969664798,\n" +
                "\t\"dateDeleted\": null\n" +
                "}]";
    }
}
