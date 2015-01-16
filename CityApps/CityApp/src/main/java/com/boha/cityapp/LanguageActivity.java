package com.boha.cityapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.boha.cityapps.R;
import com.boha.library.activities.MainPagerActivity;
import com.boha.library.util.LocaleUtil;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class LanguageActivity extends ActionBarActivity {

    View handle;
    ImageView heroImage;
    Timer timer;
    Context ctx;
    Button btn;
    boolean languageHasBeenChosen;
    int languageIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        ctx = getApplicationContext();

        handle = findViewById(R.id.LANG_handle);
        heroImage = (ImageView) findViewById(R.id.LANG_heroImage);
        btn = (Button) findViewById(R.id.LANG_btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(btn, 200, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        showPopup();
                    }
                });
            }
        });


        languageIndex = getIntent().getIntExtra("languageIndex", -1);

        if (languageIndex == -1) {
            if (SharedUtil.getName(ctx) != null) {
                Intent e = new Intent(this, MainPagerActivity.class);
                startActivity(e);
                finish();
            }
        }

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Animation an = AnimationUtils.makeOutAnimation(ctx, true);
                        an.setDuration(300);
                        an.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                heroImage.setImageDrawable(Util.getNextImage(ctx));
                                Animation an = AnimationUtils.makeInAnimation(ctx,true);
                                an.setDuration(500);
                                heroImage.startAnimation(an);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        heroImage.startAnimation(an);




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

        Util.showPopupBasicWithHeroImage(ctx, this, list, handle, getString(R.string.select_language), new Util.UtilPopupListener() {
            @Override
            public void onItemSelected(int index) {
                SharedUtil.setLanguageIndex(ctx, index);
                setLocale(index);
                languageHasBeenChosen = true;
                languageIndex = index;
                if (SharedUtil.getName(ctx) == null) {
                    Intent w = new Intent(ctx,SigninActivity.class);
                    startActivity(w);
                } else {
                    onBackPressed();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent w = new Intent();
        w.putExtra("languageIndex", languageIndex);
        w.putExtra("languageHasBeenChosen", languageHasBeenChosen);
        setResult(RESULT_OK, w);
        finish();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_language, menu);
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
