package com.boha.library.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.rssreader.AlertReadRss;
import com.boha.library.rssreader.LandingPageReadRss;
import com.boha.library.rssreader.ReadRss;
import com.boha.library.rssreader.ReadRssAdapter;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.SharedUtil;

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
    TextView txtFaqType, txtCaption;
    View view, fab, topView;
    Context ctx;
    ImageView image, iconLogin, iconNews, iconAlerts, iconFAQ, iconContact;
    Button btnFaq, btnNews, btnSignIn, btnAlerts;
    TextView txtTitle;
    Activity activity;
    String pageTitle;
    int primaryColor, darkColor, position;
    RecyclerView recyclerView;

    static final String LOG = LandingPageFragment.class.getSimpleName();

    public LandingPageFragment() {
    }

    public static LandingPageFragment newInstance(ResponseDTO response) {
        LandingPageFragment fragment = new LandingPageFragment();
        Bundle args = new Bundle();
        args.putSerializable("response", response);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    LandingPageReadRss landingPageReadRss;

    ImageView imageView;
    TextView txtHeadlines;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_landing_page, container, false);
        ctx = getActivity();
        activity = getActivity();
        setFields();

        //todo get your rss feeds
     //   landingPageReadRss = new LandingPageReadRss(ctx, recyclerView);
     //   landingPageReadRss.execute();

        alertReadRss = new AlertReadRss(ctx, recyclerView);
        alertReadRss.execute();

     //   setList();

        return view;
    }
    ReadRssAdapter readRssAdapter;
    ReadRss readRss;
    AlertReadRss alertReadRss;



    private void setFields() {
        iconAlerts = (ImageView) view.findViewById(R.id.iconAlerts);
        iconContact = (ImageView) view.findViewById(R.id.iconContact);
       /* btnAlerts = (Button) view.findViewById(R.id.flp_btnAlerts);
        btnFaq = (Button) view.findViewById(R.id.flp_btnfaq);
        btnSignIn = (Button) view.findViewById(R.id.flp_btnSignin);
      *///  imageView = (ImageView) view.findViewById(R.id.LP_image);
       // txtHeadlines = (TextView) view.findViewById(R.id.latest_news_title);
       // image = (ImageView) view.findViewById(R.id.image);
        txtTitle = (TextView) view.findViewById(R.id.title);
        txtTitle.setVisibility(View.GONE);
      //  btnNews = (Button) view.findViewById(R.id.flp_btnNews);
        txtCaption = (TextView) view.findViewById(R.id.SPLASH_caption);
      /*  image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onImageClicked();
            }
        });*/
       /* txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onTitleClicked();
            }
        });
        btnAlerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onAlertIconClicked();
            }
        });
        btnFaq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onFAQIconClicked();
            }
        });
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onLogin();
            }
        });
        btnNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onNewsIconClicked();
            }
        });
        *///image.setImageDrawable(ContextCompat.getDrawable(ctx, R.class.ReadRss.image));



      //  txtTitle.setText(readRss.title.getText().toString());

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        LinearLayoutManager lm = new LinearLayoutManager(ctx,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(lm);

        iconNews = (ImageView) view.findViewById(R.id.iconNews);

        iconFAQ = (ImageView) view.findViewById(R.id.iconFAQ);


        iconLogin = (ImageView) view.findViewById(R.id.iconLogin);
        iconContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onContactClicked();
            }
        });
        iconAlerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               mListener.onAlertIconClicked();
            }
        });

        iconNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onNewsIconClicked();
            }
        });
        iconFAQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onFAQIconClicked();
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
/*
    private void setList(){
//        txtTitle.setText(landingPageReadRss.feedItems.get(0).getTitle());
        readRssAdapter = new ReadRssAdapter(ctx, readRss.feedItems , new ReadRssAdapter.NewsListListener() {
            @Override
            public void onNewsClicked(int position) {

            }
        });
        recyclerView.setAdapter(readRssAdapter);

    } */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof LandingPageListener) {
            mListener = (LandingPageListener) activity;
        } else {
            throw new ClassCastException("Activity " + activity.getLocalClassName() +
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
//        RefWatcher refWatcher = CityApplication.getRefWatcher(getActivity());
//        refWatcher.watch(this);
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
        void onContactClicked();
    }

    int logo;
    public void setLogo(int logo) {
        this.logo = logo;
    }
}
