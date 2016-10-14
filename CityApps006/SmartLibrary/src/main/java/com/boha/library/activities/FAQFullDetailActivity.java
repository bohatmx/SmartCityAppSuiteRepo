package com.boha.library.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.dto.MunicipalityDTO;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.Util;
import com.squareup.picasso.Picasso;

public class FAQFullDetailActivity extends AppCompatActivity {

    ScrollView scrollView;
    TextView title_txt, content_txt;
    WebView webView;
    Context ctx;

    String title;
    String description;
    int position;
    MunicipalityDTO municipality;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faqfull_detail);
        ctx = getApplicationContext();

        municipality = SharedUtil.getMunicipality(ctx);
        int logo = getIntent().getIntExtra("logo", R.drawable.ic_action_globe);

        ActionBar actionBar = getSupportActionBar();
        Util.setCustomActionBar(ctx,
                actionBar,
                municipality.getMunicipalityName(),
                ctx.getResources().getDrawable(R.drawable.logo), logo);
        getSupportActionBar().setTitle("");

        scrollView = (ScrollView) findViewById(R.id.FAQ_DETAILSCROLL);
        title_txt = (TextView) findViewById(R.id.faq_title);
        webView = (WebView) findViewById(R.id.faq_details_webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setDisplayZoomControls(true);
        webView.getSettings().setLoadWithOverviewMode(true);

        content_txt = (TextView) findViewById(R.id.faq_txt);


        Intent intent = getIntent();
        title = intent.getStringExtra("newsTitle");
        description = intent.getStringExtra("newsArticle");
        title_txt.setText(title);
        content_txt.setText(description);
        setWebView(position);
    }

    private static final String TEXT = "text/html", UTF = "UTF-8";

    private void setWebView(int position) {

        // title_txt.setText(readRss.feedItems.get(position).getTitle());
        // title_txt.setText(alertReadRss.feedItems.get(position).getTitle());
        //   title_txt.setText(title);

        switch (position) {
            case 0:
                webView.loadUrl(description);/*loadData(description, TEXT, UTF);*/


                //  webView.loadDataWithBaseURL("http://www.youtube.com", TEXT, UTF, "utf-8", null);
                //       webView.loadData(alertReadRss.feedItems.get(position).getDescription(), TEXT, UTF);
                break;

        }

    }
}
