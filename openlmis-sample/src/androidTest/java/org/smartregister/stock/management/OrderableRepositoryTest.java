package org.smartregister.stock.management;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.smartregister.stock.openlmis.domain.Code;
import org.smartregister.stock.openlmis.domain.Dispensable;
import org.smartregister.stock.openlmis.domain.Orderable;
import org.smartregister.stock.openlmis.repository.OrderableRepository;

import java.util.List;
import java.util.UUID;

import static junit.framework.Assert.assertEquals;
import static org.smartregister.stock.openlmis.util.Utils.DATABASE_NAME;

/************** test naming convention followed *****************
 *
 *   testMethodNameShouldExpectedBehavior[IfStateUnderTest]
 *
 ****************************************************************/

public class OrderableRepositoryTest extends BaseRepositoryTest {

    private OrderableRepository database;

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

        // insert new Orderable
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

        // update existing Orderable
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

        // insert new Orderables
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

        // ensure all matching rows are returned
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
