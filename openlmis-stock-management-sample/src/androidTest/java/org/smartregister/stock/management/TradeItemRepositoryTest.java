package org.smartregister.stock.management;

import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.smartregister.stock.management.domain.Gtin;
import org.smartregister.stock.management.domain.TradeItem;
import org.smartregister.stock.management.repository.TradeItemRepository;

import java.util.List;
import java.util.UUID;

import static junit.framework.Assert.assertEquals;
import static org.smartregister.stock.management.util.Utils.DATABASE_NAME;

public class TradeItemRepositoryTest extends BaseRepositoryTest {

    private TradeItemRepository database;

    @Before
    public void setUp() {
        context.deleteDatabase(DATABASE_NAME);
        database = new TradeItemRepository(mainRepository);
    }

    @After
    public void tearDown() {
        mainRepository.close();
    }

    @Test
    public void testAddOrUpdateShouldAddNewTradeItem() {

        // insert new tradeItem
        TradeItem tradeItem = null;
        try {
             tradeItem = new TradeItem(
                    UUID.fromString("123e4567-e89b-42d3-a456-556642440200"),
                    new Gtin("49312943891"),
                    "manufacturer",
                    98432143091L
            );
        } catch (Exception e) {
            final String TAG = getClass().getName();
            Log.e(TAG, Log.getStackTraceString(e));
        }
        database.addOrUpdate(tradeItem);

        List<TradeItem> tradeItems = database.findTradeItems("123e4567-e89b-42d3-a456-556642440200", "49312943891",
                "manufacturer");

        assertEquals(tradeItems.size(), 1);
    }

    @Test
    public void testAddOrUpdateShouldUpdateExistingTradeItem() {

        // insert TradeItem
        TradeItem tradeItem = null;
        try {
            tradeItem = new TradeItem(
                    UUID.fromString("123e4567-e89b-42d3-a456-556642440200"),
                    new Gtin("49312943891"),
                    "manufacturer",
                    98432143091L
            );
        } catch (Exception e) {
            final String TAG = getClass().getName();
            Log.e(TAG, Log.getStackTraceString(e));
        }
        database.addOrUpdate(tradeItem);

        // update TradeItem
        tradeItem = null;
        try {
            tradeItem = new TradeItem(
                    UUID.fromString("123e4567-e89b-42d3-a456-556642440200"),
                    new Gtin("49312943892"),
                    "manufacturer",
                    98432143091L
            );
        } catch (Exception e) {
            final String TAG = getClass().getName();
            Log.e(TAG, Log.getStackTraceString(e));
        }
        database.addOrUpdate(tradeItem);

        // make sure old values are removed
        List<TradeItem> tradeItems = database.findTradeItems("123e4567-e89b-42d3-a456-556642440200", "49312943891",
                "manufacturer");
        assertEquals(tradeItems.size(), 0);

        // make sure new values exist
        tradeItems = database.findTradeItems("123e4567-e89b-42d3-a456-556642440200", "49312943892",
                "manufacturer");
        assertEquals(tradeItems.size(), 1);
    }

    @Test
    public void testFindTradeItemsShouldReturnAllMatchingRows() {

        // insert new tradeItems
        TradeItem tradeItem = null;
        try {
            tradeItem = new TradeItem(
                    UUID.fromString("123e4567-e89b-42d3-a456-556642440200"),
                    new Gtin("49312943891"),
                    "manufacturer",
                    98432143091L
            );
        } catch (Exception e) {
            final String TAG = getClass().getName();
            Log.e(TAG, Log.getStackTraceString(e));
        }
        database.addOrUpdate(tradeItem);
        
        try {
            tradeItem = new TradeItem(
                    UUID.fromString("123e4567-e89b-42d3-a456-556642440201"),
                    new Gtin("49312943891"),
                    "manufacturer",
                    98432143091L
            );
        } catch (Exception e) {
            final String TAG = getClass().getName();
            Log.e(TAG, Log.getStackTraceString(e));
        }
        database.addOrUpdate(tradeItem);

        List<TradeItem> tradeItems = tradeItems = database.findTradeItems(null, "49312943891",
                "manufacturer");

        assertEquals(tradeItems.size(), 2);
    }

    @Test
    public void testFindTradeItemsShouldNotReturnAnyMatchingRows() {

        // insert TradeItems
        TradeItem tradeItem = null;
        try {
            tradeItem = new TradeItem(
                    UUID.fromString("123e4567-e89b-42d3-a456-556642440200"),
                    new Gtin("49312943891"),
                    "manufacturer",
                    98432143091L
            );
        } catch (Exception e) {
            final String TAG = getClass().getName();
            Log.e(TAG, Log.getStackTraceString(e));
        }
        database.addOrUpdate(tradeItem);

        try {
            tradeItem = new TradeItem(
                    UUID.fromString("123e4567-e89b-42d3-a456-556642440201"),
                    new Gtin("49312943891"),
                    "manufacturer",
                    98432143091L
            );
        } catch (Exception e) {
            final String TAG = getClass().getName();
            Log.e(TAG, Log.getStackTraceString(e));
        }
        database.addOrUpdate(tradeItem);

        // fetch tradeItem with non-existing dispensable ID
        List<TradeItem> tradeItems = tradeItems = database.findTradeItems(null, "49312943891",
                "manufacturer_none");

        assertEquals(tradeItems.size(), 0);
    }
}
