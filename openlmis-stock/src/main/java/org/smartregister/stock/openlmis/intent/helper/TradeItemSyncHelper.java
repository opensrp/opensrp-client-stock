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
import org.smartregister.stock.openlmis.domain.openlmis.TradeItem;
import org.smartregister.stock.openlmis.domain.openlmis.TradeItemClassification;
import org.smartregister.stock.openlmis.repository.openlmis.TradeItemClassificationRepository;
import org.smartregister.stock.openlmis.repository.openlmis.TradeItemRepository;

import java.text.MessageFormat;
import java.util.List;

import static org.smartregister.stock.openlmis.util.Utils.BASE_URL;
import static org.smartregister.stock.openlmis.util.Utils.PREV_SYNC_SERVER_VERSION;
import static org.smartregister.stock.openlmis.util.Utils.makeGetRequest;
import static org.smartregister.util.Log.logError;
import static org.smartregister.util.Log.logInfo;

public class TradeItemSyncHelper implements BaseSyncHelper {

    private static final String TRADE_ITEM_SYNC_URL = "rest/trade-items/sync";
    private Context context;
    private HTTPAgent httpAgent;
    private ActionService actionService;

    public TradeItemSyncHelper(Context context, ActionService actionService, HTTPAgent httpAgent) {
        this.context = context;
        this.httpAgent = httpAgent;
        this.actionService = actionService;
    }

    @Override
    public void processIntent() {
        String response = pullFromServer();
        if (response == null) {
            return;
        }
        saveResponse(response, PreferenceManager.getDefaultSharedPreferences(context));
    }

    private String pullFromServer() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String baseUrl = OpenLMISLibrary.getInstance().getContext().configuration().dristhiBaseURL();
        if (baseUrl.endsWith(context.getString(R.string.url_separator))) {
            baseUrl = baseUrl.substring(0, baseUrl.lastIndexOf(context.getString(R.string.url_separator)));
        }
        long timestamp = preferences.getLong(PREV_SYNC_SERVER_VERSION, 0);
        String timestampStr = String.valueOf(timestamp);
        String uri = MessageFormat.format("{0}/{1}?sync_server_version={2}",
                BASE_URL,
                TRADE_ITEM_SYNC_URL,
                timestampStr
        );
        // TODO: make baseUrl configurable
        String jsonPayload = null;
        try {
            jsonPayload = makeGetRequest(uri);
            if (jsonPayload == null) {
                logError("TradeItems pull failed.");
            }
            logInfo("TradeItems pulled successfully!");
        } catch (Exception e) {
            logError(e.getMessage());
            return jsonPayload;
        }
        return jsonPayload;
    }

    @Override
    public void saveResponse(String jsonPayload, SharedPreferences preferences) {

        // store tradeItems
        Long highestTimeStamp = 0L;
        List<TradeItem> tradeItems = new Gson().fromJson(jsonPayload, new TypeToken<List<TradeItem>>(){}.getType());
        TradeItemRepository tradeItemRepository = OpenLMISLibrary.getInstance().getTradeItemRepository();
        TradeItemClassificationRepository tradeItemClassificationRepository = OpenLMISLibrary.getInstance().getTradeItemClassificationRepository();
        for (TradeItem tradeItem : tradeItems) {
            tradeItemRepository.addOrUpdate(tradeItem);
            // save trade item classifications
            List<TradeItemClassification> tradeItemClassifications = tradeItem.getClassifications();
            if (tradeItemClassifications.size() > 0) {
                for (TradeItemClassification tradeItemClassification : tradeItemClassifications) {
                    tradeItemClassification.setTradeItem(tradeItem);
                    tradeItemClassificationRepository.addOrUpdate(tradeItemClassification);
                }
            }
            if (tradeItem.getServerVersion() > highestTimeStamp) {
                highestTimeStamp = tradeItem.getServerVersion();
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

    public HTTPAgent getHttpAgent() {
        return httpAgent;
    }

    public void setHttpAgent(HTTPAgent httpAgent) {
        this.httpAgent = httpAgent;
    }

    public ActionService getActionService() {
        return actionService;
    }

    public void setActionService(ActionService actionService) {
        this.actionService = actionService;
    }
}
