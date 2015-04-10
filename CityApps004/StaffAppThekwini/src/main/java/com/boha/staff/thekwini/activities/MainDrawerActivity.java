package com.boha.staff.thekwini.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.boha.library.activities.AlertMapActivity;
import com.boha.library.activities.PictureActivity;
import com.boha.library.dto.AlertDTO;
import com.boha.library.dto.MunicipalityDTO;
import com.boha.library.dto.MunicipalityStaffDTO;
import com.boha.library.fragments.AlertListFragment;
import com.boha.library.fragments.CreateAlertFragment;
import com.boha.library.fragments.NavigationDrawerFragment;
import com.boha.library.fragments.PageFragment;
import com.boha.library.services.PhotoUploadService;
import com.boha.library.transfer.RequestDTO;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.CacheUtil;
import com.boha.library.util.NetUtil;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.Util;
import com.boha.staff.thekwini.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainDrawerActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerListener,
        CreateAlertFragment.CreateAlertFragmentListener,
        AlertListFragment.AlertListener,
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    CreateAlertFragment createAlertFragment;
    AlertListFragment alertListFragment;
    PagerAdapter adapter;
    Context ctx;
    int currentPageIndex;
    Location mCurrentLocation;
    ResponseDTO response;
    PagerTitleStrip strip;
    MunicipalityDTO municipality;
    ViewPager mPager;
    List<PageFragment> pageFragmentList;
    boolean mRequestingLocationUpdates;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    LocationRequest mLocationRequest;
    GoogleApiClient googleApiClient;
    ProgressBar progressBar;
    int themeDarkColor, themePrimaryColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);
        ctx = getApplicationContext();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        //change topVIEW TO MATCH APP THEME
        Resources.Theme theme = getTheme();
        TypedValue typedValue = new TypedValue();
        theme.resolveAttribute(com.boha.library.R.attr.colorPrimaryDark, typedValue, true);
        themeDarkColor = typedValue.data;
        theme.resolveAttribute(com.boha.library.R.attr.colorPrimary, typedValue, true);
        themePrimaryColor = typedValue.data;

        Log.w(LOG, "##Theme themeDarkColor: " + themeDarkColor + " themePrimaryColor: " + themePrimaryColor);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        mPager = (ViewPager) findViewById(com.boha.library.R.id.pager);
        municipality = SharedUtil.getMunicipality(ctx);
        staff = SharedUtil.getMunicipalityStaff(ctx);

        ActionBar actionBar = getSupportActionBar();
        Util.setCustomActionBar(ctx,
                actionBar,
                municipality.getMunicipalityName(),
                ctx.getResources().getDrawable(R.drawable.logo));
        getSupportActionBar().setTitle("");
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        getCachedLoginData();
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        //actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.menu_main_pager, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    boolean isRefresh;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == com.boha.library.R.id.action_refresh) {
            isRefresh = true;
            getLoginData();
            return true;
        }
        if (id == com.boha.library.R.id.action_help) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestinationSelected(int position, String text) {
        if (pageFragmentList == null) {
            Log.e(LOG, "### onDestinationSelected pageFragmentList is null");
            return;
        }
        int index = 0;
        for (PageFragment pf : pageFragmentList) {
            if (pf.getPageTitle() != null) {
                if (pf.getPageTitle().equalsIgnoreCase(text)) {
                    mPager.setCurrentItem(index, true);
                    return;
                }
            }
            index++;
        }
    }

    private void getCachedLoginData() {
        CacheUtil.getCacheLoginData(ctx, new CacheUtil.CacheRetrievalListener() {
            @Override
            public void onCacheRetrieved(ResponseDTO r) {
                response = r;
                staff = response.getMunicipalityStaffList().get(0);
                buildPages();

            }

            @Override
            public void onError() {
                Log.e(LOG, "CacheUtil onError, possibly app entering for the first time. Getting remote data");
                getLoginData();
            }
        });
    }

    MunicipalityStaffDTO staff;

    private void getLoginData() {
        Log.d(LOG, "getLoginData ");
        RequestDTO w = new RequestDTO(RequestDTO.SIGN_IN_MUNICIPALITY_STAFF);
        w.setEmail(staff.getEmail());
        w.setPassword(staff.getPassword());
        w.setMunicipalityID(SharedUtil.getMunicipality(ctx).getMunicipalityID());

        progressBar.setVisibility(View.VISIBLE);
        NetUtil.sendRequest(ctx, w, new NetUtil.NetUtilListener() {
            @Override
            public void onResponse(final ResponseDTO resp) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Log.w(LOG, "onResponse status: " + resp.getStatusCode());
                        if (resp.getStatusCode() > 0) {
                            Util.showErrorToast(ctx, resp.getMessage());
                            return;
                        }
                        response = resp;
                        Log.i("Splash", "### response OK from server");
                        staff = response.getMunicipalityStaffList().get(0);
                        if (isRefresh) {
                            isRefresh = false;
                            if (alertListFragment != null)
                                alertListFragment.refreshAlerts();
                        }

                        MunicipalityStaffDTO sp = new MunicipalityStaffDTO();
                        sp.setMunicipalityID(municipality.getMunicipalityID());
                        sp.setMunicipalityStaffID(staff.getMunicipalityStaffID());
                        sp.setFirstName(staff.getFirstName());
                        sp.setLastName(staff.getLastName());
                        sp.setEmail(staff.getEmail());
                        sp.setPassword(staff.getPassword());

                        SharedUtil.saveMunicipalityStaff(ctx, sp);
                        CacheUtil.cacheLoginData(ctx, response, null);

                    }
                });
            }

            @Override
            public void onError(final String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Util.showErrorToast(ctx, message);
                    }
                });
            }

            @Override
            public void onWebSocketClose() {

            }
        });

    }


    private void buildPages() {
        Log.e("MainPager", "starting PhotoUploadService");
        Intent x = new Intent(ctx, PhotoUploadService.class);
        startService(x);
        pageFragmentList = new ArrayList<>();
        createAlertFragment = CreateAlertFragment.newInstance(SharedUtil.getMunicipalityStaff(ctx));
        alertListFragment = AlertListFragment.newInstance(response);

        createAlertFragment.setPageTitle(ctx.getString(R.string.create_alert));
        alertListFragment.setPageTitle(ctx.getString(R.string.city_alerts));
        alertListFragment.setThemeColors(themePrimaryColor, themeDarkColor);
        createAlertFragment.setThemeColors(themePrimaryColor, themeDarkColor);


        pageFragmentList.add(alertListFragment);
        pageFragmentList.add(createAlertFragment);

        adapter = new PagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(adapter);
        strip = (PagerTitleStrip) findViewById(com.boha.library.R.id.pager_title_strip);
        strip.setVisibility(View.GONE);
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPageIndex = position;
                PageFragment pf = pageFragmentList.get(position);
                pf.animateSomething();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    /**
     * Adapter to manage fragments in view pager
     */
    private class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {

            return (Fragment) pageFragmentList.get(i);
        }

        @Override
        public int getCount() {
            return pageFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = "Title";
            PageFragment pf = pageFragmentList.get(position);
            title = pf.getPageTitle();
            return title;
        }
    }

    @Override
    public void onPause() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        super.onPause();
    }

    @Override
    public void onAlertClicked(AlertDTO alert) {
        Intent intent = new Intent(ctx, AlertMapActivity.class);
        intent.putExtra("alert", alert);
        startActivity(intent);

    }

    @Override
    public void onCreateAlertRequested() {
        int index = 0;
        for (PageFragment pf : pageFragmentList) {
            if (pf instanceof CreateAlertFragment) {
                mPager.setCurrentItem(index, true);
                return;
            }

            index++;
        }
    }

    @Override
    public void onAlertSent(AlertDTO alert) {
        //TODO add this alert on top of list
        alertListFragment.onNewAlertSent(alert);
        Intent w = new Intent(ctx, PictureActivity.class);
        w.putExtra("alert", alert);
        w.putExtra("imageType", PictureActivity.ALERT_IMAGE);
        startActivityForResult(w, ALERT_PICTURES_REQUESTED);
    }

    static final int ALERT_PICTURES_REQUESTED = 1131;

    @Override
    public void onActivityResult(int reqCode, int result, Intent data) {
        switch (reqCode) {
            case ALERT_PICTURES_REQUESTED:
                if (result == RESULT_OK) {
                    mPager.setCurrentItem(0, true);
                }
                break;
        }
    }

    @Override
    public void onLocationRequested() {
        startLocationUpdates();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(LOG,
                "+++  onConnected() -  requestLocationUpdates ...");
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(
                googleApiClient);

        Log.w(LOG, "## requesting location updates ....");
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(3000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setFastestInterval(1000);
        startLocationUpdates();
    }

    @Override
    public void onStart() {
        Log.i(LOG,
                "## onStart - locationClient connecting ... ");
        if (googleApiClient != null) {
            if (mCurrentLocation == null) {
                googleApiClient.connect();
            }
        }

        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (googleApiClient != null) {
            googleApiClient.disconnect();
            Log.e(LOG, "### onStop - locationClient disconnecting ");
        }


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    static final float ACCURACY_THRESHOLD = 30f;

    @Override
    public void onLocationChanged(Location location) {
        Log.e(LOG, "### onLocationChanged accuracy: " + location.getAccuracy());
        if (location.getAccuracy() <= ACCURACY_THRESHOLD) {
            stopLocationUpdates();
            lastAccurateGPStime = new Date().getTime();

            if (createAlertFragment != null)
                createAlertFragment.setLocation(location);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onResume() {
        Log.d(LOG, "@@@ onResume...........");
        super.onResume();
        if (googleApiClient.isConnected() && !mRequestingLocationUpdates) {
            startLocationUpdates();
        }

    }

    static final int ONE_MINUTE = 1000 * 60 * 60,
            TWO_MINUTES = ONE_MINUTE * 2;
    long lastAccurateGPStime;

    protected void startLocationUpdates() {
        Log.e(LOG, "### startLocationUpdates ....");
        Date now = new Date();

        if (now.getTime() - lastAccurateGPStime < ONE_MINUTE) {
            Log.w(LOG, "-- location update still fresh, using stored coordinates");
            onLocationChanged(mCurrentLocation);
            return;
        }
        if (googleApiClient.isConnected()) {
            mRequestingLocationUpdates = true;
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    googleApiClient, mLocationRequest, this);
            Log.e(LOG, "## requesting location updates ...");
        }
    }

    protected void stopLocationUpdates() {
        Log.e(LOG, "### stopLocationUpdates ...");
        if (googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    googleApiClient, this);
        }
    }


    static final String LOG = MainDrawerActivity.class.getSimpleName();
}
