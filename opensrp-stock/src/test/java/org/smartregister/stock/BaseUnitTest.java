package org.smartregister.stock;

import android.os.Build;

import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

/**
 * Created by ndegwamartin on 05/02/2018.
 */

@RunWith(RobolectricTestRunner.class)
@PowerMockRunnerDelegate(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.P)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
public abstract class BaseUnitTest {
}
