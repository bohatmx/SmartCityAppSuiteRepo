package com.boha.library.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.boha.cityapps.R;
import com.boha.library.dto.AlertDTO;
import com.boha.library.fragments.CitizenFragment;
import com.boha.library.fragments.CreateAlertFragment;
import com.boha.library.fragments.ImageGridFragment;
import com.boha.library.fragments.PageFragment;

import java.util.ArrayList;
import java.util.List;

public class MainPagerActivity extends ActionBarActivity implements
        CitizenFragment.CitizenFragmentListener,
        ImageGridFragment.ImageGridFragmentListener,
        CreateAlertFragment.CreateAlertFragmentListener {
    PagerAdapter adapter;
    ViewPager mPager;
    int currentPageIndex;
    Context ctx;
    CitizenFragment citizenFragment;
    CreateAlertFragment createAlertFragment;
    ImageGridFragment imageGridFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ctx = getApplicationContext();
        ctx = getApplicationContext();
        mPager = (ViewPager) findViewById(R.id.pager);
        buildPages();
    }

    private void buildPages() {
        pageFragmentList = new ArrayList<>();

        citizenFragment = CitizenFragment.newInstance();
        createAlertFragment = CreateAlertFragment.newInstance();
        imageGridFragment = ImageGridFragment.newInstance();


        pageFragmentList.add(imageGridFragment);
        pageFragmentList.add(createAlertFragment);
        pageFragmentList.add(citizenFragment);

        adapter = new PagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(adapter);
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                currentPageIndex = arg0;


            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAccountDetailRequired() {

    }

    @Override
    public void onAlertSent(AlertDTO alert) {

    }

    @Override
    public void onPictureClicked(int position) {

    }

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
            switch (position) {
                case 0:
                    title = getString(R.string.city_gallery);
                    break;
                case 1:
                    title = getString(R.string.comms);
                    break;
                case 2:
                    title = getString(R.string.info);
                    break;

            }
            return title;
        }
    }

    List<PageFragment> pageFragmentList;
}
