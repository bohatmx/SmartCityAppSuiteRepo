package com.boha.library.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.boha.cityapps.R;
import com.boha.library.util.SharedUtil;

/**
 * Fragment to house citizen's basic data + picture
 */
public class CitizenFragment extends Fragment implements PageFragment {

    private CitizenFragmentListener mListener;

    public static CitizenFragment newInstance() {
        CitizenFragment fragment = new CitizenFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public CitizenFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    View view;
    TextView txtName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_citizen, container, false);
        txtName = (TextView) view.findViewById(R.id.CITIZEN_name);
        txtName.setText(SharedUtil.getName(getActivity()));
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (CitizenFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement CitizenFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface CitizenFragmentListener {
        public void onAccountDetailRequired();
    }

    static final String LOG = CitizenFragment.class.getSimpleName();
}
