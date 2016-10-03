package com.boha.library.fragments;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.dto.NewsArticleDTO;
import com.boha.library.jsonreader.NewsFeedItem;
import com.boha.library.jsonreader.NewsRead;
import com.boha.library.rssreader.ReadRss;
import com.boha.library.rssreader.ReadRssAdapter;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.CacheUtil;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ResponseDTO r = (ResponseDTO) getArguments().getSerializable("response");
     //       newsList = r.getNewsArticleList();
        }
    }

    View view, topView, emptyLayout;
    ListView listView;
    FloatingActionButton fab;
   public TextView txtTitle, txtEmpty;
    Context ctx;
    List<NewsArticleDTO> newsList;
    Location location;
    RecyclerView newsRecyclerView;

    ImageView heroImage;
    int logo;

    public void setLocation(Location location) {
        this.location = location;
    }

    NewsRead newsRead;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_news_list, container, false);

        //txtEmpty.setVisibility(View.GONE);
        ctx = getActivity();
        setFields();

       // readRss = new ReadRss(ctx, newsRecyclerView);
       // readRss.execute();
        newsRead = new NewsRead(ctx, newsRecyclerView);
        newsRead.execute();

      /*  if (newsList != null) {
            setList();
        } else {
            newsList = new ArrayList<>();
            setList();
            getCachedNews();
        } */

        return view;
    }

    ArrayList<NewsFeedItem> newsFeedItems;



    private void setFields() {
        txtEmpty = (TextView)view.findViewById(R.id.NEWS_LIST_text);
        txtEmpty.setVisibility(View.GONE);
        /*if (feedItems != null) {
            txtEmpty.setVisibility(View.GONE);
        } else {
            txtEmpty.setVisibility(View.VISIBLE);
        }*/
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        //listView = (ListView) view.findViewById(R.id.NEWS_LIST_listView);
        newsRecyclerView = (RecyclerView) view.findViewById(R.id.news_RecyclerView);
        LinearLayoutManager lm = new LinearLayoutManager(ctx,LinearLayoutManager.VERTICAL,false);
        newsRecyclerView.setLayoutManager(lm);


        /*if (readRss.feedItems.isEmpty()) {
            txtEmpty.setVisibility(View.VISIBLE);
        }*/
       // heroImage = (ImageView) view.findViewById(R.id.FNL_hero);
        ctx = getActivity();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             /*   if (newsList == null || newsList.isEmpty()) {
                    Snackbar.make(listView,ctx.getString(R.string.nonews_map),
                            Snackbar.LENGTH_LONG).show();
                    return;
                }
                int count = 0;
                for (NewsArticleDTO m: newsList) {
                    if (m.getLatitude() != null) {
                        count++;
                    }
                }
                if (count > 0) {
                    Intent i = new Intent(ctx, NewsMapActivity.class);
                    ResponseDTO r = new ResponseDTO();
                    r.setNewsArticleList(newsList);
                    i.putExtra("newsArticleList", r);
                    startActivity(i);
                } else {
                    Util.showSnackBar(listView,"No located news to display on map", "OK", Color.parseColor("ORANGE"));
                } */

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
       /* if (newsListAdapter != null) {
            newsListAdapter.notifyDataSetChanged();
        } */

        ResponseDTO r = new ResponseDTO();
        r.setNewsArticleList(newsList);
        CacheUtil.cacheNewsData(ctx, r, null);

    }


   /* NewsListAdapter newsListAdapter;

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
    } */
    ReadRssAdapter readRssAdapter;
    ReadRss readRss;
    private void setList(){
        readRssAdapter = new ReadRssAdapter(ctx, readRss.feedItems , new ReadRssAdapter.NewsListListener() {
            @Override
            public void onNewsClicked() {

            }
        });
        newsRecyclerView.setAdapter(readRssAdapter);


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
