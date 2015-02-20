package com.boha.library.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.boha.cityapps.R;
import com.boha.library.dto.AccountDTO;
import com.boha.library.dto.ProfileInfoDTO;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.Util;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Fragment to house citizen's basic data + picture
 */
public class ProfileInfoFragment extends Fragment implements PageFragment {

    private ProfileInfoFragmentListener mListener;

    public static ProfileInfoFragment newInstance() {
        ProfileInfoFragment fragment = new ProfileInfoFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public ProfileInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    View view;
    TextView txtName, txtBalance, txtArrears, txtAccts;
    ImageView heroImage;
    static final DecimalFormat df = new DecimalFormat("###,###,###,###,###,###,###,##0.00");
    ProfileInfoDTO profileInfo;
    double totBalance, totArrears;
    Context ctx;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_citizen, container, false);
        ctx = getActivity();
        setFields();

        profileInfo = SharedUtil.getProfile(getActivity());
        txtName.setText(profileInfo.getFirstName() + " " + profileInfo.getLastName());
        getTotals();

        return view;
    }

    private void getTotals() {
        totArrears = 0;
        totBalance = 0;
        for (AccountDTO acc: profileInfo.getAccountList()) {
            if (acc.getCurrentBalance() != null) {
                totBalance += acc.getCurrentBalance();
            }
            if (acc.getCurrentArrears() != null) {
                totArrears += acc.getCurrentArrears();
            }
        }
        txtArrears.setText(df.format(totArrears));
        txtBalance.setText(df.format(totBalance));
        if (profileInfo.getAccountList() != null) {
            txtAccts.setText(""+profileInfo.getAccountList().size());
        } else {
            txtAccts.setText("0");
        }
    }
    private void setFields() {
        txtName = (TextView) view.findViewById(R.id.CITIZEN_name);
        txtBalance = (TextView) view.findViewById(R.id.CITIZEN_balance);
        txtAccts = (TextView) view.findViewById(R.id.CITIZEN_accts);
        txtArrears = (TextView) view.findViewById(R.id.CITIZEN_arrears);
        heroImage = (ImageView) view.findViewById(R.id.CITIZEN_image);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Util.showToast(ctx,ctx.getString(R.string.under_cons));
            }
        });

        final Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            heroImage.setImageDrawable(Util.getRandomBanner(ctx));
                        }
                    });

                } catch (Exception e) {
                    timer.cancel();
                }
            }
        }, 1000, 5000);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (ProfileInfoFragmentListener) activity;
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

    public interface ProfileInfoFragmentListener {
        public void onAccountDetailRequired();
    }

    static final String LOG = ProfileInfoFragment.class.getSimpleName();
}
