package org.smartregister.stock.management;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.smartregister.repository.Repository;
import org.smartregister.stock.management.application.Application;
import org.smartregister.stock.management.domain.Dispensable;
import org.smartregister.stock.management.repository.DispensableRepository;

import java.util.List;
import java.util.UUID;

import static junit.framework.Assert.assertEquals;
import static org.smartregister.stock.management.util.Utils.DATABASE_NAME;

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
                UUID.fromString("123e4567-e89b-42d3-a456-556642440200"),
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
                UUID.fromString("123e4567-e89b-42d3-a456-556642440200"),
                "dispensing_unit",
                "size_code",
                "route_of_administration"
        );
        database.addOrUpdate(dispensable);

        // update existing Dispensable
        dispensable = new Dispensable(
                UUID.fromString("123e4567-e89b-42d3-a456-556642440200"),
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

        // insert new Dispensable
        Dispensable dispensable = new Dispensable(
                UUID.fromString("123e4567-e89b-42d3-a456-556642440100"),
                "dispensing_unit",
                "size_code",
                "route_of_administration"
        );
        database.addOrUpdate(dispensable);

        // update existing Dispensable
        dispensable = new Dispensable(
                UUID.fromString("123e4567-e89b-42d3-a456-556642440200"),
                "dispensing_unit",
                "size_code",
                "route_of_administration"
        );
        database.addOrUpdate(dispensable);

        List<Dispensable> dispensables = database.findDispensables(null, "dispensing_unit",
                "size_code",   "route_of_administration");

        assertEquals(dispensables.size(), 2);
    }

    @Test
    public void testFindDispensablesShouldNotReturnAnyMatchingRows() {

        // insert new Dispensable
        Dispensable dispensable = new Dispensable(
                UUID.fromString("123e4567-e89b-42d3-a456-556642440200"),
                "dispensing_unit",
                "size_code",
                "route_of_administration"
        );
        database.addOrUpdate(dispensable);

        // update existing Dispensable
        dispensable = new Dispensable(
                UUID.fromString("123e4567-e89b-42d3-a456-556642440200"),
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

