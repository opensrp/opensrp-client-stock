package org.smartregister.stock.openlmis.adapter;

import android.content.Context;
import android.widget.LinearLayout;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RuntimeEnvironment;
import org.smartregister.stock.openlmis.BaseUnitTest;
import org.smartregister.stock.openlmis.domain.openlmis.Lot;
import org.smartregister.stock.openlmis.dto.TradeItemDto;
import org.smartregister.stock.openlmis.presenter.StockDetailsPresenter;
import org.smartregister.stock.openlmis.view.viewholder.LotViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.smartregister.stock.openlmis.adapter.LotAdapter.DATE_FORMAT;

/**
 * Created by samuelgithengi on 8/3/18.
 */
public class LotAdapterTest extends BaseUnitTest {
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    private Context context = RuntimeEnvironment.application;

    @Mock
    private StockDetailsPresenter stockDetailsPresenter;

    private LotAdapter lotAdapter;

    private UUID lotId = UUID.randomUUID();


    @Before
    public void setUp() {
        TradeItemDto tradeItemDto = new TradeItemDto(UUID.randomUUID().toString(),
                "GHGR", 100, System.currentTimeMillis(), 2, "vials", 5l);
        List<Lot> lots = new ArrayList<>();
        Lot lot=new Lot(lotId.toString(), "LC2018G", 934983l,
                92903l, null, true);
        lot.setLotStatus("VMM1");
        lots.add(lot);
        when(stockDetailsPresenter.findLotsByTradeItem(tradeItemDto.getId())).thenReturn(lots);
        lotAdapter = new LotAdapter(tradeItemDto, stockDetailsPresenter);
    }

    @Test
    public void testOnCreateViewHolder() {
        LinearLayout vg = new LinearLayout(context);
        LotViewHolder lotViewHolder = lotAdapter.onCreateViewHolder(vg, 0);
        assertNotNull(lotViewHolder.getStatusTextView());
        assertNotNull(lotViewHolder.getLotCodeTextView());
        assertNotNull(lotViewHolder.getStockOnHandTextView());
    }

    @Test
    public void testOnBindViewHolder() {
        when(stockDetailsPresenter.getTotalStockByLot(lotId.toString())).thenReturn(59);
        LotViewHolder lotViewHolder = lotAdapter.onCreateViewHolder(new LinearLayout(context), 0);
        lotAdapter.onBindViewHolder(lotViewHolder, 0);
        assertEquals("59 vials", lotViewHolder.getStockOnHandTextView().getText());
        assertEquals("LC2018G Exp. " + new LocalDate(934983l).toString(DATE_FORMAT), lotViewHolder.getLotCodeTextView().getText());
        assertEquals("VMM1", lotViewHolder.getStatusTextView().getText());
    }

    @Test
    public void testGetItemCount() {
        assertEquals(1, lotAdapter.getItemCount());
    }
}
