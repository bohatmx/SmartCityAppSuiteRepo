package com.boha.library.fragments;

import android.app.Activity;
import android.content.Context;
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

import com.boha.library.R;
import com.boha.library.activities.CityApplication;
import com.boha.library.dto.AlertDTO;
import com.boha.library.dto.AlertTypeDTO;
import com.boha.library.dto.MunicipalityStaffDTO;
import com.boha.library.dto.ProfileInfoDTO;
import com.boha.library.transfer.RequestDTO;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.CacheUtil;
import com.boha.library.util.NetUtil;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.TrafficLightUtil;
import com.boha.library.util.Util;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Fragment to house citizen's basic data + picture
 */
public class CreateAlertFragment extends Fragment implements PageFragment {

    private CreateAlertFragmentListener mListener;

    public static CreateAlertFragment newInstance(ProfileInfoDTO profileInfo) {
        CreateAlertFragment fragment = new CreateAlertFragment();
        Bundle args = new Bundle();
        args.putSerializable("profile",profileInfo);
        fragment.setArguments(args);
        return fragment;
    }
    public static CreateAlertFragment newInstance(MunicipalityStaffDTO staff) {
        CreateAlertFragment fragment = new CreateAlertFragment();
        Bundle args = new Bundle();
        args.putSerializable("staff",staff);
        fragment.setArguments(args);
        return fragment;
    }

    public CreateAlertFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w(LOG,"%%% onCreate");
        if (getArguments() != null) {
            staff = (MunicipalityStaffDTO)getArguments().getSerializable("staff");
            profileInfo = (ProfileInfoDTO)getArguments().getSerializable("profile");
        }
    }

    View view, fab;
    TextView green, amber, red, txtTitle, txtSubTitle;
    Button btnGetType;
    ImageView icon, hero;
    View  topView;
    View trafficLights;
    Context ctx;
    EditText editDesc;
    ProgressBar progressBar;
    MunicipalityStaffDTO staff;
    ProfileInfoDTO profileInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(LOG,"### onCreateView");
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
        hero = (ImageView)view.findViewById(R.id.TOP_heroImage);
        topView = view.findViewById(R.id.TOP_titleLayout);
        topView.setBackgroundColor(primaryColor);

        txtTitle = (TextView) view.findViewById(R.id.TOP_title);
        txtSubTitle = (TextView) view.findViewById(R.id.TOP_subTitle);
        fab = view.findViewById(R.id.FAB);
        icon = (ImageView) view.findViewById(R.id.TOP_icon);
        icon.setImageDrawable(ctx.getResources().getDrawable(android.R.drawable.ic_dialog_alert));
        txtTitle.setText("Alert The City");
        txtSubTitle.setText("Notify or warn the community about an event");
        editDesc = (EditText) view.findViewById(R.id.ALERT_message);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        green = (TextView) view.findViewById(R.id.TRAFF_green);
        red = (TextView) view.findViewById(R.id.TRAFF_red);
        amber = (TextView) view.findViewById(R.id.TRAFF_amber);
        trafficLights = view.findViewById(R.id.TRAFF_main);
        btnGetType = (Button) view.findViewById(R.id.button);
        btnGetType.setText(ctx.getString(com.boha.library.R.string.sel_type));
        btnGetType.setBackgroundColor(primaryDarkColor);
        Util.flashSeveralTimes(btnGetType, 300, 2, null);

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
        btnGetType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mListener.onAlertLocationRequested();

                Util.flashOnce(btnGetType, 200, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        list = new ArrayList<String>();
                        for (AlertTypeDTO t : response.getAlertTypeList()) {
                            list.add(t.getAlertTypeName());
                        }
                        try {
                            Util.showPopupBasicWithHeroImage(ctx, getActivity(), list, handle, "Alert Types", new Util.UtilPopupListener() {
                                @Override
                                public void onItemSelected(int index) {
                                    alertType = response.getAlertTypeList().get(index);
                                    btnGetType.setText(alertType.getAlertTypeName());

                                    switch (alertType.getColor()) {
                                        case AlertTypeDTO.GREEN:
                                            TrafficLightUtil.setGreen(ctx, trafficLights);
                                            green.setText("Send");
                                            Util.flashOnce(green, 300, new Util.UtilAnimationListener() {
                                                @Override
                                                public void onAnimationEnded() {
                                                    green.setEnabled(true);
                                                    Util.flashOnce(green, 200, null);
                                                }
                                            });
                                            break;
                                        case AlertTypeDTO.AMBER:
                                            TrafficLightUtil.setAmber(ctx, trafficLights);
                                            amber.setText("Send");
                                            Util.flashOnce(amber, 300, new Util.UtilAnimationListener() {
                                                @Override
                                                public void onAnimationEnded() {
                                                    amber.setEnabled(true);
                                                    Util.flashOnce(amber, 200, null);
                                                }
                                            });
                                            break;
                                        case AlertTypeDTO.RED:
                                            TrafficLightUtil.setRed(ctx, trafficLights);
                                            red.setText("Send");
                                            Util.flashOnce(red, 300, new Util.UtilAnimationListener() {
                                                @Override
                                                public void onAnimationEnded() {
                                                    red.setEnabled(true);
                                                    Util.flashOnce(red, 200, null);
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
        });


    }

    Location location;

    public void setLocation(Location location) {
        Log.d(LOG, "### setLocation");
        this.location = location;
    }

    private void sendAlert() {
        RequestDTO w = new RequestDTO(RequestDTO.ADD_ALERT);
        AlertDTO a = new AlertDTO();
        if (editDesc.getText().toString().isEmpty()) {
            Util.showErrorToast(ctx,"Please enter a brief description or message");
            return;

        }
        a.setDescription(editDesc.getText().toString());
        a.setAlertType(alertType);
        a.setMunicipalityID(SharedUtil.getMunicipality(ctx).getMunicipalityID());
        a.setLatitude(location.getLatitude());
        a.setLongitude(location.getLongitude());
        a.setId(0);
        if (profileInfo != null) {
            a.setProfileInfoID(profileInfo.getProfileInfoID());
        }
        if (staff != null) {
            a.setMunicipalityStaffID(staff.getMunicipalityStaffID());
        }
        w.setAlert(a);
        Log.d(LOG, "### sendAlert");
        progressBar.setVisibility(View.VISIBLE);
        NetUtil.sendRequest(ctx, w, new NetUtil.NetUtilListener() {
            @Override
            public void onResponse(final ResponseDTO response) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            btnGetType.setText(ctx.getString(com.boha.library.R.string.sel_type));
                            editDesc.setText("");
                            TrafficLightUtil.disable(ctx, trafficLights);
                            Util.preen(trafficLights, 500, new Util.UtilAnimationListener() {
                                @Override
                                public void onAnimationEnded() {
                                    if (response.getStatusCode() == 0) {
                                        alert = response.getAlertList().get(0);
                                        mListener.onAlertSent(alert);
                                    }
                                    Log.w(LOG, "++ alert has been sent OK: " + response.getMessage());
                                }
                            });

                        }
                    });
                }
            }

            @Override
            public void onError(final String message) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Util.showErrorToast(ctx,message);
                        }
                    });
                }
            }

            @Override
            public void onWebSocketClose() {

            }
        });

        //Track CreateAlertFragment
        CityApplication ca = (CityApplication) getActivity().getApplication();
        Tracker t = ca.getTracker(
                CityApplication.TrackerName.APP_TRACKER);
        t.setScreenName(CreateAlertFragment.class.getSimpleName());
        t.send(new HitBuilders.ScreenViewBuilder().build());

    }

    AlertDTO alert;

    public void flash() {
        Util.flashSeveralTimes(btnGetType, 200, 3, null);
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

    String title;

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
                        hero.setImageDrawable(Util.getRandomBackgroundImage(ctx));
                        Util.expand(hero, 1000, new Util.UtilAnimationListener() {
                            @Override
                            public void onAnimationEnded() {
                                Util.flashSeveralTimes(btnGetType, 30, 3, null);
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

    public interface CreateAlertFragmentListener {
        public void onAlertSent(AlertDTO alert);
        public void onAlertLocationRequested();
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
