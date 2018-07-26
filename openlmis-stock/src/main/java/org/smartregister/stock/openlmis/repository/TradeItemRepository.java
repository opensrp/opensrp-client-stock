package org.smartregister.stock.openlmis.repository;

import net.sqlcipher.database.SQLiteDatabase;

import org.smartregister.repository.BaseRepository;
import org.smartregister.repository.Repository;
import org.smartregister.stock.openlmis.domain.TradeItem;

import java.util.List;

/**
 * Created by samuelgithengi on 26/7/18.
 */
public class TradeItemRepository extends BaseRepository {

    private static final String TRADE_ITEM_TABLE = "trade_item_register";

    private static final String ID = "_id";

    private static final String COMMODITY_TYPE_ID = "commodity_type_id";

    private static final String NAME = "name";

    private static final String DATE_UPDATED = "date_updated";

    private static final String NET_CONTENT = "net_content";

    private static final String DISPENSING_UNIT = "dispensing_unit";

    private static final String DISPENSING_SIZE = "dispensing_size";

    private static final String DISPENSING_ADMINISTRATION = "dispensing_administration";

    private static final String CREATE_TADE_ITEM_TABLE = "CREATE TABLE " + TRADE_ITEM_TABLE +
            "(" + ID + " VARCHAR NOT NULL PRIMARY KEY," +
            COMMODITY_TYPE_ID + " VARCHAR ," +
            NAME + " VARCHAR, " +
            DATE_UPDATED + " INTEGER, " +
            NET_CONTENT + " INTEGER, " +
            DISPENSING_UNIT + " VARCHAR, " +
            DISPENSING_SIZE + " VARCHAR, " +
            DISPENSING_ADMINISTRATION + " VARCHAR)";

    public TradeItemRepository(Repository repository) {
        super(repository);
    }

    public static void createTable(SQLiteDatabase database) {
        database.execSQL(CREATE_TADE_ITEM_TABLE);
    }

    public void addOrUpdate(TradeItem tradeItem) {

    }

    public List<TradeItem> getByCommodityType(String commodityTypeId) {
        return null;
    }
}
