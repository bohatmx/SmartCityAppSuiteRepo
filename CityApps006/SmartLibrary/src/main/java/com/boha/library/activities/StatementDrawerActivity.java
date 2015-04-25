package com.boha.library.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.boha.library.R;
import com.boha.library.fragments.AccountFragment;
import com.boha.library.fragments.NavigationDrawerFragment;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.Util;

public class StatementDrawerActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private AccountFragment accountFragment;

    int darkColor, primaryColor, logo;
    int position;
    String pageTitle;
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_drawer);
        ctx = getApplicationContext();
        darkColor = getIntent().getIntExtra("darkColor", R.color.teal_900);
        primaryColor = getIntent().getIntExtra("primaryColor", R.color.teal_500);
        logo = getIntent().getIntExtra("logo", R.drawable.ic_action_globe);

        accountFragment = (AccountFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        ActionBar bar = getSupportActionBar();
        Util.setCustomActionBar(ctx, bar,
                SharedUtil.getMunicipality(ctx).getMunicipalityName(),
                ctx.getResources().getDrawable(logo), logo);
    }

    @Override
    public void onBackPressed() {

    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.account_drawer, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            return true;
        }
        if (id == R.id.action_help) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestinationSelected(int position, String text) {

    }

}
