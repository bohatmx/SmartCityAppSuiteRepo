package com.boha.library.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.activities.NewsMapActivity;
import com.boha.library.adapters.NewsListAdapter;
import com.boha.library.dto.NewsArticleDTO;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.CacheUtil;
import com.boha.library.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Fragment to house local pictures
 */
public class NewsListFragment extends Fragment implements PageFragment {

    private NewsListFragmentListener mListener;

    public static NewsListFragment newInstance(ResponseDTO response) {
        NewsListFragment fragment = new NewsListFragment();
        Bundle args = new Bundle();
        args.putSerializable("response",response);
        fragment.setArguments(args);
        return fragment;
    }

    public NewsListFragment() {
        // Required empty public constructor
    }

    ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ResponseDTO r = (ResponseDTO)getArguments().getSerializable("response");
            newsList = r.getNewsArticleList();
        }
    }

    View view, topView, border;
    ListView listView;
    Button btnCount;
    TextView  txtTitle, txtSubTitle;
    View fab;
    Context ctx;
    List<NewsArticleDTO> newsList;
    Location location;

    ImageView imgSearch;
    
    ImageView icon, heroImage;
    int logo;

    public void setLocation(Location location) {
        this.location = location;


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_news_list, container, false);
        topView = inflater.inflate(R.layout.news_top, null);
        ctx = getActivity();
        setFields();
        if (newsList != null) {
            setList();
        } else {
            newsList = new ArrayList<>();
            setList();
            getCachedNews();
        }

        return view;
    }

    private void setFields() {
        border = topView.findViewById(R.id.NEWS_LIST_top);
        border.setBackgroundColor(primaryColor);

        btnCount = (Button) view.findViewById(R.id.button);
        listView = (ListView) view.findViewById(R.id.NEWS_LIST_listView);
        icon = (ImageView)topView.findViewById(R.id.NEWS_LIST_icon);
        heroImage = (ImageView)topView.findViewById(R.id.NEWS_LIST_heroImage);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        txtTitle = (TextView) topView.findViewById(R.id.NEWS_LIST_title);
        txtSubTitle = (TextView) topView.findViewById(R.id.NEWS_LIST_subTitle);
        imgSearch = (ImageView) topView.findViewById(R.id.NEWS_LIST_refresh);
       
        txtTitle.setText(ctx.getResources().getText(R.string.city_news));
        txtSubTitle.setVisibility(View.INVISIBLE);

        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               
            }
        });
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        topView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //refreshAlerts();
            }
        });

        ctx = getActivity();
        btnCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(btnCount, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        Intent i = new Intent(ctx, NewsMapActivity.class);
                        ResponseDTO r = new ResponseDTO();
                        r.setNewsArticleList(newsList);
                        i.putExtra("newsArticleList", r);
                        startActivity(i);
                    }
                });

            }
        });
        animateSomething();
    }

    private void getCachedNews() {

        CacheUtil.getCacheLoginData(ctx, new CacheUtil.CacheRetrievalListener() {
            @Override
            public void onCacheRetrieved(ResponseDTO response) {
                newsList = response.getNewsArticleList();
                setList();
            }

            @Override
            public void onError() {

            }
        });
    }

    public void onNewNewsArticleSent(NewsArticleDTO newsArticle) {
        if (newsList == null) {
            newsList = new ArrayList<>();
        }
        newsList.add(0, newsArticle);
        if (newsListAdapter != null) {
            newsListAdapter.notifyDataSetChanged();
            btnCount.setText("" + newsList.size());
        }

        ResponseDTO r = new ResponseDTO();
        r.setNewsArticleList(newsList);
        CacheUtil.cacheNewsData(ctx, r, null);

    }
   

    NewsListAdapter newsListAdapter;
    private void setList() {
        if (newsList == null) return;

        btnCount.setText("" + newsList.size());
        newsListAdapter = new NewsListAdapter(ctx, R.layout.news_item, newsList, new NewsListAdapter.NewsListListener() {
            @Override
            public void onNewsClicked(int position) {
//                mListener.onNewsClicked(newsList.get(position));
                Intent i = new Intent(ctx, NewsMapActivity.class);
                ResponseDTO r = new ResponseDTO();
                r.setNewsArticleList(newsList);
                i.putExtra("newsArticle", newsList.get(position));
                startActivity(i);
            }
        });

        if (listView.getHeaderViewsCount() == 0) {
            listView.addHeaderView(topView);
        }
        listView.setAdapter(newsListAdapter);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (NewsListFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NewsListFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
                        heroImage.setImageDrawable(Util.getRandomCityImage(ctx));
                        Util.expand(heroImage, 1000, new Util.UtilAnimationListener() {
                            @Override
                            public void onAnimationEnded() {
                                Util.flashOnce(btnCount, 300, null);
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

    public interface NewsListFragmentListener {
        public void onNewsClicked(NewsArticleDTO news);
        public void onCreateNewsArticleRequested();
    }

    static final String LOG = NewsListFragment.class.getSimpleName();

    public void setLogo(int logo) {
        this.logo = logo;
    }
}
