package org.smartregister.stock.management.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.smartregister.stock.openlmis.domain.openlmis.ValidSourceDestination;
import org.smartregister.stock.openlmis.repository.openlmis.ValidSourceDestinationRepository;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.smartregister.stock.openlmis.util.Utils.DATABASE_NAME;

public class ValidSourceDestinationRepositoryTest extends BaseRepositoryTest {

    private ValidSourceDestinationRepository database;

    @Before
    public void setUp() {
        context.deleteDatabase(DATABASE_NAME);
        database = new ValidSourceDestinationRepository(mainRepository);
    }

    @After
    public void tearDown() {
        mainRepository.close();
    }

    @Test
    public void testAddOrUpdateShouldAddNewValidSourceDestination() {

        // insert new ValidSourceDestination
        ValidSourceDestination validSourceDestination = new ValidSourceDestination(
                1,
                "program_uuid",
                "facility_type_uuid",
                "facility_name",
                "openlmis_uuid",
                true
        );

        database.addOrUpdate(validSourceDestination);

        validSourceDestination = new ValidSourceDestination(
                2,
                "program_uuid",
                "facility_type_uuid",
                "facility_name_1",
                "openlmis_uuid_1",
                false
        );
        database.addOrUpdate(validSourceDestination);

        List<ValidSourceDestination> validSourceDestinations = database.findValidSourceDestinations(null, "program_uuid",
                "facility_type_uuid", null, null, null);

        assertEquals(validSourceDestinations.size(), 2);
    }

    @Test
    public void testAddOrUpdateShouldUpdateExistingValidSourceDestination() {

        // insert new ValidSourceDestination
        ValidSourceDestination validSourceDestination = new ValidSourceDestination(
                1,
                "program_uuid",
                "facility_type_uuid",
                "facility_name",
                "openlmis_uuid",
                true
        );
        database.addOrUpdate(validSourceDestination);

        // update existing ValidSourceDestination
        validSourceDestination = new ValidSourceDestination(
                1,
                "program_uuid_1",
                "facility_type_uuid_1",
                "facility_name_1",
                "openlmis_uuid_1",
                false
        );
        database.addOrUpdate(validSourceDestination);

        // make sure old values are removed
        List<ValidSourceDestination> validSourceDestinations = database.findValidSourceDestinations("1", "program_uuid",
                "facility_type_uuid", "facility_name", "openlmis_uuid", "1");
        assertEquals(validSourceDestinations.size(), 0);

        // make sure new values exist
        validSourceDestinations = database.findValidSourceDestinations("1", "program_uuid_1",
                "facility_type_uuid_1", "facility_name_1", "openlmis_uuid_1", "0");
        assertEquals(validSourceDestinations.size(), 1);
    }

    @Test
    public void testFindValidSourceDestinationsShouldReturnAllMatchingRows() {

        // insert new ValidSourceDestinations
        ValidSourceDestination validSourceDestination = new ValidSourceDestination(
                1,
                "program_uuid",
                "facility_type_uuid",
                "facility_name",
                "openlmis_uuid",
                true
        );
        database.addOrUpdate(validSourceDestination);

        validSourceDestination = new ValidSourceDestination(
                2,
                "program_uuid",
                "facility_type_uuid",
                "facility_name_1",
                "openlmis_uuid_1",
                false
        );
        database.addOrUpdate(validSourceDestination);

        // ensure all matching rows are returned
        List<ValidSourceDestination> validSourceDestinations = database.findValidSourceDestinations(null, "program_uuid",
                "facility_type_uuid", null, null, null);

        assertEquals(validSourceDestinations.size(), 2);
    }

    @Test
    public void testFindValidSourceDestinationsShouldNotReturnAnyMatchingRows() {

        // insert new ValidSourceDestinations
        ValidSourceDestination validSourceDestination = new ValidSourceDestination(
                1,
                "program_uuid",
                "facility_type_uuid",
                "facility_name",
                "openlmis_uuid",
                true
        );
        database.addOrUpdate(validSourceDestination);

        validSourceDestination = new ValidSourceDestination(
                2,
                "program_uuid",
                "facility_type_uuid",
                "facility_name_1",
                "openlmis_uuid_1",
                false
        );
        database.addOrUpdate(validSourceDestination);

        // fetch ValidSourceDestination with non-existing validSourceDestination_name
        List<ValidSourceDestination> validSourceDestinations = database.findValidSourceDestinations(null, "program_uuid_2",
                "facility_type_uuid_2", null, null, null);

        assertEquals(validSourceDestinations.size(), 0);
    }

    @Test
    public void testFindAllValidSourceDestinations() {

        assertTrue(database.findAllValidSourceDestinations().isEmpty());

        // insert new ValidSourceDestinations
        ValidSourceDestination validSourceDestination = new ValidSourceDestination(
                1,
                "program_uuid",
                "facility_type_uuid",
                "facility_name",
                "openlmis_uuid",
                true
        );
        database.addOrUpdate(validSourceDestination);

        assertEquals(database.findAllValidSourceDestinations().size(), 1);

        validSourceDestination = new ValidSourceDestination(
                2,
                "program_uuid",
                "facility_type_uuid",
                "facility_name_1",
                "openlmis_uuid_1",
                false
        );
        database.addOrUpdate(validSourceDestination);

        // ensure all matching rows are returned

        assertEquals(database.findAllValidSourceDestinations().size(), 2);
    }
}
