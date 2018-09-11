package org.smartregister.stock.management.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.smartregister.stock.openlmis.domain.Stock;
import org.smartregister.stock.openlmis.domain.openlmis.Lot;
import org.smartregister.stock.openlmis.dto.LotDetailsDto;
import org.smartregister.stock.openlmis.repository.StockRepository;
import org.smartregister.stock.openlmis.repository.openlmis.LotRepository;

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

    @Before
    public void setUp() {
        context.deleteDatabase(DATABASE_NAME);
        stockRepository = new StockRepository(mainRepository);
    }

    @After
    public void tearDown() {
        mainRepository.close();
    }

    @Test
    public void testAdd() {
        String tradeItemId = UUID.randomUUID().toString().toString();
        String lotId = UUID.randomUUID().toString().toString();
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
        String tradeItemId = UUID.randomUUID().toString().toString();
        String lotId = UUID.randomUUID().toString().toString();
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
        String tradeItemId = UUID.randomUUID().toString().toString();
        long now = System.currentTimeMillis();
        Stock stock = new Stock(null, Stock.received, "tester11", 50, now,
                "wareHouse123", "unsynched", now, tradeItemId);
        stockRepository.addOrUpdate(stock);

        stock = new Stock(null, Stock.issued, "tester11", -12, now,
                "HO", "unsynched", now, tradeItemId);
        stockRepository.addOrUpdate(stock);

        String tradeItemId2 = UUID.randomUUID().toString().toString();
        stock = new Stock(null, Stock.loss_adjustment, "tester11", 32, now,
                "HO", "unsynched", now, tradeItemId2);
        stockRepository.addOrUpdate(stock);

        assertEquals(38, stockRepository.getTotalStockByTradeItem(tradeItemId));

        assertEquals(32, stockRepository.getTotalStockByTradeItem(tradeItemId2));

        assertEquals(0, stockRepository.getTotalStockByTradeItem(UUID.randomUUID().toString().toString()));

    }

    @Test
    public void testGetStockByTradeItem() {
        String tradeItemId = UUID.randomUUID().toString().toString();
        long now = System.currentTimeMillis();
        Stock stock = new Stock(null, Stock.received, "tester11", 50, now,
                "wareHouse123", "unsynched", now, tradeItemId);
        stockRepository.addOrUpdate(stock);

        stock = new Stock(null, Stock.issued, "tester11", -12, now,
                "HO", "unsynched", now, tradeItemId);
        stockRepository.addOrUpdate(stock);

        String tradeItemId2 = UUID.randomUUID().toString().toString();
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

        assertTrue(stockRepository.getStockByTradeItem(UUID.randomUUID().toString().toString()).isEmpty());
    }

    @Test
    public void testGetNumberOfLotsByTradeItem() {
        LotRepository lotRepository = new LotRepository(mainRepository);

        String tradeItemId = UUID.randomUUID().toString();
        String tradeItemId2 = UUID.randomUUID().toString();

        Lot lotId1 = new Lot(UUID.randomUUID().toString(), "LC2018G", 389293828778878l,
                837813919L, tradeItemId, true);
        lotRepository.addOrUpdate(lotId1);

        Lot lotId2 = new Lot(UUID.randomUUID().toString(), "LC2018N", 389293828778878l,
                389189302L, tradeItemId2, true);
        lotRepository.addOrUpdate(lotId2);

        Lot lotId3 = new Lot(UUID.randomUUID().toString(), "LC2018V", 389293828778878l,
                389189302L, tradeItemId, true);
        lotRepository.addOrUpdate(lotId3);

        long now = System.currentTimeMillis();
        Stock stock = new Stock(null, Stock.received, "tester11", 50, now,
                "wareHouse123", "unsynched", now, tradeItemId);
        stock.setLotId(lotId1.getId());
        stockRepository.addOrUpdate(stock);

        stock = new Stock(null, Stock.issued, "tester11", -10, now,
                "HO", "unsynched", now, tradeItemId);
        stock.setLotId(lotId1.getId());
        stockRepository.addOrUpdate(stock);

        stock = new Stock(null, Stock.received, "tester11", 12, now,
                "HO", "unsynched", now, tradeItemId);
        stock.setLotId(lotId3.getId());
        stockRepository.addOrUpdate(stock);

        stock = new Stock(null, Stock.received, "tester11", 32, now,
                "HO", "unsynched", now, tradeItemId2);
        stock.setLotId(lotId2.getId());
        stockRepository.addOrUpdate(stock);

        assertEquals(2, stockRepository.getNumberOfLotsByTradeItem(tradeItemId).size());
        assertEquals(1, stockRepository.getNumberOfLotsByTradeItem(tradeItemId2).size());
        assertEquals(0, stockRepository.getNumberOfLotsByTradeItem(UUID.randomUUID().toString()).size());
    }
}
