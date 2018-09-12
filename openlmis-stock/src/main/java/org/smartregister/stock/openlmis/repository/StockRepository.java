package org.smartregister.stock.openlmis.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import net.sqlcipher.database.SQLiteDatabase;

import org.joda.time.LocalDate;
import org.smartregister.repository.BaseRepository;
import org.smartregister.repository.Repository;
import org.smartregister.stock.openlmis.domain.Stock;
import org.smartregister.stock.openlmis.dto.LotDetailsDto;

import java.util.ArrayList;
import java.util.List;

import static org.smartregister.stock.openlmis.repository.openlmis.LotRepository.EXPIRATION_DATE;
import static org.smartregister.stock.openlmis.repository.openlmis.LotRepository.ID;
import static org.smartregister.stock.openlmis.repository.openlmis.LotRepository.LOT_TABLE;
import static org.smartregister.stock.openlmis.repository.openlmis.LotRepository.TRADE_ITEM_ID;
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
 * inherits from StockRepository where STOCK_TYPE_ID stores the tradeItemId
 *
 * @see org.smartregister.stock.repository.StockRepository
 */
public class StockRepository extends BaseRepository {

    private static final String TAG = StockRepository.class.getName();

    public static final String LOT_ID = "lot_id";

    public static final String REASON = "reason";

    private static final String CREATE_STOCK_TABLE = "CREATE TABLE " + stock_TABLE_NAME +
            " (" + ID_COLUMN + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            STOCK_TYPE_ID + " VARCHAR NOT NULL," +
            TRANSACTION_TYPE + " VARCHAR NOT NULL," +
            LOT_ID + " VARCHAR," +
            VALUE + " INTEGER NOT NULL," +
            REASON + " VARCHAR," +
            DATE_CREATED + " DATETIME NOT NULL," +
            TO_FROM + " VARCHAR NOT NULL," +
            SYNC_STATUS + " VARCHAR," +
            DATE_UPDATED + " INTEGER," +
            PROVIDER_ID + " VARCHAR," +
            LOCATION_ID + " VARCHAR," +
            CHILD_LOCATION_ID + " VARCHAR," +
            TEAM_ID + " VARCHAR," +
            TEAM_NAME + " VARCHAR)";

    public static final String[] STOCK_TABLE_COLUMNS = {ID_COLUMN, STOCK_TYPE_ID, TRANSACTION_TYPE, LOT_ID, PROVIDER_ID, VALUE, DATE_CREATED, TO_FROM, SYNC_STATUS, DATE_UPDATED};


    public StockRepository(Repository repository) {
        super(repository);
    }

    public static void createTable(SQLiteDatabase database) {
        database.execSQL(CREATE_STOCK_TABLE);
    }

    public void addOrUpdate(Stock stock) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(STOCK_TYPE_ID, stock.getStockTypeId());
        contentValues.put(TRANSACTION_TYPE, stock.getTransactionType());
        contentValues.put(VALUE, stock.getValue());
        contentValues.put(DATE_CREATED, stock.getDateCreated());
        contentValues.put(TO_FROM, stock.getToFrom());
        contentValues.put(SYNC_STATUS, stock.getSyncStatus());
        contentValues.put(DATE_UPDATED, stock.getUpdatedAt());
        contentValues.put(LOT_ID, stock.getLotId());
        contentValues.put(REASON, stock.getReason());
        contentValues.put(PROVIDER_ID, stock.getProviderid());
        contentValues.put(LOCATION_ID, stock.getLocationId());
        contentValues.put(CHILD_LOCATION_ID, stock.getChildLocationId());
        contentValues.put(TEAM_NAME, stock.getTeam());
        contentValues.put(TEAM_ID, stock.getTeamId());
        if (stock.getId() != null) {
            getWritableDatabase().update(stock_TABLE_NAME, contentValues, ID_COLUMN + "=?", new String[]{stock.getId().toString()});
        } else {
            contentValues.put(ID_COLUMN, stock.getId());
            getWritableDatabase().insert(stock_TABLE_NAME, null, contentValues);
        }
    }

    public List<Stock> findUnSyncedWithLimit(int limit) {
        List<Stock> stocks = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = getReadableDatabase().query(stock_TABLE_NAME, STOCK_TABLE_COLUMNS, SYNC_STATUS + " = ?", new String[]{TYPE_Unsynced}, null, null, null, "" + limit);
            stocks = readAllstocks(cursor);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return stocks;
    }

    private List<Stock> readAllstocks(Cursor cursor) {
        List<Stock> stocks = new ArrayList<>();
        try {
            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    Stock stock = new Stock(cursor.getLong(cursor.getColumnIndex(ID_COLUMN)),
                                            cursor.getString(cursor.getColumnIndex(TRANSACTION_TYPE)),
                                            cursor.getString(cursor.getColumnIndex(PROVIDER_ID)),
                                            cursor.getInt(cursor.getColumnIndex(VALUE)),
                                            cursor.getLong(cursor.getColumnIndex(DATE_CREATED)),
                                            cursor.getString(cursor.getColumnIndex(TO_FROM)),
                                            cursor.getString(cursor.getColumnIndex(SYNC_STATUS)),
                                            cursor.getLong(cursor.getColumnIndex(DATE_UPDATED)),
                                            cursor.getString(cursor.getColumnIndex(STOCK_TYPE_ID))
                                        );
                    stock.setLotId(cursor.getString(cursor.getColumnIndex(LOT_ID)));
                    stocks.add(stock);
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            Log.e(getClass().getCanonicalName(), e.getMessage());
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return stocks;
    }

    public int getTotalStockByTradeItem(String tradeItemId) {
        String query = String.format("SELECT sum(%s) FROM %s WHERE %s=?", VALUE, stock_TABLE_NAME, STOCK_TYPE_ID);
        Cursor cursor = null;
        try {
            cursor = getReadableDatabase().rawQuery(query, new String[]{tradeItemId});
            if (cursor.moveToFirst())
                return cursor.getInt(0);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return 0;
    }


    public List<Stock> getStockByTradeItem(String tradeItemId) {
        String query = String.format("SELECT * FROM %s WHERE %s=? ORDER BY %s desc, %s desc", stock_TABLE_NAME
                , STOCK_TYPE_ID, DATE_CREATED, DATE_UPDATED);
        Cursor cursor = null;
        List<Stock> stockList = new ArrayList<>();
        try {
            cursor = getReadableDatabase().rawQuery(query, new String[]{tradeItemId});
            while (cursor.moveToNext()) {
                stockList.add(createStock(cursor));
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return stockList;
    }


    public List<LotDetailsDto> getNumberOfLotsByTradeItem(String tradeItemId) {
        String query = String.format("SELECT l.%s, min(%s), sum(%s) FROM %s l LEFT JOIN %s s on s.%s=l.%s" +
                        " WHERE %s=? AND %s >= ? GROUP BY l.%s ORDER BY 3 ",
                ID, EXPIRATION_DATE, VALUE, LOT_TABLE, stock_TABLE_NAME, LOT_ID, ID, TRADE_ITEM_ID, EXPIRATION_DATE, ID);
        Cursor cursor = null;
        List<LotDetailsDto> lots = new ArrayList<>();
        try {
            cursor = getReadableDatabase().rawQuery(query, new String[]{tradeItemId,
                    String.valueOf(new LocalDate().toDate().getTime())});
            while (cursor.moveToNext()) {
                lots.add(new LotDetailsDto(cursor.getString(0), cursor.getLong(1), cursor.getInt(2)));
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return lots;
    }

    public int getTotalStockByLot(String lotId) {
        String query = String.format("SELECT sum(%s) FROM %s WHERE %s=?", VALUE, stock_TABLE_NAME, LOT_ID);
        Cursor cursor = null;
        int totalStock = 0;
        try {
            cursor = getReadableDatabase().rawQuery(query, new String[]{lotId});
            if (cursor.moveToFirst()) {
                totalStock = cursor.getInt(0);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return totalStock;
    }

    public List<Stock> findUniqueStock(String stock_type_id, String transaction_type, String providerid, String value, String date_created, String to_from) {
        List<Stock> stocks = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = getReadableDatabase().query(stock_TABLE_NAME, STOCK_TABLE_COLUMNS, STOCK_TYPE_ID + " = ? AND " + TRANSACTION_TYPE + " = ? AND " + PROVIDER_ID + " = ? AND " + VALUE + " = ? AND " + DATE_CREATED + " = ? AND " + TO_FROM + " = ?", new String[]{stock_type_id, transaction_type, providerid, value, date_created, to_from}, null, null, null, null);
            stocks = readAllstocks(cursor);

        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return stocks;
    }

    public void markEventsAsSynced(ArrayList<Stock> stocks) {
        for (int i = 0; i < stocks.size(); i++) {
            Stock stockToAdd = stocks.get(i);
            stockToAdd.setSyncStatus(TYPE_Synced);
            addOrUpdate(stockToAdd);
        }
    }

    private Stock createStock(Cursor cursor) {
        Stock stock = new Stock(cursor.getLong(cursor.getColumnIndex(ID_COLUMN)),
                cursor.getString(cursor.getColumnIndex(TRANSACTION_TYPE)),
                cursor.getString(cursor.getColumnIndex(PROVIDER_ID)),
                cursor.getInt(cursor.getColumnIndex(VALUE)),
                cursor.getLong(cursor.getColumnIndex(DATE_CREATED)),
                cursor.getString(cursor.getColumnIndex(TO_FROM)),
                cursor.getString(cursor.getColumnIndex(SYNC_STATUS)),
                cursor.getLong(cursor.getColumnIndex(DATE_UPDATED)),
                cursor.getString(cursor.getColumnIndex(STOCK_TYPE_ID)));

        stock.setLotId(cursor.getString(cursor.getColumnIndex(LOT_ID)));
        stock.setReason(cursor.getString(cursor.getColumnIndex(REASON)));
        stock.setLocationId(cursor.getString(cursor.getColumnIndex(LOCATION_ID)));
        stock.setChildLocationId(cursor.getString(cursor.getColumnIndex(CHILD_LOCATION_ID)));
        stock.setTeam(cursor.getString(cursor.getColumnIndex(TEAM_NAME)));
        stock.setTeamId(cursor.getString(cursor.getColumnIndex(TEAM_ID)));
        return stock;
    }
}

