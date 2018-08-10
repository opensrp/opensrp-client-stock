package org.smartregister.stock.management.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.smartregister.stock.openlmis.domain.openlmis.Orderable;
import org.smartregister.stock.openlmis.domain.openlmis.Program;
import org.smartregister.stock.openlmis.domain.openlmis.ProgramOrderable;
import org.smartregister.stock.openlmis.repository.openlmis.ProgramOrderableRepository;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.smartregister.stock.openlmis.util.Utils.DATABASE_NAME;

/************** test naming convention followed *****************
 *
 *   testMethodNameShouldExpectedBehavior[IfStateUnderTest]
 *
 ****************************************************************/

public class ProgramOrderableRepositoryTest extends BaseRepositoryTest {

    private ProgramOrderableRepository database;

    @Before
    public void setUp() {
        context.deleteDatabase(DATABASE_NAME);
        database = new ProgramOrderableRepository(mainRepository);
    }

    @After
    public void tearDown() {
        mainRepository.close();
    }

    @Test
    public void testAddOrUpdateShouldAddNewProgramOrderable() {

        // insert new ProgramOrderable
        ProgramOrderable programOrderable = new ProgramOrderable(
                "123e4567-e89b-42d3-a456-556642440200",
                new Program("123e4567-e89b-42d3-a456-556642440100"),
                new Orderable("123e4567-e89b-42d3-a456-556642440000"),
                10,
                true,
                false,
                4924004320L
        );
        database.addOrUpdate(programOrderable);

        List<ProgramOrderable> programOrderables = database.findProgramOrderables("123e4567-e89b-42d3-a456-556642440200", "123e4567-e89b-42d3-a456-556642440100",
                "123e4567-e89b-42d3-a456-556642440000", "10", "1","0");
        assertEquals(programOrderables.size(), 1);
    }

    @Test
    public void testAddOrUpdateShouldUpdateExistingOrderable() {

        // insert new ProgramOrderable
        ProgramOrderable programOrderable = new ProgramOrderable(
                "123e4567-e89b-42d3-a456-556642440200",
                new Program("123e4567-e89b-42d3-a456-556642440700"),
                new Orderable("123e4567-e89b-42d3-a456-556642440000"),
                10,
                true,
                false,
                4924004320L
        );
        database.addOrUpdate(programOrderable);

        // update existing ProgramOrderable
        programOrderable = new ProgramOrderable(
                "123e4567-e89b-42d3-a456-556642440200",
                new Program("123e4567-e89b-42d3-a456-556642440800"),
                new Orderable("123e4567-e89b-42d3-a456-556642440000"),
                10,
                true,
                false,
                4924004320L
        );
        database.addOrUpdate(programOrderable);

        // make sure old values are removed
        List<ProgramOrderable> programOrderables = database.findProgramOrderables("123e4567-e89b-42d3-a456-556642440200", "123e4567-e89b-42d3-a456-556642440700",
                "123e4567-e89b-42d3-a456-556642440000", "10", "1","0");
        assertEquals(programOrderables.size(), 0);

        // make sure new values exist
        programOrderables = database.findProgramOrderables("123e4567-e89b-42d3-a456-556642440200", "123e4567-e89b-42d3-a456-556642440800",
                "123e4567-e89b-42d3-a456-556642440000", "10", "1","0");
        assertEquals(programOrderables.size(), 1);
    }

    @Test
    public void testFindOrderablesShouldReturnAllMatchingRows() {

        // insert new ProgramOrderables
        ProgramOrderable programOrderable = new ProgramOrderable(
                "123e4567-e89b-42d3-a456-556642440200",
                new Program("123e4567-e89b-42d3-a456-556642440700"),
                new Orderable("123e4567-e89b-42d3-a456-556642440000"),
                10,
                true,
                false,
                4924004320L
        );
        database.addOrUpdate(programOrderable);

        programOrderable = new ProgramOrderable(
                "123e4567-e89b-42d3-a456-556642440300",
                new Program("123e4567-e89b-42d3-a456-556642440700"),
                new Orderable("123e4567-e89b-42d3-a456-556642440000"),
                10,
                true,
                false,
                4924004320L
        );
        database.addOrUpdate(programOrderable);

        // ensure all matching rows are returned
        List<ProgramOrderable> programOrderables = database.findProgramOrderables(null, "123e4567-e89b-42d3-a456-556642440700",
                "123e4567-e89b-42d3-a456-556642440000", "10", "1","0");

        assertEquals(programOrderables.size(), 2);
    }

    @Test
    public void testFindOrderablesShouldNotReturnAnyMatchingRows() {

        // insert new ProgramOrderables
        ProgramOrderable programOrderable = new ProgramOrderable(
                "123e4567-e89b-42d3-a456-556642440200",
                new Program("123e4567-e89b-42d3-a456-556642440700"),
                new Orderable("123e4567-e89b-42d3-a456-556642440000"),
                10,
                true,
                false,
                4924004320L
        );
        database.addOrUpdate(programOrderable);

        programOrderable = new ProgramOrderable(
                "123e4567-e89b-42d3-a456-556642440300",
                new Program("123e4567-e89b-42d3-a456-556642440700"),
                new Orderable("123e4567-e89b-42d3-a456-556642440000"),
                10,
                true,
                false,
                4924004320L
        );
        database.addOrUpdate(programOrderable);

        // fetch ProgramOrderable with non-existing Program ID
        List<ProgramOrderable> programOrderables = database.findProgramOrderables(null, "123e4567-e89b-42d3-a456-556642440800",
                "123e4567-e89b-42d3-a456-556642440000", "10", "1","0");

        assertEquals(programOrderables.size(), 0);
    }
}
