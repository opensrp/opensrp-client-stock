package org.smartregister.stock.openlmis.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.ArrayList;
import java.util.List;

import static org.smartregister.stock.openlmis.util.OpenLMISConstants.SYNC_COMPLETE_INTENT_ACTION;
import static org.smartregister.util.Log.logError;

/**
 * Created by samuelgithengi on 9/14/18.
 */
public class SyncStatusBroadcastReceiver extends BroadcastReceiver {

    private static SyncStatusBroadcastReceiver instance;

    private List<SyncStatusListener> syncListeners = new ArrayList<>();

    private SyncStatusBroadcastReceiver() {
    }

    public static void init(Context context) {
        if (instance != null) {
            destroy(context);
        }

        instance = new SyncStatusBroadcastReceiver();
        context.registerReceiver(instance,
                new IntentFilter(SYNC_COMPLETE_INTENT_ACTION));
    }

    public static void destroy(Context context) {
        try {
            if (instance != null) {
                context.unregisterReceiver(instance);
            }
        } catch (IllegalArgumentException e) {
            logError("Error on destroy: " + e);
        }
    }

    public static SyncStatusBroadcastReceiver getInstance() {
        return instance;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        for (SyncStatusListener syncStatusListener : syncListeners) {
            syncStatusListener.onSyncComplete();
        }
    }

    public void addSyncStatusListener(SyncStatusListener syncStatusListener) {
        if (!syncListeners.contains(syncStatusListener)) {
            syncListeners.add(syncStatusListener);
        }
    }

    public void removeSyncStatusListener(SyncStatusListener syncStatusListener) {
        if (syncListeners.contains(syncStatusListener)) {
            syncListeners.remove(syncStatusListener);
        }
    }

    public interface SyncStatusListener {
        void onSyncComplete();
    }
}
