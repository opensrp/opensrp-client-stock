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
import java.util.List;
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
        List<String> expected = new ArrayList<>();
        Program program = new Program("ESSD");
        program.setName("Essential Drugs");
        expected.add(program.getName());

        when(stockListInteractor.getPrograms()).thenReturn(expected);
        List<String> returnedPrograms = stockListPresenter.getPrograms();
        verify(stockListInteractor, only()).getPrograms();
        assertEquals(1, returnedPrograms.size());
        assertEquals("Essential Drugs", returnedPrograms.get(0));
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
}
