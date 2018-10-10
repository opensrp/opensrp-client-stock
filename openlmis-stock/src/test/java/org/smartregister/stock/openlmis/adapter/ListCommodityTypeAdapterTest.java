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
import org.smartregister.stock.openlmis.domain.openlmis.Dispensable;
import org.smartregister.stock.openlmis.listener.ExpandCollapseListener;
import org.smartregister.stock.openlmis.presenter.StockListPresenter;
import org.smartregister.stock.openlmis.view.viewholder.CommodityTypeViewHolder;
import org.smartregister.stock.openlmis.wrapper.TradeItemWrapper;

import java.util.ArrayList;
import java.util.Collections;
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

    private CommodityType bcGCommodityType = new CommodityType(UUID.randomUUID().toString(), "BCG",
            null, null, null, System.currentTimeMillis());

    private List<CommodityType> commodityTypeList;

    private String tradeItemId = UUID.randomUUID().toString();

    private Map<String, Set<String>> programIds;

    private String programId = UUID.randomUUID().toString();

    @Before
    public void setUp() {

        commodityTypeList = new ArrayList<>();
        commodityTypeList.add(bcGCommodityType);
        commodityTypeList.add(new CommodityType(UUID.randomUUID().toString(), "OPV", null, null, null, System.currentTimeMillis()));

        programIds = Collections.singletonMap(bcGCommodityType.getId(), Collections.singleton(tradeItemId));
        when(stockListPresenter.searchIdsByPrograms(programId)).thenReturn(programIds);
        when(stockListPresenter.findCommodityTypesByIds(programIds.keySet())).thenReturn(commodityTypeList);


        listCommodityTypeAdapter = new ListCommodityTypeAdapter(stockListPresenter, context);
        listCommodityTypeAdapter.setProgramId(programId);
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
    public void testOnBindViewHolderWithTradeItem() {

        List<TradeItemWrapper> expectedTradeItems = new ArrayList<>();
        TradeItem tradeItem = new TradeItem(tradeItemId);
        tradeItem.setCommodityTypeId(bcGCommodityType.getId());
        tradeItem.setName("Intervax BCG 20");
        tradeItem.setNetContent(20l);
        tradeItem.setDispensable(new Dispensable("id_1", "vials", null, "doses"));
        TradeItemWrapper tradeItemWrapper = new TradeItemWrapper(tradeItem);
        tradeItemWrapper.setTotalStock(60);
        expectedTradeItems.add(tradeItemWrapper);

        Set<String> ids = new HashSet<>();
        ids.add(tradeItemId);
        when(stockListPresenter.findTradeItemsByIds(programId, ids)).thenReturn(expectedTradeItems);

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
        assertEquals("0", holder.getDoseTextView().getText().toString().trim());
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
        when(stockListPresenter.searchIds("BCG")).thenReturn(new HashMap<String, Set<String>>());
        LinearLayout vg = new LinearLayout(context);
        CommodityTypeViewHolder holder = listCommodityTypeAdapter.onCreateViewHolder(vg, 0);
        listCommodityTypeAdapter.onBindViewHolder(holder, 0);
        listCommodityTypeAdapter.onBindViewHolder(holder, 1);
        listCommodityTypeAdapter.filterCommodityTypes("BCG");
        assertEquals(0, listCommodityTypeAdapter.getItemCount());

    }


    @Test
    public void testFilterCommodityTypes() {
        Map<String, Set<String>> expected = new HashMap<>();
        expected.put(bcGCommodityType.getId(), Collections.singleton(tradeItemId));
        when(stockListPresenter.searchIds("BCG")).thenReturn(expected);
        when(stockListPresenter.filterValidPrograms(programIds, expected)).thenReturn(expected);
        Set<String> commodityTypeId = new HashSet<>();
        commodityTypeId.add(bcGCommodityType.getId());
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
        Map<String, Set<String>> expected = new HashMap<>();
        Set<String> tradeItemIds = new HashSet<>();
        tradeItemIds.add(UUID.randomUUID().toString());
        expected.put(bcGCommodityType.getId(), tradeItemIds);
        when(stockListPresenter.searchIds("BCG")).thenReturn(expected);
        when(stockListPresenter.filterValidPrograms(programIds, expected)).thenReturn(expected);
        Set<String> commodityTypeId = new HashSet<>();
        commodityTypeId.add(bcGCommodityType.getId());
        commodityTypeList.remove(1);
        when(stockListPresenter.findCommodityTypesByIds(commodityTypeId)).thenReturn(commodityTypeList);
        List<TradeItemWrapper> tradeItemWrappers = new ArrayList<>();
        tradeItemWrappers.add(new TradeItemWrapper(new TradeItem(UUID.randomUUID().toString())));
        tradeItemWrappers.get(0).setTotalStock(12);
        tradeItemWrappers.get(0).getTradeItem().setNetContent(10l);
        tradeItemWrappers.get(0).getTradeItem().setDispensable(new Dispensable("id_1", "vials", null, "doses"));
        when(stockListPresenter.findTradeItemsByIds(programId, tradeItemIds)).thenReturn(tradeItemWrappers);

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
