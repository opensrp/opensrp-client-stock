package org.smartregister.stock.openlmis.intent;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.smartregister.domain.Response;
import org.smartregister.service.ActionService;
import org.smartregister.service.HTTPAgent;
import org.smartregister.stock.openlmis.OpenLMISLibrary;
import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.domain.openlmis.CommodityType;
import org.smartregister.stock.openlmis.repository.openlmis.CommodityTypeRepository;
import org.smartregister.stock.util.NetworkUtils;

import java.text.MessageFormat;
import java.util.List;

import static org.smartregister.stock.openlmis.util.Utils.BASE_URL;
import static org.smartregister.stock.openlmis.util.Utils.PREV_SYNC_SERVER_VERSION;
import static org.smartregister.stock.openlmis.util.Utils.makeGetRequest;
import static org.smartregister.util.Log.logError;

public class CommodityTypeSyncIntentService extends IntentService implements SyncIntentService {

    private static final String LOT_SYNC_URL = "rest/commodity-types/sync";
    private Context context;
    private HTTPAgent httpAgent;
    private ActionService actionService;

    public CommodityTypeSyncIntentService() {
        super("CommodityTypeSyncIntentService");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        context = getBaseContext();
        actionService = OpenLMISLibrary.getInstance().getContext().actionService();
        httpAgent = OpenLMISLibrary.getInstance().getContext().getHttpAgent();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        if (NetworkUtils.isNetworkAvailable(context)) {
            pullFromServer();
        }
    }

    @Override
    public void pullFromServer() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        long timestamp = preferences.getLong(PREV_SYNC_SERVER_VERSION, 0);
        String timestampStr = String.valueOf(timestamp);
        String uri = MessageFormat.format("{0}/{1}?sync_server_version={2}",
                BASE_URL,
                LOT_SYNC_URL,
                timestampStr
        );
        // TODO: make baseUrl configurable
        while (true) {
            try {
                String jsonPayload = makeGetRequest(uri);
                if (jsonPayload == null) {
                    logError("TradeItemClassifications pull failed.");
                    return;
                }
                // store commodityTypes
                Long highestTimeStamp = 0L;
                List<CommodityType> commodityTypes = new Gson().fromJson(jsonPayload, new TypeToken<List<CommodityType>>(){}.getType());
                CommodityTypeRepository repository = OpenLMISLibrary.getInstance().getCommodityTypeRepository();
                for (CommodityType commodityType : commodityTypes) {
                    repository.addOrUpdate(commodityType);
                    if (commodityType.getServerVersion() > highestTimeStamp) {
                        highestTimeStamp = commodityType.getServerVersion();
                    }
                }
                // save highest server version
                SharedPreferences.Editor editor = preferences.edit();
                editor.putLong(PREV_SYNC_SERVER_VERSION, highestTimeStamp);
                editor.commit();
            } catch (Exception e) {
                logError(e.getMessage());
            }
        }
    }
}
