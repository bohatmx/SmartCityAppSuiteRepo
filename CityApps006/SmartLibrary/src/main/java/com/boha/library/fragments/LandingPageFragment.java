package com.boha.library.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.activities.CityApplication;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.SharedUtil;
import com.squareup.leakcanary.RefWatcher;

/**
 * Fragment manages FAQ UI. Downloads html FAQ files from the server
 * and displays them one at a time. Expects a ResponseDTO object
 * with a filled FAQType list in its arguments.
 */
public class LandingPageFragment extends Fragment implements PageFragment {

    public interface FaqListener {
        void setBusy(boolean busy);
    }

    ResponseDTO response;
    TextView txtFaqType;
    View view, fab, topView;
    Context ctx;
    ImageView image, iconLogin, iconNews, iconAlerts, iconFAQ;
    TextView txtTitle;
    Activity activity;
    String pageTitle;
    int primaryColor, darkColor, position;

    static final String LOG = LandingPageFragment.class.getSimpleName();

    public LandingPageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_landing_page, container, false);
        ctx = getActivity();
        activity = getActivity();
        setFields();

        //todo get your rss feeds

        return view;
    }


    private void setFields() {
        iconAlerts = (ImageView) view.findViewById(R.id.iconAlerts);
        iconAlerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               mListener.onAlertIconClicked();
            }
        });
        iconLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onLogin();
            }
        });

        if (SharedUtil.getProfile(getActivity()) != null) {
            iconLogin.setVisibility(View.GONE);
        }

    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof FaqListener) {
            mListener = (LandingPageListener) activity;
        } else {
            throw new ClassCastException("Activty " + activity.getLocalClassName() +
                    " must implement  LandingPageListener");
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

    @Override
    public void animateSomething() {
//        final Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        timer.cancel();
//                        heroImage.setImageDrawable(Util.getRandomBackgroundImage(ctx));
//                        Util.expand(heroImage, 1000, new Util.UtilAnimationListener() {
//                            @Override
//                            public void onAnimationEnded() {
//                                Util.flashOnce(fab, 300, null);
//                            }
//                        });
//                    }
//                });
//            }
//        }, 500);
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

    LandingPageListener mListener;
    public interface LandingPageListener {
        void onImageClicked();
        void onTitleClicked();
        void onAlertIconClicked();
        void onNewsIconClicked();
        void onFAQIconClicked();
        void onLogin();
    }
}
