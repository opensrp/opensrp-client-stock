package org.smartregister.stock.management.application;

import org.smartregister.SyncConfiguration;
import org.smartregister.SyncFilter;
import org.smartregister.view.activity.BaseLoginActivity;

import java.util.Collections;
import java.util.List;

public class SampleSyncConfiguration extends SyncConfiguration {

    @Override
    public int getSyncMaxRetries() {
        return 0;
    }

    @Override
    public SyncFilter getSyncFilterParam() {
        return SyncFilter.LOCATION;
    }

    @Override
    public String getSyncFilterValue() {
//        AllSharedPreferences sharedPreferences = SampleApplication.getInstance().getContext().allSharedPreferences();
//        return sharedPreferences.fetchDefaultTeamId(sharedPreferences.fetchRegisteredANM());
        return null;
    }

    @Override
    public int getUniqueIdSource() {
        return 0;
    }

    @Override
    public int getUniqueIdBatchSize() {
        return 0;
    }

    @Override
    public int getUniqueIdInitialBatchSize() {
        return 0;
    }

    @Override
    public SyncFilter getEncryptionParam() {
        return SyncFilter.LOCATION;
    }

    @Override
    public boolean updateClientDetailsTable() {
        return false;
    }

    @Override
    public List<String> getSynchronizedLocationTags() {
        return Collections.emptyList();
    }

    @Override
    public String getTopAllowedLocationLevel() {
        return "";
    }

    @Override
    public String getOauthClientId() {
        return null;
    }

    @Override
    public String getOauthClientSecret() {
        return null;
    }

    @Override
    public Class<? extends BaseLoginActivity> getAuthenticationActivity() {
        return null;
    }
}
