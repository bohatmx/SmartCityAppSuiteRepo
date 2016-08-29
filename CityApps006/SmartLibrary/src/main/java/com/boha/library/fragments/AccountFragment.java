package com.boha.library.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.activities.CityApplication;
import com.boha.library.activities.PaymentStartActivity;
import com.boha.library.dto.AccountDTO;
import com.boha.library.dto.PaymentSurveyDTO;
import com.boha.library.transfer.RequestDTO;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.NetUtil;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.Util;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.leakcanary.RefWatcher;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static com.facebook.FacebookSdk.getApplicationContext;

public class AccountFragment extends Fragment implements PageFragment {

    private AccountFragmentListener mListener;
    private AccountDTO account;
    private View view, detailView;
    private TextView
            txtBalance,
            txtArrears,
            txtLastUpdate, txtNextBill,
            txtAddress, txtLastBillAmount;
    int logo;
    Context ctx;
    Activity activity;
    int selectedIndex;

    FragmentManager fragmentManager;
    static final DecimalFormat df = new DecimalFormat("###,###,###,###,###,###,##0.00");
    static final String LOG = AccountFragment.class.getSimpleName();
    static final Locale loc = Locale.getDefault();
    static final SimpleDateFormat sdfDate = new SimpleDateFormat("EEE dd MMM yyyy", loc);
    static final SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm", loc);
    private static final String PDF_MIME_TYPE = "application/pdf";
    private static final String HTML_MIME_TYPE = "text/html";

    public static AccountFragment newInstance(AccountDTO account) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putSerializable("account", account);
        fragment.setArguments(args);
        return fragment;
    }


    public AccountFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            account = (AccountDTO) getArguments().getSerializable("account");
        }
        Resources.Theme theme = getActivity().getTheme();
        TypedValue typedValue = new TypedValue();
        theme.resolveAttribute(com.boha.library.R.attr.colorPrimaryDark, typedValue, true);
        primaryDarkColor = typedValue.data;
        theme.resolveAttribute(com.boha.library.R.attr.colorPrimary, typedValue, true);
        primaryColor = typedValue.data;

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_account, container, false);
        fragmentManager = getFragmentManager();
        activity = getActivity();
        ctx = getActivity();
        setFields();

        return view;
    }

    private void startPayment() {
        Log.d(LOG, "########## startPayment ....");
        setAnalyticsEvent("payment", "Payment clicked");
        AlertDialog.Builder d = new AlertDialog.Builder(getActivity());
        d.setTitle("Mobile Payment Survey")
                .setMessage("The payment facility is not available yet. The municipality is conducting a survey to find the level of interest in paying your account on the app.\n\n" +
                        "Do you want to be able to pay from the app?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendSurvey(true);
                        Intent intent = new Intent(ctx, PaymentStartActivity.class);
                        intent.putExtra("account", account);
                        intent.putExtra("index", selectedIndex);
                        intent.putExtra("logo", logo);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendSurvey(false);

                    }
                })
                .show();


    }
    FirebaseAnalytics mFirebaseAnalytics;
    private void setAnalyticsEvent(String id, String name) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);

        if (mFirebaseAnalytics == null) {
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
        }
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        Log.w(LOG,"analytics event sent .....");


    }
    private void sendSurvey(boolean response) {
        PaymentSurveyDTO x = new PaymentSurveyDTO();
        x.setMunicipalityID(SharedUtil.getMunicipality(getActivity()).getMunicipalityID());
        x.setResponse(response);
        x.setAccountNumber(account.getAccountNumber());

        RequestDTO w = new RequestDTO(RequestDTO.ADD_SURVEY);
        w.setPaymentSurvey(x);

        mListener.setBusy(true);
        NetUtil.sendRequest(getActivity(), w, new NetUtil.NetUtilListener() {
            @Override
            public void onResponse(ResponseDTO response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mListener.setBusy(false);
                    }
                });
            }

            @Override
            public void onError(final String message) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mListener.setBusy(false);
                        Util.showErrorToast(getActivity(), message);
                    }
                });
            }

            @Override
            public void onWebSocketClose() {

            }
        });
    }

    private void setFields() {
        detailView = view.findViewById(R.id.ACCT_detailLayout);

        txtBalance = (TextView) view.findViewById(R.id.ACCT_currBal);
        txtAddress = (TextView) view.findViewById(R.id.ACCT_address);
        txtArrears = (TextView) view.findViewById(R.id.ACCT_currArrears);
        txtLastUpdate = (TextView) view.findViewById(R.id.ACCT_lastUpdateDate);
        txtNextBill = (TextView) view.findViewById(R.id.ACCT_nextBillDate);
        txtLastBillAmount = (TextView) view.findViewById(R.id.ACCT_lastBillAmount);


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

        setAccountFields();
       // animateSomething();
    }



    int year, month;
    static final Locale LOCALE = Locale.getDefault();
    static final SimpleDateFormat sd = new SimpleDateFormat("EEEE dd MMMM yyyy", LOCALE);
    static final SimpleDateFormat sd1 = new SimpleDateFormat(" dd MMMM yyyy hh:mm a");
    public static final String CURRENCY_SYMBOL = "R";

    private void setAccountFields() {
        txtAddress.setText(account.getPropertyAddress());
        txtArrears.setText(CURRENCY_SYMBOL + df.format(account.getCurrentArrears()));
        txtBalance.setText(CURRENCY_SYMBOL + df.format(account.getCurrentBalance()));
        txtLastBillAmount.setText(CURRENCY_SYMBOL + df.format(account.getLastBillAmount()));
        if(account.getNextBillDate() != null){
            txtNextBill.setText(sd.format(account.getNextBillDate()));
        } else{
            txtNextBill.setText("Date not Set");
        }
        if (account.getDateLastUpdated() != null) {
            txtLastUpdate.setText(sd.format(account.getDateLastUpdated()));
        }

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = CityApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
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
        void onAccountStatementRequested(AccountDTO account);

        void onRefreshRequested();

        void setBusy(boolean busy);
    }

    @Override
    public void animateSomething() {
    }

    int primaryColor, primaryDarkColor;

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
