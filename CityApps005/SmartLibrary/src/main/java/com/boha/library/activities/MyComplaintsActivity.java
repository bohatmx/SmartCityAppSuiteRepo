package com.boha.library.activities;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.boha.library.R;
import com.boha.library.dto.MunicipalityDTO;
import com.boha.library.fragments.MyComplaintsFragment;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.Util;

public class MyComplaintsActivity extends ActionBarActivity implements MyComplaintsFragment.MyComplaintsListener{

    MyComplaintsFragment myComplaintsFragment;
    int logo;
    Context ctx;
    boolean isCreateComplaintRequested;
    int themePrimaryColor, themeDarkColor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_complaints);
        ctx = getApplicationContext();



        myComplaintsFragment = (MyComplaintsFragment)getSupportFragmentManager().findFragmentById(R.id.fragment);
        logo = getIntent().getIntExtra("logo", R.drawable.ic_action_globe);
        themeDarkColor = getIntent().getIntExtra("darkColor", R.color.black);
        themePrimaryColor = getIntent().getIntExtra("primaryColor", R.color.dark_slate_grey);
        myComplaintsFragment.setThemeColors(themePrimaryColor,themeDarkColor);

        MunicipalityDTO municipality = SharedUtil.getMunicipality(getApplicationContext());
        ActionBar actionBar = getSupportActionBar();
        if (logo != 0) {
            Drawable d = ctx.getResources().getDrawable(logo);
            Util.setCustomActionBar(ctx,
                    actionBar,
                    municipality.getMunicipalityName(), d);
            getSupportActionBar().setTitle("");
        } else {
            getSupportActionBar().setTitle(municipality.getMunicipalityName());
        }

    }

    @Override
    public void onBackPressed() {

        if (isCreateComplaintRequested) {
            isCreateComplaintRequested = false;
            setResult(RESULT_OK);
        } else {
            setResult(RESULT_CANCELED);
        }
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_complaints, menu);
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
    public void onNewComplaintRequested() {
        isCreateComplaintRequested = true;
        onBackPressed();
    }
}
