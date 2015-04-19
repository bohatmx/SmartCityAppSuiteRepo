package com.boha.smartcity.thekwini.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
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
import com.boha.library.activities.FaqActivity;
import com.boha.library.activities.MyComplaintsActivity;
import com.boha.library.activities.PictureActivity;
import com.boha.library.dto.AlertDTO;
import com.boha.library.dto.ComplaintDTO;
import com.boha.library.dto.MunicipalityDTO;
import com.boha.library.dto.NewsArticleDTO;
import com.boha.library.dto.ProfileInfoDTO;
import com.boha.library.fragments.AlertListFragment;
import com.boha.library.fragments.ComplaintCreateFragment;
import com.boha.library.fragments.ComplaintsAroundMeFragment;
import com.boha.library.fragments.CreateAlertFragment;
import com.boha.library.fragments.NavigationDrawerFragment;
import com.boha.library.fragments.NewsListFragment;
import com.boha.library.fragments.PageFragment;
import com.boha.library.fragments.ProfileInfoFragment;
import com.boha.library.services.PhotoUploadService;
import com.boha.library.transfer.RequestDTO;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.CacheUtil;
import com.boha.library.util.NetUtil;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.Util;
import com.boha.smartcity.thekwini.R;
import com.boha.smartcity.thekwini.fragments.ImageGridFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

/**
 * Main control point for the app. Manages the sliding drawer and a Viewpager
 * that contains the app's main fragments. Started by the SplashActivity.
 */
public class MainDrawerActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerListener,
        CreateAlertFragment.CreateAlertFragmentListener,
        ImageGridFragment.ImageGridFragmentListener,
        AlertListFragment.AlertListener,
        NewsListFragment.NewsListFragmentListener,
        ComplaintCreateFragment.ComplaintFragmentListener,
        ComplaintsAroundMeFragment.ComplaintAroundMeListener,
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    ProfileInfoFragment profileInfoFragment;
    AlertListFragment alertListFragment;
    ComplaintCreateFragment complaintCreateFragment;
    ComplaintsAroundMeFragment complaintsAroundMeFragment;
    NewsListFragment newsListFragment;
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
    int themeDarkColor, themePrimaryColor, logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w(LOG, "#### onCreate");
        setContentView(R.layout.activity_main_drawer);
        ctx = getApplicationContext();
        activity = this;
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        //change topVIEW TO MATCH APP THEME
        Resources.Theme theme = getTheme();
        TypedValue typedValue = new TypedValue();
        theme.resolveAttribute(com.boha.library.R.attr.colorPrimaryDark, typedValue, true);
        themeDarkColor = typedValue.data;
        theme.resolveAttribute(com.boha.library.R.attr.colorPrimary, typedValue, true);
        themePrimaryColor = typedValue.data;

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mNavigationDrawerFragment.setPrimaryDarkColor(themeDarkColor);
        mNavigationDrawerFragment.setPrimaryColor(themePrimaryColor);
        logo = R.drawable.logo;
        //
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        mPager = (ViewPager) findViewById(com.boha.library.R.id.pager);
        municipality = SharedUtil.getMunicipality(ctx);
        profileInfo = SharedUtil.getProfile(ctx);

        ActionBar actionBar = getSupportActionBar();
        Util.setCustomActionBar(ctx,
                actionBar,
                municipality.getMunicipalityName(),
                ctx.getResources().getDrawable(R.drawable.logo),logo);
        getSupportActionBar().setTitle("");

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        startPhotoService();
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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
        if (text.equalsIgnoreCase(ctx.getString(R.string.my_complaints))) {
            Intent x = new Intent(this, MyComplaintsActivity.class);
            x.putExtra("logo",logo);
            x.putExtra("darkColor",themeDarkColor);
            x.putExtra("primaryColor",themePrimaryColor);
            startActivityForResult(x, CREATE_COMPLAINT_REQUESTED);
            return;
        }
        if (text.equalsIgnoreCase(ctx.getString(R.string.faq))) {
            Intent x = new Intent(this, FaqActivity.class);
            x.putExtra("logo",logo);
            x.putExtra("darkColor",themeDarkColor);
            x.putExtra("primaryColor",themePrimaryColor);
            startActivity(x);
            return;
        }
        int index = 0;
        for (PageFragment pf : pageFragmentList) {
            if (pf.getPageTitle() != null) {
                if (pf.getPageTitle().equalsIgnoreCase(text)) {
                    mPager.setCurrentItem(index, true);
                    if (pf instanceof ComplaintsAroundMeFragment) {
                        complaintsAroundMeFragment.getComplaintsAroundMe();
                    }
                    return;
                }
            }
            index++;
        }
    }

    static final int CREATE_COMPLAINT_REQUESTED = 5413;

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
                Log.e(LOG, "CacheUtil onError, possibly app entering for the first time. Getting remote data");
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
        progressBar.setVisibility(View.VISIBLE);
        NetUtil.sendRequest(ctx, w, new NetUtil.NetUtilListener() {
            @Override
            public void onResponse(final ResponseDTO resp) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        response = resp;
                        profileInfo = response.getProfileInfoList().get(0);
                        if (profileInfoFragment != null) {
                            profileInfoFragment.setProfileInfo(profileInfo);
                        }
                        if (alertListFragment != null) {
                            if (isRefresh) {
                                isRefresh = false;
                                alertListFragment.refreshAlerts();
                            }
                        }
                        buildPages();
                        ProfileInfoDTO sp = new ProfileInfoDTO();
                        sp.setProfileInfoID(profileInfo.getProfileInfoID());
                        sp.setFirstName(profileInfo.getFirstName());
                        sp.setLastName(profileInfo.getLastName());
                        sp.setiDNumber(profileInfo.getiDNumber());
                        sp.setPassword(profileInfo.getPassword());

                        SharedUtil.saveProfile(ctx, sp);
                        CacheUtil.cacheLoginData(ctx, response, null);
                        if (response.isMunicipalityAccessFailed()) {
                            Util.showErrorToast(ctx, getString(R.string.unable_connect_muni));
                            return;
                        }
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Util.showErrorToast(ctx,"Network connection closed");
                    }
                });
            }
        });

    }

    private void startPhotoService() {

        Log.e("MainPager", "starting PhotoUploadService");
        Intent x = new Intent(ctx, PhotoUploadService.class);
        startService(x);

    }

    private void buildPages() {

        pageFragmentList = new ArrayList<>();
        profileInfoFragment = ProfileInfoFragment.newInstance();
        complaintCreateFragment = ComplaintCreateFragment.newInstance();
        alertListFragment = AlertListFragment.newInstance(response);
        complaintsAroundMeFragment = ComplaintsAroundMeFragment.newInstance();
        newsListFragment = NewsListFragment.newInstance(response);


        alertListFragment.setThemeColors(themePrimaryColor, themeDarkColor);
        complaintCreateFragment.setThemeColors(themePrimaryColor, themeDarkColor);
        profileInfoFragment.setThemeColors(themePrimaryColor, themeDarkColor);
        complaintsAroundMeFragment.setThemeColors(themePrimaryColor, themeDarkColor);
        newsListFragment.setThemeColors(themePrimaryColor, themeDarkColor);

        profileInfoFragment.setLogo(logo);
        complaintCreateFragment.setLogo(logo);
        complaintsAroundMeFragment.setLogo(logo);
        alertListFragment.setLogo(logo);
        newsListFragment.setLogo(logo);

        profileInfoFragment.setPageTitle(ctx.getString(R.string.my_accounts));
        alertListFragment.setPageTitle(ctx.getString(R.string.city_alerts));
        complaintCreateFragment.setPageTitle(ctx.getString(R.string.make_complaint));
        complaintsAroundMeFragment.setPageTitle(ctx.getString(R.string.complaints_around_me));
        newsListFragment.setPageTitle(ctx.getString(R.string.city_news));

        pageFragmentList.add(profileInfoFragment);
        pageFragmentList.add(alertListFragment);
        pageFragmentList.add(complaintCreateFragment);
        pageFragmentList.add(complaintsAroundMeFragment);
        pageFragmentList.add(newsListFragment);


        adapter = new PagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(adapter);
        strip = (PagerTitleStrip) findViewById(com.boha.library.R.id.pager_title_strip);
        strip.setBackgroundColor(themeDarkColor);
        //strip.setVisibility(View.GONE);
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPageIndex = position;
                PageFragment pf = pageFragmentList.get(position);
                pf.animateSomething();
                if (pf.getPageTitle().equalsIgnoreCase(ctx.getString(R.string.complaints_around_me))) {
                    if (complaintsAroundMeFragment.getComplaintList() == null || complaintsAroundMeFragment.getComplaintList().isEmpty()) {
                        complaintsAroundMeFragment.getComplaintsAroundMe();
                    }else {
                        complaintsAroundMeFragment.setList();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onFindComplaintsLikeMine(ComplaintDTO complaint) {

    }

    @Override
    public void onFindComplaintsAroundMe() {

    }

    @Override
    public void onComplaintAdded(final ComplaintDTO complaint) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                String ref = "Reference Number: " + complaint.getReferenceNumber();
                AlertDialog.Builder d = new AlertDialog.Builder(activity);
                d.setTitle("Complaint Pictures")
                        .setMessage(ref + "\n\nDo you want to take pictures for the complaint?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent x = new Intent(getApplicationContext(), PictureActivity.class);
                                x.putExtra("complaint", complaint);
                                x.putExtra("imageType", PictureActivity.COMPLAINT_IMAGE);
                                x.putExtra("logo",logo);
                                startActivityForResult(x, REQUEST_COMPLAINT_PICTURES);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getLoginData();
                            }
                        })
                        .show();
            }
        });

    }

    boolean isLocationForSearch;
    @Override
    public void onLocationForComplaintsAroundMe() {
        isLocationForSearch = true;
        startLocationUpdates();
    }

    @Override
    public void onNewsClicked(NewsArticleDTO newsArticleDTO) {

    }

    @Override
    public void onCreateNewsArticleRequested() {

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
            PageFragment pf = pageFragmentList.get(position);
            return pf.getPageTitle();
        }
    }

    @Override
    public void onPause() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        super.onPause();
    }

    @Override
    public void onAlertClicked(AlertDTO alert) {

        Intent u = new Intent(this, AlertMapActivity.class);
        u.putExtra("alert", alert);
        u.putExtra("logo", logo);
        startActivity(u);
    }

    @Override
    public void onCreateAlertRequested() {

    }

    @Override
    public void onFreshLocationRequested() {
        startLocationUpdates();
    }

    @Override
    public void onAlertSent(AlertDTO alert) {

    }

    @Override
    public void onLocationRequested() {
        Log.d(LOG, "##### onLocationChanged ...");
        startLocationUpdates();
    }

    @Override
    public void onPictureClicked(int position) {

    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(LOG,
                "+++  GoogleApiClient onConnected() ...");
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(
                googleApiClient);

        Log.w(LOG, "## setup location request, mCurrentLocation acc: " + mCurrentLocation.getAccuracy());
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setFastestInterval(500);
//        startLocationUpdates();
    }

    @Override
    public void onStart() {
        Log.d(LOG,
                "## onStart - GoogleApiClient connecting ... ");
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

    static final float ACCURACY_THRESHOLD = 20f;

    Location location;
    @Override
    public void onLocationChanged(Location location) {
        Log.e(LOG, "### onLocationChanged accuracy: " + location.getAccuracy());
        if (location.getAccuracy() <= ACCURACY_THRESHOLD) {
            this.location = location;
            stopLocationUpdates();
            mRequestingLocationUpdates = false;

            if (isLocationForSearch) {
                isLocationForSearch = false;
                if (complaintsAroundMeFragment != null) {
                    complaintsAroundMeFragment.setLocation(location);
                }
            } else {
                if (complaintCreateFragment != null)
                    complaintCreateFragment.setLocation(location);
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onResume() {
        Log.d(LOG, "@@@ onResume...........");
        super.onResume();
        if (googleApiClient.isConnected()) {
            if (!mRequestingLocationUpdates) {
                startLocationUpdates();
            }
        }else {
            Log.d(LOG,"## re-connecting GoogleApiClient ...");
            googleApiClient.connect();
        }

    }

    static final int ONE_MINUTE = 1000 * 60 * 60,
            TWO_MINUTES = ONE_MINUTE * 2, REQUEST_COMPLAINT_PICTURES = 1123;
    long lastAccurateGPStime;

    protected void startLocationUpdates() {
        Log.d(LOG, "### startLocationUpdates ....");
        if (googleApiClient.isConnected()) {
            mRequestingLocationUpdates = true;
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    googleApiClient, mLocationRequest, this);
            Log.d(LOG, "## GoogleApiClient connected, requesting location updates ...");
        } else {
            Log.e(LOG, "------- GoogleApiClient is NOT connected, not sure where we are...");


        }
    }

    MainDrawerActivity activity;


    protected void stopLocationUpdates() {
        Log.e(LOG, "### stopLocationUpdates ...");
        if (googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    googleApiClient, this);
            mRequestingLocationUpdates = false;
        }
    }

    @Override
    public void onActivityResult(int reqCode, int result, Intent data) {
        switch (reqCode) {
            case REQUEST_COMPLAINT_PICTURES:
                getLoginData();
                if (result != RESULT_OK) {
                    Util.showToast(ctx,getString(R.string.no_compl_photo));
                }
                break;
            case CREATE_COMPLAINT_REQUESTED:
                if (result == RESULT_OK) {
                    int index = 0;
                    for (PageFragment pf: pageFragmentList) {
                        if (pf instanceof ComplaintCreateFragment) {
                            mPager.setCurrentItem(index,true);
                            break;
                        }
                        index++;
                    }
                }

                break;
        }
    }

    static final String LOG = MainDrawerActivity.class.getSimpleName();
}
