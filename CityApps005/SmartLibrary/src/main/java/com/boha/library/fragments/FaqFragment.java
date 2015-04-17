package com.boha.library.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.dto.FaqTypeDTO;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.CacheUtil;
import com.boha.library.util.CommsException;
import com.boha.library.util.FAQCommsUtil;
import com.boha.library.util.FaqStrings;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class FaqFragment extends Fragment implements PageFragment{

    ResponseDTO response;
    TextView txtTitle, txtFaqType;
    View view, fab, topView;
    WebView webView;
    Context ctx;
    ImageView heroImage;
    Activity activity;
    FaqStrings faqStrings;
    String pageTitle;
    int logo, primaryColor, darkColor;

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
        if (getArguments() != null) {
            response = (ResponseDTO) getArguments().getSerializable("response");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_faq_list, container, false);
        ctx = getActivity();
        activity = getActivity();
        setFields();
        getFaqTypes();
        animateSomething();


        return view;
    }

    private static final String TEXT = "text/html", UTF = "UTF-8";
    private void setWebView(int position) {

        txtFaqType.setText(response.getFaqTypeList().get(position).getFaqTypeName());
        switch (position) {
            case 0:
                webView.loadData(faqStrings.getAccountsFAQ(), TEXT, UTF);
                break;
            case 1:
                webView.loadData(faqStrings.getBuildingPlansFAQ(), TEXT, UTF);
                break;
            case 2:
                webView.loadData(faqStrings.getCleaningWasteFAQ(), TEXT, UTF);
                break;
            case 3:
                webView.loadData(faqStrings.getElectricityFAQ(), TEXT, UTF);
                break;
            case 4:
                webView.loadData(faqStrings.getHealthFAQ(), TEXT, UTF);
                break;
            case 5:
                webView.loadData(faqStrings.getMetroPoliceFAQ(), TEXT, UTF);
                break;
            case 6:
                webView.loadData(faqStrings.getRatesTaxesFAQ(), TEXT, UTF);
                break;
            case 7:
                webView.loadData(faqStrings.getSocialServicesFAQ(), TEXT, UTF);
                break;
            case 8:
                webView.loadData(faqStrings.getWaterSanitationFAQ(), TEXT, UTF);
                break;
        }

    }
    private void getCachedFAQs() {
        CacheUtil.getCachedFAQ(ctx, new CacheUtil.FAQCacheRetrievalListener() {
            @Override
            public void onCacheRetrieved(FaqStrings fs) {
                if (fs.getAccountsFAQ() == null) {
                    getRemoteFAQs();
                    return;
                }
                faqStrings = fs;
                setWebView(0);
            }

            @Override
            public void onError() {

            }
        });
    }
    private void getRemoteFAQs() {
        try {
            FAQCommsUtil.getFAQfiles(ctx, SharedUtil.getMunicipality(ctx).getMunicipalityID(), new FAQCommsUtil.FAQListener() {
                @Override
                public void onSuccess(FaqStrings fs) throws CommsException {
                    faqStrings = fs;
                    setWebView(0);
                }

                @Override
                public void onError(String message) {
                    Util.showErrorToast(ctx,message);
                }
            });
        } catch (CommsException e) {
            e.printStackTrace();
        }
    }
    private void setFields() {
        topView = view.findViewById(R.id.FAQ_handle);
        txtFaqType = (TextView) view.findViewById(R.id.FAQ_faqType);
        heroImage = (ImageView)view.findViewById(R.id.FAQ_hero);
        txtTitle = (TextView) view.findViewById(R.id.FAQ_title);
        fab = view.findViewById(R.id.FAB);
        webView = (WebView)view.findViewById(R.id.FAQ_webView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(fab, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        showPopup();
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
                        showPopup();
                    }
                });
            }
        });
    }
    private void getFaqTypes() {
        CacheUtil.getCacheLoginData(ctx, new CacheUtil.CacheRetrievalListener() {
            @Override
            public void onCacheRetrieved(ResponseDTO r) {
                response = r;
                getCachedFAQs();
            }

            @Override
            public void onError() {

            }
        });
    }
    private void showPopup() {
        List<String> list = new ArrayList<>();
        for (FaqTypeDTO s: response.getFaqTypeList()) {
            list.add(s.getFaqTypeName());
        }
        Util.showPopupBasicWithHeroImage(ctx, activity, list, topView, "Question Types", new Util.UtilPopupListener() {
            @Override
            public void onItemSelected(int index) {
                faqType = response.getFaqTypeList().get(index);
                txtFaqType.setText(faqType.getFaqTypeName());
                setWebView(index);

            }
        });
    }
    FaqTypeDTO faqType;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

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
                        heroImage.setImageDrawable(Util.getRandomBackgroundImage(ctx));
                        Util.expand(heroImage, 1000, new Util.UtilAnimationListener() {
                            @Override
                            public void onAnimationEnded() {
                                Util.flashOnce(fab, 300, null);
                            }
                        });
                    }
                });
            }
        }, 500);
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
