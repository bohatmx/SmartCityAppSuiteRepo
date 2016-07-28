package com.boha.library.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.dto.AccountDTO;
import com.boha.library.dto.MunicipalityDTO;
import com.boha.library.dto.ProfileInfoDTO;
import com.boha.library.fragments.AccountFragment;
import com.boha.library.fragments.PageFragment;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.ThemeChooser;
import com.boha.library.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AccountDetailActivity extends AppCompatActivity implements AccountFragment.AccountFragmentListener{

    private ProfileInfoDTO profileInfo;
    private ViewPager viewPager;

    int darkColor, primaryColor, logo;
    Context ctx;
    int selectedIndex;
    static final String LOG = AccountDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeChooser.setTheme(this);
        setContentView(R.layout.activity_account_detail);

        Log.d(LOG, "@@@@@@@ onCreate");
        ctx = getApplicationContext();
        profileInfo = (ProfileInfoDTO) getIntent().getSerializableExtra("profileInfo");
        darkColor = getIntent().getIntExtra("darkColor", R.color.black);
        primaryColor = getIntent().getIntExtra("primaryColor", R.color.black);
        logo = getIntent().getIntExtra("logo", R.drawable.ic_action_globe);
        setFields();
        MunicipalityDTO municipality = SharedUtil.getMunicipality(getApplicationContext());
        Drawable d = ctx.getResources().getDrawable(logo);
        Util.setCustomActionBar(ctx,
                getSupportActionBar(),
                municipality.getMunicipalityName(), d, logo);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(darkColor);
            window.setNavigationBarColor(primaryColor);
        }
        isDebuggable = 0 != (this.getApplicationInfo().flags
                &= ApplicationInfo.FLAG_DEBUGGABLE);

    }

    PagerAdapter adapter;
    TextView name, acctNumber;
    ImageView icon;
    AccountDTO account;

    private void setFields() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        name = (TextView) findViewById(R.id.TOP_title);
        acctNumber = (TextView) findViewById(R.id.accountNumber);
        fab = (FloatingActionButton) findViewById(R.id.fabButton);
        icon = (ImageView) findViewById(R.id.TOP_icon);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar = Snackbar.make(fab, "Mobile payment will be available shortly", Snackbar.LENGTH_INDEFINITE);
                snackbar.setActionTextColor(Color.parseColor("GREEN"));
                snackbar.setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
            }
        });

        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo get account statement
            }
        });
        adapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position < profileInfo.getAccountList().size()) {
                    account = profileInfo.getAccountList().get(position);
                    name.setText(account.getCustomerAccountName());
                    acctNumber.setText(account.getAccountNumber());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        createFragments();
    }

    private void createFragments() {
        for (AccountDTO acc : profileInfo.getAccountList()) {
            AccountFragment f = AccountFragment.newInstance(acc);
            pageFragmentList.add(f);
        }
        adapter.notifyDataSetChanged();

        initializeTimerTask();
        startTimer();

    }

    private void showDialog() {
        if (isDebuggable) {
            AlertDialog.Builder d = new AlertDialog.Builder(this);
            d.setTitle("Payment Reports")
                    .setMessage("Please select the kind of payment report.")
                    .setPositiveButton("Instant EFT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent w = new Intent(ctx, SIDPaymentsActivity.class);
                            w.putExtra("primaryColor", primaryColor);
                            w.putExtra("logo", logo);
                            startActivity(w);
                        }
                    })
                    .setNegativeButton("Card Payments", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent w = new Intent(ctx, CardPaymentsActivity.class);
                            w.putExtra("primaryColor", primaryColor);
                            w.putExtra("logo", logo);
                            startActivity(w);
                        }
                    })
                    .show();
        } else {

        }
    }

    int index;
    Timer timer;
    TimerTask timerTask;
    final Handler handler = new Handler();
    Menu mMenu;
    boolean isDebuggable;
    FloatingActionButton fab;
    Snackbar snackbar;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_faq, menu);
        mMenu = menu;
        return true;
    }

    int themeDarkColor;

    static final int THEME_REQUESTED = 8075;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == com.boha.library.R.id.action_app_guide) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://etmobileguide.oneconnectgroup.com/"));
            startActivity(intent);
        }
        if (id == com.boha.library.R.id.action_theme) {
            Intent w = new Intent(this, ThemeSelectorActivity.class);
            w.putExtra("darkColor", themeDarkColor);
            startActivityForResult(w, THEME_REQUESTED);
            return true;
        }
        if (id == R.id.action_info) {
            Intent intent = new Intent(AccountDetailActivity.this, GeneralInfoActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == com.boha.library.R.id.action_emergency) {
            Intent intent = new Intent(AccountDetailActivity.this, EmergencyContactsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {


            finish();
    }



    private void animateSomething() {

    }

    @Override
    public void onPause() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        super.onPause();
    }


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
    public void onAccountStatementRequested(AccountDTO account) {

    }

    @Override
    public void onRefreshRequested() {

    }

    @Override
    public void setBusy(boolean busy) {

    }

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

    static List<PageFragment> pageFragmentList = new ArrayList<>();
    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer.schedule(timerTask, 300, 500); //
    }

    public void stopTimerTask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {

                //use a handler to run a toast that shows the current timestamp
                handler.post(new Runnable() {
                    public void run() {
                        index++;
                        if (index == pageFragmentList.size()) {
                            stopTimerTask();
                            viewPager.setCurrentItem(0);
                            name.setText(profileInfo.getAccountList().get(0).getCustomerAccountName());
                            acctNumber.setText(profileInfo.getAccountList().get(0).getAccountNumber());
                            return;
                        }
                        viewPager.setCurrentItem(index);
                        name.setText(profileInfo.getAccountList().get(index).getCustomerAccountName());
                        acctNumber.setText(profileInfo.getAccountList().get(index).getAccountNumber());
                    }
                });
            }
        };
    }
}
