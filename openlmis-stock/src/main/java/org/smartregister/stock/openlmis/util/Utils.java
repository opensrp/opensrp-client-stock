package org.smartregister.stock.openlmis.util;

import android.util.Base64;
import android.util.Pair;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import static org.smartregister.util.Log.logError;
import static org.smartregister.util.Log.logInfo;

public class Utils {

    public static final String INSERT_OR_REPLACE = "INSERT OR REPLACE INTO %s VALUES ";
    public static final String DATABASE_NAME = "drishti.db";
    private static final String USERNAME = "admin";
    private static  final String PASSWORD = "Admin123";
    public static final String BASE_URL = "https://vreach-dev.smartregister.org/opensrp";
    public static final String PREV_SYNC_SERVER_VERSION = "prev_sync_server_version";

    public static Boolean convertIntToBoolean(int i) {
        return i > 0;
    }

    public static int convertBooleanToInt(Boolean isTrue) {
        if (isTrue) {
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
        return System.nanoTime();
    }

    public static String makeGetRequest(String uri) {

        StringBuffer response = new StringBuffer();
        try {
            createAllTrustingCertValidator(); // TODO: REMOVE THIS!!!!!!!!!!!!!!!!!!!

            HttpURLConnection connection = (HttpURLConnection) new URL(uri).openConnection();
            String encoded = Base64.encodeToString((USERNAME + ":" + PASSWORD).getBytes(), Base64.NO_WRAP);
            connection.setRequestProperty("Authorization", "Basic " + encoded);

            int responseCode = connection.getResponseCode();
            logInfo("\nSending 'GET' request to URL : " + uri);
            logInfo("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            if (responseCode > 299 && responseCode < 600) {
                return null;
            }
        } catch (Exception e) {
            logError(e.getMessage());
            return null;
        }
        return response.toString();
    }

    private static void createAllTrustingCertValidator() throws Exception {

        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }
        };

        // Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }
}
