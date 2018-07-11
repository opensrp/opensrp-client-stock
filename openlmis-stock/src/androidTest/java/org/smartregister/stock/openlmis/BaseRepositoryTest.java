package org.smartregister.stock.openlmis;

import android.content.Context;
import android.support.test.runner.AndroidJUnit4;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.smartregister.repository.Repository;
import org.smartregister.stock.openlmis.mock.TestApplication;

@RunWith(AndroidJUnit4.class)
public abstract class BaseRepositoryTest {

    protected static Context context;
    protected static Repository mainRepository;
    protected static org.smartregister.Context openSRPContext;

    @BeforeClass
    public static void bootStrap() {
        context = TestApplication.getInstance().getApplicationContext();
        mainRepository = TestApplication.getInstance().getRepository();

    }
}
