package org.smartregister.stock.openlmis.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.view.inputmethod.InputMethodManager;

import org.smartregister.domain.Response;
import org.smartregister.repository.AllSettings;
import org.smartregister.service.HTTPAgent;
import org.smartregister.stock.openlmis.OpenLMISLibrary;
import org.smartregister.stock.openlmis.domain.Stock;
import org.smartregister.stock.openlmis.repository.StockRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.SYNC_COMPLETE_INTENT_ACTION;
import static org.smartregister.util.Log.logError;

public class Utils {

    public static final String INSERT_OR_REPLACE = "INSERT OR REPLACE INTO %s VALUES ";
    public static final String DATABASE_NAME = "drishti.db";
    private static final String USERNAME = "admin";
    private static  final String PASSWORD = "Admin123";
    public static final String BASE_URL = "https://vreach-dev.smartregister.org/opensrp";
    // public static final String BASE_URL = "http://192.168.0.10:8080/opensrp";
    // public static final String BASE_URL = "http://10.20.25.188:8080/opensrp";
    private static HTTPAgent httpAgent = OpenLMISLibrary.getInstance().getContext().getHttpAgent();

    public static Boolean convertIntToBoolean(int i) {
        return i > 0;
    }

    public static int convertBooleanToInt(Boolean isTrue) {
        if (isTrue != null && isTrue) {
            return 1;
        }
        return 0;
    }

    /**
     *
     * This method takes an array of {@param columnValues} and returns a {@code Pair} comprising of
     * the query string select statement and the query string arguments array.
     *
     * It assumes that {@param columnValues} is the same size as {@param SELECT_TABLE_COLUMNS} and
     * that select arguments are in the same order as {@param SELECT_TABLE_COLUMNS} column values.
     *
     * @param columnValues
     * @return
     */
    public static Pair<String, String[]> createQuery(String[] columnValues, String[] SELECT_TABLE_COLUMNS) {

        String queryString = "";
        List<String> selectionArgs = new ArrayList<>();
        for (int i = 0; i < columnValues.length; i++) {
            if (columnValues[i] == null) {
                continue;
            }

            if (!"".equals(queryString)) {
                queryString += " AND ";
            }
            queryString += SELECT_TABLE_COLUMNS[i] + "=?";
            selectionArgs.add(columnValues[i]);
        }

        String[] args = new String[selectionArgs.size()];
        args = selectionArgs.toArray(args);

        return new Pair<>(queryString, args);
    }

    public static Long getCurrentTime() {
        return System.currentTimeMillis();
    }

    public static String makeGetRequest(String uri) {

        Response<String> response;
        try {
            response = httpAgent.fetchWithCredentials(uri, USERNAME, PASSWORD);
            if (response.isFailure()) {
                logError("Pull on url: " +  uri  + ", failed.");
                return null;
            }
        } catch (Exception e) {
            logError(e.getMessage());
            return null;
        }
        return response.payload();
    }

    public static boolean makePostRequest(String uri, String payload) {

        AllSettings settings = OpenLMISLibrary.getInstance().getContext().allSettings();
        settings.registerANM(USERNAME, PASSWORD);
        Response<String> response;
        try {
            response = httpAgent.post(uri, payload);
            return response.isFailure();
        } catch (Exception e) {
            logError(e.getMessage());
            return false;
        }
    }

    public static void populateDBWithStock() {
        StockRepository repository = OpenLMISLibrary.getInstance().getStockRepository();

        List<Stock> stocks = new ArrayList<>();
        Stock stock = new Stock(null, "debit", "provider_id", 1, 1L, "to_from", "Unsynced", 1L, "trade_item_id");
        stock.setLotId("lot_id");
        stocks.add(stock);
        stock = new Stock(null, "debit", "provider_id_1", 1, 1L, "to_from", "Unsynced", 1L, "trade_item_id_1");
        stock.setLotId("lot_id_1");
        stocks.add(stock);
        stock = new Stock(null, "debit", "provider_id_2", 1, 1L, "to_from", "Unsynced", 1L, "trade_item_id_2");
        stock.setLotId("lot_id_2");
        stocks.add(stock);

        for (Stock stk : stocks) {
            repository.addOrUpdate(stk);
        }
    }

    public static void sendSyncCompleteBroadCast(Context context) {
        Intent intent = new Intent();
        intent.setAction(SYNC_COMPLETE_INTENT_ACTION);
        intent.putExtra("data","A sync intent service recently completed syncing.");
        context.sendBroadcast(intent);
    }

    public static void hideKeyboard(Activity activityContext) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activityContext.getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activityContext.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            logError("Error encountered while hiding keyboard " + e);
        }
    }

    public static boolean isEmptyCollection(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isEmptyMap(Map map) {
        return map == null || map.isEmpty();
    }
}
