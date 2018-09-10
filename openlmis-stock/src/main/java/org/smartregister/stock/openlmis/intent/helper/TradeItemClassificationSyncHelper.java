package org.smartregister.stock.openlmis.intent.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.smartregister.service.ActionService;
import org.smartregister.service.HTTPAgent;
import org.smartregister.stock.openlmis.OpenLMISLibrary;
import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.domain.openlmis.TradeItemClassification;
import org.smartregister.stock.openlmis.repository.openlmis.TradeItemClassificationRepository;

import java.text.MessageFormat;
import java.util.List;

import static org.smartregister.stock.openlmis.util.Utils.BASE_URL;
import static org.smartregister.stock.openlmis.util.Utils.PREV_SYNC_SERVER_VERSION;
import static org.smartregister.stock.openlmis.util.Utils.makeGetRequest;
import static org.smartregister.util.Log.logError;
import static org.smartregister.util.Log.logInfo;

public class TradeItemClassificationSyncHelper extends BaseSyncHelper {

    private static final String TRADE_ITEM_CLASSIFICATION_SYNC_URL = "rest/trade-item-classifications/sync";
    private ActionService actionService;
    private HTTPAgent httpAgent;
    private TradeItemClassificationRepository repository;

    public TradeItemClassificationSyncHelper(Context context, ActionService actionService, HTTPAgent httpAgent) {
        this.repository = OpenLMISLibrary.getInstance().getTradeItemClassificationRepository();
        this.context = context;
        this.actionService = actionService;
        this.httpAgent = httpAgent;
    }

    protected String pullFromServer() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String baseUrl = OpenLMISLibrary.getInstance().getContext().configuration().dristhiBaseURL();
        if (baseUrl.endsWith(context.getString(R.string.url_separator))) {
            baseUrl = baseUrl.substring(0, baseUrl.lastIndexOf(context.getString(R.string.url_separator)));
        }
        long timestamp = preferences.getLong(PREV_SYNC_SERVER_VERSION, 0);
        String timestampStr = String.valueOf(timestamp);
        String uri = MessageFormat.format("{0}/{1}?sync_server_version={2}",
                BASE_URL,
                TRADE_ITEM_CLASSIFICATION_SYNC_URL,
                timestampStr
        );
        // TODO: make baseUrl configurable
        String jsonPayload = null;
        try {
            jsonPayload = makeGetRequest(uri);
            if (jsonPayload == null) {
                logError("TradeItemClassifications pull failed.");
            }
            logInfo("TradeItemClassifications pulled successfully!");
        } catch (Exception e) {
            logError(e.getMessage());
            return jsonPayload;
        }
        return jsonPayload;
    }

    @Override
    public void saveResponse(String jsonPayload, SharedPreferences preferences) {
        // store tradeItemClassifications
        Long highestTimeStamp = 0L;
        List<TradeItemClassification> tradeItemClassifications = new Gson().fromJson(jsonPayload, new TypeToken<List<TradeItemClassification>>(){}.getType());
        for (TradeItemClassification tradeItemClassification : tradeItemClassifications) {
            repository.addOrUpdate(tradeItemClassification);
            if (tradeItemClassification.getServerVersion() > highestTimeStamp) {
                highestTimeStamp = tradeItemClassification.getServerVersion();
            }
        }
        // save highest server version
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(PREV_SYNC_SERVER_VERSION, highestTimeStamp);
        editor.commit();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ActionService getActionService() {
        return actionService;
    }

    public void setActionService(ActionService actionService) {
        this.actionService = actionService;
    }

    public HTTPAgent getHttpAgent() {
        return httpAgent;
    }

    public void setHttpAgent(HTTPAgent httpAgent) {
        this.httpAgent = httpAgent;
    }

    public TradeItemClassificationRepository getRepository() {
        return repository;
    }

    public void setRepository(TradeItemClassificationRepository repository) {
        this.repository = repository;
    }
}
