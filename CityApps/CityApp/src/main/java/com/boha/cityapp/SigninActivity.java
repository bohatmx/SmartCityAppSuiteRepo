package com.boha.cityapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.boha.library.activities.LanguageActivity;
import com.boha.library.activities.MainActivity;
import com.boha.library.util.LocaleUtil;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class SigninActivity extends ActionBarActivity {

    ImageView heroImage, imgLanguage;
    Timer timer;
    TextView txtName;
    View handle;
    Context ctx;
    ProgressBar progressBar;
    Activity activity;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("SigninActivity","#### onCreate");
        setContentView(R.layout.activity_signin);
        ctx = getApplicationContext();
        activity = this;

        int langIndex = SharedUtil.getLanguageIndex(ctx);
        if (langIndex == -1) {
            Intent i = new Intent(this, LanguageActivity.class);
            startActivity(i);
            return;
        }

        setFields();
    }

    @Override
    public void onResume() {
        Log.w("SigninActivity", "##### onResume");
        checkVirginity();
        super.onResume();
    }
    private void checkVirginity() {
        int id = SharedUtil.getID(ctx);
        if (id > 0) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);

        }
    }
    private void setFields() {
        btn = (Button)findViewById(R.id.SIGNIN_btn);
        progressBar = (ProgressBar)findViewById(R.id.SIGNIN_progress);
        heroImage = (ImageView)findViewById(R.id.SIGNIN_heroImage);
        imgLanguage = (ImageView)findViewById(R.id.SIGNIN_language);
        txtName = (TextView)findViewById(R.id.SIGNIN_cityName);
        handle = findViewById(R.id.SIGNIN_handle);
        progressBar.setVisibility(View.GONE);

        int h = Util.getWindowHeight(this);
        Log.w("SigninActivity", "## window height: " + h);
        heroImage.getLayoutParams().height = h/2;

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(btn,200,new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        SharedUtil.setID(ctx, 111);
                        SharedUtil.setName(ctx, "Aubrey M.");
                        showPopup();
                    }
                });
            }
        });

        imgLanguage.setVisibility(View.GONE);

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        heroImage.setImageDrawable(Util.getRandomImage(ctx));
                    }
                });

            }
        }, 1000, 5000);

    }


    private void showPopup() {
        List<String> list = new ArrayList<>();
        list.add("English");
        list.add("Afrikaans");
        list.add("isiZulu");
        list.add("isiXhosa");
        list.add("xiTsonga");
        list.add("seTswana");


        Util.showPopupBasicWithHeroImage(ctx,this,list, handle, getString(R.string.select_language), new Util.UtilPopupListener() {
            @Override
            public void onItemSelected(int index) {
                SharedUtil.setLanguageIndex(ctx,index);
                setLocale(index);
                startMain();
            }
        });
    }
    private void setLocale(int index) {
        switch (index) {
            case 0:
                LocaleUtil.setLocale(ctx, LocaleUtil.ENGLISH);
                break;
            case 1:
                LocaleUtil.setLocale(ctx, LocaleUtil.AFRIKAANS);
                break;
            case 2:
                LocaleUtil.setLocale(ctx, LocaleUtil.ZULU);
                break;
            case 3:
                LocaleUtil.setLocale(ctx, LocaleUtil.XHOSA);
                break;
            case 4:
                LocaleUtil.setLocale(ctx, LocaleUtil.XITSONGA);
                break;
            case 5:
                LocaleUtil.setLocale(ctx, LocaleUtil.SETSWANA);
                break;
        }
    }
    private void startMain() {
        Intent i = new Intent(this,MainActivity.class);
        i.putExtra("restarted", 1);
        startActivity(i);
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_signin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
