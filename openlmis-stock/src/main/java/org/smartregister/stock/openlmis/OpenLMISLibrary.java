package org.smartregister.stock.openlmis;

import org.smartregister.Context;
import org.smartregister.repository.Repository;

/**
 * Created by samuelgithengi on 7/10/18.
 */
public class OpenLMISLibrary {

    private static OpenLMISLibrary instance;
    private Context context;
    private Repository repository;

    public OpenLMISLibrary(Context context, Repository repository) {

        this.context = context;
        this.repository = repository;
    }

    public static void init(Context context, Repository repository) {
        if (instance == null) {
            instance = new OpenLMISLibrary(context, repository);
        }
    }

    public static OpenLMISLibrary getInstance() {
        if (instance == null) {
            throw new IllegalStateException(" Instance does not exist!!! Call " + OpenLMISLibrary.class.getName() + ".init method in the onCreate method of your Application class ");
        }
        return instance;
    }


    public Context getContext() {
        return context;
    }

    public Repository getRepository() {
        return repository;
    }
}