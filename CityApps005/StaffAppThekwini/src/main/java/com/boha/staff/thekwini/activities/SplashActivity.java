package com.boha.staff.thekwini.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.boha.library.activities.CityApplication;
import com.boha.library.dto.MunicipalityDTO;
import com.boha.library.dto.MunicipalityStaffDTO;
import com.boha.library.services.GCMDeviceService;
import com.boha.library.transfer.RequestDTO;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.NetUtil;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.Util;
import com.boha.staff.thekwini.R;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends ActionBarActivity {

    MunicipalityStaffDTO staff;
    Timer timer;
    ImageView heroImage, logo;
    View actionsView;
    Button btnSignIn;
    static Context ctx;
    ProgressBar progressBar;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ctx = getApplicationContext();
        setFields();
        //eThekwini logo - will be different for each municipality
        logo.setImageDrawable(ctx.getResources().getDrawable(R.drawable.logo));
        setTitle(MUNICIPALITY_NAME + " SmartCity");
        startTimer();
        getMunicipality();

        //TODO - custom action bar here

    }
    private void setFields() {
        actionsView = findViewById(R.id.SPLASH_actions);
        actionsView.setVisibility(View.GONE);
        heroImage = (ImageView) findViewById(R.id.SPLASH_image);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        logo = (ImageView) findViewById(R.id.SPLASH_logo);
        btnSignIn = (Button) findViewById(R.id.SPLASH_btnSignin);
        progressBar.setVisibility(View.GONE);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(btnSignIn, QUICK, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        Intent intent = new Intent(ctx, SigninActivity.class);
                        startActivity(intent);
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

    private void getMunicipality() {
        municipality = SharedUtil.getMunicipality(ctx);
        if (municipality == null) {
            RequestDTO w = new RequestDTO(RequestDTO.GET_MUNICIPALITY_BY_NAME);
            w.setMunicipalityName(MUNICIPALITY_NAME);
            progressBar.setVisibility(View.VISIBLE);
            NetUtil.sendRequest(ctx,w,new NetUtil.NetUtilListener() {
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
                                                        Intent intent = new Intent(getApplicationContext(),MainDrawerActivity.class);
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
                            Util.showErrorToast(ctx,message);
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
        staff = SharedUtil.getMunicipalityStaff(ctx);
        if (staff == null) {
            if (actionsView.getVisibility() == View.GONE) {
                Util.expand(actionsView,ONE_SECOND,null);
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
                        heroImage.setImageDrawable(getImage(ctx));
                        imageCount++;
                        if (imageCount > IMAGE_COUNT_MAX) {
                            if (timer != null) {
                                timer.cancel();
                                timer = null;
                            }
                            //Track SplashActivity
                            CityApplication ca = (CityApplication) getApplication();
                            Tracker t = ca.getTracker(
                                    CityApplication.TrackerName.APP_TRACKER);
                            t.setScreenName(SplashActivity.class.getSimpleName());
                            t.send(new HitBuilders.ScreenViewBuilder().build());
                            //
                        }
                    }
                });

            }
        }, ONE_SECOND, FIVE_SECONDS);
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
            Util.showToast(ctx,ctx.getString(R.string.under_cons));
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
    static final int IMAGE_COUNT_MAX = 10;

    public static Drawable getImage(Context ctx) {
        if (ctx == null) {
            return null;
        }
        Drawable p = null;
        switch (index) {
            case 0:
                
                p = ctx.getResources().getDrawable(R.drawable.dbn10);
                break;
            case 1:
                p = ctx.getResources().getDrawable(R.drawable.dbn11);
                break;
            case 2:
                p = ctx.getResources().getDrawable(R.drawable.dbn12);
                break;
            case 3:
                p = ctx.getResources().getDrawable(R.drawable.dbn13);
                break;
            case 4:
                p = ctx.getResources().getDrawable(R.drawable.dbn14);
                break;

            case 5:
                p = ctx.getResources().getDrawable(R.drawable.dbn15);
                break;
            case 6:
                p = ctx.getResources().getDrawable(R.drawable.dbn16);
                break;
            case 7:
                p = ctx.getResources().getDrawable(R.drawable.dbn17);
                break;
            case 8:
                p = ctx.getResources().getDrawable(R.drawable.dbn18);
                break;
            case 9:
                p = ctx.getResources().getDrawable(R.drawable.dbn19);
                break;
            default:
                p = ctx.getResources().getDrawable(R.drawable.dbn13);
                break;

        }
        index++;
        if (index > 9) {
            index = 0;
        }
        lastIndex = index;
        return p;
    }

    @Override
    public void onPause() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        super.onPause();
    }
}
