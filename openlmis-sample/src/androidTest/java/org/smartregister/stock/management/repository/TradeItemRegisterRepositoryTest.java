package org.smartregister.stock.management.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.smartregister.stock.openlmis.domain.TradeItem;
import org.smartregister.stock.openlmis.domain.openlmis.Dispensable;
import org.smartregister.stock.openlmis.repository.TradeItemRepository;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.smartregister.stock.openlmis.util.Utils.DATABASE_NAME;

/**
 * Created by samuelgithengi on 7/27/18.
 */
public class TradeItemRegisterRepositoryTest extends BaseRepositoryTest {
    private TradeItemRepository tradeItemRepository;

    @Before
    public void setUp() {
        context.deleteDatabase(DATABASE_NAME);
        tradeItemRepository = new TradeItemRepository(mainRepository);
    }

    @After
    public void tearDown() {
        mainRepository.close();
    }

    @Test
    public void testAdd() {
        String commodityTypeId = UUID.randomUUID().toString().toString();
        long now = System.currentTimeMillis();
        TradeItem tradeItem = new TradeItem(UUID.randomUUID().toString().toString());
        tradeItem.setName("Intervax BCG 20");
        tradeItem.setNetContent(20l);
        tradeItem.setDateUpdated(now);
        tradeItem.setDispensable(new Dispensable(UUID.randomUUID().toString(), "vials", "10ml", null));
        tradeItem.setCommodityTypeId(commodityTypeId);
        tradeItemRepository.addOrUpdate(tradeItem);

        List<TradeItem> tradeItems = tradeItemRepository.getTradeItemByCommodityType(commodityTypeId);
        assertEquals(1, tradeItems.size());
        assertEquals("Intervax BCG 20", tradeItems.get(0).getName());
        assertEquals(20, tradeItems.get(0).getNetContent().longValue());
        assertEquals(now, tradeItems.get(0).getDateUpdated().longValue());
        assertEquals("vials", tradeItems.get(0).getDispensable().getKeyDispensingUnit());
        assertEquals("10ml", tradeItems.get(0).getDispensable().getKeySizeCode());
    }

    @Test
    public void testUpdate() {
        String commodityTypeId = UUID.randomUUID().toString().toString();
        long now = System.currentTimeMillis();
        TradeItem tradeItem = new TradeItem(UUID.randomUUID().toString().toString());
        tradeItem.setName("Brand A OPV 5");
        tradeItem.setNetContent(5l);
        tradeItem.setDispensable(new Dispensable(UUID.randomUUID().toString(), "pills", "20 capsules", null));
        tradeItem.setDateUpdated(now);
        tradeItem.setCommodityTypeId(commodityTypeId);
        tradeItemRepository.addOrUpdate(tradeItem);

        List<TradeItem> tradeItems = tradeItemRepository.getTradeItemByCommodityType(commodityTypeId);
        assertEquals(1, tradeItems.size());
        assertEquals("Brand A OPV 5", tradeItems.get(0).getName());
        assertEquals(5, tradeItems.get(0).getNetContent().longValue());
        assertEquals(now, tradeItems.get(0).getDateUpdated().longValue());
        assertEquals("pills", tradeItems.get(0).getDispensable().getKeyDispensingUnit());
        assertEquals("20 capsules", tradeItems.get(0).getDispensable().getKeySizeCode());

        tradeItem.setName("Brand A OPV 10");
        tradeItem.setNetContent(10l);
        tradeItem.setDispensable(new Dispensable(UUID.randomUUID().toString(), "pills", "20 pills", null));
        tradeItemRepository.addOrUpdate(tradeItem);

        tradeItems = tradeItemRepository.getTradeItemByCommodityType(commodityTypeId);
        assertEquals(1, tradeItems.size());
        assertEquals("Brand A OPV 10", tradeItems.get(0).getName());
        assertEquals(10, tradeItems.get(0).getNetContent().longValue());
        assertEquals("20 pills", tradeItems.get(0).getDispensable().getKeySizeCode());

    }

    @Test
    public void testGetTradeItemByCommodityType() {

        String commodityTypeId = UUID.randomUUID().toString().toString();
        assertTrue(tradeItemRepository.getTradeItemByCommodityType(commodityTypeId).isEmpty());

        long now = System.currentTimeMillis();

        TradeItem tradeItem = new TradeItem(UUID.randomUUID().toString().toString());
        tradeItem.setName("CIntervax BCG 20");
        tradeItem.setNetContent(20l);
        tradeItem.setDateUpdated(now);
        tradeItem.setCommodityTypeId(commodityTypeId);
        tradeItem.setDispensable(new Dispensable(UUID.randomUUID().toString(), "vials", "10 ml", null));
        tradeItemRepository.addOrUpdate(tradeItem);


        tradeItem = new TradeItem(UUID.randomUUID().toString().toString());
        tradeItem.setName("Brand B OPV 5");
        tradeItem.setNetContent(5l);
        tradeItem.setDateUpdated(now);
        tradeItem.setCommodityTypeId(commodityTypeId);
        tradeItem.setDispensable(new Dispensable(UUID.randomUUID().toString(), "pills", "5 pills", null));
        tradeItemRepository.addOrUpdate(tradeItem);

        String commodityTypeId2 = UUID.randomUUID().toString().toString();
        tradeItem = new TradeItem(UUID.randomUUID().toString().toString());
        tradeItem.setName("Acetylsalicylic");
        tradeItem.setNetContent(24l);
        tradeItem.setDateUpdated(now);
        tradeItem.setCommodityTypeId(commodityTypeId2);
        tradeItemRepository.addOrUpdate(tradeItem);

        List<TradeItem> tradeItems = tradeItemRepository.getTradeItemByCommodityType(commodityTypeId);
        assertEquals(2, tradeItems.size());
        for (TradeItem tradeItem1 : tradeItems) {
            assertEquals(commodityTypeId, tradeItem1.getCommodityTypeId());
            assertTrue(tradeItem1.getName().equals("CIntervax BCG 20")
                    || tradeItem1.getName().equals("Brand B OPV 5"));
        }

        tradeItems = tradeItemRepository.getTradeItemByCommodityType(commodityTypeId2);

        assertEquals(1, tradeItems.size());
        assertEquals("Acetylsalicylic", tradeItems.get(0).getName());
        assertEquals(24, tradeItems.get(0).getNetContent().longValue());

    }

    @Test
    public void testGetTradeItemById() {

        String commodityTypeId = UUID.randomUUID().toString();
        long now = System.currentTimeMillis();
        String id = UUID.randomUUID().toString();
        TradeItem expected = new TradeItem(id);
        expected.setName("CIntervax BCG 20");
        expected.setNetContent(20l);
        expected.setDateUpdated(now);
        expected.setCommodityTypeId(commodityTypeId);
        expected.setDispensable(new Dispensable(null, "vials", "10 ml", null));
        tradeItemRepository.addOrUpdate(expected);

        TradeItem tradeItem = tradeItemRepository.getTradeItemById(id);
        assertEquals(id, tradeItem.getId());
        assertEquals("CIntervax BCG 20", tradeItem.getName());
        assertEquals(20l, tradeItem.getNetContent().longValue());
        assertEquals(now, tradeItem.getDateUpdated().longValue());
        assertEquals(commodityTypeId, tradeItem.getCommodityTypeId());
        assertEquals(null, tradeItem.getDispensable().getId());
        assertEquals("vials", tradeItem.getDispensable().getKeyDispensingUnit());
        assertEquals("10 ml", tradeItem.getDispensable().getKeySizeCode());
    }

}
