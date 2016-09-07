package com.boha.citizenapp.ethekwini.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.boha.library.R;
import com.boha.library.activities.EmergencyContactsActivity;
import com.boha.library.dto.MunicipalityDTO;
import com.boha.library.dto.ProfileInfoDTO;
import com.boha.library.fragments.LandingPageFragment;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.ThemeChooser;
import com.boha.library.util.Util;

/**
 * Created by aubreymalabie on 8/29/16.
 */

public class LandingPageActivity extends AppCompatActivity
        implements LandingPageFragment.LandingPageListener {

    LandingPageFragment landingPageFragment;
    ProfileInfoDTO profile;
    Context ctx;
    MunicipalityDTO municipality;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeChooser.setTheme(this);
        setContentView(R.layout.activity_landing_page);

        ctx = getApplicationContext();

        municipality = SharedUtil.getMunicipality(ctx);
        int logo = getIntent().getIntExtra("logo", com.boha.citizenapp.ethekwini.R.drawable.ic_action_globe);

        ActionBar actionBar = getSupportActionBar();
        Util.setCustomActionBar(ctx,
                actionBar,
                municipality.getMunicipalityName(),
                ctx.getResources().getDrawable(com.boha.citizenapp.ethekwini.R.drawable.logo), logo);
        getSupportActionBar().setTitle("");

        profile = SharedUtil.getProfile(getApplicationContext());


        landingPageFragment = (LandingPageFragment)getSupportFragmentManager().findFragmentById(R.id.fragment);


    }


    @Override
    public void onImageClicked() {
        onNewsIconClicked();
    }

    @Override
    public void onTitleClicked() {
        onNewsIconClicked();
    }

    @Override
    public void onAlertIconClicked() {
        if (profile != null) {
            Intent m = new Intent(getApplicationContext(), CitizenDrawerActivity.class);
            m.putExtra("page", "Alerts");
            startActivity(m);
        } else {
            Intent m = new Intent(getApplicationContext(), TouristDrawerActivity.class);
            m.putExtra("page", "Alerts");
            startActivity(m);
        }
    }

    @Override
    public void onNewsIconClicked() {
        if (profile != null) {
            Intent m = new Intent(getApplicationContext(), CitizenDrawerActivity.class);
            m.putExtra("page", "News");
            startActivity(m);
        } else {
            Intent m = new Intent(getApplicationContext(), TouristDrawerActivity.class);
            m.putExtra("page", "News");
            startActivity(m);
        }
    }

    @Override
    public void onFAQIconClicked() {
        if (profile != null) {
            Intent m = new Intent(getApplicationContext(), CitizenDrawerActivity.class);
            m.putExtra("page", "Faqs");
            startActivity(m);
        } else {
            Intent m = new Intent(getApplicationContext(), TouristDrawerActivity.class);
            m.putExtra("page", "Faqs");
            startActivity(m);
        }
    }

    @Override
    public void onLogin() {
        if (profile != null) {
            //todo go to signInactivity
            Intent m = new Intent(getApplicationContext(), SigninActivity.class);
            startActivity(m);
        }
    }

    @Override
    public void onContactClicked() {
        Intent intent = new Intent(getApplicationContext(), EmergencyContactsActivity.class);
        startActivity(intent);
    }
}
