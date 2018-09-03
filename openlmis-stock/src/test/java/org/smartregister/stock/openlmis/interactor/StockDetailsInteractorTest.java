package org.smartregister.stock.openlmis.interactor;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.smartregister.stock.openlmis.BaseUnitTest;
import org.smartregister.stock.openlmis.domain.Stock;
import org.smartregister.stock.openlmis.domain.TradeItem;
import org.smartregister.stock.openlmis.domain.openlmis.Lot;
import org.smartregister.stock.openlmis.repository.StockRepository;
import org.smartregister.stock.openlmis.repository.TradeItemRepository;
import org.smartregister.stock.openlmis.repository.openlmis.LotRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by samuelgithengi on 8/13/18.
 */
public class StockDetailsInteractorTest extends BaseUnitTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private StockRepository stockRepository;

    @Mock
    private LotRepository lotRepository;

    @Mock
    private TradeItemRepository tradeItemRepository;

    private StockDetailsInteractor stockDetailsInteractor;


    @Before
    public void setUp() {
        stockDetailsInteractor = new StockDetailsInteractor(stockRepository, lotRepository, tradeItemRepository);
    }

    @Test
    public void testGetTotalStockByLot() {
        UUID uuid = UUID.randomUUID();
        when(stockRepository.getTotalStockByLot(uuid.toString())).thenReturn(10);
        assertEquals(10, stockDetailsInteractor.getTotalStockByLot(uuid));

        when(stockRepository.getTotalStockByLot(uuid.toString())).thenReturn(3);
        assertEquals(3, stockDetailsInteractor.getTotalStockByLot(uuid));
        verify(stockRepository, Mockito.times(2)).getTotalStockByLot(uuid.toString());
    }

    @Test
    public void testFindLotsByTradeItem() {
        String tradeItem = UUID.randomUUID().toString();
        UUID lotId = UUID.randomUUID();
        List<Lot> expected = new ArrayList<>();
        expected.add(new Lot(lotId, "LC2018G", new LocalDate("2019-01-31"),
                new LocalDate("2018-01-01"), null, true));
        when(lotRepository.findLotsByTradeItem(tradeItem)).thenReturn(expected);
        List<Lot> lots = stockDetailsInteractor.findLotsByTradeItem(tradeItem);
        assertEquals(1, lots.size());
        assertEquals(lotId, lots.get(0).getId());
        assertEquals("LC2018G", lots.get(0).getLotCode());
        verify(lotRepository).findLotsByTradeItem(tradeItem);
    }


    @Test
    public void testFindTradeItem() {
        String tradeItemId = UUID.randomUUID().toString();
        TradeItem expected = new TradeItem(tradeItemId);
        when(stockDetailsInteractor.findTradeItem(tradeItemId)).thenReturn(expected);
        TradeItem tradeItem = stockDetailsInteractor.findTradeItem(tradeItemId);
        verify(tradeItemRepository).getTradeItemById(tradeItemId);
        assertEquals(tradeItemId, tradeItem.getId());
    }

    @Test
    public void testGetStockByTradeItem() {
        List<Stock> expected = new ArrayList<>();
        String tradeItemId = UUID.randomUUID().toString();
        expected.add(new Stock(119l, Stock.issued, "tester11", -12, 0l,
                "HO", "unsynched", 120l, tradeItemId));
        when(stockDetailsInteractor.getStockByTradeItem(tradeItemId)).thenReturn(expected);
        List<Stock> stockList = stockDetailsInteractor.getStockByTradeItem(tradeItemId);
        verify(stockRepository).getStockByTradeItem(tradeItemId);
        assertEquals(1, stockList.size());
        assertEquals(119l, stockList.get(0).getId().longValue());
        assertEquals(Stock.issued, stockList.get(0).getTransactionType());
        assertEquals(tradeItemId, stockList.get(0).getStockTypeId());
        assertEquals(-12, stockList.get(0).getValue());

    }


    @Test
    public void testFindLotNames() {
        String tradeItemId = UUID.randomUUID().toString();
        Map<String, String> expected = new HashMap<>();
        expected.put("1", "LOT12");
        expected.put("2", "LOT32");
        when(lotRepository.findLotNames(tradeItemId)).thenReturn(expected);
        Map<String, String> lotNames = stockDetailsInteractor.findLotNames(tradeItemId);
        assertEquals(2, lotNames.size());
        assertTrue(lotNames.containsKey("1"));
        assertEquals("LOT32", lotNames.get("2"));

    }

    @Test
    public void testAddStock() {
        String tradeItemId = UUID.randomUUID().toString();
        Stock stock = new Stock(119l, Stock.issued, "tester11", -12, 0l,
                "HO", "unsynched", 120l, tradeItemId);
        stockDetailsInteractor.addStock(stock);
        verify(stockRepository).addOrUpdate(stock);


    }


}
