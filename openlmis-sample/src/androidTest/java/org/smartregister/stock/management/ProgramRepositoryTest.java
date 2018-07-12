package org.smartregister.stock.management;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.smartregister.stock.openlmis.domain.Code;
import org.smartregister.stock.openlmis.domain.Program;
import org.smartregister.stock.openlmis.repository.ProgramRepository;

import java.util.List;
import java.util.UUID;

import static junit.framework.Assert.assertEquals;
import static org.smartregister.stock.openlmis.util.Utils.DATABASE_NAME;

/************** test naming convention followed *****************
 *
 *   testMethodNameShouldExpectedBehavior[IfStateUnderTest]
 *
 ****************************************************************/

public class ProgramRepositoryTest extends BaseRepositoryTest {

    private ProgramRepository database;

    @Before
    public void setUp() {
        context.deleteDatabase(DATABASE_NAME);
        database = new ProgramRepository(mainRepository);
    }

    @After
    public void tearDown() {
        mainRepository.close();
    }

    @Test
    public void testAddOrUpdateShouldAddNewProgram() {

        // insert new Program
        Program program = new Program(
                UUID.fromString("123e4567-e89b-42d3-a456-556642440200"),
                new Code("program_code"),
                "program_name",
                "program_description",
                true,
                false,
                false,
                false,
                false,
                4918234891L
        );

        database.addOrUpdate(program);

        List<Program> programs = database.findPrograms("123e4567-e89b-42d3-a456-556642440200", "program_code",
                "program_name", "1");

        assertEquals(programs.size(), 1);
    }

    @Test
    public void testAddOrUpdateShouldUpdateExistingProgram() {

        // insert new Program
        Program program = new Program(
                UUID.fromString("123e4567-e89b-42d3-a456-556642440200"),
                new Code("program_code"),
                "program_name_two",
                "program_description",
                true,
                false,
                false,
                false,
                false,
                4918234891L
        );
        database.addOrUpdate(program);

        // update existing Program
        program = new Program(
                UUID.fromString("123e4567-e89b-42d3-a456-556642440100"),
                new Code("program_code"),
                "program_name_two",
                "program_description",
                true,
                false,
                false,
                false,
                false,
                4918234891L
        );
        database.addOrUpdate(program);

        // make sure old values are removed
        List<Program> programs = database.findPrograms("123e4567-e89b-42d3-a456-556642440200", "program_code",
                "program_name", "1");
        assertEquals(programs.size(), 0);

        // make sure new values exist
       programs = database.findPrograms("123e4567-e89b-42d3-a456-556642440100", "program_code",
                "program_name_two", "1");
        assertEquals(programs.size(), 1);
    }

    @Test
    public void testFindProgramsShouldReturnAllMatchingRows() {

        // insert new Programs
        Program program = new Program(
                UUID.fromString("123e4567-e89b-42d3-a456-556642440100"),
                new Code("program_code"),
                "program_name",
                "program_description",
                true,
                false,
                false,
                false,
                false,
                4918234891L
        );
        database.addOrUpdate(program);

        program = new Program(
                UUID.fromString("123e4567-e89b-42d3-a456-556642440200"),
                new Code("program_code"),
                "program_name",
                "program_description",
                true,
                false,
                false,
                false,
                false,
                4918234891L
        );
        database.addOrUpdate(program);

        // ensure all matching rows are returned
        List<Program> programs = database.findPrograms(null, "program_code",
                "program_name", "1");

        assertEquals(programs.size(), 2);
    }
    
    @Test
    public void testFindProgramsShouldNotReturnAnyMatchingRows() {

        // insert new Programs
        Program program = new Program(
                UUID.fromString("123e4567-e89b-42d3-a456-556642440200"),
                new Code("program_code"),
                "program_name",
                "program_description",
                true,
                false,
                false,
                false,
                false,
                4918234891L
        );
        database.addOrUpdate(program);

        program = new Program(
                UUID.fromString("123e4567-e89b-42d3-a456-556642440200"),
                new Code("program_code"),
                "program_name",
                "program_description",
                true,
                false,
                false,
                false,
                false,
                4918234891L
        );
        database.addOrUpdate(program);


        // fetch Program with non-existing program_name
        List<Program> programs = database.findPrograms(null, "program_code",
                "program_name_two", "1");

        assertEquals(programs.size(), 0);
    }
}