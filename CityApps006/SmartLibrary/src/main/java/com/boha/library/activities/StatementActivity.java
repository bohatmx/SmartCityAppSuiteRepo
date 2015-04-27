package com.boha.library.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.boha.library.R;
import com.boha.library.dto.MunicipalityDTO;
import com.boha.library.dto.ProfileInfoDTO;
import com.boha.library.fragments.StatementFragment;
import com.boha.library.services.StatementService;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.CacheUtil;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.ThemeChooser;
import com.boha.library.util.Util;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.List;

public class StatementActivity extends ActionBarActivity implements StatementFragment.StatementFragmentListener{

    StatementFragment statementFragment;
    Context ctx;
    int primaryColor, darkColor, logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeChooser.setTheme(this);
        setContentView(R.layout.activity_statement);
        ctx = getApplicationContext();
        primaryColor = getIntent().getIntExtra("primaryColor", ctx.getResources().getColor(R.color.teal_500));
        darkColor = getIntent().getIntExtra("darkColor", ctx.getResources().getColor(R.color.teal_700));
        logo = getIntent().getIntExtra("logo", R.drawable.ic_action_globe);
        statementFragment = (StatementFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        statementFragment.setThemeColors(primaryColor,darkColor);
        getAccounts();

        MunicipalityDTO municipality = SharedUtil.getMunicipality(ctx);
        ActionBar actionBar = getSupportActionBar();
        Drawable d = ctx.getResources().getDrawable(logo);
        Util.setCustomActionBar(ctx,
                actionBar,
                municipality.getMunicipalityName(), d,logo);
        getSupportActionBar().setTitle("");
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
                    statementFragment.setAccountList(profileInfo.getAccountList());
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
        getMenuInflater().inflate(R.menu.menu_statement, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
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
        if (mBound) {
            mService.downloadPDFs(accountNumber, fileNameList, new StatementService.StatementListener() {
                @Override
                public void onDownloadsComplete(int count) {
                    Log.e(LOG,"+++ Statements downloaded: " + count);
                    statementFragment.downloadsCompleted();
                }
            });
        }
    }
}
