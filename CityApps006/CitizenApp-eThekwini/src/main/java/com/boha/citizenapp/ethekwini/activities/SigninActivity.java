package com.boha.citizenapp.ethekwini.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.boha.citizenapp.ethekwini.R;
import com.boha.library.activities.CityApplication;
import com.boha.library.dto.GcmDeviceDTO;
import com.boha.library.dto.MunicipalityDTO;
import com.boha.library.dto.ProfileInfoDTO;
import com.boha.library.dto.UserDTO;
import com.boha.library.transfer.RequestDTO;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.CacheUtil;
import com.boha.library.util.GCMUtil;
import com.boha.library.util.NetUtil;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.ThemeChooser;
import com.boha.library.util.Util;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This activity serves as the entry point to the app. It manages
 * the user signin; checks whether the app has just been installed
 * If new, the acticity provides the UI to accept credentials and send
 * them to the back-end.
 *
 * If already signed in, the activity passes control to the main activity
 * in the app; CitizenDrawerActivity
 */
public class SigninActivity extends AppCompatActivity {

    ImageView heroImage;
    Timer timer;
    TextView txtWelcome;
    View handle, editView;
    RadioButton radioYes, radioNo, radioTourist;
    Context ctx;
    Activity activity;
    Button btnSend;
    EditText editID, editPassword;
    static final String LOG = SigninActivity.class.getSimpleName();
    ResponseDTO response;
    ProfileInfoDTO profileInfo;
    Spinner spinner;
    GcmDeviceDTO gcmDevice;
    MunicipalityDTO municipality;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(LOG, "#### onCreate");
        ThemeChooser.setTheme(this);
        setContentView(R.layout.activity_signin);
        ctx = getApplicationContext();
        activity = this;

        municipality = SharedUtil.getMunicipality(ctx);
        int logo = getIntent().getIntExtra("logo", R.drawable.ic_action_globe);
        registerGCMDevice();

        setFields();
        getEmail();

        ActionBar actionBar = getSupportActionBar();
        Util.setCustomActionBar(ctx,
                actionBar,
                municipality.getMunicipalityName(),
                ctx.getResources().getDrawable(R.drawable.logo), logo);
        getSupportActionBar().setTitle("");
        //Track Signin
        CityApplication ca = (CityApplication) getApplication();
        Tracker t = ca.getTracker(
                CityApplication.TrackerName.APP_TRACKER);
        t.setScreenName(SigninActivity.class.getSimpleName());
        t.send(new HitBuilders.ScreenViewBuilder().build());
        //
    }

    @Override
    public void onResume() {
        Log.w(LOG, "##### onResume");
        super.onResume();
    }


    private void setFields() {
        editView = findViewById(R.id.SIGNIN_editLayout);
        radioNo = (RadioButton) findViewById(R.id.SIGNIN_radioNo);
        radioNo.setVisibility(View.GONE);
        radioYes = (RadioButton) findViewById(R.id.SIGNIN_radioYes);
        radioTourist = (RadioButton) findViewById(R.id.SIGNIN_radioTourist);
        btnSend = (Button) findViewById(R.id.SIGNIN_btnSignin);
        editID = (EditText) findViewById(R.id.SIGNIN_editUserID);
        spinner = (Spinner) findViewById(R.id.SIGNIN_emailSpinner);
        spinner.setVisibility(View.GONE);

        editPassword = (EditText) findViewById(R.id.SIGNIN_editPIN);
        heroImage = (ImageView) findViewById(R.id.SIGNIN_heroImage);
        txtWelcome = (TextView) findViewById(R.id.SIGNIN_welcome);
        handle = findViewById(R.id.SIGNIN_handle);

        editID.setHint(getString(R.string.idnumber));

//        editID.setVisibility(View.GONE);
//        spinner.setVisibility(View.GONE);
//        editPassword.setVisibility(View.GONE);
//        btnSend.setEnabled(false);
//        btnSend.setAlpha(0.3f);

        btnSend.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           Util.flashOnce(btnSend, 200, new Util.UtilAnimationListener() {
                                                       @Override
                                                       public void onAnimationEnded() {
                                                           btnSend.setEnabled(false);
                                                           if (radioNo.isChecked()) {
                                                               sendSignInUser();
                                                           }
                                                           if (radioYes.isChecked()) {
                                                               sendSignInCitizen();
                                                           }
                                                           if (radioTourist.isChecked()) {
                                                               sendSignInTourist();
                                                           }

                                                       }
                                                   }

                                           );
                                       }
                                   }

        );

        timer = new

                Timer();

        timer.scheduleAtFixedRate(new

                                          TimerTask() {
                                              @Override
                                              public void run() {
                                                  runOnUiThread(new Runnable() {
                                                      @Override
                                                      public void run() {
                                                          heroImage.setImageDrawable(SplashActivity.getImage(0));
                                                      }
                                                  });

                                              }
                                          }

                , 1000, 5000);

        radioNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()

                                           {
                                               @Override
                                               public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                   if (isChecked) {
                                                       userType = SharedUtil.CITIZEN_NO_ACCOUNT;
                                                       editID.setVisibility(View.GONE);
                                                       spinner.setVisibility(View.GONE);
                                                       editPassword.setVisibility(View.VISIBLE);
                                                       btnSend.setEnabled(true);
                                                   }
                                               }
                                           }

        );
        radioYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()

                                            {
                                                @Override
                                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                    if (isChecked) {
                                                        userType = SharedUtil.CITIZEN_WITH_ACCOUNT;
                                                        editID.setVisibility(View.VISIBLE);
                                                        editID.setHint("ID Number");
                                                        spinner.setVisibility(View.GONE);
                                                        editPassword.setVisibility(View.VISIBLE);
                                                        btnSend.setEnabled(true);
                                                    }
                                                }
                                            }

        );
        radioTourist.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()

                                                {
                                                    @Override
                                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                        if (isChecked) {
                                                            userType = SharedUtil.TOURIST_VISITOR;
                                                            editID.setVisibility(View.GONE);
                                                            spinner.setVisibility(View.GONE);
                                                            editPassword.setVisibility(View.GONE);
                                                            btnSend.setEnabled(true);
                                                        }
                                                    }
                                                }

        );


    }

    private int userType;

    public void sendSignInCitizen() {

        Snackbar.make(editPassword, "Downloading information; may take a minute or two",
                Snackbar.LENGTH_LONG).show();
        if (editID.getText().toString().isEmpty()) {
            Util.showErrorToast(ctx, getString(R.string.enter_email));
            return;
        }
        if (editPassword.getText().toString().isEmpty()) {
            Util.showErrorToast(ctx, getString(R.string.enter_pswd));
            return;
        }
        if (email == null) {
            if (tarList.size() > 1) {
                email = tarList.get(1);
            }
        }

        RequestDTO w = new RequestDTO(RequestDTO.SIGN_IN_CITIZEN);
        w.setUserName(editID.getText().toString());
        w.setPassword(editPassword.getText().toString());
        w.setEmail(email);
        w.setGcmDevice(gcmDevice);
        w.setLatitude(0.0);
        w.setLongitude(0.0);
        w.setMunicipalityID(municipality.getMunicipalityID());

        setRefreshActionButtonState(true);
        btnSend.setEnabled(false);
        NetUtil.sendRequest(ctx, w, new NetUtil.NetUtilListener() {
            @Override
            public void onResponse(final ResponseDTO resp) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setRefreshActionButtonState(false);
                        btnSend.setEnabled(true);
                        if (resp.isMunicipalityAccessFailed()) {
                            if (resp.getProfileInfoList() == null || resp.getProfileInfoList().isEmpty()) {
                                Util.showErrorToast(ctx, getString(R.string.services_not_available));
//                                finish();
                                return;
                            } else {
                                Util.showErrorToast(ctx, getString(com.boha.library.R.string.unable_connect_muni));
                            }
                        }
                        response = resp;
                        if (response.getProfileInfoList() != null && !response.getProfileInfoList().isEmpty()) {
                            profileInfo = response.getProfileInfoList().get(0);

                            ProfileInfoDTO sp = new ProfileInfoDTO();
                            sp.setProfileInfoID(profileInfo.getProfileInfoID());
                            sp.setFirstName(profileInfo.getFirstName());
                            sp.setLastName(profileInfo.getLastName());
                            sp.setiDNumber(profileInfo.getiDNumber());
                            sp.setPassword(profileInfo.getPassword());

                            SharedUtil.saveProfile(ctx, sp);
                            SharedUtil.setUserType(ctx, userType);
                            SharedUtil.saveGCMDevice(ctx,gcmDevice);
                            CacheUtil.cacheLoginData(ctx, response, new CacheUtil.CacheListener() {
                                @Override
                                public void onDataCached() {
                                    onBackPressed();
                                  //  check();
                                }

                                @Override
                                public void onError() {
                                    Util.showErrorToast(ctx, "Problem saving data");
                                }
                            });
                        } else {
                            Gson gson = new Gson();
                            Log.e(LOG, "-- sendSignInCitizen - some kind of error, json from server: " + gson.toJson(resp));
                        }

                    }
                });
            }

            @Override
            public void onError(final String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setRefreshActionButtonState(false);
                        btnSend.setEnabled(true);
                        Util.showErrorToast(ctx, message);
                    }
                });
            }

            @Override
            public void onWebSocketClose() {

            }
        });
    }


    public void sendSignInUser() {

        if (email == null) {
            if (tarList.size() > 1) {
                email = tarList.get(1);
            }
        }

        RequestDTO w = new RequestDTO(RequestDTO.SIGN_IN_USER);
        UserDTO u = new UserDTO();
        u.setMunicipalityID(municipality.getMunicipalityID());
        u.setEmail(email);
        u.setGcmDevice(gcmDevice);
        w.setUser(u);
        w.setMunicipalityID(municipality.getMunicipalityID());

        setRefreshActionButtonState(true);
        NetUtil.sendRequest(ctx, w, new NetUtil.NetUtilListener() {
            @Override
            public void onResponse(final ResponseDTO resp) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setRefreshActionButtonState(false);
                        if (resp.isMunicipalityAccessFailed()) {
                            Util.showErrorToast(ctx, ctx.getString(com.boha.library.R.string.unable_connect_muni));
                            if (response.getUserList() == null || response.getUserList().isEmpty()) {
                                return;
                            }
                        }
                        response = resp;

                        SharedUtil.saveUser(ctx, response.getUserList().get(0));
                        SharedUtil.setUserType(ctx, userType);
                        CacheUtil.cacheLoginData(ctx, response, new CacheUtil.CacheListener() {
                            @Override
                            public void onDataCached() {
                                 onBackPressed();
                              //  check();
                            }

                            @Override
                            public void onError() {
                                Util.showErrorToast(ctx, "Problem saving data");
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
                        setRefreshActionButtonState(false);
                        btnSend.setEnabled(true);
                        Util.showErrorToast(ctx, message);
                    }
                });
            }

            @Override
            public void onWebSocketClose() {

            }
        });
    }

    String mail = "";

    //Temporary Fix
    public void sendSignInTourist() {
        Snackbar.make(editPassword, "Downloading information; may take a minute or two",
                Snackbar.LENGTH_LONG).show();

        if (email == null) {
            if (tarList.size() > 1) {
                email = tarList.get(1);
            }
        }

        RequestDTO w = new RequestDTO(RequestDTO.SIGN_IN_CITIZEN);
        w.setUserName("4406230441086");
        w.setPassword("Jer3m1ah3");
        w.setEmail(email);
        w.setGcmDevice(gcmDevice);
        w.setLatitude(0.0);
        w.setLongitude(0.0);
        w.setMunicipalityID(municipality.getMunicipalityID());

        setRefreshActionButtonState(true);
        btnSend.setEnabled(false);
        NetUtil.sendRequest(ctx, w, new NetUtil.NetUtilListener() {
            @Override
            public void onResponse(final ResponseDTO resp) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setRefreshActionButtonState(false);
                        btnSend.setEnabled(true);
                        if (resp.isMunicipalityAccessFailed()) {

                            if (resp.getProfileInfoList() == null || resp.getProfileInfoList().isEmpty()) {
                                Util.showErrorToast(ctx, getString(R.string.services_not_available));
//                                finish();
                                return;
                            }else {
                                Util.showErrorToast(ctx, getString(com.boha.library.R.string.unable_connect_muni));
                            }
                        }

                        response = resp;
                   //     if (response.getProfileInfoList() != null && !response.getProfileInfoList().isEmpty()) {
                   //         profileInfo = response.getProfileInfoList().get(0);

                   //         ProfileInfoDTO sp = new ProfileInfoDTO();
                   //         sp.setProfileInfoID(profileInfo.getProfileInfoID());
                            //sp.setFirstName("eThekwini"/*profileInfo.getFirstName()*/);
                           // sp.setLastName("Visitor"/*profileInfo.getLastName()*/);
                   //         sp.setiDNumber("3702210039184"/*profileInfo.getiDNumber()*/);
                   //         sp.setPassword("alex66"/*profileInfo.getPassword()*/);

                         //   SharedUtil.saveProfile(ctx, sp);
                         //   SharedUtil.setUserType(ctx, userType);
                            SharedUtil.saveGCMDevice(ctx,gcmDevice);
                            CacheUtil.cacheLoginData(ctx, response, new CacheUtil.CacheListener() {
                                @Override
                                public void onDataCached() {
                                    // onBackPressed();
                                  //  check();
                                    TouristCheck();
                                }

                                @Override
                                public void onError() {
                                    Util.showErrorToast(ctx, "Problem saving data");
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
                        setRefreshActionButtonState(false);
                        btnSend.setEnabled(true);
                        Util.showErrorToast(ctx, message);
                    }
                });
            }

            @Override
            public void onWebSocketClose() {

            }
        });
    }

    private void check() {
        profileInfo = SharedUtil.getProfile(ctx);
        UserDTO user = SharedUtil.getUser(ctx);
        if (profileInfo != null || user != null) {
            setResult(RESULT_OK);
            finish();
            Intent i = new Intent(ctx, CitizenDrawerActivity.class);
            i.putExtra("justSignedIn", true);
            startActivity(i);
        } else {
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    private void TouristCheck() {

        Intent i = new Intent(ctx, TouristDrawerActivity.class);
        i.putExtra("justSignedIn", true);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
    Log.w(LOG, "#### onBackPressed");
        profileInfo = SharedUtil.getProfile(ctx);
        UserDTO user = SharedUtil.getUser(ctx);
        if (profileInfo != null || user != null) {
            setResult(RESULT_OK);
            finish();
            Intent i = new Intent(ctx, CitizenDrawerActivity.class);
            i.putExtra("justSignedIn", true);
            startActivity(i);
        } else {
            setResult(RESULT_CANCELED);
            finish();
        }
       /* Intent intent = new Intent(SigninActivity.this, SplashActivity.class);
        startActivity(intent);
        finish(); */
    }

    public void getEmail() {
        AccountManager am = AccountManager.get(getApplicationContext());
        Account[] accts = am.getAccounts();
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
            tarList.add("Select account for communications");
            for (int i = 0; i < accts.length; i++) {
                tarList.add(accts[i].name);
            }
            setSpinner();

        }

    }

    ArrayList<String> tarList = new ArrayList<String>();
    String email;
    Menu mMenu;

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
          //  getMenuInflater().inflate(R.menu.menu_main_pager, menu);
         //  menu.getItem(0).setVisible(false);
        //  mMenu = menu;

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

      /*  if (id == R.id.action_help) {
            Util.showToast(ctx, getString(R.string.under_cons));
            return true;
        } */

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

    public void setRefreshActionButtonState(final boolean refreshing) {
        if (mMenu != null) {
            final MenuItem refreshItem = mMenu.findItem(com.boha.library.R.id.action_help);
            if (refreshItem != null) {
                if (refreshing) {
                    refreshItem.setActionView(com.boha.library.R.layout.action_bar_progess);
                } else {
                    refreshItem.setActionView(null);
                }
            }
        }
    }
}
//static final String url = "http://41.160.126.146/esbapi/V2/userlogin?username=7406190168080&password=vatawa"
//        + "&latitude=-29.859701442126745&longitude=31.014404296875 ";