package org.smartregister.stock.helper;

import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.google.gson.reflect.TypeToken;

import org.smartregister.AllConstants;
import org.smartregister.CoreLibrary;
import org.smartregister.domain.DownloadStatus;
import org.smartregister.domain.FetchStatus;
import org.smartregister.domain.Response;
import org.smartregister.receiver.SyncStatusBroadcastReceiver;
import org.smartregister.repository.ImageRepository;
import org.smartregister.service.HTTPAgent;
import org.smartregister.stock.StockLibrary;
import org.smartregister.stock.configuration.StockSyncConfiguration;
import org.smartregister.stock.domain.StockType;
import org.smartregister.stock.repository.StockTypeRepository;
import org.smartregister.stock.util.Constants;
import org.smartregister.sync.helper.BaseHelper;
import org.smartregister.util.NetworkUtils;
import org.smartregister.util.SyncUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

import static java.text.MessageFormat.format;
import static org.smartregister.stock.sync.StockTypeIntentService.SYNC_URL;
import static org.smartregister.util.JsonFormUtils.gson;

public class SyncStockTypeServiceHelper extends BaseHelper {
    private ImageRepository imageRepository;
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
        imageRepository = coreLibrary.context().imageRepository();
        stockSyncConfiguration = stockLibrary.getStockSyncConfiguration();
    }

    public void pullStockTypeFromServer() {
        if (!NetworkUtils.isNetworkAvailable()) {
            sendSyncStatusBroadcastMessage(FetchStatus.noConnection);
        } else {
            if (!syncUtils.verifyAuthorization()) {
                try {
                    syncUtils.logoutUser();
                } catch (AuthenticatorException | OperationCanceledException | IOException e) {
                    Timber.e(e);
                }
            } else {
                sendSyncStatusBroadcastMessage(FetchStatus.fetchStarted);
                String baseUrl = getFormattedBaseUrl();
                Response<String> response = httpAgent.fetch(String.format("%s%s", baseUrl, SYNC_URL));
                if (response != null && response.payload() != null) {
                    saveAllStockTypes(response.payload());
                    if (stockSyncConfiguration.shouldFetchStockTypeImages()) {
                        downloadStockTypeImages();
                    }
                    sendSyncStatusBroadcastMessage(FetchStatus.fetched);
                }
            }
        }
    }

    public void saveAllStockTypes(@NonNull String payload) {
        List<StockType> stockTypes = gson.fromJson(payload, new TypeToken<List<StockType>>() {
        }.getType());
        stockTypeRepository.batchInsertStockTypes(stockTypes);
    }

    public void downloadStockTypeImages() {
        List<StockType> stockTypes = stockTypeRepository.findAllWithUnDownloadedPhoto();
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
                stockTypeRepository.updatePhotoLocation(stockType.getId(), detailsMap.get(AllConstants.DownloadFileConstants.FILE_PATH));
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
