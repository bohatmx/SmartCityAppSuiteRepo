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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.adapters.ComplaintAdapter;
import com.boha.library.dto.ComplaintDTO;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.CacheUtil;
import com.boha.library.util.Util;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class ComplaintListFragment extends Fragment implements PageFragment{

    private ComplaintListFragmentListener mListener;


    public static ComplaintListFragment newInstance() {
        ComplaintListFragment fragment = new ComplaintListFragment();
        Bundle args = new Bundle();
        //args.putSerializable("complaintList", response);
        fragment.setArguments(args);
        return fragment;
    }

    public ComplaintListFragment() {
        // Required empty public constructor
    }

    ResponseDTO response;
    View view, fab;
    Context ctx;
    String title;
    View handle, noComplaintsLayout;
    RecyclerView listView;
    TextView txtCount, txtTitle, txtSubTitle;
    List<ComplaintDTO> complaintList;
    ComplaintAdapter complaintAdapter;
    List<String> stringList;
    Activity activity;
    View topView;
    ImageView hero;
    ProgressBar progressBar;


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
        view = inflater.inflate(R.layout.fragment_list_complaints, container, false);

        ctx = getActivity();
        activity = getActivity();
        setFields();

        mListener.onLocationRequested();
        getCachedComplaints();
        return view;
    }

    private void getCachedComplaints() {
        CacheUtil.getCacheLoginData(ctx, new CacheUtil.CacheRetrievalListener() {
            @Override
            public void onCacheRetrieved(ResponseDTO response) {

                if (response.getComplaintList() != null) {
                    complaintList = response.getComplaintList();
                }
                setList();
            }

            @Override
            public void onError() {

            }
        });
    }

    private void setList() {

            complaintAdapter = new ComplaintAdapter(complaintList, ctx, new ComplaintAdapter.ComplaintListener() {
                @Override
                public void onComplaintSelected(int position) {

                }
            });
            listView.setAdapter(complaintAdapter);


    }
    private void setFields() {
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        handle = view.findViewById(R.id.FLC_handle);
        topView = view.findViewById(R.id.FLC_titleLayout);
        topView.setBackgroundColor(primaryDarkColor);
        hero = (ImageView)view.findViewById(R.id.FLC_hero);
        txtTitle = (TextView)view.findViewById(R.id.FLC_title);
        txtSubTitle = (TextView)view.findViewById(R.id.FLC_subTitle);
        fab = view.findViewById(R.id.FAB);

        txtCount = (TextView)view.findViewById(R.id.FLC_count);
        listView = (RecyclerView)view.findViewById(R.id.FLC_listView);

        txtTitle.setText("Service Complaints");
        txtSubTitle.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(fab, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {

                    }
                });
            }
        });

        animateSomething();
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (ComplaintListFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.getLocalClassName()
                    + " must implement ComplaintListFragmentListener");
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

    int primaryColor,  primaryDarkColor;
    @Override
    public void setThemeColors(int primaryColor, int primaryDarkColor) {
        this.primaryColor = primaryColor;
        this.primaryDarkColor = primaryDarkColor;
    }
    public interface ComplaintListFragmentListener {
        public void onLocationRequested();
    }
    Location location;


    static final String LOG = ComplaintListFragment.class.getSimpleName();
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
        if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
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
            mListener.onLocationRequested();
        }
    }
}
