package com.boha.library.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.activities.ComplaintMapActivity;
import com.boha.library.adapters.ComplaintAdapter;
import com.boha.library.dto.ComplaintDTO;
import com.boha.library.dto.ComplaintFollowerDTO;
import com.boha.library.transfer.RequestDTO;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.DividerItemDecoration;
import com.boha.library.util.NetUtil;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.Util;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class ComplaintsAroundMeFragment extends Fragment implements PageFragment {

    private ComplaintAroundMeListener mListener;


    public static ComplaintsAroundMeFragment newInstance() {
        ComplaintsAroundMeFragment fragment = new ComplaintsAroundMeFragment();
        Bundle args = new Bundle();
        //args.putSerializable("complaintList", response);
        fragment.setArguments(args);
        return fragment;
    }

    public ComplaintsAroundMeFragment() {
        // Required empty public constructor
    }

    ResponseDTO response;
    View view, fab;
    Context ctx;
    String title;
    View handle, noComplaintsLayout;
    RecyclerView recyclerView;
    TextView txtCount, txtTitle, txtSubTitle, txtRadius;
    SeekBar seekBar;
    List<ComplaintDTO> complaintList;
    ComplaintAdapter complaintAdapter;
    List<String> stringList;
    Activity activity;
    View topView;
    ImageView hero;
    ProgressBar progressBar;
    int logo = R.drawable.ic_action_globe;

    public void setLogo(int logo) {
        this.logo = logo;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            response = (ResponseDTO) getArguments().getSerializable("complaintList");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_complaints_around, container, false);

        ctx = getActivity();
        activity = getActivity();
        setFields();

        return view;
    }

    public void getComplaintsAroundMe() {
        if (location == null) {
            mListener.onLocationForComplaintsAroundMe();
            return;
        }
        RequestDTO w = new RequestDTO(RequestDTO.GET_COMPLAINTS_WITHIN_RADIUS);
        w.setRadius(seekBar.getProgress());
        w.setLatitude(location.getLatitude());
        w.setLongitude(location.getLongitude());
        w.setMunicipalityID(SharedUtil.getMunicipality(ctx).getMunicipalityID());

        progressBar.setVisibility(View.VISIBLE);
        fab.setAlpha(0.4f);
        fab.setEnabled(false);
        txtCount.setAlpha(0.4f);
        txtCount.setEnabled(false);
        NetUtil.sendRequest(ctx, w, new NetUtil.NetUtilListener() {
            @Override
            public void onResponse(final ResponseDTO response) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            fab.setAlpha(1.0f);
                            fab.setEnabled(true);
                            txtCount.setAlpha(1.0f);
                            txtCount.setEnabled(true);
                            if (response.getComplaintList() != null) {
                                complaintList = response.getComplaintList();
                                setList();
                            }
                        }
                    });
                }

            }

            @Override
            public void onError(final String message) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Util.showErrorToast(ctx, message);
                    }
                });
            }

            @Override
            public void onWebSocketClose() {

            }
        });

    }

    public void setList() {

        txtCount.setText("" + complaintList.size());
        recyclerView = (RecyclerView) view.findViewById(R.id.CAR_listView);
        LinearLayoutManager lm = new LinearLayoutManager(ctx);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(lm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(ctx, RecyclerView.VERTICAL));
        complaintAdapter = new ComplaintAdapter(ctx, complaintList, primaryDarkColor, new ComplaintAdapter.ComplaintListener() {

            @Override
            public void onFollowRequested(ComplaintDTO complaint) {
                    addFollower(complaint);
            }

            @Override
            public void onStatusRequested(ComplaintDTO complaint) {

            }

            @Override
            public void onCameraRequested(ComplaintDTO complaint) {

            }

            @Override
            public void onImagesRequested(ComplaintDTO complaint) {

            }
        });
        recyclerView.setAdapter(complaintAdapter);

    }

    private void addFollower(ComplaintDTO complaint) {
        RequestDTO w = new RequestDTO(RequestDTO.ADD_COMPLAINT_FOLLOWER);
        ComplaintFollowerDTO x = new ComplaintFollowerDTO();
        x.setComment("No comment, just started following");
        x.setComplaintID(complaint.getComplaintID());
        x.setProfileInfoID(SharedUtil.getProfile(ctx).getProfileInfoID());

        w.setComplaintFollower(x);

        progressBar.setVisibility(View.VISIBLE);
        NetUtil.sendRequest(ctx, w, new NetUtil.NetUtilListener() {
            @Override
            public void onResponse(ResponseDTO response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Util.showToast(ctx,"You have been added as interested in this complaint");
                    }
                });
            }

            @Override
            public void onError(final String message) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Util.showErrorToast(ctx, message);
                    }
                });
            }

            @Override
            public void onWebSocketClose() {

            }
        });
    }
    private void setFields() {
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        handle = view.findViewById(R.id.CAR_handle);
        topView = view.findViewById(R.id.CAR_titleLayout);
        topView.setBackgroundColor(primaryDarkColor);
        hero = (ImageView) view.findViewById(R.id.CAR_hero);
        seekBar = (SeekBar) view.findViewById(R.id.CAR_seekBar);
        txtRadius = (TextView) view.findViewById(R.id.CAR_labelKM);
        txtTitle = (TextView) view.findViewById(R.id.CAR_title);
        txtSubTitle = (TextView) view.findViewById(R.id.CAR_subTitle);
        fab = view.findViewById(R.id.FAB);

        txtCount = (TextView) view.findViewById(R.id.CAR_count);


        txtTitle.setText(ctx.getString(R.string.complaints_around_me));
        txtSubTitle.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Util.flashOnce(fab, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        progressBar.setVisibility(View.VISIBLE);
                        mListener.onLocationForComplaintsAroundMe();
                    }
                });
            }
        });
        txtCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(txtCount, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        if (complaintList != null && !complaintList.isEmpty()) {
                            Intent w = new Intent(ctx, ComplaintMapActivity.class);
                            ResponseDTO x = new ResponseDTO();
                            x.setComplaintList(complaintList);
                            w.putExtra("complaintList", x);
                            w.putExtra("logo",logo);
                            startActivity(w);
                        }
                    }
                });
            }
        });
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

        animateSomething();
    }

    public void setLocation(Location location) {
        this.location = location;
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

    @Override
    public void animateSomething() {
        if (complaintList == null) {
            getComplaintsAroundMe();
        }
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
                            }
                        });
                    }
                });
            }
        }, 500);
    }

    int primaryColor, primaryDarkColor;

    @Override
    public void setThemeColors(int primaryColor, int primaryDarkColor) {
        this.primaryColor = primaryColor;
        this.primaryDarkColor = primaryDarkColor;
    }

    public interface ComplaintAroundMeListener {
         void onLocationForComplaintsAroundMe();
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

    public void checkGPS() {
        LocationManager lm = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            // Build the alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
            builder.setTitle(ctx.getString(R.string.loc_services_not));
            builder.setMessage(ctx.getString(R.string.enable_gps));
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    ctx.startActivity(intent);
                }
            });
            Dialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        } else {
            mListener.onLocationForComplaintsAroundMe();
        }
    }

    public List<ComplaintDTO> getComplaintList() {
        return complaintList;
    }
}
