package org.smartregister.stock.openlmis.repository;

import android.content.Context;
import android.util.Log;

import net.sqlcipher.database.SQLiteDatabase;

import org.smartregister.AllConstants;
import org.smartregister.repository.DrishtiRepository;
import org.smartregister.repository.Repository;
import org.smartregister.repository.SettingsRepository;
import org.smartregister.stock.openlmis.repository.openlmis.CommodityTypeRepository;
import org.smartregister.stock.openlmis.repository.openlmis.DispensableRepository;
import org.smartregister.stock.openlmis.repository.openlmis.LotRepository;
import org.smartregister.stock.openlmis.repository.openlmis.OrderableRepository;
import org.smartregister.stock.openlmis.repository.openlmis.ProgramOrderableRepository;
import org.smartregister.stock.openlmis.repository.openlmis.ProgramRepository;
import org.smartregister.stock.openlmis.repository.openlmis.ReasonRepository;
import org.smartregister.stock.openlmis.repository.openlmis.TradeItemClassificationRepository;
import org.smartregister.stock.openlmis.repository.openlmis.TradeItemRepository;
import org.smartregister.stock.openlmis.repository.openlmis.ValidSourceDestinationRepository;

import java.util.List;

public class StockManagementRepository extends Repository {

    protected SQLiteDatabase readableDatabase;
    protected SQLiteDatabase writableDatabase;
    private String password = "";
    private static final String TAG = StockManagementRepository.class.getName();

    public StockManagementRepository(Context context, org.smartregister.Context openSRPContext) {
        super(context, AllConstants.DATABASE_NAME, AllConstants.DATABASE_VERSION, openSRPContext.session(), null, getSettingsRepository(openSRPContext.sharedRepositories()));
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        super.onCreate(database);
        createTables(database);
    }

    private void createTables(SQLiteDatabase database) {

        OrderableRepository.createTable(database);
        CommodityTypeRepository.createTable(database);
        ProgramRepository.createTable(database);
        TradeItemClassificationRepository.createTable(database);
        ProgramOrderableRepository.createTable(database);
        TradeItemRepository.createTable(database);
        DispensableRepository.createTable(database);
        ReasonRepository.createTable(database);
        LotRepository.createTable(database);
        org.smartregister.stock.openlmis.repository.TradeItemRepository.createTable(database);
        StockRepository.createTable(database);
        SearchRepository.createTable(database);
        ValidSourceDestinationRepository.createTable(database);
        StockTakeRepository.createTable(database);
    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
        return getReadableDatabase(password);
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        return getWritableDatabase(password); // could add password field if you wanted
    }

    @Override
    public synchronized SQLiteDatabase getReadableDatabase(String password) {

        try {
            if (readableDatabase == null || !readableDatabase.isOpen()) {
                if (readableDatabase != null) {
                    readableDatabase.close();
                }
                readableDatabase = super.getReadableDatabase(password);
            }
            return readableDatabase;
        } catch (Exception e) {
            Log.e(TAG, "Database Error. " + e.getMessage());
            return null;
        }

    }

    @Override
    public synchronized SQLiteDatabase getWritableDatabase(String password) {

        if (writableDatabase == null || !writableDatabase.isOpen()) {
            if (writableDatabase != null) {
                writableDatabase.close();
            }
            writableDatabase = super.getWritableDatabase(password);
        }
        return writableDatabase;
    }

    @Override
    public synchronized void close() {

        if (readableDatabase != null) {
            readableDatabase.close();
        }

        if (writableDatabase != null) {
            writableDatabase.close();
        }
        super.close();
    }

    private static SettingsRepository getSettingsRepository(List<DrishtiRepository> repositories) {
        for (DrishtiRepository repository : repositories) {
            if (repository instanceof SettingsRepository) {
                return (SettingsRepository) repository;
            }
        }
        return null;
    }
}
