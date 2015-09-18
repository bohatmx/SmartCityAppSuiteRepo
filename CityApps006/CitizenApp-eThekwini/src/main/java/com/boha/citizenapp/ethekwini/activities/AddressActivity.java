package com.boha.citizenapp.ethekwini.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.boha.citizenapp.ethekwini.R;
import com.boha.citizenapp.ethekwini.fragments.AddressFragment;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.Util;

public class AddressActivity extends AppCompatActivity implements AddressFragment.AddressFragmentListener{

    AddressFragment addressFragment;
    int type;
    Menu mMenu;
    public static final int CALLED_FROM_DRAWER_ACTIVITY = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG, "onCreate");
        setContentView(R.layout.activity_address);
        addressFragment = (AddressFragment)getSupportFragmentManager().findFragmentById(R.id.fragment);

        Drawable x = ContextCompat.getDrawable(getBaseContext(), R.drawable.logo);
        ActionBar bar = getSupportActionBar();
        Util.setCustomActionBar(getApplicationContext(),bar,
                SharedUtil.getMunicipality(getApplicationContext()).getMunicipalityName(),x,1);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(LOG, "onResume");

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_address, menu);
        mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         int id = item.getItemId();

        if (id == R.id.action_refresh) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAddressUpdated() {
        Intent w = new Intent(getApplicationContext(), CitizenDrawerActivity.class);
        startActivity(w);
        finish();
    }

    @Override
    public void setBusy(boolean busy) {
        setRefreshActionButtonState(busy);
    }
    @Override
    public void onBackPressed() {

    }
    static final String LOG = AddressActivity.class.getSimpleName();
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
