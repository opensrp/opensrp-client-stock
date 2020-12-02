package org.smartregister.stock.util;

import com.google.gson.Gson;

public class GsonUtil {
    public Gson gson;

    public static Gson getGson() {
        return new Gson();
    }
}
