package com.boha.citizenapp.ethekwini.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.boha.citizenapp.ethekwini.R;
import com.boha.citizenapp.ethekwini.fragments.AddressFragment;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.Util;

public class AddressActivity extends AppCompatActivity implements AddressFragment.AddressFragmentListener{

    AddressFragment addressFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        addressFragment = (AddressFragment)getSupportFragmentManager().findFragmentById(R.id.fragment);

        Drawable x = ContextCompat.getDrawable(getBaseContext(), R.drawable.logo);
        ActionBar bar = getSupportActionBar();
        Util.setCustomActionBar(getApplicationContext(),bar,
                SharedUtil.getMunicipality(getApplicationContext()).getMunicipalityName(),x,1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_address, menu);
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
    public void onAddressUpdated() {
        Intent w = new Intent(getApplicationContext(), CitizenDrawerActivity.class);
        startActivity(w);
    }

    @Override
    public void setBusy(boolean busy) {

    }
    @Override
    public void onBackPressed() {

    }
}
