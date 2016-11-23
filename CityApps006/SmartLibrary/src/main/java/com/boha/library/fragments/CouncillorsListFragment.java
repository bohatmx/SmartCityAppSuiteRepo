package com.boha.library.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.boha.library.R;
import com.boha.library.dto.AlertDTO;
import com.boha.library.jsonreader.AlertsRead;
import com.boha.library.jsonreader.CouncillorsRead;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.Statics;
import com.boha.library.util.WebCheck;
import com.boha.library.util.WebCheckResult;

/**
 * Created by Nkululeko on 2016/11/19.
 */

public class CouncillorsListFragment extends Fragment implements PageFragment {

    public static CouncillorsListFragment newInstance(ResponseDTO response) {
        CouncillorsListFragment fragment = new CouncillorsListFragment();
        Bundle args = new Bundle();
        args.putSerializable("response", response);
        fragment.setArguments(args);
        return fragment;
    }

    public CouncillorsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ResponseDTO r = (ResponseDTO) getArguments().getSerializable("response");
            //  alertList = r.getAlertList();
        }
    }

    View view;
    Context ctx;
    CouncillorsRead councillorsRead;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_councillors_list, container, false);
        ctx = getActivity();
        setFields();
        WebCheckResult wcr = WebCheck.checkNetworkAvailability(ctx, true);
        if (!wcr.isWifiConnected() && !wcr.isMobileConnected()) {
            Toast.makeText(getActivity(),"You are currently not connected to the network",Toast.LENGTH_LONG).show();
            //  Util.showSnackBar(view, "You are currently not connected to the network", "OK", Color.parseColor("red"));
        } else {
            councillorsRead = new CouncillorsRead(ctx, recyclerView);
            councillorsRead.execute();
        }
        return view;
    }

    private void setFields() {

        recyclerView = (RecyclerView) view.findViewById(R.id.COUNCILLORS_LIST_listView);
        LinearLayoutManager lm = new LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(lm);

        ctx = getActivity();

        animateSomething();
    }

    String pageTitle;
    int primaryColor, primaryDarkColor;

    @Override
    public void animateSomething() {

    }

    @Override
    public void setThemeColors(int primaryColor, int primaryDarkColor) {
        this.primaryColor = primaryColor;
        this.primaryDarkColor = primaryDarkColor;
    }

    @Override
    public String getPageTitle() {
        return pageTitle;
    }

    @Override
    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    int logo;

    public void setLogo(int logo) {
        this.logo = logo;
    }

    private CouncillorListener mListener;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (CouncillorListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement CouncillorListener");
        }
    }

    public interface CouncillorListener {

    }
}
