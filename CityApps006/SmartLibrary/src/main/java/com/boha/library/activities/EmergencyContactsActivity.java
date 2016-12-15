package com.boha.library.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.dto.MunicipalityDTO;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.ThemeChooser;
import com.boha.library.util.Util;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Timer;
import java.util.TimerTask;

public class EmergencyContactsActivity extends AppCompatActivity {

    TextView EMC_title, EC_title1, EC_POLICE_NO, EC_POLICE_NAME, EC_REPORT_NO, EC_REPORT_NAME, EC_EMERGENCY_NO,
            EC_EMERGENCY_NAME, EC_title2, EC_WATER_TRAFFIC_NO, EC_WATER_TRAFFIC_NAME, EC_title3, EC_ELECTRICITY_NO,
            EC_title4, EC_AMBULANCE_NO, EC_AMBULANCE_NAME, EC_CITYMED_NO1, EC_CITYMED_NO2, EC_CITYMED_NAME, EC_SAREDCROSS_NO,
            EC_SAREDCROSS_NAME, EC_STJOHNS_AMBULANCE_NO, EC_STJOHNS_AMBULANCE_NAME, EC_NETCARE_NO1, EC_NETCARE_NO2,
            EC_NETCARE_NAME, EC_title5, EC_FAULTS_NO, EC_FAULTS_NAME, EC_FAULTS_NO2, EC_FAULTS_NAME2;
    ImageView EM_hero;
    ScrollView EMERGENCY_SCROLL;
    int darkColor, primaryColor, logo;
    MunicipalityDTO municipality;
    Context ctx;

    static final String MUNICIPALITY_NAME = "eThekwini";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeChooser.setTheme(this);
        setContentView(R.layout.activity_emergency_contacts);
        ctx = getApplicationContext();

        logo = getIntent().getIntExtra("logo",R.drawable.elogo);
        primaryColor = getIntent().getIntExtra("primaryColor",R.color.teal_500);
        darkColor = getIntent().getIntExtra("primaryColor",R.color.teal_700);
        municipality = SharedUtil.getMunicipality(getApplicationContext());

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
        ActionBar actionBar = getSupportActionBar();
        /*if (logo != 0) {
            Drawable d = ctx.getResources().getDrawable(logo);
            Util.setCustomActionBar(ctx,
                    actionBar,
                    MUNICIPALITY_NAME,
                    ContextCompat.getDrawable(ctx, R.drawable.elogo), logo);

        } else {
            getSupportActionBar().setTitle(municipality.getMunicipalityName());
        }*/
        Util.setCustomActionBar(ctx,
                actionBar,
                MUNICIPALITY_NAME,
                ContextCompat.getDrawable(ctx, R.drawable.logo), logo);


        setFields();
        animateSomething();
        setAnalyticsEvent("emergency","EmergencyContacts");
    }
    private void setFields(){
        /*EM_hero = (ImageView) findViewById(R.id.EM_hero);
        EM_hero.setImageDrawable(Util.getRandomBackgroundImage(ctx));*/
        EMC_title = (TextView) findViewById(R.id.EMC_title);
        EC_title1 = (TextView) findViewById(R.id.EC_title1);
        EC_POLICE_NO = (TextView) findViewById(R.id.EC_POLICE_NO_);
        EC_POLICE_NAME = (TextView) findViewById(R.id.EC_POLICE_NAME);
        EC_REPORT_NO = (TextView) findViewById(R.id.EC_REPORT_NO_);
        EC_REPORT_NAME = (TextView) findViewById(R.id.EC_REPORT_NAME);
        EC_EMERGENCY_NO = (TextView) findViewById(R.id.EC_EMERGENCY_NO_);
        EC_EMERGENCY_NAME = (TextView) findViewById(R.id.EC_EMERGENCY_NAME);
        EC_title2 = (TextView) findViewById(R.id.EC_title2);
        EC_WATER_TRAFFIC_NO = (TextView) findViewById(R.id.EC_WATER_TRAFFIC_NO_);
        EC_WATER_TRAFFIC_NAME = (TextView) findViewById(R.id.EC_WATER_TRAFFIC_NAME);
        EC_title3 = (TextView) findViewById(R.id.EC_title3);
        EC_ELECTRICITY_NO = (TextView) findViewById(R.id.EC_ELECTRICITY_NO_);
        EC_title4 = (TextView) findViewById(R.id.EC_title4);
        EC_AMBULANCE_NO = (TextView) findViewById(R.id.EC_AMBULANCE_NO_);
        EC_AMBULANCE_NAME = (TextView) findViewById(R.id.EC_AMBULANCE_NAME);
        EC_CITYMED_NO1 = (TextView) findViewById(R.id.EC_CITYMED_NO1);
        EC_CITYMED_NO2 = (TextView) findViewById(R.id.EC_CITYMED_NO2);
        EC_CITYMED_NAME = (TextView) findViewById(R.id.EC_CITYMED_NAME);
        EC_SAREDCROSS_NO = (TextView) findViewById(R.id.EC_SAREDCROSS_NO);
        EC_SAREDCROSS_NAME = (TextView) findViewById(R.id.EC_SAREDCROSS_NAME);
        EC_STJOHNS_AMBULANCE_NO = (TextView) findViewById(R.id.EC_STJOHNS_AMBULANCE_NO);
        EC_STJOHNS_AMBULANCE_NAME = (TextView) findViewById(R.id.EC_STJOHNS_AMBULANCE_NAME);
        EC_NETCARE_NO1 = (TextView) findViewById(R.id.EC_NETCARE_NO1);
        EC_NETCARE_NO2 = (TextView) findViewById(R.id.EC_NETCARE_NO2);
        EC_NETCARE_NAME = (TextView) findViewById(R.id.EC_NETCARE_NAME);
        EC_title5 = (TextView) findViewById(R.id.EC_title5);
        EC_FAULTS_NO = (TextView) findViewById(R.id.EC_FAULTS_NO);
        EC_FAULTS_NAME =(TextView) findViewById(R.id.EC_FAULTS_NAME);
        EC_FAULTS_NO2 = (TextView) findViewById(R.id.EC_FAULTS_NO2);
        EC_FAULTS_NAME2 = (TextView) findViewById(R.id.EC_FAULTS_NAME2);
        EMERGENCY_SCROLL = (ScrollView) findViewById(R.id.EMERGENCY_SCROLL);

    }

    FirebaseAnalytics mFirebaseAnalytics;
    private void setAnalyticsEvent(String id, String name) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);

        if (mFirebaseAnalytics == null) {
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
        }
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        Log.w("EmergencyContacts","analytics event sent .....");


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_emergency_contact, menu);
        mMenu = menu;
        return true;
    }
    int themeDarkColor;

    Menu mMenu;
    static final int THEME_REQUESTED = 8075;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == com.boha.library.R.id.action_app_guide) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://etmobileguide.oneconnectgroup.com/"));
            startActivity(intent);
        }
        if (id == R.id.action_info) {
            Intent intent = new Intent(EmergencyContactsActivity.this, GeneralInfoActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }


    private void animateSomething() {
      /*  final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        timer.cancel();
                        EM_hero.setImageDrawable(Util.getRandomBackgroundImage(ctx));
                        Util.flashOnce(EM_hero, 300, null);
                    }
                });
            }
        }, 500);*/
    }
}
