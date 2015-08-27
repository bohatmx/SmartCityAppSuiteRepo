package com.boha.library.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.dto.NewsArticleDTO;
import com.boha.library.transfer.RequestDTO;
import com.boha.library.util.CommsUtil;
import com.boha.library.util.Util;

public class NewsDetailActivity extends AppCompatActivity {

    WebView webView;
    NewsArticleDTO newsArticle;
    TextView txtHeader;
    private static final String TEXT = "text/html", UTF = "UTF-8";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web);
        webView = (WebView)findViewById(R.id.webView);
        txtHeader = (TextView)findViewById(R.id.title);

        newsArticle = (NewsArticleDTO)getIntent().getSerializableExtra("newsArticle");
        txtHeader.setText(newsArticle.getNewsText());

       getDetailData();
    }

    void getDetailData() {
        setRefreshActionButtonState(true);
        CommsUtil.getData(this, newsArticle.getHref(),
                RequestDTO.GET_NEWS_DETAIL,
                new CommsUtil.CommsListener() {
            @Override
            public void onDataOK(final String data) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        setRefreshActionButtonState(false);
                        webView.loadData(data, TEXT, UTF);
                    }
                });

            }

            @Override
            public void noDataFound() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setRefreshActionButtonState(false);
                        Util.showToast(getApplicationContext(), "News article details have not been found");
                        finish();
                    }
                });
            }

            @Override
            public void onError(final String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setRefreshActionButtonState(false);
                        Util.showToast(getApplicationContext(), message);
                        finish();
                    }
                });
            }
        });
    }
    Menu mMenu;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news_detail, menu);
        mMenu = menu;
        if (newsArticle.getLatitude() == null) {
            menu.getItem(0).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_map) {
            Intent w = new Intent(this, NewsMapActivity.class);
            w.putExtra("newsArticle",newsArticle);
            startActivity(w);
            return true;
        }
        if (id == R.id.action_refresh) {
            getDetailData();
            return true;
        }
        if (id == R.id.action_help) {
            Util.showToast(getApplicationContext(),getString(R.string.under_cons));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onPause() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        super.onPause();
    }
    private void setRefreshActionButtonState(final boolean refreshing) {
        if (mMenu != null) {
            final MenuItem refreshItem = mMenu.findItem(R.id.action_refresh);
            if (refreshItem != null) {
                if (refreshing) {
                    refreshItem.setActionView(R.layout.action_bar_progess);
                } else {
                    refreshItem.setActionView(null);
                }
            }
        }
    }


}
