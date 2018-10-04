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

import static org.smartregister.stock.openlmis.util.OpenLMISConstants.PREV_SYNC_SERVER_VERSION_TRADE_ITEM;
import static org.smartregister.stock.openlmis.util.Utils.BASE_URL;
import static org.smartregister.stock.openlmis.util.Utils.makeGetRequest;
import static org.smartregister.util.Log.logError;
import static org.smartregister.util.Log.logInfo;

public class TradeItemSyncHelper extends BaseSyncHelper {

    private HTTPAgent httpAgent;
    private ActionService actionService;
    private TradeItemRepository tradeItemRepository;
    private TradeItemClassificationRepository tradeItemClassificationRepository;

    public TradeItemSyncHelper(Context context, ActionService actionService, HTTPAgent httpAgent) {
        this.context = context;
        this.httpAgent = httpAgent;
        this.actionService = actionService;
        this.tradeItemRepository = OpenLMISLibrary.getInstance().getTradeItemRepository();
        this.tradeItemClassificationRepository = OpenLMISLibrary.getInstance().getTradeItemClassificationRepository();
    }

    @Override
    protected String pullFromServer(String url) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String baseUrl = OpenLMISLibrary.getInstance().getContext().configuration().dristhiBaseURL();
        if (baseUrl.endsWith(context.getString(R.string.url_separator))) {
            baseUrl = baseUrl.substring(0, baseUrl.lastIndexOf(context.getString(R.string.url_separator)));
        }
        long timestamp = preferences.getLong(PREV_SYNC_SERVER_VERSION_TRADE_ITEM, 0);
        String timestampStr = String.valueOf(timestamp);
        String uri = MessageFormat.format("{0}/{1}?sync_server_version={2}",
                BASE_URL,
                url,
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
    public boolean saveResponse(String jsonPayload, SharedPreferences preferences) {

        // store tradeItems
        Long highestTimeStamp = 0L;
        List<TradeItem> tradeItems = new Gson().fromJson(jsonPayload, new TypeToken<List<TradeItem>>(){}.getType());
        boolean isEmptyResponse = true;
        for (TradeItem tradeItem : tradeItems) {
            isEmptyResponse = false;
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
        if (!isEmptyResponse) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putLong(PREV_SYNC_SERVER_VERSION_TRADE_ITEM, highestTimeStamp + 1);
            editor.commit();
        }
        return isEmptyResponse;
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

    public TradeItemRepository getTradeItemRepository() {
        return tradeItemRepository;
    }

    public void setTradeItemRepository(TradeItemRepository tradeItemRepository) {
        this.tradeItemRepository = tradeItemRepository;
    }

    public TradeItemClassificationRepository getTradeItemClassificationRepository() {
        return tradeItemClassificationRepository;
    }

    public void setTradeItemClassificationRepository(TradeItemClassificationRepository tradeItemClassificationRepository) {
        this.tradeItemClassificationRepository = tradeItemClassificationRepository;
    }
}
