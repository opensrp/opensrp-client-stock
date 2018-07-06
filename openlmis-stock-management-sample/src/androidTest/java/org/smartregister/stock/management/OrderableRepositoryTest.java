package org.smartregister.stock.management;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.smartregister.repository.Repository;
import org.smartregister.stock.management.application.Application;
import org.smartregister.stock.management.domain.Code;
import org.smartregister.stock.management.domain.Dispensable;
import org.smartregister.stock.management.domain.Orderable;
import org.smartregister.stock.management.repository.OrderableRepository;

import java.util.List;
import java.util.UUID;

import static junit.framework.Assert.assertEquals;
import static org.smartregister.stock.management.util.Utils.DATABASE_NAME;

/************** test naming convention followed *****************
 *
 *   testMethodNameShouldExpectedBehavior[IfStateUnderTest]
 *
 ****************************************************************/

public class OrderableRepositoryTest extends BaseRepositoryTest {

    private static Context context;
    private static Repository mainRepository;
    private OrderableRepository database;

    @BeforeClass
    public static void bootStrap() {
        context = InstrumentationRegistry.getTargetContext();
        Application.setAppContext(context);
        mainRepository = Application.getInstance().getRepository();
    }

    @Before
    public void setUp() {
        context.deleteDatabase(DATABASE_NAME);
        database = new OrderableRepository(mainRepository);
    }

    @After
    public void tearDown() {
        mainRepository.close();
    }

    @Test
    public void testAddOrUpdateShouldAddNewOrderable() {

        // insert new orderable
        Orderable orderable = new Orderable(
                UUID.fromString("123e4567-e89b-42d3-a456-556642440200"),
                new Code("orderable_code"),
                "full_product_code",
                10,
                1,
                false,
                new Dispensable(UUID.fromString("123e4567-e89b-42d3-a456-556642590067")),
                "trade_item_id",
                "commodity_type_id"
        );
        database.addOrUpdate(orderable);

        List<Orderable> orderables = database.findOrderables("123e4567-e89b-42d3-a456-556642440200", "orderable_code",
                "full_product_code", "10", "123e4567-e89b-42d3-a456-556642590067","trade_item_id", "commodity_type_id");
        assertEquals(orderables.size(), 1);
    }

    @Test
    public void testAddOrUpdateShouldUpdateExistingOrderable() {

        // add orderable
        Orderable orderable = new Orderable(
                UUID.fromString("123e4567-e89b-42d3-a456-556642440200"),
                new Code("orderable_code"),
                "full_product_code",
                10,
                1,
                false,
                new Dispensable(UUID.fromString("123e4567-e89b-24d3-a456-556642590067")),
                "trade_item_id",
                "commodity_type_id"
        );
        database.addOrUpdate(orderable);

        // update existing orderable
       orderable = new Orderable(
                UUID.fromString("123e4567-e89b-42d3-a456-556642440200"),
                new Code("orderable_code"),
                "full_product_code",
                10,
                1,
                false,
                new Dispensable(UUID.fromString("123e4567-e89b-24d3-a456-556642590067")),
                "trade_item_id",
                "commodity_type_id_two"
        );
        database.addOrUpdate(orderable);

        // make sure old values are removed
        List<Orderable> orderables = database.findOrderables("123e4567-e89b-42d3-a456-556642440200", "orderable_code",
                "full_product_code", "10", "123e4567-e89b-24d3-a456-556642590067","trade_item_id", "commodity_type_id");
        assertEquals(orderables.size(), 0);

        // make sure new values exist
        orderables = database.findOrderables("123e4567-e89b-42d3-a456-556642440200", "orderable_code",
                "full_product_code", "10", "123e4567-e89b-24d3-a456-556642590067","trade_item_id", "commodity_type_id_two");

        assertEquals(orderables.size(), 1);
    }

    @Test
    public void testFindOrderablesShouldReturnAllMatchingRows() {

        // insert new orderables
        Orderable orderable = new Orderable(
                UUID.fromString("123e4567-e89b-42d3-a456-556642440100"),
                new Code("orderable_code"),
                "full_product_code",
                10,
                1,
                false,
                new Dispensable(UUID.fromString("123e4567-e89b-42d3-a456-556642590067")),
                "trade_item_id",
                "commodity_type_id"
        );
        database.addOrUpdate(orderable);

        orderable = new Orderable(
                UUID.fromString("123e4567-e89b-42d3-a456-556642440200"),
                new Code("orderable_code"),
                "full_product_code",
                10,
                1,
                false,
                new Dispensable(UUID.fromString("123e4567-e89b-42d3-a456-556642590067")),
                "trade_item_id",
                "commodity_type_id"
        );
        database.addOrUpdate(orderable);

        List<Orderable> orderables = database.findOrderables(null, "orderable_code",
                "full_product_code", "10", "123e4567-e89b-42d3-a456-556642590067","trade_item_id", "commodity_type_id");

        assertEquals(orderables.size(), 2);
    }

    @Test
    public void testFindOrderablesShouldNotReturnAnyMatchingRows() {

        // insert Orderables
        Orderable orderable = new Orderable(
                UUID.fromString("123e4567-e89b-42d3-a456-556642440100"),
                new Code("orderable_code"),
                "full_product_code",
                10,
                1,
                false,
                new Dispensable(UUID.fromString("123e4567-e89b-42d3-a456-556642590067")),
                "trade_item_id",
                "commodity_type_id"
        );
        database.addOrUpdate(orderable);

        orderable = new Orderable(
                UUID.fromString("123e4567-e89b-42d3-a456-556642440200"),
                new Code("orderable_code"),
                "full_product_code",
                10,
                1,
                false,
                new Dispensable(UUID.fromString("123e4567-e89b-42d3-a456-556642590067")),
                "trade_item_id",
                "commodity_type_id"
        );
        database.addOrUpdate(orderable);

        // fetch orderable with non-existing dispensable ID
        List<Orderable> orderables = database.findOrderables(null, "orderable_code",
                "full_product_code", "10", "123e4567-e89b-42d3-a456-556642590068","trade_item_id", "commodity_type_id");

        assertEquals(orderables.size(), 0);
    }
}
