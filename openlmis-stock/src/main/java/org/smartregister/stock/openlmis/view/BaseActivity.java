package org.smartregister.stock.openlmis.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.smartregister.CoreLibrary;
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

        allSharedPreferences = CoreLibrary.getInstance().context().allSharedPreferences();
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
