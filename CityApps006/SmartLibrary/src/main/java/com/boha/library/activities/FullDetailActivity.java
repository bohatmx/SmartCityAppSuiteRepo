package com.boha.library.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.dto.MunicipalityDTO;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.Util;
import com.squareup.picasso.Picasso;

public class FullDetailActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_full_detail);
        ctx = getApplicationContext();

        municipality = SharedUtil.getMunicipality(ctx);
        int logo = getIntent().getIntExtra("logo", R.drawable.ic_action_globe);

        ActionBar actionBar = getSupportActionBar();
        Util.setCustomActionBar(ctx,
                actionBar,
                municipality.getMunicipalityName(),
                ctx.getResources().getDrawable(R.drawable.logo), logo);
        getSupportActionBar().setTitle("");

        scrollView = (ScrollView) findViewById(R.id.DETAILSCROLL);
        title_txt = (TextView) findViewById(R.id.details_title);
        detailImg = (ImageView) findViewById(R.id.DETAILSIMG);
        webView = (WebView) findViewById(R.id.details_webView);
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

        // title_txt.setText(readRss.feedItems.get(position).getTitle());
        // title_txt.setText(alertReadRss.feedItems.get(position).getTitle());
        //   title_txt.setText(title);

        switch (position) {
            case 0:
                webView.loadData(articletxt.getText().toString(), TEXT, UTF);
                //       webView.loadData(alertReadRss.feedItems.get(position).getDescription(), TEXT, UTF);
                break;

        }

    }
}
