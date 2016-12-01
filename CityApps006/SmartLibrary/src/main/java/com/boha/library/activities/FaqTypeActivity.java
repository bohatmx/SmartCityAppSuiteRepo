package com.boha.library.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.dto.FreqQuestionTypeDTO;
import com.boha.library.dto.MunicipalityDTO;
/*import com.boha.library.fragments.FaqFragment;*/
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.CacheUtil;
import com.boha.library.util.FAQCommsUtil;
import com.boha.library.util.FAQs;
import com.boha.library.util.FaqStrings;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.ThemeChooser;
import com.boha.library.util.Util;
import com.google.firebase.analytics.FirebaseAnalytics;

public class FaqTypeActivity extends AppCompatActivity /*implements FaqFragment.FaqListener*/{

    WebView webView;
    Context ctx;
    ResponseDTO response;
    FaqStrings faqStrings;
    int position;
    int darkColor, primaryColor, logo;
    MunicipalityDTO municipality;
    FreqQuestionTypeDTO freqQuestionType;
    TextView txtTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeChooser.setTheme(this);
        setContentView(R.layout.activity_faq_type);
        webView = (WebView) findViewById(R.id.FAQ_webView);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        ctx = getApplicationContext();

        logo = getIntent().getIntExtra("logo",R.drawable.elogo);
        position = getIntent().getIntExtra("position",0);
        txtTitle = (TextView)findViewById(R.id.title);

        primaryColor = getIntent().getIntExtra("primaryColor",R.color.teal_500);
        darkColor = getIntent().getIntExtra("primaryColor",R.color.teal_700);
        municipality = SharedUtil.getMunicipality(getApplicationContext());

        ActionBar actionBar = getSupportActionBar();
        if (logo != 0) {
            Drawable d = ctx.getResources().getDrawable(logo);
            Util.setCustomActionBar(ctx,
                    actionBar,
                    municipality.getMunicipalityName(),
                    ContextCompat.getDrawable(ctx, R.drawable.elogo), logo);

        } else {
            getSupportActionBar().setTitle(municipality.getMunicipalityName());
        }


        setWebView(position);
      //  getFaqTypes();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        outState.putInt("positionIndex", position);
        super.onSaveInstanceState(outState);
    }


    private void getFaqTypes() {
        CacheUtil.getCacheLoginData(ctx, new CacheUtil.CacheRetrievalListener() {
            @Override
            public void onCacheRetrieved(ResponseDTO r) {
                response = r;
                getCachedFAQs();
            }

            @Override
            public void onError() {

            }
        });
    }

    private void getCachedFAQs() {
        CacheUtil.getCachedFAQ(ctx, new CacheUtil.FAQCacheRetrievalListener() {
            @Override
            public void onCacheRetrieved(FaqStrings fs) {
                if (fs.getAccountsFAQ() == null) {
                    getRemoteFAQs();
                    return;
                }
                faqStrings = fs;
                setWebView(position);
            }

            @Override
            public void onError() {
             //   mListener.setBusy(false);
            }
        });
    }



    public void getRemoteFAQs() {
//        mListener.setBusy(true);
        FAQCommsUtil.getFAQfiles(ctx,
                SharedUtil.getMunicipality(ctx).getMunicipalityID(),
                new FAQCommsUtil.FAQListener() {
                    @Override
                    public void onSuccess(FaqStrings fs) {
                        faqStrings = fs;
                        setWebView(position);
                    }

                    @Override
                    public void onError(String message) {
                        Util.showErrorToast(ctx, message);
                        onBackPressed();
                    }
                });

    }

    private static final String TEXT = "text/html", UTF = "UTF-8";

    FreqQuestionTypeDTO faqType;
    //FaqFragment.FaqListener mListener;
    ImageView icon;

    /*@Override
    public void setBusy(boolean busy) {

    }*/

    /*@Override
    public void onFaqCLicked() {

    }
*/

    String AccountFAQ =  "http://icsmnewsdev.oneconnectgroup.com/et/faq/AccountsPayments.html";
    //
    //
    //
    //
    //
    //
    //
    //

    private FirebaseAnalytics mFirebaseAnalytics;
    private void setAnalyticsEvent(String id, String name) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        Log.w(LOG, "analytics event sent .....");

    }

    private void setWebView(int position) {

      //  txtTitle.setText(/*"Faq Title"*/response.getFaqTypeList().get(position).getFaqTypeName());
        switch (position) {
            case 0:
                /*Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://icsmnewsdev.oneconnectgroup.com/et/faq/AccountsPayments.html"));
                startActivity(intent);*/
               // setAnalyticsEvent("guide", "AppGuide");
                txtTitle.setText("Accounts & Payments");
                webView.loadData(FAQs.getACCOUNTSPAYMENTS(), TEXT, UTF);

                //webView.loadData(faqStrings.getAccountsFAQ(), TEXT, UTF);
//                icon.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.accounts_statement));
                break;
            case 1:
                /*Intent intent1 = new Intent(Intent.ACTION_VIEW);
                intent1.setData(Uri.parse("http://icsmnewsdev.oneconnectgroup.com/et/faq/WaterSanitation.html"));
                startActivity(intent1);*/
                webView.loadData(FAQs.getWATERSANITATION(), TEXT, UTF);
                txtTitle.setText("Water & Sanitation");
                /*webView.loadData(faqStrings.getBuildingPlansFAQ(), TEXT, UTF);*/
               // icon.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.building_plans));
                break;
            case 2:
                /*Intent intent2 = new Intent(Intent.ACTION_VIEW);
                intent2.setData(Uri.parse("http://icsmnewsdev.oneconnectgroup.com/et/faq/CleansingSolidWaste.html"));
                startActivity(intent2);*/
                webView.loadData(FAQs.getCLEANINGSOLIDWASTE(), TEXT, UTF);
                txtTitle.setText("Cleansing & Solid Waste");
                //webView.loadData(faqStrings.getCleaningWasteFAQ(), TEXT, UTF);
             //   icon.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.cleaning_solid_waste));
                break;
            case 3:
                /*Intent intent3 = new Intent(Intent.ACTION_VIEW);
                intent3.setData(Uri.parse("http://icsmnewsdev.oneconnectgroup.com/et/faq/RatesTaxes.html"));
                startActivity(intent3);*/
                webView.loadData(FAQs.getRATESTAXES(), TEXT, UTF);
                txtTitle.setText("Rates & Taxes");
               // webView.loadData(faqStrings.getElectricityFAQ(), TEXT, UTF);
            //    icon.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.electricity));
                break;
            case 4:
                /*Intent intent4 = new Intent(Intent.ACTION_VIEW);
                intent4.setData(Uri.parse("http://icsmnewsdev.oneconnectgroup.com/et/faq/BuildingPlans.html"));
                startActivity(intent4);*/
                webView.loadData(FAQs.getBUILDINGPLANS(), TEXT, UTF);
                txtTitle.setText("Building Plans");
               // webView.loadData(faqStrings.getHealthFAQ(), TEXT, UTF);
             //   icon.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.health));
                break;
            case 5:
                /*Intent intent5 = new Intent(Intent.ACTION_VIEW);
                intent5.setData(Uri.parse("http://icsmnewsdev.oneconnectgroup.com/et/faq/Electricity.html"));
                startActivity(intent5);*/
                webView.loadData(FAQs.getELECTRICITY(), TEXT, UTF);
                txtTitle.setText("Electricity");
               // webView.loadData(faqStrings.getMetroPoliceFAQ(), TEXT, UTF);
             //   icon.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.metro_police));
                break;
            case 6:
                /*Intent intent6 = new Intent(Intent.ACTION_VIEW);
                intent6.setData(Uri.parse("http://icsmnewsdev.oneconnectgroup.com/et/faq/SocialServices.html"));
                startActivity(intent6);*/
                webView.loadData(FAQs.getSOCIALSERVICES(), TEXT, UTF);
                txtTitle.setText("Social Services");
               // webView.loadData(faqStrings.getRatesTaxesFAQ(), TEXT, UTF);
             //   icon.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.rates_taxes));
                break;
            case 7:
                /*Intent intent7 = new Intent(Intent.ACTION_VIEW);
                intent7.setData(Uri.parse("http://icsmnewsdev.oneconnectgroup.com/et/faq/Health.html"));
                startActivity(intent7);*/
                webView.loadData(FAQs.getHEALTH(), TEXT, UTF);
                txtTitle.setText("Health");
              //  webView.loadData(faqStrings.getSocialServicesFAQ(), TEXT, UTF);
            //    icon.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.social_services));
                break;
            case 8:
                /*Intent intent8 = new Intent(Intent.ACTION_VIEW);
                intent8.setData(Uri.parse("http://icsmnewsdev.oneconnectgroup.com/et/faq/MetroPolice.html"));
                startActivity(intent8);*/
                webView.loadData(FAQs.getMETROPOLICE(), TEXT, UTF);
                txtTitle.setText("Metro Police");
               // webView.loadData(faqStrings.getWaterSanitationFAQ(), TEXT, UTF);
             //   icon.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.water_sanitation));
                break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_faq, menu);
        mMenu = menu;
        return true;
    }
    int themeDarkColor;

    Menu mMenu;
    static final int THEME_REQUESTED = 8075;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == com.boha.library.R.id.action_app_guide) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://etmobileguide.oneconnectgroup.com/"));
            startActivity(intent);
        }
       /* if (id == com.boha.library.R.id.action_theme) {
            Intent w = new Intent(this, ThemeSelectorActivity.class);
            w.putExtra("darkColor", themeDarkColor);
            startActivityForResult(w, THEME_REQUESTED);
            return true;
        } */
        if (id == R.id.action_info) {
            Intent intent = new Intent(FaqTypeActivity.this, GeneralInfoActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == com.boha.library.R.id.action_emergency) {
            Intent intent = new Intent(FaqTypeActivity.this, EmergencyContactsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private static final String LOG = FaqTypeActivity.class.getSimpleName();
}
