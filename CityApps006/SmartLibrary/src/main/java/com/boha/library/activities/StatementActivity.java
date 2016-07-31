package com.boha.library.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.boha.library.R;
import com.boha.library.dto.AccountDTO;
import com.boha.library.dto.MunicipalityDTO;
import com.boha.library.fragments.StatementListFragment;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.ThemeChooser;
import com.boha.library.util.Util;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class StatementActivity extends AppCompatActivity
        implements StatementListFragment.StatementFragmentListener {

    StatementListFragment statementListFragment;
    Context ctx;
    int primaryColor, darkColor, logo;
    Menu mMenu;
    AccountDTO account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeChooser.setTheme(this);
        setContentView(R.layout.activity_statement);
        ctx = getApplicationContext();
        account = (AccountDTO)getIntent().getSerializableExtra("account");
        primaryColor = getIntent().getIntExtra("primaryColor", ctx.getResources().getColor(R.color.teal_500));
        darkColor = getIntent().getIntExtra("darkColor", ctx.getResources().getColor(R.color.teal_700));
        logo = getIntent().getIntExtra("logo", R.drawable.ic_action_globe);
        statementListFragment = (StatementListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        statementListFragment.setThemeColors(primaryColor, darkColor);
        statementListFragment.setAccount(account);

        MunicipalityDTO municipality = SharedUtil.getMunicipality(ctx);
        ActionBar actionBar = getSupportActionBar();
        Util.setCustomActionBar(ctx,
                actionBar,
                municipality.getMunicipalityName(), ContextCompat.getDrawable(ctx,logo), logo);

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
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(LOG, "## onStop ");

    }



    static final String LOG = StatementActivity.class.getSimpleName();


    ProgressDialog progressDialog;

    @Override
    public void onPDFDownloaded(String fileName) {

    }

    @Override
    public void setBusy(boolean busy) {
        if (busy) {
            progressDialog = Util.showProgressDialog("Account Statement", "Downloading account statement ....", this);
        } else {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }
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
