package org.smartregister.stock.management.repository;

import android.content.Context;
import android.support.test.runner.AndroidJUnit4;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.smartregister.repository.Repository;
import org.smartregister.stock.management.application.Application;

@RunWith(AndroidJUnit4.class)
public abstract class BaseRepositoryTest {

    protected static Context context;
    protected static Repository mainRepository;

    @BeforeClass
    public static void bootStrap() {
        context = Application.getInstance().getApplicationContext();
        mainRepository = Application.getInstance().getRepository();

    }
}
