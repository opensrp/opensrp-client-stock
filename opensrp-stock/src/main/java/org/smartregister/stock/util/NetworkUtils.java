package org.smartregister.stock.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by samuelgithengi on 2/16/18.
 */

public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getName();

    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();

        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }

        return false;
    }
}
