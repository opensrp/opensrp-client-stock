package org.smartregister.stock.management;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.smartregister.repository.Repository;
import org.smartregister.stock.management.application.Application;
import org.smartregister.stock.management.domain.CommodityType;
import org.smartregister.stock.management.repository.CommodityTypeRepository;

import java.util.List;
import java.util.UUID;

import static junit.framework.Assert.assertEquals;
import static org.smartregister.stock.management.util.Utils.DATABASE_NAME;

/************** test naming convention followed *****************
 *
 *   testMethodNameShouldExpectedBehaviorIfStateUnderTest
 *
 ****************************************************************/

public class CommodityTypeRepositoryTest extends BaseRepositoryTest {

    private static Context context;
    private static Repository mainRepository;
    private CommodityTypeRepository database;

    @BeforeClass
    public static void bootStrap() {
        context = InstrumentationRegistry.getTargetContext();
        Application.setAppContext(context);
        mainRepository = Application.getInstance().getRepository();
    }

    @Before
    public void setUp() {
        context.deleteDatabase(DATABASE_NAME);
        database = new CommodityTypeRepository(mainRepository);
    }

    @After
    public void tearDown() {
        mainRepository.close();
    }

    @Test
    public void testAddOrUpdateShouldAddNewCommodityType() {

        // insert new CommodityType
        CommodityType commodityType = new CommodityType(
                UUID.fromString("123e4567-e89b-42d3-a456-556642440200"),
                "commodity",
                "123e4567-e89b-42d3-a456-556642440276",
                "classification_system",
                "classification_id",
                421309130103L
        );
        database.addOrUpdate(commodityType);

        List<CommodityType> commodityTypes = database.findCommodityTypes("123e4567-e89b-42d3-a456-556642440200", "commodity",
                "123e4567-e89b-42d3-a456-556642440276", "classification_system", "classification_id");

        assertEquals(commodityTypes.size(), 1);
    }

    @Test
    public void testAddOrUpdateShouldUpdateExistingCommodityType() {

        // update existing CommodityType
        CommodityType commodityType = new CommodityType(
                UUID.fromString("123e4567-e89b-42d3-a456-556642440200"),
                "commodity",
                "123e4567-e89b-42d3-a456-556642440286",
                "classification_system",
                "classification_id",
                421309130103L
        );
        database.addOrUpdate(commodityType);

        // make sure old values are removed
        List<CommodityType> commodityTypes = database.findCommodityTypes("123e4567-e89b-42d3-a456-556642440200", "commodity",
                "123e4567-e89b-42d3-a456-556642440276", "classification_system", "classification_id");
        assertEquals(commodityTypes.size(), 0);

        // make sure new values exist
        commodityTypes = database.findCommodityTypes("123e4567-e89b-42d3-a456-556642440200", "commodity",
                "123e4567-e89b-42d3-a456-556642440286", "classification_system", "classification_id");
        assertEquals(commodityTypes.size(), 1);
    }

    @Test
    public void testFindCommodityTypesShouldReturnAllMatchingRows() {

        // insert CommodityTypes
        CommodityType commodityType = new CommodityType(
                UUID.fromString("123e4567-e89b-42d3-a456-556642440100"),
                "commodity",
                "123e4567-e89b-42d3-a456-556642440276",
                "classification_system",
                "classification_id",
                421309130103L
        );
        database.addOrUpdate(commodityType);

        commodityType = new CommodityType(
                UUID.fromString("123e4567-e89b-42d3-a456-556642440200"),
                "commodity",
                "123e4567-e89b-42d3-a456-556642440276",
                "classification_system",
                "classification_id",
                421309130103L
        );
        database.addOrUpdate(commodityType);

        List<CommodityType> commodityTypes = database.findCommodityTypes(null, "commodity",
                "123e4567-e89b-42d3-a456-556642440276", "classification_system", "classification_id");

        assertEquals(commodityTypes.size(), 2);
    }

    @Test
    public void testFindCommodityTypesShouldNotReturnAnyMatchingRows() {

        // insert commodityTypes
        CommodityType commodityType = new CommodityType(
                UUID.fromString("123e4567-e89b-42d3-a456-556642440100"),
                "commodity",
                "123e4567-e89b-42d3-a456-556642440276",
                "classification_system",
                "classification_id",
                421309130103L
        );
        database.addOrUpdate(commodityType);

        commodityType = new CommodityType(
                UUID.fromString("123e4567-e89b-42d3-a456-556642440200"),
                "commodity",
                "123e4567-e89b-42d3-a456-556642440276",
                "classification_system",
                "classification_id",
                421309130103L
        );
        database.addOrUpdate(commodityType);

        // fetch commodityType with non-existing parent ID
        List<CommodityType> commodityTypes = database.findCommodityTypes(null, "commodity",
                "123e4567-e89b-42d3-a456-556642440286", "classification_system", "classification_id");

        assertEquals(commodityTypes.size(), 0);
    }
}
