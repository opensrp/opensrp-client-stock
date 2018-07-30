package org.smartregister.stock.management;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.smartregister.stock.openlmis.domain.Stock;
import org.smartregister.stock.openlmis.domain.openlmis.Lot;
import org.smartregister.stock.openlmis.domain.openlmis.TradeItem;
import org.smartregister.stock.openlmis.repository.StockRepository;
import org.smartregister.stock.openlmis.repository.openlmis.LotRepository;

import java.util.List;
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
        UUID lotId = UUID.randomUUID();
        Lot expected = new Lot(lotId, "LC2018G", new LocalDate("2019-01-01"),
                new LocalDate("2018-01-01"), new TradeItem(UUID.randomUUID()), true);
        lotRepository.addOrUpdate(expected);

        Lot lot = lotRepository.findLotById(lotId.toString());
        assertEquals(lotId, lot.getId());
    }

    @Test
    public void testFindLotById() {
        UUID lotId = UUID.randomUUID();
        Lot expected = new Lot(lotId, "LC2018G", new LocalDate("2019-01-01"),
                new LocalDate("2018-01-01"), new TradeItem(UUID.randomUUID()), true);
        lotRepository.addOrUpdate(expected);

        Lot lot = lotRepository.findLotById(lotId.toString());
        assertEquals(lotId, lot.getId());
        assertEquals("LC2018G", lot.getLotCode());
        assertEquals("2019-01-01", lot.getExpirationDate().toString(DATE_FORMAT));
        assertEquals("2018-01-01", lot.getManufactureDate().toString(DATE_FORMAT));
        assertTrue(lot.isActive());

        assertNull(lotRepository.findLotById(UUID.randomUUID().toString()));
    }

    @Test
    public void testUpdate() {
        UUID tradeItemId = UUID.randomUUID();
        assertTrue(lotRepository.findLotsByTradeItem(tradeItemId.toString()).isEmpty());

        UUID lotId = UUID.randomUUID();
        Lot expected = new Lot(lotId, "LC20187G", new LocalDate("2019-11-01"),
                new LocalDate("2018-01-01"), new TradeItem(tradeItemId), true);
        lotRepository.addOrUpdate(expected);

        Lot lot = lotRepository.findLotById(lotId.toString());
        assertEquals(lotId, lot.getId());
        assertEquals("2019-11-01", lot.getExpirationDate().toString(DATE_FORMAT));
        assertTrue(lot.isActive());

        lot.setActive(false);
        lot.setExpirationDate(new LocalDate("2019-06-01"));
        lot.setManufactureDate(new LocalDate("2019-01-11"));
        lotRepository.addOrUpdate(lot);

        Lot updatedLot = lotRepository.findLotById(lotId.toString());

        assertEquals(tradeItemId, updatedLot.getTradeItem().getId());
        assertEquals("2019-01-11", updatedLot.getManufactureDate().toString(DATE_FORMAT));
        assertEquals("2019-06-01", updatedLot.getExpirationDate().toString(DATE_FORMAT));
        assertFalse(updatedLot.isActive());
    }

    @Test
    public void testFindLotsByTradeItemWithStock() {
        UUID tradeItemId = UUID.randomUUID();
        long now = System.currentTimeMillis();

        assertTrue(lotRepository.findLotsByTradeItem(tradeItemId.toString()).isEmpty());
        Lot lot = new Lot(UUID.randomUUID(), "LC2018G", new LocalDate("2019-01-31"),
                new LocalDate("2018-01-01"), new TradeItem(tradeItemId), true);
        lotRepository.addOrUpdate(lot);

        Stock stock = new Stock(null, Stock.received, "tester11", 25, now,
                "wareHouse123", "unsynched", now, tradeItemId.toString());
        stock.setLotId(lot.getId().toString());
        stockRepository.addOrUpdate(stock);


        lot = new Lot(UUID.randomUUID(), "LC2018N", new LocalDate("2020-07-11"),
                new LocalDate("2018-01-15"), new TradeItem(tradeItemId), true);
        lotRepository.addOrUpdate(lot);
        stock = new Stock(null, Stock.received, "tester11", 3, now,
                "wareHouse123", "unsynched", now, tradeItemId.toString());
        stock.setLotId(lot.getId().toString());
        stockRepository.addOrUpdate(stock);


        UUID tradeItem2 = UUID.randomUUID();
        lot = new Lot(UUID.randomUUID(), "LC2016FG", new LocalDate("2017-01-04"),
                new LocalDate("2016-04-05"), new TradeItem(tradeItem2), false);
        lotRepository.addOrUpdate(lot);

        stock = new Stock(null, Stock.received, "tester11", 10, now,
                "wareHouse123", "unsynched", now, tradeItem2.toString());
        stock.setLotId(lot.getId().toString());
        stockRepository.addOrUpdate(stock);


        lot = new Lot(UUID.randomUUID(), "LC2018FG", new LocalDate("2018-03-04"),
                new LocalDate("2019-03-02"), new TradeItem(tradeItem2), true);
        lotRepository.addOrUpdate(lot);

        List<Lot> lots = lotRepository.findLotsByTradeItem(tradeItemId.toString());

        assertEquals(2, lots.size());
        for (Lot returnedLot : lots) {
            assertEquals(tradeItemId, returnedLot.getTradeItem().getId());
            assertTrue(returnedLot.getLotCode().equals("LC2018G") || returnedLot.getLotCode().equals("LC2018N"));
            assertTrue(returnedLot.getExpirationDate().toString(DATE_FORMAT).equals("2019-01-31")
                    || returnedLot.getExpirationDate().toString(DATE_FORMAT).equals("2020-07-11"));
            assertTrue(returnedLot.isActive());
        }

        lots = lotRepository.findLotsByTradeItem(tradeItem2.toString());

        assertEquals(1, lots.size());
        assertEquals(tradeItem2, lots.get(0).getTradeItem().getId());
        assertEquals("LC2016FG", lots.get(0).getLotCode());
        assertEquals("2017-01-04", lots.get(0).getExpirationDate().toString(DATE_FORMAT));
        assertFalse(lots.get(0).isActive());
    }

    @Test
    public void testFindLotsByTradeItemWithoutStock() {
        UUID tradeItemId = UUID.randomUUID();
        assertTrue(lotRepository.findLotsByTradeItem(tradeItemId.toString()).isEmpty());
        Lot lot = new Lot(UUID.randomUUID(), "LC2018G", new LocalDate("2019-01-31"),
                new LocalDate("2018-01-01"), new TradeItem(tradeItemId), true);
        lotRepository.addOrUpdate(lot);


        List<Lot> lots = lotRepository.findLotsByTradeItem(tradeItemId.toString());

        assertTrue(lots.isEmpty());

    }

    @Test
    public void testGetNumberOfLotsByTradeItem() {
        UUID tradeItemId = UUID.randomUUID();
        assertEquals(0, lotRepository.getNumberOfLotsByTradeItem(tradeItemId.toString()));

        Lot lot = new Lot(UUID.randomUUID(), "LC2018G", new LocalDate("2019-01-31"),
                new LocalDate("2018-01-01"), new TradeItem(tradeItemId), true);
        lotRepository.addOrUpdate(lot);

        lot = new Lot(UUID.randomUUID(), "LC2018N", new LocalDate("2020-07-11"),
                new LocalDate("2018-01-15"), new TradeItem(tradeItemId), true);
        lotRepository.addOrUpdate(lot);

        UUID tradeItem2 = UUID.randomUUID();
        lot = new Lot(UUID.randomUUID(), "LC2016FG", new LocalDate("2017-01-04"),
                new LocalDate("2016-04-05"), new TradeItem(tradeItem2), false);
        lotRepository.addOrUpdate(lot);

        int lots = lotRepository.getNumberOfLotsByTradeItem(tradeItemId.toString());

        assertEquals(2, lots);

        lots = lotRepository.getNumberOfLotsByTradeItem(tradeItem2.toString());

        assertEquals(1, lots);

    }
}
