package com.boha.citizenapp.activities;

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

import com.boha.citizenapp.R;
import com.boha.citizenapp.services.GCMDeviceService;
import com.boha.citylibrary.dto.MunicipalityDTO;
import com.boha.citylibrary.dto.ProfileInfoDTO;
import com.boha.citylibrary.transfer.RequestDTO;
import com.boha.citylibrary.transfer.ResponseDTO;
import com.boha.citylibrary.util.NetUtil;
import com.boha.citylibrary.util.SharedUtil;
import com.boha.citylibrary.util.Util;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends ActionBarActivity {

    ProfileInfoDTO profile;
    Timer timer;
    ImageView heroImage, logo;
    View actionsView;
    Button btnSignIn, btnRegister;
    Context ctx;
    MunicipalityDTO municipality;
    static final int ONE_SECOND = 1000, QUICK = 200;
    static final String LOG = SplashActivity.class.getSimpleName();

    static final String MUNICIPALITY_NAME = "eThekwini";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ctx = getApplicationContext();
        setFields();

        //eThekwini logo - will be different for each muni
        logo.setImageDrawable(ctx.getResources().getDrawable(R.drawable.dome5));
        setTitle("eThekwini SmartCity");
        startTimer();
        heroImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actionsView.getVisibility() == View.VISIBLE) {
                    Util.collapse(actionsView, ONE_SECOND, null);
                } else {
                    getMunicipality();
                }
            }
        });


    }

    private void setFields() {
        actionsView = findViewById(R.id.SPLASH_actions);
        actionsView.setVisibility(View.GONE);
        heroImage = (ImageView) findViewById(R.id.SPLASH_image);
        logo = (ImageView) findViewById(R.id.SPLASH_logo);
        btnRegister = (Button) findViewById(R.id.SPLASH_btnRegister);
        btnSignIn = (Button) findViewById(R.id.SPLASH_btnSignin);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(btnRegister, QUICK, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        Intent intent = new Intent(ctx, SigninActivity.class);
                        intent.putExtra("action", SigninActivity.REGISTER);
                        startActivity(intent);
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
                        intent.putExtra("action", SigninActivity.SIGN_IN);
                        startActivity(intent);
                    }
                });
            }
        });

    }

    private void getMunicipality() {
        municipality = SharedUtil.getMunicipality(ctx);
        if (municipality == null) {
            RequestDTO w = new RequestDTO(RequestDTO.GET_MUNICIPALITY_BY_NAME);
            w.setMunicipalityName(MUNICIPALITY_NAME);

            NetUtil.sendRequest(ctx,w,new NetUtil.NetUtilListener() {
                @Override
                public void onResponse(final ResponseDTO response) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (response.getStatusCode() == 0) {
                                SharedUtil.saveMunicipality(ctx, response.getMunicipalityList().get(0));
                                checkVirginity();
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
            Log.i(LOG,"Municipality found: " + municipality.getMunicipalityName());
            checkVirginity();
        }
    }
    private void checkVirginity() {
        profile = SharedUtil.getProfile(ctx);
        if (profile == null) {
            actionsView.setVisibility(View.VISIBLE);
            Util.expand(actionsView, ONE_SECOND, null);
        } else {
            if (SharedUtil.getRegistrationID(ctx) == null) {
                Intent intent = new Intent(ctx, GCMDeviceService.class);
                intent.putExtra("profile", profile);
                Log.w(LOG,"GCMDeviceService starting .....");
                startService(intent);
            }
            Log.w(LOG,"Starting MainPagerActivity ....");

            Intent intent = new Intent(ctx, MainPagerActivity.class);
            startActivity(intent);
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
                        heroImage.setImageDrawable(getNextImage(ctx));
                    }
                });

            }
        }, 1000, 5000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

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


    static int index;

    public static Drawable getNextImage(Context ctx) {

        Drawable p = null;
        switch (index) {
            case 0:
                p = ctx.getResources().getDrawable(R.drawable.dbn1);
                break;
            case 1:
                p = ctx.getResources().getDrawable(R.drawable.dbn2);
                break;
            case 2:
                p = ctx.getResources().getDrawable(R.drawable.dbn3);
                break;
            case 3:
                p = ctx.getResources().getDrawable(R.drawable.dbn4);
                break;
            case 4:
                p = ctx.getResources().getDrawable(R.drawable.dbn6);
                break;

            case 5:
                p = ctx.getResources().getDrawable(R.drawable.dbn8);
                break;
            case 6:
                p = ctx.getResources().getDrawable(R.drawable.dbn10);
                break;
            case 7:
                p = ctx.getResources().getDrawable(R.drawable.dbn11);
                break;
            case 8:
                p = ctx.getResources().getDrawable(R.drawable.dbn12);
                break;
            case 9:
                p = ctx.getResources().getDrawable(R.drawable.dbn13);
                break;

            case 10:
                p = ctx.getResources().getDrawable(R.drawable.dbn14);
                break;
            case 11:
                p = ctx.getResources().getDrawable(R.drawable.dbn15);
                break;
            case 12:
                p = ctx.getResources().getDrawable(R.drawable.dbn16);
                break;
            case 13:
                p = ctx.getResources().getDrawable(R.drawable.dbn17);
                break;
            case 14:
                p = ctx.getResources().getDrawable(R.drawable.dbn18);
                break;

            case 15:
                p = ctx.getResources().getDrawable(R.drawable.dbn19);
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
                p = ctx.getResources().getDrawable(R.drawable.dbn24);
                break;
            case 21:
                p = ctx.getResources().getDrawable(R.drawable.dbn25);
                break;
            case 22:
                p = ctx.getResources().getDrawable(R.drawable.dbn26);
                break;
            case 23:
                p = ctx.getResources().getDrawable(R.drawable.dbn27);
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
                p = ctx.getResources().getDrawable(R.drawable.dbn13);
                break;

        }
        index++;
        if (index > 32) {
            index = 0;
        }
        return p;
    }

    @Override
    public void onPause() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        super.onPause();
    }
}
