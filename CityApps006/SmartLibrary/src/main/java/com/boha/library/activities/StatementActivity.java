package com.boha.library.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.boha.library.R;
import com.boha.library.dto.MunicipalityDTO;
import com.boha.library.dto.ProfileInfoDTO;
import com.boha.library.fragments.StatementListFragment;
import com.boha.library.services.StatementService;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.CacheUtil;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.ThemeChooser;
import com.boha.library.util.Util;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.List;

public class StatementActivity extends AppCompatActivity
        implements StatementListFragment.StatementFragmentListener {

    StatementListFragment statementListFragment;
    Context ctx;
    int primaryColor, darkColor, logo;
    Menu mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeChooser.setTheme(this);
        setContentView(R.layout.activity_statement);
        ctx = getApplicationContext();
        primaryColor = getIntent().getIntExtra("primaryColor", ctx.getResources().getColor(R.color.teal_500));
        darkColor = getIntent().getIntExtra("darkColor", ctx.getResources().getColor(R.color.teal_700));
        logo = getIntent().getIntExtra("logo", R.drawable.ic_action_globe);
        statementListFragment = (StatementListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        statementListFragment.setThemeColors(primaryColor, darkColor);
        getAccounts();

        MunicipalityDTO municipality = SharedUtil.getMunicipality(ctx);
        ActionBar actionBar = getSupportActionBar();
        Drawable d = ctx.getResources().getDrawable(logo);
        Util.setCustomActionBar(ctx,
                actionBar,
                municipality.getMunicipalityName(), d, logo);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(darkColor);
            window.setNavigationBarColor(darkColor);
        }
        //Track analytics
        CityApplication ca = (CityApplication) getApplication();
        Tracker t = ca.getTracker(
                CityApplication.TrackerName.APP_TRACKER);
        t.setScreenName(StatementActivity.class.getSimpleName());
        t.send(new HitBuilders.ScreenViewBuilder().build());

    }

    private void getAccounts() {
        CacheUtil.getCacheLoginData(ctx, new CacheUtil.CacheRetrievalListener() {
            @Override
            public void onCacheRetrieved(ResponseDTO response) {
                if (response.getProfileInfoList() != null && !response.getProfileInfoList().isEmpty()) {
                    ProfileInfoDTO profileInfo = response.getProfileInfoList().get(0);
                    if (profileInfo.getAccountList().isEmpty()) {
                        Log.e(LOG, "## Profile has no accounts");
                        finish();
                    } else {
                        statementListFragment.setAccountList(profileInfo.getAccountList());
                    }
                }
            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_faq, menu);
        menu.getItem(0).setVisible(false);
        mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        /*if (id == R.id.action_refresh) {
            return true;
        } */
        if(id == com.boha.library.R.id.action_app_guide) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://etmobileguide.oneconnectgroup.com/"));
            startActivity(intent);
        }
        if (id == R.id.action_info) {
            Intent intent = new Intent(StatementActivity.this, GeneralInfoActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == com.boha.library.R.id.action_emergency) {
            Intent intent = new Intent(StatementActivity.this, EmergencyContactsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {

        Log.w(LOG, "## onStart Bind to StatementService");
        Intent intent = new Intent(this, StatementService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();

        Log.e(LOG, "## onStop unBind from StatementService");
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }

    }


    boolean mBound;
    StatementService mService;

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            Log.e(LOG, "## StatementService ServiceConnection onServiceConnected");
            StatementService.LocalBinder binder = (StatementService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
            if (accountNumber != null && fileNameList != null) {
                mService.downloadPDFs(accountNumber, fileNameList, new StatementService.StatementListener() {
                    @Override
                    public void onDownloadsComplete(int count) {
                        Log.e(LOG, "Statements downloaded: " + count);
                        statementListFragment.downloadsCompleted();
                        accountNumber = null;
                        fileNameList = null;
                    }
                });
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.w(LOG, "## StatementService onServiceDisconnected");
            mBound = false;
        }
    };


    static final String LOG = StatementActivity.class.getSimpleName();

    @Override
    public void onPDFDownloadRequested(String accountNumber, List<String> fileNameList) {
        this.accountNumber = accountNumber;
        this.fileNameList = fileNameList;
        if (mBound) {
            mService.downloadPDFs(accountNumber, fileNameList, new StatementService.StatementListener() {
                @Override
                public void onDownloadsComplete(int count) {
                    Log.e(LOG, "+++ Statements downloaded: " + count);
                    statementListFragment.downloadsCompleted();

                }
            });
        } else {
            Log.w(LOG, "## onStart Bind to StatementService");
            Intent intent = new Intent(this, StatementService.class);
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        }
    }

    String accountNumber;
    List<String> fileNameList;

    @Override
    public void setBusy(boolean busy) {
        setRefreshActionButtonState(busy);
    }

    @Override
    public void onPause() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        super.onPause();
    }

    public void setRefreshActionButtonState(final boolean refreshing) {
        if (mMenu != null) {
            final MenuItem refreshItem = mMenu.findItem(R.id.action_help);
            if (refreshItem != null) {
                if (refreshing) {
                    refreshItem.setActionView(R.layout.action_bar_progess);
                } else {
                    refreshItem.setActionView(null);
                }
            }
        }
    }
}
