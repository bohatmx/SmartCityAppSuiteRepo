package com.boha.library.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.dto.MunicipalityDTO;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.ThemeChooser;
import com.boha.library.util.Util;
import com.squareup.picasso.Picasso;

public class FAQFullDetailActivity extends AppCompatActivity {

    ScrollView scrollView;
    TextView title_txt, content_txt;
    WebView webView;
    Context ctx;

    String title;
    String description;
    int position, darkColor, primaryColor;
    MunicipalityDTO municipality;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeChooser.setTheme(this);
        setContentView(R.layout.activity_faqfull_detail);
        ctx = getApplicationContext();

        municipality = SharedUtil.getMunicipality(ctx);
        int logo = getIntent().getIntExtra("logo", R.drawable.ic_action_globe);
        darkColor = getIntent().getIntExtra("darkColor", R.color.black);
        primaryColor = getIntent().getIntExtra("primaryColor", R.color.black);
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


    Menu mMenu;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tourist_pager, menu);
        mMenu = menu;
        MenuItem favoriteItem = menu.findItem(com.boha.library.R.id.action_refresh);
        Drawable newIcon = (Drawable)favoriteItem.getIcon();
        newIcon.mutate().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
        favoriteItem.setIcon(newIcon);



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == com.boha.library.R.id.action_info) {
            Intent intent = new Intent(FAQFullDetailActivity.this, GeneralInfoActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == com.boha.library.R.id.action_emergency) {
            Intent intent = new Intent(FAQFullDetailActivity.this, EmergencyContactsActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == com.boha.library.R.id.action_theme) {
            Intent w = new Intent(FAQFullDetailActivity.this, ThemeSelectorActivity.class);
            w.putExtra("darkColor", themeDarkColor);
            startActivityForResult(w, THEME_REQUESTED);
            return true;
        }
        if (id == com.boha.library.R.id.action_app_guide) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://etmobileguide.oneconnectgroup.com/"));
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }

    int themeDarkColor;
    static final int THEME_REQUESTED = 8075;
}
