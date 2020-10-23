package org.smartregister.stock;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.smartregister.Context;
import org.smartregister.repository.Repository;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

/**
 * Created by ndegwamartin on 05/02/2018.
 */

public class StockLibraryTest extends BaseUnitTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private Repository repository;

    private Context context = Context.getInstance();

    @Test
    public void callingGetInstanceOfStockLibraryDoesNotReturnNull() {
        StockLibrary.init(context, repository);
        StockLibrary library = StockLibrary.getInstance();
        assertNotNull(library);
    }

    @Test(expected = IllegalStateException.class)
    public void callingGetInstanceOfStockLibraryWithoutInitFirstThrowsIllegalStateException() {
        StockLibrary library = StockLibrary.getInstance();
        assertNull(library);
    }
}
