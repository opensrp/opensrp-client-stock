package org.smartregister.stock.openlmis.view.contract;

import android.content.Context;

/**
 * Created by samuelgithengi on 9/27/18.
 */
public interface BaseView {

    Context getContext();

    void showProgressDialog(String title, String message);

    void hideProgressDialog();
}
