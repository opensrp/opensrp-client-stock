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
import org.smartregister.stock.openlmis.domain.openlmis.Orderable;
import org.smartregister.stock.openlmis.repository.openlmis.OrderableRepository;
import org.smartregister.stock.openlmis.util.SynchronizedUpdater;

import java.text.MessageFormat;
import java.util.List;

import static org.smartregister.stock.openlmis.util.Utils.BASE_URL;
import static org.smartregister.stock.openlmis.util.Utils.PREV_SYNC_SERVER_VERSION;
import static org.smartregister.stock.openlmis.util.Utils.makeGetRequest;
import static org.smartregister.util.Log.logError;
import static org.smartregister.util.Log.logInfo;

public class OrderableSyncHelper extends BaseSyncHelper {

    private static final String TRADE_ITEM_SYNC_URL = "rest/orderables/sync";
    private HTTPAgent httpAgent;
    private ActionService actionService;
    private OrderableRepository orderableRepository;

    public OrderableSyncHelper(Context context, ActionService actionService,  HTTPAgent httpAgent) {
        this.orderableRepository = OpenLMISLibrary.getInstance().getOrderableRepository();
        this.context = context;
        this.httpAgent = httpAgent;
        this.actionService = actionService;
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
                TRADE_ITEM_SYNC_URL,
                timestampStr
        );
        // TODO: make baseUrl configurable
        String jsonPayload = null;
        try {
            jsonPayload = makeGetRequest(uri);
            if (jsonPayload == null) {
                logError("Orderables pull failed.");
            }
            logInfo("Orderables pulled successfully!");
        } catch (Exception e) {
            logError(e.getMessage());
            return jsonPayload;
        }
        return jsonPayload;
    }

    @Override
    public void saveResponse(String jsonPayload, SharedPreferences preferences) {

        // store Orderables
        Long highestTimeStamp = 0L;
        List<Orderable> orderables = new Gson().fromJson(jsonPayload, new TypeToken<List<Orderable>>(){}.getType());
        for (Orderable orderable : orderables) {
            orderableRepository.addOrUpdate(orderable);
            // update trade item that feeds views
            if (orderable.getTradeItemId() != null) {
                SynchronizedUpdater.getInstance().updateInfo(orderable);
            }
            if (orderable.getServerVersion() > highestTimeStamp) {
                highestTimeStamp = orderable.getServerVersion();
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

    public OrderableRepository getOrderableRepository() {
        return orderableRepository;
    }

    public void setOrderableRepository(OrderableRepository orderableRepository) {
        this.orderableRepository = orderableRepository;
    }
}