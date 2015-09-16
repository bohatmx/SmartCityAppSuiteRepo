package com.boha.citizenapp.ethekwini.activities;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
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
import com.boha.library.activities.PictureActivity;
import com.boha.library.activities.ThemeSelectorActivity;
import com.boha.library.adapters.AddressListAdapter;
import com.boha.library.dto.AlertDTO;
import com.boha.library.dto.ComplaintCategoryDTO;
import com.boha.library.dto.ComplaintDTO;
import com.boha.library.dto.ComplaintTypeDTO;
import com.boha.library.dto.GISAddressDTO;
import com.boha.library.dto.MunicipalityDTO;
import com.boha.library.dto.NewsArticleDTO;
import com.boha.library.dto.ProfileInfoDTO;
import com.boha.library.dto.UserDTO;
import com.boha.library.fragments.AddressDialog;
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
import com.boha.library.util.ResidentialAddress;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.ThemeChooser;
import com.boha.library.util.Util;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.acra.ACRA;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeChooser.setTheme(this);

        setContentView(R.layout.activity_main2);
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


        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        checkAddress();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navImage = (ImageView) findViewById(R.id.NAVHEADER_image);
        navText = (TextView) findViewById(R.id.NAVHEADER_text);


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

        if (profileInfo != null) {
            navText.setText(profileInfo.getFirstName() + " " + profileInfo.getLastName());
        }
        user = SharedUtil.getUser(ctx);
        if (user != null) {
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
    }

    private void checkAddress() {
        ResidentialAddress address = SharedUtil.getAddress(ctx);
        if (address == null) {
            Intent w = new Intent(ctx, AddressActivity.class);
            w.putExtra("type", AddressActivity.CALLED_FROM_DRAWER_ACTIVITY);
            startActivity(w);
            finish();
        }
    }
    private void getCachedLoginData() {

        CacheUtil.getCacheLoginData(ctx, new CacheUtil.CacheRetrievalListener() {
            @Override
            public void onCacheRetrieved(ResponseDTO r) {
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
        if (location != null) {
            w.setLatitude(location.getLatitude());
            w.setLongitude(location.getLongitude());
        } else {
            w.setLatitude(0.0);
            w.setLongitude(0.0);
        }
        setRefreshActionButtonState(true);
        NetUtil.sendRequest(ctx, w, new NetUtil.NetUtilListener() {
            @Override
            public void onResponse(final ResponseDTO resp) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
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

                        for (ComplaintCategoryDTO x : response.getComplaintCategoryList()) {
                            for (ComplaintTypeDTO y : x.getComplaintTypeList()) {
                                y.setCategoryName(x.getComplaintCategoryName());
                            }
                        }
                        setupViewPager();
//                        complaintList = response.getComplaintList();
//                        getAllCaseDetails();


                    }
                });
            }

            @Override
            public void onError(final String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setRefreshActionButtonState(false);
                        Util.showErrorToast(ctx, message);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_pager, menu);
        mMenu = menu;
        return true;
    }

    static final int THEME_REQUESTED = 8075;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        if (id == com.boha.library.R.id.action_logoff) {
            finish();
            return true;
        }
        if (id == com.boha.library.R.id.action_refresh) {
            index = 0;
            getLoginData();
            return true;
        }
        if (id == com.boha.library.R.id.action_theme) {
            Intent w = new Intent(this, ThemeSelectorActivity.class);
            w.putExtra("darkColor", themeDarkColor);
            startActivityForResult(w, THEME_REQUESTED);
            return true;
        }
        if (id == com.boha.library.R.id.action_help) {
            Util.showToast(this, getString(R.string.under_cons));
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

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

        complaintCreateFragment.setLogo(logo);
        complaintsAroundMeFragment.setLogo(logo);
        alertListFragment.setLogo(logo);
        newsListFragment.setLogo(logo);


        myComplaintsFragment.setPageTitle(getString(R.string.my_complaints));

        alertListFragment.setPageTitle(ctx.getString(R.string.city_alerts));
        complaintCreateFragment.setPageTitle(ctx.getString(R.string.make_complaint));
        complaintsAroundMeFragment.setPageTitle(ctx.getString(R.string.complaints_around_me));
        newsListFragment.setPageTitle(ctx.getString(R.string.city_news));
        faqFragment.setPageTitle(getString(R.string.faq));

        if (profileInfoFragment != null) {
            pageFragmentList.add(profileInfoFragment);
        }
        pageFragmentList.add(myComplaintsFragment);
        pageFragmentList.add(complaintCreateFragment);
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
            Log.e(LOG, "Fuck!", e);
            try {
                Util.showErrorToast(ctx, e.getMessage());
                ACRA.getErrorReporter().handleException(e, false);
                finish();
                Intent w = new Intent(this, CitizenDrawerActivity.class);
                startActivity(w);
            } catch (Exception e2) {
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
                if (menuItem.getItemId() == R.id.nav_mycompl) {
                    mPager.setCurrentItem(1, true);
                    return true;
                }
                if (menuItem.getItemId() == R.id.nav_createComplaint) {
                    mPager.setCurrentItem(2, true);
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            timer.purge();
                            timer.cancel();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    complaintCreateFragment.showComplaintCategoryPopup();
                                }
                            });

                        }
                    }, 500);
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

    @Override
    public void onFindComplaintsLikeMine(ComplaintDTO complaint) {

    }

    @Override
    public void onFindComplaintsAroundMe() {

    }

    @Override
    public void onComplaintAdded(final List<ComplaintDTO> complaintList) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                currentPageIndex = 1;
                response.setComplaintList(complaintList);

                Snackbar.make(mDrawerLayout,"Refreshing list of complaints, will take a minute",
                        Snackbar.LENGTH_LONG).show();
                index = 0;
                getLoginData();

                String ref = "Reference Number: " + complaintList.get(0).getReferenceNumber();
                AlertDialog.Builder d = new AlertDialog.Builder(activity);
                d.setTitle("Complaint Pictures")
                        .setMessage(ref + "\n\nPictures may give the Municipality more information about your complaint.\n\nDo you want to take pictures for the complaint?")
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
            //startLocationUpdates = true;

        }
    }

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
    public void onAccountDetailRequested(ProfileInfoDTO profileInfo) {
        if (profileInfo.getAccountList() == null || profileInfo.getAccountList().isEmpty()) {
            Util.showErrorToast(ctx, "Account information not available at this time");
            return;
        }
        Intent intent = new Intent(ctx, AccountDetailActivity.class);
        intent.putExtra("profileInfo", profileInfo);
        intent.putExtra("logo", logo);
        intent.putExtra("darkColor", themeDarkColor);
        intent.putExtra("primaryColor", themePrimaryColor);
//        startActivityForResult(intent, CHECK_DESTINATION);


        startActivityForResult(intent, CHECK_DESTINATION);


    }

    static final int CHECK_DESTINATION = 9086;

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

    static final float ACCURACY_THRESHOLD = 20f;

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
    public void onRefreshRequested(ComplaintDTO complaint) {
        mRefreshFromComplaint = true;
        getLoginData();
    }

    @Override
    public void onMultiAddressDialog(List<GISAddressDTO> list) {

        AddressDialog addressDialog = new AddressDialog();
        addressDialog.setAddressList(list);
        addressDialog.setListener(new AddressListAdapter.AddressListener() {
            @Override
            public void onAddressClicked(GISAddressDTO address) {

                complaintCreateFragment.setSelectedAddress(address);
            }
        });

        addressDialog.show(getFragmentManager(), "");
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
