package org.smartregister.stock.management.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.smartregister.stock.openlmis.domain.openlmis.Reason;
import org.smartregister.stock.openlmis.domain.openlmis.StockCardLineItemReason;
import org.smartregister.stock.openlmis.repository.openlmis.ReasonRepository;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.smartregister.stock.openlmis.util.Utils.DATABASE_NAME;

public class ReasonRepositoryTest extends BaseRepositoryTest {

    private ReasonRepository database;

    @Before
    public void setUp() {
        context.deleteDatabase(DATABASE_NAME);
        database = new ReasonRepository(mainRepository);
    }

    @After
    public void tearDown() {
        mainRepository.close();
    }

    @Test
    public void testAddOrUpdateShouldAddNewReason() {

        // insert new reason
        Reason reason = new Reason(
                "123e4567-e89b-42d3-a456-556642440200",
                "program_id",
                "facility_type",
                getStockCardLineItemReason("123e4567-e89b-42d3-a456-556642440200")
        );
        database.addOrUpdate(reason);

        List<Reason> reasons = database.findReasons("123e4567-e89b-42d3-a456-556642440200",  "program_id", null,null);
        assertEquals(reasons.size(), 1);
    }

    @Test
    public void testAddOrUpdateShouldUpdateExistingReason() {

        // insert new Reason
        Reason reason = new Reason(
                "123e4567-e89b-42d3-a456-556642440200",
                "program_id",
                "facility_type",
                getStockCardLineItemReason("123e4567-e89b-42d3-a456-556642440200")
        );
        database.addOrUpdate(reason);

        // update existing Reason
        reason = new Reason(
                "123e4567-e89b-42d3-a456-556642440200",
                "program_id_1",
                "facility_type_1",
                getStockCardLineItemReason("123e4567-e89b-42d3-a456-556642440200")
        );
        reason.getStockCardLineItemReason().setName("name_1");
        database.addOrUpdate(reason);

        // make sure old values are removed
        List<Reason> reasons = database.findReasons("123e4567-e89b-42d3-a456-556642440200", "program_id", null, null);
        assertEquals(reasons.size(), 0);

        // make sure new values exist
        reasons = database.findReasons("123e4567-e89b-42d3-a456-556642440200", "program_id_1", null, null);

        assertEquals(reasons.size(), 1);
    }

    @Test
    public void testFindReasonsShouldReturnAllMatchingRows() {

        // insert new Reasons
        Reason reason = new Reason(
                "123e4567-e89b-42d3-a456-556642440100",
                "program_id",
                "facility_type",
                getStockCardLineItemReason("123e4567-e89b-42d3-a456-556642440100")
        );
        database.addOrUpdate(reason);

        reason = new Reason(
                "123e4567-e89b-42d3-a456-556642440200",
                "program_id_1",
                "facility_type_1",
                getStockCardLineItemReason("123e4567-e89b-42d3-a456-556642440200")
        );
        database.addOrUpdate(reason);

        // ensure all matching rows are returned
        List<Reason> reasons = database.findReasons(null, null, null, null);
        assertEquals(reasons.size(), 2);
    }

    @Test
    public void testFindReasonsShouldNotReturnAnyMatchingRows() {

        // insert Reasons
        Reason reason = new Reason(
                "123e4567-e89b-42d3-a456-556642440100",
                "program_id",
                "facility_type",
                getStockCardLineItemReason("123e4567-e89b-42d3-a456-556642440200")
        );
        database.addOrUpdate(reason);

        reason = new Reason(
                "123e4567-e89b-42d3-a456-556642440200",
                "program_id_1",
                "facility_type_1",
                getStockCardLineItemReason("123e4567-e89b-42d3-a456-556642440200")
        );
        database.addOrUpdate(reason);

        // fetch reason with non-existing ID
        List<Reason> reasons = database.findReasons("123e4567-e89b-42d3-a456-556642440300", "program_id", null, null);

        assertEquals(reasons.size(), 0);
    }

    private StockCardLineItemReason getStockCardLineItemReason(String id) {

        return new StockCardLineItemReason(
                id,
                "name",
                "description",
                "reason_type",
                "reason_category",
                true
        );
    }
}
