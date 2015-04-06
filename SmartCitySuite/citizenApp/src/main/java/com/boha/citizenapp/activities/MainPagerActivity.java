package com.boha.citizenapp.activities;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.boha.citizenapp.fragments.AlertListFragment;
import com.boha.citizenapp.fragments.CreateAlertFragment;
import com.boha.citizenapp.fragments.ImageGridFragment;
import com.boha.citizenapp.fragments.PageFragment;
import com.boha.citizenapp.fragments.ProfileInfoFragment;
import com.boha.citizenapp.fragments.SplashFragment;
import com.boha.library.R;
import com.boha.library.dto.AlertDTO;
import com.boha.library.dto.MunicipalityDTO;
import com.boha.library.dto.ProfileInfoDTO;
import com.boha.library.services.PhotoUploadService;
import com.boha.library.transfer.RequestDTO;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.CacheUtil;
import com.boha.library.util.NetUtil;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.Util;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

public class MainPagerActivity extends ActionBarActivity
    implements
        CreateAlertFragment.CreateAlertFragmentListener,
        ImageGridFragment.ImageGridFragmentListener,
        AlertListFragment.AlertListener,
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    LocationRequest mLocationRequest;
    GoogleApiClient googleApiClient;
    ViewPager mPager;
    List<PageFragment> pageFragmentList;

    SplashFragment splashFragment;
    ProfileInfoFragment profileInfoFragment;
    CreateAlertFragment createAlertFragment;
    ImageGridFragment imageGridFragment;
    AlertListFragment alertListFragment;
    PagerAdapter adapter;
    Context ctx;
    int currentPageIndex;
    Location mCurrentLocation;
    ResponseDTO response;
    PagerTitleStrip strip;
    MunicipalityDTO municipality;
    boolean mRequestingLocationUpdates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_pager);
        ctx = getApplicationContext();
        mPager = (ViewPager) findViewById(R.id.pager);
        buildSplashPage();
        //
        municipality = SharedUtil.getMunicipality(ctx);

        ActionBar actionBar = getSupportActionBar();
        Util.setCustomActionBar(ctx,
                actionBar,
                municipality.getMunicipalityName(),
                ctx.getResources().getDrawable(com.boha.citizenapp.R.drawable.logo));

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        getCachedLoginData();
    }

    private void getCachedLoginData() {
        CacheUtil.getCacheLoginData(ctx, new CacheUtil.CacheRetrievalListener() {
            @Override
            public void onCacheRetrieved(ResponseDTO r) {
                response = r;
                profileInfo = response.getProfileInfoList().get(0);
                Util.preen(mPager, 1000, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        buildPages();
                        profileInfoFragment.setProfileInfo(profileInfo);

                    }
                });

            }

            @Override
            public void onError() {
                Log.e(LOG,"CacheUtil onError, possibly app entering for the first time. Getting remote data");
                getLoginData();
            }
        });
    }
    ProfileInfoDTO profileInfo;
    private void getLoginData() {
        Log.d(LOG, "getLoginData ");
        RequestDTO w = new RequestDTO(RequestDTO.SIGN_IN_CITIZEN);
        w.setUserName(profileInfo.getiDNumber());
        w.setPassword(profileInfo.getPassword());

        w.setMunicipalityID(SharedUtil.getMunicipality(ctx).getMunicipalityID());
        NetUtil.sendRequest(ctx, w, new NetUtil.NetUtilListener() {
            @Override
            public void onResponse(final ResponseDTO resp) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.w(LOG, "onResponse status: " + resp.getStatusCode());
                        if (resp.getStatusCode() > 0) {
                            Util.showErrorToast(ctx, resp.getMessage());
                            return;
                        }
                        response = resp;
                        Log.i("Splash", "### response OK from server");
                        profileInfo = response.getProfileInfoList().get(0);
                        profileInfoFragment.setProfileInfo(profileInfo);
                        if (isRefresh) {
                            isRefresh = false;
                            alertListFragment.refreshAlerts();
                        }

                        ProfileInfoDTO sp = new ProfileInfoDTO();
                        sp.setProfileInfoID(profileInfo.getProfileInfoID());
                        sp.setFirstName(profileInfo.getFirstName());
                        sp.setLastName(profileInfo.getLastName());
                        sp.setiDNumber(profileInfo.getiDNumber());
                        sp.setPassword(profileInfo.getPassword());

                        SharedUtil.saveProfile(ctx, sp);
                        CacheUtil.cacheLoginData(ctx, response, null);

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

    }
    private void buildSplashPage() {
        pageFragmentList = new ArrayList<>();
        splashFragment = SplashFragment.newInstance();

        pageFragmentList.add(splashFragment);

        adapter = new PagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(adapter);
        strip = (PagerTitleStrip) findViewById(R.id.pager_title_strip);

        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPageIndex = position;
                PageFragment pf = pageFragmentList.get(position);
                if (pf instanceof CreateAlertFragment) {
                    createAlertFragment.flash();
                    if (mCurrentLocation != null)
                        createAlertFragment.setLocation(mCurrentLocation);
                }
                if (pf instanceof AlertListFragment) {
                    if (mCurrentLocation != null)
                        alertListFragment.setLocation(mCurrentLocation);
                    alertListFragment.getCachedAlerts();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        Log.e("MainPager", "starting PhotoUploadService");
        Intent x = new Intent(ctx, PhotoUploadService.class);
        startService(x);

    }
    private void buildPages() {

        pageFragmentList = new ArrayList<>();
        profileInfoFragment = ProfileInfoFragment.newInstance();
        createAlertFragment = CreateAlertFragment.newInstance();
        imageGridFragment = ImageGridFragment.newInstance();
        alertListFragment = AlertListFragment.newInstance();
        splashFragment = SplashFragment.newInstance();


        pageFragmentList.add(profileInfoFragment);
        pageFragmentList.add(alertListFragment);
        pageFragmentList.add(imageGridFragment);
        pageFragmentList.add(createAlertFragment);

        pageFragmentList.add(splashFragment);

        adapter = new PagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(adapter);
        strip = (PagerTitleStrip) findViewById(R.id.pager_title_strip);

        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPageIndex = position;
                PageFragment pf = pageFragmentList.get(position);
                if (pf instanceof CreateAlertFragment) {
                    createAlertFragment.flash();
                    if (mCurrentLocation != null)
                        createAlertFragment.setLocation(mCurrentLocation);
                }
                if (pf instanceof AlertListFragment) {
                    if (mCurrentLocation != null)
                        alertListFragment.setLocation(mCurrentLocation);
                    alertListFragment.getCachedAlerts();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_pager, menu);
        return true;
    }
    boolean isRefresh;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
             int id = item.getItemId();

        if (id == R.id.action_refresh) {
            isRefresh = true;
            getLoginData();
            return true;
        }
        if (id == R.id.action_help) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onPause() {
        overridePendingTransition(com.boha.citizenapp.R.anim.slide_in_left, com.boha.citizenapp.R.anim.slide_out_right);
        super.onPause();
    }

    @Override
    public void onAlertClicked(AlertDTO alert) {

    }

    @Override
    public void onCreateAlertRequested() {
        int index = 0;
        for (PageFragment pf: pageFragmentList) {
            if (pf instanceof CreateAlertFragment) {
                mPager.setCurrentItem(index,true);
                return;
            }

            index++;
        }
    }

    @Override
    public void onAlertSent(AlertDTO alert) {

    }

    @Override
    public void onLocationRequested() {

    }

    @Override
    public void onPictureClicked(int position) {

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

    @Override
    public void onLocationChanged(Location location) {

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
    protected void startLocationUpdates() {
        if (googleApiClient.isConnected()) {
            mRequestingLocationUpdates = true;
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    googleApiClient, mLocationRequest, this);
        }
    }

    protected void stopLocationUpdates() {
        if (googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    googleApiClient, this);
        }
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
            if (pf instanceof ProfileInfoFragment) {
                title = "Accounts Summary";
            }
            if (pf instanceof ImageGridFragment) {
                title = getString(R.string.city_gallery);
            }
            if (pf instanceof CreateAlertFragment) {
                title = getString(R.string.comms);
            }
            if (pf instanceof AlertListFragment) {
                title = "Alerts ";
            }
            if (pf instanceof SplashFragment) {
                title = SplashActivity.MUNICIPALITY_NAME + " SmartCity";
            }

            return title;
        }
    }

    static final String LOG = MainPagerActivity.class.getSimpleName();
}
