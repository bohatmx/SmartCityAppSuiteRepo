package com.boha.citizenapp.ethekwini.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.boha.citizenapp.ethekwini.R;
import com.boha.library.activities.PickSuburbActivity;
import com.boha.library.dto.MunicipalityDTO;
import com.boha.library.dto.ProfileInfoDTO;
import com.boha.library.dto.UserDTO;
import com.boha.library.transfer.RequestDTO;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.CacheUtil;
import com.boha.library.util.NetUtil;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.ThemeChooser;
import com.boha.library.util.Util;
import com.boha.library.util.WebCheck;
import com.boha.library.util.WebCheckResult;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import java.util.Timer;

/**
 * This activity serves as the entry point to the app. It manages
 * the user signin; checks whether the app has just been installed
 * If new, the acticity provides the UI to accept credentials and send
 * them to the back-end.
 * <p>
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
    Button btnSend, btnTourist;
    EditText editEmail, editPassword;
    static final String LOG = SigninActivity.class.getSimpleName();
    ResponseDTO response;
    ProfileInfoDTO profileInfo;
    MunicipalityDTO municipality;
    int userType;
    FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(LOG, "#### onCreate");
        ThemeChooser.setTheme(this);
        setContentView(R.layout.activity_signin);
        ctx = getApplicationContext();
        activity = this;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        municipality = SharedUtil.getMunicipality(ctx);
        int logo = getIntent().getIntExtra("logo", R.drawable.ic_action_globe);

        setFields();
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
        editView = findViewById(R.id.SIGNIN_editLayout);
        radioNo = (RadioButton) findViewById(R.id.SIGNIN_radioNo);
        radioNo.setVisibility(View.GONE);
        radioYes = (RadioButton) findViewById(R.id.SIGNIN_radioYes);
        radioTourist = (RadioButton) findViewById(R.id.SIGNIN_radioTourist);
        radioTourist.setVisibility(View.GONE);
        btnSend = (Button) findViewById(R.id.SIGNIN_btnSignin);
        editEmail = (EditText) findViewById(R.id.SIGNIN_editEmail);

        editPassword = (EditText) findViewById(R.id.SIGNIN_editPIN);
        heroImage = (ImageView) findViewById(R.id.SIGNIN_heroImage);
        handle = findViewById(R.id.SIGNIN_handle);

        btnTourist = (Button) findViewById(R.id.SIGNIN_btnTourist);
        btnTourist.setVisibility(View.GONE);

        btnSend.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {

                                           btnSend.setEnabled(false);
                                           hideKeyboard();
                                           switch (userType) {
                                               case SharedUtil.CITIZEN_WITH_ACCOUNT:
                                                   sendSignInCitizen();
                                                   break;
                                               case SharedUtil.CITIZEN_NO_ACCOUNT:
                                                   sendSignInUser();
                                                   break;
                                           }

                                       }
                                   }

        );
        btnTourist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SigninActivity.this, TouristDrawerActivity.class);
                startActivity(intent);
            }
        });
        userType = SharedUtil.CITIZEN_WITH_ACCOUNT;
       // editEmail.setHint(R.string.enter_email);
      // editPassword.setVisibility(View.VISIBLE);
        radioNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                userType = SharedUtil.CITIZEN_NO_ACCOUNT;
                editEmail.setHint("Optional Email Address");
                editPassword.setVisibility(View.GONE);
            }
        });
        radioYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editEmail.setVisibility(View.GONE);
                editPassword.setVisibility(View.GONE);
                btnSend.setVisibility(View.GONE);
                btnTourist.setVisibility(View.VISIBLE);
               // userType = SharedUtil.CITIZEN_WITH_ACCOUNT;

                //editEmail.setHint(R.string.enter_email);
            }
        });
        radioTourist.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editEmail.setVisibility(View.VISIBLE);
                editPassword.setVisibility(View.VISIBLE);
                btnTourist.setVisibility(View.GONE);
                btnSend.setVisibility(View.VISIBLE);

            }
        });


    }

    public void sendSignInCitizen() {

        WebCheckResult wcr = WebCheck.checkNetworkAvailability(this);
        if (!wcr.isWifiConnected() && !wcr.isMobileConnected()) {
            Util.showSnackBar(editPassword,"You are currently not connected to the network","OK", Color.parseColor("red"));
            return;
        }
        final long start = System.currentTimeMillis();
        Snackbar.make(editPassword, "Downloading information; may take a minute or two",
                Snackbar.LENGTH_LONG).show();
        if (editEmail.getText().toString().isEmpty()) {
            Util.showErrorToast(ctx, getString(R.string.enter_email));
            return;
        }
        if (editPassword.getText().toString().isEmpty()) {
            Util.showErrorToast(ctx, getString(R.string.enter_pswd));
            return;
        }

        RequestDTO w = new RequestDTO(RequestDTO.SIGN_IN_CITIZEN);
        w.setUserName(editEmail.getText().toString());
        w.setPassword(editPassword.getText().toString());
        w.setEmail(editEmail.getText().toString());
        w.setGcmDevice(SharedUtil.getGCMDevice(this));

        w.setLatitude(0.0);
        w.setLongitude(0.0);
        w.setMunicipalityID(municipality.getMunicipalityID());

        //todo reset after testing complete
        w.setSpoof(false);
        //

        btnSend.setEnabled(false);
        setProgressDialog();
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "signIN");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Citizen Sign In");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);

        NetUtil.sendRequest(ctx, w, new NetUtil.NetUtilListener() {
            @Override
            public void onResponse(final ResponseDTO resp) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        long end = System.currentTimeMillis();
                        Log.e(LOG, "sendSignInCitizen: elapsed seconds : " + ((end-start)/1000) + " seconds");
                        progressDialog.dismiss();
                        btnSend.setEnabled(true);
                        if (resp.isMunicipalityAccessFailed()) {
                            if (resp.getProfileInfoList() == null || resp.getProfileInfoList().isEmpty()) {
                                Util.showErrorToast(ctx, getString(R.string.services_not_available));
                                return;
                            } else {
                                Util.showErrorToast(ctx, getString(com.boha.library.R.string.unable_connect_muni));
                            }
                        }
                        response = resp;
                        if (response.getProfileInfoList() != null && !response.getProfileInfoList().isEmpty()) {
                            profileInfo = response.getProfileInfoList().get(0);


                            SharedUtil.saveProfile(ctx, profileInfo);
                            SharedUtil.setUserType(ctx, userType);
                            MunicipalityDTO mu = SharedUtil.getMunicipality(getApplicationContext());
                            FirebaseMessaging.getInstance().subscribeToTopic(
                                    "general-" + mu.getMunicipalityName().replaceAll(" ",""));

                            Log.w(LOG, "Subscribed to topic: general-" + mu.getMunicipalityName().replaceAll(" ",""));
                            CacheUtil.cacheLoginData(ctx, response, new CacheUtil.CacheListener() {
                                @Override
                                public void onDataCached() {
                                    Log.e(LOG, "cacheLoginData..... onDataCached: " + profileInfo.getFirstName() + " " + profileInfo.getLastName());
                                  //  onBackPressed();
                                    profileInfo = SharedUtil.getProfile(ctx);
                                    UserDTO user = SharedUtil.getUser(ctx);
                                    if (profileInfo != null) {
                                        selectSuburb();
                                    }
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
                        progressDialog.dismiss();
                        btnSend.setEnabled(true);
                        Log.e(LOG, message);
                       // Util.showToast(ctx, "Incorrect email/password");
                        Util.showSnackBar(editPassword, getString(com.boha.library.R.string.incorrect_credentials),
                                "Dismiss", Color.parseColor("red"));

                        //   Util.showErrorToast(ctx, message);
                    }
                });
            }

            @Override
            public void onWebSocketClose() {

            }
        });
    }

    public static final int PICK_SUBURB = 917;
    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        switch (reqCode) {
            case PICK_SUBURB:
                Intent intent = new Intent(SigninActivity.this, CitizenDrawerActivity.class);
                startActivity(intent);
                break;
        }
    }
    public void sendSignInUser() {


        RequestDTO w = new RequestDTO(RequestDTO.SIGN_IN_USER);
        UserDTO u = new UserDTO();
        u.setMunicipalityID(municipality.getMunicipalityID());
        u.setEmail(editEmail.getText().toString());
        u.setGcmDevice(SharedUtil.getGCMDevice(this));
        w.setUser(u);
        w.setMunicipalityID(municipality.getMunicipalityID());

        setProgressDialog();
        NetUtil.sendRequest(ctx, w, new NetUtil.NetUtilListener() {
            @Override
            public void onResponse(final ResponseDTO resp) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        if (resp.isMunicipalityAccessFailed()) {
                            Util.showErrorToast(ctx, ctx.getString(com.boha.library.R.string.unable_connect_muni));
                            if (response.getUserList() == null || response.getUserList().isEmpty()) {
                                return;
                            }
                        }
                        response = resp;

                        SharedUtil.saveUser(ctx, response.getUserList().get(0));
                        SharedUtil.setUserType(ctx, userType);
                        MunicipalityDTO mu = SharedUtil.getMunicipality(getApplicationContext());
                        FirebaseMessaging.getInstance().subscribeToTopic(
                                "general-" + mu.getMunicipalityName().replaceAll(" ",""));

                        Log.w(LOG, "Subscribed to topic: general-" + mu.getMunicipalityName().replaceAll(" ",""));
                        CacheUtil.cacheLoginData(ctx, response, new CacheUtil.CacheListener() {
                            @Override
                            public void onDataCached() {
                                selectSuburb();
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
                        progressDialog.dismiss();
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

    private void selectSuburb() {
        Intent intent = new Intent(SigninActivity.this, PickSuburbActivity.class);
        startActivityForResult(intent, PICK_SUBURB);
    }

    void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editEmail.getWindowToken(), 0);
    }
    @Override
    public void onBackPressed() {
        Log.w(LOG, "#### onBackPressed");
        profileInfo = SharedUtil.getProfile(ctx);
        UserDTO user = SharedUtil.getUser(ctx);
        if (profileInfo != null || user != null) {
            setResult(RESULT_OK);
            finish();

        } else {
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    ProgressDialog progressDialog;
    void setProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Signing In");
        progressDialog.setMessage("eThekwini Services is signing you in ...");
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

    }

    Menu mMenu;


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



}
