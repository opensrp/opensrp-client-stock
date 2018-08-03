package org.smartregister.stock.management;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.smartregister.stock.openlmis.domain.openlmis.TradeItem;
import org.smartregister.stock.openlmis.domain.openlmis.TradeItemClassification;
import org.smartregister.stock.openlmis.repository.openlmis.TradeItemClassificationRepository;

import java.util.List;
import java.util.UUID;

import static junit.framework.Assert.assertEquals;
import static org.smartregister.stock.openlmis.util.Utils.DATABASE_NAME;

/************** test naming convention followed *****************
 *
 *   testMethodNameShouldExpectedBehavior[IfStateUnderTest]
 *
 ****************************************************************/

public class TradeItemClassificationRepositoryTest extends BaseRepositoryTest {

    private TradeItemClassificationRepository database;

    @Before
    public void setUp() {
        context.deleteDatabase(DATABASE_NAME);
        database = new TradeItemClassificationRepository(mainRepository);
    }

    @After
    public void tearDown() {
        mainRepository.close();
    }

    @Test
    public void testAddOrUpdateShouldAddNewTradeItemClassification() {

        // insert new TradeItemClassification
        TradeItemClassification tradeItemClassification = new TradeItemClassification(
                UUID.fromString("123e4567-e89b-42d3-a456-556642440200"),
                new TradeItem(UUID.fromString("123e4567-e89b-42d3-a456-556642440300")),
                "classification_system",
                "classification_id",
                398143982349L
        );
        database.addOrUpdate(tradeItemClassification);

        List<TradeItemClassification> tradeItemClassifications = database.findTradeItemClassifications("123e4567-e89b-42d3-a456-556642440200", "123e4567-e89b-42d3-a456-556642440300",
                "classification_system", "classification_id");

        assertEquals(tradeItemClassifications.size(), 1);
    }

    @Test
    public void testAddOrUpdateShouldUpdateExistingTradeItemClassification() {

        // insert new TradeItemClassification
        TradeItemClassification tradeItemClassification = new TradeItemClassification(
                UUID.fromString("123e4567-e89b-42d3-a456-556642440200"),
                new TradeItem(UUID.fromString("123e4567-e89b-42d3-a456-556642440100")),
                "classification_system",
                "classification_id",
                398143982349L
        );
        database.addOrUpdate(tradeItemClassification);

        // update existing TradeItemClassification
        tradeItemClassification = new TradeItemClassification(
                UUID.fromString("123e4567-e89b-42d3-a456-556642440200"),
                new TradeItem(UUID.fromString("123e4567-e89b-42d3-a456-556642440200")),
                "classification_system",
                "classification_id",
                398143982349L
        );
        database.addOrUpdate(tradeItemClassification);

        // make sure old values are removed
        List<TradeItemClassification> tradeItemClassifications = database.findTradeItemClassifications("123e4567-e89b-42d3-a456-556642440200", "123e4567-e89b-42d3-a456-556642440100",
                "classification_system", "classification_id");
        assertEquals(tradeItemClassifications.size(), 0);

        // make sure new values exist
        tradeItemClassifications = database.findTradeItemClassifications("123e4567-e89b-42d3-a456-556642440200", "123e4567-e89b-42d3-a456-556642440200",
                "classification_system", "classification_id");

        assertEquals(tradeItemClassifications.size(), 1);
    }

    @Test
    public void testFindTradeItemClassificationsShouldReturnAllMatchingRows() {

        // insert new TradeItemClassifications
        TradeItemClassification tradeItemClassification = new TradeItemClassification(
                UUID.fromString("123e4567-e89b-42d3-a456-556642440200"),
                new TradeItem(UUID.fromString("123e4567-e89b-42d3-a456-556642440100")),
                "classification_system",
                "classification_id",
                398143982349L
        );
        database.addOrUpdate(tradeItemClassification);

        tradeItemClassification = new TradeItemClassification(
                UUID.fromString("123e4567-e89b-42d3-a456-556642440000"),
                new TradeItem(UUID.fromString("123e4567-e89b-42d3-a456-556642440100")),
                "classification_system",
                "classification_id",
                398143982349L
        );
        database.addOrUpdate(tradeItemClassification);

        // ensure all matching rows are returned
        List<TradeItemClassification> tradeItemClassifications = database.findTradeItemClassifications(null, "123e4567-e89b-42d3-a456-556642440100",
                "classification_system", "classification_id");
        assertEquals(tradeItemClassifications.size(), 2);
    }

    @Test
    public void testFindTradeItemClassificationsShouldNotReturnAnyMatchingRows() {

        // insert new TradeItemClassifications
        TradeItemClassification tradeItemClassification = new TradeItemClassification(
                UUID.fromString("123e4567-e89b-42d3-a456-556642440200"),
                new TradeItem(UUID.fromString("123e4567-e89b-42d3-a456-556642440100")),
                "classification_system",
                "classification_id",
                398143982349L
        );
        database.addOrUpdate(tradeItemClassification);

        tradeItemClassification = new TradeItemClassification(
                UUID.fromString("123e4567-e89b-42d3-a456-556642440000"),
                new TradeItem(UUID.fromString("123e4567-e89b-42d3-a456-556642440100")),
                "classification_system",
                "classification_id",
                398143982349L
        );
        database.addOrUpdate(tradeItemClassification);

        // fetch TradeItemClassification with non-existing classification system
        List<TradeItemClassification> tradeItemClassifications = database.findTradeItemClassifications(null, "123e4567-e89b-42d3-a456-556642440100",
                "classification_system_none", "classification_id");

        assertEquals(tradeItemClassifications.size(), 0);
    }
}
