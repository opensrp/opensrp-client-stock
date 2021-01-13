package org.smartregister.stock.repository.dao;

import com.ibm.fhir.model.resource.Bundle;

import org.apache.commons.lang3.NotImplementedException;
import org.junit.Before;
import org.junit.Test;
import org.smartregister.domain.StockAndProductDetails;
import org.smartregister.stock.BaseUnitTest;
import org.smartregister.stock.domain.Stock;
import org.smartregister.stock.domain.StockType;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class StockDaoImplTest extends BaseUnitTest {

    private StockDaoImpl stockDao;

    @Before
    public void setUp() {
        stockDao = spy(new StockDaoImpl());
    }

    @Test(expected = NotImplementedException.class)
    public void testFindInventoryItemsInAJurisdiction() {
        stockDao.findInventoryItemsInAJurisdiction(anyString());
    }

    @Test(expected = NotImplementedException.class)
    public void testFindInventoryInAServicePoint() {
        stockDao.findInventoryInAServicePoint(anyString());
    }

    @Test
    public void testGetStockByIdShouldReturnABundleList() {
        String stockId = UUID.randomUUID().toString();
        Stock stock = new Stock(stockId, "", "", 0, 4343444l, "", "", 4343444l, "");
        StockType stockType = new StockType(1l, 0, "", "", "", "");
        stockType.setUniqueId(2l);
        StockAndProductDetails stockAndProductDetails = new StockAndProductDetails(stock, stockType);

        doReturn(stockAndProductDetails).when(stockDao).findStockWithStockTypeByStockId(eq(stockId));

        List<Bundle> bundleList = stockDao.getStockById(stockId);

        assertNotNull(bundleList);

        assertEquals(1, bundleList.size());
    }
}