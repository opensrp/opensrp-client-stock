package org.smartregister.stock.management.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.smartregister.stock.openlmis.domain.Stock;
import org.smartregister.stock.openlmis.domain.openlmis.Lot;
import org.smartregister.stock.openlmis.repository.StockRepository;
import org.smartregister.stock.openlmis.repository.openlmis.LotRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.smartregister.stock.openlmis.util.Utils.DATABASE_NAME;

/**
 * Created by samuelgithengi on 7/27/18.
 */
public class LotRepositoryTest extends BaseRepositoryTest {

    private final static String DATE_FORMAT = "yyyy-MM-dd";
    private LotRepository lotRepository;
    private StockRepository stockRepository;

    @Before
    public void setUp() {
        context.deleteDatabase(DATABASE_NAME);
        lotRepository = new LotRepository(mainRepository);
        stockRepository = new StockRepository(mainRepository);
    }

    @After
    public void tearDown() {
        mainRepository.close();
    }

    @Test
    public void testAdd() {
        String lotId = UUID.randomUUID().toString();
        Lot expected = new Lot(lotId, "LC2018G", 837813900L,
                837813919L, UUID.randomUUID().toString(), true);
        lotRepository.addOrUpdate(expected);

        Lot lot = lotRepository.findLotById(lotId.toString());
        assertEquals(lotId, lot.getId());
    }

    @Test
    public void testFindLotById() {
        String lotId = UUID.randomUUID().toString();
        Lot expected = new Lot(lotId, "LC2018G", 837813900L,
                837813919L, UUID.randomUUID().toString(), true);
        lotRepository.addOrUpdate(expected);

        Lot lot = lotRepository.findLotById(lotId.toString());
        assertEquals(lotId, lot.getId());
        assertEquals("LC2018G", lot.getLotCode());
        assertEquals(837813900L, lot.getExpirationDate().longValue());
        assertEquals(837813919L, lot.getManufactureDate().longValue());
        assertTrue(lot.isActive());

        assertNull(lotRepository.findLotById(UUID.randomUUID().toString().toString()));
    }

    @Test
    public void testUpdate() {
        String tradeItemId = UUID.randomUUID().toString();
        assertTrue(lotRepository.findLotsByTradeItem(tradeItemId.toString()).isEmpty());

        String lotId = UUID.randomUUID().toString();
        Lot expected = new Lot(lotId, "LC20187G", 837813900L,
                837813919L, tradeItemId, true);
        lotRepository.addOrUpdate(expected);

        Lot lot = lotRepository.findLotById(lotId.toString());
        assertEquals(lotId, lot.getId());
        assertEquals(837813919L, lot.getManufactureDate().longValue());
        assertTrue(lot.isActive());

        lot.setActive(false);
        lot.setExpirationDate(398439403L);
        lot.setManufactureDate(3984930439403L);
        lotRepository.addOrUpdate(lot);

        Lot updatedLot = lotRepository.findLotById(lotId.toString());

        assertEquals(tradeItemId, updatedLot.getTradeItemId());
        assertEquals(398439403L, updatedLot.getExpirationDate().longValue());
        assertEquals(3984930439403L, updatedLot.getManufactureDate().longValue());
        assertFalse(updatedLot.isActive());
    }

    @Test
    public void testFindLotsByTradeItemWithStock() {
        String tradeItemId = UUID.randomUUID().toString();
        long now = System.currentTimeMillis();

        assertTrue(lotRepository.findLotsByTradeItem(tradeItemId.toString()).isEmpty());
        Lot lot = new Lot(UUID.randomUUID().toString(), "LC2018G", 9439843L,
                837813919L, tradeItemId, true);
        lotRepository.addOrUpdate(lot);

        Stock stock = new Stock(null, Stock.received, "tester11", 25, now,
                "wareHouse123", "unsynched", now, tradeItemId.toString());
        stock.setLotId(lot.getId().toString());
        stockRepository.addOrUpdate(stock);


        lot = new Lot(UUID.randomUUID().toString(), "LC2018N", 2839237L,
                3923892983L, tradeItemId, true);
        lotRepository.addOrUpdate(lot);
        stock = new Stock(null, Stock.received, "tester11", 3, now,
                "wareHouse123", "unsynched", now, tradeItemId.toString());
        stock.setLotId(lot.getId().toString());
        stockRepository.addOrUpdate(stock);


        String tradeItem2 = UUID.randomUUID().toString();
        lot = new Lot(UUID.randomUUID().toString(), "LC2016FG", 8934983L,
                2398298L, tradeItem2, false);
        lotRepository.addOrUpdate(lot);

        stock = new Stock(null, Stock.received, "tester11", 10, now,
                "wareHouse123", "unsynched", now, tradeItem2.toString());
        stock.setLotId(lot.getId().toString());
        stockRepository.addOrUpdate(stock);


        lot = new Lot(UUID.randomUUID().toString(), "LC2018FG", 239823389L,
                9227637L, tradeItem2, true);
        lotRepository.addOrUpdate(lot);

        List<Lot> lots = lotRepository.findLotsByTradeItem(tradeItemId.toString());

        assertEquals(2, lots.size());
        for (Lot returnedLot : lots) {
            assertEquals(tradeItemId, returnedLot.getTradeItemId());
            assertTrue(returnedLot.getLotCode().equals("LC2018G") || returnedLot.getLotCode().equals("LC2018N"));
            assertTrue(returnedLot.getExpirationDate().equals(2839237L)
                    || returnedLot.getExpirationDate().equals(9439843L));
            assertTrue(returnedLot.isActive());
        }

        lots = lotRepository.findLotsByTradeItem(tradeItem2.toString());

        assertEquals(1, lots.size());
        assertEquals(tradeItem2, lots.get(0).getTradeItemId());
        assertEquals("LC2016FG", lots.get(0).getLotCode());
        assertEquals(8934983L, lots.get(0).getExpirationDate().longValue());
        assertFalse(lots.get(0).isActive());
    }

    @Test
    public void testFindLotsByTradeItemWithoutStock() {
        String tradeItemId = UUID.randomUUID().toString();
        assertTrue(lotRepository.findLotsByTradeItem(tradeItemId.toString()).isEmpty());
        Lot lot = new Lot(UUID.randomUUID().toString(), "LC2018G", 982489298L,
                837813919L, tradeItemId, true);
        lotRepository.addOrUpdate(lot);


        List<Lot> lots = lotRepository.findLotsByTradeItem(tradeItemId.toString());

        assertTrue(lots.isEmpty());

    }

    @Test
    public void testGetNumberOfLotsByTradeItem() {

        String tradeItemId = UUID.randomUUID().toString();
        String tradeItemId2 = UUID.randomUUID().toString();

        long now = System.currentTimeMillis();


        Lot lot = new Lot(UUID.randomUUID().toString(), "LC2018G", 378874l,
                3478483l, tradeItemId, true);
        lotRepository.addOrUpdate(lot);

        lot = new Lot(UUID.randomUUID().toString(), "LC2018N", 8843l,
                4838l, tradeItemId, true);
        lotRepository.addOrUpdate(lot);

        UUID tradeItem2 = UUID.randomUUID();
        lot = new Lot(UUID.randomUUID().toString(), "LC2016FG", 89343l,
                9343l, tradeItemId2, false);
        lotRepository.addOrUpdate(lot);

        int lots = lotRepository.getNumberOfLotsByTradeItem(tradeItemId);
        assertEquals(2, lots);

        lots = lotRepository.getNumberOfLotsByTradeItem(tradeItemId2);
        assertEquals(1, lots);

        assertEquals(0, lotRepository.getNumberOfLotsByTradeItem(UUID.randomUUID().toString()));
    }

    @Test
    public void testFindLotNames() {
        UUID tradeItemId = UUID.randomUUID();
        assertEquals(0, lotRepository.findLotNames(tradeItemId.toString()).size());

        UUID lotId = UUID.randomUUID();
        Lot lot = new Lot(lotId.toString(), "LC2018G", 490234l,
                248392l, tradeItemId.toString(), true);
        lotRepository.addOrUpdate(lot);

        UUID lot2Id = UUID.randomUUID();
        lot = new Lot(lot2Id.toString(), "LC2018N", 4893942l,
                3193982l, tradeItemId.toString(), true);
        lotRepository.addOrUpdate(lot);

        UUID tradeItem2 = UUID.randomUUID();
        UUID lot3Id = UUID.randomUUID();
        lot = new Lot(lot3Id.toString(), "LC2016FG", 949123l,
                391390l, tradeItem2.toString(), false);
        lotRepository.addOrUpdate(lot);

        Map<String, String> lots = lotRepository.findLotNames(tradeItemId.toString());

        assertEquals(2, lots.size());
        assertTrue(lots.containsKey(lotId.toString()));
        assertEquals("LC2018G", lots.get(lotId.toString()));

        assertTrue(lots.containsKey(lot2Id.toString()));
        assertEquals("LC2018N", lots.get(lot2Id.toString()));

        lots = lotRepository.findLotNames(tradeItem2.toString());

        assertEquals(1, lots.size());
        assertTrue(lots.containsKey(lot3Id.toString()));
        assertEquals("LC2016FG", lots.get(lot3Id.toString()));

        assertEquals(0, lotRepository.findLotNames(UUID.randomUUID().toString()).size());

    }

}
