package org.smartregister.stock.management.intent;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.smartregister.stock.management.application.Application;
import org.smartregister.stock.openlmis.util.Utils;

import static org.smartregister.stock.openlmis.util.Utils.DATABASE_NAME;
import static org.smartregister.stock.openlmis.util.Utils.PREV_SYNC_SERVER_VERSION;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Utils.class })
@PowerMockIgnore({"org.mockito.*","android.*"})
public abstract class BaseSyncIntentServiceTest {
    @Before
    public void setUp() {
        Context context = Application.getInstance().getApplicationContext();
        context.deleteDatabase(DATABASE_NAME);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(PREV_SYNC_SERVER_VERSION, 0);
        editor.commit();
    }
}
