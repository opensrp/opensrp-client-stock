package org.smartregister.stock.management.util;

public class Utils {

    public static final String INSERT_OR_REPLACE = "INSERT OR REPLACE INTO %s VALUES ";

    public static Boolean convertIntToBoolean(int i) {
        return i > 0;
    }
}
