package com.boha.library.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.activities.CityApplication;
import com.boha.library.adapters.CardTypePopupListAdapter;
import com.boha.library.dto.AccountDTO;
import com.boha.library.dto.CreditCard;
import com.boha.library.dto.FNBandNedbankResponseDTO;
import com.boha.library.dto.PaymentRequestDTO;
import com.boha.library.transfer.RequestDTO;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.CreditCardValidator;
import com.boha.library.util.NetUtil;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.Util;
import com.squareup.leakcanary.RefWatcher;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
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
public class PaymentStartFragment extends Fragment implements PageFragment {

    private PaymentStartListener mListener;
    private AccountDTO account;
    private View view, handle;
    private int index, logo;
    private TextView txtTitle, txtSubTitle, txtSelect;
    private EditText editAmount, editCardholder, editNumber, editCCV;
    Spinner monthSpinner, yearSpinner;
    private ImageView hero, icon;
    private Button btnPay;

    private String selectedCard;
    private FNBandNedbankResponseDTO FNBandNedbankResponse;
    private Context ctx;
    private ListPopupWindow popupWindow;
    private LayoutInflater inflater;

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
        this.inflater = inflater;
        view = inflater.inflate(R.layout.fragment_payment_request, container, false);
        setFields();
        if (account != null) {
            txtSubTitle.setText("Account No - " + account.getAccountNumber());
            editAmount.setText(df.format(account.getCurrentBalance()));
        }
        return view;
    }

    public void setAccount(AccountDTO account, int index) {
        Log.w(LOG, "### setAccount");
        this.account = account;
        this.index = index;

        if (txtSubTitle != null) {
            txtSubTitle.setText("Account No - " + account.getAccountNumber());
            editAmount.setText(df.format(account.getCurrentBalance()));
        }


    }

    boolean isDebuggable;
    private void setFields() {
        Log.w(LOG, "### setFields");
        handle = view.findViewById(R.id.FPR_handle);
        txtSubTitle = (TextView) view.findViewById(R.id.FPR_subtitle);
        txtSelect = (TextView) view.findViewById(R.id.FPR_name);
        editCardholder = (EditText) view.findViewById(R.id.FPR_cardHolder);
        editNumber = (EditText) view.findViewById(R.id.FPR_cardNumber);
        editCCV = (EditText) view.findViewById(R.id.FPR_ccv);
        editAmount = (EditText) view.findViewById(R.id.FPR_amout);
        monthSpinner = (Spinner) view.findViewById(R.id.FPR_month);
        yearSpinner = (Spinner) view.findViewById(R.id.FPR_year);
        hero = (ImageView) view.findViewById(R.id.FPR_hero);

        btnPay = (Button) view.findViewById(R.id.FPR_btn);
        icon = (ImageView) view.findViewById(R.id.FPR_icon);

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPayment();
            }
        });
        txtSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCardTypePopup();
            }
        });
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCardTypePopup();
            }
        });
        setSpinners();
         isDebuggable = 0 != (getActivity().getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE);
        if (isDebuggable) {
            creditCard = new CreditCard();
            creditCard.setCardHolder("Test Buyer VS1");
            creditCard.setCardNumber("4000 0000 0000 0010");
            creditCard.setCcv("111");
            creditCard.setExpiryMonth(11);
            creditCard.setExpiryYear(2020);
            creditCard.setCardType("Visa");
            SharedUtil.saveCreditCard(getActivity(),creditCard);

        }

        creditCard = SharedUtil.getCreditCard(getActivity());
        if (creditCard != null) {
            editCardholder.setText(creditCard.getCardHolder());
            editNumber.setText(creditCard.getCardNumber());
            editCCV.setText(creditCard.getCcv());

            monthSpinner.setSelection(creditCard.getExpiryMonth());
            int index = 0;
            for (String s: yearList) {
                if (index == 0) {
                    index++;
                    continue;
                }
                if (creditCard.getExpiryYear() == Integer.parseInt(s)) {
                    break;
                }
                index++;
            }
            if (index < yearList.size()) {
                yearSpinner.setSelection(index);
            }
            if (creditCard.getCardType() != null) {
                selectedCard = creditCard.getCardType();
                Util.setCardTypeIcon(selectedCard, icon, ctx);
                txtSelect.setText(selectedCard);
            }
        }

        animateSomething();

    }
    CreditCard creditCard;
    private void sendPayment() {

        if (editCardholder.getText().toString().isEmpty()) {
            Util.showToast(getActivity(), "Please enter name on the the card");
            return;
        }
        if (editNumber.getText().toString().isEmpty()) {
            Util.showToast(getActivity(), "Please enter the card number");
            return;
        }
        if (editCCV.getText().toString().isEmpty()) {
            Util.showToast(getActivity(), "Please enter the CCV number on the back of the card");
            return;
        }
        if (editAmount.getText().toString().isEmpty()) {
            Util.showToast(getActivity(), "Please enter the amount");
            return;
        }
        double money = Double.parseDouble(editAmount.getText().toString());

        if (money == 0.0) {
            Util.showToast(getActivity(), "Please enter the amount");
            return;
        }
        if (selectedMonth == 0) {
            Util.showToast(getActivity(),"Please select expiry month");
            return;
        }
        if (selectedYear == 0) {
            Util.showToast(getActivity(),"Please select expiry year");
            return;
        }
        if (selectedCard == null || selectedCard.isEmpty()) {
            showCardTypePopup();
            return;
        }

        if (selectedCard.equalsIgnoreCase("MasterCard")) {
            if (!CreditCardValidator.isCreditCardValid(editNumber.getText().toString(),
                    CreditCardValidator.MASTERCARD)) {
                Util.showErrorToast(getActivity(),"Credit Card number is not valid");
                return;
            }
        }
        if (selectedCard.equalsIgnoreCase("Visa")) {
            if (!CreditCardValidator.isCreditCardValid(editNumber.getText().toString(),
                    CreditCardValidator.VISA)) {
                Util.showErrorToast(getActivity(),"Credit Card number is not valid");
                return;
            }
        }
        PaymentRequestDTO req = new PaymentRequestDTO();
        if (isDebuggable) {
            req.setCardHolder(creditCard.getCardHolder());
            req.setCardNumber(creditCard.getCardNumber());
            req.setCCVV(creditCard.getCcv());
            req.setExpiryMonth(creditCard.getExpiryMonth());
            req.setExpiryYear(creditCard.getExpiryYear());

        } else {
            req.setCardHolder(editCardholder.getText().toString());
            req.setCardNumber(editNumber.getText().toString());
            req.setCCVV(editCCV.getText().toString());
            req.setExpiryMonth(selectedMonth);
            req.setExpiryYear(selectedYear);
        }
        req.setAmount(Double.parseDouble(editAmount.getText().toString()));
        Integer id = SharedUtil.getProfile(getActivity()).getProfileInfoID();
        req.setBuyerID("" + id.intValue());
        req.setReference("" + id.intValue() + "-" + System.currentTimeMillis());



        final RequestDTO w = new RequestDTO(RequestDTO.SEND_PAYMENT);
        w.setPaymentRequest(req);

        CreditCard creditCard = new CreditCard();
        creditCard.setCardHolder(editCardholder.getText().toString());
        creditCard.setCardNumber(editNumber.getText().toString());
        creditCard.setCcv(editCCV.getText().toString());
        creditCard.setExpiryMonth(selectedMonth);
        creditCard.setExpiryYear(selectedYear);
        creditCard.setBank(PaymentRequestDTO.FNB);

        SharedUtil.saveCreditCard(getActivity(), creditCard);

        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
        b.setTitle("Payment Confirmation")
                .setMessage("Please confirm that you want to submit a payment of "
                + df.format(req.getAmount()))
                .setPositiveButton("Confirm Payment", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.setBusy(true);
                        btnPay.setEnabled(false);
                        btnPay.setAlpha(0.3f);
                        NetUtil.sendRequest(getActivity(), w, new NetUtil.NetUtilListener() {
                            @Override
                            public void onResponse(final ResponseDTO response) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mListener.setBusy(false);
                                        FNBandNedbankResponse = response.getFNBandNedbankResponse();
                                        processFNBNedbankResponse();
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
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();





    }

    private void processFNBNedbankResponse() {
        if (FNBandNedbankResponse.getTransactionOutcome().equalsIgnoreCase("APPROVED")) {
            Util.showToast(getActivity(), "Your payment has been approved by your bank.\n\nThank You!");
            mListener.onPaymentSuccess();
            return;
        }
        if (FNBandNedbankResponse.getTransactionOutcome().equalsIgnoreCase("DECLINED")) {
            Util.showToast(getActivity(), "Your payment has been declined by your bank." +
                    "\nPlease get in touch with your bank");
            mListener.onPaymentFailed();
            return;
        }
        if (FNBandNedbankResponse.getTransactionOutcome().equalsIgnoreCase("ERROR")) {
            Util.showErrorToast(getActivity(),"Your transaction has been declined by your bank");
            return;
        }
        if (FNBandNedbankResponse.getTransactionOutcome().equalsIgnoreCase("ENROLLED")) {

            return;
        }
        if (FNBandNedbankResponse.getTransactionOutcome().equalsIgnoreCase("UNAVAILABLE")) {

            return;
        }
        if (FNBandNedbankResponse.getTransactionOutcome().equalsIgnoreCase("ATTEMPTED")) {

            return;
        }
        if (FNBandNedbankResponse.getTransactionOutcome().equalsIgnoreCase("STOP")) {

            return;
        }
        if (FNBandNedbankResponse.getTransactionOutcome().equalsIgnoreCase("REVIEW")) {

            return;
        }
        if (FNBandNedbankResponse.getTransactionOutcome().equalsIgnoreCase("DUPLICATE")) {

            return;
        }
    }

    public void showCardTypePopup() {
        final List<String> list = new ArrayList<>();
        list.add(ctx.getString(R.string.visa));
        list.add(ctx.getString(R.string.mastercard));
        list.add(ctx.getString(R.string.instant_eft));
        list.add(ctx.getString(R.string.ukash));


        final ListPopupWindow popupWindow = new ListPopupWindow(getActivity());

        View v = inflater.inflate(R.layout.hero_image_popup, null);
        TextView txt = (TextView) v.findViewById(R.id.HERO_caption);
        txt.setText("Payment Types");
        ImageView img = (ImageView) v.findViewById(R.id.HERO_image);
        img.setImageDrawable(Util.getRandomBackgroundImage(ctx));

        popupWindow.setPromptView(v);
        popupWindow.setPromptPosition(ListPopupWindow.POSITION_PROMPT_ABOVE);
        popupWindow.setAdapter(new CardTypePopupListAdapter(ctx,
                R.layout.xspinner_creditcard, list, primaryDarkColor));
        popupWindow.setAnchorView(handle);
        popupWindow.setHorizontalOffset(Util.getPopupHorizontalOffset(getActivity()));
        popupWindow.setModal(true);
        popupWindow.setWidth(Util.getPopupWidth(getActivity()));
        popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                popupWindow.dismiss();
                selectedCard = list.get(position);
                Util.setCardTypeIcon(selectedCard, icon, ctx);
                txtSelect.setText(selectedCard);

            }
        });
        popupWindow.show();
    }

    List<String> monthList, yearList;
    int selectedMonth,  selectedYear;
    private void setSpinners() {
        monthList = new ArrayList<>();
        monthList.add("January");
        monthList.add("February");
        monthList.add("March");
        monthList.add("April");
        monthList.add("May");
        monthList.add("June");
        monthList.add("July");
        monthList.add("August");
        monthList.add("September");
        monthList.add("October");
        monthList.add("November");
        monthList.add("December");

        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(
                getActivity(),android.R.layout.simple_spinner_item, monthList);
        monthSpinner.setAdapter(mAdapter);
        monthSpinner.setPrompt("Select Month");
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedMonth = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Calendar cal = GregorianCalendar.getInstance();
        int startYear = cal.get(Calendar.YEAR);
        yearList = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            yearList.add("" + (startYear + i));
        }
        ArrayAdapter<String> yAdapter = new ArrayAdapter<String>(
                getActivity(),android.R.layout.simple_spinner_item, yearList);
        yearSpinner.setAdapter(yAdapter);
        yearSpinner.setPrompt("Select Year");
        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedYear = Integer.parseInt("" + yearList.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface PaymentStartListener {
        void onPaymentSuccess();
        void setBusy(boolean busy);
        void onPaymentFailed();
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
