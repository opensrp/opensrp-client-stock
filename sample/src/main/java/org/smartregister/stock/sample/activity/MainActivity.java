package org.smartregister.stock.sample.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import org.smartregister.stock.sample.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.location_switching_toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.title_activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        initializeCustomNavbarListeners();

    }

    private void initializeCustomNavbarListeners() {
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        LinearLayout stockregister = (LinearLayout) drawer.findViewById(R.id.stock_control);
        stockregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SampleStockActivity.class);
                startActivity(intent);
                drawer.closeDrawer(GravityCompat.START);

            }
        });

        LinearLayout childRegister = (LinearLayout) drawer.findViewById(R.id.child_register);
        childRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(GravityCompat.START);

            }
        });
    }

}
