package com.boha.library.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.activities.CityApplication;
import com.boha.library.dto.AccountDTO;
import com.boha.library.dto.ComplaintDTO;
import com.boha.library.dto.ProfileInfoDTO;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.CacheUtil;
import com.boha.library.util.Statics;
import com.boha.library.util.Util;
import com.squareup.leakcanary.RefWatcher;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Fragment to house citizen's basic data + picture
 */
public class ProfileInfoFragment extends Fragment implements PageFragment {


    public static ProfileInfoFragment newInstance(ResponseDTO p) {
        ProfileInfoFragment fragment = new ProfileInfoFragment();
        Bundle args = new Bundle();
        args.putSerializable("response",p);
        fragment.setArguments(args);
        return fragment;
    }

    ResponseDTO response;
    public ProfileInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            response = (ResponseDTO) getArguments().getSerializable("response");
            profileInfo = response.getProfileInfoList().get(0);
            complaintList = response.getComplaintList();

        }

    }

    View view;
    TextView txtName, txtBalance, txtArrears, txtComplaints, txtResolved, txtAccounts;
    Button btnAccountDetails;
    ImageView heroImage, previousIMG, nextIMG;
    static final DecimalFormat df = new DecimalFormat("###,###,###,###,###,###,###,##0.00");
    ProfileInfoDTO profileInfo;
    double totBalance, totArrears;
    Context ctx;
    int logo;
    List<ComplaintDTO> complaintList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG,"****** ...onCreateView ....");
        view = inflater.inflate(R.layout.fragment_citizen, container, false);
        ctx = getActivity();
        setFields();
        txtName.setText(profileInfo.getFirstName() + " " + profileInfo.getLastName());
        getTotals();

        //getAccounts();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(LOG, "******* onResume ....");
        btnAccountDetails = (Button) view.findViewById(R.id.button);
    }


    private void getTotals() {
        totArrears = 0;
        totBalance = 0;
        if (profileInfo.getAccountList() == null) {
            profileInfo.setAccountList(new ArrayList<AccountDTO>());
        }
        for (AccountDTO acc : profileInfo.getAccountList()) {
            if (acc.getCurrentBalance() != null) {
                totBalance += acc.getCurrentBalance();
            }
            if (acc.getCurrentArrears() != null) {
                totArrears += acc.getCurrentArrears();
            }
        }
        String currency = "";
        txtAccounts.setText("" + profileInfo.getAccountList().size());
        txtArrears.setText(currency + df.format(totArrears));
        txtBalance.setText(currency + df.format(totBalance));
        btnAccountDetails.setText("My Account Details & Payment");
        txtComplaints.setText("0");
        txtResolved.setText("");
        if (complaintList != null) {
            txtComplaints.setText("" + complaintList.size());
            int resolved = 0;
            for (ComplaintDTO x : complaintList) {
                if (x.getActiveFlag() == Boolean.FALSE) {
                    resolved++;
                }
            }
            txtResolved.setText("" + resolved);
        }
        if (profileInfo.getAccountList().isEmpty()) {
            btnAccountDetails.setEnabled(false);
            btnAccountDetails.setAlpha(0.7f);
            txtBalance.setAlpha(0.6f);
            txtArrears.setAlpha(0.6f);
        }

    }

    List<AccountDTO> accountList = new ArrayList<AccountDTO>();


public static final String TAG = ProfileInfoFragment.class.getSimpleName();

    double balance, arrears;
    private void getAccounts() {
        CacheUtil.getCacheLoginData(ctx, new CacheUtil.CacheRetrievalListener() {
            @Override
            public void onCacheRetrieved(ResponseDTO response) {

                if (response.getAccountList() != null) {
                    accountList = response.getAccountList();
                    txtAccounts.setText("" + accountList.size());
                    for (AccountDTO a: accountList) {
                        balance += a.getCurrentBalance();
                        arrears += a.getCurrentArrears();
                        txtArrears.setText(df.format(arrears));
                        txtBalance.setText(df.format(balance));
                        Log.d(TAG, "onCacheRetrieved: adding up balance: " + balance + " arrears: " + arrears);
                    }
                    txtAccounts.setText("" + accountList.size());
                } else {
                    txtAccounts.setText("0");
                }

            }

            @Override
            public void onError() {

            }
        });
    }

    private void setFields() {
        txtName = (TextView) view.findViewById(R.id.CITIZEN_name);
        txtBalance = (TextView) view.findViewById(R.id.DASH_currBal);
        btnAccountDetails = (Button) view.findViewById(R.id.button);
        txtArrears = (TextView) view.findViewById(R.id.DASH_arrears);
        txtComplaints = (TextView) view.findViewById(R.id.DASH_complaintCount);
        txtResolved = (TextView) view.findViewById(R.id.DASH_resolvedCount);
        heroImage = (ImageView) view.findViewById(R.id.CITIZEN_image);
        txtAccounts = (TextView) view.findViewById(R.id.DASH_accounts);

        txtComplaints.setText("" + complaintList.size());
        txtResolved.setText("0");

        txtAccounts.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        startAccountActivity();
                    }
                });


        Statics.setRobotoFontLight(ctx, txtBalance);
        Statics.setRobotoFontLight(ctx, txtArrears);

        btnAccountDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(btnAccountDetails, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        startAccountActivity();
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
                        startAccountActivity();
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
                        startAccountActivity();
                    }
                });
            }
        });
        txtComplaints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(txtComplaints, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        //startMyComplaintsActivity();
                    }
                });
            }
        });



    }

    private void startAccountActivity() {
        profileInfoListener.onAccountDetailRequested();
    }

    @Override
    public void onAttach(Activity a) {
        super.onAttach(a);
        if (a instanceof ProfileInfoListener) {
            profileInfoListener = (ProfileInfoListener)a;
        } else {
            throw new ClassCastException("Host activity "
            + a.getLocalClassName() + " must implement ProfileInfoListener");
        }

    }

    public interface ProfileInfoListener {
        void onAccountDetailRequested();
    }
    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = CityApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }
    static final String LOG = ProfileInfoFragment.class.getSimpleName();


    @Override
    public void animateSomething() {

        Util.flashSeveralTimes(btnAccountDetails, 50, 3, null);
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            heroImage.setImageDrawable(Util.getRandomBackgroundImage(ctx));
                            timer.cancel();
                        }
                    });

                } catch (Exception e) {
                    timer.cancel();
                }
            }
        }, 50);

    }

    int primaryColor, primaryDarkColor;
    ProfileInfoListener profileInfoListener;

    @Override
    public void setThemeColors(int primaryColor, int primaryDarkColor) {
        this.primaryColor = primaryColor;
        this.primaryDarkColor = primaryDarkColor;
    }

    String pageTitle;

    @Override
    public String getPageTitle() {
        return pageTitle;
    }

    @Override
    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public void setLogo(int logo) {
        this.logo = logo;
    }
}
