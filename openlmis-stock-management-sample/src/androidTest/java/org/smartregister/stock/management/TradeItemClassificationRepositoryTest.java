package org.smartregister.stock.management;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.smartregister.repository.Repository;
import org.smartregister.stock.management.application.Application;
import org.smartregister.stock.management.domain.TradeItem;
import org.smartregister.stock.management.domain.TradeItemClassification;
import org.smartregister.stock.management.repository.TradeItemClassificationRepository;

import java.util.List;
import java.util.UUID;

import static junit.framework.Assert.assertEquals;
import static org.smartregister.stock.management.util.Utils.DATABASE_NAME;

/************** test naming convention followed *****************
 *
 *   testMethodNameShouldExpectedBehaviorIfStateUnderTest
 *
 ****************************************************************/

public class TradeItemClassificationRepositoryTest extends BaseRepositoryTest {

    private static Context context;
    private static Repository mainRepository;
    private TradeItemClassificationRepository database;

    @BeforeClass
    public static void bootStrap() {
        context = InstrumentationRegistry.getTargetContext();
        Application.setAppContext(context);
        mainRepository = Application.getInstance().getRepository();
    }

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

        // insert new tradeItemClassification
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

        // update existing tradeItemClassification
        TradeItemClassification tradeItemClassification = new TradeItemClassification(
                UUID.fromString("123e4567-e89b-42d3-a456-556642440200"),
                new TradeItem(UUID.fromString("123e4567-e89b-42d3-a456-556642440100")),
                "classification_system",
                "classification_id",
                398143982349L
        );
        database.addOrUpdate(tradeItemClassification);

        // make sure old values are removed
        List<TradeItemClassification> tradeItemClassifications = database.findTradeItemClassifications("123e4567-e89b-42d3-a456-556642440200", "123e4567-e89b-42d3-a456-556642440300",
                "classification_system", "classification_id");
        assertEquals(tradeItemClassifications.size(), 0);

        // make sure new values exist
        tradeItemClassifications = database.findTradeItemClassifications("123e4567-e89b-42d3-a456-556642440200", "123e4567-e89b-42d3-a456-556642440100",
                "classification_system", "classification_id");

        assertEquals(tradeItemClassifications.size(), 1);
    }

    @Test
    public void testFindTradeItemClassificationsShouldReturnAllMatchingRows() {

        // insert new tradeItemClassifications
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

        // make sure new values exist
        List<TradeItemClassification> tradeItemClassifications = database.findTradeItemClassifications(null, "123e4567-e89b-42d3-a456-556642440100",
                "classification_system", "classification_id");
        assertEquals(tradeItemClassifications.size(), 2);
    }

    @Test
    public void testFindTradeItemClassificationsShouldNotReturnAnyMatchingRows() {

        // insert new tradeItemClassifications
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

        // fetch tradeItemClassification with non-existing classification system
        List<TradeItemClassification> tradeItemClassifications = database.findTradeItemClassifications(null, "123e4567-e89b-42d3-a456-556642440100",
                "classification_system_none", "classification_id");

        assertEquals(tradeItemClassifications.size(), 0);
    }
}
