package org.smartregister.stock.openlmis.interactor;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.smartregister.Context;
import org.smartregister.repository.Repository;
import org.smartregister.stock.openlmis.BaseUnitTest;
import org.smartregister.stock.openlmis.OpenLMISLibrary;
import org.smartregister.stock.openlmis.domain.TradeItem;
import org.smartregister.stock.openlmis.domain.openlmis.Code;
import org.smartregister.stock.openlmis.domain.openlmis.CommodityType;
import org.smartregister.stock.openlmis.domain.openlmis.Program;
import org.smartregister.stock.openlmis.dto.LotDetailsDto;
import org.smartregister.stock.openlmis.repository.SearchRepository;
import org.smartregister.stock.openlmis.repository.StockRepository;
import org.smartregister.stock.openlmis.repository.TradeItemRepository;
import org.smartregister.stock.openlmis.repository.openlmis.CommodityTypeRepository;
import org.smartregister.stock.openlmis.repository.openlmis.ProgramOrderableRepository;
import org.smartregister.stock.openlmis.repository.openlmis.ProgramRepository;
import org.smartregister.stock.openlmis.wrapper.TradeItemWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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

    @Mock
    private SearchRepository searchRepository;

    @Mock
    private Context context;

    @Mock
    private Repository repository;

    @Mock
    private ProgramOrderableRepository programOrderableRepository;

    private StockListInteractor stockListInteractor;

    @Before
    public void setUp() {
        stockListInteractor = new StockListInteractor(programRepository, commodityTypeRepository,
                tradeItemRepository, stockRepository, searchRepository, programOrderableRepository);
    }

    @Test
    public void testConstructor() {
        OpenLMISLibrary.init(context, repository);
        StockListInteractor stockListInteractor = new StockListInteractor();
        assertNotNull(stockListInteractor);
    }

    @Test
    public void testGetPrograms() {
        List<Program> expected = new ArrayList<>();
        expected.add(new Program(UUID.randomUUID().toString(), new Code("PRG002"), "Essential Drugs",
                null, true));
        when(programRepository.findAllPrograms()).thenReturn(expected);
        List<Program> programs = stockListInteractor.getPrograms();
        assertEquals(1, programs.size());
        assertEquals("PRG002", programs.get(0).getCode().toString());
        assertEquals("Essential Drugs", programs.get(0).getName());
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
        when(tradeItemRepository.getTradeItemByCommodityType(commodityType.getId())).thenReturn(expectedTradeItems);
        List<TradeItemWrapper> tradeItems = stockListInteractor.getTradeItems(null, commodityType);
        assertTrue(tradeItems.isEmpty());
        verify(tradeItemRepository).getTradeItemByCommodityType(commodityType.getId());
    }

    @Test
    public void testGetTradeItems() {

        CommodityType commodityType = new CommodityType(UUID.randomUUID().toString(), "BCG", null, null,
                null, System.currentTimeMillis());
        List<TradeItem> expectedTradeItems = new ArrayList<>();
        TradeItem tradeItem = new TradeItem(UUID.randomUUID().toString());
        tradeItem.setName("Intervax BCG 20");
        tradeItem.setNetContent(16l);
        tradeItem.setCommodityTypeId(commodityType.getId());
        tradeItem.setHasLots(true);
        expectedTradeItems.add(tradeItem);
        when(tradeItemRepository.getTradeItemByCommodityType(commodityType.getId())).thenReturn(expectedTradeItems);
        List<LotDetailsDto> lots = new ArrayList<>();
        lots.add(new LotDetailsDto("lot1", 1l, 20));
        lots.add(new LotDetailsDto("lot2", 30l, 30));
        Map<String, List<LotDetailsDto>> lotsMap = new HashMap<>();
        lotsMap.put(tradeItem.getId(), lots);

        List<String> ids = new ArrayList<>();
        ids.add(tradeItem.getId());
        when(stockRepository.getNumberOfLotsByTradeItem(null, ids)).thenReturn(lotsMap);
        List<TradeItemWrapper> tradeItems = stockListInteractor.getTradeItems(null, commodityType);
        assertEquals(1, tradeItems.size());
        assertEquals(tradeItem.getId(), tradeItems.get(0).getTradeItem().getId());
        assertEquals("Intervax BCG 20", tradeItems.get(0).getTradeItem().getName());
        assertEquals(16l, tradeItems.get(0).getTradeItem().getNetContent().longValue());
        assertEquals(tradeItem.getId(), tradeItems.get(0).getTradeItem().getId());
        assertEquals(2, tradeItems.get(0).getNumberOfLots());
        assertEquals(50, tradeItems.get(0).getTotalStock());
        verify(tradeItemRepository).getTradeItemByCommodityType(commodityType.getId().toString());
    }

    @Test
    public void testSearchIds() {
        Map<String, Set<String>> expected = new HashMap<>();
        Set<String> tradeItems = new HashSet<>();
        String tradeItemId = UUID.randomUUID().toString();
        tradeItems.add(tradeItemId);
        String commodityId = UUID.randomUUID().toString();
        expected.put(commodityId, tradeItems);
        when(searchRepository.searchIds("BCG")).thenReturn(expected);
        Map<String, Set<String>> actual = stockListInteractor.searchIds("BCG");
        verify(searchRepository).searchIds("BCG");
        assertEquals(1, actual.size());
        assertEquals(1, actual.get(commodityId).size());
        assertEquals(tradeItemId, actual.get(commodityId).iterator().next());
    }


    @Test
    public void testFindTradeItemsByIds() {
        Set<String> tradeItems = new HashSet<>();
        String tradeItemId = UUID.randomUUID().toString();
        tradeItems.add(tradeItemId);

        List<TradeItem> expectedTradeItems = new ArrayList<>();
        TradeItem tradeItem = new TradeItem(tradeItemId);
        tradeItem.setName("Intervax BCG 20");
        tradeItem.setNetContent(16l);
        tradeItem.setCommodityTypeId(UUID.randomUUID().toString());
        tradeItem.setHasLots(true);
        expectedTradeItems.add(tradeItem);

        when(tradeItemRepository.getTradeItemByIds(tradeItems)).thenReturn(expectedTradeItems);

        List<LotDetailsDto> lots = new ArrayList<>();
        lots.add(new LotDetailsDto(UUID.randomUUID().toString(), 0l, 12));
        lots.add(new LotDetailsDto(UUID.randomUUID().toString(), 120l, 20));
        Map<String, List<LotDetailsDto>> lotMap = new HashMap<>();
        lotMap.put(tradeItemId, lots);
        when(stockRepository.getNumberOfLotsByTradeItem(anyString(), any(List.class))).thenReturn(lotMap);


        List<TradeItemWrapper> actual = stockListInteractor.findTradeItemsByIds("", tradeItems);
        verify(tradeItemRepository).getTradeItemByIds(tradeItems);
        assertEquals(1, actual.size());
        assertEquals(tradeItem.getId(), actual.get(0).getTradeItem().getId());
        assertEquals(tradeItem.getNetContent(), actual.get(0).getTradeItem().getNetContent());

        assertEquals(2, actual.get(0).getNumberOfLots());

        assertEquals(32, actual.get(0).getTotalStock());

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
        when(commodityTypeRepository.findCommodityTypesByIds(commodityIds)).thenReturn(expected);
        List<CommodityType> actual = stockListInteractor.findCommodityTypesByIds(commodityIds);
        verify(commodityTypeRepository).findCommodityTypesByIds(commodityIds);
        assertEquals(1, actual.size());
        assertEquals(commodityType.getId(), actual.get(0).getId());
        assertEquals("BCG", actual.get(0).getName());

    }
}
