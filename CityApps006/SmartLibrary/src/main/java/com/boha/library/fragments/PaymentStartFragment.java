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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.activities.StatementActivity;
import com.boha.library.dto.AccountDTO;
import com.boha.library.util.Statics;
import com.boha.library.util.Util;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.boha.library.fragments.PaymentStartFragment.PaymentStartListener} interface
 * to handle interaction events.
 * Use the {@link PaymentStartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PaymentStartFragment extends Fragment implements PageFragment{

    private PaymentStartListener mListener;
    private AccountDTO account;
    private View view, topView;
    private int index, logo;
    private TextView txtTitle, txtSubTitle, txtFAB;
    private EditText editAmount;
    private ImageView fabIcon, hero, icon;
    private Button btnPay;
    Context ctx;

    static final String LOG = PaymentStartFragment.class.getSimpleName();
    static final DecimalFormat df = new DecimalFormat("###,###,###,###,###,###,##0.00");

    public static PaymentStartFragment newInstance(AccountDTO account) {
        PaymentStartFragment fragment = new PaymentStartFragment();
        Bundle args = new Bundle();
        args.putSerializable("account", account);
        fragment.setArguments(args);
        return fragment;
    }

    public PaymentStartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w(LOG, "### onCreate");
        if (getArguments() != null) {
            account = (AccountDTO) getArguments().getSerializable("account");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.w(LOG, "### onCreateView");
        ctx = getActivity();
        view = inflater.inflate(R.layout.fragment_payment_start, container, false);
        setFields();
        return view;
    }

    public void setAccount(AccountDTO account, int index) {
        Log.w(LOG, "### setAccount");
        this.account = account;
        this.index = index;

        
        txtTitle.setText(account.getCustomerAccountName());
        txtSubTitle.setText(ctx.getString(R.string.acct_paymnts) + " - " + account.getAccountNumber());
        editAmount.setText("" + account.getCurrentBalance());


    }

    private void setFields() {
        Log.w(LOG, "### setFields");
        txtTitle = (TextView)view.findViewById(R.id.TOP_title);
        topView = view.findViewById(R.id.TOP_titleLayout);
        btnPay = (Button)view.findViewById(R.id.button);
        txtSubTitle = (TextView)view.findViewById(R.id.TOP_subTitle);
        txtFAB = (TextView)view.findViewById(R.id.FAB_text);
        fabIcon = (ImageView)view.findViewById(R.id.FAB_icon);
        hero = (ImageView)view.findViewById(R.id.TOP_heroImage);
        editAmount = (EditText)view.findViewById(R.id.PAY_amount);
        icon = (ImageView) topView.findViewById(R.id.TOP_icon);
        txtFAB.setVisibility(View.GONE);
        txtFAB.setText(getActivity().getString(R.string.pay));
        fabIcon.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_action_secure));
        fabIcon.setVisibility(View.VISIBLE);
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(icon, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        Intent w = new Intent(ctx, StatementActivity.class);
                        w.putExtra("primaryColor", primaryColor);
                        w.putExtra("darkColor", primaryDarkColor);
                        w.putExtra("logo", logo);
                        startActivity(w);

                    }
                });

            }
        });
        Statics.setRobotoFontLight(getActivity(),editAmount);
        animateSomething();
        
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (PaymentStartListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement PaymentStartListener");
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
    public interface PaymentStartListener {
        public void onPaymentTypeSelected(int paymentType);
    }

    public static final int
                  VISA = 1,
                  MASTERCARD = 2,
                  INSTANT_EFT = 3,
                  ABSA = 4;

    @Override
    public void animateSomething() {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        timer.cancel();
                        hero.setImageDrawable(Util.getRandomBackgroundImage(ctx));
                        Util.expand(hero, 1000, new Util.UtilAnimationListener() {
                            @Override
                            public void onAnimationEnded() {
                                Util.flashSeveralTimes(btnPay, 30, 3, null);
                            }
                        });
                    }
                });
            }
        }, 500);
    }

    int primaryColor,  primaryDarkColor;
    @Override
    public void setThemeColors(int primaryColor, int primaryDarkColor) {
        this.primaryColor = primaryColor;
        this.primaryDarkColor = primaryDarkColor;
        if (topView != null) {
            topView.setBackgroundColor(primaryColor);
        }
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
