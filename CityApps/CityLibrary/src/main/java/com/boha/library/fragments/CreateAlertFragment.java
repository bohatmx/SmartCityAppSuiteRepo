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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.boha.cityapps.R;
import com.boha.library.activities.PictureActivity;
import com.boha.library.dto.AlertDTO;
import com.boha.library.dto.AlertTypeDTO;
import com.boha.library.dto.transfer.RequestDTO;
import com.boha.library.dto.transfer.ResponseDTO;
import com.boha.library.util.CacheUtil;
import com.boha.library.util.NetUtil;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.TrafficLightUtil;
import com.boha.library.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment to house citizen's basic data + picture
 */
public class CreateAlertFragment extends Fragment implements PageFragment {

    private CreateAlertFragmentListener mListener;

    public static CreateAlertFragment newInstance() {
        CreateAlertFragment fragment = new CreateAlertFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public CreateAlertFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    View view;
    TextView green, yellow, red, txtType;
    ImageView hero;
    View trafficLights;
    Context ctx;
    Button btnPic, btnSend;
    EditText editDesc;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_create_alert, container, false);
        ctx = getActivity();
        setFields();
        getCachedData();
        return view;
    }

    List<String> list;
    AlertTypeDTO alertType;
    View handle;

    private void setFields() {
        handle = view.findViewById(R.id.ALERT_handle);
        hero = (ImageView) view.findViewById(R.id.ALERT_heroImage);
        editDesc = (EditText) view.findViewById(R.id.ALERT_message);
        btnPic = (Button) view.findViewById(R.id.ALERT_btnTakePicture);
        btnSend = (Button) view.findViewById(R.id.ALERT_btnSend);
        btnSend.setEnabled(false);
        btnPic.setVisibility(View.GONE);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        green = (TextView) view.findViewById(R.id.TRAFF_green);
        red = (TextView) view.findViewById(R.id.TRAFF_red);
        yellow = (TextView) view.findViewById(R.id.TRAFF_yellow);
        trafficLights = view.findViewById(R.id.TRAFF_main);
        txtType = (TextView) view.findViewById(R.id.ALERT_category);
        Util.flashSeveralTimes(txtType, 300, 3, null);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(btnSend, 200, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        sendAlert();
                    }
                });
            }
        });
        txtType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list = new ArrayList<String>();
                for (AlertTypeDTO t : response.getAlertTypeList()) {
                    list.add(t.getAlertTypeNmae());
                }
                Util.showPopupBasicWithHeroImage(ctx, getActivity(), list, handle, "Alert Types", new Util.UtilPopupListener() {
                    @Override
                    public void onItemSelected(int index) {
                        alertType = response.getAlertTypeList().get(index);
                        txtType.setText(alertType.getAlertTypeNmae());
                        btnSend.setEnabled(true);
                        btnPic.setEnabled(true);
                        switch (alertType.getColor()) {
                            case AlertTypeDTO.GREEN:
                                TrafficLightUtil.setGreen(ctx, trafficLights);
                                break;
                            case AlertTypeDTO.YELLOW:
                                TrafficLightUtil.setYellow(ctx, trafficLights);
                                break;
                            case AlertTypeDTO.RED:
                                TrafficLightUtil.setRed(ctx, trafficLights);
                                break;
                        }
                    }
                });
            }
        });


    }

    Location location;

    public void setLocation(Location location) {
        this.location = location;
    }

    private void sendAlert() {
        RequestDTO w = new RequestDTO(RequestDTO.ADD_ALERT);
        AlertDTO a = new AlertDTO();
        if (editDesc.getText().toString().isEmpty()) {
            a.setDescription("No message entered");
        } else {
            a.setDescription(editDesc.getText().toString());
        }

        a.setAlertType(alertType);
        a.setCityID(SharedUtil.getProfile(ctx).getCityID());
        a.setLatitude(location.getLatitude());
        a.setLongitude(location.getLongitude());
        a.setCategoryID(2);
        w.setAlert(a);

        progressBar.setVisibility(View.VISIBLE);
        NetUtil.sendRequest(ctx, w, new NetUtil.NetUtilListener() {
            @Override
            public void onResponse(final ResponseDTO response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        if (response.getStatusCode() == 0) {
                            alert = response.getAlertList().get(0);
                            startPictureActivity();
                        }
                        Log.w(LOG,"++ alert has been sent OK: " + response.getMessage());
                        //Util.showToast(ctx, "Alert has been sent");
                    }
                });
            }

            @Override
            public void onError(String message) {

            }

            @Override
            public void onWebSocketClose() {

            }
        });


    }

    AlertDTO alert;
    void startPictureActivity() {
        Intent w = new Intent(ctx, PictureActivity.class);
        w.putExtra("alert",alert);
        startActivity(w);
    }
    public void flash() {
        Util.flashSeveralTimes(txtType, 200, 3, null);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (CreateAlertFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement CreateAlertFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface CreateAlertFragmentListener {
        public void onAlertSent(AlertDTO alert);
    }

    static final String LOG = CreateAlertFragment.class.getSimpleName();

    ResponseDTO response;

    private void getCachedData() {
        CacheUtil.getCacheLoginData(ctx, new CacheUtil.CacheRetrievalListener() {
            @Override
            public void onCacheRetrieved(ResponseDTO r) {
                response = r;
            }

            @Override
            public void onError() {

            }
        });
    }
}
