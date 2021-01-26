package org.smartregister.stock.helper;

import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.util.ReflectionHelpers;
import org.smartregister.Context;
import org.smartregister.CoreLibrary;
import org.smartregister.domain.DownloadStatus;
import org.smartregister.domain.Response;
import org.smartregister.domain.ResponseStatus;
import org.smartregister.service.HTTPAgent;
import org.smartregister.stock.BaseUnitTest;
import org.smartregister.stock.StockLibrary;
import org.smartregister.stock.configuration.StockSyncConfiguration;
import org.smartregister.stock.domain.StockType;
import org.smartregister.stock.repository.StockTypeRepository;
import org.smartregister.util.SyncUtils;

import java.io.IOException;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;


public class SyncStockTypeServiceHelperTest extends BaseUnitTest {

    private SyncStockTypeServiceHelper syncStockTypeServiceHelper;

    @Mock
    private CoreLibrary coreLibrary;

    @Mock
    private StockLibrary stockLibrary;

    @Mock
    private Context opensrpContext;

    @Mock
    private HTTPAgent httpAgent;

    @Mock
    private StockSyncConfiguration stockSyncConfiguration;

    @Mock
    private StockTypeRepository stockTypeRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionHelpers.setStaticField(CoreLibrary.class, "instance", coreLibrary);
        ReflectionHelpers.setStaticField(StockLibrary.class, "instance", stockLibrary);

        doReturn(stockTypeRepository).when(stockLibrary).getStockTypeRepository();
        doReturn(stockSyncConfiguration).when(stockLibrary).getStockSyncConfiguration();
        doReturn(opensrpContext).when(coreLibrary).context();
        doReturn(httpAgent).when(opensrpContext).getHttpAgent();

        syncStockTypeServiceHelper = spy(new SyncStockTypeServiceHelper(RuntimeEnvironment.application));
    }

    @Test
    public void testPullStockTypeFromServerShouldLogOutUserIfNotVerified() throws AuthenticatorException, OperationCanceledException, IOException {
        SyncUtils syncUtilsSpy = spy(new SyncUtils(RuntimeEnvironment.application));

        ReflectionHelpers.setField(syncStockTypeServiceHelper, "syncUtils", syncUtilsSpy);

        doReturn("").when(syncStockTypeServiceHelper).getFormattedBaseUrl();

        doNothing().when(syncUtilsSpy).logoutUser();

        doReturn(false).when(syncUtilsSpy).verifyAuthorization();

        doReturn(false).when(syncStockTypeServiceHelper).isNetworkAvailable();

        doReturn(true).when(stockSyncConfiguration).shouldFetchStockTypeImages();

        syncStockTypeServiceHelper.pullStockTypeFromServer();

        verify(syncUtilsSpy).logoutUser();

    }

    @Test
    public void testPullStockTypeFromServerShouldInvokeRequiredMethodsIfResponseNotEmpty() {

        SyncUtils syncUtilsSpy = spy(new SyncUtils(RuntimeEnvironment.application));

        ReflectionHelpers.setField(syncStockTypeServiceHelper, "syncUtils", syncUtilsSpy);

        String payload = "[{\"uniqueId\":7,\"productName\":\"TestUpload1\",\"isAttractiveItem\":false,\"materialNumber\":\"TestUpload1\",\"availability\":\"TestUpload1\",\"condition\":\"TestUpload1\",\"appropriateUsage\":\"TestUpload1\",\"accountabilityPeriod\":1,\"photoURL\":\"\"},{\"uniqueId\":9,\"productName\":\"Scale Mother-Child\",\"isAttractiveItem\":false,\"materialNumber\":\"S0141021\",\"availability\":\"Electronic scale for weighing adults and children, for use up to 250kg\",\"condition\":\"Supplied with customer-replaceable batteries. \",\"appropriateUsage\":\"Scale allows a child's weight to be measured while being held by an adult.\",\"accountabilityPeriod\":36,\"photoURL\":\"\",\"serverVersion\":4}]";

        doAnswer(new Answer() {
            int count = -1;

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                count++;
                return count == 0 ? new Response<>(ResponseStatus.success, payload) : new Response<>(ResponseStatus.failure, payload);
            }
        }).when(httpAgent).fetch(anyString());

        doReturn("").when(syncStockTypeServiceHelper).getFormattedBaseUrl();

        doReturn(true).when(syncUtilsSpy).verifyAuthorization();

        doReturn(false).when(syncStockTypeServiceHelper).isNetworkAvailable();

        doReturn(false).when(stockSyncConfiguration).shouldFetchStockTypeImages();

        syncStockTypeServiceHelper.pullStockTypeFromServer();

        verify(syncStockTypeServiceHelper).saveAllStockTypes(anyList());
    }

    @Test
    public void testDownloadStockTypeImagesShouldFetchImagesOfStockTypes() throws InterruptedException {
        StockType stockType = new StockType(1l, 0, "", "", "", "");
        stockType.setUniqueId(2l);

        Response<DownloadStatus> response = new Response<>(ResponseStatus.success, DownloadStatus.downloaded);

        doReturn(response).when(httpAgent).downloadFromURL(anyString(), anyString(), anyMap());

        doReturn("").when(syncStockTypeServiceHelper).getFormattedBaseUrl();

        doReturn(false).when(syncStockTypeServiceHelper).isNetworkAvailable();

        doReturn(true).when(stockSyncConfiguration).shouldFetchStockTypeImages();

        syncStockTypeServiceHelper.downloadStockTypeImages(Collections.singletonList(stockType));

        verify(stockTypeRepository).updatePhotoLocation(eq(stockType.getUniqueId()), isNull());

    }

    @After
    public void tearDown() {
        ReflectionHelpers.setStaticField(CoreLibrary.class, "instance", null);
        ReflectionHelpers.setStaticField(StockLibrary.class, "instance", null);
    }
}