package org.smartregister.stock.openlmis.mock;

import android.content.Context;

import net.sqlcipher.database.SQLiteDatabase;

import org.smartregister.commonregistry.CommonFtsObject;
import org.smartregister.repository.DrishtiRepository;
import org.smartregister.repository.Repository;
import org.smartregister.stock.openlmis.repository.CommodityTypeRepository;
import org.smartregister.stock.openlmis.repository.DispensableRepository;
import org.smartregister.stock.openlmis.repository.OrderableRepository;
import org.smartregister.stock.openlmis.repository.ProgramOrderableRepository;
import org.smartregister.stock.openlmis.repository.ProgramRepository;
import org.smartregister.stock.openlmis.repository.TradeItemClassificationRepository;
import org.smartregister.stock.openlmis.repository.TradeItemRepository;
import org.smartregister.util.Session;

/**
 * Created by samuelgithengi on 7/11/18.
 */
public class TestRepository extends Repository {

    public TestRepository(Context context, Session session, CommonFtsObject commonFtsObject, DrishtiRepository... repositories) {
        super(context, session, commonFtsObject, repositories);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        OrderableRepository.createTable(database);
        CommodityTypeRepository.createTable(database);
        ProgramRepository.createTable(database);
        TradeItemClassificationRepository.createTable(database);
        ProgramOrderableRepository.createTable(database);
        TradeItemRepository.createTable(database);
        DispensableRepository.createTable(database);
    }
}
