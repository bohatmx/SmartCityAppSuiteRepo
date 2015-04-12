package com.boha.library.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.adapters.ComplaintAdapter;
import com.boha.library.dto.ComplaintDTO;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.CacheUtil;
import com.boha.library.util.DividerItemDecoration;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.Util;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Manage the User's own complaints history
 */
public class MyComplaintsFragment extends Fragment implements PageFragment {


    public static MyComplaintsFragment newInstance() {
        MyComplaintsFragment fragment = new MyComplaintsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public MyComplaintsFragment() {
    }

    public interface MyComplaintsListener {
        public void onNewComplaintRequested();
    }
    MyComplaintsListener mListener;
    ResponseDTO response;
    View view, fab;
    Context ctx;
    View handle;
    RecyclerView recyclerView;
    TextView txtCount, txtTitle, txtSubTitle, txtUserName;
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
        txtCount.setText("" + complaintList.size());
        LinearLayoutManager lm = new LinearLayoutManager(ctx);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(lm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(ctx, RecyclerView.VERTICAL));

        complaintAdapter = new ComplaintAdapter(ctx,complaintList,
                primaryDarkColor, new ComplaintAdapter.ComplaintListener() {
            @Override
            public void onFollowRequested(ComplaintDTO complaint) {

            }

            @Override
            public void onDetailsRequested(ComplaintDTO complaint) {

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

    private void setFields() {
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        handle = view.findViewById(R.id.FLC_handle);
        topView = view.findViewById(R.id.FLC_titleLayout);
        topView.setBackgroundColor(primaryDarkColor);
        hero = (ImageView) view.findViewById(R.id.FLC_hero);
        txtUserName = (TextView) view.findViewById(R.id.FLC_userName);
        txtTitle = (TextView) view.findViewById(R.id.FLC_title);
        txtSubTitle = (TextView) view.findViewById(R.id.FLC_subTitle);
        fab = view.findViewById(R.id.FAB);

        txtCount = (TextView) view.findViewById(R.id.FLC_count);
        recyclerView = (RecyclerView) view.findViewById(R.id.FLC_listView);


        txtTitle.setText(ctx.getString(R.string.my_complaints));
        txtSubTitle.setText("A history of my complaints to the City");
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(fab, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        mListener.onNewComplaintRequested();
                    }
                });
            }
        });

        txtUserName.setText(SharedUtil.getProfile(ctx).getFirstName() + " " + SharedUtil.getProfile(ctx).getLastName());
        animateSomething();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (MyComplaintsListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.getLocalClassName()
                    + " must implement MyComplaintsListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
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
                        Util.expand(hero, 600, new Util.UtilAnimationListener() {
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

    static final String LOG = MyComplaintsFragment.class.getSimpleName();
    String pageTitle;

    @Override
    public String getPageTitle() {
        return pageTitle;
    }

    @Override
    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

}
