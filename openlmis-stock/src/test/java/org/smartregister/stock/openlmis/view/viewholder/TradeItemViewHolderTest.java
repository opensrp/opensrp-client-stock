package org.smartregister.stock.openlmis.view.viewholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RuntimeEnvironment;
import org.smartregister.stock.openlmis.BaseUnitTest;
import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.domain.TradeItem;
import org.smartregister.stock.openlmis.domain.openlmis.Dispensable;
import org.smartregister.stock.openlmis.dto.TradeItemDto;
import org.smartregister.stock.openlmis.presenter.StockListPresenter;
import org.smartregister.stock.openlmis.wrapper.TradeItemWrapper;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

/**
 * Created by samuelgithengi on 8/13/18.
 */
public class TradeItemViewHolderTest extends BaseUnitTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private StockListPresenter stockListPresenter;

    @Captor
    private ArgumentCaptor<TradeItemDto> tradeItemDtoArgumentCaptor;

    private Context context = RuntimeEnvironment.application;

    private TradeItemViewHolder viewHolder;

    private View view;

    private String tradeItemId = UUID.randomUUID().toString();

    @Before
    public void setUp() {
        view = LayoutInflater.from(context).inflate(R.layout.trade_item_row, null);
        viewHolder = new TradeItemViewHolder(stockListPresenter, view);
        TradeItem tradeItem = new TradeItem(tradeItemId);
        tradeItem.setName("Intervax BCG 20");
        tradeItem.setCommodityTypeId("305730154758");
        tradeItem.setDispensable(new Dispensable(UUID.randomUUID().toString(), "vials", null, null));
        TradeItemWrapper tradeItemWrapper = new TradeItemWrapper(tradeItem);
        tradeItemWrapper.setNumberOfLots(10);
        tradeItemWrapper.setTotalStock(50);
        viewHolder.setTradeItemWrapper(tradeItemWrapper);
    }

    @Test
    public void testOnClick() {
        view.performClick();
        verify(stockListPresenter).startStockDetailsActivity(tradeItemDtoArgumentCaptor.capture());
        assertEquals("Intervax BCG 20", tradeItemDtoArgumentCaptor.getValue().getName());
        assertEquals(tradeItemId, tradeItemDtoArgumentCaptor.getValue().getId());
        assertEquals(10, tradeItemDtoArgumentCaptor.getValue().getNumberOfLots().intValue());
        assertEquals(50, tradeItemDtoArgumentCaptor.getValue().getTotalStock().intValue());
        assertEquals("vials", tradeItemDtoArgumentCaptor.getValue().getDispensingUnit());
    }
}