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
import com.boha.citizenapp.fragments.AccountFragment;
import com.boha.library.dto.AccountDTO;
import com.boha.library.dto.MunicipalityDTO;
import com.boha.library.dto.ProfileInfoDTO;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.CacheUtil;
import com.boha.library.util.SharedUtil;

public class AccountActivity extends ActionBarActivity implements AccountFragment.AccountFragmentListener{

    AccountFragment accountFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        accountFragment = (AccountFragment)getSupportFragmentManager().findFragmentById(R.id.fragment);
        ProfileInfoDTO profileInfoDTO = (ProfileInfoDTO)getIntent().getSerializableExtra("profileInfo");
        if (profileInfoDTO != null) {
            accountFragment.setProfileInfo(profileInfoDTO);
        } else {
            getCachedData();
        }
        //
        MunicipalityDTO municipality = SharedUtil.getMunicipality(getApplicationContext());
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater inflator = (LayoutInflater)
                this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(com.boha.library.R.layout.action_bar_logo, null);
        TextView txt = (TextView)v.findViewById(com.boha.library.R.id.ACTION_BAR_text);
        ImageView logo = (ImageView)v.findViewById(com.boha.library.R.id.ACTION_BAR_logo);
        txt.setText(municipality.getMunicipalityName());
        //
        logo.setImageDrawable(getApplicationContext().getResources()
                .getDrawable(com.boha.citizenapp.R.drawable.logo));
        getSupportActionBar().setTitle("");
        actionBar.setCustomView(v);
    }


    private void getCachedData() {
        CacheUtil.getCacheLoginData(getApplicationContext(), new CacheUtil.CacheRetrievalListener() {
            @Override
            public void onCacheRetrieved(ResponseDTO response) {
                if (response != null) {
                    accountFragment.setProfileInfo(response.getProfileInfoList().get(0));
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
        getMenuInflater().inflate(R.menu.menu_account, menu);
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
    public void onAccountStatementRequested(AccountDTO account) {

    }

    @Override
    public void onRefreshRequested() {


    }
    @Override
    public void onPause() {
        overridePendingTransition(com.boha.citizenapp.R.anim.slide_in_left, com.boha.citizenapp.R.anim.slide_out_right);
        super.onPause();
    }

}
