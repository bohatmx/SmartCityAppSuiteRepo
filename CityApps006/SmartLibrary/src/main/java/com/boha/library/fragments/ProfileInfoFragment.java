package com.boha.library.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.boha.library.activities.MyComplaintsActivity;
import com.boha.library.dto.AccountDTO;
import com.boha.library.dto.ComplaintDTO;
import com.boha.library.dto.ProfileInfoDTO;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.CacheUtil;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.Statics;
import com.boha.library.util.Util;

import org.acra.ACRA;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
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

    }

    View view;
    TextView txtName, txtBalance, txtArrears, txtComplaints, txtResolved;
    Button btnAccountDetails;
    ImageView heroImage;
    static final DecimalFormat df = new DecimalFormat("###,###,###,###,###,###,###,##0.00");
    ProfileInfoDTO profileInfo;
    double totBalance, totArrears;
    Context ctx;
    String title;
    int logo;
    List<ComplaintDTO> complaintList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG,"******* onCreateView ....");
        view = inflater.inflate(R.layout.fragment_citizen, container, false);
        ctx = getActivity();
        setFields();

        profileInfo = SharedUtil.getProfile(getActivity());
        txtName.setText(profileInfo.getFirstName() + " " + profileInfo.getLastName());
        getCachedInfo();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(LOG, "******* onResume ....");
        btnAccountDetails = (Button) view.findViewById(R.id.button);
    }

    private void getCachedInfo() {
        CacheUtil.getCacheLoginData(ctx, new CacheUtil.CacheRetrievalListener() {
            @Override
            public void onCacheRetrieved(ResponseDTO response) {
                if (response != null) {
                    profileInfo = response.getProfileInfoList().get(0);
                    complaintList = response.getComplaintList();
                    getTotals();
                }
            }

            @Override
            public void onError() {

            }
        });
    }

    private void setProfileInfo(ProfileInfoDTO profileInfo) {
        this.profileInfo = profileInfo;
        Log.d(LOG,"******* setProfileInfo ....");
        getTotals();
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
        String currency = "R";
        if (txtArrears == null) {
            Log.e(LOG, "--- strange, txtArrears is NULL");
            try {
                ACRA.getErrorReporter().handleException(
                        new UnsupportedOperationException("Things fucked up. txtArrears is NULL"),false);
            } catch (Exception e) {}
            return;
        }
        txtArrears.setText(currency + df.format(totArrears));
        txtBalance.setText(currency + df.format(totBalance));
        btnAccountDetails.setText("Account Details & Payment");
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

    }

    private void setFields() {
        txtName = (TextView) view.findViewById(R.id.CITIZEN_name);
        txtBalance = (TextView) view.findViewById(R.id.DASH_currBal);
        btnAccountDetails = (Button) view.findViewById(R.id.button);
        txtArrears = (TextView) view.findViewById(R.id.DASH_arrears);
        txtComplaints = (TextView) view.findViewById(R.id.DASH_complaintCount);
        txtResolved = (TextView) view.findViewById(R.id.DASH_resolvedCount);
        heroImage = (ImageView) view.findViewById(R.id.CITIZEN_image);

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
                        startMyComplaintsActivity();
                    }
                });
            }
        });



    }

    private void startAccountActivity() {
        profileInfoListener.onAccountDetailRequested(profileInfo);
//        Intent intent = new Intent(ctx, FakeMainActivity.class);
//        intent.putExtra("profileInfo", profileInfo);
//        intent.putExtra("logo", logo);
//        intent.putExtra("darkColor",primaryDarkColor);
//        intent.putExtra("primaryColor",primaryColor);
//        startActivity(intent);
    }
    private void startMyComplaintsActivity() {
        Intent intent = new Intent(ctx, MyComplaintsActivity.class);
        intent.putExtra("darkColor", primaryDarkColor);
        intent.putExtra("primaryColor", primaryColor);
        intent.putExtra("logo",logo);
        startActivity(intent);
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
        void onAccountDetailRequested(ProfileInfoDTO profileInfo);
    }
    @Override
    public void onDetach() {
        super.onDetach();
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
                            Util.expand(heroImage, 1000, new Util.UtilAnimationListener() {
                                @Override
                                public void onAnimationEnded() {
                                    timer.cancel();
                                }
                            });
                        }
                    });

                } catch (Exception e) {
                    timer.cancel();
                }
            }
        }, 1000);

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
