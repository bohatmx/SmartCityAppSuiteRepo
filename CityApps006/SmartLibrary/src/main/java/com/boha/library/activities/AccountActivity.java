package com.boha.library.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.boha.library.R;
import com.boha.library.dto.AccountDTO;
import com.boha.library.dto.MunicipalityDTO;
import com.boha.library.dto.ProfileInfoDTO;
import com.boha.library.fragments.AccountFragment;
import com.boha.library.fragments.NavigationDrawerFragment;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.CacheUtil;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.ThemeChooser;
import com.boha.library.util.Util;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Hosts the Fragment that manages the user's accounts.
 * Allows for payment process to be kicked off.
 */
public class AccountActivity extends ActionBarActivity
        implements AccountFragment.AccountFragmentListener,
        NavigationDrawerFragment.NavigationDrawerListener {

    AccountFragment accountFragment;
    MunicipalityDTO municipality;
    int themeDarkColor, themePrimaryColor, logo;
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("AccountActivity", "### onCreate");
        ThemeChooser.setTheme(this);
        setContentView(R.layout.activity_account);
        ctx = getApplicationContext();
        accountFragment = (AccountFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        ProfileInfoDTO profileInfoDTO = (ProfileInfoDTO) getIntent().getSerializableExtra("profileInfo");

        themeDarkColor = getIntent().getIntExtra("darkColor", R.color.teal_900);
        themePrimaryColor = getIntent().getIntExtra("primaaryColor", R.color.teal_500);

        if (profileInfoDTO != null) {
            accountFragment.setProfileInfo(profileInfoDTO);
        } else {
            getCachedData();
        }
        //
        logo = getIntent().getIntExtra("logo", R.drawable.ic_action_globe);
        accountFragment.setLogo(logo);
        municipality = SharedUtil.getMunicipality(getApplicationContext());
        ActionBar actionBar = getSupportActionBar();
        if (logo != 0) {
            Drawable d = ctx.getResources().getDrawable(logo);
            Util.setCustomActionBar(ctx,
                    actionBar,
                    municipality.getMunicipalityName(), d, logo);
            getSupportActionBar().setTitle("");
        } else {
            getSupportActionBar().setTitle(municipality.getMunicipalityName());
        }

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mNavigationDrawerFragment.setPrimaryDarkColor(themeDarkColor);
        mNavigationDrawerFragment.setPrimaryColor(themePrimaryColor);
        //
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        //Track analytics
        CityApplication ca = (CityApplication) getApplication();
        Tracker t = ca.getTracker(
                CityApplication.TrackerName.APP_TRACKER);
        t.setScreenName(AccountActivity.class.getSimpleName());
        t.send(new HitBuilders.ScreenViewBuilder().build());
        //
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(themeDarkColor);
            window.setNavigationBarColor(themeDarkColor);
        }
    }

    NavigationDrawerFragment mNavigationDrawerFragment;

    private void getCachedData() {
        CacheUtil.getCacheLoginData(getApplicationContext(), new CacheUtil.CacheRetrievalListener() {
            @Override
            public void onCacheRetrieved(ResponseDTO response) {
                if (response != null) {
                    accountFragment.setProfileInfo(response.getProfileInfoList().get(0));
                }
            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_account, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onAccountStatementRequested(AccountDTO account) {

    }

    @Override
    public void onRefreshRequested() {


    }

    @Override
    public void onPause() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        super.onPause();
    }

    @Override
    public void onDestinationSelected(int position, String text) {
        destinationSelected = text;
        this.position = position;
        onBackPressed();


    }

    static final int CREATE_COMPLAINT_REQUESTED = 7813;
    public static final int PROFILE = 1, ALERTS = 2, MY_COMPLAINTS = 3,
            COMPLAINTS_AROUND_ME = 4, CREATE_COMPLAINT = 5, NEWS = 6, FAQ = 7;
    String destinationSelected;
    int position;

    @Override
    public void onBackPressed() {
        if (destinationSelected != null) {
            Intent w = new Intent();
            w.putExtra("destinationSelected", destinationSelected);
            w.putExtra("position", position);
            setResult(RESULT_OK, w);
        } else {
            setResult(RESULT_CANCELED);
        }

        finish();

    }
}
