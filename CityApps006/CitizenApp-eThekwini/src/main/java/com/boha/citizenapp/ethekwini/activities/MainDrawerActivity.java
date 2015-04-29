package com.boha.citizenapp.ethekwini.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.boha.citizenapp.ethekwini.R;
import com.boha.library.activities.AccountDetailWithDrawer;
import com.boha.library.activities.AlertMapActivity;
import com.boha.library.activities.CityApplication;
import com.boha.library.activities.FaqActivity;
import com.boha.library.activities.MyComplaintsActivity;
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

import java.util.ArrayList;
import java.util.List;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w(LOG, "#### onCreate");
        ThemeChooser.setTheme(this);
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
                (DrawerLayout) findViewById(R.id.drawer_layout),NavigationDrawerFragment.FROM_MAIN);
        mPager = (ViewPager) findViewById(com.boha.library.R.id.pager);
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
        if (json == null) {
            startPhotoService();
            getCachedLoginData();
        } else {
            Log.e("MainDrawerActivity", "@@@@ started because of message: " + json);
            goToAlerts = true;
            getLoginData();
        }
        checkGPS();
        //Track analytics
        CityApplication ca = (CityApplication) getApplication();
        Tracker t = ca.getTracker(
                CityApplication.TrackerName.APP_TRACKER);
        t.setScreenName(MainDrawerActivity.class.getSimpleName());
        t.send(new HitBuilders.ScreenViewBuilder().build());
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
        if (text == null) {
            return;
        }
        if (text.equalsIgnoreCase(ctx.getString(R.string.my_complaints))) {
            Intent x = new Intent(this, MyComplaintsActivity.class);
            x.putExtra("logo", logo);
            x.putExtra("darkColor", themeDarkColor);
            x.putExtra("primaryColor", themePrimaryColor);
            startActivityForResult(x, CREATE_COMPLAINT_REQUESTED);
            return;
        }
        if (text.equalsIgnoreCase(ctx.getString(R.string.faq))) {
            Intent x = new Intent(this, FaqActivity.class);
            x.putExtra("logo", logo);
            x.putExtra("darkColor", themeDarkColor);
            x.putExtra("primaryColor", themePrimaryColor);
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
                if (profileInfo != null) {
                    profileInfo = response.getProfileInfoList().get(0);
                }

                buildPages();


            }

            @Override
            public void onError() {
                Log.e(LOG, "CacheUtil onError, possibly app entering for the first time. Getting remote data");
                getLoginData();
            }
        });
    }

    ProfileInfoDTO profileInfo;
    UserDTO user;

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
        progressBar.setVisibility(View.VISIBLE);
        NetUtil.sendRequest(ctx, w, new NetUtil.NetUtilListener() {
            @Override
            public void onResponse(final ResponseDTO resp) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        response = resp;
                        if (w.getRequestType() == RequestDTO.SIGN_IN_CITIZEN) {

                            ProfileInfoDTO sp = new ProfileInfoDTO();
                            sp.setProfileInfoID(profileInfo.getProfileInfoID());
                            sp.setFirstName(profileInfo.getFirstName());
                            sp.setLastName(profileInfo.getLastName());
                            sp.setiDNumber(profileInfo.getiDNumber());
                            sp.setPassword(profileInfo.getPassword());

                            SharedUtil.saveProfile(ctx, sp);
                        } else {
                            SharedUtil.saveUser(ctx,response.getUserList().get(0));
                        }
                        if (alertListFragment != null) {
                            if (isRefresh) {
                                isRefresh = false;
                                alertListFragment.refreshAlerts();
                            }
                        }


                        CacheUtil.cacheLoginData(ctx, response, new CacheUtil.CacheListener() {
                            @Override
                            public void onDataCached()  {
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
                        Util.showErrorToast(ctx, "Network connection closed");
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

    /**
     * Build fragments for the ViewPager. Check user type and omit fragments
     * as appropriate
     */
    private void buildPages() {

        pageFragmentList = new ArrayList<>();
        if (profileInfo != null) {
            profileInfoFragment = ProfileInfoFragment.newInstance();
            profileInfoFragment.setThemeColors(themePrimaryColor, themeDarkColor);
            profileInfoFragment.setLogo(logo);
            profileInfoFragment.setPageTitle(ctx.getString(R.string.my_accounts));
        }

        complaintCreateFragment = ComplaintCreateFragment.newInstance();
        alertListFragment = AlertListFragment.newInstance(response);
        complaintsAroundMeFragment = ComplaintsAroundMeFragment.newInstance();
        newsListFragment = NewsListFragment.newInstance(response);


        alertListFragment.setThemeColors(themePrimaryColor, themeDarkColor);
        complaintCreateFragment.setThemeColors(themePrimaryColor, themeDarkColor);

        complaintsAroundMeFragment.setThemeColors(themePrimaryColor, themeDarkColor);
        newsListFragment.setThemeColors(themePrimaryColor, themeDarkColor);

        complaintCreateFragment.setLogo(logo);
        complaintsAroundMeFragment.setLogo(logo);
        alertListFragment.setLogo(logo);
        newsListFragment.setLogo(logo);

        alertListFragment.setPageTitle(ctx.getString(R.string.city_alerts));
        complaintCreateFragment.setPageTitle(ctx.getString(R.string.make_complaint));
        complaintsAroundMeFragment.setPageTitle(ctx.getString(R.string.complaints_around_me));
        newsListFragment.setPageTitle(ctx.getString(R.string.city_news));

        if (profileInfo != null) {
            pageFragmentList.add(profileInfoFragment);
        }
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
//                if (pf.getPageTitle().equalsIgnoreCase(ctx.getString(R.string.complaints_around_me))) {
//                    if (complaintsAroundMeFragment.getComplaintList() == null || complaintsAroundMeFragment.getComplaintList().isEmpty()) {
//                        complaintsAroundMeFragment.getComplaintsAroundMe();
//                    } else {
//                        complaintsAroundMeFragment.setList();
//                    }
//                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (goToAlerts) {
            goToAlerts = false;
            mPager.setCurrentItem(1,true);
        }
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
    public void onAccountDetailRequested(ProfileInfoDTO profileInfo) {
        Intent intent = new Intent(ctx, AccountDetailWithDrawer.class);
        intent.putExtra("profileInfo", profileInfo);
        intent.putExtra("logo", logo);
        intent.putExtra("darkColor",themeDarkColor);
        intent.putExtra("primaryColor",themePrimaryColor);
        startActivityForResult(intent, CHECK_DESTINATION);

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
        Log.e(LOG,"### onActivityResult reqCode: " + reqCode + " result: " + result);
        switch (reqCode) {
            case CHECK_DESTINATION:
                if (result == RESULT_OK) {
                    int position = data.getIntExtra("position", 0);
                    String text = data.getStringExtra("destinationSelected");
                    onDestinationSelected(position, text);
                } else {
                    Log.e(LOG,"onActivityResult cancelled: " + RESULT_CANCELED);
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

    static final String LOG = MainDrawerActivity.class.getSimpleName();
}
