package org.smartregister.stock.management.intent;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.smartregister.stock.management.application.Application;
import org.smartregister.stock.openlmis.OpenLMISLibrary;
import org.smartregister.stock.openlmis.intent.OpenLMISStockSyncIntentService;
import org.smartregister.stock.openlmis.repository.StockRepository;
import org.smartregister.stock.openlmis.util.Utils;

import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.smartregister.stock.management.util.ServiceUtils.startService;
import static org.smartregister.stock.management.util.ServiceUtils.stopService;

public class OpenLMISStockSyncIntentServiceTest extends BaseSyncIntentServiceTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    private StockRepository repository = OpenLMISLibrary.getInstance().getStockRepository();

    @Before
    @Override
    public void setUp() {
        super.setUp();
        startService(Application.getInstance().getApplicationContext(), OpenLMISStockSyncIntentService.class);
    }

    @After
    public void tearDown() {
        stopService(Application.getInstance().getApplicationContext(), OpenLMISStockSyncIntentService.class);
        repository.getReadableDatabase().close();
    }

    @Test
    public void testOpenLMISStocksAreSyncedDownwardAndSaved() {
        mockStatic(Utils.class);
        when(Utils.makeGetRequest(anyString())).thenReturn(responseString());
        assertEquals(1, repository.findUniqueStock("trade_item_id", "debit", "provider_id", "1", "1", "to_from").size());
        assertEquals(1, repository.findUniqueStock("trade_item_id_1", "debit", "provider_id_1", "1", "1", "to_from").size());
        assertEquals(1, repository.findUniqueStock("trade_item_id_2", "debit", "provider_id_2", "1", "1", "to_from").size());
    }

    private String responseString() {
        return "{\n" +
                "    \"stocks\": [\n" +
                "        {\n" +
                "            \"identifier\": 1,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536064794036,\n" +
                "            \"dateCreated\": \"2018-09-04T15:39:54.039+03:00\",\n" +
                "            \"serverVersion\": 1536064794036,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"c7a7ac8e-fcd3-4cdd-952d-84ed594e2778\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 2,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536064794038,\n" +
                "            \"dateCreated\": \"2018-09-04T15:39:54.287+03:00\",\n" +
                "            \"serverVersion\": 1536064794038,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"49e75fb1-e69d-49ea-9f5f-e459845a7397\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 3,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536064794038,\n" +
                "            \"dateCreated\": \"2018-09-04T15:39:54.395+03:00\",\n" +
                "            \"serverVersion\": 1536064794038,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"9d35a82f-de67-4e04-8e36-769e96fef814\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 1,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536064838737,\n" +
                "            \"dateCreated\": \"2018-09-04T15:40:38.737+03:00\",\n" +
                "            \"serverVersion\": 1536064838737,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"156e4426-4e3b-4822-ae83-8ce8fff69b7f\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 2,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536064838737,\n" +
                "            \"dateCreated\": \"2018-09-04T15:40:38.835+03:00\",\n" +
                "            \"serverVersion\": 1536064838737,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"2f7e0c1a-2c73-40b2-8a18-5ac0ce165d2d\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 3,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536064838737,\n" +
                "            \"dateCreated\": \"2018-09-04T15:40:38.840+03:00\",\n" +
                "            \"serverVersion\": 1536064838737,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"702407fc-ceb2-4343-9780-87a9f704c2f7\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 4,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536064838737,\n" +
                "            \"dateCreated\": \"2018-09-04T15:40:38.843+03:00\",\n" +
                "            \"serverVersion\": 1536064838737,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"248edb7d-38d9-47df-a768-55748e113e78\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 5,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536064838737,\n" +
                "            \"dateCreated\": \"2018-09-04T15:40:38.848+03:00\",\n" +
                "            \"serverVersion\": 1536064838737,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"04ea8a5a-56d5-48e1-bf4b-b11aac946c24\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 6,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536064838737,\n" +
                "            \"dateCreated\": \"2018-09-04T15:40:38.850+03:00\",\n" +
                "            \"serverVersion\": 1536064838737,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"5eeb2841-adae-4d8c-afb7-83b16e3b39ce\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 1,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536064916841,\n" +
                "            \"dateCreated\": \"2018-09-04T15:41:56.842+03:00\",\n" +
                "            \"serverVersion\": 1536064916841,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"67148b29-81fb-4e64-9816-0064090e4832\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 2,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536064916841,\n" +
                "            \"dateCreated\": \"2018-09-04T15:41:56.970+03:00\",\n" +
                "            \"serverVersion\": 1536064916841,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"7f5f4559-5de4-4547-b8a1-2c29106e3431\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 3,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536064916841,\n" +
                "            \"dateCreated\": \"2018-09-04T15:41:57.014+03:00\",\n" +
                "            \"serverVersion\": 1536064916841,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"e9b6c6d3-f3f7-4b59-b685-810d2cfd78ae\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 4,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536064916841,\n" +
                "            \"dateCreated\": \"2018-09-04T15:41:57.016+03:00\",\n" +
                "            \"serverVersion\": 1536064916841,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"4c8503f3-bb3a-4a7f-b548-c2e8de3b5a08\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 5,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536064916841,\n" +
                "            \"dateCreated\": \"2018-09-04T15:41:57.018+03:00\",\n" +
                "            \"serverVersion\": 1536064916841,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"575b008e-d107-4bfd-ba39-fce81b5da8c7\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 6,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536064916841,\n" +
                "            \"dateCreated\": \"2018-09-04T15:41:57.020+03:00\",\n" +
                "            \"serverVersion\": 1536064916841,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"281249c8-df6d-4ba1-b239-a846d02eaf27\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 7,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536064916842,\n" +
                "            \"dateCreated\": \"2018-09-04T15:41:57.065+03:00\",\n" +
                "            \"serverVersion\": 1536064916842,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"f074c088-af94-4c7c-bde1-0f2ac7260d6e\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 8,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536064916842,\n" +
                "            \"dateCreated\": \"2018-09-04T15:41:57.068+03:00\",\n" +
                "            \"serverVersion\": 1536064916842,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"9eb5b113-c9ef-4878-b13f-e0c509231867\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 9,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536064916842,\n" +
                "            \"dateCreated\": \"2018-09-04T15:41:57.074+03:00\",\n" +
                "            \"serverVersion\": 1536064916842,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"e3cd9f50-b7d2-4207-99c5-dfa122518ad9\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 1,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536064961421,\n" +
                "            \"dateCreated\": \"2018-09-04T15:42:41.422+03:00\",\n" +
                "            \"serverVersion\": 1536064961421,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"d55b50da-a342-4c89-b4db-dff630b56924\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 2,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536064961421,\n" +
                "            \"dateCreated\": \"2018-09-04T15:42:41.514+03:00\",\n" +
                "            \"serverVersion\": 1536064961421,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"be3c18e3-e82b-43b7-882d-6103f850203e\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 3,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536064961421,\n" +
                "            \"dateCreated\": \"2018-09-04T15:42:41.558+03:00\",\n" +
                "            \"serverVersion\": 1536064961421,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"e7bb6ae8-ef1d-44c3-a011-a45e9e3c3f04\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 4,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536064961421,\n" +
                "            \"dateCreated\": \"2018-09-04T15:42:41.560+03:00\",\n" +
                "            \"serverVersion\": 1536064961421,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"131a2fda-52ab-4b99-9796-14a9674fa841\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 5,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536064961421,\n" +
                "            \"dateCreated\": \"2018-09-04T15:42:41.566+03:00\",\n" +
                "            \"serverVersion\": 1536064961421,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"c8710033-01b7-45ba-8416-d3ef59e482dc\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 6,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536064961421,\n" +
                "            \"dateCreated\": \"2018-09-04T15:42:41.570+03:00\",\n" +
                "            \"serverVersion\": 1536064961421,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"420377ad-9184-494f-9822-25cdb5087f46\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 7,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536064961421,\n" +
                "            \"dateCreated\": \"2018-09-04T15:42:41.578+03:00\",\n" +
                "            \"serverVersion\": 1536064961421,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"174952e5-a53b-486e-8b22-22dc705b01cd\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 8,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536064961421,\n" +
                "            \"dateCreated\": \"2018-09-04T15:42:41.582+03:00\",\n" +
                "            \"serverVersion\": 1536064961421,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"0cc13a1a-bff9-4259-9913-09f7adf3dab5\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 9,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536064961421,\n" +
                "            \"dateCreated\": \"2018-09-04T15:42:41.625+03:00\",\n" +
                "            \"serverVersion\": 1536064961421,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"9b330e83-c42f-440d-9320-04378cd99dcd\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 10,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536064961421,\n" +
                "            \"dateCreated\": \"2018-09-04T15:42:41.729+03:00\",\n" +
                "            \"serverVersion\": 1536064961421,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"1af4de61-fd87-4931-b0ce-46f2c1738fb2\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 11,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536064961421,\n" +
                "            \"dateCreated\": \"2018-09-04T15:42:41.774+03:00\",\n" +
                "            \"serverVersion\": 1536064961421,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"d0af2d56-0d28-4d82-9f4b-ad6f4c608475\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 12,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536064961421,\n" +
                "            \"dateCreated\": \"2018-09-04T15:42:41.777+03:00\",\n" +
                "            \"serverVersion\": 1536064961421,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"3e1cc22d-d46d-4eee-acee-d56e15e0ec42\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 1,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065033544,\n" +
                "            \"dateCreated\": \"2018-09-04T15:43:53.544+03:00\",\n" +
                "            \"serverVersion\": 1536065033544,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"48f1f630-d23e-4aa6-8821-0eb208dbd1c5\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 2,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065033544,\n" +
                "            \"dateCreated\": \"2018-09-04T15:43:53.587+03:00\",\n" +
                "            \"serverVersion\": 1536065033544,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"4bd89d1c-d4a7-4e3f-b035-307849ab30e6\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 3,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065033544,\n" +
                "            \"dateCreated\": \"2018-09-04T15:43:53.589+03:00\",\n" +
                "            \"serverVersion\": 1536065033544,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"2d3b0629-f0dd-4d92-b26b-db58ba84efdb\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 4,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065033544,\n" +
                "            \"dateCreated\": \"2018-09-04T15:43:53.591+03:00\",\n" +
                "            \"serverVersion\": 1536065033544,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"8a3ed51f-3fed-49cd-9127-9438beee1c12\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 5,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065033544,\n" +
                "            \"dateCreated\": \"2018-09-04T15:43:53.593+03:00\",\n" +
                "            \"serverVersion\": 1536065033544,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"a44c24c8-5bf6-490d-80db-f19669f92be5\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 6,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065033544,\n" +
                "            \"dateCreated\": \"2018-09-04T15:43:53.596+03:00\",\n" +
                "            \"serverVersion\": 1536065033544,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"04074a60-b53c-427d-9a35-785bba2e5b8d\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 7,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065033544,\n" +
                "            \"dateCreated\": \"2018-09-04T15:43:53.597+03:00\",\n" +
                "            \"serverVersion\": 1536065033544,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"b8dbea94-c825-42ec-83f0-56e5910cf4b6\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 8,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065033544,\n" +
                "            \"dateCreated\": \"2018-09-04T15:43:53.633+03:00\",\n" +
                "            \"serverVersion\": 1536065033544,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"5115997e-751a-4bc0-a214-0944101ba9be\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 9,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065033544,\n" +
                "            \"dateCreated\": \"2018-09-04T15:43:53.635+03:00\",\n" +
                "            \"serverVersion\": 1536065033544,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"7ec214f3-5c20-4b82-8bb2-6ce9ce74b974\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 10,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065033544,\n" +
                "            \"dateCreated\": \"2018-09-04T15:43:53.665+03:00\",\n" +
                "            \"serverVersion\": 1536065033544,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"1de44978-8493-4e78-9897-768a5e54357f\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 11,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065033544,\n" +
                "            \"dateCreated\": \"2018-09-04T15:43:53.667+03:00\",\n" +
                "            \"serverVersion\": 1536065033544,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"ba685d94-4235-43dd-9085-24408468e674\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 12,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065033544,\n" +
                "            \"dateCreated\": \"2018-09-04T15:43:53.730+03:00\",\n" +
                "            \"serverVersion\": 1536065033544,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"b19d6761-8433-4d62-af24-649cb8f5e0a2\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 13,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065033544,\n" +
                "            \"dateCreated\": \"2018-09-04T15:43:53.732+03:00\",\n" +
                "            \"serverVersion\": 1536065033544,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"847cc123-cae5-498d-b1ed-b1159e2f1f58\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 14,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065033544,\n" +
                "            \"dateCreated\": \"2018-09-04T15:43:53.762+03:00\",\n" +
                "            \"serverVersion\": 1536065033544,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"277d1565-af3e-458d-be08-a150cb09fa73\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 15,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065033544,\n" +
                "            \"dateCreated\": \"2018-09-04T15:43:53.764+03:00\",\n" +
                "            \"serverVersion\": 1536065033544,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"b6cdb756-3c84-4dd6-9faa-58323bb2bc09\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 1,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065080673,\n" +
                "            \"dateCreated\": \"2018-09-04T15:44:40.675+03:00\",\n" +
                "            \"serverVersion\": 1536065080673,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"3650bbf2-99f0-43ac-9f8f-07b9ab0738a3\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 2,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065080673,\n" +
                "            \"dateCreated\": \"2018-09-04T15:44:40.762+03:00\",\n" +
                "            \"serverVersion\": 1536065080673,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"0b082639-9014-4d71-932c-3acb81e077ef\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 3,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065080673,\n" +
                "            \"dateCreated\": \"2018-09-04T15:44:40.790+03:00\",\n" +
                "            \"serverVersion\": 1536065080673,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"3f6a2f05-4c2f-4a95-8bb0-c30b4a1b280c\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 4,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065080673,\n" +
                "            \"dateCreated\": \"2018-09-04T15:44:40.793+03:00\",\n" +
                "            \"serverVersion\": 1536065080673,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"18a133d2-72a3-4b0a-ab50-1802893d323e\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 5,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065080673,\n" +
                "            \"dateCreated\": \"2018-09-04T15:44:40.796+03:00\",\n" +
                "            \"serverVersion\": 1536065080673,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"b6498d3e-52ed-44e0-a2eb-75618e09095c\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 6,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065080673,\n" +
                "            \"dateCreated\": \"2018-09-04T15:44:40.799+03:00\",\n" +
                "            \"serverVersion\": 1536065080673,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"617f2dfb-bc04-4929-bf56-679645d64259\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 7,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065080673,\n" +
                "            \"dateCreated\": \"2018-09-04T15:44:40.801+03:00\",\n" +
                "            \"serverVersion\": 1536065080673,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"68512c61-3e26-455f-866f-41c0b95f1e27\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 8,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065080673,\n" +
                "            \"dateCreated\": \"2018-09-04T15:44:40.805+03:00\",\n" +
                "            \"serverVersion\": 1536065080673,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"0b238a6d-b516-4829-bb1e-c121d80b5911\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 9,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065080673,\n" +
                "            \"dateCreated\": \"2018-09-04T15:44:40.807+03:00\",\n" +
                "            \"serverVersion\": 1536065080673,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"f7e5bd01-9e70-4f4c-bc15-8e4de15c4696\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 10,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065080673,\n" +
                "            \"dateCreated\": \"2018-09-04T15:44:40.809+03:00\",\n" +
                "            \"serverVersion\": 1536065080673,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"1054ec09-01ac-4a7c-b3c0-ae1f971d21b6\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 11,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065080673,\n" +
                "            \"dateCreated\": \"2018-09-04T15:44:40.891+03:00\",\n" +
                "            \"serverVersion\": 1536065080673,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"36b7a347-9ba5-49be-82ea-35030c51aa72\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 12,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065080673,\n" +
                "            \"dateCreated\": \"2018-09-04T15:44:40.922+03:00\",\n" +
                "            \"serverVersion\": 1536065080673,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"34813d9d-3409-4fd8-8d9d-404157a39ba2\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 13,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065080673,\n" +
                "            \"dateCreated\": \"2018-09-04T15:44:40.924+03:00\",\n" +
                "            \"serverVersion\": 1536065080673,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"7c2aa04e-0348-444f-92dd-1c9a7989d4e2\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 14,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065080673,\n" +
                "            \"dateCreated\": \"2018-09-04T15:44:40.926+03:00\",\n" +
                "            \"serverVersion\": 1536065080673,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"ec034475-8b70-4fdb-89b1-6e44070ec362\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 15,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065080673,\n" +
                "            \"dateCreated\": \"2018-09-04T15:44:40.928+03:00\",\n" +
                "            \"serverVersion\": 1536065080673,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"32cbc632-edfc-4ad9-92d6-c85418462d27\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 16,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065080673,\n" +
                "            \"dateCreated\": \"2018-09-04T15:44:40.930+03:00\",\n" +
                "            \"serverVersion\": 1536065080673,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"5887e1d4-cc4d-4e23-80a2-6cbc4be99672\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 17,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065080673,\n" +
                "            \"dateCreated\": \"2018-09-04T15:44:40.932+03:00\",\n" +
                "            \"serverVersion\": 1536065080673,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"01c81bcc-83cd-4aac-ac88-e40d8e686015\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 18,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065080674,\n" +
                "            \"dateCreated\": \"2018-09-04T15:44:40.933+03:00\",\n" +
                "            \"serverVersion\": 1536065080674,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"7ba9ff45-d1a8-4160-ae3d-78de06b05af8\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 1,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065154582,\n" +
                "            \"dateCreated\": \"2018-09-04T15:45:54.583+03:00\",\n" +
                "            \"serverVersion\": 1536065154582,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"deafd720-b705-4924-b959-ada65f7325a5\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 2,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065154582,\n" +
                "            \"dateCreated\": \"2018-09-04T15:45:54.626+03:00\",\n" +
                "            \"serverVersion\": 1536065154582,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"098e4f57-96ab-498a-907a-202bf1114c75\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 3,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065154582,\n" +
                "            \"dateCreated\": \"2018-09-04T15:45:54.628+03:00\",\n" +
                "            \"serverVersion\": 1536065154582,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"9d67ce48-6d5f-40e5-b6bb-ea42ab96ff84\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 4,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065154582,\n" +
                "            \"dateCreated\": \"2018-09-04T15:45:54.670+03:00\",\n" +
                "            \"serverVersion\": 1536065154582,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"9822e7c4-b449-4043-985e-cec73f65daf5\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 5,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065154582,\n" +
                "            \"dateCreated\": \"2018-09-04T15:45:54.673+03:00\",\n" +
                "            \"serverVersion\": 1536065154582,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"8957d291-977e-4526-b6ab-dfc7373b76fd\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 6,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065154582,\n" +
                "            \"dateCreated\": \"2018-09-04T15:45:54.675+03:00\",\n" +
                "            \"serverVersion\": 1536065154582,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"5f5bd75c-7ed2-4621-b2db-00c99f8aa291\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 7,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065154582,\n" +
                "            \"dateCreated\": \"2018-09-04T15:45:54.676+03:00\",\n" +
                "            \"serverVersion\": 1536065154582,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"79358a3f-d590-4093-94ce-4ba4cef83f8d\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 8,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065154582,\n" +
                "            \"dateCreated\": \"2018-09-04T15:45:54.678+03:00\",\n" +
                "            \"serverVersion\": 1536065154582,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"06a5165f-cce6-4d49-9893-1392c5133b9e\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 9,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065154582,\n" +
                "            \"dateCreated\": \"2018-09-04T15:45:54.680+03:00\",\n" +
                "            \"serverVersion\": 1536065154582,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"7fb9909f-e86e-41bf-a112-0a2c94f4af3c\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 10,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065154582,\n" +
                "            \"dateCreated\": \"2018-09-04T15:45:54.682+03:00\",\n" +
                "            \"serverVersion\": 1536065154582,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"f5509000-41e8-4bf7-9680-aa41a1b4a629\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 11,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065154582,\n" +
                "            \"dateCreated\": \"2018-09-04T15:45:54.684+03:00\",\n" +
                "            \"serverVersion\": 1536065154582,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"7059433c-36e9-4f1b-8892-007fd9607261\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 12,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065154582,\n" +
                "            \"dateCreated\": \"2018-09-04T15:45:54.686+03:00\",\n" +
                "            \"serverVersion\": 1536065154582,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"bf5d8b8c-fe68-4f94-88b5-c4ba7d456ee9\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 13,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065154583,\n" +
                "            \"dateCreated\": \"2018-09-04T15:45:54.688+03:00\",\n" +
                "            \"serverVersion\": 1536065154583,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"2a2acd1c-b82b-4eec-a8f5-2ec2e2d8dbd0\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 14,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065154583,\n" +
                "            \"dateCreated\": \"2018-09-04T15:45:54.693+03:00\",\n" +
                "            \"serverVersion\": 1536065154583,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"328deb98-a504-4862-a517-087e339532b4\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 15,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065154583,\n" +
                "            \"dateCreated\": \"2018-09-04T15:45:54.695+03:00\",\n" +
                "            \"serverVersion\": 1536065154583,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"648dc6a0-8e5d-48c0-92d3-6e7c7603fecc\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 16,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065154583,\n" +
                "            \"dateCreated\": \"2018-09-04T15:45:54.697+03:00\",\n" +
                "            \"serverVersion\": 1536065154583,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"be026f5d-1279-4086-ad2e-761e770c44cc\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 17,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065154583,\n" +
                "            \"dateCreated\": \"2018-09-04T15:45:54.700+03:00\",\n" +
                "            \"serverVersion\": 1536065154583,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"729db788-d17e-4252-883b-dbf34067c7ab\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 18,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065154583,\n" +
                "            \"dateCreated\": \"2018-09-04T15:45:54.702+03:00\",\n" +
                "            \"serverVersion\": 1536065154583,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"ced94e4f-14b7-4e18-a118-b7366cec115c\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 19,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065154583,\n" +
                "            \"dateCreated\": \"2018-09-04T15:45:54.705+03:00\",\n" +
                "            \"serverVersion\": 1536065154583,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"938e4e15-e7f1-4359-a4cc-7645723e39fe\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 20,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065154583,\n" +
                "            \"dateCreated\": \"2018-09-04T15:45:54.749+03:00\",\n" +
                "            \"serverVersion\": 1536065154583,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"df7d4266-e51b-410f-9a59-88879549b7f5\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 21,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065154583,\n" +
                "            \"dateCreated\": \"2018-09-04T15:45:54.832+03:00\",\n" +
                "            \"serverVersion\": 1536065154583,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"29dbe3ca-7ac5-47f7-89dc-6e187e1f099d\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 1,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065273885,\n" +
                "            \"dateCreated\": \"2018-09-04T15:47:53.885+03:00\",\n" +
                "            \"serverVersion\": 1536065273885,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"e7c3a8e8-c12b-4d56-9d04-b1e512f81322\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 2,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065273885,\n" +
                "            \"dateCreated\": \"2018-09-04T15:47:53.892+03:00\",\n" +
                "            \"serverVersion\": 1536065273885,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"89dd0184-5bab-44ee-8566-571ebf04952c\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 3,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065273885,\n" +
                "            \"dateCreated\": \"2018-09-04T15:47:53.893+03:00\",\n" +
                "            \"serverVersion\": 1536065273885,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"d9bcfeea-744b-47f1-bc2b-529cd76cce51\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 4,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065273885,\n" +
                "            \"dateCreated\": \"2018-09-04T15:47:53.895+03:00\",\n" +
                "            \"serverVersion\": 1536065273885,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"d0919051-a59c-4ff1-9b43-6ed4630e0f7a\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 5,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065273885,\n" +
                "            \"dateCreated\": \"2018-09-04T15:47:53.897+03:00\",\n" +
                "            \"serverVersion\": 1536065273885,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"dbc0e0d5-9c0d-4906-be6b-4f09285ccd4f\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 6,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065273885,\n" +
                "            \"dateCreated\": \"2018-09-04T15:47:53.899+03:00\",\n" +
                "            \"serverVersion\": 1536065273885,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"c1c3bc8f-e380-44ab-a06b-58c22b396f75\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 7,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065273885,\n" +
                "            \"dateCreated\": \"2018-09-04T15:47:53.941+03:00\",\n" +
                "            \"serverVersion\": 1536065273885,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"47eb7f33-189f-4507-89fb-792e58042329\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 8,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065273885,\n" +
                "            \"dateCreated\": \"2018-09-04T15:47:53.943+03:00\",\n" +
                "            \"serverVersion\": 1536065273885,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"f352ab15-b800-4fe8-bdf6-7788e1ca5b5e\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 9,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065273885,\n" +
                "            \"dateCreated\": \"2018-09-04T15:47:53.945+03:00\",\n" +
                "            \"serverVersion\": 1536065273885,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"fb0072b4-6363-4e85-8ced-c85a68b9403d\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 10,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065273885,\n" +
                "            \"dateCreated\": \"2018-09-04T15:47:53.946+03:00\",\n" +
                "            \"serverVersion\": 1536065273885,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"f1b82bbb-ada0-4278-9050-b70d56ff7ab4\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 11,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065273885,\n" +
                "            \"dateCreated\": \"2018-09-04T15:47:53.948+03:00\",\n" +
                "            \"serverVersion\": 1536065273885,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"20939469-8df2-4ddb-92d5-3358f1d28a7e\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 12,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065273885,\n" +
                "            \"dateCreated\": \"2018-09-04T15:47:53.950+03:00\",\n" +
                "            \"serverVersion\": 1536065273885,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"87a04889-4abd-42e0-a46e-4a40fa929206\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 13,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065273885,\n" +
                "            \"dateCreated\": \"2018-09-04T15:47:53.952+03:00\",\n" +
                "            \"serverVersion\": 1536065273885,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"34a25254-b9c1-4de6-90af-8c3f09c69d46\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 14,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065273885,\n" +
                "            \"dateCreated\": \"2018-09-04T15:47:53.954+03:00\",\n" +
                "            \"serverVersion\": 1536065273885,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"e7c0001a-de03-45f7-9494-d27bf94993cb\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 15,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065273885,\n" +
                "            \"dateCreated\": \"2018-09-04T15:47:53.996+03:00\",\n" +
                "            \"serverVersion\": 1536065273885,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"99d94cb2-f1af-4fa4-afb9-62b488307b57\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 16,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065273885,\n" +
                "            \"dateCreated\": \"2018-09-04T15:47:53.999+03:00\",\n" +
                "            \"serverVersion\": 1536065273885,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"fd5fd46d-73f7-4e38-aca2-205f3e604f69\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 17,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065273885,\n" +
                "            \"dateCreated\": \"2018-09-04T15:47:54.001+03:00\",\n" +
                "            \"serverVersion\": 1536065273885,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"0d28a1dc-e185-4a6a-8eec-3436535edf8c\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 18,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065273885,\n" +
                "            \"dateCreated\": \"2018-09-04T15:47:54.046+03:00\",\n" +
                "            \"serverVersion\": 1536065273885,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"6d06b415-a019-4798-89f7-7e993593fd9d\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 19,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065273885,\n" +
                "            \"dateCreated\": \"2018-09-04T15:47:54.090+03:00\",\n" +
                "            \"serverVersion\": 1536065273885,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"7a85ca07-c260-43eb-ac96-fb80ecc8c8bd\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 20,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065273885,\n" +
                "            \"dateCreated\": \"2018-09-04T15:47:54.092+03:00\",\n" +
                "            \"serverVersion\": 1536065273885,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"876984e4-b885-41a0-8e11-f4ac39249121\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 21,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065273885,\n" +
                "            \"dateCreated\": \"2018-09-04T15:47:54.093+03:00\",\n" +
                "            \"serverVersion\": 1536065273885,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"65278188-cbb1-4869-bc94-8b4e9dddd0ba\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 22,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065273885,\n" +
                "            \"dateCreated\": \"2018-09-04T15:47:54.095+03:00\",\n" +
                "            \"serverVersion\": 1536065273885,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"8a441dee-6d5c-4925-96f1-162306de05b3\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 23,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065273885,\n" +
                "            \"dateCreated\": \"2018-09-04T15:47:54.097+03:00\",\n" +
                "            \"serverVersion\": 1536065273885,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"23ff0d64-9ff0-413d-9b3d-95f25ff2a218\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 24,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065273885,\n" +
                "            \"dateCreated\": \"2018-09-04T15:47:54.098+03:00\",\n" +
                "            \"serverVersion\": 1536065273885,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"94168385-f52c-44d9-84d5-78a6f423d8e1\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 25,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065273885,\n" +
                "            \"dateCreated\": \"2018-09-04T15:47:54.100+03:00\",\n" +
                "            \"serverVersion\": 1536065273885,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"77c59abc-781b-4305-b1db-40ff11d135d2\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 26,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065273885,\n" +
                "            \"dateCreated\": \"2018-09-04T15:47:54.143+03:00\",\n" +
                "            \"serverVersion\": 1536065273885,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"6ec8dd6b-9dcf-42db-aeda-d9843abf7994\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 27,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065273885,\n" +
                "            \"dateCreated\": \"2018-09-04T15:47:54.145+03:00\",\n" +
                "            \"serverVersion\": 1536065273885,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"4a304a12-a095-4a54-8130-81fdadec9580\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 1,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065349157,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:09.157+03:00\",\n" +
                "            \"serverVersion\": 1536065349157,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"6c7df7a9-8a32-4891-8beb-699807cc46aa\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 2,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065349157,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:09.207+03:00\",\n" +
                "            \"serverVersion\": 1536065349157,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"8d52727e-ebdc-4a86-bde9-d9700cb31eda\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 3,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065349157,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:09.209+03:00\",\n" +
                "            \"serverVersion\": 1536065349157,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"dce4995a-8ab4-456b-8bb1-0bc93525ec01\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 4,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065349157,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:09.213+03:00\",\n" +
                "            \"serverVersion\": 1536065349157,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"f6d7e360-e1d6-437a-bd1b-44ce0a943578\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 5,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065349157,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:09.216+03:00\",\n" +
                "            \"serverVersion\": 1536065349157,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"1fd4ba1b-9a40-4693-ae20-f77ca075b013\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 6,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065349157,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:09.218+03:00\",\n" +
                "            \"serverVersion\": 1536065349157,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"abcffc86-0427-4958-b13c-e3f643db7666\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 7,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065349157,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:09.220+03:00\",\n" +
                "            \"serverVersion\": 1536065349157,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"669aba70-49c2-45c7-8f6b-e422d213c4b1\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 8,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065349157,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:09.222+03:00\",\n" +
                "            \"serverVersion\": 1536065349157,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"6d41a65e-1b25-4e7b-8a96-2f6dfb16d4fa\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 9,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065349157,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:09.251+03:00\",\n" +
                "            \"serverVersion\": 1536065349157,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"544ff117-b4ea-4743-9f85-60dd72e6decf\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 10,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065349157,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:09.280+03:00\",\n" +
                "            \"serverVersion\": 1536065349157,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"9ca9ca13-95a4-4a73-b2c3-521a77ced8c2\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 11,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065349157,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:09.283+03:00\",\n" +
                "            \"serverVersion\": 1536065349157,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"00a2378e-ded5-4006-bd5a-a1d8fd2b4df4\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 12,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065349157,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:09.314+03:00\",\n" +
                "            \"serverVersion\": 1536065349157,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"4590ebf4-515a-4ad2-b75d-6cf1314841dd\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 13,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065349157,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:09.316+03:00\",\n" +
                "            \"serverVersion\": 1536065349157,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"b3314ad0-579b-4994-9810-8ce44764f066\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 14,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065349157,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:09.344+03:00\",\n" +
                "            \"serverVersion\": 1536065349157,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"e0c1c20b-53ac-4e11-99a0-396d903dd953\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 15,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065349157,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:09.378+03:00\",\n" +
                "            \"serverVersion\": 1536065349157,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"5467cb35-a12e-4486-9735-d4a612041c3b\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 16,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065349157,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:09.381+03:00\",\n" +
                "            \"serverVersion\": 1536065349157,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"517b2599-c076-49f3-af48-a2114c8ff721\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 17,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065349157,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:09.383+03:00\",\n" +
                "            \"serverVersion\": 1536065349157,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"07a18ea8-3167-4735-92db-e4401f160d7f\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 18,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065349157,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:09.411+03:00\",\n" +
                "            \"serverVersion\": 1536065349157,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"2e73b8a3-fe34-4f6c-840b-be84b5110928\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 19,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065349157,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:09.414+03:00\",\n" +
                "            \"serverVersion\": 1536065349157,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"fb2ba737-9aa2-462c-88bc-07ba6e933a16\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 20,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065349157,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:09.417+03:00\",\n" +
                "            \"serverVersion\": 1536065349157,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"4caed333-aa6a-4e4b-9b4f-8e5c5d950678\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 21,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065349157,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:09.481+03:00\",\n" +
                "            \"serverVersion\": 1536065349157,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"c4e59b67-02ae-4a5b-87ac-4e0931786b15\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 22,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065349157,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:09.484+03:00\",\n" +
                "            \"serverVersion\": 1536065349157,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"68b2a122-e45d-47fe-8a67-3556ae89e302\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 23,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065349157,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:09.487+03:00\",\n" +
                "            \"serverVersion\": 1536065349157,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"6c807f41-8ce4-464e-a211-6e78e82d6da8\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 24,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065349157,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:09.531+03:00\",\n" +
                "            \"serverVersion\": 1536065349157,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"b6e0af49-1234-4cf2-b693-feae137066d7\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 25,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065349157,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:09.533+03:00\",\n" +
                "            \"serverVersion\": 1536065349157,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"57f823ac-c0e4-4077-a89c-1718638d0d71\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 26,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065349157,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:09.535+03:00\",\n" +
                "            \"serverVersion\": 1536065349157,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"99df5bd0-0872-467c-92ef-8d39c7b6709a\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 27,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065349157,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:09.537+03:00\",\n" +
                "            \"serverVersion\": 1536065349157,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"8e93b099-2f4b-4e7b-aac3-403f602c8c7f\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 28,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065349157,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:09.538+03:00\",\n" +
                "            \"serverVersion\": 1536065349157,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"32d1438e-95a4-4e18-9146-b10fa8d5a224\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 29,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065349157,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:09.540+03:00\",\n" +
                "            \"serverVersion\": 1536065349157,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"3064b975-ff8d-4178-a4e2-548be16ae8e7\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 30,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065349157,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:09.542+03:00\",\n" +
                "            \"serverVersion\": 1536065349157,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"28157a8d-be36-4230-8d57-59acf883b742\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 1,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065392151,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:52.152+03:00\",\n" +
                "            \"serverVersion\": 1536065392151,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"dbb7b20b-27e2-4612-9be1-cfbf2387f151\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 2,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065392151,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:52.156+03:00\",\n" +
                "            \"serverVersion\": 1536065392151,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"4b76da33-f610-44a2-bf81-f26f9d15cb10\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 3,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065392151,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:52.158+03:00\",\n" +
                "            \"serverVersion\": 1536065392151,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"edf727c4-e20f-464b-9a93-8813ffb1f648\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 4,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065392151,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:52.160+03:00\",\n" +
                "            \"serverVersion\": 1536065392151,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"37fc3197-4bde-4c53-a960-61c42917df4d\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 5,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065392151,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:52.161+03:00\",\n" +
                "            \"serverVersion\": 1536065392151,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"c75e190d-8a09-4225-b63f-878865e33ce3\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 6,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065392151,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:52.163+03:00\",\n" +
                "            \"serverVersion\": 1536065392151,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"a39ec639-34b5-4474-82d3-e4330e38b660\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 7,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065392151,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:52.165+03:00\",\n" +
                "            \"serverVersion\": 1536065392151,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"c41b132d-1dba-4a55-9810-e94b719fae4f\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 8,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065392151,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:52.166+03:00\",\n" +
                "            \"serverVersion\": 1536065392151,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"507dd8e3-5a62-4a9b-96a4-de92bc7b85f0\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 9,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065392151,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:52.168+03:00\",\n" +
                "            \"serverVersion\": 1536065392151,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"b46e0a52-f363-4922-be04-b2e0456caf6d\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 10,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065392151,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:52.170+03:00\",\n" +
                "            \"serverVersion\": 1536065392151,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"76aee27c-27c6-42d1-884a-b644a17f07ea\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 11,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065392151,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:52.213+03:00\",\n" +
                "            \"serverVersion\": 1536065392151,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"6b7f1506-141f-42b0-a7d1-d6a8ee73fe74\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 12,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065392151,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:52.215+03:00\",\n" +
                "            \"serverVersion\": 1536065392151,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"fadaea42-163e-47bc-a9cb-4ccc587ce537\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 13,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065392151,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:52.217+03:00\",\n" +
                "            \"serverVersion\": 1536065392151,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"c58d6607-d169-453d-94bc-ab56d23afc9c\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 14,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065392151,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:52.219+03:00\",\n" +
                "            \"serverVersion\": 1536065392151,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"923bb907-b0fb-4be3-b665-084d5b52377c\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 15,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065392151,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:52.261+03:00\",\n" +
                "            \"serverVersion\": 1536065392151,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"1919465c-dca3-40b9-b79a-fee6fc56bf8c\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 16,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065392151,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:52.263+03:00\",\n" +
                "            \"serverVersion\": 1536065392151,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"07201e5b-3321-430e-8b35-2645be419701\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 17,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065392151,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:52.267+03:00\",\n" +
                "            \"serverVersion\": 1536065392151,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"0ecea21a-3905-46aa-8d1c-2cfe4fe1180c\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 18,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065392151,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:52.271+03:00\",\n" +
                "            \"serverVersion\": 1536065392151,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"5077284f-6971-401e-97ce-e51f712aa531\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 19,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065392151,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:52.273+03:00\",\n" +
                "            \"serverVersion\": 1536065392151,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"6643bafc-b259-49de-b704-0aa6369f89ed\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 20,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065392151,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:52.274+03:00\",\n" +
                "            \"serverVersion\": 1536065392151,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"68616d92-22aa-4198-8b4d-eeaf74c693c7\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 21,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065392151,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:52.276+03:00\",\n" +
                "            \"serverVersion\": 1536065392151,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"acf8ca46-fd6f-4fad-9898-e031d09325dd\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 22,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065392151,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:52.278+03:00\",\n" +
                "            \"serverVersion\": 1536065392151,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"82ea5952-c5d4-4de6-af94-1f95716ddfed\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 23,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065392151,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:52.279+03:00\",\n" +
                "            \"serverVersion\": 1536065392151,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"bb4532aa-c4df-4f38-a179-497422d74405\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 24,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065392151,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:52.321+03:00\",\n" +
                "            \"serverVersion\": 1536065392151,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"4e7559c6-6bbd-4675-a75e-5958fdc16030\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 25,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065392151,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:52.364+03:00\",\n" +
                "            \"serverVersion\": 1536065392151,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"3abb5b3a-b3af-4056-a5f3-624e9cf10181\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 26,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065392151,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:52.366+03:00\",\n" +
                "            \"serverVersion\": 1536065392151,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"78f7162c-0008-4d95-89c6-bbb1ae2791c6\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 27,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065392151,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:52.368+03:00\",\n" +
                "            \"serverVersion\": 1536065392151,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"c954e868-055c-4ee5-8a69-8a1852987b99\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 28,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065392151,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:52.369+03:00\",\n" +
                "            \"serverVersion\": 1536065392151,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"36be08fa-8e30-41b8-a158-2340608d18d6\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 29,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065392151,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:52.371+03:00\",\n" +
                "            \"serverVersion\": 1536065392151,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"1c20f7df-56d6-479c-834d-c8c8a07d3b67\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 30,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065392151,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:52.373+03:00\",\n" +
                "            \"serverVersion\": 1536065392151,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"40d4641a-b88a-4e07-b83d-67c949446ae2\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 31,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065392151,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:52.375+03:00\",\n" +
                "            \"serverVersion\": 1536065392151,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"9ea9e852-0ec3-4e77-ab10-95b62e294032\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 32,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065392151,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:52.378+03:00\",\n" +
                "            \"serverVersion\": 1536065392151,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"28a0ff66-0af5-4221-b929-7d4102dd41dc\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 33,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065392151,\n" +
                "            \"dateCreated\": \"2018-09-04T15:49:52.423+03:00\",\n" +
                "            \"serverVersion\": 1536065392151,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"04ea8c3b-6220-4044-bd32-088948fa5d7f\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 1,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065440753,\n" +
                "            \"dateCreated\": \"2018-09-04T15:50:40.754+03:00\",\n" +
                "            \"serverVersion\": 1536065440753,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"ea5512ab-9663-4fa6-8a21-d73fb6adf008\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 2,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065440753,\n" +
                "            \"dateCreated\": \"2018-09-04T15:50:40.936+03:00\",\n" +
                "            \"serverVersion\": 1536065440753,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"37220d81-8f64-4333-a6a7-b8543e688551\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 3,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065440753,\n" +
                "            \"dateCreated\": \"2018-09-04T15:50:40.982+03:00\",\n" +
                "            \"serverVersion\": 1536065440753,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"28e04aac-60de-4333-8a8c-7519afa829d5\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 4,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065440754,\n" +
                "            \"dateCreated\": \"2018-09-04T15:50:40.995+03:00\",\n" +
                "            \"serverVersion\": 1536065440754,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"633ba8fa-6a4e-469e-8982-fa80d6789396\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 5,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065440754,\n" +
                "            \"dateCreated\": \"2018-09-04T15:50:40.998+03:00\",\n" +
                "            \"serverVersion\": 1536065440754,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"50d9c69f-59a6-4a2a-bc15-42728cad30b6\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 6,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065440754,\n" +
                "            \"dateCreated\": \"2018-09-04T15:50:41.001+03:00\",\n" +
                "            \"serverVersion\": 1536065440754,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"69591e43-e673-4d1c-982b-86cdbff60da8\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 7,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065440754,\n" +
                "            \"dateCreated\": \"2018-09-04T15:50:41.004+03:00\",\n" +
                "            \"serverVersion\": 1536065440754,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"3c1be502-cf8c-4e66-b98d-696592d635ec\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 8,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065440754,\n" +
                "            \"dateCreated\": \"2018-09-04T15:50:41.006+03:00\",\n" +
                "            \"serverVersion\": 1536065440754,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"fbdad326-e8d3-4315-bb63-75ff2fc55f8a\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 9,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065440754,\n" +
                "            \"dateCreated\": \"2018-09-04T15:50:41.008+03:00\",\n" +
                "            \"serverVersion\": 1536065440754,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"17a4e982-3b21-4b8c-89ec-bdbf3c271fab\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 10,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065440754,\n" +
                "            \"dateCreated\": \"2018-09-04T15:50:41.012+03:00\",\n" +
                "            \"serverVersion\": 1536065440754,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"d6820d29-c42a-4ff4-907a-30368528fabf\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 11,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065440754,\n" +
                "            \"dateCreated\": \"2018-09-04T15:50:41.015+03:00\",\n" +
                "            \"serverVersion\": 1536065440754,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"82449f46-116b-494b-a4b5-387b451fc8fc\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 12,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065440754,\n" +
                "            \"dateCreated\": \"2018-09-04T15:50:41.019+03:00\",\n" +
                "            \"serverVersion\": 1536065440754,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"71c881a7-ff27-47b5-8004-1afce431dbd3\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 13,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065440754,\n" +
                "            \"dateCreated\": \"2018-09-04T15:50:41.025+03:00\",\n" +
                "            \"serverVersion\": 1536065440754,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"2e471748-09a0-4f0c-a3f3-f4b98d348f88\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 14,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065440754,\n" +
                "            \"dateCreated\": \"2018-09-04T15:50:41.113+03:00\",\n" +
                "            \"serverVersion\": 1536065440754,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"975c210f-6f69-45d5-a2b2-0da84c20cc05\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 15,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065440754,\n" +
                "            \"dateCreated\": \"2018-09-04T15:50:41.233+03:00\",\n" +
                "            \"serverVersion\": 1536065440754,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"4cfa5b79-18c3-4c9b-a762-23afbf3213c7\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 16,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065440754,\n" +
                "            \"dateCreated\": \"2018-09-04T15:50:41.316+03:00\",\n" +
                "            \"serverVersion\": 1536065440754,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"6536bcd4-dbf4-470a-a300-fca0976684ee\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 17,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065440754,\n" +
                "            \"dateCreated\": \"2018-09-04T15:50:41.332+03:00\",\n" +
                "            \"serverVersion\": 1536065440754,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"84710d0f-be8f-4d04-b334-a6083f765340\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 18,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065440754,\n" +
                "            \"dateCreated\": \"2018-09-04T15:50:41.336+03:00\",\n" +
                "            \"serverVersion\": 1536065440754,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"d1523c33-e18a-4660-861c-7a2d146d7c8a\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 19,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065440754,\n" +
                "            \"dateCreated\": \"2018-09-04T15:50:41.348+03:00\",\n" +
                "            \"serverVersion\": 1536065440754,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"6c815fc7-c1d8-4ca3-812d-b8747e367a3e\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 20,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065440754,\n" +
                "            \"dateCreated\": \"2018-09-04T15:50:41.351+03:00\",\n" +
                "            \"serverVersion\": 1536065440754,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"760ea65c-942b-4d88-a68f-0c0f5cab9d05\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 21,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065440754,\n" +
                "            \"dateCreated\": \"2018-09-04T15:50:41.357+03:00\",\n" +
                "            \"serverVersion\": 1536065440754,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"7a7028a1-7376-4af2-8de0-6c61814d8868\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 22,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065440754,\n" +
                "            \"dateCreated\": \"2018-09-04T15:50:41.400+03:00\",\n" +
                "            \"serverVersion\": 1536065440754,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"75ab3e9b-e7b1-49b7-a9e5-fb1eeb49cbb5\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 23,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065440754,\n" +
                "            \"dateCreated\": \"2018-09-04T15:50:41.402+03:00\",\n" +
                "            \"serverVersion\": 1536065440754,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"ac12cf4e-4b11-4b42-94da-c5884013faf1\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 24,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065440754,\n" +
                "            \"dateCreated\": \"2018-09-04T15:50:41.404+03:00\",\n" +
                "            \"serverVersion\": 1536065440754,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"e53bf4f3-93ab-4ccf-a4f4-da68fcdc5b2e\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 25,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065440754,\n" +
                "            \"dateCreated\": \"2018-09-04T15:50:41.405+03:00\",\n" +
                "            \"serverVersion\": 1536065440754,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"13229ace-28d9-4f62-bf40-226adff31500\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 26,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065440754,\n" +
                "            \"dateCreated\": \"2018-09-04T15:50:41.407+03:00\",\n" +
                "            \"serverVersion\": 1536065440754,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"ebb69b9f-8800-445e-9a57-797610c0ba9d\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 27,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065440754,\n" +
                "            \"dateCreated\": \"2018-09-04T15:50:41.449+03:00\",\n" +
                "            \"serverVersion\": 1536065440754,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"2f16676e-5565-4953-b8f3-bce1a4067d53\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 28,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065440754,\n" +
                "            \"dateCreated\": \"2018-09-04T15:50:41.452+03:00\",\n" +
                "            \"serverVersion\": 1536065440754,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"c47eea3a-56af-4fcb-ac0c-47b6d75d048a\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 29,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065440754,\n" +
                "            \"dateCreated\": \"2018-09-04T15:50:41.454+03:00\",\n" +
                "            \"serverVersion\": 1536065440754,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"83789285-edd5-42ee-b474-4fed0fd10484\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 30,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065440754,\n" +
                "            \"dateCreated\": \"2018-09-04T15:50:41.455+03:00\",\n" +
                "            \"serverVersion\": 1536065440754,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"0087a9d1-0c3f-4143-92f4-57ac77da0998\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 31,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065440754,\n" +
                "            \"dateCreated\": \"2018-09-04T15:50:41.457+03:00\",\n" +
                "            \"serverVersion\": 1536065440754,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"aa48fa97-cc8a-4bfd-866b-37a3e714e887\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 32,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065440754,\n" +
                "            \"dateCreated\": \"2018-09-04T15:50:41.459+03:00\",\n" +
                "            \"serverVersion\": 1536065440754,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"e47c55b6-587a-44ed-84b2-3757e60fa720\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 33,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065440754,\n" +
                "            \"dateCreated\": \"2018-09-04T15:50:41.467+03:00\",\n" +
                "            \"serverVersion\": 1536065440754,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"ce4915f1-3a65-44ff-9e5e-8ed1e2ed2622\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 34,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065440754,\n" +
                "            \"dateCreated\": \"2018-09-04T15:50:41.469+03:00\",\n" +
                "            \"serverVersion\": 1536065440754,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"ae9edc51-8aa3-4eb3-b701-1c9bcab23518\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 35,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065440754,\n" +
                "            \"dateCreated\": \"2018-09-04T15:50:41.470+03:00\",\n" +
                "            \"serverVersion\": 1536065440754,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"5c20fcf6-612d-4b56-8c7c-f1ebdc656157\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 36,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065440754,\n" +
                "            \"dateCreated\": \"2018-09-04T15:50:41.472+03:00\",\n" +
                "            \"serverVersion\": 1536065440754,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"cdacf444-b9b5-4f07-9b65-20d5ce13a872\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 1,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065510081,\n" +
                "            \"dateCreated\": \"2018-09-04T15:51:50.082+03:00\",\n" +
                "            \"serverVersion\": 1536065510081,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"707ec698-4591-454a-b1ee-7e090c12f954\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 2,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065510081,\n" +
                "            \"dateCreated\": \"2018-09-04T15:51:50.209+03:00\",\n" +
                "            \"serverVersion\": 1536065510081,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"840f9f5e-c79b-4106-af54-b551a024de28\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 3,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065510081,\n" +
                "            \"dateCreated\": \"2018-09-04T15:51:50.213+03:00\",\n" +
                "            \"serverVersion\": 1536065510081,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"e6690172-92a2-4782-9528-2498af8e63e8\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 4,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065510081,\n" +
                "            \"dateCreated\": \"2018-09-04T15:51:50.217+03:00\",\n" +
                "            \"serverVersion\": 1536065510081,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"3d814eb3-82de-4f69-80f6-a064ac72807d\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 5,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065510081,\n" +
                "            \"dateCreated\": \"2018-09-04T15:51:50.220+03:00\",\n" +
                "            \"serverVersion\": 1536065510081,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"c843b0da-ee66-406b-8632-a0234b6f0b88\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 6,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065510081,\n" +
                "            \"dateCreated\": \"2018-09-04T15:51:50.222+03:00\",\n" +
                "            \"serverVersion\": 1536065510081,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"d01bd4fc-13c6-4b7c-b76b-ebb18020ac5b\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 7,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065510081,\n" +
                "            \"dateCreated\": \"2018-09-04T15:51:50.224+03:00\",\n" +
                "            \"serverVersion\": 1536065510081,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"b4274e80-6247-40e7-830d-2a036963a5cb\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 8,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065510081,\n" +
                "            \"dateCreated\": \"2018-09-04T15:51:50.268+03:00\",\n" +
                "            \"serverVersion\": 1536065510081,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"f0ac38d8-32ab-4cb5-83e4-a6a4c9e32eca\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 9,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065510081,\n" +
                "            \"dateCreated\": \"2018-09-04T15:51:50.270+03:00\",\n" +
                "            \"serverVersion\": 1536065510081,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"cfb66343-cd22-40b6-b075-503be270c7d1\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 10,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065510081,\n" +
                "            \"dateCreated\": \"2018-09-04T15:51:50.272+03:00\",\n" +
                "            \"serverVersion\": 1536065510081,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"349e209a-ba46-4e6d-82bb-03872b389ec7\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 11,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065510081,\n" +
                "            \"dateCreated\": \"2018-09-04T15:51:50.275+03:00\",\n" +
                "            \"serverVersion\": 1536065510081,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"892279fa-0285-408c-8c4c-ebbce828fdb6\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 12,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065510081,\n" +
                "            \"dateCreated\": \"2018-09-04T15:51:50.277+03:00\",\n" +
                "            \"serverVersion\": 1536065510081,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"572f6ba1-363e-4afd-9342-061a2ceff130\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 13,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065510081,\n" +
                "            \"dateCreated\": \"2018-09-04T15:51:50.279+03:00\",\n" +
                "            \"serverVersion\": 1536065510081,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"fe74d404-9468-44b0-8339-7714439fc0ad\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 14,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065510081,\n" +
                "            \"dateCreated\": \"2018-09-04T15:51:50.281+03:00\",\n" +
                "            \"serverVersion\": 1536065510081,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"df03e731-878f-4bed-9ce4-63e66c6eec39\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 15,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065510081,\n" +
                "            \"dateCreated\": \"2018-09-04T15:51:50.283+03:00\",\n" +
                "            \"serverVersion\": 1536065510081,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"a089a234-8e44-4c9d-92bd-8da938fea3fe\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 16,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065510081,\n" +
                "            \"dateCreated\": \"2018-09-04T15:51:50.286+03:00\",\n" +
                "            \"serverVersion\": 1536065510081,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"675cae53-e658-4a43-8d87-2ef634af8f74\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 17,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065510081,\n" +
                "            \"dateCreated\": \"2018-09-04T15:51:50.289+03:00\",\n" +
                "            \"serverVersion\": 1536065510081,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"aa94abc1-9bc3-4d84-816f-8b9bf2bc5748\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 18,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065510081,\n" +
                "            \"dateCreated\": \"2018-09-04T15:51:50.293+03:00\",\n" +
                "            \"serverVersion\": 1536065510081,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"63f5f420-580c-4348-87d4-19c2b7c074ee\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 19,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065510081,\n" +
                "            \"dateCreated\": \"2018-09-04T15:51:50.295+03:00\",\n" +
                "            \"serverVersion\": 1536065510081,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"0b8204fc-ace5-4ad8-84a0-9be5406e2826\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 20,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065510081,\n" +
                "            \"dateCreated\": \"2018-09-04T15:51:50.297+03:00\",\n" +
                "            \"serverVersion\": 1536065510081,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"473afcea-0822-459a-960b-2535b1a9c63d\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 21,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065510081,\n" +
                "            \"dateCreated\": \"2018-09-04T15:51:50.300+03:00\",\n" +
                "            \"serverVersion\": 1536065510081,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"3cbcab4f-01f5-4187-aa28-bafec527077b\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 22,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065510081,\n" +
                "            \"dateCreated\": \"2018-09-04T15:51:50.302+03:00\",\n" +
                "            \"serverVersion\": 1536065510081,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"e0930b42-8ad8-426d-8bdf-959e9349c098\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 23,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065510081,\n" +
                "            \"dateCreated\": \"2018-09-04T15:51:50.304+03:00\",\n" +
                "            \"serverVersion\": 1536065510081,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"24ee0626-5d17-401c-ba73-607be241d715\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 24,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065510081,\n" +
                "            \"dateCreated\": \"2018-09-04T15:51:50.306+03:00\",\n" +
                "            \"serverVersion\": 1536065510081,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"8d864c40-4693-40fe-b2f4-7fdf2db9bd58\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 25,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065510081,\n" +
                "            \"dateCreated\": \"2018-09-04T15:51:50.308+03:00\",\n" +
                "            \"serverVersion\": 1536065510081,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"99450653-9e73-418f-9376-c0adf64e4b37\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 26,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065510081,\n" +
                "            \"dateCreated\": \"2018-09-04T15:51:50.311+03:00\",\n" +
                "            \"serverVersion\": 1536065510081,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"7b1e6a59-674d-496a-b108-feec03d9970e\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 27,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065510081,\n" +
                "            \"dateCreated\": \"2018-09-04T15:51:50.313+03:00\",\n" +
                "            \"serverVersion\": 1536065510081,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"153b1749-5ab1-48f1-85c9-ca4a09ac52a8\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 28,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065510081,\n" +
                "            \"dateCreated\": \"2018-09-04T15:51:50.315+03:00\",\n" +
                "            \"serverVersion\": 1536065510081,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"307db7ff-7571-4ebd-afaa-f1d6d28f36a3\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 29,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065510081,\n" +
                "            \"dateCreated\": \"2018-09-04T15:51:50.317+03:00\",\n" +
                "            \"serverVersion\": 1536065510081,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"7fcc84a1-95d9-4028-8ac5-b0eb6f807bfc\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 30,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065510081,\n" +
                "            \"dateCreated\": \"2018-09-04T15:51:50.319+03:00\",\n" +
                "            \"serverVersion\": 1536065510081,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"69f5367f-46a4-4b69-b009-0e0f752f5147\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 31,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065510081,\n" +
                "            \"dateCreated\": \"2018-09-04T15:51:50.321+03:00\",\n" +
                "            \"serverVersion\": 1536065510081,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"f25d853a-ade3-4c4b-b98f-6a24751dff1a\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 32,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065510081,\n" +
                "            \"dateCreated\": \"2018-09-04T15:51:50.323+03:00\",\n" +
                "            \"serverVersion\": 1536065510081,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"6c757429-e5d1-45ca-87f9-f4a7d0815913\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 33,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065510081,\n" +
                "            \"dateCreated\": \"2018-09-04T15:51:50.325+03:00\",\n" +
                "            \"serverVersion\": 1536065510081,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"d803e930-bb09-4df5-997f-0ce95e168298\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 34,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065510081,\n" +
                "            \"dateCreated\": \"2018-09-04T15:51:50.327+03:00\",\n" +
                "            \"serverVersion\": 1536065510081,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"22d33b43-e66a-4ccd-aefa-0392d0d94615\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 35,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065510081,\n" +
                "            \"dateCreated\": \"2018-09-04T15:51:50.329+03:00\",\n" +
                "            \"serverVersion\": 1536065510081,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"f872c622-f77b-4728-99a4-bda219692105\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 36,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065510082,\n" +
                "            \"dateCreated\": \"2018-09-04T15:51:50.330+03:00\",\n" +
                "            \"serverVersion\": 1536065510082,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"ced039c3-4dde-4887-9ecd-f9d6a296b31a\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 37,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065510082,\n" +
                "            \"dateCreated\": \"2018-09-04T15:51:50.332+03:00\",\n" +
                "            \"serverVersion\": 1536065510082,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"223f7c15-884a-4697-bf1e-c04c71e51c9d\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 38,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065510082,\n" +
                "            \"dateCreated\": \"2018-09-04T15:51:50.374+03:00\",\n" +
                "            \"serverVersion\": 1536065510082,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"2109c52c-fa07-4747-98ad-9762e0f39b0b\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 39,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065510082,\n" +
                "            \"dateCreated\": \"2018-09-04T15:51:50.376+03:00\",\n" +
                "            \"serverVersion\": 1536065510082,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"72d99edc-1431-4fc9-b572-8cee7fb92487\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 1,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065560210,\n" +
                "            \"dateCreated\": \"2018-09-04T15:52:40.210+03:00\",\n" +
                "            \"serverVersion\": 1536065560210,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"6e6d009d-bced-40ee-a6bf-a168a7f78eac\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 2,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065560210,\n" +
                "            \"dateCreated\": \"2018-09-04T15:52:40.224+03:00\",\n" +
                "            \"serverVersion\": 1536065560210,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"cc96b93b-47c3-4181-86c0-33b6a06a3029\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 3,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065560210,\n" +
                "            \"dateCreated\": \"2018-09-04T15:52:40.231+03:00\",\n" +
                "            \"serverVersion\": 1536065560210,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"b7a9bd53-d5c2-4000-a91f-563ab6bbc4b3\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 4,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065560210,\n" +
                "            \"dateCreated\": \"2018-09-04T15:52:40.235+03:00\",\n" +
                "            \"serverVersion\": 1536065560210,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"31574752-b044-41d5-bbdb-aea5e1b8f42f\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 5,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065560210,\n" +
                "            \"dateCreated\": \"2018-09-04T15:52:40.241+03:00\",\n" +
                "            \"serverVersion\": 1536065560210,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"fc4b3e67-8457-4cdf-bec8-4a3fabe24f82\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 6,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065560210,\n" +
                "            \"dateCreated\": \"2018-09-04T15:52:40.243+03:00\",\n" +
                "            \"serverVersion\": 1536065560210,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"1514c84a-27a9-47e6-bc43-696c51671695\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 7,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065560210,\n" +
                "            \"dateCreated\": \"2018-09-04T15:52:40.253+03:00\",\n" +
                "            \"serverVersion\": 1536065560210,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"c7ff3870-aecb-4b6a-8f25-cdf1c7d656e1\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 8,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065560210,\n" +
                "            \"dateCreated\": \"2018-09-04T15:52:40.275+03:00\",\n" +
                "            \"serverVersion\": 1536065560210,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"eabbd0ba-1add-4b76-8bf9-72b1cc2facc1\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 9,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065560210,\n" +
                "            \"dateCreated\": \"2018-09-04T15:52:40.279+03:00\",\n" +
                "            \"serverVersion\": 1536065560210,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"7bc2577e-c472-4680-841c-c09c1a7a44be\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 10,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065560210,\n" +
                "            \"dateCreated\": \"2018-09-04T15:52:40.282+03:00\",\n" +
                "            \"serverVersion\": 1536065560210,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"0b9817e3-be50-49c9-9d3a-6fcea6597930\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 11,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065560210,\n" +
                "            \"dateCreated\": \"2018-09-04T15:52:40.284+03:00\",\n" +
                "            \"serverVersion\": 1536065560210,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"3cb13f56-116a-4ed1-8262-99043649d59c\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 12,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065560210,\n" +
                "            \"dateCreated\": \"2018-09-04T15:52:40.287+03:00\",\n" +
                "            \"serverVersion\": 1536065560210,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"f91c716e-4af8-4f9c-ab2c-0b5d54c84cb6\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 13,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065560210,\n" +
                "            \"dateCreated\": \"2018-09-04T15:52:40.289+03:00\",\n" +
                "            \"serverVersion\": 1536065560210,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"500cdecf-a53b-4124-8c5c-53b752b68987\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 14,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065560210,\n" +
                "            \"dateCreated\": \"2018-09-04T15:52:40.299+03:00\",\n" +
                "            \"serverVersion\": 1536065560210,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"c6974a47-4717-4fdd-8439-9bbf336ee385\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 15,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065560210,\n" +
                "            \"dateCreated\": \"2018-09-04T15:52:40.312+03:00\",\n" +
                "            \"serverVersion\": 1536065560210,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"02df1424-aede-4b49-abe9-3a0d3f199fc3\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 16,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065560210,\n" +
                "            \"dateCreated\": \"2018-09-04T15:52:40.320+03:00\",\n" +
                "            \"serverVersion\": 1536065560210,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"90f75cc9-5922-489b-8251-845777d9ff65\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 17,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065560210,\n" +
                "            \"dateCreated\": \"2018-09-04T15:52:40.329+03:00\",\n" +
                "            \"serverVersion\": 1536065560210,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"f82da0b2-3285-4a3c-a101-753dbef3a31a\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 18,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065560210,\n" +
                "            \"dateCreated\": \"2018-09-04T15:52:40.380+03:00\",\n" +
                "            \"serverVersion\": 1536065560210,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"38c09a75-65d4-45f4-aec7-d09775718e29\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 19,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065560210,\n" +
                "            \"dateCreated\": \"2018-09-04T15:52:40.395+03:00\",\n" +
                "            \"serverVersion\": 1536065560210,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"b179eb03-87ea-4cad-b5ad-2f8b9b43f332\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 20,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065560210,\n" +
                "            \"dateCreated\": \"2018-09-04T15:52:40.399+03:00\",\n" +
                "            \"serverVersion\": 1536065560210,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"c09c7826-0634-465f-82d3-ee73bba79e11\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 21,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065560210,\n" +
                "            \"dateCreated\": \"2018-09-04T15:52:40.406+03:00\",\n" +
                "            \"serverVersion\": 1536065560210,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"123834e0-c0f7-4fcd-9f63-91657c335993\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 22,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065560210,\n" +
                "            \"dateCreated\": \"2018-09-04T15:52:40.410+03:00\",\n" +
                "            \"serverVersion\": 1536065560210,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"6928fced-a7b4-4a64-a795-fea901a68aa0\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 23,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065560210,\n" +
                "            \"dateCreated\": \"2018-09-04T15:52:40.413+03:00\",\n" +
                "            \"serverVersion\": 1536065560210,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"209288fd-a19b-4319-aab9-fc3a8c29f22f\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 24,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065560210,\n" +
                "            \"dateCreated\": \"2018-09-04T15:52:40.426+03:00\",\n" +
                "            \"serverVersion\": 1536065560210,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"2db2b1f6-240a-4d1b-8b98-b607aca52055\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 25,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065560210,\n" +
                "            \"dateCreated\": \"2018-09-04T15:52:40.431+03:00\",\n" +
                "            \"serverVersion\": 1536065560210,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"c1cbd804-52d2-477c-af96-9a07fbf3eefc\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 26,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065560210,\n" +
                "            \"dateCreated\": \"2018-09-04T15:52:40.528+03:00\",\n" +
                "            \"serverVersion\": 1536065560210,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"9988dddf-b4fd-45ea-aeec-aa2721bb1e4a\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 27,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065560210,\n" +
                "            \"dateCreated\": \"2018-09-04T15:52:40.566+03:00\",\n" +
                "            \"serverVersion\": 1536065560210,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"969f8996-a824-41df-b854-4a41344afcf7\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 28,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065560210,\n" +
                "            \"dateCreated\": \"2018-09-04T15:52:40.574+03:00\",\n" +
                "            \"serverVersion\": 1536065560210,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"27643398-371a-44c9-bf3e-d70c1b8ae602\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 29,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065560210,\n" +
                "            \"dateCreated\": \"2018-09-04T15:52:40.582+03:00\",\n" +
                "            \"serverVersion\": 1536065560210,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"dd8fa86f-6846-4140-89c6-e6cea388f607\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 30,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065560210,\n" +
                "            \"dateCreated\": \"2018-09-04T15:52:40.584+03:00\",\n" +
                "            \"serverVersion\": 1536065560210,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"10b8d0c9-ab3f-4cc6-b88c-b11e094c9bca\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 31,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065560210,\n" +
                "            \"dateCreated\": \"2018-09-04T15:52:40.673+03:00\",\n" +
                "            \"serverVersion\": 1536065560210,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"6b8b03b5-2d75-42b6-bbaa-e37d344332d0\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 32,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065560210,\n" +
                "            \"dateCreated\": \"2018-09-04T15:52:40.676+03:00\",\n" +
                "            \"serverVersion\": 1536065560210,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"b1920b30-5874-41a0-849e-e97779239b17\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 33,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065560210,\n" +
                "            \"dateCreated\": \"2018-09-04T15:52:40.678+03:00\",\n" +
                "            \"serverVersion\": 1536065560210,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"a2959d1f-b90f-4357-8877-1f708c193e9e\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 34,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065560210,\n" +
                "            \"dateCreated\": \"2018-09-04T15:52:40.681+03:00\",\n" +
                "            \"serverVersion\": 1536065560210,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"8a6e651c-c946-4e74-bf9d-a0ad3f3c1cfc\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 35,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065560210,\n" +
                "            \"dateCreated\": \"2018-09-04T15:52:40.683+03:00\",\n" +
                "            \"serverVersion\": 1536065560210,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"043d7f6c-bdb2-4c6d-9f15-f7dd83833fb6\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 36,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065560210,\n" +
                "            \"dateCreated\": \"2018-09-04T15:52:40.689+03:00\",\n" +
                "            \"serverVersion\": 1536065560210,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"205480e7-deda-40f4-8ad2-7d8b3b57a2b4\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 37,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065560210,\n" +
                "            \"dateCreated\": \"2018-09-04T15:52:40.692+03:00\",\n" +
                "            \"serverVersion\": 1536065560210,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"4c08f1a1-e3dd-4d74-b956-3cd19666bfa9\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 38,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065560210,\n" +
                "            \"dateCreated\": \"2018-09-04T15:52:40.693+03:00\",\n" +
                "            \"serverVersion\": 1536065560210,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"d99a8885-ca2c-4222-b574-44e9d57f3a6a\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 39,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065560210,\n" +
                "            \"dateCreated\": \"2018-09-04T15:52:40.695+03:00\",\n" +
                "            \"serverVersion\": 1536065560210,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"55c2377e-9366-4e21-a680-cc23561ecf1e\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 40,\n" +
                "            \"vaccine_type_id\": \"trade_item_id\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id\",\n" +
                "            \"providerid\": \"provider_id\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065560210,\n" +
                "            \"dateCreated\": \"2018-09-04T15:52:40.701+03:00\",\n" +
                "            \"serverVersion\": 1536065560210,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"fd90081d-44ac-4296-8f1e-21a03b408d97\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 41,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_1\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_1\",\n" +
                "            \"providerid\": \"provider_id_1\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065560210,\n" +
                "            \"dateCreated\": \"2018-09-04T15:52:40.706+03:00\",\n" +
                "            \"serverVersion\": 1536065560210,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"8c1eb867-b2e2-49ff-b1bc-b0d277794eba\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"identifier\": 42,\n" +
                "            \"vaccine_type_id\": \"trade_item_id_2\",\n" +
                "            \"transaction_type\": \"debit\",\n" +
                "            \"lotId\": \"lot_id_2\",\n" +
                "            \"providerid\": \"provider_id_2\",\n" +
                "            \"value\": 1,\n" +
                "            \"date_created\": 1,\n" +
                "            \"to_from\": \"to_from\",\n" +
                "            \"date_updated\": 1,\n" +
                "            \"version\": 1536065560210,\n" +
                "            \"dateCreated\": \"2018-09-04T15:52:40.708+03:00\",\n" +
                "            \"serverVersion\": 1536065560210,\n" +
                "            \"type\": \"Stock\",\n" +
                "            \"id\": \"142ca9a1-22a9-4094-8d6a-6893a8b7d9d6\",\n" +
                "            \"revision\": \"v1\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
    }
}
