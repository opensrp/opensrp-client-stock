package org.smartregister.stock.openlmis.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.view.contract.BaseView;

/**
 * Created by samuelgithengi on 9/18/18.
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseView {

    protected Toolbar toolbar;

    protected ProgressDialog progressDialog;

    protected AllSharedPreferences allSharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutView());
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        allSharedPreferences = new AllSharedPreferences(preferences);
    }

    public abstract int getLayoutView();

    @Override
    public void showProgressDialog(String title, String message) {
        progressDialog = ProgressDialog.show(this, title,
                message, true, false);
    }

    @Override
    public void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public Context getContext() {
        return this;
    }
}
