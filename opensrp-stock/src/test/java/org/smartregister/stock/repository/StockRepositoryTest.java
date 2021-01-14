package org.smartregister.stock.repository;

import android.content.ContentValues;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.util.ReflectionHelpers;
import org.smartregister.domain.StockAndProductDetails;
import org.smartregister.stock.BaseUnitTest;
import org.smartregister.stock.StockLibrary;
import org.smartregister.stock.domain.Stock;

import java.util.Collections;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class StockRepositoryTest extends BaseUnitTest {

    private StockRepository stockRepository;

    @Mock
    private SQLiteDatabase sqLiteDatabase;

    @Mock
    private Cursor cursor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        stockRepository = spy(new StockRepository());
    }

    @Test
    public void testBatchInsertStockShouldInsertNewRecord() {
        Stock stock = new Stock(null, "", "", 0, 4343444l, "", "", 4343444l, "");
        stock.setCustomProperties("{sd =0}");
        stock.setAccountabilityEndDate("2021-02-02T03:00:00.000+0300");
        stock.setDeliveryDate("2021-02-02T03:00:00.000+0300");
        stock.setDateCreated(1610614283338l);

        doReturn(sqLiteDatabase).when(stockRepository).getReadableDatabase();
        doReturn(sqLiteDatabase).when(stockRepository).getWritableDatabase();

        assertTrue(StringUtils.isBlank(stock.getSyncStatus()));

        stockRepository.batchInsertStock(Collections.singletonList(stock));

        ArgumentCaptor<ContentValues> contentValuesArgumentCaptor = ArgumentCaptor.forClass(ContentValues.class);
        verify(sqLiteDatabase).insert(eq(StockRepository.STOCK_TABLE_NAME), isNull(), contentValuesArgumentCaptor.capture());

        assertNotNull(contentValuesArgumentCaptor.getValue());

        ContentValues tempContentValues = contentValuesArgumentCaptor.getValue();

        assertEquals(stock.getUpdatedAt(), tempContentValues.get(StockRepository.DATE_UPDATED));

        assertNotNull(stock.getSyncStatus());
    }

    @Test
    public void testBatchInsertStockShouldInsertUpdateExistingRecord() {
        Stock stock = new Stock(null, "", "", 0, 4343444l, "", "", 4343444l, "");
        stock.setStockId(UUID.randomUUID().toString());

        doReturn(sqLiteDatabase).when(stockRepository).getReadableDatabase();
        doReturn(sqLiteDatabase).when(stockRepository).getWritableDatabase();

        assertTrue(StringUtils.isBlank(stock.getSyncStatus()));

        doReturn(Collections.singleton(stock.getStockId())).when(stockRepository).populateStockIds(anySet());

        stockRepository.batchInsertStock(Collections.singletonList(stock));

        ArgumentCaptor<ContentValues> contentValuesArgumentCaptor = ArgumentCaptor.forClass(ContentValues.class);

        verify(sqLiteDatabase, never()).insert(eq(StockRepository.STOCK_TABLE_NAME), isNull(), contentValuesArgumentCaptor.capture());

        verify(sqLiteDatabase).update(eq(StockRepository.STOCK_TABLE_NAME), contentValuesArgumentCaptor.capture(), eq(StockRepository.STOCK_ID + " = ?"), eq(new String[]{stock.getStockId()}));

        assertNotNull(contentValuesArgumentCaptor.getValue());

        ContentValues tempContentValues = contentValuesArgumentCaptor.getValue();

        assertEquals(stock.getUpdatedAt(), tempContentValues.get(StockRepository.DATE_UPDATED));

        assertNotNull(stock.getSyncStatus());

        assertNull(tempContentValues.get(StockRepository.ID_COLUMN));
    }

    @Test
    public void testFindStockWithStockTypeByStockIdShouldReturnStockAndProductDetails() {
        String stockId = "43245";

        StockLibrary stockLibrary = mock(StockLibrary.class);

        StockTypeRepository stockTypeRepository = new StockTypeRepository();

        doReturn(stockTypeRepository).when(stockLibrary).getStockTypeRepository();

        ReflectionHelpers.setStaticField(StockLibrary.class, "instance", stockLibrary);

        doReturn(1).when(cursor).getColumnIndex(StockRepository.ID_COLUMN);

        doReturn("23").when(cursor).getString(1);

        doReturn(sqLiteDatabase).when(stockRepository).getReadableDatabase();

        doReturn(cursor).when(sqLiteDatabase).rawQuery(anyString(), any(String[].class));

        doReturn(true).when(cursor).moveToNext();

        StockAndProductDetails stockAndProductDetails = stockRepository.findStockWithStockTypeByStockId(stockId);

        assertNotNull(stockAndProductDetails);
    }
}