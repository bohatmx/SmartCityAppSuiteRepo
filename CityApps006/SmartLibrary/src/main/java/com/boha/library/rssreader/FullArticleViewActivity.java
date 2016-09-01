package com.boha.library.rssreader;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import com.boha.library.R;
import com.boha.library.dto.MunicipalityDTO;
import com.boha.library.fragments.LandingPageFragment;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.Util;
import com.squareup.picasso.Picasso;


public class FullArticleViewActivity extends AppCompatActivity {

    TextView title_txt, articletxt;
    ImageView newsimg;
    WebView webView;
    ReadRss readRss;
    AlertReadRss alertReadRss;
    int position;
    LandingPageReadRss landingPageReadRss;
    RecyclerView recyclerView;
    Context ctx;
    String title;
    String description;
    String image;
    ScrollView newsScroll;
    MunicipalityDTO municipality;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ctx = getApplicationContext();

        municipality = SharedUtil.getMunicipality(ctx);
        int logo = getIntent().getIntExtra("logo", R.drawable.ic_action_globe);

        ActionBar actionBar = getSupportActionBar();
        Util.setCustomActionBar(ctx,
                actionBar,
                municipality.getMunicipalityName(),
                ctx.getResources().getDrawable(R.drawable.logo), logo);
        getSupportActionBar().setTitle("");

        title_txt = (TextView) findViewById(R.id.news_title);
        articletxt = (TextView) findViewById(R.id.testtxt);
        newsimg = (ImageView) findViewById(R.id.NEWSIMG);
        newsScroll = (ScrollView) findViewById(R.id.NEWSSCROLL);
        webView = (WebView) findViewById(R.id.NEWS_webView);
     //   recyclerView = (RecyclerView) findViewById(R.id.FAVRecyclerView);
      //  LinearLayoutManager lm = new LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL,false);
       // recyclerView.setLayoutManager(lm);

       // ReadRss readRss = new ReadRss(this, recyclerView);
      //  readRss.execute();

        Intent intent = getIntent();
        title = intent.getStringExtra("newsTitle");
        description = intent.getStringExtra("newsArticle");
        image = intent.getStringExtra("newsImage");
        title_txt.setText(title);
      //  articletxt.setText(readRss.feedItems.get, TEXT, UTF);
        if (!image.isEmpty()) {
            Picasso.with(ctx).load(image).into(newsimg);


        }

        articletxt.setText(description);




        // readRss.feedItems.get(index);

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

    @Override
    public void onBackPressed() {
        /*Intent intent = new Intent(FullArticleViewActivity.this, LandingPageActivity.class);
        startActivity(intent);*/
    }
}
