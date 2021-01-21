package org.smartregister.stock.repository;

import android.content.ContentValues;

import net.sqlcipher.database.SQLiteDatabase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.smartregister.stock.BaseUnitTest;
import org.smartregister.stock.domain.StockType;

import java.util.Collections;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class StockTypeRepositoryTest extends BaseUnitTest {

    private StockTypeRepository stockTypeRepository;

    @Mock
    private SQLiteDatabase sqLiteDatabase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        stockTypeRepository = spy(new StockTypeRepository());
    }

    @Test
    public void testBatchInsertStockTypesShouldInsertNewRecord() {
        StockType stockType = new StockType(1l, 0, "", "", "", "");
        stockType.setUniqueId(2l);

        doReturn(sqLiteDatabase).when(stockTypeRepository).getReadableDatabase();
        doReturn(sqLiteDatabase).when(stockTypeRepository).getWritableDatabase();

        stockTypeRepository.batchInsertStockTypes(Collections.singletonList(stockType));

        ArgumentCaptor<ContentValues> contentValuesArgumentCaptor = ArgumentCaptor.forClass(ContentValues.class);
        verify(sqLiteDatabase).insert(eq(StockTypeRepository.STOCK_TYPE_TABLE_NAME), isNull(), contentValuesArgumentCaptor.capture());

        assertNotNull(contentValuesArgumentCaptor.getValue());
    }

    @Test
    public void testBatchInsertStockTypesShouldUpdateRecord() {
        StockType stockType = new StockType(1l, 0, "", "", "", "");
        stockType.setUniqueId(2l);

        doReturn(Collections.singleton(stockType.getUniqueId()))
                .when(stockTypeRepository).populateStockTypeUniqueIds(anySet());

        doReturn(sqLiteDatabase).when(stockTypeRepository).getReadableDatabase();

        doReturn(sqLiteDatabase).when(stockTypeRepository).getWritableDatabase();

        stockTypeRepository.batchInsertStockTypes(Collections.singletonList(stockType));

        ArgumentCaptor<ContentValues> contentValuesArgumentCaptor = ArgumentCaptor.forClass(ContentValues.class);

        verify(sqLiteDatabase, never()).insert(eq(StockTypeRepository.STOCK_TYPE_TABLE_NAME), isNull(), contentValuesArgumentCaptor.capture());

        verify(sqLiteDatabase).update(eq(StockTypeRepository.STOCK_TYPE_TABLE_NAME),contentValuesArgumentCaptor.capture(), eq(StockTypeRepository.UNIQUE_ID + " = ?"), eq(new String[]{stockType.getUniqueId().toString()}));

        assertNotNull(contentValuesArgumentCaptor.getValue());
    }
}