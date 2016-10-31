package com.boha.citizenapp.ethekwini.activities;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.boha.citizenapp.ethekwini.R;
import com.boha.library.activities.AccountDetailActivity;
import com.boha.library.activities.AlertDetailActivity;
import com.boha.library.activities.EmergencyContactsActivity;
import com.boha.library.activities.GeneralInfoActivity;
import com.boha.library.activities.PictureActivity;
import com.boha.library.activities.ThemeSelectorActivity;
import com.boha.library.dto.AlertDTO;
import com.boha.library.dto.ComplaintCategoryDTO;
import com.boha.library.dto.ComplaintDTO;
import com.boha.library.dto.ComplaintTypeDTO;
import com.boha.library.dto.MunicipalityDTO;
import com.boha.library.dto.NewsArticleDTO;
import com.boha.library.dto.ProfileInfoDTO;
import com.boha.library.dto.UserDTO;
import com.boha.library.fragments.AlertListFragment;
import com.boha.library.fragments.ComplaintCreateFragment;
import com.boha.library.fragments.ComplaintsAroundMeFragment;
import com.boha.library.fragments.CreateAlertFragment;
import com.boha.library.fragments.FaqFragment;
import com.boha.library.fragments.MyComplaintsFragment;
import com.boha.library.fragments.NavigationDrawerFragment;
import com.boha.library.fragments.NewsListFragment;
import com.boha.library.fragments.PageFragment;
import com.boha.library.fragments.ProfileInfoFragment;
import com.boha.library.services.PhotoUploadService;
import com.boha.library.services.RequestService;
import com.boha.library.transfer.RequestDTO;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.CacheUtil;
import com.boha.library.util.CommsUtil;
import com.boha.library.util.DepthPageTransformer;
import com.boha.library.util.NetUtil;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.ThemeChooser;
import com.boha.library.util.Util;
import com.boha.library.util.WebCheck;
import com.boha.library.util.WebCheckResult;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;

import org.acra.ACRA;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Timer;

/**
 * TODO
 */
public class CitizenDrawerActivity extends AppCompatActivity implements
        CreateAlertFragment.CreateAlertFragmentListener,
        AlertListFragment.AlertListener,
        NewsListFragment.NewsListFragmentListener,
        ComplaintCreateFragment.ComplaintFragmentListener,
        ComplaintsAroundMeFragment.ComplaintAroundMeListener,
        MyComplaintsFragment.MyComplaintsListener,
        LocationListener,
        ProfileInfoFragment.ProfileInfoListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        FaqFragment.FaqListener {


    ActionBar ab;
    private FirebaseAnalytics mFirebaseAnalytics;
    String page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeChooser.setTheme(this);

        setContentView(R.layout.activity_main2);
        page = getIntent().getStringExtra("page");
        ctx = getApplicationContext();
        activity = this;
        //change topVIEW TO MATCH APP THEME
        Resources.Theme theme = getTheme();
        TypedValue typedValue = new TypedValue();
        theme.resolveAttribute(com.boha.library.R.attr.colorPrimaryDark, typedValue, true);
        themeDarkColor = typedValue.data;
        theme.resolveAttribute(com.boha.library.R.attr.colorPrimary, typedValue, true);
        themePrimaryColor = typedValue.data;
        logo = R.drawable.logo;

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "123");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, CitizenDrawerActivity.class.getSimpleName());
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navImage = (ImageView) findViewById(R.id.NAVHEADER_image);
//        navText = (TextView) findViewById(R.id.NAVHEADER_text);


        mPager = (ViewPager) findViewById(R.id.viewpager);
        PagerTitleStrip strip = (PagerTitleStrip) mPager.findViewById(com.boha.library.R.id.pager_title_strip);
        strip.setVisibility(View.VISIBLE);
        strip.setBackgroundColor(themeDarkColor);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        municipality = SharedUtil.getMunicipality(ctx);
        profileInfo = SharedUtil.getProfile(ctx);
        Log.i(LOG, "accountList is: " + profileInfo.getAccountList().size());

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        if (profileInfo != null) {
            if (navText != null)
                navText.setText(profileInfo.getFirstName() + " " + profileInfo.getLastName());
            else {
                FirebaseCrash.report(new Exception("UI field navText is not initialized"));
            }
        }
        user = SharedUtil.getUser(ctx);
        if (user != null) {
            if (navText != null)
                navText.setText(user.getFirstName() + " " + user.getLastName());
        }


        ActionBar actionBar = getSupportActionBar();
        Util.setCustomActionBar(ctx,
                actionBar,
                municipality.getMunicipalityName(),
                ContextCompat.getDrawable(ctx, R.drawable.logo), logo);

        getCachedLoginData();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(themeDarkColor);
            window.setNavigationBarColor(themeDarkColor);
        }

        mDrawerLayout.openDrawer(GravityCompat.START);
        setAnalyticsEvent("main", "Main Screen");
    }


    private void setAnalyticsEvent(String id, String name) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        Log.w(LOG, "analytics event sent .....");

    }

    private void getCachedLoginData() {

        CacheUtil.getCacheLoginData(ctx, new CacheUtil.CacheRetrievalListener() {
            @Override
            public void onCacheRetrieved(ResponseDTO r) {
                Log.d(LOG, "getCachedLoginData: onCacheRetrieved: ");
                response = r;
                if (response.getProfileInfoList() != null && !response.getProfileInfoList().isEmpty()) {
                    setupViewPager();
                    return;
                }
                if (response.getUserList() != null && !response.getUserList().isEmpty()) {
                    setupViewPager();
                    return;
                }
                getLoginData();
            }

            @Override
            public void onError() {
                Log.e(LOG, "CacheUtil onError, possibly app entering for the first time. Getting remote data");
                getLoginData();
            }
        });
    }

    private void getLoginData() {
        Log.e(LOG, "@@@@@@@@@ .............. ............  getLoginData ...... ");
        WebCheckResult wcr = WebCheck.checkNetworkAvailability(ctx, true);
        if (!wcr.isWifiConnected() && !wcr.isMobileConnected()) {
            Util.showSnackBar(mDrawerLayout,"You are currently not connected to the network","OK", Color.parseColor("red"));
            return;
        }
        final RequestDTO w = new RequestDTO(RequestDTO.SIGN_IN_CITIZEN);
        if (user != null) {
            w.setUser(user);
            w.setRequestType(RequestDTO.SIGN_IN_USER);
        } else {
            w.setUserName(profileInfo.getEmail());
            w.setPassword(profileInfo.getPassword());
        }

        w.setMunicipalityID(SharedUtil.getMunicipality(ctx).getMunicipalityID());
        if (location != null) {
            w.setLatitude(location.getLatitude());
            w.setLongitude(location.getLongitude());
        } else {
            w.setLatitude(0.0);
            w.setLongitude(0.0);
        }
        //todo reset after testing finished
        w.setSpoof(false);
        //

        final long start = System.currentTimeMillis();
        setRefreshActionButtonState(true);
        NetUtil.sendRequest(ctx, w, new NetUtil.NetUtilListener() {
            @Override
            public void onResponse(final ResponseDTO resp) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final long end = System.currentTimeMillis();
                        Log.w(LOG, "getLoginData: elapsed:  " + ((end - start) / 1000) + " seconds");
                        setRefreshActionButtonState(false);
                        response = resp;
                        boolean noProfile = false, noUser = false;
                        if (response.getProfileInfoList() == null || response.getProfileInfoList().isEmpty()) {
                            noProfile = true;
                        }
                        if (response.getUserList() == null || response.getUserList().isEmpty()) {
                            noUser = true;
                        }
                        if (noProfile && noUser) {
                            return;
                        }
                        if (w.getRequestType() == RequestDTO.SIGN_IN_CITIZEN) {
                            SharedUtil.saveProfile(ctx, profileInfo);
                        } else {
                            SharedUtil.saveUser(ctx, response.getUserList().get(0));
                        }

                        for (ComplaintCategoryDTO x : response.getComplaintCategoryList()) {
                            for (ComplaintTypeDTO y : x.getComplaintTypeList()) {
                                y.setCategoryName(x.getComplaintCategoryName());
                            }
                        }
                        if (isRefresh) {
                            if (myComplaintsFragment != null)
                                myComplaintsFragment.setComplaintList(resp.getComplaintList());
                        } else {
                            setupViewPager();
                        }
                        complaintList = response.getComplaintList();


                    }
                });
            }

            @Override
            public void onError(final String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setRefreshActionButtonState(false);
                        Util.showSnackBar(mPager, message, "OK", Color.parseColor("RED"));
                    }
                });
            }

            @Override
            public void onWebSocketClose() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Util.showErrorToast(ctx, "Network connection closed");
                    }
                });
            }
        });

    }

    private void refreshPictures() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_pager, menu);
        mMenu = menu;
        MenuItem favoriteItem = menu.findItem(com.boha.library.R.id.action_refresh);
        Drawable newIcon = (Drawable) favoriteItem.getIcon();
        newIcon.mutate().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
        favoriteItem.setIcon(newIcon);


        return true;
    }

    static final int THEME_REQUESTED = 8075;
    static boolean logOff;
    boolean isRefresh;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
            return true;
        }

        if (id == com.boha.library.R.id.action_logoff) {
            SharedUtil.clearProfile(ctx);
            Intent intent = new Intent(CitizenDrawerActivity.this, SigninActivity.class);
            startActivity(intent);
            logOff = true;
            finish();
            return true;
        }
        if (id == com.boha.library.R.id.action_refresh) {
            index = 0;
            isRefresh = true;
            getLoginData();
            return true;
        }
        if (id == com.boha.library.R.id.action_info) {
            Intent intent = new Intent(CitizenDrawerActivity.this, GeneralInfoActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == com.boha.library.R.id.action_emergency) {
            //todo remove after test
            RequestDTO w = new RequestDTO(RequestDTO.SEND_CLOUD_MESSAGE);
            w.setMunicipalityID(municipality.getMunicipalityID());
            w.setMessage("Test Message: " + sdf.format(new Date()));
            NetUtil.sendRequest(ctx, w, new NetUtil.NetUtilListener() {
                @Override
                public void onResponse(ResponseDTO response) {

                }

                @Override
                public void onError(String message) {

                }

                @Override
                public void onWebSocketClose() {

                }
            });

            Intent intent = new Intent(CitizenDrawerActivity.this, EmergencyContactsActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == com.boha.library.R.id.action_theme) {
            Intent w = new Intent(CitizenDrawerActivity.this, ThemeSelectorActivity.class);
            w.putExtra("darkColor", themeDarkColor);
            startActivityForResult(w, THEME_REQUESTED);
            return true;
        }
        if (id == com.boha.library.R.id.action_help) {
            Util.showToast(this, getString(R.string.under_cons));
            return true;
        }
        if (id == com.boha.library.R.id.action_app_guide) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://etmobileguide.oneconnectgroup.com/"));
            startActivity(intent);
            setAnalyticsEvent("guide", "AppGuide");
        }


        return super.onOptionsItemSelected(item);
    }

    static final SimpleDateFormat sdf = new SimpleDateFormat("dd MM yyyy HH:mm:ss");

    private void setupViewPager() {

        setMenuDestinations();

        pageFragmentList = new ArrayList<>();
        if (response.getProfileInfoList() != null && !response.getProfileInfoList().isEmpty()) {
            profileInfoFragment = ProfileInfoFragment.newInstance(response);
            profileInfoFragment.setThemeColors(themePrimaryColor, themeDarkColor);
            profileInfoFragment.setLogo(logo);
            profileInfoFragment.setPageTitle(ctx.getString(R.string.my_accounts));
        }

        complaintCreateFragment = ComplaintCreateFragment.newInstance();
        alertListFragment = AlertListFragment.newInstance(response);
        complaintsAroundMeFragment = ComplaintsAroundMeFragment.newInstance();
        newsListFragment = NewsListFragment.newInstance(response);
        myComplaintsFragment = MyComplaintsFragment.newInstance(response);
        faqFragment = FaqFragment.newInstance(response);


        alertListFragment.setThemeColors(themePrimaryColor, themeDarkColor);
        complaintCreateFragment.setThemeColors(themePrimaryColor, themeDarkColor);
        faqFragment.setThemeColors(themePrimaryColor, themeDarkColor);
        myComplaintsFragment.setThemeColors(themePrimaryColor, themeDarkColor);

        complaintsAroundMeFragment.setThemeColors(themePrimaryColor, themeDarkColor);
        newsListFragment.setThemeColors(themePrimaryColor, themeDarkColor);

         complaintsAroundMeFragment.setLogo(logo);
        alertListFragment.setLogo(logo);
        newsListFragment.setLogo(logo);


        myComplaintsFragment.setPageTitle(getString(R.string.my_complaints));

        alertListFragment.setPageTitle(ctx.getString(R.string.city_alerts));
        complaintCreateFragment.setPageTitle(ctx.getString(R.string.make_complaint));
        complaintsAroundMeFragment.setPageTitle(ctx.getString(R.string.complaints_around_me));
        newsListFragment.setPageTitle(ctx.getString(R.string.headlines));
        faqFragment.setPageTitle(getString(R.string.faq));


        pageFragmentList.add(profileInfoFragment);
        pageFragmentList.add(complaintCreateFragment);
        pageFragmentList.add(myComplaintsFragment);
        pageFragmentList.add(alertListFragment);
        pageFragmentList.add(newsListFragment);
        pageFragmentList.add(complaintsAroundMeFragment);
        pageFragmentList.add(faqFragment);

        try {
            adapter = new PagerAdapter(getSupportFragmentManager());
            mPager.setAdapter(adapter);
            mPager.setPageTransformer(true, new DepthPageTransformer());
//            mPager.setPageTransformer(true, new ZoomPageTransformer());
            mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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


            mPager.setCurrentItem(currentPageIndex);
        } catch (Exception e) {
            Log.e(LOG, "PagerAdapter failed", e);
            try {
                Util.showErrorToast(ctx, e.getMessage());
                ACRA.getErrorReporter().handleException(e, false);
                finish();
                Intent w = new Intent(this, CitizenDrawerActivity.class);
                startActivity(w);
            } catch (Exception e2) {
            }

            if (page != null) {
                if (page.equalsIgnoreCase("Alerts")) {
                    mPager.setCurrentItem(4);
                }
                if (page.equalsIgnoreCase("News")) {
                    mPager.setCurrentItem(5);
                }
                if (page.equalsIgnoreCase("Faqs")) {
                    mPager.setCurrentItem(7);
                }
            }
        }

        if (page != null) {
            if (page.equalsIgnoreCase("Alerts")) {
                mPager.setCurrentItem(1);
            }
            if (page.equalsIgnoreCase("News")) {
                mPager.setCurrentItem(2);
            }
            if (page.equalsIgnoreCase("Faqs")) {
                mPager.setCurrentItem(3);
            }
        }
    }

    Timer timer;

    private void setMenuDestinations() {


        Menu menu = navigationView.getMenu();

        if (user != null) {
            menu.findItem(R.id.nav_myaccts).setVisible(false);
        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                mDrawerLayout.closeDrawers();
                if (menuItem.getItemId() == R.id.nav_myaccts) {
                    mPager.setCurrentItem(0, true);
                    return true;
                }
                if (menuItem.getItemId() == R.id.nav_createComplaint) {
                    mPager.setCurrentItem(1, true);

                }
                if (menuItem.getItemId() == R.id.nav_mycompl) {
                    mPager.setCurrentItem(2, true);
                    return true;
                }
                if (menuItem.getItemId() == R.id.nav_alerts) {
                    mPager.setCurrentItem(3, true);
                    return true;
                }
                if (menuItem.getItemId() == R.id.nav_news) {
                    mPager.setCurrentItem(4, true);
                    return true;
                }
                if (menuItem.getItemId() == R.id.nav_complaintsAroundMe) {
                    mPager.setCurrentItem(5, true);
                    return true;
                }
                if (menuItem.getItemId() == R.id.nav_faq) {
                    mPager.setCurrentItem(6, true);
                    return true;
                }
                if (menuItem.getItemId() == R.id.nav_gallery) {
//                    mPager.setCurrentItem(6,true);
//                        return true;
                }

                return false;
            }
        });

    }


    /**
     * Adapter to manage fragments in view pager
     */
    private static class PagerAdapter extends FragmentStatePagerAdapter {

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

    static final String LOG = CitizenDrawerActivity.class.getSimpleName();

    public void setRefreshActionButtonState(final boolean refreshing) {
        if (mMenu != null) {
            final MenuItem refreshItem = mMenu.findItem(R.id.action_refresh);
            if (refreshItem != null) {
                if (refreshing) {
                    refreshItem.setActionView(R.layout.action_bar_progess);
                } else {
                    refreshItem.setActionView(null);
                }
            }
        }
    }


    /**
     * A complaint has been added and a fresh list of complaints has been received from the server.
     * My
     *
     * @param complaintList
     */
    @Override
    public void onComplaintAdded(final List<ComplaintDTO> complaintList) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                currentPageIndex = 1;
                Collections.sort(complaintList);
                response.getComplaintList().add(0, complaintList.get(0));
                myComplaintsFragment.setComplaintList(response.getComplaintList());
//                getLoginData();

            }
        });

    }

    boolean isLocationForSearch;

    @Override
    public void onLocationForComplaintsAroundMe() {
        isLocationForSearch = true;
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        Log.d(LOG, "### startLocationUpdates ....");
        if (googleApiClient.isConnected()) {
            mRequestingLocationUpdates = true;
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.d(LOG, "onConnected: Requesting location permission");
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQ_PERMISSION);
                return;
            }
            mLocationRequest = LocationRequest.create();
            mLocationRequest.setInterval(1000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setFastestInterval(500);

            LocationServices.FusedLocationApi.requestLocationUpdates(
                    googleApiClient, mLocationRequest, this);
            Log.d(LOG, "## GoogleApiClient connected, requesting location updates ...");
        } else {
            Log.e(LOG, "------- GoogleApiClient is NOT connected, not sure where we are...");
            googleApiClient.connect();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {

        Log.w(LOG, "onRequestPermissionsResult: " + permissions + " result: " + grantResults);
        switch (requestCode) {
            case REQ_PERMISSION:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        startLocationUpdates();
                    }
                }
                break;
        }
    }

    public static final int REQ_PERMISSION = 114;
    CitizenDrawerActivity activity;


    protected void stopLocationUpdates() {
        Log.e(LOG, "### stopLocationUpdates ...");
        if (googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    googleApiClient, this);
            mRequestingLocationUpdates = false;
        }
    }

    @Override
    public void onNewsClicked(NewsArticleDTO newsArticleDTO) {

    }

    @Override
    public void onCreateNewsArticleRequested() {

    }

    @Override
    public void onAccountDetailRequested() {
        if (profileInfo.getAccountList() == null || profileInfo.getAccountList().isEmpty()) {
            Util.showErrorToast(ctx, "Account information not available at this time");
            return;
        }
        Intent intent = new Intent(ctx, AccountDetailActivity.class);
        intent.putExtra("logo", logo);
        intent.putExtra("darkColor", themeDarkColor);
        intent.putExtra("primaryColor", themePrimaryColor);

        startActivityForResult(intent, CHECK_DESTINATION);

//
    }

    static final int CHECK_DESTINATION = 9086;

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(LOG,
                "+++  GoogleApiClient onConnected() ...");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(LOG, "onConnected: Requesting location permission");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQ_PERMISSION);
            return;
        }
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(
                googleApiClient);

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
            googleApiClient.connect();
        }
        Log.i(LOG, "## onStart Bind to PhotoUploadService, RequestService");
        Intent intent = new Intent(this, PhotoUploadService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        Intent intentw = new Intent(this, RequestService.class);
        bindService(intentw, rConnection, Context.BIND_AUTO_CREATE);
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (googleApiClient != null) {
            googleApiClient.disconnect();
            Log.e(LOG, "### onStop - locationClient disconnecting ");
        }
        Log.e(LOG, "## onStop unBind from PhotoUploadService, RequestService");
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
        if (rBound) {
            unbindService(rConnection);
            rBound = false;
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    static final float ACCURACY_THRESHOLD = 100f;

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
        super.onResume();
        Log.d(LOG, "##onResume connecting GoogleApiClient ...if needed");
        if (!googleApiClient.isConnected()) {
            googleApiClient.connect();
        }


    }

    @Override
    public void onActivityResult(int reqCode, int result, Intent data) {
        Log.e(LOG, "### onActivityResult reqCode: " + reqCode + " result: " + result);
        switch (reqCode) {

            case THEME_REQUESTED:
                if (result == RESULT_OK) {
                    finish();
                    Intent w = new Intent(this, CitizenDrawerActivity.class);
                    startActivity(w);
                }
                break;
            case REQUEST_LOCATION_ENABLE:
                Log.e(LOG, "### sneaky, sneaky. check gps again!");
//                checkGPS();
                break;
            case REQUEST_COMPLAINT_PICTURES:
                Log.d(LOG, "onActivityResult: REQUEST_COMPLAINT_PICTURES");
                complaintCreateFragment.onCameraCompleted();
                break;
            case CREATE_COMPLAINT_REQUESTED:
                if (result == RESULT_OK) {
                    int index = 0;
                    for (PageFragment pf : pageFragmentList) {
                        if (pf instanceof ComplaintCreateFragment) {
                            mPager.setCurrentItem(index, true);
                            break;
                        }
                        index++;
                    }
                }

                break;
        }
    }

    boolean mBound, rBound, mRefreshFromComplaint;
    PhotoUploadService mService;
    RequestService rService;
    Menu mMenu;


    private ServiceConnection rConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            Log.w(LOG, "## RequestService ServiceConnection onServiceConnected");
            RequestService.LocalBinder binder = (RequestService.LocalBinder) service;
            rService = binder.getService();
            rBound = true;

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.w(LOG, "## RequestService onServiceDisconnected");
            mBound = false;
        }
    };

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            Log.w(LOG, "## PhotoUploadService ServiceConnection onServiceConnected");
            PhotoUploadService.LocalBinder binder = (PhotoUploadService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
            Log.i(LOG, "## PhotoUploadService starting mService.uploadCachedPhotos");
            mService.uploadCachedPhotos(new PhotoUploadService.UploadListener() {
                @Override
                public void onUploadsComplete(int count) {
                    Log.w(LOG, "$$$ onUploadsComplete, list: " + count);
                    if (rBound) {
                        rService.sendRequests(new RequestService.RequestServiceListener() {
                            @Override
                            public void onRequestsProcessed(int goodResponses, int badResponses) {
                                Log.i(LOG, "## onTasksSynced, goodResponses: " + goodResponses + " badResponses: " + badResponses);
                            }

                            @Override
                            public void onError(String message) {
                                Log.e(LOG, "Error with sync: " + message);
                                Snackbar.make(mPager, "Unable to send saved data", Snackbar.LENGTH_LONG).show();
                            }
                        });

                    }
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.w(LOG, "## PhotoUploadService onServiceDisconnected");
            mBound = false;
        }
    };

    @Override
    public void setBusy(boolean busy) {
        setRefreshActionButtonState(busy);
    }



    @Override
    public void onPictureRequired(ComplaintDTO complaint) {
        Intent m = new Intent(getApplicationContext(), PictureActivity.class);
        m.putExtra("complaint", complaint);
        m.putExtra("imageType", PictureActivity.COMPLAINT_IMAGE);
        startActivity(m);
    }

    @Override
    public void onRefreshRequested() {
        getLoginData();
    }

    @Override
    public void onRefreshRequested(ComplaintDTO complaint) {
        mRefreshFromComplaint = true;
        getLoginData();
    }

    @Override
    public void onCameraRequested(ComplaintDTO complaint) {
        Intent m = new Intent(getApplicationContext(), PictureActivity.class);
        m.putExtra("imageType", PictureActivity.COMPLAINT_IMAGE);
        m.putExtra("complaint", complaint);
        m.putExtra("logo", logo);
        startActivity(m);
    }


    @Override
    public void onAlertClicked(final AlertDTO alert) {


        showAlertDetail(alert);


    }

    private void showAlertDetail(AlertDTO a) {

        Intent w = new Intent(this, AlertDetailActivity.class);
        w.putExtra("alert", a);
        startActivity(w);
    }

    @Override
    public void onCreateAlertRequested() {

    }

    @Override
    public void onFreshLocationRequested() {
        Log.d(LOG, "##### onFreshLocationRequested");
        startLocationUpdates();
    }

    @Override
    public void onAlertSent(AlertDTO alert) {

    }

    @Override
    public void onAlertLocationRequested() {
        Log.d(LOG, "##### onAlertLocationRequested .....");
        startLocationUpdates();
    }


    @Override
    public void onComplaintLocationRequested() {
        Log.d(LOG, "##### onComplaintLocationRequested .....");
        startLocationUpdates();
    }

    static final int ONE_MINUTE = 1000 * 60 * 60,
            TWO_MINUTES = ONE_MINUTE * 2, REQUEST_COMPLAINT_PICTURES = 1123,
            CREATE_COMPLAINT_REQUESTED = 1124,
            REQUEST_LOCATION_ENABLE = 1125;

    private DrawerLayout mDrawerLayout;
    ProfileInfoFragment profileInfoFragment;
    AlertListFragment alertListFragment;
    ComplaintCreateFragment complaintCreateFragment;
    ComplaintsAroundMeFragment complaintsAroundMeFragment;
    NewsListFragment newsListFragment;
    MyComplaintsFragment myComplaintsFragment;
    FaqFragment faqFragment;
    android.support.v4.view.PagerAdapter adapter;
    Context ctx;
    int currentPageIndex;
    Location mCurrentLocation;
    ResponseDTO response;
    PagerTitleStrip strip;
    MunicipalityDTO municipality;
    ViewPager mPager;
    static List<PageFragment> pageFragmentList;
    boolean mRequestingLocationUpdates;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    LocationRequest mLocationRequest;
    GoogleApiClient googleApiClient;
    int themeDarkColor, themePrimaryColor, logo;
    ImageView navImage;
    TextView navText;
    Location location;
    private ProfileInfoDTO profileInfo;
    UserDTO user;
    List<ComplaintDTO> complaintList;
    int index;
    NavigationView navigationView;

    private void getAllCaseDetails() {
        if (index < complaintList.size() && index < 10) {
            if (complaintList.get(index).getComplaintUpdateStatusList() == null
                    || complaintList.get(index).getComplaintUpdateStatusList().isEmpty()) {
                if (complaintList.get(0).getHref() != null) {
                    getCaseDetails(complaintList.get(0).getHref(), index);
                }
            }
        } else {
            myComplaintsFragment.setComplaintList(complaintList);
            index = 0;
            getAlertDetails();
        }
    }

    private void getCaseDetails(final String href, final int position) {
        Log.e(LOG, "##getCaseDetails, href: " + href);
        RequestDTO w = new RequestDTO(RequestDTO.GET_COMPLAINT_STATUS);
        w.setReferenceNumber(href);
        w.setMunicipalityID(SharedUtil.getMunicipality(ctx).getMunicipalityID());

        setBusy(true);
        NetUtil.sendRequest(ctx, w, new NetUtil.NetUtilListener() {
            @Override
            public void onResponse(final ResponseDTO response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setBusy(false);
                        if (response.getStatusCode() == 0) {
                            complaintList.get(position).setComplaintUpdateStatusList(response.getComplaintUpdateStatusList());
                        }
                        index++;
                        getAllCaseDetails();

                    }
                });

            }

            @Override
            public void onError(final String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setBusy(false);
                        Util.showToast(ctx, message);
                    }
                });

            }

            @Override
            public void onWebSocketClose() {

            }
        });
    }

    private void getAlertDetails() {
        if (index < response.getAlertList().size()) {
            if (response.getAlertList().get(index).getHref() != null) {
                getDetailData(response.getAlertList().get(index));
            }
        } else {
            setupViewPager();
            CacheUtil.cacheLoginData(ctx, response, null);
        }
    }

    private void getDetailData(final AlertDTO alert) {
        setBusy(true);
        CommsUtil.getData(this, alert.getHref(),
                RequestDTO.GET_ALERT_DETAIL, new CommsUtil.CommsListener() {
                    @Override
                    public void onDataOK(final String data) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setBusy(false);
                                alert.setAlertData(data);
                                index++;
                                getAlertDetails();
                            }
                        });

                    }

                    @Override
                    public void noDataFound() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setBusy(false);
                                index++;
                                getAlertDetails();
                            }
                        });
                    }

                    @Override
                    public void onError(final String message) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setRefreshActionButtonState(false);
                                Util.showToast(getApplicationContext(), message);
                            }
                        });
                    }
                });
    }

    @Override
    public void onBackPressed() {
        final Handler h = new Handler(Looper.getMainLooper());
        final Runnable r = new Runnable() {
            public void run() {
                int index = 0;
                switch (currentPageIndex) {
                    case 0:
                        finish();
                        return;
                    case 1:
                        index = 0;
                        break;
                    case 2:
                        index = 1;
                        break;
                    case 3:
                        index = 2;
                        break;
                    case 4:
                        index = 3;
                        break;
                    case 5:
                        index = 4;
                        break;
                    case 6:
                        index = 5;
                        break;
                    case 7:
                        index = 6;
                        break;
                    case 8:
                        index = 7;
                        break;
                    case 9:
                        index = 8;
                        break;
                }
                mPager.setCurrentItem(index, true);
                //h.postDelayed(this, 1000);

            }
        };
        r.run();

    }
}
