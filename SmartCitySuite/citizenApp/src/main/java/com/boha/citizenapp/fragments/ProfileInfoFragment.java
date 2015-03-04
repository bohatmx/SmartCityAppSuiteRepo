package com.boha.citizenapp.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.boha.citizenapp.R;
import com.boha.citizenapp.activities.AccountActivity;
import com.boha.citylibrary.dto.AccountDTO;
import com.boha.citylibrary.dto.ProfileInfoDTO;
import com.boha.citylibrary.transfer.ResponseDTO;
import com.boha.citylibrary.util.CacheUtil;
import com.boha.citylibrary.util.SharedUtil;
import com.boha.citylibrary.util.Util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Fragment to house citizen's basic data + picture
 */
public class ProfileInfoFragment extends Fragment implements PageFragment {


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
        Log.w(LOG,"onCreateView");
        view = inflater.inflate(R.layout.fragment_citizen, container, false);
        ctx = getActivity();
        setFields();

        profileInfo = SharedUtil.getProfile(getActivity());
        txtName.setText(profileInfo.getFirstName() + " " + profileInfo.getLastName());
        getCachedInfo();

        return view;
    }

    private void getCachedInfo() {
        Log.e(LOG,"getCachedInfo");
        CacheUtil.getCacheLoginData(ctx, new CacheUtil.CacheRetrievalListener() {
            @Override
            public void onCacheRetrieved(ResponseDTO response) {
                Log.w(LOG, "Cache has returned, checking for data");
                if (response != null) {
                    profileInfo = response.getProfileInfoList().get(0);
                    getTotals();
                }
            }

            @Override
            public void onError() {

            }
        });
    }
    public void setProfileInfo(ProfileInfoDTO profileInfo) {
        this.profileInfo = profileInfo;
        getTotals();
    }
    private void getTotals() {
        Log.w(LOG, "getTotals - setting field values");
        totArrears = 0;
        totBalance = 0;
        if (profileInfo.getAccountList() == null) {
            profileInfo.setAccountList(new ArrayList<AccountDTO>());
        }
        for (AccountDTO acc: profileInfo.getAccountList()) {
            if (acc.getCurrentBalance() != null) {
                totBalance += acc.getCurrentBalance();
            }
            if (acc.getCurrentArrears() != null) {
                totArrears += acc.getCurrentArrears();
            }
        }
        String currency = "R";
        txtArrears.setText(currency + df.format(totArrears));
        txtBalance.setText(currency + df.format(totBalance));
        txtAccts.setText(""+profileInfo.getAccountList().size());

    }
    private void setFields() {
        txtName = (TextView) view.findViewById(R.id.CITIZEN_name);
        txtBalance = (TextView) view.findViewById(R.id.CITIZEN_balance);
        txtAccts = (TextView) view.findViewById(R.id.CITIZEN_accts);
        txtArrears = (TextView) view.findViewById(R.id.CITIZEN_arrears);
        heroImage = (ImageView) view.findViewById(R.id.CITIZEN_image);

        txtAccts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(txtAccts, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        Intent intent = new Intent(ctx, AccountActivity.class);
                        intent.putExtra("profileInfo",profileInfo);
                        startActivity(intent);
                    }
                });
            }
        });
        txtBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(txtBalance, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        Intent intent = new Intent(ctx, AccountActivity.class);
                        intent.putExtra("profileInfo",profileInfo);
                        startActivity(intent);
                    }
                });
            }
        });
        txtArrears.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(txtArrears, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        Intent intent = new Intent(ctx, AccountActivity.class);
                        intent.putExtra("profileInfo",profileInfo);
                        startActivity(intent);
                    }
                });
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

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }



    static final String LOG = ProfileInfoFragment.class.getSimpleName();
}
