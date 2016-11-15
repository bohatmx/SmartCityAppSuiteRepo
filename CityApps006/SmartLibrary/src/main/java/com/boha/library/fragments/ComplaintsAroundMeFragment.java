package com.boha.library.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.activities.ComplaintMapActivity;
import com.boha.library.adapters.ComplaintListAdapter;
import com.boha.library.dto.ComplaintDTO;
import com.boha.library.dto.ComplaintUpdateStatusDTO;
import com.boha.library.transfer.RequestDTO;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.NetUtil;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.Statics;
import com.boha.library.util.Util;
import com.boha.library.util.WebCheck;
import com.boha.library.util.WebCheckResult;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.facebook.FacebookSdk.getApplicationContext;


public class ComplaintsAroundMeFragment extends Fragment implements PageFragment {

    private ComplaintAroundMeListener mListener;


    public static ComplaintsAroundMeFragment newInstance() {
        ComplaintsAroundMeFragment fragment = new ComplaintsAroundMeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ComplaintsAroundMeFragment() {
        // Required empty public constructor
    }

    ResponseDTO response;
    View view, emptyLayout;
    FloatingActionButton fab;
    Context ctx;
    View handle;
    ListView listView;
    TextView txtCount, txtTitle, txtSubTitle, txtRadius;
    SeekBar seekBar;
    List<ComplaintDTO> complaintList = new ArrayList<>();
    Activity activity;
    ImageView hero;
    List<ComplaintUpdateStatusDTO> complaintUpdateStatusList;

    int logo = R.drawable.ic_action_globe;

    public void setLogo(int logo) {
        this.logo = logo;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());

        if (getArguments() != null) {
            response = (ResponseDTO) getArguments().getSerializable("complaintList");
           // complaintList = response.getComplaintList();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_complaints_around, container, false);
        txtEmpty = (TextView)view.findViewById(R.id.CAR_text);

        ctx = getActivity();
        Statics.setRobotoFontLight(ctx, txtEmpty);
        activity = getActivity();
        setFields();

        if (savedInstanceState != null) {
            Log.e(LOG,"##onCreateView, savedInstanceState not null");
            location = new Location(LocationManager.GPS_PROVIDER);
            if (savedInstanceState.getDouble("latitude") != 0.0) {
                location.setLatitude(savedInstanceState.getDouble("latitude"));
                location.setLongitude(savedInstanceState.getDouble("longitude"));
            }
            try {
                ResponseDTO w = (ResponseDTO) savedInstanceState.getSerializable("response");
                if (w != null) {
                    complaintList = w.getComplaintList();
                    setList();
                }
            }catch (Exception e) {
                Log.e("","Error: " + e.getMessage());
            }

        }


        return view;
    }

    public void getComplaintsAroundMe() {
        if (location == null) {
            disableFAB();
            mListener.onLocationForComplaintsAroundMe();
            return;
        }
        RequestDTO w = new RequestDTO(RequestDTO.GET_COMPLAINTS_WITHIN_RADIUS);
        w.setRadius(seekBar.getProgress());
        w.setLatitude(location.getLatitude());
        w.setLongitude(location.getLongitude());
        w.setMunicipalityID(SharedUtil.getMunicipality(ctx).getMunicipalityID());

        //todo remove when done testing
        w.setSpoof(false);
        //
        disableFAB();
        mListener.setBusy(true);
        snackbar = Util.showSnackBar(listView,"Searching for Complaints around you ...", "OK", Color.parseColor("TEAL"));
        setAnalyticsEvent("cases", "CasesAroundMe");
        NetUtil.sendRequest(ctx, w, new NetUtil.NetUtilListener() {
            @Override
            public void onResponse(final ResponseDTO response) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            enableFAB();
                            mListener.setBusy(false);
                            snackbar.dismiss();
                            complaintList = response.getComplaintList();
                            setList();
                            Util.showSnackBar(listView,"Complaints around you found: "
                                    + complaintList.size(),"OK",Color.parseColor("GREEN"));
                        }
                    });
                }

            }

            @Override
            public void onError(final String message) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mListener.setBusy(false);
                        snackbar.dismiss();
                        enableFAB();
                        Util.showSnackBar(fab,message,"OK", Color.parseColor("RED"));
                    }
                });
            }

            @Override
            public void onWebSocketClose() {

            }
        });

    }

    FirebaseAnalytics mFirebaseAnalytics;
    private void setAnalyticsEvent(String id, String name) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);

        if (mFirebaseAnalytics == null) {
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
        }
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        Log.w(LOG,"analytics event sent .....");


    }
    private void enableFAB() {
        fab.setAlpha(1.0f);
        fab.setEnabled(true);
        txtCount.setAlpha(1.0f);
        txtCount.setEnabled(true);
    }
    private void disableFAB() {
        fab.setAlpha(0.4f);
        fab.setEnabled(false);
        txtCount.setAlpha(0.4f);
        txtCount.setEnabled(false);
    }

    View header;
    TextView txtEmpty;
    private void setHeader() {
        header = getActivity().getLayoutInflater().inflate(R.layout.complaints_header,null);
        handle = header.findViewById(R.id.CAR_handle);

      //  hero = (ImageView) header.findViewById(R.id.CAR_hero);
        seekBar = (SeekBar) header.findViewById(R.id.CAR_seekBar);
        txtRadius = (TextView) header.findViewById(R.id.CAR_labelKM);
        txtTitle = (TextView) header.findViewById(R.id.CAR_title);
        txtSubTitle = (TextView) header.findViewById(R.id.CAR_subTitle);
        fab = (FloatingActionButton)header.findViewById(R.id.fab);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtRadius.setText("" + progress + " KM");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        txtTitle.setText(ctx.getString(R.string.complaints_around_me));
        txtSubTitle.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mListener.setBusy(true);
                disableFAB();
                snackbar = Util.showSnackBar(listView, "Searching for GPS coordinates ...", "OK", Color.parseColor("YELLOW"));
                mListener.onLocationForComplaintsAroundMe();
            }
        });

    }
    ComplaintListAdapter adapter;
    Snackbar snackbar;
    public void setList() {

        if(complaintList == null) {
            complaintList = new ArrayList<>();
        }
        Collections.sort(complaintList);
        txtCount.setText("" + complaintList.size());
         adapter = new ComplaintListAdapter(ctx, R.layout.complaint_item, primaryDarkColor,
                 ComplaintListAdapter.AROUND_ME,
                complaintList, new ComplaintListAdapter.ComplaintListListener() {
            @Override
            public void onComplaintFollowRequested(ComplaintDTO complaint) {
                addFollower(complaint);
            }

            @Override
            public void onComplaintStatusRequested(ComplaintDTO complaint,int position) {
                getCaseDetails(complaint.getHref(),position);
            }

            @Override
            public void onComplaintCameraRequested(ComplaintDTO complaint) {
                underConstruction();
            }

            @Override
            public void onComplaintImagesRequested(ComplaintDTO complaint) {
                underConstruction();
            }
        });


        if (listView.getHeaderViewsCount() == 0) {
            listView.addHeaderView(header);
        }
        if (complaintList.isEmpty()) {
            txtEmpty.setVisibility(View.VISIBLE);
        } else {
            txtEmpty.setVisibility(View.GONE);
        }
        listView.setAdapter(adapter);

    }

    private void addFollower(ComplaintDTO complaint) {
        Util.showToast(ctx,getString(R.string.under_cons));
//        RequestDTO w = new RequestDTO(RequestDTO.ADD_COMPLAINT_FOLLOWER);
//        ComplaintFollowerDTO x = new ComplaintFollowerDTO();
//        x.setComment("No comment, just started following");
//        x.setComplaintID(complaint.getComplaintID());
//        x.setProfileInfoID(SharedUtil.getProfile(ctx).getProfileInfoID());
//
//        w.setComplaintFollower(x);
//
//        progressBar.setVisibility(View.VISIBLE);
//        NetUtil.sendRequest(ctx, w, new NetUtil.NetUtilListener() {
//            @Override
//            public void onResponse(ResponseDTO response) {
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        progressBar.setVisibility(View.GONE);
//                        Util.showToast(ctx, "You have been added as interested in this complaint");
//                    }
//                });
//            }
//
//            @Override
//            public void onError(final String message) {
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        progressBar.setVisibility(View.GONE);
//                        Util.showErrorToast(ctx, message);
//                    }
//                });
//            }
//
//            @Override
//            public void onWebSocketClose() {
//
//            }
//        });
    }
    private void getCaseDetails(final String href, final int position) {
        WebCheckResult wcr = WebCheck.checkNetworkAvailability(ctx);
        if (!wcr.isWifiConnected() && !wcr.isMobileConnected()) {
            Util.showSnackBar(txtCount,"You are currently not connected to the network","OK", Color.parseColor("red"));
            //   Toast.makeText(SplashActivity.this,"You are currently not connected to the network",Toast.LENGTH_LONG).show();
            return;
        }
        RequestDTO w = new RequestDTO(RequestDTO.GET_COMPLAINT_STATUS);
        w.setReferenceNumber(href);
        w.setMunicipalityID(SharedUtil.getMunicipality(ctx).getMunicipalityID());

        mListener.setBusy(true);
        NetUtil.sendRequest(ctx, w, new NetUtil.NetUtilListener() {
            @Override
            public void onResponse(final ResponseDTO response) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mListener.setBusy(false);
                            if (response.getStatusCode() == 0) {
                                complaintUpdateStatusList = response.getComplaintUpdateStatusList();
                                complaintList.get(position).setComplaintUpdateStatusList(complaintUpdateStatusList);
                                adapter.notifyDataSetChanged();

                            }
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
                            mListener.setBusy(false);
                            Util.showToast(ctx, message);
                        }
                    });
                }
            }

            @Override
            public void onWebSocketClose() {

            }
        });
    }
    private void underConstruction() {
        Util.showToast(ctx, getString(R.string.under_cons));
    }
    private void setFields() {
        setHeader();
        txtCount = (TextView) view.findViewById(R.id.CAR_count);
        listView = (ListView) view.findViewById(R.id.CAR_listView);


        txtCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(txtCount, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                       /* if (complaintList != null && !complaintList.isEmpty()) {
                            Intent w = new Intent(ctx, ComplaintMapActivity.class);
                            ResponseDTO x = new ResponseDTO();
                            x.setComplaintList(complaintList);
                            w.putExtra("complaintList", x);
                            w.putExtra("logo", logo);
                            startActivity(w);
                        }*/
                    }
                });
            }
        });

        setList();
        animateSomething();
    }

    @Override
    public void onSaveInstanceState(Bundle b) {
        Log.e(LOG, "**onSaveInstanceState");
        if (location != null) {
            b.putDouble("latitude", location.getLatitude());
            b.putDouble("longitude", location.getLongitude());
        }
        if (complaintList != null) {
            ResponseDTO w = new ResponseDTO();
            w.setComplaintList(complaintList);
            b.putSerializable("response",w);
        }
        super.onSaveInstanceState(b);
    }
    public void setLocation(Location location) {
        this.location = location;
        snackbar.dismiss();
        getComplaintsAroundMe();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (ComplaintAroundMeListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.getLocalClassName()
                    + " must implement ComplaintAroundMeListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    @Override public void onDestroy() {
        super.onDestroy();
//        RefWatcher refWatcher = CityApplication.getRefWatcher(getActivity());
//        refWatcher.watch(this);
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
                        /*hero.setImageDrawable(Util.getRandomBackgroundImage(ctx));
                        Util.flashSeveralTimes(fab, 100, 2, null);*/
                    }
                });
            }
        }, 50);
    }

    int primaryColor, primaryDarkColor;

    @Override
    public void setThemeColors(int primaryColor, int primaryDarkColor) {
        this.primaryColor = primaryColor;
        this.primaryDarkColor = primaryDarkColor;
    }

    public interface ComplaintAroundMeListener {
         void onLocationForComplaintsAroundMe();
        void setBusy(boolean busy);
    }

    Location location;


    static final String LOG = ComplaintsAroundMeFragment.class.getSimpleName();
    String pageTitle;

    @Override
    public String getPageTitle() {
        return pageTitle;
    }

    @Override
    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }


    public List<ComplaintDTO> getComplaintList() {
        return complaintList;
    }
}
