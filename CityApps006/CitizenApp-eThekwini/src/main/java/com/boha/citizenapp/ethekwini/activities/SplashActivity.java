package com.boha.citizenapp.ethekwini.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.boha.citizenapp.ethekwini.R;
import com.boha.library.dto.MunicipalityDTO;
import com.boha.library.dto.ProfileInfoDTO;
import com.boha.library.dto.UserDTO;
import com.boha.library.services.GCMDeviceService;
import com.boha.library.transfer.RequestDTO;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.CityImages;
import com.boha.library.util.NetUtil;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.ThemeChooser;
import com.boha.library.util.Util;
import com.google.firebase.crash.FirebaseCrash;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    ProfileInfoDTO profile;
    Timer timer;
    ImageView heroImage, logo;
    View actionsView;
    Button btnSignIn, btnRegister;
    static Context ctx;
    ProgressBar progressBar;
    UserDTO user;
    MunicipalityDTO municipality;
    static final Random RANDOM = new Random(System.currentTimeMillis());
    static final int ONE_SECOND = 1000, QUICK = 200, SECONDS_TO_WAIT = ONE_SECOND * 4;
    static final String TAG = SplashActivity.class.getSimpleName();
    //TODO - customize the app for each Municipality
    /**
     * This name identifies the app; the  name must be as recorded in the database
     * Each municipality will have their own version of the app.
     * The list of splash images should be unique to each municipality
     */
    static final String MUNICIPALITY_NAME = "eThekwini";
    int themeDarkColor, themePrimaryColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ThemeChooser.setTheme(this);
        setContentView(R.layout.activity_splash);
        ctx = getApplicationContext();
        setFields();
        Resources.Theme theme = getTheme();
        TypedValue typedValue = new TypedValue();
        theme.resolveAttribute(com.boha.library.R.attr.colorPrimaryDark, typedValue, true);
        themeDarkColor = typedValue.data;
        theme.resolveAttribute(com.boha.library.R.attr.colorPrimary, typedValue, true);
        themePrimaryColor = typedValue.data;


        //eThekwini logo - will be different for each municipality
        logo.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.logo));
        setTitle(MUNICIPALITY_NAME + " SmartCity");
        ActionBar actionBar = getSupportActionBar();
        Util.setCustomActionBar(ctx,
                actionBar,
                MUNICIPALITY_NAME,
                ContextCompat.getDrawable(ctx, R.drawable.logo), R.drawable.logo);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(themeDarkColor);
            window.setNavigationBarColor(themeDarkColor);
        }
        getMunicipality();
        //Track analytics
//        CityApplication ca = (CityApplication) getApplication();
//        Tracker t = ca.getTracker(
//                CityApplication.TrackerName.APP_TRACKER);
//        t.setScreenName(SplashActivity.class.getSimpleName());
//        t.send(new HitBuilders.ScreenViewBuilder().build());


    }

    private void setFields() {
        actionsView = findViewById(R.id.SPLASH_actions);
        actionsView.setVisibility(View.GONE);
        heroImage = (ImageView) findViewById(R.id.SPLASH_image);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        logo = (ImageView) findViewById(R.id.SPLASH_logo);
        btnRegister = (Button) findViewById(R.id.SPLASH_btnRegister);
        btnSignIn = (Button) findViewById(R.id.SPLASH_btnSignin);
        progressBar.setVisibility(View.GONE);

        btnRegister.setVisibility(View.GONE);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(btnRegister, QUICK, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        Intent intent = new Intent(ctx, RegistrationActivity.class);
                        intent.putExtra("logo", R.drawable.logo);
                        startActivityForResult(intent, REQUEST_SIGN_IN);
                    }
                });

            }
        });
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(btnSignIn, QUICK, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        Intent intent = new Intent(ctx, SigninActivity.class);
                        intent.putExtra("logo", R.drawable.logo);

                        startActivityForResult(intent, REQUEST_SIGN_IN);
                    }
                });
            }
        });


        heroImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkVirginity();
            }
        });

        if (SharedUtil.getProfile(ctx) != null) {
            btnSignIn.setVisibility(View.GONE);
            btnRegister.setVisibility(View.GONE);
        }

        if (SharedUtil.getUser(ctx) != null) {
            btnSignIn.setVisibility(View.GONE);
            btnRegister.setVisibility(View.GONE);
        }

    }


    @Override
    public void onActivityResult(int reqCode, int resCode, Intent data) {
        Log.d(TAG, "##------> onActivityResult reqCode: "
                + reqCode + " resCode: " + resCode);
        switch (reqCode) {
            case REQUEST_SIGN_IN:

                Intent i = new Intent(ctx, CitizenDrawerActivity.class);
                i.putExtra("justSignedIn", true);
                startActivity(i);

                break;
            case REQUEST_THEME_CHANGE:
                finish();
                Intent w = new Intent(this, SplashActivity.class);
                startActivity(w);

                break;
        }
    }

    private void getMunicipality() {
        Log.d(TAG, ".........getMunicipality: ");
        municipality = SharedUtil.getMunicipality(ctx);

        if (municipality == null) {
            RequestDTO w = new RequestDTO(RequestDTO.GET_MUNICIPALITY_BY_NAME);
            w.setMunicipalityName(MUNICIPALITY_NAME);
            setProgressDialog();
            NetUtil.sendRequest(ctx, w, new NetUtil.NetUtilListener() {
                @Override
                public void onResponse(final ResponseDTO response) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            progressDialog.dismiss();
                            if (response.getStatusCode() == 0) {
                                municipality = response.getMunicipalityList().get(0);
                                SharedUtil.saveMunicipality(ctx, municipality);
                                Util.expand(actionsView, ONE_SECOND, null);
                            } else {
                                FirebaseCrash.report(new Exception("Municipality cannot be determined"));
                            }
                        }
                    });
                }

                @Override
                public void onError(final String message) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            FirebaseCrash.report(new Exception("Municipality cannot be determined"));
                            Util.showSnackBar(heroImage,message,"OK", Color.parseColor("RED"));
                        }
                    });
                }

                @Override
                public void onWebSocketClose() {

                }
            });

        } else {
            Log.i(TAG, "Municipality record found: " + municipality.getMunicipalityName());
            checkVirginity();
        }
    }

    private void checkVirginity() {
        if (SharedUtil.getRegistrationID(ctx) == null) {
            Intent intent = new Intent(ctx, GCMDeviceService.class);
            intent.putExtra("profile", profile);
            Log.w(TAG, "GCMDeviceService starting ....");
            startService(intent);
        }
        profile = SharedUtil.getProfile(ctx);
        user = SharedUtil.getUser(ctx);
        if (profile != null) {
            startTimer();
            return;
        }
        if (user != null) {
            startTimer();
            return;
        }

        Util.expand(actionsView, ONE_SECOND, null);
    }


    private void startTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timer.purge();
                timer.cancel();
                Intent m = new Intent(getApplicationContext(), CitizenDrawerActivity.class);
                startActivity(m);
                finish();
            }
        }, SECONDS_TO_WAIT);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


    /*    if (id == R.id.action_logoff) {
            finish();
            return true;
        }
        if (id == R.id.action_help) {
            Util.showToast(ctx, ctx.getString(R.string.under_cons));
            return true;
        }
        if (id == R.id.action_theme) {
            Intent w = new Intent(this, ThemeSelectorActivity.class);
            w.putExtra("darkColor",themeDarkColor);
            startActivityForResult(w,REQUEST_THEME_CHANGE);
            return true;
        }
        if (id == R.id.action_afrikaans) {
            return true;
        }
        if (id == R.id.action_zulu) {
            return true;
        }
        if (id == R.id.action_english) {
            return true;
        }
        if (id == R.id.action_french) {
            return true;
        }
        if (id == R.id.action_german) {
            return true;
        }
        if (id == R.id.action_portuguese) {
            return true;
        } */

        return super.onOptionsItemSelected(item);
    }


    ProgressDialog progressDialog;

    void setProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Municipality Checkin");
        progressDialog.setMessage("Finding your municipality....");
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

    }

    static final int REQUEST_SIGN_IN = 9033,
            REQUEST_THEME_CHANGE = 1782,
            NUMBER_OF_IMAGES = 5;


    @Override
    public void onResume() {
        super.onResume();
        Log.e("SplashActivity", "### onResume");
        if (SharedUtil.getProfile(ctx) != null) {
            btnSignIn.setVisibility(View.GONE);
            btnRegister.setVisibility(View.GONE);
        }
        if (SharedUtil.getUser(ctx) != null) {
            btnSignIn.setVisibility(View.GONE);
            btnRegister.setVisibility(View.GONE);
        }

    }

    @Override
    public void onPause() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        super.onPause();
    }

    static CityImages cityImages;

    public static Drawable getImage(int index) {
        if (cityImages == null) {
            getLocalCityImages();
        }
        return cityImages.getImage(ctx, index);
    }

    //todo - download new, improved images in background if available.
    private static void getLocalCityImages() {

        if (SharedUtil.getCityImages(ctx) != null) {
            cityImages = SharedUtil.getCityImages(ctx);
            return;
        }

        cityImages = new CityImages();
        int[] imageResourceIDs = new int[NUMBER_OF_IMAGES];
        imageResourceIDs[0] = R.drawable.c1;
        imageResourceIDs[1] = R.drawable.c2;
        imageResourceIDs[2] = R.drawable.c3;
        imageResourceIDs[3] = R.drawable.c4;
        imageResourceIDs[4] = R.drawable.c5;
        cityImages.setImageResourceIDs(imageResourceIDs);
        SharedUtil.setCityImages(ctx, cityImages);

    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
