package com.boha.citizenapp.ethekwini.rssreader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.boha.citizenapp.ethekwini.R;

public class FullArticleViewActivity extends AppCompatActivity {

    TextView title_txt;
    WebView webView;
    ReadRss readRss;
    NewReadRss newReadRss;
    int position;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title_txt = (TextView) findViewById(R.id.news_title);
        webView = (WebView) findViewById(R.id.NEWS_webView);
       /* recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        ReadRss readRss = new ReadRss(this, recyclerView);
        readRss.execute();
        */
        // readRss.feedItems.get(index);

        setWebView(position);


    }



    private static final String TEXT = "text/html", UTF = "UTF-8";

    private void setWebView(int position) {

        title_txt.setText(readRss.feedItems.get(position).getTitle());
       // title_txt.setText(newReadRss.feedItems.get(position).getTitle());
        switch (position) {
            case 0:
                webView.loadData(readRss.feedItems.get(position).getDescription(), TEXT, UTF);
         //       webView.loadData(newReadRss.feedItems.get(position).getDescription(), TEXT, UTF);
                break;

        }

    }

}
