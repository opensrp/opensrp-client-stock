package org.smartregister.stock.util;

import android.content.Context;
import android.util.Log;

import net.sqlcipher.database.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.stock.domain.StockType;
import org.smartregister.stock.repository.StockTypeRepository;
import org.smartregister.util.Utils;

import static org.smartregister.stock.repository.StockTypeRepository.NAME;
import static org.smartregister.stock.repository.StockTypeRepository.OPENMRS_DATE_CONCEPT_ID;
import static org.smartregister.stock.repository.StockTypeRepository.OPENMRS_PARENT_ENTITIY_ID;
import static org.smartregister.stock.repository.StockTypeRepository.OPENMRS_QUANTITY_CONCEPT_ID;
import static org.smartregister.stock.repository.StockTypeRepository.QUANTITY;
import static org.smartregister.stock.util.Constants.VACCINES_JSON_FILE;

/**
 * Created by ndegwamartin on 05/02/2018.
 */

public class StockUtils {
    private static String TAG = StockUtils.class.getName();

    public static void populateStockTypesFromAssets(Context context, StockTypeRepository stockTypeRepository, SQLiteDatabase db) {
        if (stockTypeRepository.getAllStockTypes(db).size() < 1) {
            String stockTypeJson = Utils.readAssetContents(context, Constants.STOCK_TYPE_JSON_FILE);
            try {
                JSONArray stockTypeArray = new JSONArray(stockTypeJson);
                for (int i = 0; i < stockTypeArray.length(); i++) {
                    JSONObject stockType = stockTypeArray.getJSONObject(i);
                    StockType vtObject = new StockType(null, stockType.getInt(QUANTITY), stockType.getString(NAME), stockType.getString(OPENMRS_PARENT_ENTITIY_ID), stockType.getString(OPENMRS_DATE_CONCEPT_ID), stockType.getString(OPENMRS_QUANTITY_CONCEPT_ID));
                    stockTypeRepository.add(vtObject, db);
                }
            } catch (JSONException e) {
                Log.e(TAG, "populateStockTypes: ", e);
            }
        }
    }

    public static String getSupportedVaccines(Context context) {
        return Utils.readAssetContents(context, VACCINES_JSON_FILE);
    }

    //Testing
    public static int sampleAdditionMethod(int a, int b) {
        return a + b;
    }
}
