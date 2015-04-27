package com.boha.citizenapp.ethekwini.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
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
import com.boha.library.activities.CityApplication;
import com.boha.library.activities.ThemeSelectorActivity;
import com.boha.library.dto.MunicipalityDTO;
import com.boha.library.dto.ProfileInfoDTO;
import com.boha.library.dto.UserDTO;
import com.boha.library.services.GCMDeviceService;
import com.boha.library.transfer.RequestDTO;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.NetUtil;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.ThemeChooser;
import com.boha.library.util.Util;
import com.boha.library.util.event.BusProvider;
import com.boha.library.util.event.UserSignedInEvent;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.squareup.otto.Subscribe;

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
    static final int ONE_SECOND = 1000, QUICK = 200, FIVE_SECONDS = ONE_SECOND * 5;
    static final String LOG = SplashActivity.class.getSimpleName();
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
        logo.setImageDrawable(ctx.getResources().getDrawable(R.drawable.logo));
        setTitle(MUNICIPALITY_NAME + " SmartCity");
        startTimer();
        ActionBar actionBar = getSupportActionBar();
        Util.setCustomActionBar(ctx,
                actionBar,
                MUNICIPALITY_NAME,
                ctx.getResources().getDrawable(R.drawable.logo), R.drawable.logo);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(themeDarkColor);
            window.setNavigationBarColor(themeDarkColor);
        }
        getMunicipality();
        //Track analytics
        CityApplication ca = (CityApplication) getApplication();
        Tracker t = ca.getTracker(
                CityApplication.TrackerName.APP_TRACKER);
        t.setScreenName(SplashActivity.class.getSimpleName());
        t.send(new HitBuilders.ScreenViewBuilder().build());


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
                checkVirginity(true);
            }
        });

        if (SharedUtil.getProfile(ctx) != null) {
            btnSignIn.setVisibility(View.GONE);
            btnRegister.setVisibility(View.GONE);
        }

    }

    @Override
    public void onActivityResult(int reqCode, int resCode, Intent data) {
        Log.d(LOG, "##------> onActivityResult reqCode: "
                + reqCode + " resCode: " + resCode);
        switch (reqCode) {
            case REQUEST_SIGN_IN:
                if (SharedUtil.getProfile(ctx) != null
                        || SharedUtil.getUser(ctx) != null) {
                    btnSignIn.setVisibility(View.GONE);
                    btnRegister.setVisibility(View.GONE);
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
                                                    if (profile == null) {
                                                        Util.expand(actionsView, ONE_SECOND, null);
                                                    } else {
                                                        Intent intent = new Intent(getApplicationContext(), MainDrawerActivity.class);
                                                        startActivity(intent);
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
        profile = SharedUtil.getProfile(ctx);
        user = SharedUtil.getUser(ctx);
        if (profile == null && user == null) {
            if (actionsView.getVisibility() == View.GONE) {
                Util.expand(actionsView, ONE_SECOND, null);
            }

        } else {
            if (SharedUtil.getRegistrationID(ctx) == null) {
                Intent intent = new Intent(ctx, GCMDeviceService.class);
                intent.putExtra("profile", profile);
                Log.w(LOG, "GCMDeviceService starting .....");
                startService(intent);
            }

            if (goToMain) {
                Intent intent = new Intent(ctx, MainDrawerActivity.class);
                startActivity(intent);
            }
        }
    }


    private void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        index = RANDOM.nextInt(32);
                        if (index == lastIndex) {
                            index = RANDOM.nextInt(32);
                        }
                        heroImage.setImageDrawable(getImage(ctx));
                        timer.cancel();

                    }
                });

            }
        }, ONE_SECOND / 2, FIVE_SECONDS * 2);
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
        }

        return super.onOptionsItemSelected(item);
    }



    static int index, imageCount;
    static int lastIndex;
    static final int REQUEST_SIGN_IN = 9033, REQUEST_THEME_CHANGE = 1782;
    static final int IMAGE_COUNT_MAX = 20;

    public static Drawable getImage(Context ctx) {
        if (ctx == null) {
            return null;
        }
        int index = RANDOM.nextInt(32);
        if (index == lastIndex) {
            index = RANDOM.nextInt(32);
        }
        try {
            Drawable p = null;
            switch (index) {
                case 0:

                    p = ctx.getResources().getDrawable(R.drawable.durban17);
                    break;
                case 1:
                    p = ctx.getResources().getDrawable(R.drawable.durban16);
                    break;
                case 2:
                    p = ctx.getResources().getDrawable(R.drawable.dbn3);
                    break;
                case 3:
                    p = ctx.getResources().getDrawable(R.drawable.durban14);
                    break;
                case 4:
                    p = ctx.getResources().getDrawable(R.drawable.durban9);
                    break;

                case 5:
                    p = ctx.getResources().getDrawable(R.drawable.durban13);
                    break;
                case 6:
                    p = ctx.getResources().getDrawable(R.drawable.durban6);
                    break;
                case 7:
                    p = ctx.getResources().getDrawable(R.drawable.durban12);
                    break;
                case 8:
                    p = ctx.getResources().getDrawable(R.drawable.durban11);
                    break;
                case 9:
                    p = ctx.getResources().getDrawable(R.drawable.dbn13);
                    break;

                case 10:
                    p = ctx.getResources().getDrawable(R.drawable.durban8);
                    break;
                case 11:
                    p = ctx.getResources().getDrawable(R.drawable.dbn15);
                    break;
                case 12:
                    p = ctx.getResources().getDrawable(R.drawable.dbn16);
                    break;
                case 13:
                    p = ctx.getResources().getDrawable(R.drawable.durban8);
                    break;
                case 14:
                    p = ctx.getResources().getDrawable(R.drawable.dbn21);
                    break;

                case 15:
                    p = ctx.getResources().getDrawable(R.drawable.durban2);
                    break;
                case 16:
                    p = ctx.getResources().getDrawable(R.drawable.dbn20);
                    break;
                case 17:
                    p = ctx.getResources().getDrawable(R.drawable.dbn21);
                    break;
                case 18:
                    p = ctx.getResources().getDrawable(R.drawable.dbn22);
                    break;
                case 19:
                    p = ctx.getResources().getDrawable(R.drawable.dbn23);
                    break;

                case 20:
                    p = ctx.getResources().getDrawable(R.drawable.durban16);
                    break;
                case 21:
                    p = ctx.getResources().getDrawable(R.drawable.dbn25);
                    break;
                case 22:
                    p = ctx.getResources().getDrawable(R.drawable.dbn33);
                    break;
                case 23:
                    p = ctx.getResources().getDrawable(R.drawable.dbn13);
                    break;
                case 24:
                    p = ctx.getResources().getDrawable(R.drawable.dbn28);
                    break;

                case 25:
                    p = ctx.getResources().getDrawable(R.drawable.dbn29);
                    break;
                case 26:
                    p = ctx.getResources().getDrawable(R.drawable.dbn30);
                    break;
                case 27:
                    p = ctx.getResources().getDrawable(R.drawable.dbn31);
                    break;
                case 28:
                    p = ctx.getResources().getDrawable(R.drawable.dbn32);
                    break;
                case 29:
                    p = ctx.getResources().getDrawable(R.drawable.dbn33);
                    break;
                case 30:
                    p = ctx.getResources().getDrawable(R.drawable.dbn34);
                    break;
                case 31:
                    p = ctx.getResources().getDrawable(R.drawable.dbn35);
                    break;
                case 32:
                    p = ctx.getResources().getDrawable(R.drawable.dbn37);
                    break;
                default:
                    p = ctx.getResources().getDrawable(R.drawable.durban9);
                    break;

            }

            lastIndex = index;
            return p;
        } catch (OutOfMemoryError e) {
            Log.e("SplashActivity","Error loading bitmap: " + index);
            return ctx.getResources().getDrawable(R.drawable.durban9);
        } catch (Exception e) {
            return ctx.getResources().getDrawable(R.drawable.durban9);
        }
    }

    @Subscribe
    public void onUserSignedIn(UserSignedInEvent e) {
        Log.e("SplashActivity","+++++ Yay! Boooyah! event bus is humming!");
        btnSignIn.setVisibility(View.GONE);
        btnRegister.setVisibility(View.GONE);
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.e("SplashActivity", "### onResume");
        BusProvider.getInstance().register(this);
        if (SharedUtil.getProfile(ctx) != null) {
            btnSignIn.setVisibility(View.GONE);
            btnRegister.setVisibility(View.GONE);
        }
    }
    @Override
    public void onPause() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        BusProvider.getInstance().unregister(this);
        super.onPause();
    }
}
