package org.smartregister.stock;

import org.junit.Test;
import org.robolectric.Robolectric;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

/**
 * Created by ndegwamartin on 05/02/2018.
 */

public class StockLibraryTest extends BaseUnitTest {

    @Test
    public void callingGetInstanceOfStockLibraryDoesNotReturnNull() {
        StockLibrary.init(Robolectric.application);
        StockLibrary library = StockLibrary.getInstance();
        assertNotNull(library);

    }

    @Test(expected = IllegalStateException.class)
    public void callingGetInstanceOfStockLibraryWithoutInitFirstThrowsIllegalStateException() {
        StockLibrary library = StockLibrary.getInstance();
        assertNull(library);

    }
}
