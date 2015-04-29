package com.boha.library.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.adapters.AccountAdapter;
import com.boha.library.dto.AccountDTO;
import com.boha.library.dto.MunicipalityDTO;
import com.boha.library.dto.ProfileInfoDTO;
import com.boha.library.fragments.NavigationDrawerFragment;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.Statics;
import com.boha.library.util.ThemeChooser;
import com.boha.library.util.Util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AccountDetailWithDrawer extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
//    private CharSequence mTitle;
    private ProfileInfoDTO profileInfo;
    private View view, detailView, topView, handle;
    private TextView
            txtName, txtAcctNumber, txtSubtitle,
            txtArrears, txtFAB,
            txtLastUpdate, txtNextBill,
            txtAddress, txtLastBillAmount;
    View fab, topLayout;
    Button btnCurrBal;
    ImageView fabIcon, hero;
    ScrollView scrollView;
    AccountAdapter adapter;
    ImageView icon;

    Activity activity;
    AccountDTO account;
    int darkColor, primaryColor, logo;
    int position;
    String pageTitle;
    Context ctx;
    int selectedIndex;
    static final String LOG = AccountDetailWithDrawer.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeChooser.setTheme(this);
        setContentView(R.layout.activity_fake_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
//        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout), NavigationDrawerFragment.FROM_ACCOUNT);

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
    }

    private void setFields() {
        topView = findViewById(R.id.template);
        fab = findViewById(R.id.FAB);
        hero = (ImageView) findViewById(R.id.TOP_heroImage);
        topLayout = findViewById(R.id.TOP_titleLayout);
        topLayout.setBackgroundColor(primaryColor);
        handle = findViewById(R.id.ACCT_handle);
        scrollView = (ScrollView) findViewById(R.id.ACCT_scroll);
        detailView = findViewById(R.id.ACCT_detailLayout);
        txtName = (TextView) topView.findViewById(R.id.TOP_title);
        Statics.setRobotoFontLight(ctx, txtName);
        txtSubtitle = (TextView) topView.findViewById(R.id.TOP_subTitle);
        icon = (ImageView) topView.findViewById(R.id.TOP_icon);
        txtFAB = (TextView) topView.findViewById(R.id.FAB_text);
        fabIcon = (ImageView) topView.findViewById(R.id.FAB_icon);

        txtAcctNumber = (TextView) findViewById(R.id.ACCT_number);
        txtAddress = (TextView) findViewById(R.id.ACCT_address);
        txtArrears = (TextView) findViewById(R.id.ACCT_currArrears);
        txtLastUpdate = (TextView) findViewById(R.id.ACCT_lastUpdateDate);
        btnCurrBal = (Button) findViewById(R.id.button);
        txtNextBill = (TextView) findViewById(R.id.ACCT_nextBillDate);
        txtLastBillAmount = (TextView) findViewById(R.id.ACCT_lastBillAmount);

        fabIcon.setVisibility(View.GONE);
        txtFAB.setVisibility(View.VISIBLE);
        //btnCurrBal.setTextSize(2f);
        setFont();
        btnCurrBal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(btnCurrBal, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        startPayment();
                    }
                });
            }
        });
        txtArrears.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(txtArrears, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        startPayment();
                    }
                });
            }
        });
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(icon, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        Intent w = new Intent(ctx, StatementActivity.class);
                        w.putExtra("primaryColor", primaryColor);
                        w.putExtra("darkColor", darkColor);
                        w.putExtra("logo", logo);
                        startActivity(w);

                    }
                });

            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(fab, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        if (profileInfo.getAccountList().size() > 1) {
                            List<String> list = new ArrayList<String>();
                            for (AccountDTO a : profileInfo.getAccountList()) {
                                list.add("" + a.getAccountNumber() + " - " + a.getCustomerAccountName());
                            }
                            Util.showPopupBasicWithHeroImage(ctx, activity, list, handle, "Accounts", new Util.UtilPopupListener() {
                                @Override
                                public void onItemSelected(int index) {
                                    account = profileInfo.getAccountList().get(index);
                                    setAccountFields(account);
                                    txtFAB.setText("" + (index + 1));
                                    selectedIndex = index;
                                    Util.expand(detailView, 1000, null);
                                }
                            });
                        }
                    }
                });
            }
        });

        if (profileInfo != null) {
            if (!profileInfo.getAccountList().isEmpty()) {
                txtFAB.setText("" + profileInfo.getAccountList().size());

                account = profileInfo.getAccountList().get(0);
                setAccountFields(account);
            }
        }

        animateSomething();
    }

    private void setAccountFields(AccountDTO account) {
        String currency = "R";
        txtAcctNumber.setText(account.getAccountNumber());
        txtAddress.setText(account.getPropertyAddress());

        txtArrears.setText(currency + df.format(account.getCurrentArrears()));
        btnCurrBal.setText(currency + df.format(account.getCurrentBalance()));
        txtLastBillAmount.setText(currency + df.format(account.getLastBillAmount()));

        txtLastUpdate.setText(sd.format(account.getDateLastUpdated()));
        txtName.setText(account.getCustomerAccountName());
        txtNextBill.setText(sd.format(account.getNextBillDate()));
        txtSubtitle.setText("Date Last Update : " + sd.format(account.getDateLastUpdated()));


        Util.expand(detailView, 1000, null);

    }

    private void setFont() {
        Statics.setRobotoFontLight(ctx, txtAcctNumber);
        Statics.setRobotoFontLight(ctx, txtArrears);
        Statics.setRobotoFontLight(ctx, btnCurrBal);
        Statics.setRobotoFontLight(ctx, txtLastBillAmount);
        Statics.setRobotoFontLight(ctx, txtNextBill);
        Statics.setRobotoFontLight(ctx, txtLastUpdate);


    }

    static final DecimalFormat df = new DecimalFormat("###,###,###,###,###,###,##0.00");

    static final Locale LOCALE = Locale.getDefault();
    static final SimpleDateFormat sd = new SimpleDateFormat("EEEE dd MMMM yyyy", LOCALE);

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.fake_main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            return true;
        }
        if (id == R.id.action_help) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {


        finish();
    }

    @Override
    public void onDestinationSelected(int position, String text) {

        if (position == 0) {
            return;
        }
        Log.i(LOG, "#### onDestinationSelected: " + text);
        Intent w = new Intent();
        w.putExtra("destinationSelected", text);
        w.putExtra("position", position);
        setResult(RESULT_OK, w);
        finish();
//        onBackPressed();
    }

    private void startPayment() {
        Intent w = new Intent(this, PaymentStartActivity.class);
        w.putExtra("account", account);
        w.putExtra("logo",logo);
        w.putExtra("primaryColor",primaryColor);
        w.putExtra("darkColor",darkColor);
        startActivity(w);

    }


    private void animateSomething() {

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_fake_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);

        }
    }

    @Override
    public void onPause() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        super.onPause();
    }

}
