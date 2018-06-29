package org.smartregister.stock.management.repository;

import org.smartregister.repository.BaseRepository;
import org.smartregister.repository.Repository;

public class TradeItemRepository extends BaseRepository {

    public static final String TAG = TradeItemRepository.class.getName();
    public static final String TRADE_ITEM_TABLE = "trade_items";
    public static final String ID = "id";
    public static final String GTIN = "gtin";
    public static final String MANUFACTURER_OF_TRADE_ITEM = "manufacturer_of_trade_item";
    public static final String CLASSIFICATIONS = "classifications";

    public static final String CREATE_TRADE_ITEM_TABLE =

            "CREATE TABLE " + TRADE_ITEM_TABLE
            + "("
                    + ID + " VARCHAR NOT NULL,"
                    + GTIN + " VARCHAR NOT NULL,"
                    + MANUFACTURER_OF_TRADE_ITEM + " VARCHAR NOT NULL"
            + ")";

    public TradeItemRepository(Repository repository) { super(repository); }
}
