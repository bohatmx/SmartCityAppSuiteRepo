package com.boha.library.activities;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.boha.library.R;
import com.boha.library.dto.AccountDTO;
import com.boha.library.dto.MunicipalityDTO;
import com.boha.library.dto.ProfileInfoDTO;
import com.boha.library.fragments.AccountFragment;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.CacheUtil;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.Util;

public class AccountActivity extends ActionBarActivity implements AccountFragment.AccountFragmentListener{

    AccountFragment accountFragment;
    MunicipalityDTO municipality;
    int themeDarkColor, themePrimaryColor, logo;
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ctx = getApplicationContext();
        accountFragment = (AccountFragment)getSupportFragmentManager().findFragmentById(R.id.fragment);
        ProfileInfoDTO profileInfoDTO = (ProfileInfoDTO)getIntent().getSerializableExtra("profileInfo");

        accountFragment.setLogo(logo);
        if (profileInfoDTO != null) {
            accountFragment.setProfileInfo(profileInfoDTO);
        } else {
            getCachedData();
        }
        //
        logo = getIntent().getIntExtra("logo",R.drawable.ic_action_globe);
        accountFragment.setLogo(logo);
        municipality = SharedUtil.getMunicipality(getApplicationContext());
        ActionBar actionBar = getSupportActionBar();
        if (logo != 0) {
            Drawable d = ctx.getResources().getDrawable(logo);
            Util.setCustomActionBar(ctx,
                    actionBar,
                    municipality.getMunicipalityName(),d);
            getSupportActionBar().setTitle("");
        } else {
            getSupportActionBar().setTitle(municipality.getMunicipalityName());
        }

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
//        getMenuInflater().inflate(R.menu.menu_account, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
               int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

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
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        super.onPause();
    }

}
