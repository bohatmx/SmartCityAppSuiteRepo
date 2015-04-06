package com.boha.library.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.dto.AccountDTO;
import com.boha.library.util.Statics;

import java.text.DecimalFormat;

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
    private ImageView fabIcon;
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

        txtSubTitle = (TextView)view.findViewById(R.id.TOP_subTitle);
        txtFAB = (TextView)view.findViewById(R.id.FAB_text);
        fabIcon = (ImageView)view.findViewById(R.id.FAB_icon);
        editAmount = (EditText)view.findViewById(R.id.PAY_amount);
        txtFAB.setVisibility(View.GONE);
        txtFAB.setText(getActivity().getString(R.string.pay));
        fabIcon.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_action_secure));
        fabIcon.setVisibility(View.VISIBLE);

        Statics.setRobotoFontLight(getActivity(),editAmount);
        
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
