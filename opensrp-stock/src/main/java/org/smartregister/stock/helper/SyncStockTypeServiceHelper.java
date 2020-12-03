package org.smartregister.stock.helper;

import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Context;

import com.google.gson.reflect.TypeToken;

import org.smartregister.AllConstants;
import org.smartregister.CoreLibrary;
import org.smartregister.domain.DownloadStatus;
import org.smartregister.domain.Response;
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
            //complete(FetchStatus.noConnection);
            return;
        }

        if (!syncUtils.verifyAuthorization()) {
            try {
                syncUtils.logoutUser();
            } catch (AuthenticatorException | OperationCanceledException | IOException e) {
                Timber.e(e);
            }
        } else {
//            SyncProgress syncProgress = new SyncProgress();
//            syncProgress.setSyncEntity(Constants.ARG_STOCK_TYPE);
//            sendSyncProgressBroadcast(syncProgress,context);
            String baseUrl = getFormattedBaseUrl();
            Response<String> response = httpAgent.fetch(String.format("%s%s", baseUrl, SYNC_URL));
            if (response.payload() != null) {
                saveAllStockTypes(response.payload());
                if (stockSyncConfiguration.shouldFetchStockTypeImages()) {
                    downloadStockTypeImages();
                }
            }
        }
    }

    public void saveAllStockTypes(String payload) {
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
            Map<String, String> map = new HashMap<>();
            Response<DownloadStatus> status = httpAgent.downloadFromURL(photoUrl, fileName, map);
            DownloadStatus downloadStatus = status.payload();
            if (downloadStatus == downloadStatus.downloaded) {
                stockTypeRepository.updatePhotoLocation(stockType.getId(), map.get(AllConstants.DownloadFileConstants.FILE_PATH));
//                ProfileImage profileImage = new ProfileImage();
//                profileImage.setEntityID(String.valueOf(stockType.getUniqueId()));
//                profileImage.setImageid(String.valueOf(stockType.getUniqueId()));
//                profileImage.setSyncStatus(TYPE_Synced);
//                profileImage.setFilecategory(Constants.PRODUCT_IMAGE);
//                profileImage.setFilepath(map.get(AllConstants.DownloadFileConstants.FILE_PATH));
//                imageRepository.add(profileImage);
            }
        }
    }
}
