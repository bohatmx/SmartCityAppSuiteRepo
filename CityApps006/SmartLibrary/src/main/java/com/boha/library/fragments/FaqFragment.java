package com.boha.library.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.activities.FAQFullDetailActivity;
import com.boha.library.adapters.FaqTypeAdapter;
import com.boha.library.dto.FreqQuestionTypeDTO;
import com.boha.library.rssreader.FaqAdapter;
import com.boha.library.rssreader.FaqTest;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.CacheUtil;
import com.boha.library.util.FAQCommsUtil;
import com.boha.library.util.FAQs;
import com.boha.library.util.FaqStrings;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.Util;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Fragment manages FAQ UI. Downloads html FAQ files from the server
 * and displays them one at a time. Expects a ResponseDTO object
 * with a filled FAQType list in its arguments.
 */
public class FaqFragment extends Fragment implements PageFragment {

    public interface FaqListener {
        void setBusy(boolean busy);
      //  void onFaqCLicked();
    }

    ResponseDTO response;
    TextView txtFaqType;
    View view, fab, topView;
    Context ctx;
    ImageView heroImage;
    Activity activity;
    FaqStrings faqStrings;
    String pageTitle;
    ProgressBar progressBar;
    int logo, primaryColor, darkColor, position;

    static final String LOG = FaqFragment.class.getSimpleName();

    public static FaqFragment newInstance(ResponseDTO response) {
        FaqFragment fragment = new FaqFragment();
        Bundle args = new Bundle();
        args.putSerializable("response", response);
        fragment.setArguments(args);
        return fragment;
    }

    public FaqFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
        if (getArguments() != null) {
            response = (ResponseDTO) getArguments().getSerializable("response");
//            faqTypeList = response.getFaqTypeList();
        }
    }
    String [] FAQ = {"Account Payments", "Water Sanitation", "Cleaning & Solid Waste",
            "Rates & Taxes", "Building Plans", "Electricity", "Social Services",
            "Health", "Metro Police"};
    String [] FAQ_NUMBER = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_faq_list, container, false);
        ctx = getActivity();
        activity = getActivity();
        setFields();




        animateSomething();
        /*if (faqTypeList != null) {
            setList();
        } else {
            faqTypeList = new ArrayList<>();
            setList();

        }
        getCachedFAQs();*/
        return view;
    }



    private static final String TEXT = "text/html", UTF = "UTF-8";

//    private void setWebView(int position) {
//
//        txtFaqType.setText(response.getFaqTypeList().get(position).getFaqTypeName());
//        switch (position) {
//            case 0:
//                webView.loadData(faqStrings.getAccountsFAQ(), TEXT, UTF);
//                icon.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.accounts_statement));
//                break;
//            case 1:
//                webView.loadData(faqStrings.getBuildingPlansFAQ(), TEXT, UTF);
//                icon.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.building_plans));
//                break;
//            case 2:
//                webView.loadData(faqStrings.getCleaningWasteFAQ(), TEXT, UTF);
//                icon.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.cleaning_solid_waste));
//                break;
//            case 3:
//                webView.loadData(faqStrings.getElectricityFAQ(), TEXT, UTF);
//                icon.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.electricity));
//                break;
//            case 4:
//                webView.loadData(faqStrings.getHealthFAQ(), TEXT, UTF);
//                icon.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.health));
//                break;
//            case 5:
//                webView.loadData(faqStrings.getMetroPoliceFAQ(), TEXT, UTF);
//                icon.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.metro_police));
//                break;
//            case 6:
//                webView.loadData(faqStrings.getRatesTaxesFAQ(), TEXT, UTF);
//                icon.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.rates_taxes));
//                break;
//            case 7:
//                webView.loadData(faqStrings.getSocialServicesFAQ(), TEXT, UTF);
//                icon.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.social_services));
//                break;
//            case 8:
//                webView.loadData(faqStrings.getWaterSanitationFAQ(), TEXT, UTF);
//                icon.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.water_sanitation));
//                break;
//        }
//
//    }


    private void getCachedFAQs() {
        mListener.setBusy(true);
        CacheUtil.getCachedFAQ(ctx, new CacheUtil.FAQCacheRetrievalListener() {
            @Override
            public void onCacheRetrieved(FaqStrings fs) {
                mListener.setBusy(false);
                if (fs.getAccountsFAQ() == null) {
                    getRemoteFAQs();
                    return;
                }
                faqStrings = fs;
            }

            @Override
            public void onError() {
                mListener.setBusy(false);
            }
        });
    }

    public void getRemoteFAQs() {
        mListener.setBusy(true);
        FAQCommsUtil.getFAQfiles(ctx,
                SharedUtil.getMunicipality(ctx).getMunicipalityID(),
                new FAQCommsUtil.FAQListener() {
                    @Override
                    public void onSuccess(FaqStrings fs) {
                        mListener.setBusy(false);
                        faqStrings = fs;
                    }

                    @Override
                    public void onError(String message) {
                        mListener.setBusy(false);
                        Util.showErrorToast(ctx, message);
                    }
                });

    }

    ListView FAQ_LIST;
    FaqTypeAdapter faqTypeAdapter;
    List<FreqQuestionTypeDTO> faqTypeList;
    ArrayList<FaqStrings> faqStringsList;


    private RecyclerView recyclerView;
    private void setList() {
        if (faqTypeList == null) {
            faqTypeList = new ArrayList<>();
        }
       /* faqTypeAdapter = new FaqTypeAdapter(ctx, R.layout.faqtype_item, darkColor, faqTypeList, new FaqTypeAdapter.FaqTypeListListener() {

            @Override
            public void onFaqTypeClicked(int position) {
                setAnalyticsEvent("faq", "FrequentlyAsked");
                Intent intent = new Intent(ctx, FaqTypeActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);

            }

        }); */
       // faqAdapter = new FaqAdapter(ctx, faqList);


     //   FAQ_LIST.setAdapter(faqAdapter/*faqTypeAdapter*/);

    }

    FaqAdapter faqAdapter;

    FirebaseAnalytics mFirebaseAnalytics;

    private void setAnalyticsEvent(String id, String name) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);

        if (mFirebaseAnalytics == null) {
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
        }
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        Log.w(LOG, "analytics event sent .....");


    }

    TextView txtTitle, Faq_text;
    ArrayList<FaqTest> faqList;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    private void setFields() {

      //  topView = view.findViewById(R.id.FAQ_handle);
       // txtFaqType = (TextView) view.findViewById(R.id.FAQ_faqType);
       // txtFaqType.setVisibility(View.GONE);
      //  icon = (ImageView) view.findViewById(R.id.FAQ_icon);
       // icon.setVisibility(View.GONE);
       // heroImage = (ImageView) view.findViewById(R.id.FAQ_hero);
      //  txtTitle = (TextView) view.findViewById(R.id.FAQ_title);
        Faq_text = (TextView) view.findViewById(R.id.NEWS_LIST_text);
        fab = view.findViewById(R.id.FAB);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        //FAQ_LIST = (ListView) view.findViewById(R.id.FAQ_LIST);
        recyclerView = (RecyclerView) view.findViewById(R.id.faqRecyclerView);
        LinearLayoutManager lm = new LinearLayoutManager(ctx,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(lm);

        faqAdapter = new FaqAdapter(ctx, FAQ, FAQ_NUMBER, new FaqAdapter.FaqListListener() {
            @Override
            public void onFaqClicked(int position) {
                setWebView(position);
            }
        });
        /*faqTypeAdapter = new FaqTypeAdapter(ctx, R.layout.faqtype_item, darkColor, faqTypeList, new FaqTypeAdapter.FaqTypeListListener() {
            @Override
            public void onFaqTypeClicked(int position) {
                Intent intent = new Intent(ctx, FaqTypeActivity.class);
                startActivity(intent);
            }
        });*/
        layoutManager = new LinearLayoutManager(ctx);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(faqAdapter);

        fab.setVisibility(View.GONE);
      /*  fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(fab, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                    //    showPopup();
                    }
                });
            }
        });
        txtFaqType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(txtFaqType, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        setWebView();
                        showPopup();
                        setIcon();
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
                           showPopup();
                    }
                });
            }
        });*/
        //  setIcon();
    }

    private void setWebView(int position) {

//        txtTitle.setText("Faq Title"/*response.getFaqTypeList().get(position).getFaqTypeName()*/);
        switch (position) {
            case 0:
                Intent intent = new Intent(ctx, FAQFullDetailActivity.class);
                intent.putExtra("newsTitle", "Account Payments");
                intent.putExtra("newsArticle", FAQs.getACCOUNTSPAYMENTS());
                ctx.startActivity(intent);
                break;
            case 1:
                Intent intent1 = new Intent(ctx, FAQFullDetailActivity.class);
                intent1.putExtra("newsTitle", "Water & Sanitation");
                intent1.putExtra("newsArticle", FAQs.getWATERSANITATION());
                ctx.startActivity(intent1);
                break;
            case 2:
                Intent intent2 = new Intent(ctx, FAQFullDetailActivity.class);
                intent2.putExtra("newsTitle", "Cleansing & Solid Waste");
                intent2.putExtra("newsArticle", FAQs.getCLEANINGSOLIDWASTE());
                ctx.startActivity(intent2);
                break;
            case 3:
                Intent intent3 = new Intent(ctx, FAQFullDetailActivity.class);
                intent3.putExtra("newsTitle", "Rates & Taxes");
                intent3.putExtra("newsArticle", FAQs.getRATESTAXES());
                ctx.startActivity(intent3);
                break;
            case 4:
                Intent intent4 = new Intent(ctx, FAQFullDetailActivity.class);
                intent4.putExtra("newsTitle", "Building Plans");
                intent4.putExtra("newsArticle", FAQs.getBUILDINGPLANS());
                ctx.startActivity(intent4);
                break;
            case 5:
                Intent intent5 = new Intent(ctx, FAQFullDetailActivity.class);
                intent5.putExtra("newsTitle", "Electricity");
                intent5.putExtra("newsArticle", FAQs.getELECTRICITY());
                ctx.startActivity(intent5);
                break;
            case 6:
                Intent intent6 = new Intent(ctx, FAQFullDetailActivity.class);
                intent6.putExtra("newsTitle", "Social Services");
                intent6.putExtra("newsArticle", FAQs.getSOCIALSERVICES());
                ctx.startActivity(intent6);
                break;
            case 7:
                Intent intent7 = new Intent(ctx, FAQFullDetailActivity.class);
                intent7.putExtra("newsTitle", "Health");
                intent7.putExtra("newsArticle", FAQs.getHEALTH());
                ctx.startActivity(intent7);
                break;
            case 8:
                Intent intent8 = new Intent(ctx, FAQFullDetailActivity.class);
                intent8.putExtra("newsTitle", "Metro Police");
                intent8.putExtra("newsArticle", FAQs.getMETROPOLICE());
                ctx.startActivity(intent8);
                break;
        }

    }

    FreqQuestionTypeDTO faqType;
    FaqListener mListener;
    ImageView icon;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof FaqListener) {
            mListener = (FaqListener) activity;
        } else {
            throw new ClassCastException("Activty " + activity.getLocalClassName() +
                    " must implement  FaqListener");
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
//        RefWatcher refWatcher = CityApplication.getRefWatcher(getActivity());
//        refWatcher.watch(this);
    }

    @Override
    public void animateSomething() {
    }

    @Override
    public void setThemeColors(int primaryColor, int primaryDarkColor) {
        this.primaryColor = primaryColor;
        this.darkColor = primaryDarkColor;
    }

    @Override
    public String getPageTitle() {
        return pageTitle;
    }

    @Override
    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public void setResponse(ResponseDTO response) {
        this.response = response;
    }
}
