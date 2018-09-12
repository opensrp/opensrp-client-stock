package org.smartregister.stock.management;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.smartregister.stock.openlmis.domain.TradeItem;
import org.smartregister.stock.openlmis.domain.openlmis.CommodityType;
import org.smartregister.stock.openlmis.repository.SearchRepository;
import org.smartregister.stock.openlmis.util.TestDataUtils;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.smartregister.stock.openlmis.util.Utils.DATABASE_NAME;

/**
 * Created by samuelgithengi on 9/12/18.
 */
public class SearchRepositoryTest extends BaseRepositoryTest {

    private SearchRepository searchRepository;

    @Before
    public void setUp() {
        context.deleteDatabase(DATABASE_NAME);
        searchRepository = new SearchRepository(mainRepository);
    }

    @After
    public void tearDown() {
        mainRepository.close();
    }


    @Test
    public void testAddWithoutTradeItem() {
        CommodityType commodityType = new CommodityType(UUID.randomUUID(), "BCG", "", null, null, System.currentTimeMillis());
        searchRepository.addOrUpdate(commodityType, null);

        Map<String, List<String>> ids = searchRepository.searchIds("B");
        assertEquals(1, ids.size());
        assertTrue(ids.containsKey(commodityType.getId().toString()));
        List<String> tradeItems = ids.get(commodityType.getId().toString());
        for (String tradeItem : tradeItems)
            assertNull(tradeItem);

    }


    @Test
    public void testAddOrUpdateWithoutTradeItem() {
        CommodityType commodityType = new CommodityType(UUID.randomUUID(), "OPV", "", null, null, System.currentTimeMillis());
        searchRepository.addOrUpdate(commodityType, null);

        Map<String, List<String>> ids = searchRepository.searchIds("PV");
        assertEquals(1, ids.size());
        assertTrue(ids.containsKey(commodityType.getId().toString()));

        assertTrue(searchRepository.searchIds("BC").isEmpty());

        commodityType.setName("BCG");
        searchRepository.addOrUpdate(commodityType, null);

        ids = searchRepository.searchIds("BCG");
        assertEquals(1, ids.size());
        assertTrue(ids.containsKey(commodityType.getId().toString()));

        assertTrue(searchRepository.searchIds("PV").isEmpty());

    }


    @Test
    public void testAddWithTradeItem() {
        CommodityType commodityType = new CommodityType(UUID.randomUUID(), "BCG", "", null, null, System.currentTimeMillis());
        List<TradeItem> tradeItems = TestDataUtils.getInstance().createTradeItems(commodityType);

        searchRepository.addOrUpdate(commodityType, tradeItems);

        Map<String, List<String>> ids = searchRepository.searchIds("B");
        assertEquals(1, ids.keySet().size());
        assertTrue(ids.containsKey(commodityType.getId().toString()));
        assertEquals(4, ids.get(commodityType.getId().toString()).size());
        List<String> tradeItemsIds = ids.get(commodityType.getId().toString());
        for (String tradeItem : tradeItemsIds)
            assertNotNull(tradeItem);

    }


    @Test
    public void testAddOrUpdateWithTradeItem() {
        CommodityType commodityType = new CommodityType(UUID.randomUUID(), "Tetanus", "", null, null, System.currentTimeMillis());
        List<TradeItem> tradeItems = TestDataUtils.getInstance().createTradeItems(commodityType);

        searchRepository.addOrUpdate(commodityType, tradeItems);

        Map<String, List<String>> ids = searchRepository.searchIds("tanus");
        assertEquals(1, ids.keySet().size());
        assertTrue(ids.containsKey(commodityType.getId().toString()));
        assertEquals(4, ids.get(commodityType.getId().toString()).size());
        List<String> tradeItemsIds = ids.get(commodityType.getId().toString());
        for (String tradeItem : tradeItemsIds)
            assertNotNull(tradeItem);

        assertTrue(searchRepository.searchIds("nta").isEmpty());

        commodityType.setName("Penta");
        searchRepository.addOrUpdate(commodityType, tradeItems);

        ids = searchRepository.searchIds("nta");

        assertEquals(1, ids.keySet().size());
        assertTrue(ids.containsKey(commodityType.getId().toString()));
        assertEquals(4, ids.get(commodityType.getId().toString()).size());

    }

    @Test
    public void testSearchIds() {
        CommodityType commodityType = new CommodityType(UUID.randomUUID(), "Tetanus", "", null, null, System.currentTimeMillis());
        List<TradeItem> tradeItems = TestDataUtils.getInstance().createTradeItems(commodityType);
        searchRepository.addOrUpdate(commodityType, tradeItems);

        Map<String, List<String>> ids = searchRepository.searchIds("Tetanus");

        assertEquals(1, ids.keySet().size());
        assertTrue(ids.containsKey(commodityType.getId().toString()));
        assertEquals(4, ids.get(commodityType.getId().toString()).size());


        ids = searchRepository.searchIds("Tetan");

        assertEquals(1, ids.keySet().size());
        assertTrue(ids.containsKey(commodityType.getId().toString()));
        assertEquals(4, ids.get(commodityType.getId().toString()).size());


        ids = searchRepository.searchIds("tan");

        assertEquals(1, ids.keySet().size());
        assertTrue(ids.containsKey(commodityType.getId().toString()));
        assertEquals(4, ids.get(commodityType.getId().toString()).size());


        ids = searchRepository.searchIds("Inter");

        assertEquals(1, ids.keySet().size());
        assertTrue(ids.containsKey(commodityType.getId().toString()));
        assertEquals(1, ids.get(commodityType.getId().toString()).size());


        CommodityType commodityTypePolio = new CommodityType(UUID.randomUUID(), "Polio", "", null, null, System.currentTimeMillis());
        searchRepository.addOrUpdate(commodityTypePolio, null);

        CommodityType commodityTypePenta = new CommodityType(UUID.randomUUID(), "Pentavalent", "", null, null, System.currentTimeMillis());
        List<TradeItem> pentaTradeItems = TestDataUtils.getInstance().createTradeItems(commodityTypePenta);
        searchRepository.addOrUpdate(commodityTypePenta, pentaTradeItems);

        ids = searchRepository.searchIds("Po");

        assertEquals(1, ids.keySet().size());
        assertTrue(ids.containsKey(commodityTypePolio.getId().toString()));
        assertEquals(1, ids.get(commodityTypePolio.getId().toString()).size());


        ids = searchRepository.searchIds("P");

        assertEquals(2, ids.keySet().size());
        assertTrue(ids.containsKey(commodityTypePolio.getId().toString()));
        assertTrue(ids.containsKey(commodityTypePenta.getId().toString()));
        assertEquals(1, ids.get(commodityTypePolio.getId().toString()).size());
        assertEquals(4, ids.get(commodityTypePenta.getId().toString()).size());

    }

}
