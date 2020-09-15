package org.smartregister.stock.management.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.joda.time.DateTime;
import org.smartregister.Context;
import org.smartregister.domain.LoginResponse;
import org.smartregister.domain.TimeStatus;
import org.smartregister.domain.jsonmapping.LoginResponseData;
import org.smartregister.event.Listener;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.stock.management.BuildConfig;
import org.smartregister.stock.management.R;
import org.smartregister.stock.management.application.Application;
import org.smartregister.stock.management.helper.LocationHelper;
import org.smartregister.stock.openlmis.view.StockListActivity;
import org.smartregister.util.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static org.smartregister.AllConstants.DRISHTI_BASE_URL;
import static org.smartregister.AllConstants.PIONEER_USER;
import static org.smartregister.domain.LoginResponse.NO_INTERNET_CONNECTIVITY;
import static org.smartregister.domain.LoginResponse.SUCCESS;
import static org.smartregister.domain.LoginResponse.UNAUTHORIZED;
import static org.smartregister.domain.LoginResponse.UNKNOWN_RESPONSE;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.IS_REMOTE_LOGIN;
import static org.smartregister.stock.openlmis.util.Utils.hideKeyboard;
import static org.smartregister.util.Log.logError;

public class LoginActivity extends AppCompatActivity {

    private EditText userNameEditText;
    private EditText passwordEditText;
    private CheckBox showPasswordCheckBox;
    private ProgressDialog progressDialog;
    private RemoteLoginTask remoteLoginTask;
    private android.content.Context appContext;
    private static final String TAG = LoginActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.black)));

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().putString(DRISHTI_BASE_URL, "https://vreach-dev.smartregister.org/opensrp/").commit();
        preferences.edit().putString(PIONEER_USER, "admin").commit();

        appContext = this;
        positionViews();
        initializeLoginFields();
        initializeBuildDetails();
        setDoneActionHandlerOnPasswordField();
        setListenerOnShowPasswordCheckbox();
        initializeProgressDialog();
        setLanguage();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ;
//        if (!getOpenSRPContext().IsUserLoggedOut()) {
//            goToHome(false);
//        }
    }

    private void setDoneActionHandlerOnPasswordField() {
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    login(findViewById(R.id.login_login_btn));
                }
                return false;
            }
        });
    }

    private void setListenerOnShowPasswordCheckbox() {
        showPasswordCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }

    private void initializeLoginFields() {
        userNameEditText = findViewById(R.id.login_user_name_edit_text);
        passwordEditText = findViewById(R.id.login_password_edit_text);
        showPasswordCheckBox = findViewById(R.id.login_show_password_checkbox);
    }

    private void initializeProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle(getString(org.smartregister.R.string.loggin_in_dialog_title));
        progressDialog.setMessage(getString(org.smartregister.R.string.loggin_in_dialog_message));
    }

    private void initializeBuildDetails() {
        TextView buildDetailsView = findViewById(R.id.login_build_text_view);
        try {
            buildDetailsView.setText("Version " + getVersion() + ", Built on: " + getBuildDate());
        } catch (Exception e) {
            logError("Error fetching build details: " + e);
        }
    }

    public void login(final View view) {
        login(view, !getOpenSRPContext().allSharedPreferences().fetchForceRemoteLogin(getOpenSRPContext().allSharedPreferences().fetchRegisteredANM()));
    }

    private void login(final View view, boolean localLogin) {

        Log.i(getClass().getName(), "Hiding Keyboard " + DateTime.now().toString());
        hideKeyboard(this);
        view.setClickable(false);

        final String userName = userNameEditText.getText().toString().trim();
        final char[] password = passwordEditText.getText().toString().trim().toCharArray();
        if (!TextUtils.isEmpty(userName) && password.length > 0) {
            if (localLogin) {
                localLogin(view, userName, password);
            } else {
                remoteLogin(view, userName, String.valueOf(password));
            }
        } else {
            showErrorDialog(getResources().getString(R.string.unauthorized));
            view.setClickable(true);
        }
        Log.i(getClass().getName(), "Login result finished " + DateTime.now().toString());
    }

    private void localLogin(View view, String userName, char[] password) {
        view.setClickable(true);
        if (getOpenSRPContext().userService().isUserInValidGroup(userName, password)
                && (!BuildConfig.TIME_CHECK || TimeStatus.OK.equals(getOpenSRPContext().userService().validateStoredServerTimeZone()))) {
            localLoginWith(userName, password);
        } else {
            login(findViewById(R.id.login_login_btn), false);
        }
    }

    private void localLoginWith(String userName, char[] password) {
        getOpenSRPContext().userService().localLoginWith(userName);
        goToHome(false);
    }

    private void remoteLogin(final View view, final String userName, final String password) {

        try {
            if (!getOpenSRPContext().allSharedPreferences().fetchBaseURL("").isEmpty()) {
                tryRemoteLogin(userName, password, new Listener<LoginResponse>() {

                    public void onEvent(LoginResponse loginResponse) {
                        view.setClickable(true);
                        if (loginResponse == LoginResponse.SUCCESS) {
                            if (getOpenSRPContext().userService().isUserInPioneerGroup(userName)) {
                                TimeStatus timeStatus = getOpenSRPContext().userService().validateDeviceTime(
                                        loginResponse.payload(), BuildConfig.MAX_SERVER_TIME_DIFFERENCE
                                );
                                if (!BuildConfig.TIME_CHECK || timeStatus.equals(TimeStatus.OK)) {
                                    remoteLoginWith(userName, password, loginResponse.payload());
                                } else {
                                    if (timeStatus.equals(TimeStatus.TIMEZONE_MISMATCH)) {
                                        TimeZone serverTimeZone = getOpenSRPContext().userService()
                                                .getServerTimeZone(loginResponse.payload());
                                        showErrorDialog(getString(timeStatus.getMessage(),
                                                serverTimeZone.getDisplayName()));
                                    } else {
                                        showErrorDialog(getString(timeStatus.getMessage()));
                                    }
                                }
                            } else {
                                // Valid user from wrong group trying to log in
                                showErrorDialog(getString(R.string.unauthorized_group));
                            }
                        } else {
                            if (loginResponse == null) {
                                showErrorDialog("Sorry, your login failed. Please try again");
                            } else {
                                if (loginResponse == NO_INTERNET_CONNECTIVITY) {
                                    showErrorDialog(getResources().getString(R.string.no_internet_connectivity));
                                } else if (loginResponse == UNKNOWN_RESPONSE) {
                                    showErrorDialog(getResources().getString(R.string.unknown_response));
                                } else if (loginResponse == UNAUTHORIZED) {
                                    showErrorDialog(getResources().getString(R.string.unauthorized));
                                } else {
                                    showErrorDialog(loginResponse.message());
                                }
                            }
                        }
                    }
                });
            } else {
                view.setClickable(true);
                showErrorDialog("OpenSRP Base URL is missing. Please add it in Setting and try again");
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());

            showErrorDialog("Error occurred trying to login in. Please try again...");
        }
    }

    private void tryRemoteLogin(final String userName, final String password, final Listener<LoginResponse> afterLogincheck) {
        if (remoteLoginTask != null && !remoteLoginTask.isCancelled()) {
            remoteLoginTask.cancel(true);
        }
        remoteLoginTask = new RemoteLoginTask(userName, password, afterLogincheck);
        remoteLoginTask.execute();
    }

    private void remoteLoginWith(String userName, String password, LoginResponseData userInfo) {
        getOpenSRPContext().userService().forceRemoteLogin(userName);
        goToHome(true);
    }

    private void goToHome(boolean remote) {
        if (remote) {
            Utils.startAsyncTask(new SaveTeamLocationsTask(), null);
        }
        Intent intent = new Intent(this, StockListActivity.class);
        intent.putExtra(IS_REMOTE_LOGIN, remote);
        startActivity(intent);

        finish();
    }


    private void showErrorDialog(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(getString(org.smartregister.R.string.login_failed_dialog_title))
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create();
        alertDialog.show();
    }

    private String getVersion() throws PackageManager.NameNotFoundException {
        PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        return packageInfo.versionName;
    }

    private String getBuildDate() {
        return new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(new Date(BuildConfig.BUILD_TIMESTAMP));
    }

    public static void setLanguage() {
        AllSharedPreferences allSharedPreferences = new AllSharedPreferences(getDefaultSharedPreferences(getOpenSRPContext().applicationContext()));
        String preferredLocale = allSharedPreferences.fetchLanguagePreference();
        Resources resources = getOpenSRPContext().applicationContext().getResources();

        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = new Locale(preferredLocale);
        resources.updateConfiguration(configuration, displayMetrics);
    }

    public static Context getOpenSRPContext() {
        return Application.getInstance().getContext();
    }

    private void positionViews() {
        final ScrollView canvasSV = findViewById(R.id.canvasSV);
        final RelativeLayout canvasRL = findViewById(R.id.login_layout);
        final LinearLayout logoCanvasLL = findViewById(R.id.bottom_section);
        final LinearLayout credentialsCanvasLL = findViewById(R.id.middle_section);

        canvasSV.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                canvasSV.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                int windowHeight = canvasSV.getHeight();
                int topMargin = (windowHeight / 2)
                        - (credentialsCanvasLL.getHeight() / 2)
                        - logoCanvasLL.getHeight();
                topMargin = topMargin / 2;

                RelativeLayout.LayoutParams logoCanvasLP = (RelativeLayout.LayoutParams) logoCanvasLL.getLayoutParams();
                logoCanvasLP.setMargins(0, topMargin, 0, 0);
                logoCanvasLL.setLayoutParams(logoCanvasLP);

                canvasRL.setMinimumHeight(windowHeight);
            }
        });
    }


    /**
     * ============================ AsyncTasks =====================================
     */
    private class RemoteLoginTask extends AsyncTask<Void, Void, LoginResponse> {
        private final String username;
        private final String password;
        private final Listener<LoginResponse> afterLoginCheck;

        private RemoteLoginTask(String username, String password, Listener<LoginResponse> afterLoginCheck) {
            this.username = username;
            this.password = password;
            this.afterLoginCheck = afterLoginCheck;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected LoginResponse doInBackground(Void... params) {
//            if (getOpenSRPContext().userService().isValidLocalLogin(username, password.getBytes())){
            if (true) {
                return SUCCESS;
            } else {
                return UNAUTHORIZED;
            }
        }

        @Override
        protected void onPostExecute(LoginResponse loginResponse) {
            super.onPostExecute(loginResponse);
            if (!isDestroyed()) {
                progressDialog.dismiss();
                afterLoginCheck.onEvent(loginResponse);
            }
        }
    }

    private class SaveTeamLocationsTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            LocationHelper.getInstance().locationIdsFromHierarchy();
            return null;
        }
    }
}
