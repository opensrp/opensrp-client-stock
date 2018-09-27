package org.smartregister.stock.openlmis.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.smartregister.stock.openlmis.R;

/**
 * Created by samuelgithengi on 9/18/18.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected Toolbar toolbar;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutView());
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public abstract int getLayoutView();


}
