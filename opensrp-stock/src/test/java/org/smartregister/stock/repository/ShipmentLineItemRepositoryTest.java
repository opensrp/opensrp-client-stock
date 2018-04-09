package org.smartregister.stock.repository;

import net.sqlcipher.Cursor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.robolectric.RobolectricTestRunner;
import org.smartregister.repository.Repository;
import org.smartregister.stock.domain.ShipmentLineItem;

import java.lang.reflect.Method;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Created by Ephraim Kigamba - ekigamba@ona.io on 09/04/2018.
 */
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(RobolectricTestRunner.class)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
public class ShipmentLineItemRepositoryTest {

    @Mock
    private Repository repository;

    @Test
    public void getShipmentLineItemsShouldReturnEmptyListWhenGivenNullCusor() throws Exception {
        ShipmentLineItemRepository shipmentLineItemRepository = new ShipmentLineItemRepository(repository);

        Class targetClass = shipmentLineItemRepository.getClass();
        Method method = targetClass.getDeclaredMethod("getShipmentLineItems", Cursor.class);
        method.setAccessible(true);
        Object returnObject = method.invoke(shipmentLineItemRepository, new Object[]{null});

        assertNotNull(returnObject);
        if (!(returnObject instanceof ArrayList)) {
            fail();
        }
        ArrayList<ShipmentLineItem> actualResult = (ArrayList<ShipmentLineItem>) returnObject;
        assertEquals(0, actualResult.size());
    }

}