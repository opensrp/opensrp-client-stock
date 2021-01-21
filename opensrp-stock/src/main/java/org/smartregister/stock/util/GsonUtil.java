package org.smartregister.stock.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;
import org.smartregister.utils.TaskDateTimeTypeConverter;

public class GsonUtil {

    public static Gson getGson() {
        return new Gson();
    }

    public static Gson getGsonWithTimeTypeConverter() {
        return new GsonBuilder().registerTypeAdapter(DateTime.class, new TaskDateTimeTypeConverter())
                .serializeNulls().create();
    }
}
