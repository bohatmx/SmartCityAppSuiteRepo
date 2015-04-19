package com.boha.foureyes.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.boha.foureyes.R;
import com.boha.foureyes.dto.ResponseDTO;
import com.boha.foureyes.util.Util;

import java.text.DecimalFormat;

/**
 * Created by aubreyM on 2014/04/09.
 */
public class ServerLogFragment extends Fragment implements PageFragment {

    ScrollView scrollView;
    public interface LogListener {
        void onLogRefreshRequested();
    }
    LogListener logListener;


    @Override
    public void onAttach(Activity a) {
        if (a instanceof LogListener) {
            logListener = (LogListener)a;
        } else {
            throw new UnsupportedOperationException("Host activity " + a.getLocalClassName() +
                    " must implement LogListener");
        }
//        Log.i(LOG,
//                "onAttach ---- Fragment called and hosted by "
//                        + a.getLocalClassName()
//        );
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
                .inflate(R.layout.fragment_log, container, false);
        scrollView = (ScrollView) view.findViewById(R.id.LOG_scroll);
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
            }
        }
        setList();

        return view;
    }


    @Override
    public void onSaveInstanceState(Bundle b) {
        b.putSerializable("response", response);
        super.onSaveInstanceState(b);
    }

    public void setFields() {
        txtLog = (TextView) view.findViewById(R.id.LOG_log);
        txtCount = (TextView) view.findViewById(R.id.LOG_count);

        txtCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(txtCount, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        logListener.onLogRefreshRequested();
                    }
                });
            }
        });


    }

    public void setList() {
        txtLog.setText(response.getLog());
        txtCount.setText(df.format(getKB(response.getLog().length())) + " KB");

        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });


    }

    private double getKB(int length) {
        Double d = Double.valueOf("" + length) / Double.valueOf("1024");
        return d.doubleValue();
    }

    static final DecimalFormat df = new DecimalFormat("###,###,###,###,###,##0.00");
    TextView txtLog, txtCount;

    static final String LOG = "ServerLogFrag";
    Context ctx;
    View view;
    ResponseDTO response;


}
