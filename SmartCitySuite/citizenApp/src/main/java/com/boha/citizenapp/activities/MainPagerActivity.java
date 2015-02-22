package com.boha.citizenapp.activities;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.boha.citizenapp.fragments.AlertListFragment;
import com.boha.citizenapp.fragments.CreateAlertFragment;
import com.boha.citizenapp.fragments.ImageGridFragment;
import com.boha.citizenapp.fragments.PageFragment;
import com.boha.citizenapp.fragments.ProfileInfoFragment;
import com.boha.citylibrary.R;
import com.boha.citylibrary.dto.AlertDTO;

import java.util.ArrayList;
import java.util.List;

public class MainPagerActivity extends ActionBarActivity
implements ProfileInfoFragment.ProfileInfoFragmentListener,
        CreateAlertFragment.CreateAlertFragmentListener,
        ImageGridFragment.ImageGridFragmentListener,
        AlertListFragment.AlertListener{

    ViewPager mPager;
    List<PageFragment> pageFragmentList;
    ProfileInfoFragment citizenFragment;
    CreateAlertFragment createAlertFragment;
    ImageGridFragment imageGridFragment;
    AlertListFragment alertListFragment;
    PagerAdapter adapter;
    Context ctx;
    int currentPageIndex;
    Location mCurrentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_pager);
        ctx = getApplicationContext();
        mPager = (ViewPager) findViewById(R.id.pager);
        buildPages();
    }

    private void buildPages() {
        pageFragmentList = new ArrayList<>();

        citizenFragment = ProfileInfoFragment.newInstance();
        createAlertFragment = CreateAlertFragment.newInstance();
        imageGridFragment = ImageGridFragment.newInstance();
        alertListFragment = AlertListFragment.newInstance();


        pageFragmentList.add(citizenFragment);
        pageFragmentList.add(imageGridFragment);
        pageFragmentList.add(createAlertFragment);
        pageFragmentList.add(alertListFragment);


        adapter = new PagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(adapter);
        PagerSlidingTabStrip strip = (PagerSlidingTabStrip) findViewById(R.id.pager_title_strip);
        strip.setAllCaps(false);
        strip.setIndicatorColor(ctx.getResources().getColor(R.color.absa_red));
        strip.setUnderlineColor(ctx.getResources().getColor(R.color.blue));
        strip.setViewPager(mPager);
        strip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPageIndex = position;
                PageFragment pf = pageFragmentList.get(position);
                if (pf instanceof CreateAlertFragment) {
                    createAlertFragment.flash();
                    if (mCurrentLocation != null)
                        createAlertFragment.setLocation(mCurrentLocation);
                }
                if (pf instanceof AlertListFragment) {
                    if (mCurrentLocation != null)
                        alertListFragment.setLocation(mCurrentLocation);
                    alertListFragment.getCachedAlerts();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_pager, menu);
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
        overridePendingTransition(com.boha.citizenapp.R.anim.slide_in_left, com.boha.citizenapp.R.anim.slide_out_right);
        super.onPause();
    }

    @Override
    public void onAlertClicked(AlertDTO alert) {

    }

    @Override
    public void onAlertSent(AlertDTO alert) {

    }

    @Override
    public void onPictureClicked(int position) {

    }

    @Override
    public void onAccountDetailRequired() {

    }

    /**
     * Adapter to manage fragments in view pager
     */
    private class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {

            return (Fragment) pageFragmentList.get(i);
        }

        @Override
        public int getCount() {
            return pageFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = "Title";
            PageFragment pf = pageFragmentList.get(position);
            if (pf instanceof ProfileInfoFragment) {
                title = getString(R.string.info);
            }
            if (pf instanceof ImageGridFragment) {
                title = getString(R.string.city_gallery);
            }
            if (pf instanceof CreateAlertFragment) {
                title = getString(R.string.comms);
            }
            if (pf instanceof AlertListFragment) {
                title = "Alerts ";
            }

            return title;
        }
    }

}
