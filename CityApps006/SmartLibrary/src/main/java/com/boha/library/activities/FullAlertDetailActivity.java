package com.boha.library.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.dto.MunicipalityDTO;
import com.boha.library.dto.ProfileInfoDTO;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.ThemeChooser;
import com.boha.library.util.ThemeListener;
import com.boha.library.util.Util;
import com.squareup.picasso.Picasso;

public class FullAlertDetailActivity extends AppCompatActivity implements ThemeListener{

    ScrollView scrollView;
    TextView title_txt, articletxt;
    ImageView detailImg;
    WebView webView;
    Context ctx;

    String title;
    String description;
    String image;
    int position, darkColor, primaryColor;
    MunicipalityDTO municipality;
    int logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeChooser.setTheme(this);
        setContentView(R.layout.activity_full_alert_detail);
        ctx = getApplicationContext();


        municipality = SharedUtil.getMunicipality(ctx);
        themeDarkColor = getIntent().getIntExtra("darkColor", R.color.teal_900);
        logo = getIntent().getIntExtra("logo", R.drawable.ic_action_globe);
        darkColor = getIntent().getIntExtra("darkColor", R.color.black);
        primaryColor = getIntent().getIntExtra("primaryColor", R.color.black);
        ActionBar actionBar = getSupportActionBar();
        Util.setCustomActionBar(ctx, actionBar, municipality.getMunicipalityName(),
                ctx.getResources().getDrawable(R.drawable.logo), logo);
        getSupportActionBar().setTitle("");

        profile = SharedUtil.getProfile(getApplicationContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(themeDarkColor);
            window.setNavigationBarColor(themeDarkColor);
        }
        scrollView = (ScrollView) findViewById(R.id.ALERTDETAILSCROLL);
        title_txt = (TextView) findViewById(R.id.alert_details_title);
        detailImg = (ImageView) findViewById(R.id.alert_DETAILSIMG);
        webView = (WebView) findViewById(R.id.alert_details_webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
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

    Menu mMenu;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tourist_pager, menu);
        mMenu = menu;
        MenuItem favoriteItem = menu.findItem(R.id.action_refresh);
        Drawable newIcon = (Drawable)favoriteItem.getIcon();
        newIcon.mutate().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
        favoriteItem.setIcon(newIcon);



        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_info) {
            Intent intent = new Intent(FullAlertDetailActivity.this, GeneralInfoActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_emergency) {
            Intent intent = new Intent(FullAlertDetailActivity.this, EmergencyContactsActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_theme) {
            Intent w = new Intent(FullAlertDetailActivity.this, ThemeSelectorActivity.class);
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
        /*if (id == android.R.id.home) {
            if (profile != null) {
            Intent m = new Intent(getApplicationContext(), CitizenDrawerActivity.class);
            m.putExtra("page", "Alerts");
            startActivity(m);
            return true;
        }
        } else {
            Intent m = new Intent(getApplicationContext(), TouristDrawerActivity.class);
            m.putExtra("page", "Alerts");
            startActivity(m);
            return true;
        }*/


        return super.onOptionsItemSelected(item);
    }

    ProfileInfoDTO profile;

    int themeDarkColor, themePrimaryColor;
    static final int THEME_REQUESTED = 8075;

    static final String LOG = FullDetailActivity.class.getSimpleName();

    @Override
    public void onThemeChanged() {
        Intent intent = new Intent(ctx, FullAlertDetailActivity.class);
        intent.putExtra("logo", logo);
        intent.putExtra("darkColor", themeDarkColor);
        intent.putExtra("primaryColor", themePrimaryColor);

        startActivityForResult(intent, CHECK_DESTINATION);
    }

    /*@Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent m = new Intent(getApplicationContext(), CitizenDrawerActivity.class);
        m.putExtra("page", "Alerts");
        startActivity(m);
    }
*/
    static final int CHECK_DESTINATION = 9086;
}
