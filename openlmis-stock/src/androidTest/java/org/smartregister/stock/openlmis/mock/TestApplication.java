package org.smartregister.stock.openlmis.mock;

import org.smartregister.Context;
import org.smartregister.CoreLibrary;
import org.smartregister.repository.Repository;
import org.smartregister.view.activity.DrishtiApplication;

import static org.smartregister.util.Log.logError;

/**
 * Created by samuelgithengi on 7/10/18.
 */
public class TestApplication extends DrishtiApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        context = Context.getInstance();
        context.updateApplicationContext(getApplicationContext());
        CoreLibrary.init(context);
    }

    @Override
    public Repository getRepository() {
        try {
            if (repository == null) {
                repository = new TestRepository(getApplicationContext(), context.session(), null,
                        context.sharedRepositoriesArray());
            }
        } catch (UnsatisfiedLinkError e) {
            logError("Error on getRepository: " + e);
        }
        return repository;
    }

    @Override
    public String getPassword() {
        return "Testing_Pass";
    }

    @Override
    public void logoutCurrentUser() {

    }
}
