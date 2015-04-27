package com.boha.library.activities;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.boha.library.R;
import com.boha.library.dto.MunicipalityDTO;
import com.boha.library.fragments.MyComplaintsFragment;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.ThemeChooser;
import com.boha.library.util.Util;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class MyComplaintsActivity extends ActionBarActivity implements MyComplaintsFragment.MyComplaintsListener{

    MyComplaintsFragment myComplaintsFragment;
    int logo;
    Context ctx;
    boolean isCreateComplaintRequested;
    int themePrimaryColor, themeDarkColor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeChooser.setTheme(this);
        setContentView(R.layout.activity_my_complaints);
        ctx = getApplicationContext();

        myComplaintsFragment = (MyComplaintsFragment)getSupportFragmentManager().findFragmentById(R.id.fragment);
        logo = getIntent().getIntExtra("logo", R.drawable.ic_action_globe);
        themeDarkColor = getIntent().getIntExtra("darkColor", R.color.black);
        themePrimaryColor = getIntent().getIntExtra("primaryColor", R.color.dark_slate_grey);
        myComplaintsFragment.setThemeColors(themePrimaryColor,themeDarkColor);

        MunicipalityDTO municipality = SharedUtil.getMunicipality(getApplicationContext());
        ActionBar actionBar = getSupportActionBar();
        if (logo != 0) {
            Drawable d = ctx.getResources().getDrawable(logo);
            Util.setCustomActionBar(ctx,
                    actionBar,
                    municipality.getMunicipalityName(), d,logo);
            getSupportActionBar().setTitle("");
        } else {
            getSupportActionBar().setTitle(municipality.getMunicipalityName());
        }
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
           // getWindow().setNavigationBarColor(ctx.getResources().getColor(themePrimaryColor));
        }
        //Track analytics
        CityApplication ca = (CityApplication) getApplication();
        Tracker t = ca.getTracker(
                CityApplication.TrackerName.APP_TRACKER);
        t.setScreenName(MyComplaintsActivity.class.getSimpleName());
        t.send(new HitBuilders.ScreenViewBuilder().build());
//
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(themeDarkColor);
            window.setNavigationBarColor(themeDarkColor);
        }
    }

    @Override
    public void onBackPressed() {

        if (isCreateComplaintRequested) {
            isCreateComplaintRequested = false;
            setResult(RESULT_OK);
        } else {
            setResult(RESULT_CANCELED);
        }
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_complaints, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
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


    @Override
    public void onNewComplaintRequested() {
        isCreateComplaintRequested = true;
        onBackPressed();
    }
}
