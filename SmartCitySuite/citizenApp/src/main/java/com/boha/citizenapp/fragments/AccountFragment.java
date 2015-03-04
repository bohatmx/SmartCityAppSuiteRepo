package com.boha.citizenapp.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.boha.citizenapp.R;
import com.boha.citizenapp.adapters.AccountAdapter;
import com.boha.citizenapp.activities.PaymentStartActivity;
import com.boha.citylibrary.dto.AccountDTO;
import com.boha.citylibrary.dto.ProfileInfoDTO;
import com.boha.citylibrary.util.Util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.boha.citizenapp.fragments.AccountFragment.AccountFragmentListener} interface
 * to handle interaction events.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {

    private AccountFragmentListener mListener;
    private ProfileInfoDTO profileInfo;
    private View view, detailView, topView, handle;
    private TextView
            txtName, txtAcctNumber, txtSubtitle,
            txtCurrBal, txtArrears, txtFAB,
            txtLastUpdate, txtNextBill,
            txtAddress, txtLastBillAmount;
    ScrollView scrollView;
    RecyclerView recyclerView;
    AccountAdapter adapter;
    ImageView icon;
    Context ctx;
    Activity activity;
    AccountDTO account;
    int selectedIndex;
    static final DecimalFormat df = new DecimalFormat("###,###,###,###,###,###,##0.00");
    static final String LOG = AccountFragment.class.getSimpleName();
    static final Locale loc = Locale.getDefault();
    static final SimpleDateFormat sdfDate = new SimpleDateFormat("EEE dd MMM yyyy", loc);
    static final SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm", loc);

    public static AccountFragment newInstance(ProfileInfoDTO profileInfo) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putSerializable("profileInfo", profileInfo);
        fragment.setArguments(args);
        return fragment;
    }

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w(LOG, "### onCreate");
        if (getArguments() != null) {
            profileInfo = (ProfileInfoDTO) getArguments().getSerializable("profileInfo");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(LOG, "### onCreateView");
        view = inflater.inflate(R.layout.fragment_account, container, false);
        activity = getActivity();
        ctx = getActivity();
        setFields();

        if (profileInfo != null) {
            if (!profileInfo.getAccountList().isEmpty()) {
                account = profileInfo.getAccountList().get(0);
                setAccountFields(account);
            }

        }
        //LocaleUtil.setLocale(ctx, LocaleUtil.ENGLISH);
        return view;
    }

    public void setProfileInfo(ProfileInfoDTO profileInfo) {
        this.profileInfo = profileInfo;
        account = profileInfo.getAccountList().get(0);
        setAccountFields(account);
        txtFAB.setText("1");

    }

    private void startPayment() {
        Intent intent = new Intent(ctx, PaymentStartActivity.class);
        intent.putExtra("account", account);
        intent.putExtra("index", selectedIndex);
        startActivity(intent);

    }

    private void setFields() {
        topView = view.findViewById(R.id.template);
        handle = view.findViewById(R.id.ACCT_handle);
        scrollView = (ScrollView) view.findViewById(R.id.ACCT_scroll);
        detailView = view.findViewById(R.id.ACCT_detailLayout);
        txtName = (TextView) topView.findViewById(R.id.TOP_title);
        txtSubtitle = (TextView) topView.findViewById(R.id.TOP_subTitle);
        icon = (ImageView) topView.findViewById(R.id.TOP_icon);
//        icon.setImageDrawable(ctx.getResources().getDrawable(R.drawable.document_48));
        txtFAB = (TextView) topView.findViewById(R.id.TOP_fab);

        txtAcctNumber = (TextView) view.findViewById(R.id.ACCT_number);
        txtAddress = (TextView) view.findViewById(R.id.ACCT_address);
        txtArrears = (TextView) view.findViewById(R.id.ACCT_currArrears);
        txtLastUpdate = (TextView) view.findViewById(R.id.ACCT_lastUpdateDate);
        txtCurrBal = (TextView) view.findViewById(R.id.ACCT_currBal);
        txtNextBill = (TextView) view.findViewById(R.id.ACCT_nextBillDate);
        txtLastBillAmount = (TextView) view.findViewById(R.id.ACCT_lastBillAmount);

        txtCurrBal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(txtCurrBal, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        startPayment();
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
                        startPayment();
                    }
                });
            }
        });
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(icon, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        Util.showToast(ctx, ctx.getString(com.boha.citylibrary.R.string.under_cons));

                    }
                });

            }
        });
        txtFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(txtFAB, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        if (profileInfo.getAccountList().size() > 1) {
                            List<String> list = new ArrayList<String>();
                            for (AccountDTO a : profileInfo.getAccountList()) {
                                list.add("" + a.getAccountNumber() + " - " + a.getCustomerAccountName());
                            }
                            Util.showPopupBasicWithHeroImage(ctx, activity, list, handle, "Accounts", new Util.UtilPopupListener() {
                                @Override
                                public void onItemSelected(int index) {
                                    account = profileInfo.getAccountList().get(index);
                                    setAccountFields(account);
                                    txtFAB.setText("" + (index + 1));
                                    selectedIndex = index;
                                    Util.expand(detailView, 1000, null);
                                }
                            });
                        }
                    }
                });
            }
        });


    }

    static final Locale LOCALE = Locale.getDefault();
    static final SimpleDateFormat sd = new SimpleDateFormat("dd MMMM yyyy");

    private void setAccountFields(AccountDTO account) {
        String currency = "R";
        txtAcctNumber.setText(account.getAccountNumber());
        txtAddress.setText(account.getPropertyAddress());

        txtArrears.setText(currency + df.format(account.getCurrentArrears()));
        txtCurrBal.setText(currency + df.format(account.getCurrentBalance()));
        txtLastBillAmount.setText(currency + df.format(account.getLastBillAmount()));

        txtLastUpdate.setText(sdfDate.format(account.getDateLastUpdated()));
        txtName.setText(account.getCustomerAccountName());
        txtNextBill.setText(sdfDate.format(account.getNextBillDate()));
        txtSubtitle.setText("Date Last Update : " + sdfDate.format(account.getDateLastUpdated()));


        Util.expand(detailView, 1000, null);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (AccountFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement AccountFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface AccountFragmentListener {
        public void onAccountStatementRequested(AccountDTO account);

        public void onRefreshRequested();
    }

}
