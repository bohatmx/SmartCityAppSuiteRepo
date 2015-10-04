package com.boha.library.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.dto.ProfileInfoDTO;
import com.boha.library.dto.SIDPaymentRequestDTO;
import com.boha.library.dto.SIDResponseDTO;
import com.boha.library.transfer.RequestDTO;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.CacheUtil;
import com.boha.library.util.NetUtil;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.ThemeChooser;
import com.boha.library.util.Util;

import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import khandroid.ext.apache.http.util.EncodingUtils;

public class SIDPaymentsActivity extends AppCompatActivity {

    TextView txtTitle, txtSubTitle;
    WebView webView;
    View responseView;
    SIDPaymentRequestDTO paymentRequest;
    SIDResponseDTO sidResponse;
    FloatingActionButton fab;

    private static final String TEXT = "text/html", UTF = "UTF-8",
            LOG = SIDPaymentsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeChooser.setTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        paymentRequest = (SIDPaymentRequestDTO) getIntent().getSerializableExtra("paymentRequest");
        sidResponse = (SIDResponseDTO) getIntent().getSerializableExtra("SIDResponse");

        webView = (WebView) findViewById(R.id.AWB_webView);
        responseView = findViewById(R.id.AWB_responseView);
        txtTitle = (TextView) findViewById(R.id.AWB_title);
        txtSubTitle = (TextView) findViewById(R.id.AWB_subtitle);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        txtSubTitle.setText("Instant EFT");
        //
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);

        String str = SharedUtil.getMunicipality(getApplicationContext()).getMunicipalityName();

        Util.setCustomActionBar(getApplicationContext(),
                getSupportActionBar(), str,
                ContextCompat.getDrawable(getApplicationContext(),
                        R.drawable.ic_action_secure), 0);
        if (paymentRequest != null) {
            setWebView();
        } else {
            if (sidResponse != null) {
                setResponse();
            } else {
                responseView.setVisibility(View.GONE);
                getCachedPaymentResponses();
            }

        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCachedPaymentResponses();
            }
        });
    }

    private void getCachedPaymentResponses() {
        CacheUtil.getCachedSIDResponses(getApplicationContext(), new CacheUtil.CacheRetrievalListener() {
            @Override
            public void onCacheRetrieved(ResponseDTO response) {
                if (response.getSidResponseList() != null) {
                    displayPaymentHistory(response.getSidResponseList());
                }
                refreshSIDResponses();
            }

            @Override
            public void onError() {

            }
        });
    }

    private void refreshSIDResponses() {
        RequestDTO w = new RequestDTO(RequestDTO.GET_SID_PAYMENT_RESPONSES);
        ProfileInfoDTO p = SharedUtil.getProfile(getApplicationContext());
        ProfileInfoDTO x = new ProfileInfoDTO();
        x.setProfileInfoID(p.getProfileInfoID());
        w.setProfileInfo(x);

        setRefreshActionButtonState(true);
        NetUtil.sendRequest(getApplicationContext(), w, new NetUtil.NetUtilListener() {
            @Override
            public void onResponse(final ResponseDTO response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setRefreshActionButtonState(false);
                        displayPaymentHistory(response.getSidResponseList());
                        CacheUtil.cacheSIDResponses(getApplicationContext(), response, null);
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

    private void displayPaymentHistory(List<SIDResponseDTO> list) {

        if (list == null || list.isEmpty()) {
            Util.showToast(getApplicationContext(), "No Instant EFT payment responses found");
            finish();
            return;
        }
        Util.collapse(responseView,1000,null);

        TableLayout table = (TableLayout) findViewById(R.id.AWB_tableLayout);
        TableRow header = (TableRow) getLayoutInflater().inflate(R.layout.sid_response_header, null);
        table.removeAllViews();
        table.addView(header);

        int count = 0, completed = 0;
        double totalAmount = 0.0;
        for (SIDResponseDTO x : list) {
            TableRow row = (TableRow) getLayoutInflater().inflate(R.layout.sid_response_row, null);
            TextView status = (TextView) row.findViewById(R.id.SIDROW_status);
            TextView amount = (TextView) row.findViewById(R.id.SIDROW_amount);
            TextView ref = (TextView) row.findViewById(R.id.SIDROW_ref);
            TextView date = (TextView) row.findViewById(R.id.SIDROW_date);
            status.setText(x.getStatus());
            amount.setText(df.format(x.getAmount()));
            ref.setText(x.getReference());
            if (x.getDateRegistered() != null) {
                date.setText(sdf.format(new Date(x.getDateRegistered())));
            } else {
                date.setText(x.getDate());
            }
            if (x.getStatus().equalsIgnoreCase("COMPLETED")) {
                completed++;
                status.setTextColor(getResources().getColor(R.color.teal_800));
            } else {
                status.setTextColor(getResources().getColor(R.color.red_800));
                amount.setTextColor(getResources().getColor(R.color.red_800));
            }
            table.addView(row);
            totalAmount += x.getAmount().doubleValue();
            count++;
            if (list.size() > 30) {
                int rem = count % 20;
                if (rem == 0) {
                    table.addView(header);
                }
            }
        }

        TableRow footer = (TableRow) getLayoutInflater().inflate(R.layout.sid_response_footer, null);
        TextView status = (TextView) footer.findViewById(R.id.SIDROW_status);
        TextView amount = (TextView) footer.findViewById(R.id.SIDROW_amount);
        TextView date = (TextView) footer.findViewById(R.id.SIDROW_date);

        status.setText("");
        amount.setText(df.format(totalAmount));
        date.setText("Payments: " + completed);

        table.addView(footer);
        table.setVisibility(View.VISIBLE);

    }

    private void setResponse() {
        webView.setVisibility(View.GONE);
        responseView.setVisibility(View.VISIBLE);
        TextView txtStatus = (TextView) findViewById(R.id.SIDR_status);
        TextView txtBank = (TextView) findViewById(R.id.SIDR_bank);
        TextView txtDate = (TextView) findViewById(R.id.SIDR_date);
        TextView txtRecept = (TextView) findViewById(R.id.SIDR_receipt);
        TextView txtAmt = (TextView) findViewById(R.id.SIDR_amount);
        TextView txtRef = (TextView) findViewById(R.id.SIDR_reference);
        ImageView logo = (ImageView) findViewById(R.id.SIDR_bankIcon);
        Button btn = (Button) findViewById(R.id.SIDR_btnClose);

        txtStatus.setText(sidResponse.getStatus());
        txtBank.setText(sidResponse.getBank());
        txtDate.setText(sidResponse.getDate());
        txtRecept.setText(sidResponse.getReceiptNumber());
        txtAmt.setText(df.format(sidResponse.getAmount()));
        txtRef.setText(sidResponse.getReference());


        if (sidResponse.getStatus().equalsIgnoreCase("COMPLETED")) {
            txtStatus.setTextColor(getResources().getColor(R.color.teal_800));
        }
        if (sidResponse.getStatus().equalsIgnoreCase("CANCELLED")) {
            txtStatus.setTextColor(getResources().getColor(R.color.red_800));
        }
        if (sidResponse.getStatus().equalsIgnoreCase("DECLINED")) {
            txtStatus.setTextColor(getResources().getColor(R.color.purple_700));
        }
        if (sidResponse.getBank() != null) {
            if (sidResponse.getBank().contains("Absa")) {
                logo.setImageDrawable(ContextCompat.getDrawable(
                        getApplicationContext(), R.drawable.absa_logo_red));
            }
            if (sidResponse.getBank().contains("Nedbank")) {
                logo.setImageDrawable(ContextCompat.getDrawable(
                        getApplicationContext(), R.drawable.nedbank2));
            }
            if (sidResponse.getBank().contains("FNB")) {
                logo.setImageDrawable(ContextCompat.getDrawable(
                        getApplicationContext(), R.drawable.fnb_logo));
            }
            if (sidResponse.getBank().contains("Standard")) {
                logo.setImageDrawable(ContextCompat.getDrawable(
                        getApplicationContext(), R.drawable.standard_bank_logo));
            }
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    static final DecimalFormat df = new DecimalFormat("###,###,###,###,###,##0.00");
    static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    private void setWebView() {
        webView.setVisibility(View.VISIBLE);
        responseView.setVisibility(View.GONE);
        StringBuilder sb = new StringBuilder();

        sb.append(SIDPaymentRequestDTO.SID_MERCHANT).append("=").append(paymentRequest.getMerchant()).append("&");
        sb.append(SIDPaymentRequestDTO.SID_CURRENCY).append("=").append(paymentRequest.getCurrency()).append("&");
        sb.append(SIDPaymentRequestDTO.SID_COUNTRY).append("=").append(paymentRequest.getCountry()).append("&");
        sb.append(SIDPaymentRequestDTO.SID_REFERENCE).append("=").append(paymentRequest.getReference()).append("&");
        sb.append(SIDPaymentRequestDTO.SID_AMOUNT).append("=").append(paymentRequest.getAmount()).append("&");
        sb.append(SIDPaymentRequestDTO.SID_CONSISTENT).append("=").append(paymentRequest.getConsistentKey());

        String parms = sb.toString();
        Log.d(LOG, parms);

        setRefreshActionButtonState(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.d(LOG, "###onPageStarted ... " + url);
                setRefreshActionButtonState(true);
                if (url.contains("/sc/")) {
                    Util.showToast(getApplicationContext(),
                            "Transaction is being processed. Watch for the notification");
                    finish();
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.i(LOG, "###onPageFinished ... " + url);
                setRefreshActionButtonState(false);
            }


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i(LOG, "shouldOverrideUrlLoading: " + url);

                return false;
            }
        });


        webView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                Log.w(LOG, "onViewAttachedToWindow");
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                Log.w(LOG, "onViewDetachedFromWindow");
            }
        });
        webView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                Log.w(LOG, "onLayoutChange");
            }
        });
        webView.postUrl(SIDPaymentRequestDTO.SID_URL, EncodingUtils.getBytes(parms, "BASE64"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fake_main, menu);
        mMenu = menu;
        return true;
    }

    static final int THEME_REQUESTED = 8075;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            refreshSIDResponses();
            return true;
        }
        if (id == R.id.action_help) {
            Util.showToast(getApplicationContext(), "Help will be available soon!");
            return true;
        }
        if (id == android.R.id.home) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    Menu mMenu;

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
