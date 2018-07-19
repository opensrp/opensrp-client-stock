package org.smartregister.stock.openlmis.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RuntimeEnvironment;
import org.smartregister.stock.openlmis.BaseUnitTest;
import org.smartregister.stock.openlmis.domain.CommodityType;
import org.smartregister.stock.openlmis.domain.Gtin;
import org.smartregister.stock.openlmis.domain.TradeItem;
import org.smartregister.stock.openlmis.listener.ExpandCollapseListener;
import org.smartregister.stock.openlmis.presenter.StockListPresenter;
import org.smartregister.stock.openlmis.view.viewholder.CommodityTypeViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by samuelgithengi on 7/19/18.
 */
public class ListCommodityTypeAdapterTest extends BaseUnitTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private StockListPresenter stockListPresenter;

    private Context context = RuntimeEnvironment.application;

    private ListCommodityTypeAdapter listCommodityTypeAdapter;

    private CommodityType bcGCommodityType = new CommodityType(UUID.randomUUID(), "BCG",
            "", null, null, System.currentTimeMillis());


    @Before
    public void setup() {
        List<CommodityType> expected = new ArrayList<>();
        expected.add(bcGCommodityType);
        expected.add(new CommodityType(UUID.randomUUID(), "OPV", "", null, null, System.currentTimeMillis()));

        when(stockListPresenter.getCommodityTypes()).thenReturn(expected);

        listCommodityTypeAdapter = new ListCommodityTypeAdapter(stockListPresenter, context);
    }

    @Test
    public void testOnCreateViewHolder() {
        LinearLayout vg = new LinearLayout(context);
        CommodityTypeViewHolder holder = listCommodityTypeAdapter.onCreateViewHolder(vg, 0);
        assertNotNull(holder.getCommodityTypeTextView());
        assertNotNull(holder.getDoseTextView());
        assertNotNull(holder.getTradeItemsRecyclerView());

        assertEquals(View.GONE, holder.getTradeItemsRecyclerView().getVisibility());
    }


    @Test
    public void testOnBindViewHolderWithTradeItem() throws Exception {

        List<TradeItem> expectedTradeItems = new ArrayList<>();
        TradeItem tradeItem = new TradeItem(UUID.randomUUID());
        tradeItem.setManufacturerOfTradeItem("Intervax BCG 20");
        tradeItem.setGtin(new Gtin("305730154758"));
        expectedTradeItems.add(tradeItem);

        when(stockListPresenter.getTradeItems(bcGCommodityType)).thenReturn(expectedTradeItems);

        LinearLayout vg = new LinearLayout(context);
        CommodityTypeViewHolder holder = listCommodityTypeAdapter.onCreateViewHolder(vg, 0);
        listCommodityTypeAdapter.onBindViewHolder(holder, 0);
        assertEquals("BCG ( 1)", holder.getCommodityTypeTextView().getText());
        assertEquals("1200 doses", holder.getDoseTextView().getText());
        assertNotNull(holder.getTradeItemsRecyclerView().getAdapter());
        assertEquals(1, holder.getTradeItemsRecyclerView().getAdapter().getItemCount());

    }

    @Test
    public void testOnBindViewHolderWithoutTradeItem() {
        LinearLayout vg = new LinearLayout(context);
        CommodityTypeViewHolder holder = listCommodityTypeAdapter.onCreateViewHolder(vg, 0);
        listCommodityTypeAdapter.onBindViewHolder(holder, 1);
        assertEquals("OPV ( 0)", holder.getCommodityTypeTextView().getText());
        assertEquals("1200 doses", holder.getDoseTextView().getText());
        assertNotNull(holder.getTradeItemsRecyclerView().getAdapter());
        assertEquals(0, holder.getTradeItemsRecyclerView().getAdapter().getItemCount());
    }


    @Test
    public void testGetItemCount() {
        assertEquals(2, listCommodityTypeAdapter.getItemCount());
    }

    @Test
    public void testExpandAllViews() {
        ExpandCollapseListener listener = mock(ExpandCollapseListener.class);
        listCommodityTypeAdapter.registerExpandCollapseListeners(listener);
        listCommodityTypeAdapter.expandAllViews();
        verify(listener).expandView();


    }

    @Test
    public void testCollapseAllViews() {
        ExpandCollapseListener listener = mock(ExpandCollapseListener.class);
        listCommodityTypeAdapter.registerExpandCollapseListeners(listener);
        listCommodityTypeAdapter.collapseAllViews();
        verify(listener).collapseView();
    }


}
