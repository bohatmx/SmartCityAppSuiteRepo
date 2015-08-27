package com.boha.foureyes.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.boha.foureyes.R;
import com.boha.foureyes.dto.ErrorStoreAndroidDTO;
import com.boha.foureyes.dto.ErrorStoreDTO;
import com.boha.foureyes.dto.RequestDTO;
import com.boha.foureyes.dto.ResponseDTO;
import com.boha.foureyes.fragments.AndroidCrashListFragment;
import com.boha.foureyes.fragments.DashboardListFragment;
import com.boha.foureyes.fragments.PageFragment;
import com.boha.foureyes.fragments.ServerLogFragment;
import com.boha.foureyes.fragments.ServerLogFragment.LogListener;
import com.boha.foureyes.fragments.SeverEventListFragment;
import com.boha.foureyes.util.NetUtil;
import com.boha.foureyes.util.Util;

import java.util.ArrayList;
import java.util.List;

public class EventActivity extends ActionBarActivity
        implements AndroidCrashListFragment.CrashListListener,
        SeverEventListFragment.EventListListener,
        LogListener, DashboardListFragment.DashboardListListener{


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_pager);
        ctx = getApplicationContext();
        mPager = (ViewPager) findViewById(R.id.pager);
        getServerData();

        ActionBar actionBar = getSupportActionBar();
        Util.setCustomActionBar(ctx,actionBar,"SmartCity FourEyes",
                ContextCompat.getDrawable(ctx,R.drawable.glasses32));
    }


    private void getServerData() {
        Log.w(LOG, "### getServerData and events...");
        RequestDTO w = new RequestDTO(RequestDTO.GET_ERROR_REPORTS);

        setRefreshActionButtonState(true);
        NetUtil.sendRequest(ctx, w, new NetUtil.NetUtilListener() {
            @Override
            public void onResponse(final ResponseDTO r) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setRefreshActionButtonState(false);
                        response = r;
                        buildPages();
                    }
                });
            }

            @Override
            public void onError(final String message) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setRefreshActionButtonState(false);
                            Util.showErrorToast(ctx,message);
                        }
                    });
            }

            @Override
            public void onWebSocketClose() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setRefreshActionButtonState(false);
                        Util.showErrorToast(ctx,"Comms broken, please try again");
                    }
                });
            }
        });

    }


    private void buildPages() {
        pageFragmentList = new ArrayList<PageFragment>();

        androidCrashListFragment = new AndroidCrashListFragment();
        ResponseDTO r1 = new ResponseDTO();
        Bundle data1 = new Bundle();
        r1.setErrorStoreAndroidList(response.getErrorStoreAndroidList());
        data1.putSerializable("response", r1);
        androidCrashListFragment.setArguments(data1);

        severEventListFragment = new SeverEventListFragment();
        ResponseDTO r2 = new ResponseDTO();
        Bundle data2 = new Bundle();
        r2.setErrorStoreList(response.getErrorStoreList());
        data2.putSerializable("response", r2);
        severEventListFragment.setArguments(data2);

        serverLogFragment = new ServerLogFragment();
        ResponseDTO r3 = new ResponseDTO();
        Bundle data3 = new Bundle();
        r3.setLog(response.getLog());
        data3.putSerializable("response", r3);
        serverLogFragment.setArguments(data3);

        dashboardListFragment = new DashboardListFragment();
        ResponseDTO r4 = new ResponseDTO();
        Bundle data4 = new Bundle();
        r4.setSummaryList(response.getSummaryList());
        data4.putSerializable("response", r4);
        dashboardListFragment.setArguments(data4);

        pageFragmentList.add(dashboardListFragment);
        pageFragmentList.add(androidCrashListFragment);
        pageFragmentList.add(severEventListFragment);
        pageFragmentList.add(serverLogFragment);
        initializeAdapter();
    }

    private void initializeAdapter() {
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
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
        mPager.setCurrentItem(currentPageIndex);
    }

    @Override
    public void onRefreshRequested() {
        getServerData();
    }

    @Override
    public void onEventRefreshRequested() {
        getServerData();
    }

    @Override
    public void onLogRefreshRequested() {
        getServerData();
    }

    @Override
    public void onDashboardRefresh() {
        getServerData();
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
                    title = "Municipality Dashboards";
                    break;
                case 1:
                    title = "Mobile Device Crashes";
                    break;
                case 2:
                    title = "Server Events";
                    break;
                case 3:
                    title = "Server Log";
                    break;

                default:
                    break;
            }
            return title;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.event, menu);
        mMenu = menu;

        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_refresh:
                getServerData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

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


    Context ctx;
    ViewPager mPager;
    Menu mMenu;
    ResponseDTO response;
    PagerAdapter mPagerAdapter;
    int currentPageIndex;
    List<ErrorStoreAndroidDTO> errorStoreAndroidList;
    List<ErrorStoreDTO> errorStoreList;
    List<PageFragment> pageFragmentList;
    static final String LOG = "EventActivity";
    AndroidCrashListFragment androidCrashListFragment;
    SeverEventListFragment severEventListFragment;
    ServerLogFragment serverLogFragment;
    DashboardListFragment dashboardListFragment;
}
