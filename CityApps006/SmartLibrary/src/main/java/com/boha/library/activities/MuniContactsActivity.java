package com.boha.library.activities;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.boha.library.R;
import com.boha.library.dto.MunicipalityDTO;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.ThemeChooser;
import com.boha.library.util.Util;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class MuniContactsActivity extends AppCompatActivity {

    int logo,  primaryColor, darkColor;
    MunicipalityDTO municipality;
    Context ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeChooser.setTheme(this);
        setContentView(R.layout.activity_muni_contacts);
        ctx = getApplicationContext();

        logo = getIntent().getIntExtra("logo",R.drawable.ic_action_globe);
        primaryColor = getIntent().getIntExtra("primaryColor",R.color.teal_500);
        darkColor = getIntent().getIntExtra("primaryColor",R.color.teal_700);
        municipality = SharedUtil.getMunicipality(getApplicationContext());
        ActionBar actionBar = getSupportActionBar();
        if (logo != 0) {
            Drawable d = ctx.getResources().getDrawable(logo);
            Util.setCustomActionBarNoAction(ctx,
                    actionBar,
                    municipality.getMunicipalityName(), d);
        } else {
            getSupportActionBar().setTitle(municipality.getMunicipalityName());
        }
        //Track analytics
        CityApplication ca = (CityApplication) getApplication();
        Tracker t = ca.getTracker(
                CityApplication.TrackerName.APP_TRACKER);
        t.setScreenName(MuniContactsActivity.class.getSimpleName());
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_muni_contacts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
      /*  if (id == R.id.action_settings) {
            return true;
        } */

        return super.onOptionsItemSelected(item);
    }
}
