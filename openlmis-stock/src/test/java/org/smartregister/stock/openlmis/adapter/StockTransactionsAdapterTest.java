package org.smartregister.stock.openlmis.adapter;

import android.content.Context;
import android.widget.LinearLayout;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RuntimeEnvironment;
import org.smartregister.stock.openlmis.BaseUnitTest;
import org.smartregister.stock.openlmis.domain.Stock;
import org.smartregister.stock.openlmis.dto.TradeItemDto;
import org.smartregister.stock.openlmis.presenter.StockDetailsPresenter;
import org.smartregister.stock.openlmis.view.viewholder.StockTransactionsViewHolder;
import org.smartregister.stock.openlmis.wrapper.StockWrapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * Created by samuelgithengi on 8/3/18.
 */
public class StockTransactionsAdapterTest extends BaseUnitTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    private Context context = RuntimeEnvironment.application;

    @Mock
    private StockDetailsPresenter stockDetailsPresenter;

    private StockTransactionAdapter stockTransactionAdapter;

    @Before
    public void setUp() {
        TradeItemDto tradeItemDto = new TradeItemDto(UUID.randomUUID().toString(),
                "GHGR", 100, System.currentTimeMillis(), 2, "vials", 5l, "doses");

        List<StockWrapper> stockList = new ArrayList<>();
        long now = System.currentTimeMillis();
        stockList.add(new StockWrapper(new Stock(null, Stock.received, "tester11", 50, now,
                "wareHouse123", "unsynched", now, tradeItemDto.getId()), "200lK", 52));

        stockList.add(new StockWrapper(new Stock(null, Stock.issued, "tester11", -12, now,
                "HO", "unsynched", now, tradeItemDto.getId()), "200K", 38));

        StockWrapper stockWrapper = new StockWrapper(new Stock(null, Stock.loss_adjustment, "tester11", 32, now,
                "Store", "unsynched", now, tradeItemDto.getId()), "200L", 60);
        stockWrapper.setReason("Beginning Balance Excess");
        stockList.add(stockWrapper);
        when(stockDetailsPresenter.findStockByTradeItem(tradeItemDto.getId())).thenReturn(null);
        when(stockDetailsPresenter.populateLotNamesAndBalance(tradeItemDto, null)).thenReturn(stockList);
        stockTransactionAdapter = new StockTransactionAdapter(tradeItemDto, stockDetailsPresenter);
    }


    @Test
    public void testOnCreateViewHolder() {
        LinearLayout vg = new LinearLayout(context);
        StockTransactionsViewHolder viewHolder = stockTransactionAdapter.onCreateViewHolder(vg, 0);
        assertNotNull(viewHolder.getDateTextView());
        assertNotNull(viewHolder.getToFromTextView());
        assertNotNull(viewHolder.getLotCodeTextView());
        assertNotNull(viewHolder.getReceivedTextView());
        assertNotNull(viewHolder.getIssuedTextView());
        assertNotNull(viewHolder.getAdjustmentTextView());
        assertNotNull(viewHolder.getBalanceTextView());
    }

    @Test
    public void testOnBindViewHolderReceived() {
        StockTransactionsViewHolder viewHolder = stockTransactionAdapter.onCreateViewHolder(new LinearLayout(context), 0);
        stockTransactionAdapter.onBindViewHolder(viewHolder, 0);
        assertEquals(StockTransactionAdapter.dateFormatter.format(new Date()), viewHolder.getDateTextView().getText());
        assertEquals("wareHouse123", viewHolder.getToFromTextView().getText());
        assertEquals("200lK", viewHolder.getLotCodeTextView().getText());
        assertEquals("50", viewHolder.getReceivedTextView().getText());
        assertEquals("", viewHolder.getIssuedTextView().getText());
        assertEquals("", viewHolder.getAdjustmentTextView().getText());
        assertEquals("52", viewHolder.getBalanceTextView().getText());
    }

    @Test
    public void testOnBindViewHolderIssued() {
        StockTransactionsViewHolder viewHolder = stockTransactionAdapter.onCreateViewHolder(new LinearLayout(context), 0);
        stockTransactionAdapter.onBindViewHolder(viewHolder, 1);
        assertEquals(StockTransactionAdapter.dateFormatter.format(new Date()), viewHolder.getDateTextView().getText());
        assertEquals("HO", viewHolder.getToFromTextView().getText());
        assertEquals("200K", viewHolder.getLotCodeTextView().getText());
        assertEquals("", viewHolder.getReceivedTextView().getText());
        assertEquals("12", viewHolder.getIssuedTextView().getText());
        assertEquals("", viewHolder.getAdjustmentTextView().getText());
        assertEquals("38", viewHolder.getBalanceTextView().getText());
    }


    @Test
    public void testOnBindViewHolderAdjusted() {
        StockTransactionsViewHolder viewHolder = stockTransactionAdapter.onCreateViewHolder(new LinearLayout(context), 0);
        stockTransactionAdapter.onBindViewHolder(viewHolder, 2);
        assertEquals(StockTransactionAdapter.dateFormatter.format(new Date()), viewHolder.getDateTextView().getText());
        assertEquals("Beginning Balance Excess", viewHolder.getToFromTextView().getText());
        assertEquals("200L", viewHolder.getLotCodeTextView().getText());
        assertEquals("", viewHolder.getReceivedTextView().getText());
        assertEquals("", viewHolder.getIssuedTextView().getText());
        assertEquals("32", viewHolder.getAdjustmentTextView().getText());
        assertEquals("60", viewHolder.getBalanceTextView().getText());
    }

    @Test
    public void testGetItemCount() {
        assertEquals(3, stockTransactionAdapter.getItemCount());
    }
}
