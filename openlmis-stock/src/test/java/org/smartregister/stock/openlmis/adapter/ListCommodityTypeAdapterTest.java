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
import org.smartregister.stock.openlmis.domain.TradeItem;
import org.smartregister.stock.openlmis.domain.openlmis.CommodityType;
import org.smartregister.stock.openlmis.listener.ExpandCollapseListener;
import org.smartregister.stock.openlmis.presenter.StockListPresenter;
import org.smartregister.stock.openlmis.view.viewholder.CommodityTypeViewHolder;
import org.smartregister.stock.openlmis.wrapper.TradeItemWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    private List<CommodityType> commodityTypeList;


    @Before
    public void setUp() {
        commodityTypeList = new ArrayList<>();
        commodityTypeList.add(bcGCommodityType);
        commodityTypeList.add(new CommodityType(UUID.randomUUID(), "OPV", "", null, null, System.currentTimeMillis()));

        when(stockListPresenter.getCommodityTypes()).thenReturn(commodityTypeList);

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

        List<TradeItemWrapper> expectedTradeItems = new ArrayList<>();
        TradeItem tradeItem = new TradeItem(UUID.randomUUID().toString());
        tradeItem.setCommodityTypeId(bcGCommodityType.getId().toString());
        tradeItem.setName("Intervax BCG 20");
        tradeItem.setNetContent(20l);
        TradeItemWrapper tradeItemWrapper = new TradeItemWrapper(tradeItem);
        tradeItemWrapper.setTotalStock(60);
        expectedTradeItems.add(tradeItemWrapper);

        when(stockListPresenter.getTradeItems(bcGCommodityType)).thenReturn(expectedTradeItems);

        LinearLayout vg = new LinearLayout(context);
        CommodityTypeViewHolder holder = listCommodityTypeAdapter.onCreateViewHolder(vg, 0);
        listCommodityTypeAdapter.onBindViewHolder(holder, 0);
        assertEquals("BCG (1)", holder.getCommodityTypeTextView().getText());
        assertEquals("1200 doses", holder.getDoseTextView().getText());
        assertNotNull(holder.getTradeItemsRecyclerView().getAdapter());
        assertEquals(1, holder.getTradeItemsRecyclerView().getAdapter().getItemCount());

    }

    @Test
    public void testOnBindViewHolderWithoutTradeItem() {
        LinearLayout vg = new LinearLayout(context);
        CommodityTypeViewHolder holder = listCommodityTypeAdapter.onCreateViewHolder(vg, 0);
        listCommodityTypeAdapter.onBindViewHolder(holder, 1);
        assertEquals("OPV (0)", holder.getCommodityTypeTextView().getText());
        assertEquals("0 doses", holder.getDoseTextView().getText());
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


    @Test
    public void testFilterCommodityTypesInvalidPhrase() {
        when(stockListPresenter.searchIds("BCG")).thenReturn(new HashMap<String, List<String>>());
        LinearLayout vg = new LinearLayout(context);
        CommodityTypeViewHolder holder = listCommodityTypeAdapter.onCreateViewHolder(vg, 0);
        listCommodityTypeAdapter.onBindViewHolder(holder, 0);
        listCommodityTypeAdapter.onBindViewHolder(holder, 1);
        listCommodityTypeAdapter.filterCommodityTypes("BCG");
        assertEquals(0, listCommodityTypeAdapter.getItemCount());

    }


    @Test
    public void testFilterCommodityTypes() {
        Map<String, List<String>> expected = new HashMap<>();
        expected.put(bcGCommodityType.getId().toString(), null);
        when(stockListPresenter.searchIds("BCG")).thenReturn(expected);
        Set<String> commodityTypeId = new HashSet<>();
        commodityTypeId.add(bcGCommodityType.getId().toString());
        commodityTypeList.remove(1);
        when(stockListPresenter.findCommodityTypesByIds(commodityTypeId)).thenReturn(commodityTypeList);
        LinearLayout vg = new LinearLayout(context);
        CommodityTypeViewHolder holder = listCommodityTypeAdapter.onCreateViewHolder(vg, 0);
        listCommodityTypeAdapter.onBindViewHolder(holder, 0);
        listCommodityTypeAdapter.filterCommodityTypes("BCG");
        assertEquals(1, listCommodityTypeAdapter.getItemCount());

    }


    @Test
    public void testFilterTradeItemsTypes() {
        Map<String, List<String>> expected = new HashMap<>();
        List<String> tradeItemIds = new ArrayList<>();
        tradeItemIds.add(UUID.randomUUID().toString());
        expected.put(bcGCommodityType.getId().toString(), tradeItemIds);
        when(stockListPresenter.searchIds("BCG")).thenReturn(expected);
        Set<String> commodityTypeId = new HashSet<>();
        commodityTypeId.add(bcGCommodityType.getId().toString());
        commodityTypeList.remove(1);
        when(stockListPresenter.findCommodityTypesByIds(commodityTypeId)).thenReturn(commodityTypeList);
        List<TradeItemWrapper> tradeItemWrappers = new ArrayList<>();
        tradeItemWrappers.add(new TradeItemWrapper(new TradeItem(UUID.randomUUID().toString())));
        tradeItemWrappers.get(0).setTotalStock(12);
        tradeItemWrappers.get(0).getTradeItem().setNetContent(10l);
        when(stockListPresenter.findTradeItemsByIds(tradeItemIds)).thenReturn(tradeItemWrappers);

        LinearLayout vg = new LinearLayout(context);
        CommodityTypeViewHolder holder = listCommodityTypeAdapter.onCreateViewHolder(vg, 0);
        listCommodityTypeAdapter.onBindViewHolder(holder, 0);


        listCommodityTypeAdapter.filterCommodityTypes("BCG1");
        assertEquals(0, listCommodityTypeAdapter.getItemCount());

        listCommodityTypeAdapter.filterCommodityTypes("BCG");
        assertEquals(1, listCommodityTypeAdapter.getItemCount());

        listCommodityTypeAdapter.onBindViewHolder(holder, 0);
        assertEquals("120 doses", holder.getDoseTextView().getText());

    }


}
