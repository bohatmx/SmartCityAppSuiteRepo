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

public class FullAlertDetailActivity extends AppCompatActivity {

    ScrollView scrollView;
    TextView title_txt, articletxt;
    ImageView detailImg;
    WebView webView;
    Context ctx;

    String title;
    String description;
    String image;
    int position;
    MunicipalityDTO municipality;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_alert_detail);
        ctx = getApplicationContext();

        municipality = SharedUtil.getMunicipality(ctx);
        int logo = getIntent().getIntExtra("logo", R.drawable.ic_action_globe);

        ActionBar actionBar = getSupportActionBar();
        Util.setCustomActionBar(ctx,
                actionBar,
                municipality.getMunicipalityName(),
                ctx.getResources().getDrawable(R.drawable.logo), logo);
        getSupportActionBar().setTitle("");

        scrollView = (ScrollView) findViewById(R.id.ALERTDETAILSCROLL);
        title_txt = (TextView) findViewById(R.id.alert_details_title);
        detailImg = (ImageView) findViewById(R.id.alert_DETAILSIMG);
        webView = (WebView) findViewById(R.id.alert_details_webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
       /* webView.setWebViewClient(new WebViewClient());*/
        /*webView.getSettings().setDisplayZoomControls(true);

        webView.getSettings().setLoadWithOverviewMode(true);*/

        articletxt = (TextView) findViewById(R.id.testtxt);


        Intent intent = getIntent();
        title = intent.getStringExtra("newsTitle");
        description = intent.getStringExtra("newsArticle");
        image = intent.getStringExtra("newsImage");
        title_txt.setText(title);
        //  articletxt.setText(readRss.feedItems.get, TEXT, UTF);
        if (!image.isEmpty()) {
            Picasso.with(ctx).load(image).into(detailImg);


        }
        articletxt.setText(description);
        setWebView(position);
    }

    private static final String TEXT = "text/html", UTF = "UTF-8";

    private void setWebView(int position) {

        switch (position) {
            case 0:
                webView.loadData(articletxt.getText().toString(), TEXT, UTF);
                break;

        }

    }

    static final String LOG = FullDetailActivity.class.getSimpleName();
}
