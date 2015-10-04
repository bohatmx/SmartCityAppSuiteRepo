package com.boha.library.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.dto.AccountDTO;
import com.boha.library.dto.MunicipalityDTO;
import com.boha.library.dto.PaymentSurveyDTO;
import com.boha.library.dto.ProfileInfoDTO;
import com.boha.library.transfer.RequestDTO;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.CacheUtil;
import com.boha.library.util.NetUtil;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.Statics;
import com.boha.library.util.ThemeChooser;
import com.boha.library.util.Util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class AccountDetailActivity extends AppCompatActivity {

    private ProfileInfoDTO profileInfo;
    private View view, detailView, topView, handle;
    private TextView
            txtName, txtAcctNumber, txtSubtitle,
            txtArrears,
            txtLastUpdate, txtNextBill,
            txtAddress, txtLastBillAmount;
    View  topLayout;
    Button btnCurrBal;
    ImageView  hero;
    ScrollView scrollView;
    ImageView icon;
    FloatingActionButton fab;

    AccountDTO account;
    int darkColor, primaryColor, logo;
    Context ctx;
    int selectedIndex;
    static final String LOG = AccountDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeChooser.setTheme(this);
        setContentView(R.layout.fragment_account);

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
        fab = (FloatingActionButton)findViewById(R.id.fab);
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
//        txtFAB = (TextView) topView.findViewById(R.id.FAB_text);
//        fabIcon = (ImageView) topView.findViewById(R.id.FAB_icon);

        txtAcctNumber = (TextView) findViewById(R.id.ACCT_number);
        txtAddress = (TextView) findViewById(R.id.ACCT_address);
        txtArrears = (TextView) findViewById(R.id.ACCT_currArrears);
        txtLastUpdate = (TextView) findViewById(R.id.ACCT_lastUpdateDate);
        btnCurrBal = (Button) findViewById(R.id.button);
        txtNextBill = (TextView) findViewById(R.id.ACCT_nextBillDate);
        txtLastBillAmount = (TextView) findViewById(R.id.ACCT_lastBillAmount);

//        fabIcon.setVisibility(View.GONE);
//        txtFAB.setVisibility(View.VISIBLE);
        hero.setImageDrawable(Util.getRandomBackgroundImage(ctx));
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
               showDialog();

            }
        });

        setProfileData();

        animateSomething();
    }

    private void showDialog() {
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
    }
    private void setProfileData() {
        if (profileInfo != null) {
            if (!profileInfo.getAccountList().isEmpty()) {
                account = profileInfo.getAccountList().get(0);
                setAccountFields(account);
            }
        }
    }

    private void setAccountFields(AccountDTO account) {
        String currency = "R";
        txtAcctNumber.setText(account.getAccountNumber());
        txtAddress.setText(account.getPropertyAddress());

        if (account.getCurrentArrears() != null) {
            txtArrears.setText(currency + df.format(account.getCurrentArrears()));
        } else {
            txtArrears.setText("0.00");
        }
        if (account.getCurrentBalance() != null) {
            btnCurrBal.setText(currency + df.format(account.getCurrentBalance()));
        } else {
            btnCurrBal.setText("0.00");
        }
        if (account.getLastBillAmount() != null) {
            txtLastBillAmount.setText(currency + df.format(account.getLastBillAmount()));
        } else {
            txtLastBillAmount.setText("0.00");
        }
        if (account.getDateLastUpdated() != null) {
            txtSubtitle.setText(ctx.getString(R.string.last_update) + sd.format(account.getDateLastUpdated()));
            txtLastUpdate.setText(sd.format(account.getDateLastUpdated()));
        } else {
            txtLastUpdate.setText(R.string.not_available);
        }
        txtName.setText(account.getCustomerAccountName());
        if (account.getNextBillDate() != null) {
            txtNextBill.setText(sd.format(account.getNextBillDate()));
        }


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


    Menu mMenu;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fake_main, menu);
        mMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            getLoginData();
            return true;
        }
        if (id == R.id.action_help) {
            Util.showToast(ctx, getString(R.string.under_cons));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {


        finish();
    }


    private void startPayment() {
        Log.e(LOG, "########## startPayment");

        boolean isDebuggable = 0 != (this.getApplicationInfo().flags
                &= ApplicationInfo.FLAG_DEBUGGABLE);

        if (isDebuggable) {
            Intent intent = new Intent(ctx, PaymentStartActivity.class);
            intent.putExtra("account", account);
            intent.putExtra("index", selectedIndex);
            intent.putExtra("logo", logo);
            startActivity(intent);
        } else {
            AlertDialog.Builder d = new AlertDialog.Builder(this);
            d.setTitle("Mobile Payment Survey")
                    .setMessage("The payment facility is not available yet. The municipality is conducting a survey to find the level of interest in paying your account on the app.\n\n" +
                            "Do you want to be able to pay from the app?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sendSurvey(true);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sendSurvey(false);
                        }
                    })
                    .show();
        }


//        Intent intent = new Intent(ctx, PaymentStartActivity.class);
//        intent.putExtra("account", account);
//        intent.putExtra("index", selectedIndex);
//        intent.putExtra("logo", logo);
//        startActivity(intent);

    }

    private void sendSurvey(boolean response) {
        PaymentSurveyDTO x = new PaymentSurveyDTO();
        x.setMunicipalityID(SharedUtil.getMunicipality(getApplicationContext()).getMunicipalityID());
        x.setResponse(response);
        x.setAccountNumber(account.getAccountNumber());

        RequestDTO w = new RequestDTO(RequestDTO.ADD_SURVEY);
        w.setPaymentSurvey(x);

        setRefreshActionButtonState(true);
        NetUtil.sendRequest(getApplicationContext(), w, new NetUtil.NetUtilListener() {
            @Override
            public void onResponse(ResponseDTO response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setRefreshActionButtonState(false);
                    }
                });
            }

            @Override
            public void onError(final String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setRefreshActionButtonState(false);
                        Util.showErrorToast(getApplicationContext(), message);
                    }
                });
            }

            @Override
            public void onWebSocketClose() {

            }
        });
    }


    private void animateSomething() {

    }

    @Override
    public void onPause() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        super.onPause();
    }

    private void getLoginData() {
        Log.e(LOG, "@@@@@@@@@ getLoginData ...... ");

        final RequestDTO w = new RequestDTO(RequestDTO.SIGN_IN_CITIZEN);

        w.setUserName(profileInfo.getiDNumber());
        w.setPassword(profileInfo.getPassword());

        w.setMunicipalityID(SharedUtil.getMunicipality(ctx).getMunicipalityID());
        setRefreshActionButtonState(true);
        NetUtil.sendRequest(ctx, w, new NetUtil.NetUtilListener() {
            @Override
            public void onResponse(final ResponseDTO resp) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setRefreshActionButtonState(false);
                        if (resp.getProfileInfoList() != null) {
                            profileInfo = resp.getProfileInfoList().get(0);
                            setProfileData();
                        }

                        CacheUtil.cacheLoginData(ctx, resp, new CacheUtil.CacheListener() {
                            @Override
                            public void onDataCached() {
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
                        setRefreshActionButtonState(false);
                        Util.showErrorToast(ctx, "Network connection closed");
                    }
                });
            }
        });

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
}
