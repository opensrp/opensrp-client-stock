package org.smartregister.stock.management.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.smartregister.stock.openlmis.domain.openlmis.Dispensable;
import org.smartregister.stock.openlmis.repository.openlmis.DispensableRepository;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.smartregister.stock.openlmis.util.Utils.DATABASE_NAME;

/************** test naming convention followed *****************
 *
 *   testMethodNameShouldExpectedBehavior[IfStateUnderTest]
 *
 ****************************************************************/

public class DispensableRepositoryTest extends BaseRepositoryTest {

    private DispensableRepository database;

    @Before
    public void setUp() {
        context.deleteDatabase(DATABASE_NAME);
        database = new DispensableRepository(mainRepository);
    }

    @After
    public void tearDown() {
        mainRepository.close();
    }

    @Test
    public void testAddOrUpdateShouldAddNewDispensable() {

        // insert new dispensable
        Dispensable dispensable = new Dispensable(
                "123e4567-e89b-42d3-a456-556642440200",
                "dispensing_unit",
                "size_code",
                "route_of_administration"
        );
        database.addOrUpdate(dispensable);

        List<Dispensable> dispensables = database.findDispensables("123e4567-e89b-42d3-a456-556642440200", "dispensing_unit",
                "size_code",   "route_of_administration");

        assertEquals(dispensables.size(), 1);
    }

    @Test
    public void testAddOrUpdateShouldUpdateExistingDispensable() {

        // insert new Dispensable
        Dispensable dispensable = new Dispensable(
                "123e4567-e89b-42d3-a456-556642440200",
                "dispensing_unit",
                "size_code",
                "route_of_administration"
        );
        database.addOrUpdate(dispensable);

        // update existing Dispensable
        dispensable = new Dispensable(
                "123e4567-e89b-42d3-a456-556642440200",
                "dispensing_unit",
                "size_code",
                "route_of_administration_two"
        );
        database.addOrUpdate(dispensable);

        // make sure old values are removed
        List<Dispensable> dispensables = database.findDispensables("123e4567-e89b-42d3-a456-556642440200", "dispensing_unit",
                "size_code",   "route_of_administration");
        assertEquals(dispensables.size(), 0);

        // make sure new values exist
        dispensables = database.findDispensables("123e4567-e89b-42d3-a456-556642440200", "dispensing_unit",
                "size_code",   "route_of_administration_two");
        assertEquals(dispensables.size(), 1);
    }

    @Test
    public void testFindDispensablesShouldReturnAllMatchingRows() {

        // insert new Dispensables
        Dispensable dispensable = new Dispensable(
                "123e4567-e89b-42d3-a456-556642440100",
                "dispensing_unit",
                "size_code",
                "route_of_administration"
        );
        database.addOrUpdate(dispensable);

        dispensable = new Dispensable(
                "123e4567-e89b-42d3-a456-556642440200",
                "dispensing_unit",
                "size_code",
                "route_of_administration"
        );
        database.addOrUpdate(dispensable);

        // ensure all matching rows are returned
        List<Dispensable> dispensables = database.findDispensables(null, "dispensing_unit",
                "size_code",   "route_of_administration");

        assertEquals(dispensables.size(), 2);
    }

    @Test
    public void testFindDispensablesShouldNotReturnAnyMatchingRows() {

        // insert new Dispensable
        Dispensable dispensable = new Dispensable(
                "123e4567-e89b-42d3-a456-556642440200",
                "dispensing_unit",
                "size_code",
                "route_of_administration"
        );
        database.addOrUpdate(dispensable);

        // update existing Dispensable
        dispensable = new Dispensable(
                "123e4567-e89b-42d3-a456-556642440200",
                "dispensing_unit",
                "size_code",
                "route_of_administration_two"
        );
        database.addOrUpdate(dispensable);

        List<Dispensable> dispensables = database.findDispensables("123e4567-e89b-42d3-a456-556642440200", "dispensing_unit",
                "size_code",   "route_of_administration_three");

        assertEquals(dispensables.size(), 0);
    }
}

