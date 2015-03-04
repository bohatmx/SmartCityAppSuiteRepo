package com.boha.citizenapp.fragments;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.boha.citizenapp.R;
import com.boha.citizenapp.activities.PictureActivity;
import com.boha.citylibrary.dto.AlertDTO;
import com.boha.citylibrary.dto.AlertTypeDTO;
import com.boha.citylibrary.transfer.RequestDTO;
import com.boha.citylibrary.transfer.ResponseDTO;
import com.boha.citylibrary.util.CacheUtil;
import com.boha.citylibrary.util.NetUtil;
import com.boha.citylibrary.util.SharedUtil;
import com.boha.citylibrary.util.TrafficLightUtil;
import com.boha.citylibrary.util.Util;

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
    TextView green, amber, red, txtType, txtTitle, txtSubTitle, txtFAB;
    ImageView icon;
    View hero;
    View trafficLights;
    Context ctx;
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
        hero = view.findViewById(R.id.ALERT_heroImage);
        txtTitle = (TextView)view.findViewById(R.id.TOP_title);
        txtSubTitle = (TextView)view.findViewById(R.id.TOP_subTitle);
        txtFAB = (TextView)view.findViewById(R.id.TOP_fab);
        txtTitle.setText("Alert The City");
        txtSubTitle.setText("Notify or warn the community about an event");
        editDesc = (EditText) view.findViewById(R.id.ALERT_message);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        green = (TextView) view.findViewById(R.id.TRAFF_green);
        red = (TextView) view.findViewById(R.id.TRAFF_red);
        amber = (TextView) view.findViewById(R.id.TRAFF_amber);
        trafficLights = view.findViewById(R.id.TRAFF_main);
        txtType = (TextView) view.findViewById(R.id.ALERT_category);
        Util.flashSeveralTimes(txtType, 300, 5, null);

        red.setEnabled(false);
        amber.setEnabled(false);
        green.setEnabled(false);

        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(red, 200, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        sendAlert();
                    }
                });
            }
        });
        amber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(red, 200, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        sendAlert();
                    }
                });
            }
        });
        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(red, 200, new Util.UtilAnimationListener() {
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
                    list.add(t.getAlertTypeName());
                }
                try {
                    Util.showPopupBasicWithHeroImage(ctx, getActivity(), list, handle, "Alert Types", new Util.UtilPopupListener() {
                        @Override
                        public void onItemSelected(int index) {
                            alertType = response.getAlertTypeList().get(index);
                            txtType.setText(alertType.getAlertTypeName());

                            switch (alertType.getColor()) {
                                case AlertTypeDTO.GREEN:
                                    TrafficLightUtil.setGreen(ctx, trafficLights);
                                    Util.flashOnce(green, 300, new Util.UtilAnimationListener() {
                                        @Override
                                        public void onAnimationEnded() {
                                            green.setEnabled(true);
                                            Util.flashSeveralTimes(green,200,3,null);
                                        }
                                    });
                                    break;
                                case AlertTypeDTO.AMBER:
                                    TrafficLightUtil.setAmber(ctx, trafficLights);
                                    Util.flashOnce(amber, 300, new Util.UtilAnimationListener() {
                                        @Override
                                        public void onAnimationEnded() {
                                            amber.setEnabled(true);
                                            Util.flashSeveralTimes(amber,200,3,null);
                                        }
                                    });
                                    break;
                                case AlertTypeDTO.RED:
                                    TrafficLightUtil.setRed(ctx, trafficLights);
                                    Util.flashOnce(red, 300, new Util.UtilAnimationListener() {
                                        @Override
                                        public void onAnimationEnded() {
                                            red.setEnabled(true);
                                            Util.flashSeveralTimes(red,200,3,null);
                                        }
                                    });
                                    break;
                            }
                        }
                    });
                } catch (Exception e) {
                    Log.e(LOG, "Failed", e);
                }
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
            a.setDescription(alertType.getAlertTypeName());
        } else {
            a.setDescription(editDesc.getText().toString());
        }

        a.setAlertType(alertType);
        a.setMunicipalityID(SharedUtil.getMunicipality(ctx).getMunicipalityID());
        a.setLatitude(location.getLatitude());
        a.setLongitude(location.getLongitude());
        a.setId(0);
        a.setProfileInfoID(SharedUtil.getProfile(ctx).getProfileInfoID());
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
                        Log.w(LOG, "++ alert has been sent OK: " + response.getMessage());
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
        w.putExtra("alert", alert);
        w.putExtra("imageType", PictureActivity.ALERT_IMAGE);
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
