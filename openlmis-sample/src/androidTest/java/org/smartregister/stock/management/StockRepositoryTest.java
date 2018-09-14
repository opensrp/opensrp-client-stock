package org.smartregister.stock.management;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.smartregister.stock.openlmis.domain.Stock;
import org.smartregister.stock.openlmis.domain.openlmis.Lot;
import org.smartregister.stock.openlmis.domain.openlmis.TradeItem;
import org.smartregister.stock.openlmis.dto.LotDetailsDto;
import org.smartregister.stock.openlmis.repository.StockRepository;
import org.smartregister.stock.openlmis.repository.openlmis.LotRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.smartregister.stock.openlmis.util.Utils.DATABASE_NAME;

/**
 * Created by samuelgithengi on 7/27/18.
 */
public class StockRepositoryTest extends BaseRepositoryTest {

    private StockRepository stockRepository;
    private LotRepository lotRepository;

    @Before
    public void setUp() {
        context.deleteDatabase(DATABASE_NAME);
        stockRepository = new StockRepository(mainRepository);
        lotRepository = new LotRepository(mainRepository);
    }

    @After
    public void tearDown() {
        mainRepository.close();
    }

    @Test
    public void testAdd() {
        String tradeItemId = UUID.randomUUID().toString();
        String lotId = UUID.randomUUID().toString();
        long now = System.currentTimeMillis();
        Stock stock = new Stock(null, Stock.received, "tester11", 25, now,
                "wareHouse123", "unsynched", now, tradeItemId);
        stock.setLotId(lotId);
        stockRepository.addOrUpdate(stock);
        List<Stock> stockList = stockRepository.getStockByTradeItem(tradeItemId);

        assertEquals(1, stockList.size());
        assertEquals(tradeItemId, stockList.get(0).getStockTypeId());
        assertEquals(Stock.received, stockList.get(0).getTransactionType());
        assertEquals(25, stockList.get(0).getValue());
        assertEquals("wareHouse123", stockList.get(0).getToFrom());
        assertNotNull(stockList.get(0).getId());
        assertEquals(lotId, stockList.get(0).getLotId());

    }

    @Test
    public void testUpdate() {
        String tradeItemId = UUID.randomUUID().toString();
        String lotId = UUID.randomUUID().toString();
        long now = System.currentTimeMillis();
        Stock stock = new Stock(null, Stock.issued, "tester11", 32, now,
                "HO", "unsynched", now, tradeItemId);
        stock.setLotId(lotId);
        stockRepository.addOrUpdate(stock);
        List<Stock> stockList = stockRepository.getStockByTradeItem(tradeItemId);

        assertEquals(1, stockList.size());
        assertEquals(tradeItemId, stockList.get(0).getStockTypeId());
        assertEquals(Stock.issued, stockList.get(0).getTransactionType());
        assertEquals(32, stockList.get(0).getValue());
        assertEquals("HO", stockList.get(0).getToFrom());
        assertEquals(lotId, stockList.get(0).getLotId());
        assertNull(stockList.get(0).getTeam());

        long updateTimestamp = System.currentTimeMillis();
        stock.setUpdatedAt(updateTimestamp);
        stock.setValue(24);
        stock.setTeam("ATeam");
        stock.setId(stockList.get(0).getId());
        stockRepository.addOrUpdate(stock);

        stockList = stockRepository.getStockByTradeItem(tradeItemId);
        assertEquals(1, stockList.size());
        assertEquals(24, stockList.get(0).getValue());
        assertEquals("ATeam", stockList.get(0).getTeam());
        assertEquals(updateTimestamp, stockList.get(0).getUpdatedAt().longValue());

    }

    @Test
    public void testGetTotalStockByTradeItem() {
        String tradeItemId = UUID.randomUUID().toString();
        long now = System.currentTimeMillis();
        Stock stock = new Stock(null, Stock.received, "tester11", 50, now,
                "wareHouse123", "unsynched", now, tradeItemId);
        stockRepository.addOrUpdate(stock);

        stock = new Stock(null, Stock.issued, "tester11", -12, now,
                "HO", "unsynched", now, tradeItemId);
        stockRepository.addOrUpdate(stock);

        String tradeItemId2 = UUID.randomUUID().toString();
        stock = new Stock(null, Stock.loss_adjustment, "tester11", 32, now,
                "HO", "unsynched", now, tradeItemId2);
        stockRepository.addOrUpdate(stock);

        assertEquals(38, stockRepository.getTotalStockByTradeItem(tradeItemId));

        assertEquals(32, stockRepository.getTotalStockByTradeItem(tradeItemId2));

        assertEquals(0, stockRepository.getTotalStockByTradeItem(UUID.randomUUID().toString()));

    }

    @Test
    public void testGetStockByTradeItem() {
        String tradeItemId = UUID.randomUUID().toString();
        long now = System.currentTimeMillis();
        Stock stock = new Stock(null, Stock.received, "tester11", 50, now,
                "wareHouse123", "unsynched", now, tradeItemId);
        stockRepository.addOrUpdate(stock);

        stock = new Stock(null, Stock.issued, "tester11", -12, now,
                "HO", "unsynched", now, tradeItemId);
        stockRepository.addOrUpdate(stock);

        String tradeItemId2 = UUID.randomUUID().toString();
        stock = new Stock(null, Stock.loss_adjustment, "tester1134", 321, now,
                "Zomba District", "unsynched", now, tradeItemId2);
        stockRepository.addOrUpdate(stock);

        List<Stock> stockList = stockRepository.getStockByTradeItem(tradeItemId);
        for (Stock stock1 : stockList) {
            assertEquals(tradeItemId, stock1.getStockTypeId());
        }

        stockList = stockRepository.getStockByTradeItem(tradeItemId2);

        assertEquals(1, stockList.size());
        assertEquals("Zomba District", stockList.get(0).getToFrom());
        assertEquals("tester1134", stockList.get(0).getProviderid());
        assertEquals(321, stockList.get(0).getValue());
        assertEquals(Stock.loss_adjustment, stockList.get(0).getTransactionType());

        assertTrue(stockRepository.getStockByTradeItem(UUID.randomUUID().toString()).isEmpty());
    }

    @Test
    public void testGetNumberOfLotsByTradeItem() {

        UUID tradeItemId = UUID.randomUUID();
        UUID tradeItemId2 = UUID.randomUUID();

        UUID lotId = UUID.randomUUID();
        LocalDate lotExpiry = new LocalDate().plusMonths(2);

        Lot lot = new Lot(lotId, "LC2018G", lotExpiry,
                new LocalDate("2018-01-01"), new TradeItem(tradeItemId), true);
        lotRepository.addOrUpdate(lot);

        UUID lotId2 = UUID.randomUUID();
        LocalDate lot2Expiry = new LocalDate().plusDays(2);
        lot = new Lot(lotId2, "LC2016FG", lot2Expiry,
                new LocalDate("2016-04-05"), new TradeItem(tradeItemId2), false);
        lotRepository.addOrUpdate(lot);

        long now = System.currentTimeMillis();
        Stock stock = new Stock(null, Stock.received, "tester11", 50, now,
                "wareHouse123", "unsynched", now, tradeItemId.toString());
        stock.setLotId(lotId.toString());
        stockRepository.addOrUpdate(stock);

        stock = new Stock(null, Stock.issued, "tester11", -10, now,
                "HO", "unsynched", now, tradeItemId.toString());
        stock.setLotId(lotId.toString());
        stockRepository.addOrUpdate(stock);

        stock = new Stock(null, Stock.received, "tester11", 12, now,
                "HO", "unsynched", now, tradeItemId.toString());
        stock.setLotId(lotId2.toString());
        stockRepository.addOrUpdate(stock);

        stock = new Stock(null, Stock.received, "tester11", 32, now,
                "HO", "unsynched", now, tradeItemId2.toString());
        stock.setLotId(lotId2.toString());
        stockRepository.addOrUpdate(stock);

        List<String> ids = new ArrayList<>();
        ids.add(tradeItemId.toString());
        ids.add(tradeItemId2.toString());

        List<LotDetailsDto> lots = stockRepository.getNumberOfLotsByTradeItem(ids).get(tradeItemId.toString());

        assertEquals(1, lots.size());
        assertEquals(lotExpiry.toDate().getTime(), lots.get(0).getMinimumExpiryDate());
        assertEquals(40, lots.get(0).getTotalStock());
        assertEquals(lotId.toString(), lots.get(0).getLotId());


        lots = stockRepository.getNumberOfLotsByTradeItem(ids).get(tradeItemId2.toString());
        assertEquals(1, lots.size());

        assertEquals(lot2Expiry.toDate().getTime(), lots.get(0).getMinimumExpiryDate());
        assertEquals(44, lots.get(0).getTotalStock());
        assertEquals(lotId2.toString(), lots.get(0).getLotId());


        ids.clear();
        assertEquals(0, stockRepository.getNumberOfLotsByTradeItem(ids).size());

    }

    @Test
    public void testGetTotalStockByLot() {
        UUID lotId = UUID.randomUUID();
        UUID tradeItemId = UUID.randomUUID();
        Lot lot = new Lot(lotId, "LC2018G", new LocalDate("2019-01-01"),
                new LocalDate("2018-01-01"), new TradeItem(tradeItemId), true);
        lotRepository.addOrUpdate(lot);

        UUID lotId2 = UUID.randomUUID();
        LocalDate lot2Expiry = new LocalDate("2017-01-04");
        lot = new Lot(lotId2, "LC2016FG", lot2Expiry,
                new LocalDate("2016-04-05"), new TradeItem(tradeItemId), false);
        lotRepository.addOrUpdate(lot);

        long now = System.currentTimeMillis();
        Stock stock = new Stock(null, Stock.received, "tester11", 50, now,
                "wareHouse123", "unsynched", now, tradeItemId.toString());
        stock.setLotId(lotId.toString());
        stockRepository.addOrUpdate(stock);

        stock = new Stock(null, Stock.issued, "tester11", -10, now,
                "HO", "unsynched", now, tradeItemId.toString());
        stock.setLotId(lotId.toString());
        stockRepository.addOrUpdate(stock);

        stock = new Stock(null, Stock.received, "tester11", 12, now,
                "HO", "unsynched", now, tradeItemId.toString());
        stock.setLotId(lotId2.toString());
        stockRepository.addOrUpdate(stock);

        assertEquals(40, stockRepository.getTotalStockByLot(lotId.toString()));

        assertEquals(12, stockRepository.getTotalStockByLot(lotId2.toString()));

        assertEquals(0, stockRepository.getTotalStockByLot(UUID.randomUUID().toString()));
    }
}
