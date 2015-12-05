package com.boha.library.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.dto.CardResponseDTO;
import com.boha.library.dto.ProfileInfoDTO;
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

public class CardPaymentsActivity extends AppCompatActivity {

    TextView txtTitle, txtSubTitle;
    View responseView;
    CardResponseDTO cardResponse;
    FloatingActionButton fab;
    ImageView sidLogo, bankLogo;

    private static final String TEXT = "text/html", UTF = "UTF-8",
            LOG = CardPaymentsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeChooser.setTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        cardResponse = (CardResponseDTO) getIntent().getSerializableExtra("cardResponse");

        responseView = findViewById(R.id.AWB_responseView);
        txtTitle = (TextView) findViewById(R.id.AWB_title);
        sidLogo = (ImageView) findViewById(R.id.SIDR_logo);
        bankLogo = (ImageView) findViewById(R.id.SIDR_bankIcon);
        sidLogo.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.visa_mastercard));
        bankLogo.setVisibility(View.GONE);

        txtSubTitle = (TextView) findViewById(R.id.AWB_subtitle);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        txtSubTitle.setText("Card Payment");
        //
        String str = SharedUtil.getMunicipality(getApplicationContext()).getMunicipalityName();

        Util.setCustomActionBar(getApplicationContext(),
                getSupportActionBar(), str,
                ContextCompat.getDrawable(getApplicationContext(),
                        R.drawable.ic_action_secure), 0);
        if (cardResponse != null) {
            setResponse();
        } else {
            responseView.setVisibility(View.GONE);
            getCachedPaymentResponses();
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCachedPaymentResponses();
            }
        });
    }

    private void getCachedPaymentResponses() {
        CacheUtil.getCachedCardResponses(getApplicationContext(), new CacheUtil.CacheRetrievalListener() {
            @Override
            public void onCacheRetrieved(ResponseDTO response) {
                if (response.getCardResponseList() != null) {
                    displayPaymentHistory(response.getCardResponseList());
                }
                refreshCardResponses();
            }

            @Override
            public void onError() {

            }
        });
    }

    private void refreshCardResponses() {
        RequestDTO w = new RequestDTO(RequestDTO.GET_CARD_PAYMENT_RESPONSES);
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
                        displayPaymentHistory(response.getCardResponseList());
                        CacheUtil.cacheCardResponses(getApplicationContext(), response, null);
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

    private void displayPaymentHistory(List<CardResponseDTO> list) {

        if (list == null || list.isEmpty()) {
            Util.showToast(getApplicationContext(), "No Card payment responses found");
            finish();
            return;
        }
        Util.collapse(responseView, 1000, null);

        TableLayout table = (TableLayout) findViewById(R.id.AWB_tableLayout);
        TableRow header = (TableRow) getLayoutInflater().inflate(R.layout.card_response_header, null);
        table.removeAllViews();
        table.addView(header);

        int count = 0, completed = 0;
        double totalAmount = 0.0;
        for (CardResponseDTO x : list) {
            TableRow row = (TableRow) getLayoutInflater().inflate(R.layout.card_response_row, null);
            TextView status = (TextView) row.findViewById(R.id.SIDROW_status);
            TextView amount = (TextView) row.findViewById(R.id.SIDROW_amount);
            TextView ref = (TextView) row.findViewById(R.id.SIDROW_ref);
            TextView date = (TextView) row.findViewById(R.id.SIDROW_date);
            ImageView icon = (ImageView) row.findViewById(R.id.SIDROW_icon);

            Util.setCardTypeIcon(x.getCardType(), icon, getApplicationContext());
            status.setText(x.getOutcome());
            amount.setText(df.format(x.getAmount()));
            ref.setText(x.getMerchantReference());
            if (x.getDateRegistered() != null) {
                date.setText(sdf.format(new Date(x.getDateRegistered())));
            } else {
                date.setText(x.getDate());
            }
            if (x.getOutcome().equalsIgnoreCase("APPROVED")) {
                completed++;
                status.setTextColor(getResources().getColor(R.color.teal_800));
            } else {
                status.setTextColor(getResources().getColor(R.color.red_800));
                amount.setTextColor(getResources().getColor(R.color.red_800));
            }
            table.addView(row);
            totalAmount += x.getAmount().doubleValue();
            count++;
//            if (list.size() > 30) {
//                int rem = count % 20;
//                if (rem == 0) {
//                    table.addView(header);
//                }
//            }
        }

        TableRow footer = (TableRow) getLayoutInflater().inflate(R.layout.card_response_footer, null);
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
        responseView.setVisibility(View.VISIBLE);
        TextView txtStatus = (TextView) findViewById(R.id.SIDR_status);
        TextView txtBank = (TextView) findViewById(R.id.SIDR_bank);
        TextView txtDate = (TextView) findViewById(R.id.SIDR_date);
        TextView txtRecept = (TextView) findViewById(R.id.SIDR_receipt);
        TextView txtAmt = (TextView) findViewById(R.id.SIDR_amount);
        TextView txtRef = (TextView) findViewById(R.id.SIDR_reference);
        ImageView logo = (ImageView) findViewById(R.id.SIDR_bankIcon);
        Button btn = (Button) findViewById(R.id.SIDR_btnClose);

        txtStatus.setText(cardResponse.getOutcome());
        txtBank.setText("Card Payments");
        txtDate.setText(cardResponse.getDate());
        txtRecept.setText(cardResponse.getMerchantReference());
        txtAmt.setText(df.format(cardResponse.getAmount()));
        txtRef.setText(cardResponse.getMerchantReference());

        Util.setCardTypeIcon(cardResponse.getCardType(), sidLogo, getApplicationContext());


        if (cardResponse.getOutcome().equalsIgnoreCase("APPROVED")) {
            txtStatus.setTextColor(getResources().getColor(R.color.teal_800));
        }
        if (cardResponse.getOutcome().equalsIgnoreCase("CANCELLED")) {
            txtStatus.setTextColor(getResources().getColor(R.color.red_800));
        }
        if (cardResponse.getOutcome().equalsIgnoreCase("DECLINED")) {
            txtStatus.setTextColor(getResources().getColor(R.color.purple_700));
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
            refreshCardResponses();
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
