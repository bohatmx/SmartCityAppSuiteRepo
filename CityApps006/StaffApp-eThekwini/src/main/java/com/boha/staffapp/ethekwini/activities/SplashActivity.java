package com.boha.staffapp.ethekwini.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
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

import com.boha.library.activities.ThemeSelectorActivity;
import com.boha.library.dto.MunicipalityDTO;
import com.boha.library.dto.MunicipalityStaffDTO;
import com.boha.library.services.GCMDeviceService;
import com.boha.library.transfer.RequestDTO;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.CityImages;
import com.boha.library.util.NetUtil;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.ThemeChooser;
import com.boha.library.util.Util;
import com.boha.staffapp.ethekwini.R;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends ActionBarActivity {

    MunicipalityStaffDTO staff;
    Timer timer;
    ImageView heroImage, logoImage;
    View actionsView;
    Button btnSignIn;
    static Context ctx;
    ProgressBar progressBar;
    MunicipalityDTO municipality;
    int themeDarkColor, themePrimaryColor;
    static final Random RANDOM = new Random(System.currentTimeMillis());
    static final int ONE_SECOND = 1000, QUICK = 200, FIVE_SECONDS = ONE_SECOND * 5;
    static final String LOG = SplashActivity.class.getSimpleName();
    //TODO - customize the app for each Municipality
    /**
     * This name identifies the app; the  name must be as recorded in the database
     * Each municipality will have their own version of the app.
     * The list of splash images should be unique to each municipality
     */
    static final String MUNICIPALITY_NAME = "eThekwini";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w("Splash", "## onCreate");
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

        //eThekwini logoImage - will be different for each municipality
        logoImage.setImageDrawable(ctx.getResources().getDrawable(R.drawable.logo));
        setTitle(MUNICIPALITY_NAME + " SmartCity");
        startTimer();
        getMunicipality();

        municipality = SharedUtil.getMunicipality(ctx);
        staff = SharedUtil.getMunicipalityStaff(ctx);

        ActionBar actionBar = getSupportActionBar();
        Util.setCustomActionBar(ctx,
                actionBar,
                MUNICIPALITY_NAME,
                ctx.getResources().getDrawable(R.drawable.logo), R.drawable.logo);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(themeDarkColor);
            window.setNavigationBarColor(themeDarkColor);
        }
    }

    private void setFields() {
        actionsView = findViewById(R.id.SPLASH_actions);
        actionsView.setVisibility(View.GONE);
        heroImage = (ImageView) findViewById(R.id.SPLASH_image);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        logoImage = (ImageView) findViewById(R.id.SPLASH_logo);
        btnSignIn = (Button) findViewById(R.id.SPLASH_btnSignin);
        progressBar.setVisibility(View.GONE);
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
                checkVirginity(true);
            }
        });

    }

    static final int REQUEST_SIGN_IN = 9033, REQUEST_THEME_CHANGE = 5063;

    @Override
    public void onActivityResult(int reqCode, int resCode, Intent data) {
        Log.w(LOG, "## onActivityResult resCode: " + resCode);
        switch (reqCode) {
            case REQUEST_SIGN_IN:
                if (resCode == RESULT_OK) {
                    Log.w(LOG, "## Collapsing sign in layout");
                    Util.collapse(actionsView, 100, null);
                    finish();
                }
                break;
            case REQUEST_THEME_CHANGE:
                finish();
                Intent w = new Intent(this,SplashActivity.class);
                startActivity(w);

                break;
        }
    }

    private void getMunicipality() {
        municipality = SharedUtil.getMunicipality(ctx);
        if (municipality == null) {
            RequestDTO w = new RequestDTO(RequestDTO.GET_MUNICIPALITY_BY_NAME);
            w.setMunicipalityName(MUNICIPALITY_NAME);
            progressBar.setVisibility(View.VISIBLE);
            NetUtil.sendRequest(ctx, w, new NetUtil.NetUtilListener() {
                @Override
                public void onResponse(final ResponseDTO response) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            if (response.getStatusCode() == 0) {

                                municipality = response.getMunicipalityList().get(0);
                                SharedUtil.saveMunicipality(ctx, municipality);
                                Util.expand(actionsView, ONE_SECOND, null);
                                heroImage.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Util.flashOnce(heroImage, 20, new Util.UtilAnimationListener() {
                                            @Override
                                            public void onAnimationEnded() {
                                                if (actionsView.getVisibility() == View.GONE) {
                                                    if (staff == null) {
                                                        Util.expand(actionsView, ONE_SECOND, null);
                                                    } else {
//                                                        Intent intent = new Intent(getApplicationContext(), MainDrawerActivity.class);
//                                                        startActivity(intent);
                                                    }
                                                }
                                                //checkVirginity();
                                            }
                                        });
                                    }
                                });
                            }
                        }
                    });
                }

                @Override
                public void onError(final String message) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Util.showErrorToast(ctx, message);
                        }
                    });
                }

                @Override
                public void onWebSocketClose() {

                }
            });

        } else {
            Log.i(LOG, "Municipality found: " + municipality.getMunicipalityName());
            checkVirginity(false);
        }
    }

    private void checkVirginity(boolean goToMain) {
        Log.w("Splash", "## checkVirginity");
        staff = SharedUtil.getMunicipalityStaff(ctx);
        if (staff == null) {
            if (actionsView.getVisibility() == View.GONE) {
                Util.expand(actionsView, ONE_SECOND, null);
            }

        } else {
            if (SharedUtil.getRegistrationID(ctx) == null) {
                Intent intent = new Intent(ctx, GCMDeviceService.class);
                intent.putExtra("staff", staff);
                startService(intent);
            }
            if (goToMain) {
                Intent intent = new Intent(ctx, MainDrawerActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.w("Splash", "## onResume");
    }

    private void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        index = RANDOM.nextInt(9);
                        if (index == lastIndex) {
                            index = RANDOM.nextInt(9);
                        }
                        heroImage.setImageDrawable(getImage());
                        timer.cancel();
                    }
                });

            }
        }, ONE_SECOND/2, FIVE_SECONDS);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_help) {
            Util.showToast(ctx, ctx.getString(R.string.under_cons));
            return true;
        }
        if (id == R.id.action_theme) {
            Intent w = new Intent(this, ThemeSelectorActivity.class);
            w.putExtra("darkColor",themeDarkColor);
            w.putExtra("primaryColor",themePrimaryColor);
            startActivityForResult(w, REQUEST_THEME_CHANGE);
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
        }

        return super.onOptionsItemSelected(item);
    }


    static int index, imageCount;
    static int lastIndex;
    static final int NUMBER_OF_IMAGES = 30;


    @Override
    public void onPause() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        super.onPause();
    }
    static CityImages cityImages;

    public  static Drawable getImage() {
        if (cityImages == null) {
            getLocalCityImages();
        }
        return cityImages.getImage(ctx);
    }
    //todo - download new, improved images in background if available.
    private static void getLocalCityImages() {

        if (SharedUtil.getCityImages(ctx) != null) {
            cityImages = SharedUtil.getCityImages(ctx);
            return;
        }

        cityImages = new CityImages();
        int[]imageResourceIDs = new int[NUMBER_OF_IMAGES];
        imageResourceIDs[0] = R.drawable.acity1;
        imageResourceIDs[1] = R.drawable.acity2;
        imageResourceIDs[2] = R.drawable.acity3;
        imageResourceIDs[3] = R.drawable.acity4;
        imageResourceIDs[4] = R.drawable.acity5;
        imageResourceIDs[5] = R.drawable.acity6;
        imageResourceIDs[6] = R.drawable.acity7;
        imageResourceIDs[7] = R.drawable.acity8;
        imageResourceIDs[8] = R.drawable.acity9;
        imageResourceIDs[9] = R.drawable.acity10;
        imageResourceIDs[10] = R.drawable.acity11;
        imageResourceIDs[11] = R.drawable.acity12;
        imageResourceIDs[12] = R.drawable.acity13;
        imageResourceIDs[13] = R.drawable.acity14;
        imageResourceIDs[14] = R.drawable.acity15;
        imageResourceIDs[15] = R.drawable.acity16;
        imageResourceIDs[16] = R.drawable.acity17;
        imageResourceIDs[17] = R.drawable.acity18;
        imageResourceIDs[18] = R.drawable.acity19;
        imageResourceIDs[19] = R.drawable.acity20;
        imageResourceIDs[20] = R.drawable.acity21;
        imageResourceIDs[21] = R.drawable.acity22;
        imageResourceIDs[22] = R.drawable.acity23;
        imageResourceIDs[23] = R.drawable.acity24;
        imageResourceIDs[24] = R.drawable.acity25;
        imageResourceIDs[25] = R.drawable.acity26;
        imageResourceIDs[26] = R.drawable.acity27;
        imageResourceIDs[27] = R.drawable.acity28;
        imageResourceIDs[28] = R.drawable.acity29;
        imageResourceIDs[29] = R.drawable.acity30;

        cityImages.setImageResourceIDs(imageResourceIDs);
        SharedUtil.setCityImages(ctx, cityImages);

    }
}
