package org.smartregister.stock.openlmis.intent.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.smartregister.service.ActionService;
import org.smartregister.service.HTTPAgent;
import org.smartregister.stock.openlmis.OpenLMISLibrary;
import org.smartregister.stock.openlmis.domain.openlmis.CommodityType;
import org.smartregister.stock.openlmis.domain.openlmis.TradeItem;
import org.smartregister.stock.openlmis.repository.openlmis.CommodityTypeRepository;
import org.smartregister.stock.openlmis.repository.openlmis.TradeItemRepository;
import org.smartregister.stock.openlmis.util.SynchronizedUpdater;

import java.text.MessageFormat;
import java.util.List;

import static org.smartregister.stock.openlmis.util.Utils.BASE_URL;
import static org.smartregister.stock.openlmis.util.Utils.PREV_SYNC_SERVER_VERSION;
import static org.smartregister.stock.openlmis.util.Utils.makeGetRequest;
import static org.smartregister.util.Log.logError;

public class CommodityTypeSyncHelper extends BaseSyncHelper {

    private static final String LOT_SYNC_URL = "rest/commodity-types/sync";
    private HTTPAgent httpAgent;
    private ActionService actionService;
    private CommodityTypeRepository commodityTypeRepository;
    private TradeItemRepository tradeItemRepository;

    public CommodityTypeSyncHelper(Context context, ActionService actionService, HTTPAgent httpAgent) {
        this.commodityTypeRepository = OpenLMISLibrary.getInstance().getCommodityTypeRepository();
        this.tradeItemRepository = OpenLMISLibrary.getInstance().getTradeItemRepository();
        this.context = context;
        this.actionService = actionService;
        this.httpAgent = httpAgent;
    }

    @Override
    protected String pullFromServer() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        long timestamp = preferences.getLong(PREV_SYNC_SERVER_VERSION, 0);
        String timestampStr = String.valueOf(timestamp);
        String uri = MessageFormat.format("{0}/{1}?sync_server_version={2}",
                BASE_URL,
                LOT_SYNC_URL,
                timestampStr
        );
        // TODO: make baseUrl configurable
        String jsonPayload = null;
        try {
            jsonPayload = makeGetRequest(uri);
            if (jsonPayload == null) {
                logError("TradeItemClassifications pull failed.");
            }
        } catch (Exception e) {
            logError(e.getMessage());
            return jsonPayload;
        }
        return jsonPayload;
    }

    @Override
    public boolean saveResponse(String jsonPayload, SharedPreferences preferences) {
        // store commodityTypes
        Long highestTimeStamp = 0L;
        List<CommodityType> commodityTypes = new Gson().fromJson(jsonPayload, new TypeToken<List<CommodityType>>(){}.getType());
        boolean isEmptyResponse = true;
        for (CommodityType commodityType : commodityTypes) {
            isEmptyResponse = false;
            commodityTypeRepository.addOrUpdate(commodityType);
            // update trade item repository
            for (TradeItem tradeItem : commodityType.getTradeItems()) {
                tradeItemRepository.addOrUpdate(tradeItem);
            }
            // update trade item register repository
            SynchronizedUpdater.getInstance().updateInfo(commodityType);
            if (commodityType.getServerVersion() > highestTimeStamp) {
                highestTimeStamp = commodityType.getServerVersion();
            }
        }
        // save highest server version
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(PREV_SYNC_SERVER_VERSION, highestTimeStamp);
        editor.commit();

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

    public CommodityTypeRepository getCommodityTypeRepository() {
        return commodityTypeRepository;
    }

    public void setCommodityTypeRepository(CommodityTypeRepository commodityTypeRepository) {
        this.commodityTypeRepository = commodityTypeRepository;
    }

    public TradeItemRepository getTradeItemRepository() {
        return tradeItemRepository;
    }

    public void setTradeItemRepository(TradeItemRepository tradeItemRepository) {
        this.tradeItemRepository = tradeItemRepository;
    }
}
