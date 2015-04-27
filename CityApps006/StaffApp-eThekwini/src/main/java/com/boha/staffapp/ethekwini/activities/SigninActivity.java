package com.boha.staffapp.ethekwini.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.boha.library.activities.CityApplication;
import com.boha.library.dto.GcmDeviceDTO;
import com.boha.library.dto.MunicipalityDTO;
import com.boha.library.dto.MunicipalityStaffDTO;
import com.boha.library.transfer.RequestDTO;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.CacheUtil;
import com.boha.library.util.GCMUtil;
import com.boha.library.util.NetUtil;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.Util;
import com.boha.staffapp.ethekwini.R;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class SigninActivity extends ActionBarActivity {

    ImageView heroImage, logoImage;
    Timer timer;
    TextView txtWelcome;
    View handle;
    Context ctx;
    ProgressBar progressBar;
    Activity activity;
    Button btnSend;
    EditText editPassword;
    ResponseDTO response;
    Spinner spinner;
    MunicipalityStaffDTO staff;
    GcmDeviceDTO gcmDevice;
    MunicipalityDTO municipality;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(LOG, "#### onCreate");
        setContentView(R.layout.activity_signin);
        ctx = getApplicationContext();
        activity = this;

        municipality = SharedUtil.getMunicipality(ctx);
        registerGCMDevice();

        setFields();
        getEmail();
        municipality = SharedUtil.getMunicipality(ctx);
        int logo = getIntent().getIntExtra("logoImage", R.drawable.ic_action_globe);
        ActionBar actionBar = getSupportActionBar();
        Util.setCustomActionBar(ctx,
                actionBar,
                municipality.getMunicipalityName(),
                ctx.getResources().getDrawable(R.drawable.logo), logo);
        getSupportActionBar().setTitle("");
    }

    @Override
    public void onResume() {
        Log.w(LOG, "##### onResume");
        super.onResume();
    }


    private void setFields() {
        btnSend = (Button) findViewById(R.id.SIGNIN_btnSignin);
        spinner = (Spinner) findViewById(R.id.SIGNIN_emailSpinner);

        editPassword = (EditText) findViewById(R.id.SIGNIN_editPIN);
        progressBar = (ProgressBar) findViewById(R.id.SIGNIN_progress);
        heroImage = (ImageView) findViewById(R.id.SIGNIN_heroImage);
        txtWelcome = (TextView) findViewById(R.id.SIGNIN_welcome);
        logoImage = (ImageView) findViewById(R.id.SIGNIN_dome);

        logoImage.setImageDrawable(ctx.getResources().getDrawable(R.drawable.logo));
        handle = findViewById(R.id.SIGNIN_handle);
        progressBar.setVisibility(View.GONE);

        int h = Util.getWindowHeight(this);
        Log.w(LOG, "## window height: " + h);
        heroImage.getLayoutParams().height = h / 2;

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(btnSend, 200, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        sendSignIn();
                    }
                });
            }
        });

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        heroImage.setImageDrawable(SplashActivity.getImage(ctx));
                    }
                });

            }
        }, 1000, 5000);

    }

    private void sendSignIn() {

        if (editPassword.getText().toString().isEmpty()) {
            Util.showErrorToast(ctx, getString(R.string.enter_pswd));
            return;
        }
        if (email == null) {
            Util.showErrorToast(ctx, "Select the email account to sign in");
            return;
        }

        //Track Signin
        CityApplication ca = (CityApplication) getApplication();
        Tracker t = ca.getTracker(
                CityApplication.TrackerName.APP_TRACKER);
        t.setScreenName(SigninActivity.class.getSimpleName());
        t.send(new HitBuilders.ScreenViewBuilder().build());
        //
        RequestDTO w = new RequestDTO(RequestDTO.SIGN_IN_MUNICIPALITY_STAFF);
        w.setPassword(editPassword.getText().toString());
        w.setEmail(email);
        w.setGcmDevice(gcmDevice);
        w.setMunicipalityID(municipality.getMunicipalityID());

        progressBar.setVisibility(View.VISIBLE);
        NetUtil.sendRequest(ctx, w, new NetUtil.NetUtilListener() {
            @Override
            public void onResponse(final ResponseDTO resp) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        if (resp.getStatusCode() > 0) {
                            Util.showErrorToast(ctx, resp.getMessage());
                            return;
                        }
                        response = resp;
                        staff = response.getMunicipalityStaffList().get(0);

                        MunicipalityStaffDTO sp = new MunicipalityStaffDTO();
                        sp.setMunicipalityID(staff.getMunicipalityID());
                        sp.setMunicipalityStaffID(staff.getMunicipalityStaffID());
                        sp.setFirstName(staff.getFirstName());
                        sp.setLastName(staff.getLastName());
                        sp.setEmail(staff.getEmail());
                        sp.setPassword(staff.getPassword());
                        btnSend.setVisibility(View.GONE);

                        SharedUtil.saveMunicipalityStaff(ctx, sp);
                        CacheUtil.cacheLoginData(ctx, response, new CacheUtil.CacheListener() {
                            @Override
                            public void onDataCached() {
                                Intent i = new Intent(ctx, MainDrawerActivity.class);
                                startActivity(i);
                                onBackPressed();
                            }

                            @Override
                            public void onError() {

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Util.showErrorToast(ctx, "Problem saving data");
                                    }
                                });
                            }
                        });

                    }
                });
            }

            @Override
            public void onError(final String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Util.showErrorToast(ctx, message);
                    }
                });
            }

            @Override
            public void onWebSocketClose() {

            }
        });
    }

    @Override
    public void onBackPressed() {
        Log.w(LOG, "## onBackPressed");
        staff = SharedUtil.getMunicipalityStaff(ctx);
        if (staff != null) {
            setResult(RESULT_OK);
        } else {
            setResult(RESULT_CANCELED);
        }
        finish();
    }
    static final String LOG = SplashActivity.class.getSimpleName();
    public void getEmail() {
        AccountManager am = AccountManager.get(getApplicationContext());
        Account[] accts = am.getAccountsByType("com.google");
        if (accts.length == 0) {
            Util.showErrorToast(ctx, "No Accounts found. Please create one and try again");
            finish();
            return;
        }
        if (accts != null) {
            if (accts.length == 1) {
                email = accts[0].name;
                spinner.setVisibility(View.GONE);
                return;
            }
            tarList.add(getString(R.string.choose_email));
            for (int i = 0; i < accts.length; i++) {
                tarList.add(accts[i].name);
            }
            setSpinner();

        }

    }

    ArrayList<String> tarList = new ArrayList<String>();
    String email;

    private void setSpinner() {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ctx, R.layout.xxsimple_spinner_item, tarList);
        adapter.setDropDownViewResource(R.layout.xxsimple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    email = null;
                } else {
                    email = tarList.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_signin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        super.onPause();
    }

    private void registerGCMDevice() {

        Log.e(LOG, "############# Starting Google Cloud Messaging registration");
        GCMUtil.startGCMRegistration(ctx, new GCMUtil.GCMUtilListener() {
            @Override
            public void onDeviceRegistered(String id) {
                Log.e(LOG, "############# GCM - we cool, cool.....: " + id);
                gcmDevice = new GcmDeviceDTO();
                gcmDevice.setManufacturer(Build.MANUFACTURER);
                gcmDevice.setModel(Build.MODEL);
                gcmDevice.setSerialNumber(Build.SERIAL);
                gcmDevice.setAndroidVersion(Build.VERSION.RELEASE);
                gcmDevice.setGcmRegistrationID(id);

            }

            @Override
            public void onGCMError() {
                Log.e(LOG, "############# onGCMError --- we got GCM problems");

            }
        });

    }
}
//static final String url = "http://41.160.126.146/esbapi/V2/userlogin?username=7406190168080&password=vatawa"
//        + "&latitude=-29.859701442126745&longitude=31.014404296875 ";