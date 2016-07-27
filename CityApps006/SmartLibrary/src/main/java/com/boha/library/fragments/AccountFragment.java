package com.boha.library.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.activities.CityApplication;
import com.boha.library.activities.PaymentStartActivity;
import com.boha.library.activities.SIDPaymentsActivity;
import com.boha.library.activities.StatementActivity;
import com.boha.library.adapters.AccountAdapter;
import com.boha.library.adapters.AccountPopupListAdapter;
import com.boha.library.dto.AccountDTO;
import com.boha.library.dto.PaymentSurveyDTO;
import com.boha.library.dto.ProfileInfoDTO;
import com.boha.library.transfer.RequestDTO;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.CacheUtil;
import com.boha.library.util.NetUtil;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.Statics;
import com.boha.library.util.Util;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.squareup.leakcanary.RefWatcher;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class AccountFragment extends Fragment implements PageFragment {

    private AccountFragmentListener mListener;
    private ProfileInfoDTO profileInfo;
    private View view, detailView, topView, handle, npView;
    private TextView
            txtName, txtAcctNumber, txtSubtitle,
            txtArrears, txtFAB, txtClickToPay,
            txtLastUpdate, txtNextBill,
            txtAddress, txtLastBillAmount, ACCFlipText;
    View topLayout, npLayout;
    FloatingActionButton fab;
    Button btnCurrBal;
    ImageView fabIcon, hero, previousIMG, nextIMG;
    ScrollView scrollView;
    AccountAdapter adapter;
    ImageView icon;
    int logo;
    Context ctx;
    Activity activity;
    AccountDTO account;
    int selectedIndex;

    FragmentManager fragmentManager;
    static final DecimalFormat df = new DecimalFormat("###,###,###,###,###,###,##0.00");
    static final String LOG = AccountFragment.class.getSimpleName();
    static final Locale loc = Locale.getDefault();
    static final SimpleDateFormat sdfDate = new SimpleDateFormat("EEE dd MMM yyyy", loc);
    static final SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm", loc);
    private static final String PDF_MIME_TYPE = "application/pdf";
    private static final String HTML_MIME_TYPE = "text/html";

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
        //change topVIEW TO MATCH APP THEME
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
        Log.e(LOG, "### onCreateView");
        view = inflater.inflate(R.layout.fragment_account, container, false);
        fragmentManager = getFragmentManager();
        activity = getActivity();
        ctx = getActivity();
        setFields();
        getAccounts();

        if (profileInfo != null) {
            if (!profileInfo.getAccountList().isEmpty()) {
                nextIMG.setVisibility(View.GONE);
                previousIMG.setVisibility(View.GONE);
                ACCFlipText.setVisibility(View.GONE);
                account = profileInfo.getAccountList().get(0);
                setAccountFields(account);

            }

        }
        //Track AccountFragment
        CityApplication ca = (CityApplication) getActivity().getApplication();
        Tracker t = ca.getTracker(
                CityApplication.TrackerName.APP_TRACKER);
        t.setScreenName(AccountFragment.class.getSimpleName());
        t.send(new HitBuilders.ScreenViewBuilder().build());
        //
        //LocaleUtil.setLocale(ctx, LocaleUtil.ENGLISH);
        return view;
    }

    ListPopupWindow accountPopup;
    List<AccountDTO> accountList;

    public void showAccountPopup(final List<AccountDTO> list) {
        if (accountPopup != null) {
            accountPopup.dismiss();
        }

        accountPopup = new ListPopupWindow(getActivity());
        LayoutInflater inf = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inf.inflate(R.layout.hero_image_popup, null);
        TextView txt = (TextView) v.findViewById(R.id.HERO_caption);
        txt.setText("My Accounts");
        ImageView img = (ImageView) v.findViewById(R.id.HERO_image);
        img.setImageDrawable(Util.getRandomBackgroundImage(ctx));

        accountPopup.setPromptView(v);
        accountPopup.setPromptPosition(ListPopupWindow.POSITION_PROMPT_ABOVE);
        accountPopup.setAdapter(new AccountPopupListAdapter(ctx,
                R.layout.xspinner_item, list, primaryDarkColor));
        accountPopup.setAnchorView(view);
        accountPopup.setHorizontalOffset(Util.getPopupHorizontalOffset(getActivity()));
        accountPopup.setModal(true);
        accountPopup.setWidth(Util.getPopupWidth(getActivity()));
        accountPopup.setHeight(Util.getWindowHeight(getActivity()));
        accountPopup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                accountPopup.dismiss();
                account = list.get(position);
                Util.setComplaintCategoryIcon(account.getAccountNumber(), nextIMG, getActivity());
            }
        });
        accountPopup.show();
    }

    private void getAccounts() {
        CacheUtil.getCacheLoginData(ctx, new CacheUtil.CacheRetrievalListener() {
            @Override
            public void onCacheRetrieved(ResponseDTO response) {

                if (response.getAccountList() != null) {
                    accountList = response.getAccountList();
                }

            }

            @Override
            public void onError() {

            }
        });
    }

    int next = 0;
    private void setNextAccount() {

        next++;
        if(next == profileInfo.getAccountList().size()) {
            nextIMG.setVisibility(View.GONE);
            } else {
            for(AccountDTO acc : profileInfo.getAccountList()) {
                profileInfo.getAccountList().get(next);
                setAccountFields(acc);
            }
        }


    }

    int previous = 0;
    private void setPreviousAccount() {

        previous--;
        if(previous < 0) {
            previousIMG.setVisibility(View.GONE);
        } else {
            for(AccountDTO acc : profileInfo.getAccountList()) {
            profileInfo.getAccountList().get(previous);
            setAccountFields(acc);
        }}


    }

    int index;
    public void setProfileInfo(final ProfileInfoDTO profileInfo) {
        this.profileInfo = profileInfo;

        index++;
        if (profileInfo.getAccountList().isEmpty()) {
            Log.e("AccountFragment", "--- account list is empty");
            return;
        }

        account = profileInfo.getAccountList().get(0);
        setAccountFields(account);
        txtFAB.setText("1");

      /*  nextIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account = profileInfo.getAccountList().get(index);
                setAccountFields(account);
                txtFAB.setText("2");
            }
        });
        account = profileInfo.getAccountList().get(1);
        setAccountFields(account);
        txtFAB.setText("2");

        account = profileInfo.getAccountList().get(2);
        setAccountFields(account);
        txtFAB.setText("3");

        account = profileInfo.getAccountList().get(3);
        setAccountFields(account);
        txtFAB.setText("4");

        account = profileInfo.getAccountList().get(4);
        setAccountFields(account);
        txtFAB.setText("5");

        account = profileInfo.getAccountList().get(5);
        setAccountFields(account);
        txtFAB.setText("6");

        account = profileInfo.getAccountList().get(6);
        setAccountFields(account);
        txtFAB.setText("7");

        account = profileInfo.getAccountList().get(7);
        setAccountFields(account);
        txtFAB.setText("8");

        account = profileInfo.getAccountList().get(8);
        setAccountFields(account);
        txtFAB.setText("9");

        account = profileInfo.getAccountList().get(9);
        setAccountFields(account);
        txtFAB.setText("10"); */

    }

    private void startPayment() {
        Log.d(LOG,"########## startPayment ....");


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
        topView = view.findViewById(R.id.template);
        npView = view.findViewById(R.id.npLay);
       // npView = view.findViewById(R.id.NP);
      //  npView = view.findViewById(R.id.NP);
        fab = (FloatingActionButton)view.findViewById(R.id.fab);
        hero = (ImageView) view.findViewById(R.id.TOP_heroImage);
        topLayout = view.findViewById(R.id.TOP_titleLayout);
        topLayout.setBackgroundColor(primaryColor);
        handle = view.findViewById(R.id.ACCT_handle);
        scrollView = (ScrollView) view.findViewById(R.id.ACCT_scroll);
        detailView = view.findViewById(R.id.ACCT_detailLayout);
        txtName = (TextView) topView.findViewById(R.id.TOP_title);
        txtClickToPay = (TextView) topView.findViewById(R.id.ACCT_clickToPay);
        Statics.setRobotoFontLight(ctx, txtName);
        txtSubtitle = (TextView) topView.findViewById(R.id.TOP_subTitle);
        icon = (ImageView) topView.findViewById(R.id.TOP_icon);
//        txtFAB = (TextView) topView.findViewById(R.id.FAB_text);
//        fabIcon = (ImageView) topView.findViewById(R.id.FAB_icon);

        txtAcctNumber = (TextView) view.findViewById(R.id.ACCT_number);
        txtAddress = (TextView) view.findViewById(R.id.ACCT_address);
        txtArrears = (TextView) view.findViewById(R.id.ACCT_currArrears);
        txtLastUpdate = (TextView) view.findViewById(R.id.ACCT_lastUpdateDate);
        btnCurrBal = (Button) view.findViewById(R.id.button);
        txtNextBill = (TextView) view.findViewById(R.id.ACCT_nextBillDate);
        txtLastBillAmount = (TextView) view.findViewById(R.id.ACCT_lastBillAmount);

      //  detailView = view.findViewById(R.id.ACCS);
        previousIMG = (ImageView) detailView.findViewById(R.id.previousIMG);
        nextIMG = (ImageView) detailView.findViewById(R.id.nextIMG);
        ACCFlipText = (TextView) detailView.findViewById(R.id.ACCFlipText);

        ACCFlipText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(LOG, "text clicked");
                showAccountPopup(profileInfo.getAccountList());
            }
        });

        nextIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(LOG, "nextIMG btn clicked");
              //  setProfileInfo(profileInfo);
                setNextAccount();
            }
        });
        previousIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(LOG, "previousIMG btn clicked");
                setPreviousAccount();
            }
        });

//        fabIcon.setVisibility(View.GONE);
//        txtFAB.setVisibility(View.VISIBLE);
        hero.setImageDrawable(Util.getRandomBackgroundImage(ctx));

        setFont();
        txtClickToPay.setVisibility(View.GONE);
        btnCurrBal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(btnCurrBal, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        startPayment();
                    }
                });
            }
        });
     /*   txtClickToPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(btnCurrBal, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                    //    startPayment();
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
        }); */
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
       // fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent w = new Intent(ctx, SIDPaymentsActivity.class);
                w.putExtra("primaryColor", primaryColor);
                w.putExtra("darkColor", primaryDarkColor);
                w.putExtra("logo", logo);
                startActivity(w);
            }
        });

        animateSomething();
    }

    private void setFont() {
        Statics.setRobotoFontLight(ctx, txtAcctNumber);
        Statics.setRobotoFontLight(ctx, txtArrears);
        Statics.setRobotoFontLight(ctx, btnCurrBal);
        Statics.setRobotoFontLight(ctx, txtLastBillAmount);
        Statics.setRobotoFontLight(ctx, txtNextBill);
        Statics.setRobotoFontLight(ctx, txtLastUpdate);


    }

    int year, month;
    static final Locale LOCALE = Locale.getDefault();
    static final SimpleDateFormat sd = new SimpleDateFormat("EEEE dd MMMM yyyy", LOCALE);

    private void setAccountFields(AccountDTO account) {
        String currency = "R";
        txtAcctNumber.setText(account.getAccountNumber());
        txtAddress.setText(account.getPropertyAddress());

        txtArrears.setText(currency + df.format(account.getCurrentArrears()));
        btnCurrBal.setText(currency + df.format(account.getCurrentBalance()));
        txtLastBillAmount.setText(currency + df.format(account.getLastBillAmount()));

        txtLastUpdate.setText(sd.format(account.getDateLastUpdated()));
        txtName.setText(account.getCustomerAccountName());
        txtNextBill.setText(sd.format(account.getNextBillDate()));
        txtSubtitle.setText(ctx.getString(R.string.last_update) + sd.format(account.getDateLastUpdated()));


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
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            timer.cancel();
                            hero.setImageDrawable(Util.getRandomBackgroundImage(ctx));
                            Util.expand(hero, 1000, new Util.UtilAnimationListener() {
                                @Override
                                public void onAnimationEnded() {
                                    Util.flashSeveralTimes(btnCurrBal, 30, 3, null);
                                }
                            });
                        }
                    });
                }
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
