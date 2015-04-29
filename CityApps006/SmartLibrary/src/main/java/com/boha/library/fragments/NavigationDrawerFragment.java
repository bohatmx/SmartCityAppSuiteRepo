package com.boha.library.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.adapters.DrawerListAdapter;
import com.boha.library.dto.MunicipalityDTO;
import com.boha.library.dto.MunicipalityStaffDTO;
import com.boha.library.dto.ProfileInfoDTO;
import com.boha.library.dto.UserDTO;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class NavigationDrawerFragment extends Fragment {

    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerListener mCallbacks;

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private View mFragmentContainerView;

    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;
    int primaryColor, primaryDarkColor;
    View view;
    List<String> destinationList = new ArrayList<>();
    DrawerListAdapter drawerListAdapter;
    TextView  txtSubTitle;
    ImageView drawerImage;
    Context ctx;
    MunicipalityDTO municipality;
    MunicipalityStaffDTO staff;
    ProfileInfoDTO profileInfo;
    UserDTO user;
    static final String LOG = NavigationDrawerFragment.class.getSimpleName();
    public static final int FROM_MAIN = 1, FROM_ACCOUNT = 2;

    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG,"## onCreate");
        ctx = getActivity();

        Resources.Theme theme = getActivity().getTheme();
        TypedValue typedValue = new TypedValue();
        theme.resolveAttribute(com.boha.library.R.attr.colorPrimaryDark, typedValue, true);
        primaryDarkColor = typedValue.data;
        theme.resolveAttribute(com.boha.library.R.attr.colorPrimary, typedValue, true);
        primaryColor = typedValue.data;

        Log.w(LOG, "##Theme themeDarkColor: " + primaryDarkColor + " themePrimaryColor: " + primaryColor);
        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

        // Select either the default item (0) or the last selected item.

        municipality = SharedUtil.getMunicipality(ctx);
        staff = SharedUtil.getMunicipalityStaff(ctx);
        profileInfo = SharedUtil.getProfile(ctx);
        user = SharedUtil.getUser(ctx);
        makeDestinations();

//        selectItem(mCurrentSelectedPosition);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ctx = getActivity();
        view = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        setFields();

        drawerListAdapter = new DrawerListAdapter(ctx,
                R.layout.drawer_item, destinationList, primaryDarkColor);
        mDrawerListView.setAdapter(drawerListAdapter);
        mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
        return view;
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout, int type) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                R.drawable.ic_drawer,             /* nav drawer image to replace 'Up' caret */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                drawerImage.setImageDrawable(Util.getRandomBackgroundImage(ctx));
                if (!isAdded()) {
                    return;
                }
                SharedUtil.incrementSlidingMenuCount(ctx);
                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                if (!isAdded()) {
                    return;
                }

                SharedUtil.incrementSlidingMenuCount(ctx);
                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.

        if (type == FROM_MAIN) {
            if (SharedUtil.getSlidingMenuCount(ctx) < SharedUtil.MAX_SLIDING_TAB_VIEWS) {
                if (!mFromSavedInstanceState) {
                    mDrawerLayout.openDrawer(mFragmentContainerView);
                }
            }
        }

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerLayout.closeDrawers();
    }

    private void setFields() {
        mDrawerListView = (ListView) view.findViewById(R.id.NAV_list);
        drawerImage = (ImageView)view.findViewById(R.id.NAV_image);
        drawerImage.setImageDrawable(Util.getRandomBackgroundImage(ctx));
        txtSubTitle = (TextView) view.findViewById(R.id.NAV_subtitle);
        if (staff != null) {
            txtSubTitle.setText(staff.getFirstName() + " " + staff.getLastName());
        }
        if (profileInfo != null) {
            txtSubTitle.setText(profileInfo.getFirstName() + " " + profileInfo.getLastName());
        }
        if (user != null) {
            txtSubTitle.setText(user.getEmail());
        }
        mDrawerListView.setDividerHeight(1);
        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });
    }

    private void selectItem(int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onDestinationSelected(position, destinationList.get(position));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
        if (mDrawerLayout != null && isDrawerOpen()) {
            inflater.inflate(R.menu.global, menu);
            showGlobalContextActionBar();
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

//        if (item.getItemId() == R.id.action_example) {
//            Toast.makeText(getActivity(), "Example action.", Toast.LENGTH_SHORT).show();
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Per the navigation drawer design guidelines, updates the action bar to show the global app
     * 'context', rather than just what's in the current screen.
     */
    private void showGlobalContextActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        //actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        //actionBar.setTitle(R.string.app_name);
    }

    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public static interface NavigationDrawerListener {
        public void onDestinationSelected(int position, String text);
    }


    private void makeDestinations() {

        if (destinationList == null) {
            destinationList = new ArrayList<>();
        }
        if (staff != null) {
            destinationList.add(ctx.getString(R.string.city_alerts));
            destinationList.add(ctx.getString(R.string.create_alert));
            destinationList.add(ctx.getString(R.string.comps_around_me));
            destinationList.add(ctx.getString(R.string.city_news));
            destinationList.add(ctx.getString(R.string.faq));
        }
        if (profileInfo != null) {
            destinationList.add(ctx.getString(R.string.my_accounts));

            destinationList.add(ctx.getString(R.string.make_complaint));
            destinationList.add(ctx.getString(R.string.my_complaints));
            destinationList.add(ctx.getString(R.string.comps_around_me));

            destinationList.add(ctx.getString(R.string.city_alerts));
            destinationList.add(ctx.getString(R.string.city_news));

            destinationList.add(ctx.getString(R.string.faq));
        }
        if (user != null) {
            destinationList.add(ctx.getString(R.string.make_complaint));
            destinationList.add(ctx.getString(R.string.my_complaints));
            destinationList.add(ctx.getString(R.string.comps_around_me));

            destinationList.add(ctx.getString(R.string.city_alerts));
            destinationList.add(ctx.getString(R.string.city_news));

            destinationList.add(ctx.getString(R.string.faq));
        }
    }


    public void setPrimaryColor(int primaryColor) {
        this.primaryColor = primaryColor;
    }

    public void setPrimaryDarkColor(int primaryDarkColor) {
        this.primaryDarkColor = primaryDarkColor;
    }
}
