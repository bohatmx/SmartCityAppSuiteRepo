package com.boha.foureyes.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.boha.foureyes.R;
import com.boha.foureyes.adapters.DashboardAdapter;
import com.boha.foureyes.dto.ResponseDTO;
import com.boha.foureyes.dto.SummaryDTO;
import com.boha.foureyes.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aubreyM on 2014/04/09.
 */
public class DashboardListFragment extends Fragment implements PageFragment {

    TextView txtMsg;
    ImageView imageView;

    public interface DashboardListListener {
        void onDashboardRefresh();
    }
    DashboardListListener dashboardListListener;


    @Override
    public void onAttach(Activity a) {
        if (a instanceof DashboardListListener) {
            dashboardListListener = (DashboardListListener)a;
        } else {
            throw new UnsupportedOperationException("Host activity " + a.getLocalClassName() +
            " must implemet DashboardListListener");
        }
        super.onAttach(a);
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saved) {
        ctx = getActivity();
        inflater = getActivity().getLayoutInflater();
        view = inflater
                .inflate(R.layout.fragmen_dashboard_list, container, false);

        setFields();
        if (response != null) {
            setList();
            return view;
        }
        if (saved != null) {
            response = (ResponseDTO) saved.getSerializable("response");
        } else {
            Bundle bundle = getArguments();
            if (bundle != null) {
                response = (ResponseDTO) bundle.getSerializable("response");
                if (response.getSummaryList() == null)
                    summaryList = new ArrayList<SummaryDTO>();
                else
                    summaryList = response.getSummaryList();
            }
        }
        setList();

        return view;
    }

    public void setDashBoardList(List<SummaryDTO> list) {
        Log.i(LOG, "setting summaryList ....");
        this.summaryList = list;
        setList();

    }

    @Override
    public void onSaveInstanceState(Bundle b) {
        b.putSerializable("response", response);
        super.onSaveInstanceState(b);
    }

    public void setFields() {
        listView = (ListView) view.findViewById(R.id.FD_list);
        txtCount = (TextView) view.findViewById(R.id.FD_muniCount);

        txtCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(txtCount, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        dashboardListListener.onDashboardRefresh();
                    }
                });

            }
        });

    }

    public void setList() {
        if (summaryList == null)
            summaryList = new ArrayList<SummaryDTO>();

        for (SummaryDTO x: summaryList) {
            x.setImageResource(Util.getRandomBackgroundImageResource(ctx));
        }

        dashboardAdapter = new DashboardAdapter(ctx, R.layout.muni_dasboard_card, summaryList);
        listView.setAdapter(dashboardAdapter);
        txtCount.setText("" + summaryList.size());

    }


    ListView listView;
    TextView txtCount;

    static final String LOG = DashboardListFragment.class.getSimpleName();
    Context ctx;
    View view;
    ResponseDTO response;
    List<SummaryDTO> summaryList;
    DashboardAdapter dashboardAdapter;

}
