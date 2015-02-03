package com.boha.library.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.boha.cityapps.R;
import com.boha.library.activities.AlertMapActivity;
import com.boha.library.activities.AlertPictureGridActivity;
import com.boha.library.adapters.AlertAdapter;
import com.boha.library.dto.AlertDTO;
import com.boha.library.dto.transfer.RequestDTO;
import com.boha.library.dto.transfer.ResponseDTO;
import com.boha.library.util.CacheUtil;
import com.boha.library.util.NetUtil;
import com.boha.library.util.Util;
import com.boha.library.util.WebCheck;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment to house local pictures
 */
public class AlertListFragment extends Fragment implements PageFragment {

    private AlertListener mListener;

    public static AlertListFragment newInstance() {
        AlertListFragment fragment = new AlertListFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public AlertListFragment() {
        // Required empty public constructor
    }

    AlertAdapter adapter;
    ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    View view;
    RecyclerView grid;
    Button btnmap;
    TextView txtCount;
    Context ctx;
    List<AlertDTO> alertList;
    Location location;

    TextView txtKM;
    SeekBar seekBar;
    ImageView imgSearch;
    static final int MIN_KM = 30;
    int radius = MIN_KM;

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_alert_list, container, false);
        txtCount = (TextView) view.findViewById(R.id.ALERT_LIST_count);
        grid = (RecyclerView) view.findViewById(R.id.ALERT_LIST_recyclerView);

        LinearLayoutManager lm = new LinearLayoutManager(ctx);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        grid.setLayoutManager(lm);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        txtKM = (TextView) view.findViewById(R.id.ALERT_LIST_km);
        imgSearch = (ImageView) view.findViewById(R.id.ALERT_LIST_refresh);
        seekBar = (SeekBar) view.findViewById(R.id.ALERT_LIST_seek);
        seekBar.setProgress(MIN_KM);
        txtKM.setText("" + MIN_KM);

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRemoteData();
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
        txtCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(txtCount, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        Intent i = new Intent(ctx, AlertMapActivity.class);
                        ResponseDTO r = new ResponseDTO();
                        r.setAlertList(alertList);
                        i.putExtra("alertList", r);
                        startActivity(i);
                    }
                });

            }
        });

        getCachedAlerts();

        return view;
    }

    ResponseDTO response;

    private void getCachedAlerts() {

        CacheUtil.getCacheAlertData(ctx, new CacheUtil.CacheRetrievalListener() {
            @Override
            public void onCacheRetrieved(ResponseDTO response) {
                alertList = response.getAlertList();
                setList();
                getRemoteData();

            }

            @Override
            public void onError() {
                getRemoteData();
            }
        });
    }

    private void getRemoteData() {
        if (location == null) return;
        if (WebCheck.checkNetworkAvailability(ctx, true).isWifiAvailable() ||
                WebCheck.checkNetworkAvailability(ctx, true).isMobileAvailable()) {
            RequestDTO w = new RequestDTO(RequestDTO.GET_ALERTS);
            w.setLatitude(location.getLatitude());
            w.setLongitude(location.getLongitude());
            w.setRadius(radius);

            Log.w(LOG, "getting alerts, radius: " + radius);
            progressBar.setVisibility(View.VISIBLE);
            NetUtil.sendRequest(ctx, w, new NetUtil.NetUtilListener() {
                @Override
                public void onResponse(final ResponseDTO response) {
                    if (response.getStatusCode() == 0) {
                        Log.e(LOG,"### OK response drom server ...");
                        if (alertList == null) alertList = new ArrayList<AlertDTO>();
                        if (response.getMessage() == null) {
                                alertList = response.getAlertList();
                        } else {
                            boolean found = false;
                            for (AlertDTO a : alertList) {
                                if (response.getAlertList().get(0).getAlertID().intValue()
                                        == a.getAlertID().intValue()) {
                                    found = true;
                                    Log.w(LOG,"alert already in list, ..will be ignored");
                                }
                            }
                            if (!found) {
                                alertList.add(0, response.getAlertList().get(0));
                                Log.i(LOG,"## alert added to list");
                            }
                        }
                        response.setAlertList(alertList);
                        CacheUtil.cacheAlertData(ctx, response, null);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                Log.w(LOG,".... about to set list");
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
    }

    private void setList() {
        if (alertList == null) return;

        txtCount.setText("" + alertList.size());
        adapter = new AlertAdapter(alertList, ctx, new AlertAdapter.AlertListener() {
            @Override
            public void onAlertClicked(int position) {
                mListener.onAlertClicked(alertList.get(position));

                if (alertList.get(position).getPhotoUploadList().isEmpty()) {
                    Util.showToast(ctx, "No photos were recoreded for this alert");
                    return;
                }
                Intent intent = new Intent(ctx, AlertPictureGridActivity.class);
                intent.putExtra("alert",alertList.get(position));
                startActivity(intent);
            }
        });

        grid.setAdapter(adapter);
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

    public interface AlertListener {
        public void onAlertClicked(AlertDTO alert);
    }

    static final String LOG = AlertListFragment.class.getSimpleName();
}
