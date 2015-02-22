package com.boha.library.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.boha.cityapps.R;
import com.boha.library.dto.AlertDTO;
import com.boha.library.fragments.AlertListFragment;
import com.boha.library.fragments.CreateAlertFragment;
import com.boha.library.fragments.ImageGridFragment;
import com.boha.library.fragments.PageFragment;
import com.boha.library.fragments.ProfileInfoFragment;
import com.boha.library.transfer.RequestDTO;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.CacheUtil;
import com.boha.library.util.NetUtil;
import com.boha.library.util.SharedUtil;

import java.util.ArrayList;
import java.util.List;

public class MainPagerActivity extends ActionBarActivity implements
        LocationListener,
        AlertListFragment.AlertListener,
        ProfileInfoFragment.ProfileInfoFragmentListener,
        ImageGridFragment.ImageGridFragmentListener,
        CreateAlertFragment.CreateAlertFragmentListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    PagerAdapter adapter;
    ViewPager mPager;
    int currentPageIndex;
    Context ctx;
    ProfileInfoFragment citizenFragment;
    CreateAlertFragment createAlertFragment;
    ImageGridFragment imageGridFragment;
    AlertListFragment alertListFragment;
    GoogleApiClient apiClient;
    FusedLocationProviderApi locationApi;
    private static final long INTERVAL = 1000;
    private static final long FASTEST_INTERVAL = 500;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ctx = getApplicationContext();
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
        mResolvingError = savedInstanceState != null
                && savedInstanceState.getBoolean(STATE_RESOLVING_ERROR, false);
        mPager = (ViewPager) findViewById(R.id.pager);

        getLoginData();
        buildPages();
        apiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mLocationRequest = LocationRequest.create();
        locationApi = LocationServices.FusedLocationApi;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (apiClient != null) {
            if (!mResolvingError) {
                apiClient.connect();
                Log.i(LOG, "### onStart - apiClient connecting ... ");
            }

        }
    }

    @Override
    public void onStop() {
        Log.d(LOG, "#################### onStop - disconnect apiClient");
        if (apiClient != null) {
            if (apiClient.isConnected()) {
                apiClient.disconnect();
            }
        }
        super.onStop();
    }

    Location mCurrentLocation;
    // Request code to use when launching the resolution activity
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    // Unique tag for the error dialog fragment
    private static final String DIALOG_ERROR = "dialog_error";
    // Bool to track whether the app is already resolving an error
    private boolean mResolvingError = false;

    @Override
    public void onLocationChanged(Location loc) {
        Log.w(LOG, "### Location changed, \nlatitude: "
                + loc.getLatitude() + " longitude: "
                + loc.getLongitude()
                + " -- acc: " + loc.getAccuracy());

        if (loc.getAccuracy() <= ACCURACY_THRESHOLD) {
            mCurrentLocation = loc;
            Log.e(LOG, "+++ best accuracy found: " + mCurrentLocation.getAccuracy());
            locationApi.removeLocationUpdates(apiClient, this);
            if (createAlertFragment != null) {
                createAlertFragment.setLocation(mCurrentLocation);
            }
            if (alertListFragment != null) {
                alertListFragment.setLocation(mCurrentLocation);
            }

        }

    }

    LocationRequest mLocationRequest;
    static final int ACCURACY_THRESHOLD = 30;

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(LOG,
                "### ---> PlayServices onConnected() - gotta start something! >>");
        mCurrentLocation = locationApi.getLastLocation(apiClient);
        if (mCurrentLocation != null) {
            Log.w(LOG, "### LAST Location: \nlatitude: "
                    + mCurrentLocation.getLatitude() + " longitude: "
                    + mCurrentLocation.getLongitude()
                    + " -- acc: " + mCurrentLocation.getAccuracy());
        }
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationApi.requestLocationUpdates(apiClient, mLocationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(LOG, "--- onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(LOG,
                "### ---> PlayServices onConnectionFailed: " + connectionResult.toString());
        if (mResolvingError) {
            // Already attempting to resolve an error.
            return;
        } else if (connectionResult.hasResolution()) {
            try {
                mResolvingError = true;
                connectionResult.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.
                apiClient.connect();
            }
        } else {
            // Show dialog using GooglePlayServicesUtil.getErrorDialog()
            showErrorDialog(connectionResult.getErrorCode());
            mResolvingError = true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_RESOLVE_ERROR) {
            mResolvingError = false;
            if (resultCode == RESULT_OK) {
                // Make sure the app is not already connected or attempting to connect
                if (!apiClient.isConnecting() &&
                        !apiClient.isConnected()) {
                    apiClient.connect();
                }
            }
        }
    }

    private static final String STATE_RESOLVING_ERROR = "resolving_error";

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_RESOLVING_ERROR, mResolvingError);
    }

    /* Creates a dialog for an error message */
    private void showErrorDialog(int errorCode) {
        // Create a fragment for the error dialog
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        // Pass the error that should be displayed
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(getSupportFragmentManager(), "errordialog");
    }

    /* Called from ErrorDialogFragment when the dialog is dismissed. */
    public void onDialogDismissed() {
        mResolvingError = false;
    }

    @Override
    public void onAlertClicked(AlertDTO alert) {

    }

    /* A fragment to display an error dialog */
    public static class ErrorDialogFragment extends DialogFragment {
        public ErrorDialogFragment() {
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Get the error code and retrieve the appropriate dialog
            int errorCode = this.getArguments().getInt(DIALOG_ERROR);
            return GooglePlayServicesUtil.getErrorDialog(errorCode,
                    this.getActivity(), REQUEST_RESOLVE_ERROR);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            ((MainPagerActivity) getActivity()).onDialogDismissed();
        }
    }

    private void getLoginData() {
        final RequestDTO w = new RequestDTO(RequestDTO.LOGIN);
        w.setUserName(SharedUtil.getID(ctx));
        w.setPassword(SharedUtil.getPassword(ctx));

        NetUtil.sendRequest(ctx, w, new NetUtil.NetUtilListener() {
            @Override
            public void onResponse(ResponseDTO response) {
                CacheUtil.cacheLoginData(ctx, response, null);
            }

            @Override
            public void onError(String message) {

            }

            @Override
            public void onWebSocketClose() {

            }
        });
    }

    private void buildPages() {
        pageFragmentList = new ArrayList<>();

        citizenFragment = ProfileInfoFragment.newInstance();
        createAlertFragment = CreateAlertFragment.newInstance();
        imageGridFragment = ImageGridFragment.newInstance();
        alertListFragment = AlertListFragment.newInstance();


        pageFragmentList.add(citizenFragment);
        pageFragmentList.add(imageGridFragment);
        pageFragmentList.add(createAlertFragment);
        pageFragmentList.add(alertListFragment);


        adapter = new PagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(adapter);
        PagerSlidingTabStrip strip = (PagerSlidingTabStrip) findViewById(R.id.pager_title_strip);
        strip.setAllCaps(false);
        strip.setIndicatorColor(ctx.getResources().getColor(R.color.absa_red));
        strip.setUnderlineColor(ctx.getResources().getColor(R.color.blue));
        strip.setViewPager(mPager);
        strip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAccountDetailRequired() {

    }

    @Override
    public void onAlertSent(AlertDTO alert) {

    }

    @Override
    public void onPictureClicked(int position) {

    }

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
                title = getString(R.string.info);
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

            return title;
        }
    }

    Drawable myDrawable;

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    List<PageFragment> pageFragmentList;
    static final String LOG = MainPagerActivity.class.getSimpleName();

}
