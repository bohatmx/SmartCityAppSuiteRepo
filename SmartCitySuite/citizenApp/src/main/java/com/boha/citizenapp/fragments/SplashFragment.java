package com.boha.citizenapp.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.boha.citizenapp.R;
import com.boha.library.dto.MunicipalityDTO;
import com.boha.library.util.SharedUtil;

/**
 * Fragment to house local pictures
 */
public class SplashFragment extends Fragment implements PageFragment {


    public static SplashFragment newInstance() {
        SplashFragment fragment = new SplashFragment();
        return fragment;
    }

    public SplashFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    View view;
    ImageView heroImage;
    TextView txtTitle;
    Context ctx;
    MunicipalityDTO municipality;
    String title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_splash, container, false);
        txtTitle = (TextView) view.findViewById(R.id.FRAG_SPLASH_caption);
        heroImage = (ImageView) view.findViewById(R.id.FRAG_SPLASH_heroImage);
        ctx = getActivity();

        municipality = SharedUtil.getMunicipality(ctx);
        txtTitle.setText(municipality.getMunicipalityName());
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        try {
//            mListener = (ImageGridFragmentListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement ImageGridFragmentListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

    static final String LOG = SplashFragment.class.getSimpleName();

    @Override
    public String getPageTitle() {
        return title;
    }

    @Override
    public void setPageTitle(String title) {
        this.title = title;
    }
}
