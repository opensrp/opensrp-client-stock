package org.smartregister.stock.openlmis.repository;

import net.sqlcipher.database.SQLiteDatabase;

import org.smartregister.repository.BaseRepository;
import org.smartregister.repository.Repository;
import org.smartregister.stock.openlmis.domain.Stock;

import java.util.List;

import static org.smartregister.stock.repository.StockRepository.CHILD_LOCATION_ID;
import static org.smartregister.stock.repository.StockRepository.DATE_CREATED;
import static org.smartregister.stock.repository.StockRepository.DATE_UPDATED;
import static org.smartregister.stock.repository.StockRepository.ID_COLUMN;
import static org.smartregister.stock.repository.StockRepository.LOCATION_ID;
import static org.smartregister.stock.repository.StockRepository.PROVIDER_ID;
import static org.smartregister.stock.repository.StockRepository.STOCK_TYPE_ID;
import static org.smartregister.stock.repository.StockRepository.SYNC_STATUS;
import static org.smartregister.stock.repository.StockRepository.TEAM_ID;
import static org.smartregister.stock.repository.StockRepository.TEAM_NAME;
import static org.smartregister.stock.repository.StockRepository.TO_FROM;
import static org.smartregister.stock.repository.StockRepository.TRANSACTION_TYPE;
import static org.smartregister.stock.repository.StockRepository.VALUE;
import static org.smartregister.stock.repository.StockRepository.stock_TABLE_NAME;

/**
 * Created by samuelgithengi on 26/7/18.
 */
public class StockRepository extends BaseRepository {

    private static final String LOT_ID = "lot_id";

    private static final String CREATE_STOCK_TABLE = "CREATE TABLE " + stock_TABLE_NAME +
            " (" + ID_COLUMN + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            STOCK_TYPE_ID + " VARCHAR NOT NULL," +
            TRANSACTION_TYPE + " VARCHAR NULL," +
            LOT_ID + " VARCHAR NULL," +
            VALUE + " INTEGER," +
            DATE_CREATED + " DATETIME NOT NULL," +
            TO_FROM + " VARCHAR NULL," +
            SYNC_STATUS + " VARCHAR," +
            DATE_UPDATED + " INTEGER NULL" +
            PROVIDER_ID + " VARCHAR NOT NULL," +
            LOCATION_ID + " VARCHAR NOT NULL," +
            CHILD_LOCATION_ID + " VARCHAR NOT NULL," +
            TEAM_ID + " VARCHAR NOT NULL," +
            TEAM_NAME + " VARCHAR NOT NULL)";

    public StockRepository(Repository repository) {
        super(repository);
    }

    public static void createTable(SQLiteDatabase database) {
        database.execSQL(CREATE_STOCK_TABLE);
    }

    public void addOrUpdate(Stock stock) {

    }

    public int getTotalStockByTradeItem(String tradeItemId) {
        return 0;
    }

    public List<Stock> getStockByTradeItem(String tradeItemId) {
        return null;
    }


}
