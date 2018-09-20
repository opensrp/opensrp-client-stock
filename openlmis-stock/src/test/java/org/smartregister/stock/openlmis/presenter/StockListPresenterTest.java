package org.smartregister.stock.openlmis.presenter;

import android.view.View;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.smartregister.stock.openlmis.BaseUnitTest;
import org.smartregister.stock.openlmis.adapter.ListCommodityTypeAdapter;
import org.smartregister.stock.openlmis.domain.TradeItem;
import org.smartregister.stock.openlmis.domain.openlmis.CommodityType;
import org.smartregister.stock.openlmis.domain.openlmis.Program;
import org.smartregister.stock.openlmis.interactor.StockListInteractor;
import org.smartregister.stock.openlmis.view.contract.StockListView;
import org.smartregister.stock.openlmis.wrapper.TradeItemWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by samuelgithengi on 7/18/18.
 */
public class StockListPresenterTest extends BaseUnitTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private StockListView stockListView;

    @Mock
    private StockListInteractor stockListInteractor;

    @Mock
    private ListCommodityTypeAdapter commodityTypeAdapter;

    private StockListPresenter stockListPresenter;

    @Before
    public void setUp() {
        stockListPresenter = new StockListPresenter(stockListView, stockListInteractor);
        stockListPresenter.setCommodityTypeAdapter(commodityTypeAdapter);
    }

    @Test
    public void testGetPrograms() {
        List<Program> expected = new ArrayList<>();
        Program program = new Program("ESSD");
        program.setName("Essential Drugs");
        expected.add(program);

        when(stockListInteractor.getPrograms()).thenReturn(expected);
        List<Program> returnedPrograms = stockListPresenter.getPrograms();
        verify(stockListInteractor, only()).getPrograms();
        assertEquals(1, returnedPrograms.size());
        assertEquals("ESSD", returnedPrograms.get(0).getId());
        assertEquals("Essential Drugs", returnedPrograms.get(0).getName());
    }

    @Test
    public void testGetCommodityTypes() {
        List<CommodityType> expected = new ArrayList<>();
        String uuid = UUID.randomUUID().toString();
        expected.add(new CommodityType(uuid, "BCG", null, null, null, System.currentTimeMillis()));
        expected.add(new CommodityType(UUID.randomUUID().toString(), "OPV", null, null, null, System.currentTimeMillis()));

        when(stockListInteractor.getCommodityTypes()).thenReturn(expected);
        List<CommodityType> commodityTypes = stockListPresenter.getCommodityTypes();
        verify(stockListInteractor).getCommodityTypes();
        assertEquals(2, commodityTypes.size());
        assertEquals(uuid, commodityTypes.get(0).getId());
        assertEquals("BCG", commodityTypes.get(0).getName());
        assertEquals("OPV", commodityTypes.get(1).getName());

    }

    @Test
    public void getTradeItems() throws Exception {

        List<TradeItemWrapper> expected = new ArrayList<>();
        UUID uuid = UUID.randomUUID();
        TradeItem tradeItem = new TradeItem(uuid.toString());
        tradeItem.setName("Intervax BCG 20");
        tradeItem.setCommodityTypeId("305730154758");
        expected.add(new TradeItemWrapper(tradeItem));

        CommodityType commodityType = new CommodityType(uuid.toString(), "BCG", null,
                null, null, System.currentTimeMillis());
        when(stockListInteractor.getTradeItems(commodityType)).thenReturn(expected);

        List<TradeItemWrapper> tradeItems = stockListPresenter.getTradeItems(commodityType);

        verify(stockListInteractor).getTradeItems(commodityType);

        assertEquals(1, tradeItems.size());
        assertEquals(uuid.toString(), tradeItems.get(0).getTradeItem().getId());
        assertEquals("305730154758", tradeItems.get(0).getTradeItem().getCommodityTypeId().toString());
        assertEquals("Intervax BCG 20", tradeItems.get(0).getTradeItem().getName());
    }

    @Test
    public void testStockActionClicked() {
        View view = mock(View.class);
        stockListPresenter.stockActionClicked(view);
        verify(stockListView).showStockActionMenu(view);
    }

    @Test
    public void testExpandAllClicked() {
        stockListPresenter.expandAllClicked();
        verify(commodityTypeAdapter).expandAllViews();
    }

    @Test
    public void testCollapseAllClicked() {
        stockListPresenter.collapseAllClicked();
        verify(commodityTypeAdapter).collapseAllViews();

    }

    @Test
    public void testFindCommodityTypesByIds() {
        Set<String> commodityIds = new HashSet<>();
        String commodityTypeID = UUID.randomUUID().toString();
        commodityIds.add(commodityTypeID);

        CommodityType commodityType = new CommodityType(UUID.randomUUID().toString(), "BCG", null, null,
                null, System.currentTimeMillis());
        List<CommodityType> expected = new ArrayList<>();
        expected.add(commodityType);
        when(stockListInteractor.findCommodityTypesByIds(commodityIds)).thenReturn(expected);
        List<CommodityType> actual = stockListPresenter.findCommodityTypesByIds(commodityIds);
        verify(stockListInteractor).findCommodityTypesByIds(commodityIds);
        assertEquals(1, actual.size());
        assertEquals(commodityType.getId(), actual.get(0).getId());
        assertEquals("BCG", actual.get(0).getName());

    }

    @Test
    public void testSearchIds() {
        Map<String, Set<String>> expected = new HashMap<>();
        Set<String> tradeItems = new HashSet<>();
        String tradeItemId = UUID.randomUUID().toString();
        tradeItems.add(tradeItemId);
        String commodityId = UUID.randomUUID().toString();
        expected.put(commodityId, tradeItems);
        when(stockListInteractor.searchIds("BCG")).thenReturn(expected);
        Map<String, Set<String>> actual = stockListPresenter.searchIds("BCG");
        verify(stockListInteractor).searchIds("BCG");
        assertEquals(1, actual.size());
        assertEquals(1, actual.get(commodityId).size());
        assertEquals(tradeItemId, actual.get(commodityId).iterator().next());
    }

    @Test
    public void testFindTradeItemsByIds() {
        Set<String> tradeItems = new HashSet<>();
        String tradeItemId = UUID.randomUUID().toString();
        tradeItems.add(tradeItemId);

        List<TradeItemWrapper> expectedTradeItems = new ArrayList<>();
        TradeItem tradeItem = new TradeItem(tradeItemId);
        tradeItem.setName("Intervax BCG 20");
        tradeItem.setNetContent(16l);
        tradeItem.setCommodityTypeId(UUID.randomUUID().toString());
        TradeItemWrapper tradeItemWrapper = new TradeItemWrapper(tradeItem);
        tradeItemWrapper.setTotalStock(32);
        tradeItemWrapper.setNumberOfLots(2);
        expectedTradeItems.add(tradeItemWrapper);


        when(stockListInteractor.findTradeItemsByIds(tradeItems)).thenReturn(expectedTradeItems);


        List<TradeItemWrapper> actual = stockListPresenter.findTradeItemsByIds(tradeItems);
        verify(stockListInteractor).findTradeItemsByIds(tradeItems);
        assertEquals(1, actual.size());
        assertEquals(tradeItem.getId(), actual.get(0).getTradeItem().getId());
        assertEquals(tradeItem.getNetContent(), actual.get(0).getTradeItem().getNetContent());

        assertEquals(2, actual.get(0).getNumberOfLots());

        assertEquals(32, actual.get(0).getTotalStock());
    }
}
