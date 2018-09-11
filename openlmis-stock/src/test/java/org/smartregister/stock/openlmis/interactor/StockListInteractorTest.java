package org.smartregister.stock.openlmis.interactor;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.smartregister.stock.openlmis.BaseUnitTest;
import org.smartregister.stock.openlmis.domain.TradeItem;
import org.smartregister.stock.openlmis.domain.openlmis.Code;
import org.smartregister.stock.openlmis.domain.openlmis.CommodityType;
import org.smartregister.stock.openlmis.domain.openlmis.Program;
import org.smartregister.stock.openlmis.dto.LotDetailsDto;
import org.smartregister.stock.openlmis.repository.StockRepository;
import org.smartregister.stock.openlmis.repository.TradeItemRepository;
import org.smartregister.stock.openlmis.repository.openlmis.CommodityTypeRepository;
import org.smartregister.stock.openlmis.repository.openlmis.ProgramRepository;
import org.smartregister.stock.openlmis.wrapper.TradeItemWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by samuelgithengi on 8/13/18.
 */
public class StockListInteractorTest extends BaseUnitTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private ProgramRepository programRepository;

    @Mock
    private CommodityTypeRepository commodityTypeRepository;

    @Mock
    private TradeItemRepository tradeItemRepository;

    @Mock
    private StockRepository stockRepository;

    private StockListInteractor stockListInteractor;

    @Before
    public void setUp() {
        stockListInteractor = new StockListInteractor(programRepository, commodityTypeRepository,
                tradeItemRepository, stockRepository);
    }

    @Test
    public void testGetPrograms() {
        List<Program> expected = new ArrayList<>();
        expected.add(new Program(UUID.randomUUID().toString(), new Code("PRG002"), "Essential Drugs",
                null, true, true, true,
                true, true, null));
        when(programRepository.findAllPrograms()).thenReturn(expected);
        List<String> programs = stockListInteractor.getPrograms();
        assertEquals(1, programs.size());
        assertEquals("Essential Drugs", programs.get(0));
    }

    @Test
    public void testGetCommodityTypes() {
        List<CommodityType> expected = new ArrayList<>();
        UUID uuid = UUID.randomUUID();
        expected.add(new CommodityType(uuid.toString(), "BCG", null, null,
                null, System.currentTimeMillis()));
        when(commodityTypeRepository.findAllCommodityTypes()).thenReturn(expected);
        List<CommodityType> commodityTypes = stockListInteractor.getCommodityTypes();
        assertEquals(1, commodityTypes.size());
        assertEquals(uuid.toString(), commodityTypes.get(0).getId());
        assertEquals("BCG", commodityTypes.get(0).getName());
        verify(commodityTypeRepository).findAllCommodityTypes();
    }

    @Test
    public void testGetTradeItemsEmptyTradeItems() {

        CommodityType commodityType = new CommodityType(UUID.randomUUID().toString(), "BCG", null, null,
                null, System.currentTimeMillis());
        List<TradeItem> expectedTradeItems = new ArrayList<>();
        when(tradeItemRepository.getTradeItemByCommodityType(commodityType.getId().toString())).thenReturn(expectedTradeItems);
        List<TradeItemWrapper> tradeItems = stockListInteractor.getTradeItems(commodityType);
        assertTrue(tradeItems.isEmpty());
        verify(tradeItemRepository).getTradeItemByCommodityType(commodityType.getId().toString());
    }

    @Test
    public void testGetTradeItems() {

        CommodityType commodityType = new CommodityType(UUID.randomUUID().toString(), "BCG", null, null,
                null, System.currentTimeMillis());
        List<TradeItem> expectedTradeItems = new ArrayList<>();
        TradeItem tradeItem = new TradeItem(UUID.randomUUID().toString());
        tradeItem.setName("Intervax BCG 20");
        tradeItem.setNetContent(16l);
        tradeItem.setCommodityTypeId(commodityType.getId().toString());
        expectedTradeItems.add(tradeItem);
        when(tradeItemRepository.getTradeItemByCommodityType(commodityType.getId().toString())).thenReturn(expectedTradeItems);
        List<LotDetailsDto> lots = new ArrayList<>();
        lots.add(new LotDetailsDto("lot1",1l,20));
        lots.add(new LotDetailsDto("lot2",30l,30));
        when(stockRepository.getNumberOfLotsByTradeItem(tradeItem.getId())).thenReturn(lots);
        List<TradeItemWrapper> tradeItems = stockListInteractor.getTradeItems(commodityType);
        assertEquals(1, tradeItems.size());
        assertEquals(tradeItem.getId(), tradeItems.get(0).getTradeItem().getId());
        assertEquals("Intervax BCG 20", tradeItems.get(0).getTradeItem().getName());
        assertEquals(16l, tradeItems.get(0).getTradeItem().getNetContent().longValue());
        assertEquals(tradeItem.getId(), tradeItems.get(0).getTradeItem().getId());
        assertEquals(2, tradeItems.get(0).getNumberOfLots());
        assertEquals(50, tradeItems.get(0).getTotalStock());
        verify(tradeItemRepository).getTradeItemByCommodityType(commodityType.getId().toString());
    }
}
