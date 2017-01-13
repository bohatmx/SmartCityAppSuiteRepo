package com.boha.library.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.dto.MunicipalityDTO;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.ThemeChooser;
import com.boha.library.util.Util;

import java.util.Timer;
import java.util.TimerTask;

public class GeneralInfoActivity extends AppCompatActivity {

    TextView GEN_txt, GEN_txt2, GEN_txt3, GEN_txt4, GEN_txt5, GEN_txt6,
            GEN_txt7, GEN_txt8, GEN_txt9, GEN_txt10, GEN_txt11, GEN_txt12, GEN_txt13, GEN_txt_title;
    ImageView GEN_hero, newsIMG, eServicesIMG;
    ScrollView GEN_SCROLL;
    Context ctx;
    int logo;
    MunicipalityDTO municipality;
    boolean isDebuggable;
    int darkColor, primaryColor;

    static final String MUNICIPALITY_NAME = "eThekwini";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeChooser.setTheme(this);
        setContentView(R.layout.activity_general_info);
        ctx = getApplicationContext();

        logo = getIntent().getIntExtra("logo",R.drawable.elogo);
        primaryColor = getIntent().getIntExtra("primaryColor",R.color.teal_500);
        darkColor = getIntent().getIntExtra("primaryColor",R.color.teal_700);
        municipality = SharedUtil.getMunicipality(getApplicationContext());

        ActionBar actionBar = getSupportActionBar();
        /*if (logo != 0) {
            Drawable d = ctx.getResources().getDrawable(logo);
            Util.setCustomActionBar(ctx,
                    actionBar,
                    municipality.getMunicipalityName(),
                    ContextCompat.getDrawable(ctx, R.drawable.elogo), logo);

        } else {
            getSupportActionBar().setTitle(municipality.getMunicipalityName());
        }*/
        Util.setCustomActionBar(ctx,
                actionBar,
                MUNICIPALITY_NAME,
                ContextCompat.getDrawable(ctx, R.drawable.logo), logo);

        animateSomething();
        setFields();
    }
    private void setFields() {
        GEN_txt = (TextView) findViewById(R.id.GEN_txt);
        GEN_txt2 = (TextView) findViewById(R.id.GEN_txt2);
        GEN_txt3 = (TextView) findViewById(R.id.GEN_txt3);
        GEN_txt4 = (TextView) findViewById(R.id.GEN_txt4);
        GEN_txt5 = (TextView) findViewById(R.id.GEN_txt5);
        GEN_txt6 = (TextView) findViewById(R.id.GEN_txt6);
        GEN_txt7 = (TextView) findViewById(R.id.GEN_txt7);
        GEN_txt8 = (TextView) findViewById(R.id.GEN_txt8);
        GEN_txt9 = (TextView) findViewById(R.id.GEN_txt9);
        GEN_txt10 = (TextView) findViewById(R.id.GEN_txt10);
        GEN_txt11 = (TextView) findViewById(R.id.GEN_txt11);
        GEN_txt12 = (TextView) findViewById(R.id.GEN_txt12);
        GEN_txt13 = (TextView) findViewById(R.id.GEN_txt13);
        GEN_hero = (ImageView) findViewById(R.id.img);
        newsIMG = (ImageView) findViewById(R.id.news_alerts) ;
        eServicesIMG = (ImageView) findViewById(R.id.e_services);
        GEN_txt_title = (TextView) findViewById(R.id.GEN_txt_title);
        GEN_SCROLL = (ScrollView) findViewById(R.id.GEN_SCROLL);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_general_info, menu);
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
        if (id == com.boha.library.R.id.action_emergency) {
            Intent intent = new Intent(GeneralInfoActivity.this, EmergencyContactsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setLogo(int logo) {
        this.logo = logo;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void animateSomething() {
        /*final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        timer.cancel();
                        GEN_hero.setImageDrawable(Util.getRandomBackgroundImage(ctx));
                        Util.flashOnce(GEN_hero, 300, null);
                    }
                });
            }
        }, 500);*/
    }
}
