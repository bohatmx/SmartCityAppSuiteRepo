package com.boha.library.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.activities.AlertMapActivity;
import com.boha.library.adapters.AlertListAdapter;
import com.boha.library.dto.AlertDTO;
import com.boha.library.transfer.RequestDTO;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.CacheUtil;
import com.boha.library.util.NetUtil;
import com.boha.library.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Fragment to house local pictures
 */
public class AlertListFragment extends Fragment implements PageFragment {

    private AlertListener mListener;

    public static AlertListFragment newInstance(ResponseDTO response) {
        AlertListFragment fragment = new AlertListFragment();
        Bundle args = new Bundle();
        args.putSerializable("response",response);
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
            ResponseDTO r = (ResponseDTO)getArguments().getSerializable("response");
            alertList = r.getAlertList();
        }
    }

    View view, topView, border;
    ListView listView;
    Button btnCount;
    TextView  txtTitle, txtSubTitle;
    View fab;
    Context ctx;
    List<AlertDTO> alertList;
    Location location;

    TextView txtKM;
    SeekBar seekBar;
    ImageView imgSearch;
    static final int MIN_KM = 100;
    int radius = MIN_KM;
    ImageView icon, heroImage;
    int logo;

    public void setLocation(Location location) {
        this.location = location;
        refreshAlerts();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_alert_list, container, false);
        topView = inflater.inflate(R.layout.alert_top, null);
        ctx = getActivity();
        setFields();
        if (alertList != null) {
            setList();
        } else {
            getCachedAlerts();
        }

        return view;
    }

    private void setFields() {
        border = topView.findViewById(R.id.ALERT_LIST_top);
        border.setBackgroundColor(primaryColor);

        btnCount = (Button) view.findViewById(R.id.button);
        listView = (ListView) view.findViewById(R.id.ALERT_LIST_listView);
        icon = (ImageView)topView.findViewById(R.id.ALERT_LIST_icon);
        heroImage = (ImageView)topView.findViewById(R.id.ALERT_LIST_heroImage);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        txtKM = (TextView) topView.findViewById(R.id.ALERT_LIST_km);
        txtTitle = (TextView) topView.findViewById(R.id.ALERT_LIST_title);
        txtSubTitle = (TextView) topView.findViewById(R.id.ALERT_LIST_subTitle);
        imgSearch = (ImageView) topView.findViewById(R.id.ALERT_LIST_refresh);
        seekBar = (SeekBar) topView.findViewById(R.id.ALERT_LIST_seek);
        seekBar.setProgress(MIN_KM);
        txtKM.setText("" + MIN_KM);
        txtTitle.setText(ctx.getResources().getText(R.string.active_alerts));
        txtSubTitle.setVisibility(View.INVISIBLE);

        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCreateAlertRequested();
                Util.flashOnce(icon, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        mListener.onCreateAlertRequested();
                    }
                });
            }
        });
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshAlerts();
            }
        });
        topView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //refreshAlerts();
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress < MIN_KM) {
                    seekBar.setProgress(MIN_KM);
                    radius = MIN_KM;
                    txtKM.setText("" + MIN_KM);
                    return;
                }
                radius = progress;
                txtKM.setText("" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        ctx = getActivity();
        btnCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(btnCount, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        Intent i = new Intent(ctx, AlertMapActivity.class);
                        ResponseDTO r = new ResponseDTO();
                        r.setAlertList(alertList);
                        i.putExtra("newsList", r);
                        startActivity(i);
                    }
                });

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
            btnCount.setText("" + alertList.size());
        }

        ResponseDTO r = new ResponseDTO();
        r.setAlertList(alertList);
        CacheUtil.cacheAlertData(ctx,r,null);

    }
    public void refreshAlerts() {
        if (location == null) {
            return;
        }

        RequestDTO w = new RequestDTO(RequestDTO.GET_ALERTS_WITHIN_RADIUS);
        w.setLatitude(location.getLatitude());
        w.setLongitude(location.getLongitude());
        w.setRadius(radius);

        progressBar.setVisibility(View.VISIBLE);
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
            public void onError(String message) {

            }

            @Override
            public void onWebSocketClose() {

            }
        });

    }

    AlertListAdapter alertListAdapter;
    private void setList() {
        if (alertList == null) return;

        btnCount.setText("" + alertList.size());
        alertListAdapter = new AlertListAdapter(ctx, R.layout.alert_item,
                alertList, new AlertListAdapter.AlertListListener() {
            @Override
            public void onAlertClicked(int position) {
//                if (position == 0) return;
                mListener.onAlertClicked(alertList.get(position));
            }
        });

        if (listView.getHeaderViewsCount() == 0) {
            listView.addHeaderView(topView);
        }
        listView.setAdapter(alertListAdapter);
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
    public void animateSomething() {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        timer.cancel();
                        heroImage.setImageDrawable(Util.getRandomCityImage(ctx));
                        Util.expand(heroImage, 1000, new Util.UtilAnimationListener() {
                            @Override
                            public void onAnimationEnded() {
                                Util.flashOnce(btnCount, 300, null);
                            }
                        });
                    }
                });
            }
        }, 500);

    }

    int primaryColor,  primaryDarkColor;
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
        public void onAlertClicked(AlertDTO alert);

        public void onCreateAlertRequested();
    }

    static final String LOG = AlertListFragment.class.getSimpleName();

    public void setLogo(int logo) {
        this.logo = logo;
    }
}
