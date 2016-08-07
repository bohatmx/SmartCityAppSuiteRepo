package com.boha.citizenapp.msunduzi.activities;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ProgressBar;

import com.boha.citizenapp.msunduzi.R;
import com.boha.library.activities.AccountDetailActivity;
import com.boha.library.activities.AlertDetailActivity;
import com.boha.library.activities.AlertMapActivity;
import com.boha.library.activities.CityApplication;
import com.boha.library.activities.PictureActivity;
import com.boha.library.dto.AlertDTO;
import com.boha.library.dto.ComplaintDTO;
import com.boha.library.dto.MunicipalityDTO;
import com.boha.library.dto.NewsArticleDTO;
import com.boha.library.dto.ProfileInfoDTO;
import com.boha.library.dto.UserDTO;
import com.boha.library.fragments.AlertListFragment;
import com.boha.library.fragments.ComplaintCreateFragment;
import com.boha.library.fragments.ComplaintsAroundMeFragment;
import com.boha.library.fragments.CreateAlertFragment;
import com.boha.library.fragments.NavigationDrawerFragment;
import com.boha.library.fragments.NewsListFragment;
import com.boha.library.fragments.PageFragment;
import com.boha.library.fragments.ProfileInfoFragment;
import com.boha.library.services.PhotoUploadService;
import com.boha.library.services.RequestService;
import com.boha.library.transfer.RequestDTO;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.CacheUtil;
import com.boha.library.util.NetUtil;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.ThemeChooser;
import com.boha.library.util.Util;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.acra.ACRA;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Main control point for the app. Manages the sliding drawer and a Viewpager
 * that contains the app's main fragments. Started by the SplashActivity.
 */
public class MainDrawerActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerListener,
        CreateAlertFragment.CreateAlertFragmentListener,
        AlertListFragment.AlertListener,
        NewsListFragment.NewsListFragmentListener,
        ComplaintCreateFragment.ComplaintFragmentListener,
        ComplaintsAroundMeFragment.ComplaintAroundMeListener,
        LocationListener,
        ProfileInfoFragment.ProfileInfoListener,
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
    boolean goToAlerts;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w(LOG, "#### onCreate");
        ThemeChooser.setTheme(this);
        setContentView(R.layout.activity_main_drawer);
        view = findViewById(R.id.drawer_layout);
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
                (DrawerLayout) findViewById(R.id.drawer_layout), NavigationDrawerFragment.ORIGIN_MAIN_DRAWER);
        mPager = (ViewPager) findViewById(com.boha.library.R.id.pager);
        strip = (PagerTitleStrip) mPager.findViewById(com.boha.library.R.id.pager_title_strip);
        strip.setVisibility(View.VISIBLE);
        strip.setBackgroundColor(themeDarkColor);
        mPager.setOffscreenPageLimit(NUM_ITEMS);

        municipality = SharedUtil.getMunicipality(ctx);
        profileInfo = SharedUtil.getProfile(ctx);
        user = SharedUtil.getUser(ctx);

        ActionBar actionBar = getSupportActionBar();
        Util.setCustomActionBar(ctx,
                actionBar,
                municipality.getMunicipalityName(),
                ctx.getResources().getDrawable(R.drawable.logo), logo);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(themeDarkColor);
            window.setNavigationBarColor(themeDarkColor);
        }

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        String json = getIntent().getStringExtra("message");
        getCachedLoginData();

//        checkGPS();
        //Track analytics
        CityApplication ca = (CityApplication) getApplication();
        Tracker t = ca.getTracker(
                CityApplication.TrackerName.APP_TRACKER);
        t.setScreenName(MainDrawerActivity.class.getSimpleName());
        t.send(new HitBuilders.ScreenViewBuilder().build());

        ViewPropertyAnimatorCompat x = ViewCompat.animate(view);
        x.setDuration(1000);
        x.setInterpolator(new AccelerateDecelerateInterpolator());
        x.setStartDelay(1000);
        x.alphaBy(.2f);
        x.start();
    }

    private void refreshListFromNotification() {
        goToAlerts = true;
        getLoginData();
    }

    private void checkGPS() {
        LocationManager lm = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle(getString(R.string.loc_services));
            dialog.setMessage(ctx.getString(R.string.enable_gps));
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent, REQUEST_LOCATION_ENABLE);
                }
            });
            dialog.setCancelable(false);
            if (ctx != null) {
                dialog.setIcon(ctx.getResources().getDrawable(R.drawable.ic_action_globe));
            }
            dialog.show();
        }
    }

    static final int REQUEST_LOCATION_ENABLE = 9231;

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
            mMenu = menu;
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
            Util.showToast(this, getString(R.string.under_cons));
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
        if (text == null) {
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
                if (response.getProfileInfoList() != null && !response.getProfileInfoList().isEmpty()) {
                    buildPages();
                } else {
                    if (response.getUserList() != null && !response.getUserList().isEmpty()) {
                        buildPages();
                    } else {
                        getLoginData();
                    }
                }


            }

            @Override
            public void onError() {
                Log.e(LOG, "CacheUtil onError, possibly app entering for the first time. Getting remote data");
                getLoginData();
            }
        });
    }

    private ProfileInfoDTO profileInfo;
    UserDTO user;
    private static final int NUM_ITEMS = 5;

    private void makeFakeData() {

    }
    private void getLoginData() {
        Log.e(LOG, "@@@@@@@@@ getLoginData ...... ");
        final RequestDTO w = new RequestDTO(RequestDTO.SIGN_IN_CITIZEN);
        if (user != null) {
            w.setUser(user);
            w.setRequestType(RequestDTO.SIGN_IN_USER);
        } else {
            w.setUserName(profileInfo.getiDNumber());
            w.setPassword(profileInfo.getPassword());
        }

        w.setMunicipalityID(SharedUtil.getMunicipality(ctx).getMunicipalityID());
        setBusy(true);
        NetUtil.sendRequest(ctx, w, new NetUtil.NetUtilListener() {
            @Override
            public void onResponse(final ResponseDTO resp) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setBusy(false);
                        response = resp;
                        if (response.isMunicipalityAccessFailed()) {
                            Util.showToast(ctx, getString(R.string.unable_connect_muni));
                        }
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

                            ProfileInfoDTO sp = new ProfileInfoDTO();
                            sp.setProfileInfoID(profileInfo.getProfileInfoID());
                            sp.setFirstName(profileInfo.getFirstName());
                            sp.setLastName(profileInfo.getLastName());
                            sp.setiDNumber(profileInfo.getiDNumber());
                            sp.setPassword(profileInfo.getPassword());

                            SharedUtil.saveProfile(ctx, sp);
                        } else {
                            SharedUtil.saveUser(ctx, response.getUserList().get(0));
                        }
                        if (alertListFragment != null) {
                            if (isRefresh) {
                                isRefresh = false;
                                alertListFragment.refreshAlerts();
                            }
                        }


                        CacheUtil.cacheLoginData(ctx, response, new CacheUtil.CacheListener() {
                            @Override
                            public void onDataCached() {
                                buildPages();
                            }

                            @Override
                            public void onError() {

                            }
                        });

                    }
                });
            }

            @Override
            public void onError(final String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setBusy(false);
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
                        Util.showErrorToast(ctx, "Network connection closed");
                    }
                });
            }
        });

    }


    static final long FOURTEEN_DAYS = TimeUnit.MILLISECONDS.convert(14, TimeUnit.DAYS); //gives 86400000;
    static final long TWENTY_ONE_DAYS = TimeUnit.MILLISECONDS.convert(21, TimeUnit.DAYS); //gives 86400000

    /**
     * Build fragments for the ViewPager. Check user type and omit fragments
     * as appropriate
     */
    private void buildPages() {

        pageFragmentList = new ArrayList<>();
        if (response.getProfileInfoList() != null && !response.getProfileInfoList().isEmpty()) {
            profileInfoFragment = ProfileInfoFragment.newInstance(response);
            profileInfoFragment.setThemeColors(themePrimaryColor, themeDarkColor);
            profileInfoFragment.setLogo(logo);
            profileInfoFragment.setPageTitle(ctx.getString(R.string.my_accounts));
        }

//        //remove all alerts older than 3 days
//        List<AlertDTO> list = new ArrayList<>();
//        long now = new Date().getTime();
//        Log.i(LOG,"### alertList from response: " + response.getAlertList().size());
//        for (AlertDTO k: response.getAlertList()) {
//            long delta = now - k.getUpdated().longValue();
//            Log.e(LOG,"now: " + now + " delta: " + delta + " alert: " + k.getUpdated().longValue() + " 3days: " + FOURTEEN_DAYS);
//            if ( delta > FOURTEEN_DAYS) {
//                continue;
//            }
//            list.add(k);
//        }
//        boolean noAlerts = false, noNews = false;
//        if (list.isEmpty()) {
//            noAlerts = true;
//            Util.showToast(ctx, "There are no alerts withint the last 14 days");
//        }
//        response.setAlertList(list);
//        //remove all news older than 7 days
//        Log.i(LOG,"### newsList from response: " + response.getNewsArticleList().size());
//        List<NewsArticleDTO> alist = new ArrayList<>();
//        for (NewsArticleDTO k : response.getNewsArticleList()) {
//            if (now - k.getNewsDate().longValue() > TWENTY_ONE_DAYS) {
//                continue;
//            }
//            alist.add(k);
//        }
//
//        if (alist.isEmpty()) {
//            noNews = true;
//            Util.showToast(ctx, "There are no news within the last 21 days");
//
//        }
//        response.setNewsArticleList(alist);
        complaintCreateFragment = ComplaintCreateFragment.newInstance();
        alertListFragment = AlertListFragment.newInstance(response);
        complaintsAroundMeFragment = ComplaintsAroundMeFragment.newInstance();
        newsListFragment = NewsListFragment.newInstance(response);


        alertListFragment.setThemeColors(themePrimaryColor, themeDarkColor);
        complaintCreateFragment.setThemeColors(themePrimaryColor, themeDarkColor);

        complaintsAroundMeFragment.setThemeColors(themePrimaryColor, themeDarkColor);
        newsListFragment.setThemeColors(themePrimaryColor, themeDarkColor);

        complaintsAroundMeFragment.setLogo(logo);
        alertListFragment.setLogo(logo);
        newsListFragment.setLogo(logo);

        alertListFragment.setPageTitle(ctx.getString(R.string.city_alerts));
        complaintCreateFragment.setPageTitle(ctx.getString(R.string.make_complaint));
        complaintsAroundMeFragment.setPageTitle(ctx.getString(R.string.complaints_around_me));
        newsListFragment.setPageTitle(ctx.getString(R.string.city_news));

        if (profileInfoFragment != null) {
            pageFragmentList.add(profileInfoFragment);
        }
        pageFragmentList.add(alertListFragment);
        pageFragmentList.add(complaintCreateFragment);
        pageFragmentList.add(complaintsAroundMeFragment);
        pageFragmentList.add(newsListFragment);


        try {
            adapter = new PagerAdapter(getSupportFragmentManager());
            mPager.setAdapter(adapter);
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

            mPager.setCurrentItem(currentPageIndex);
        } catch (Exception e) {
            Log.e(LOG, "Fuck!", e);
            try {
                Util.showErrorToast(ctx, e.getMessage());
                ACRA.getErrorReporter().handleException(e, false);
                finish();
                Intent w = new Intent(this, MainDrawerActivity.class);
                startActivity(w);
            } catch (Exception e2) {
            }
        }
    }


    @Override
    public void onComplaintAdded(final List<ComplaintDTO> complaintList) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                String ref = "Reference Number: " + complaintList.get(0).getReferenceNumber();
                AlertDialog.Builder d = new AlertDialog.Builder(activity);
                d.setTitle("Complaint Pictures")
                        .setMessage(ref + "\n\nDo you want to take pictures for the complaint?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent x = new Intent(getApplicationContext(), PictureActivity.class);
                                x.putExtra("complaint", complaintList.get(0));
                                x.putExtra("imageType", PictureActivity.COMPLAINT_IMAGE);
                                x.putExtra("logo", logo);
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

    @Override
    public void onAccountDetailRequested() {

        Intent intent = new Intent(ctx, AccountDetailActivity.class);
        intent.putExtra("logo", logo);
        intent.putExtra("darkColor", themeDarkColor);
        intent.putExtra("primaryColor", themePrimaryColor);
//        startActivityForResult(intent, CHECK_DESTINATION);

        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                        view,
                        "trans1"
                );
        ActivityCompat.startActivityForResult(this, intent, CHECK_DESTINATION,options.toBundle());


    }

    static final int CHECK_DESTINATION = 9086;

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
    public void onAlertClicked(final AlertDTO alert) {

        if (alert.getLatitude() != null) {
            AlertDialog.Builder x = new AlertDialog.Builder(this);
            x.setTitle("Alert Information")
                    .setMessage("Do you want to view more details or do you want to see the alert on a map?")
                    .setPositiveButton("Details", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            showAlertDetail(alert);
                        }
                    })
                    .setNegativeButton("View on Map", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent u = new Intent(getApplicationContext(), AlertMapActivity.class);
                            u.putExtra("alert", alert);
                            u.putExtra("logo", logo);
                            u.putExtra("primaryColorDark", themeDarkColor);
                            startActivity(u);
                        }
                    })
                    .show();
        } else {
            showAlertDetail(alert);
        }


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

    @Override
    public void setBusy(boolean busy) {
        setRefreshActionButtonState(busy);
    }

    @Override
    public void onPictureRequired(ComplaintDTO complaint) {

    }

    @Override
    public void onRefreshRequested() {

    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(LOG,
                "+++  GoogleApiClient onConnected() ...");
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(
                googleApiClient);

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setFastestInterval(500);

        if (startLocationUpdates) {
            startLocationUpdates = false;
            startLocationUpdates();
        }
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

    static final float ACCURACY_THRESHOLD = 15f;

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
        super.onResume();
        Log.d(LOG, "##onResume connecting GoogleApiClient ...if needed");
        if (!googleApiClient.isConnected()) {
            googleApiClient.connect();
        }

    }

    static final int ONE_MINUTE = 1000 * 60 * 60,
            TWO_MINUTES = ONE_MINUTE * 2, REQUEST_COMPLAINT_PICTURES = 1123;

    boolean startLocationUpdates;

    protected void startLocationUpdates() {
        Log.d(LOG, "### startLocationUpdates ....");
        if (googleApiClient.isConnected()) {
            mRequestingLocationUpdates = true;
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    googleApiClient, mLocationRequest, this);
            Log.d(LOG, "## GoogleApiClient connected, requesting location updates ...");
        } else {
            Log.e(LOG, "------- GoogleApiClient is NOT connected, not sure where we are...");
            googleApiClient.connect();
            startLocationUpdates = true;

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
        Log.e(LOG, "### onActivityResult reqCode: " + reqCode + " result: " + result);
        switch (reqCode) {
            case CHECK_DESTINATION:
                if (result == RESULT_OK) {
                    int position = data.getIntExtra("position", 0);
                    String text = data.getStringExtra("destinationSelected");
                    onDestinationSelected(position, text);
                } else {
                    Log.e(LOG, "onActivityResult cancelled: " + RESULT_CANCELED);
                }
                break;
            case REQUEST_LOCATION_ENABLE:
                Log.e(LOG, "### sneaky, sneaky. check gps again!");
                checkGPS();
                break;
            case REQUEST_COMPLAINT_PICTURES:
                getLoginData();
                if (result != RESULT_OK) {
                    Util.showToast(ctx, "No complaint photos");
                }
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

    boolean mBound, rBound;
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

    static final String LOG = MainDrawerActivity.class.getSimpleName();
}
