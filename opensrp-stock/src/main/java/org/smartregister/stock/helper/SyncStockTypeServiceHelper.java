package org.smartregister.stock.helper;

import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.AllConstants;
import org.smartregister.CoreLibrary;
import org.smartregister.domain.DownloadStatus;
import org.smartregister.domain.FetchStatus;
import org.smartregister.domain.Response;
import org.smartregister.receiver.SyncStatusBroadcastReceiver;
import org.smartregister.service.HTTPAgent;
import org.smartregister.stock.StockLibrary;
import org.smartregister.stock.configuration.StockSyncConfiguration;
import org.smartregister.stock.domain.StockType;
import org.smartregister.stock.repository.StockTypeRepository;
import org.smartregister.stock.util.Constants;
import org.smartregister.sync.helper.BaseHelper;
import org.smartregister.util.NetworkUtils;
import org.smartregister.util.SyncUtils;
import org.smartregister.util.Utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

import static java.text.MessageFormat.format;
import static org.smartregister.stock.sync.StockTypeIntentService.SYNC_URL;
import static org.smartregister.util.JsonFormUtils.gson;

public class SyncStockTypeServiceHelper extends BaseHelper {
    private StockTypeRepository stockTypeRepository;
    private HTTPAgent httpAgent;
    private StockSyncConfiguration stockSyncConfiguration;
    private SyncUtils syncUtils;
    private StockLibrary stockLibrary;
    private Context context;

    public SyncStockTypeServiceHelper(Context context) {
        this.context = context;
        init(context);
    }

    protected void init(Context context) {
        CoreLibrary coreLibrary = CoreLibrary.getInstance();
        stockLibrary = StockLibrary.getInstance();
        syncUtils = new SyncUtils(context);
        httpAgent = coreLibrary.context().getHttpAgent();
        stockTypeRepository = stockLibrary.getStockTypeRepository();
        stockSyncConfiguration = stockLibrary.getStockSyncConfiguration();
    }

    public void pullStockTypeFromServer() {
        if (isNetworkAvailable()) {
            sendSyncStatusBroadcastMessage(FetchStatus.noConnection);
        } else {
            if (!syncUtils.verifyAuthorization()) {
                try {
                    syncUtils.logoutUser();
                } catch (AuthenticatorException | OperationCanceledException | IOException e) {
                    Timber.e(e);
                }
            } else {
                SharedPreferences preferences = Utils.getAllSharedPreferences().getPreferences();
                while (true) {
                    long timestamp = preferences.getLong(Constants.LAST_STOCK_TYPE_SYNC, 0);
                    if (timestamp > 0) {
                        timestamp += 1;
                    }
                    sendSyncStatusBroadcastMessage(FetchStatus.fetchStarted);
                    String baseUrl = getFormattedBaseUrl();
                    Response<String> response = httpAgent.fetch(String.format("%s%s?serverVersion=%s", baseUrl, SYNC_URL, timestamp));
                    if (response != null && response.payload() != null) {
                        if (response.isFailure() || StringUtils.isBlank(response.payload())) {
                            return;
                        }
                        List<StockType> stockTypes = gson.fromJson(response.payload(), new TypeToken<List<StockType>>() {
                        }.getType());
                        if (stockTypes == null || stockTypes.isEmpty()) {
                            return;
                        }
                        saveAllStockTypes(stockTypes);

                        sendSyncStatusBroadcastMessage(FetchStatus.fetched);

                        Long highestTimestamp = getHighestTimestampFromStockTypePayLoad(stockTypes);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putLong(Constants.LAST_STOCK_TYPE_SYNC, highestTimestamp);
                        editor.commit();
                    } else {
                        return;
                    }
                }
            }
        }
    }

    private Long getHighestTimestampFromStockTypePayLoad(List<StockType> stockTypes) {
        Long maxServerVersion = 0L;
        for (StockType stockType : stockTypes) {
            Long serverVersion = stockType.getServerVersion();
            if (serverVersion != null && serverVersion > maxServerVersion) {
                maxServerVersion = serverVersion;
            }
        }
        return maxServerVersion;
    }

    protected boolean isNetworkAvailable() {
        return !NetworkUtils.isNetworkAvailable();
    }

    public void saveAllStockTypes(@NonNull List<StockType> stockTypes) {
        stockTypeRepository.batchInsertStockTypes(stockTypes);

        if (stockSyncConfiguration.shouldFetchStockTypeImages()) {
            stockLibrary.getAppExecutors().networkIO().execute(() -> downloadStockTypeImages(stockTypes));
        }
    }

    public void downloadStockTypeImages(@NonNull List<StockType> stockTypes) {
        for (StockType stockType : stockTypes) {
            String photoId = String.valueOf(stockType.getUniqueId());
            String photoUrl = format("{0}/{1}/{2}",
                    getFormattedBaseUrl(),
                    Constants.MEDIA_URL, photoId);

            String fileName = Constants.PRODUCT_IMAGE + "_" + stockType.getUniqueId();
            Map<String, String> detailsMap = new HashMap<>();
            Response<DownloadStatus> status = httpAgent.downloadFromURL(photoUrl, fileName, detailsMap);
            DownloadStatus downloadStatus = status.payload();
            if (downloadStatus == downloadStatus.downloaded) {
                stockTypeRepository.updatePhotoLocation(stockType.getUniqueId(), detailsMap.get(AllConstants.DownloadFileConstants.FILE_PATH));
            }
        }
    }

    private void sendSyncStatusBroadcastMessage(@NonNull FetchStatus fetchStatus) {
        Intent intent = new Intent();
        intent.setAction(SyncStatusBroadcastReceiver.ACTION_SYNC_STATUS);
        intent.putExtra(SyncStatusBroadcastReceiver.EXTRA_FETCH_STATUS, fetchStatus);
        context.sendBroadcast(intent);
    }
}
