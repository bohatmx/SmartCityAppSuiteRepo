package com.boha.library.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.activities.CityApplication;
import com.boha.library.activities.NewsDetailActivity;
import com.boha.library.activities.NewsMapActivity;
import com.boha.library.adapters.NewsListAdapter;
import com.boha.library.dto.NewsArticleDTO;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.CacheUtil;
import com.boha.library.util.Statics;
import com.boha.library.util.Util;
import com.squareup.leakcanary.RefWatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment to house local pictures
 */
public class NewsListFragment extends Fragment implements PageFragment {

    private NewsListFragmentListener mListener;

    public static NewsListFragment newInstance(ResponseDTO response) {
        NewsListFragment fragment = new NewsListFragment();
        Bundle args = new Bundle();
        args.putSerializable("response", response);
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
            ResponseDTO r = (ResponseDTO) getArguments().getSerializable("response");
            newsList = r.getNewsArticleList();
        }
    }

    View view, topView, emptyLayout;
    ListView listView;
    FloatingActionButton fab;
    TextView txtTitle, txtEmpty;
    Context ctx;
    List<NewsArticleDTO> newsList;
    Location location;

    ImageView heroImage;
    int logo;

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_news_list, container, false);
        //topView = inflater.inflate(R.layout.news_top, null);
        txtEmpty = (TextView)view.findViewById(R.id.NEWS_LIST_text);
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

        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        listView = (ListView) view.findViewById(R.id.NEWS_LIST_listView);
        heroImage = (ImageView) view.findViewById(R.id.FNL_hero);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
       // txtTitle = (TextView) topView.findViewById(R.id.NEWS_LIST_title);
       // txtTitle.setText(ctx.getResources().getText(R.string.city_news));

       /* topView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //refreshAlerts();
            }
        });*/

        ctx = getActivity();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newsList == null || newsList.isEmpty()) {
                    Snackbar.make(listView,ctx.getString(R.string.nonews_map),
                            Snackbar.LENGTH_LONG).show();
                    return;
                }
                Intent i = new Intent(ctx, NewsMapActivity.class);
                ResponseDTO r = new ResponseDTO();
                r.setNewsArticleList(newsList);
                i.putExtra("newsArticleList", r);
                startActivity(i);

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
        }

        ResponseDTO r = new ResponseDTO();
        r.setNewsArticleList(newsList);
        CacheUtil.cacheNewsData(ctx, r, null);

    }


    NewsListAdapter newsListAdapter;

    private void setList() {
        if (newsList == null) {
           newsList = new ArrayList<>();
        }
        newsListAdapter = new NewsListAdapter(ctx, R.layout.news_item, newsList, new NewsListAdapter.NewsListListener() {
            @Override
            public void onNewsClicked(final int position) {
                Intent w = new Intent(getActivity(), NewsDetailActivity.class);
                w.putExtra("newsArticle", newsList.get(position));
                startActivity(w);

            }
        });
        if (newsList.isEmpty()) {
            txtEmpty.setVisibility(View.VISIBLE);
        } else {
            txtEmpty.setVisibility(View.GONE);
        }
        Statics.setRobotoFontLight(ctx,txtEmpty);
        if (listView.getHeaderViewsCount() == 0) {
            heroImage.setImageDrawable(Util.getRandomBackgroundImage(ctx));
        //    recyclerView.addHeaderView(topView);
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

    public interface NewsListFragmentListener {
        void onNewsClicked(NewsArticleDTO news);

        void onCreateNewsArticleRequested();

        void setBusy(boolean busy);
    }

    static final String LOG = NewsListFragment.class.getSimpleName();

    public void setLogo(int logo) {
        this.logo = logo;
    }
}
