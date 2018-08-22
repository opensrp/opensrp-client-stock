package org.smartregister.stock.openlmis.presenter;

import android.view.View;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.smartregister.stock.openlmis.BaseUnitTest;
import org.smartregister.stock.openlmis.domain.Stock;
import org.smartregister.stock.openlmis.domain.TradeItem;
import org.smartregister.stock.openlmis.domain.openlmis.Lot;
import org.smartregister.stock.openlmis.dto.TradeItemDto;
import org.smartregister.stock.openlmis.interactor.StockDetailsInteractor;
import org.smartregister.stock.openlmis.view.contract.StockDetailsView;
import org.smartregister.stock.openlmis.wrapper.StockWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by samuelgithengi on 8/3/18.
 */
public class StockDetailsPresenterTest extends BaseUnitTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private StockDetailsView stockDetailsView;

    @Mock
    private StockDetailsInteractor stockDetailsInteractor;

    private StockDetailsPresenter stockDetailsPresenter;

    @Before
    public void setUp() {
        stockDetailsPresenter = new StockDetailsPresenter(stockDetailsView, stockDetailsInteractor);
    }

    @Test
    public void testGetTotalStockByLot() {
        UUID lotId = UUID.randomUUID();
        when(stockDetailsInteractor.getTotalStockByLot(lotId)).thenReturn(2);
        assertEquals(2, stockDetailsPresenter.getTotalStockByLot(lotId));
    }

    @Test
    public void testFindLotsByTradeItemWithoutLots() {
        String tradeItemId = UUID.randomUUID().toString();
        when(stockDetailsInteractor.findLotsByTradeItem(tradeItemId)).thenReturn(new ArrayList<Lot>());
        assertTrue(stockDetailsPresenter.findLotsByTradeItem(tradeItemId).isEmpty());
        verify(stockDetailsView, never()).showLotsHeader();

    }

    @Test
    public void testFindLotsByTradeItemWithLots() {
        String tradeItemId = UUID.randomUUID().toString();
        List<Lot> expectedLots = new ArrayList<>();
        Lot lot = new Lot(UUID.randomUUID(), "LC2018G", new LocalDate("2019-01-31"),
                new LocalDate("2017-01-31"),
                new org.smartregister.stock.openlmis.domain.openlmis.TradeItem(UUID.fromString(tradeItemId)),
                true);
        expectedLots.add(lot);
        when(stockDetailsInteractor.findLotsByTradeItem(tradeItemId)).thenReturn(expectedLots);
        List<Lot> returnedLots = stockDetailsPresenter.findLotsByTradeItem(tradeItemId);
        assertEquals(1, returnedLots.size());
        assertEquals("LC2018G", returnedLots.get(0).getLotCode());
        assertEquals("2019-01-31", returnedLots.get(0).getExpirationDate().toString());
        assertEquals("2017-01-31", returnedLots.get(0).getManufactureDate().toString());
        assertEquals(tradeItemId, returnedLots.get(0).getTradeItem().getId().toString());
        verify(stockDetailsView).showLotsHeader();

    }


    @Test
    public void testFindTradeItem() {
        String commodityTypeId = UUID.randomUUID().toString();
        long now = System.currentTimeMillis();
        String tradeItemId = UUID.randomUUID().toString();
        TradeItem expected = new TradeItem(tradeItemId);
        expected.setName("CIntervax BCG 20");
        expected.setNetContent(20l);
        expected.setDateUpdated(now);
        expected.setCommodityTypeId(commodityTypeId);

        when(stockDetailsInteractor.findTradeItem(tradeItemId)).thenReturn(expected);

        TradeItem tradeItem = stockDetailsPresenter.findTradeItem(tradeItemId);
        assertEquals(tradeItemId, tradeItem.getId());
        assertEquals("CIntervax BCG 20", tradeItem.getName());
        assertEquals(20l, tradeItem.getNetContent().longValue());
        assertEquals(now, tradeItem.getDateUpdated().longValue());
        assertEquals(commodityTypeId, tradeItem.getCommodityTypeId());

    }


    @Test
    public void testFindStockByTradeItemWithStock() {

        List<Stock> expected = new ArrayList<>();
        long now = System.currentTimeMillis();
        String tradeItemId = UUID.randomUUID().toString();
        Stock stock = new Stock(null, Stock.received, "tester11", 50, now,
                "wareHouse123", "unsynched", now, tradeItemId);
        expected.add(stock);

        stock = new Stock(null, Stock.issued, "tester11", -12, now,
                "HO", "unsynched", now, tradeItemId);
        expected.add(stock);

        when(stockDetailsInteractor.getStockByTradeItem(tradeItemId)).thenReturn(expected);

        List<Stock> actual = stockDetailsPresenter.findStockByTradeItem(tradeItemId);
        assertEquals(2, actual.size());
        for (Stock stock1 : actual) {
            assertEquals(tradeItemId, stock1.getStockTypeId());
        }
        verify(stockDetailsView).showTransactionsHeader();

    }

    @Test
    public void testFindStockByTradeItemWithoutStock() {
        String tradeItemId = UUID.randomUUID().toString();
        when(stockDetailsInteractor.getStockByTradeItem(tradeItemId)).thenReturn(new ArrayList<Stock>());

        List<Stock> actual = stockDetailsPresenter.findStockByTradeItem(tradeItemId);
        assertEquals(0, actual.size());
        verify(stockDetailsView, never()).showTransactionsHeader();

    }

    @Test
    public void testPopulateLotNamesAndBalance() {
        String tradeItemId = UUID.randomUUID().toString();
        String lotId = UUID.randomUUID().toString();
        String lot2Id = UUID.randomUUID().toString();

        Map<String, String> expected = new HashMap<>();
        expected.put(lotId, "LC2134K");
        expected.put(lot2Id, "LC5453K");

        when(stockDetailsInteractor.findLotNames(tradeItemId)).thenReturn(expected);

        List<Stock> stockList = new ArrayList<>();
        long now = System.currentTimeMillis();

        Stock stock = new Stock(1l, Stock.received, "tester11", 50, now,
                "wareHouse123", "unsynched", now, tradeItemId);
        stock.setLotId(lotId);
        stockList.add(stock);

        stock = new Stock(2l, Stock.issued, "tester11", -12, now,
                "HO", "unsynched", now, tradeItemId);
        stock.setLotId(lotId);
        stockList.add(stock);


        stock = new Stock(3l, Stock.loss_adjustment, "tester11", -2, now,
                "HO", "unsynched", now, tradeItemId);
        stock.setLotId(lot2Id);
        stockList.add(stock);

        List<StockWrapper> actual = stockDetailsPresenter.populateLotNamesAndBalance(new TradeItemDto(tradeItemId,
                        "GHGR", 100, now, 2, "vials", 5l),
                stockList);

        assertEquals(3, actual.size());
        for (StockWrapper stockWrapper : actual) {
            assertEquals(tradeItemId, stockWrapper.getStock().getStockTypeId());
            if (stockWrapper.getStock().getLotId().equals(lotId)) {
                assertEquals("LC2134K", stockWrapper.getLotCode());
                if (stockWrapper.getStock().getId().equals(1l))
                    assertEquals(100, stockWrapper.getStockBalance());
                else
                    assertEquals(50, stockWrapper.getStockBalance());

            } else {
                assertEquals(lot2Id, stockWrapper.getStock().getLotId());
                assertEquals("LC5453K", stockWrapper.getLotCode());
                assertEquals(62, stockWrapper.getStockBalance());
            }
        }
    }


    @Test
    public void testCollapseClicked() {
        stockDetailsPresenter.collapseExpandClicked(View.VISIBLE);
        verify(stockDetailsView).collapseLots();
        verify(stockDetailsView, never()).expandLots();
    }

    @Test
    public void testExpandClicked() {
        stockDetailsPresenter.collapseExpandClicked(View.GONE);
        verify(stockDetailsView).expandLots();
        verify(stockDetailsView, never()).collapseLots();
    }

}
