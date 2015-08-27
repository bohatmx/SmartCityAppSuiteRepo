package com.boha.library.activities;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.boha.library.R;
import com.boha.library.dto.AccountDTO;
import com.boha.library.dto.MunicipalityDTO;
import com.boha.library.fragments.PaymentStartFragment;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.ThemeChooser;
import com.boha.library.util.Util;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class PaymentStartActivity extends AppCompatActivity
        implements PaymentStartFragment.PaymentStartListener{

    AccountDTO account;
    PaymentStartFragment paymentStartFragment;
    int themeDarkColor, themePrimaryColor, logo = R.drawable.ic_action_globe;
    Context ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeChooser.setTheme(this);
        setContentView(R.layout.activity_payment_start);
        ctx = getApplicationContext();

        account = (AccountDTO)getIntent().getSerializableExtra("account");
        logo = getIntent().getIntExtra("logo",R.drawable.ic_action_globe);
        themeDarkColor = getIntent().getIntExtra("darkColor",R.color.blue_900);
        themePrimaryColor = getIntent().getIntExtra("primaryColor",R.color.blue_500);
        int index = getIntent().getIntExtra("index",0);
        paymentStartFragment = (PaymentStartFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment);
        paymentStartFragment.setAccount(account, index);
        paymentStartFragment.setLogo(logo);


        MunicipalityDTO municipality = SharedUtil.getMunicipality(getApplicationContext());
            Drawable d = ContextCompat.getDrawable(ctx,logo);
            Util.setCustomActionBar(ctx,
                    getSupportActionBar(),
                    municipality.getMunicipalityName(), d,logo);
            getSupportActionBar().setTitle("");

        //Track PaymentStartActivity
        CityApplication ca = (CityApplication) getApplication();
        Tracker t = ca.getTracker(
                CityApplication.TrackerName.APP_TRACKER);
        t.setScreenName(PaymentStartActivity.class.getSimpleName());
        t.send(new HitBuilders.ScreenViewBuilder().build());
        //
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(themeDarkColor);
            window.setNavigationBarColor(themeDarkColor);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_payment_start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onPause() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        super.onPause();
    }

    @Override
    public void onPaymentTypeSelected(int paymentType) {

    }
}
