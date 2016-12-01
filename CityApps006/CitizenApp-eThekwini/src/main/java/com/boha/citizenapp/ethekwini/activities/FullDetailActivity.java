package com.boha.citizenapp.ethekwini.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.activities.EmergencyContactsActivity;
import com.boha.library.activities.GeneralInfoActivity;
import com.boha.library.activities.ThemeSelectorActivity;
import com.boha.library.dto.MunicipalityDTO;
import com.boha.library.dto.ProfileInfoDTO;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.ThemeChooser;
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
    int position, darkColor, primaryColor, logo;
    MunicipalityDTO municipality;
    ProfileInfoDTO profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeChooser.setTheme(this);
        setContentView(R.layout.activity_full_detail);
        ctx = getApplicationContext();

        municipality = SharedUtil.getMunicipality(ctx);
        darkColor = getIntent().getIntExtra("darkColor", R.color.black);
        primaryColor = getIntent().getIntExtra("primaryColor", R.color.black);
        logo = getIntent().getIntExtra("logo", R.drawable.ic_action_globe);
        ActionBar actionBar = getSupportActionBar();
        Util.setCustomActionBar(ctx,
                actionBar,
                municipality.getMunicipalityName(),
                ctx.getResources().getDrawable(R.drawable.logo), logo);
        getSupportActionBar().setTitle("");

        profile = SharedUtil.getProfile(getApplicationContext());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(themeDarkColor);
            window.setNavigationBarColor(themeDarkColor);
        }

        scrollView = (ScrollView) findViewById(R.id.DETAILSCROLL);
        title_txt = (TextView) findViewById(R.id.details_title);
        detailImg = (ImageView) findViewById(R.id.DETAILSIMG);
        webView = (WebView) findViewById(R.id.details_webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setDisplayZoomControls(true);

        //
       webView.getSettings().setLoadWithOverviewMode(true);
       // webView.getSettings().setUseWideViewPort(true);
      //  webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
       // webView.setScrollbarFadingEnabled(true);
        //
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

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == com.boha.library.R.id.action_info) {
            Intent intent = new Intent(FullDetailActivity.this, GeneralInfoActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == com.boha.library.R.id.action_emergency) {
            Intent intent = new Intent(FullDetailActivity.this, EmergencyContactsActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == com.boha.library.R.id.action_theme) {
            Intent w = new Intent(FullDetailActivity.this, ThemeSelectorActivity.class);
            w.putExtra("darkColor", themeDarkColor);
            startActivityForResult(w, THEME_REQUESTED);
            return true;
        }
        if (id == R.id.action_app_guide) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://etmobileguide.oneconnectgroup.com/"));
            startActivity(intent);
            return true;
        }
        if (id == android.R.id.home) {
            if (profile != null) {
                Intent m = new Intent(getApplicationContext(), CitizenDrawerActivity.class);
                m.putExtra("page", "News");
                startActivity(m);
                return true;
            }
        } else {
            Intent m = new Intent(getApplicationContext(), TouristDrawerActivity.class);
            m.putExtra("page", "News");
            startActivity(m);
            return true;
        }




        return super.onOptionsItemSelected(item);
    }

    int themeDarkColor;
    static final int THEME_REQUESTED = 8075;
    static final String LOG = FullDetailActivity.class.getSimpleName();
}
