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
import org.smartregister.stock.openlmis.domain.TradeItem;
import org.smartregister.stock.openlmis.domain.openlmis.Dispensable;
import org.smartregister.stock.openlmis.domain.openlmis.Orderable;
import org.smartregister.stock.openlmis.repository.TradeItemRepository;
import org.smartregister.stock.openlmis.repository.openlmis.DispensableRepository;
import org.smartregister.stock.openlmis.repository.openlmis.OrderableRepository;
import org.smartregister.stock.util.NetworkUtils;

import java.text.MessageFormat;
import java.util.List;

import static org.smartregister.util.Log.logError;

public class OrderableSyncIntentService extends IntentService implements SyncIntentService {

    private static final String TRADE_ITEM_SYNC_URL = "rest/orderables/sync";
    private Context context;
    private HTTPAgent httpAgent;
    private ActionService actionService;

    public OrderableSyncIntentService() {
        super("OrderableSyncIntentService");
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

        final String PREV_SYNC_SERVER_VERSION = "prev_sync_server_version";
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        String baseUrl = OpenLMISLibrary.getInstance().getContext().configuration().dristhiBaseURL();
        if (baseUrl.endsWith(context.getString(R.string.url_separator))) {
            baseUrl = baseUrl.substring(0, baseUrl.lastIndexOf(context.getString(R.string.url_separator)));
        }

        while (true) {
            long timestamp = preferences.getLong(PREV_SYNC_SERVER_VERSION, 0);
            String timeStampString = String.valueOf(timestamp);

            baseUrl = "http://10.20.25.188:8080/openlmis"; // TODO REMOVE THIS
            timeStampString = "0"; // TODO REMOVE THIS

            String uri = MessageFormat.format("{0}/{1}?sync_server_version={2}",
                    baseUrl,
                    TRADE_ITEM_SYNC_URL,
                    timeStampString
            );

            try {
                Response<String> response = httpAgent.fetch(uri);
                if (response.isFailure()) {
                    logError("Orderables pull failed.");
                    return;
                }

                // store Orderables
                Long highestTimeStamp = 0L;
                String jsonPayload = response.payload();
                List<Orderable> orderables = new Gson().fromJson(jsonPayload, new TypeToken<List<Orderable>>(){}.getType());
                OrderableRepository orderableRepository = OpenLMISLibrary.getInstance().getOrderableRepository();
                TradeItemRepository tradeItemRepository = OpenLMISLibrary.getInstance().getTradeItemRegisterRepository();
                DispensableRepository dispensableRepository = OpenLMISLibrary.getInstance().getDispensableRepository();
                for (Orderable orderable : orderables) {
                    orderableRepository.addOrUpdate(orderable);
                    // update trade item that feeds views
                    if (orderable.getTradeItemId() != null) {
                        TradeItem tradeItem = new TradeItem(orderable.getTradeItemId());
                        tradeItem.setCommodityTypeId(orderable.getCommodityTypeId());
                        tradeItem.setNetContent(orderable.getNetContent());
                        tradeItem.setName(orderable.getFullProductName());

                        Dispensable dispensable = dispensableRepository.findDispensable(orderable.getDispensableId());
                        tradeItem.setDispensable(dispensable);
                        tradeItemRepository.addOrUpdate(tradeItem);
                    }

                    if (orderable.getServerVersion() > highestTimeStamp) {
                        highestTimeStamp = orderable.getServerVersion();
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
