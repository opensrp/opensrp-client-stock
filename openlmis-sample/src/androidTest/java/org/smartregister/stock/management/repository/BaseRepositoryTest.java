package org.smartregister.stock.management.repository;

import android.content.Context;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.smartregister.repository.Repository;
import org.smartregister.stock.management.application.Application;

@RunWith(MockitoJUnitRunner.class)
public abstract class BaseRepositoryTest {

    protected static Context context;
    protected static Repository mainRepository;

    @BeforeClass
    public static void bootStrap() {
        context = Application.getInstance().getApplicationContext();
        mainRepository = Application.getInstance().getRepository();
    }
}
