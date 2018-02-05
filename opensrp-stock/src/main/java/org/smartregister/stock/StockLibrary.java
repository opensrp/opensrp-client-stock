package org.smartregister.stock;

import android.content.Context;

/**
 * Created by ndegwamartin on 05/02/2018.
 */

public class StockLibrary {
    private static StockLibrary instance;
    private static Context context;

    public static void init(Context context) {
        if (instance == null) {
            instance = new StockLibrary(context);
        }
    }

    public static StockLibrary getInstance() {
        if (instance == null) {
            throw new IllegalStateException(" Instance does not exist!!! Call " + StockLibrary.class.getName() + ".init method in the onCreate method of your Application class ");
        }
        return instance;
    }

    private StockLibrary(Context context) {
        this.context = context;
    }
}
