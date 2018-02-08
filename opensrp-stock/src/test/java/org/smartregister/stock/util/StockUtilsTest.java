package org.smartregister.stock.util;

import org.junit.Test;
import org.smartregister.stock.BaseUnitTest;

import static junit.framework.Assert.assertEquals;

/**
 * Created by ndegwamartin on 05/02/2018.
 */

public class StockUtilsTest extends BaseUnitTest {
    @Test
    public void testingClassMethodReturnsCorrectValue() {
        assertEquals(4, StockUtils.sampleAdditionMethod(1, 3));
    }
}
