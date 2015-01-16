package com.boha.library.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.boha.cityapps.R;
import com.boha.library.dto.AlertDTO;
import com.boha.library.util.TrafficLightUtil;
import com.boha.library.util.Util;

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
    TextView green, yellow, red;
    View trafficLights;
    Context ctx;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_create_alert, container, false);
        ctx = getActivity();
        setFields();

        return view;
    }

    private void setFields() {
        green = (TextView)view.findViewById(R.id.TRAFF_green);
        red = (TextView)view.findViewById(R.id.TRAFF_red);
        yellow = (TextView)view.findViewById(R.id.TRAFF_yellow);
        trafficLights = view.findViewById(R.id.TRAFF_main);

        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(green, 200, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        TrafficLightUtil.setGreen(ctx, trafficLights);
                    }
                });
            }
        });
        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(red,200,new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        TrafficLightUtil.setRed(ctx, trafficLights);
                    }
                });
            }
        });
        yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(yellow,200,new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        TrafficLightUtil.setYellow(ctx, trafficLights);
                    }
                });
            }
        });
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
}
