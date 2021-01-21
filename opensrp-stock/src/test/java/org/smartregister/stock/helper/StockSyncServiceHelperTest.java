package org.smartregister.stock.helper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.util.ReflectionHelpers;
import org.smartregister.stock.BaseUnitTest;
import org.smartregister.stock.StockLibrary;
import org.smartregister.stock.configuration.StockSyncConfiguration;
import org.smartregister.stock.domain.Stock;
import org.smartregister.stock.repository.StockRepository;

import java.util.ArrayList;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class StockSyncServiceHelperTest extends BaseUnitTest {

    @Mock
    private StockRepository stockRepository;

    @Mock
    private StockSyncConfiguration stockSyncConfiguration;

    private StockSyncServiceHelper stockSyncServiceHelper;

    @Mock
    private StockLibrary stockLibrary;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        stockSyncServiceHelper = spy(new StockSyncServiceHelper(stockSyncConfiguration));
    }

    @Test
    public void testBatchInsertStocksShouldUseDefaultLogicIfConfigIsFalse() {
        ReflectionHelpers.setStaticField(StockLibrary.class, "instance", stockLibrary);

        Stock stock = new Stock(null, "", "", 0, 4343444l, "", "", 4343444l, "");

        doReturn(true).when(stockSyncConfiguration).useDefaultStockExistenceCheck();

        doReturn(stockRepository).when(stockSyncServiceHelper).getStockRepository();

        doReturn(new ArrayList<>()).when(stockRepository).findUniqueStock(anyString(), anyString(), anyString(), anyString(), anyString(), anyString());

        stockSyncServiceHelper.batchInsertStocks(Collections.singletonList(stock));

        verify(stockRepository).add(any(Stock.class));
    }

    @Test
    public void testBatchInsertStocksShouldUseDefaultLogicIfConfigIsTrue() {
        ReflectionHelpers.setStaticField(StockLibrary.class, "instance", stockLibrary);

        Stock stock = new Stock(null, "", "", 0, 4343444l, "", "", 4343444l, "");

        doReturn(stockRepository).when(stockSyncServiceHelper).getStockRepository();

        stockSyncServiceHelper.batchInsertStocks(Collections.singletonList(stock));

        verify(stockRepository).batchInsertStock(anyList());
    }

    @After
    public void tearDown() {
        ReflectionHelpers.setStaticField(StockLibrary.class, "instance", null);
    }

}