package com.boha.library.activities;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.boha.library.R;
import com.boha.library.dto.MunicipalityDTO;
import com.boha.library.dto.ProfileInfoDTO;
import com.boha.library.fragments.StatementFragment;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.CacheUtil;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.Util;

public class StatementActivity extends ActionBarActivity {

    StatementFragment statementFragment;
    Context ctx;
    int primaryColor, darkColor, logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statement);
        ctx = getApplicationContext();
        primaryColor = getIntent().getIntExtra("primaryColor", ctx.getResources().getColor(R.color.teal_500));
        darkColor = getIntent().getIntExtra("darkColor", ctx.getResources().getColor(R.color.teal_700));
        logo = getIntent().getIntExtra("logo", R.drawable.ic_action_globe);
        statementFragment = (StatementFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        statementFragment.setThemeColors(primaryColor,darkColor);
        getAccounts();

        MunicipalityDTO municipality = SharedUtil.getMunicipality(ctx);
        ActionBar actionBar = getSupportActionBar();
        Drawable d = ctx.getResources().getDrawable(logo);
        Util.setCustomActionBar(ctx,
                actionBar,
                municipality.getMunicipalityName(), d,logo);
        getSupportActionBar().setTitle("");

    }

    private void getAccounts() {
        CacheUtil.getCacheLoginData(ctx, new CacheUtil.CacheRetrievalListener() {
            @Override
            public void onCacheRetrieved(ResponseDTO response) {
                if (response.getProfileInfoList() != null && !response.getProfileInfoList().isEmpty()) {
                    ProfileInfoDTO profileInfo = response.getProfileInfoList().get(0);
                    statementFragment.setAccountList(profileInfo.getAccountList());
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
        getMenuInflater().inflate(R.menu.menu_statement, menu);
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
}
