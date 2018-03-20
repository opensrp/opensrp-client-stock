package org.smartregister.stock.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.stock.R;

import static org.smartregister.util.Log.logError;

/**
 *
 * Created by Ephraim Kigamba - ekigamba@ona.io on 15/03/2018.
 *
 */
public abstract class BasicOrderActivity extends AppCompatActivity {

    protected Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        context = this;

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getStatusBarColor());
        }

        TextView nameInitials = (TextView) findViewById(R.id.name_inits);
        nameInitials.setText(getLoggedInUserInitials());

        ((LinearLayout) findViewById(R.id.btn_back_to_home)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        afterOnCreate();
    }


    protected String getLoggedInUserInitials() {
        try {
            AllSharedPreferences allSharedPreferences = new AllSharedPreferences(PreferenceManager.getDefaultSharedPreferences(context));

            String preferredName = allSharedPreferences.getANMPreferredName(
                    allSharedPreferences.fetchRegisteredANM());
            if (!TextUtils.isEmpty(preferredName)) {
                String[] initialsArray = preferredName.split(" ");
                String initials = "";
                if (initialsArray.length > 0) {
                    initials = initialsArray[0].substring(0, 1);
                    if (initialsArray.length > 1) {
                        initials = initials + initialsArray[1].substring(0, 1);
                    }
                }

                return initials.toUpperCase();
            }

        } catch (Exception e) {
            logError("Error on initView : Getting Preferences: Getting Initials");
        }

        return null;
    }

    protected abstract int getContentView();

    protected abstract int getStatusBarColor();

    protected abstract void afterOnCreate();
}
