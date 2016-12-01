package com.boha.library.activities;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.boha.library.R;
import com.boha.library.dto.MunicipalityDTO;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.ThemeChooser;
import com.boha.library.util.Util;

public class FaqActivity extends AppCompatActivity /*implements FaqFragment.FaqListener*/ {

    /*FaqFragment faqFragment;*/
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = getApplicationContext();
        ThemeChooser.setTheme(this);
        setContentView(R.layout.activity_faq);
        Resources.Theme theme = getTheme();
        TypedValue typedValue = new TypedValue();
        theme.resolveAttribute(com.boha.library.R.attr.colorPrimaryDark, typedValue, true);
        int themeDarkColor = typedValue.data;

        /*faqFragment = (FaqFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        faqFragment.setThemeColors(0,themeDarkColor);*/

        MunicipalityDTO municipality = SharedUtil.getMunicipality(ctx);
        int logo = getIntent().getIntExtra("logo", R.drawable.ic_action_globe);

        ActionBar actionBar = getSupportActionBar();
        Util.setCustomActionBar(ctx,
                actionBar,
                municipality.getMunicipalityName(),
                ctx.getResources().getDrawable(logo), logo);
        getSupportActionBar().setTitle("");

        //Track analytics
//        CityApplication ca = (CityApplication) getApplication();
//        Tracker t = ca.getTracker(
//                CityApplication.TrackerName.APP_TRACKER);
//        t.setScreenName(FaqActivity.class.getSimpleName());
//        t.send(new HitBuilders.ScreenViewBuilder().build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(themeDarkColor);
            window.setNavigationBarColor(themeDarkColor);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_faq, menu);
        mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
          //  faqFragment.getRemoteFAQs();
            return true;
        }
        if (id == R.id.action_help) {
            Util.showToast(ctx,getString(R.string.under_cons));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    Menu mMenu;
    public void setRefreshActionButtonState(final boolean busy) {
        if (mMenu != null) {
            final MenuItem refreshItem = mMenu.findItem(R.id.action_refresh);
            if (refreshItem != null) {
                if (busy) {
                    refreshItem.setActionView(R.layout.action_bar_progess);
                } else {
                    refreshItem.setActionView(null);
                }
            }
        }
    }

   /* @Override
    public void setBusy(boolean busy) {
        setRefreshActionButtonState(busy);
    }*/

    /*@Override
    public void onFaqCLicked() {

    }*/
}
