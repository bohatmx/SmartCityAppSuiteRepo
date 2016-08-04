package com.boha.library.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.activities.AlertMapActivity;
import com.boha.library.activities.CityApplication;
import com.boha.library.adapters.AlertRecyclerAdapter;
import com.boha.library.dto.AlertDTO;
import com.boha.library.transfer.RequestDTO;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.CacheUtil;
import com.boha.library.util.NetUtil;
import com.boha.library.util.Statics;
import com.squareup.leakcanary.RefWatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment to house local pictures
 */
public class AlertListFragment extends Fragment implements PageFragment {

    private AlertListener mListener;

    public static AlertListFragment newInstance(ResponseDTO response) {
        AlertListFragment fragment = new AlertListFragment();
        Bundle args = new Bundle();
        args.putSerializable("response", response);
        fragment.setArguments(args);
        return fragment;
    }

    public AlertListFragment() {
        // Required empty public constructor
    }

    ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ResponseDTO r = (ResponseDTO) getArguments().getSerializable("response");
            alertList = r.getAlertList();
        }
    }

    View view, topView;
    RecyclerView recyclerView;
    TextView txtTitle, txtEmpty;
    Context ctx;
    List<AlertDTO> alertList;
    Location location;


    static final int MIN_KM = 100;
    int radius = MIN_KM;
    ImageView  heroImage;
    int logo;
    FloatingActionButton fab;

    public void setLocation(Location location) {
        this.location = location;
        Log.w(LOG, "### setLocation: will refresh alerts");
        refreshAlerts();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_alert_list, container, false);
        ctx = getActivity();
        setFields();
        if (alertList != null) {
            setList();
        } else {
            getCachedAlerts();
        }

        switch(primaryColor) {
            case CityApplication.THEME_INDIGO:
                view.findViewById(R.id.AI_color).setBackground(ContextCompat.getDrawable(ctx, R.drawable.xindigo_oval_small));
                break;
            case CityApplication.THEME_GREEN:
                view.findViewById(R.id.AI_color).setBackground(ContextCompat.getDrawable(ctx, R.drawable.xgreen_oval_small));
                break;
            case CityApplication.THEME_BROWN:
                view.findViewById(R.id.AI_color).setBackground(ContextCompat.getDrawable(ctx, R.drawable.xbrown_oval_small));
                break;
            case CityApplication.THEME_AMBER:
                view.findViewById(R.id.AI_color).setBackground(ContextCompat.getDrawable(ctx, R.drawable.xamber_oval_small));
                break;
            case CityApplication.THEME_PURPLE:
                view.findViewById(R.id.AI_color).setBackground(ContextCompat.getDrawable(ctx, R.drawable.xpurple_oval_small));
                break;
            case CityApplication.THEME_LIME:
                view.findViewById(R.id.AI_color).setBackground(ContextCompat.getDrawable(ctx, R.drawable.xlime_oval_small));
                break;
            case CityApplication.THEME_GREY:
                view.findViewById(R.id.AI_color).setBackground(ContextCompat.getDrawable(ctx, R.drawable.xgrey_oval_small));
                break;
            case CityApplication.THEME_BLUE:
                view.findViewById(R.id.AI_color).setBackground(ContextCompat.getDrawable(ctx, R.drawable.xblue_oval_small));
                break;
            case CityApplication.THEME_BLUE_GRAY:
                view.findViewById(R.id.AI_color).setBackground(ContextCompat.getDrawable(ctx, R.drawable.xblue_gray_oval_small));
                break;
            case CityApplication.THEME_TEAL:
                view.findViewById(R.id.AI_color).setBackground(ContextCompat.getDrawable(ctx, R.drawable.xteal_oval_small));
                break;
            case CityApplication.THEME_CYAN:
                view.findViewById(R.id.AI_color).setBackground(ContextCompat.getDrawable(ctx, R.drawable.xcyan_oval_small));
                break;
            case CityApplication.THEME_ORANGE:
                view.findViewById(R.id.AI_color).setBackground(ContextCompat.getDrawable(ctx, R.drawable.xorange_oval_small));
                break;
            case CityApplication.THEME_PINK:
                view.findViewById(R.id.AI_color).setBackground(ContextCompat.getDrawable(ctx, R.drawable.xpink_oval_small));
                break;
            case CityApplication.THEME_RED:
                view.findViewById(R.id.AI_color).setBackground(ContextCompat.getDrawable(ctx, R.drawable.xred_oval_small));
                break;
        }

        return view;
    }


    public void setAlertList(List<AlertDTO> alertList) {
        this.alertList = alertList;
        setList();
    }

    private void setFields() {
        txtEmpty = (TextView)view.findViewById(R.id.ALERT_LIST_text);
        Statics.setRobotoFontLight(ctx, txtEmpty);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        recyclerView = (RecyclerView) view.findViewById(R.id.ALERT_LIST_listView);
        LinearLayoutManager lm = new LinearLayoutManager(ctx,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(lm);
        heroImage = (ImageView) view.findViewById(R.id.FAL_hero);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
       // txtTitle = (TextView) topView.findViewById(R.id.ALERT_LIST_title);
       // txtTitle.setText(ctx.getResources().getText(R.string.active_alerts));

        ctx = getActivity();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alertList == null || alertList.isEmpty()) {
                    Snackbar.make(recyclerView, ctx.getString(R.string.noalerts_map),
                            Snackbar.LENGTH_LONG).show();
                    return;
                }
                Intent i = new Intent(ctx, AlertMapActivity.class);
                ResponseDTO r = new ResponseDTO();
                r.setAlertList(alertList);
                i.putExtra("alertList", r);
                i.putExtra("primaryColorDark", primaryDarkColor);
                startActivity(i);

            }
        });
        animateSomething();
    }

    private void getCachedAlerts() {

        CacheUtil.getCacheLoginData(ctx, new CacheUtil.CacheRetrievalListener() {
            @Override
            public void onCacheRetrieved(ResponseDTO response) {
                alertList = response.getAlertList();
                setList();
            }

            @Override
            public void onError() {
                refreshAlerts();
            }
        });
    }

    public void onNewAlertSent(AlertDTO alert) {
        if (alertList == null) {
            alertList = new ArrayList<>();
        }
        alertList.add(0, alert);
        if (alertListAdapter != null) {
            alertListAdapter.notifyDataSetChanged();
        }

        ResponseDTO r = new ResponseDTO();
        r.setAlertList(alertList);
        CacheUtil.cacheAlertData(ctx, r, null);

    }

    public void refreshAlerts() {
        if (location == null) {
            return;
        }
        Log.d("AlertListFragment", "refreshAlerts");
        RequestDTO w = new RequestDTO(RequestDTO.GET_ALERTS_WITHIN_RADIUS);
        w.setLatitude(location.getLatitude());
        w.setLongitude(location.getLongitude());
        w.setRadius(radius);

        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.VISIBLE);
                }
            });
        } else {
            return;
        }

        NetUtil.sendRequest(ctx, w, new NetUtil.NetUtilListener() {
            @Override
            public void onResponse(final ResponseDTO response) {
                if (response.getStatusCode() == 0) {
                    if (alertList == null) alertList = new ArrayList<AlertDTO>();
                    if (response.getMessage() == null) {
                        alertList = response.getAlertList();
                    } else {
                        boolean found = false;
                        for (AlertDTO a : alertList) {
                            if (response.getAlertList().get(0).getAlertID().intValue()
                                    == a.getAlertID().intValue()) {
                                found = true;
                                Log.w(LOG, "alert already in list, ..will be ignored");
                            }
                        }
                        if (!found) {
                            alertList.add(0, response.getAlertList().get(0));
                            Log.i(LOG, "## alert added to list");
                        }
                    }
                    response.setAlertList(alertList);
                    CacheUtil.cacheAlertData(ctx, response, null);

                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                setList();

                            }
                        });
                    }
                }
            }

            @Override
            public void onError(String message) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            setList();

                        }
                    });
                }
            }

            @Override
            public void onWebSocketClose() {

            }
        });

    }

    AlertRecyclerAdapter alertListAdapter;
    static final long THREE_DAYS = 1000 * 60 * 60 * 24 * 3;

    private void setList() {
        if (alertList == null) {
            alertList = new ArrayList<>();
        }
        alertListAdapter = new AlertRecyclerAdapter(alertList, ctx, new AlertRecyclerAdapter.AlertAdapterListener() {
            @Override
            public void onAlertClicked(AlertDTO alert) {
            mListener.onAlertClicked(alert);
            }
        });


        if (alertList.isEmpty()) {
            txtEmpty.setVisibility(View.VISIBLE);
        } else {
            txtEmpty.setVisibility(View.GONE);
        }

        recyclerView.setAdapter(alertListAdapter);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (AlertListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement AlertListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = CityApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }

    @Override
    public void animateSomething() {
//        final Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                if (getActivity() != null) {
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            timer.cancel();
//                            heroImage.setImageDrawable(Util.getRandomBackgroundImage(ctx));
//                            Util.expand(heroImage, 1000, new Util.UtilAnimationListener() {
//                                @Override
//                                public void onAnimationEnded() {
//                                    Util.flashOnce(fab, 300, null);
//                                }
//                            });
//
//                        }
//                    });
//                }
//            }
//        }, 500);

    }

    int primaryColor, primaryDarkColor;

    @Override
    public void setThemeColors(int primaryColor, int primaryDarkColor) {
        this.primaryColor = primaryColor;
        this.primaryDarkColor = primaryDarkColor;
    }

    String pageTitle;

    @Override
    public String getPageTitle() {
        return pageTitle;
    }

    @Override
    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public interface AlertListener {
        void onAlertClicked(AlertDTO alert);

        void onCreateAlertRequested();

        void onFreshLocationRequested();

    }

    static final String LOG = AlertListFragment.class.getSimpleName();

    public void setLogo(int logo) {
        this.logo = logo;
    }
}
