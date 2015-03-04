package com.boha.citizenapp.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.boha.citizenapp.R;
import com.boha.citylibrary.dto.AccountDTO;
import com.boha.citylibrary.dto.MunicipalityDTO;
import com.boha.citylibrary.fragments.PaymentStartFragment;
import com.boha.citylibrary.util.SharedUtil;

public class PaymentStartActivity extends ActionBarActivity implements PaymentStartFragment.PaymentStartListener{

    AccountDTO account;
    PaymentStartFragment paymentStartFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_start);

        account = (AccountDTO)getIntent().getSerializableExtra("account");
        int index = getIntent().getIntExtra("index",0);
        paymentStartFragment = (PaymentStartFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment);
        paymentStartFragment.setAccount(account, index);

        //
        MunicipalityDTO municipality = SharedUtil.getMunicipality(getApplicationContext());
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater inflator = (LayoutInflater)
                this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(com.boha.citylibrary.R.layout.action_bar_logo, null);
        TextView txt = (TextView)v.findViewById(com.boha.citylibrary.R.id.ACTION_BAR_text);
        ImageView logo = (ImageView)v.findViewById(com.boha.citylibrary.R.id.ACTION_BAR_logo);
        txt.setText(municipality.getMunicipalityName());
        //
        logo.setImageDrawable(getApplicationContext().getResources()
                .getDrawable(com.boha.citizenapp.R.drawable.logo));
        getSupportActionBar().setTitle("");
        actionBar.setCustomView(v);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_payment_start, menu);
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
    public void onPause() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        super.onPause();
    }

    @Override
    public void onPaymentTypeSelected(int paymentType) {

    }
}
