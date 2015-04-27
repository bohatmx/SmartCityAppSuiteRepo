package com.boha.library.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.boha.library.R;
import com.boha.library.dto.MunicipalityDTO;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.ThemeChooser;
import com.boha.library.util.Util;

public class ThemeSelectorActivity extends AppCompatActivity {

    View indigo, blue, blueGray, teal, pink, orange, red, cyan,
    green, brown, amber, grey, purple, lime;
    Activity activity;
    Context ctx;
    int themeDarkColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeChooser.setTheme(this);
        setContentView(R.layout.theme_chooser);
        activity = this;
        ctx = getApplicationContext();
        setFields();

        int logo = getIntent().getIntExtra("logo", R.drawable.ic_action_globe);
        themeDarkColor = getIntent().getIntExtra("darkColor", R.color.teal_900);
        MunicipalityDTO muni = SharedUtil.getMunicipality(ctx);
        Util.setCustomActionBar(ctx, getSupportActionBar(),
                muni.getMunicipalityName(),ctx.getResources().getDrawable(logo),logo);
        //
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(themeDarkColor);
            window.setNavigationBarColor(themeDarkColor);
        }
    }

    private void setFields() {
        indigo = findViewById(R.id.TC_indigoLayout);
        blue = findViewById(R.id.TC_blueLayout);
        blueGray = findViewById(R.id.TC_blueGrayLayout);
        teal = findViewById(R.id.TC_tealLayout);
        pink = findViewById(R.id.TC_pinkLayout);
        orange = findViewById(R.id.TC_orangeLayout);
        red = findViewById(R.id.TC_redLayout);
        cyan = findViewById(R.id.TC_cyanLayout);
        green = findViewById(R.id.TC_greenLayout);
        brown = findViewById(R.id.TC_brownLayout);
        amber = findViewById(R.id.TC_amberLayout);
        grey = findViewById(R.id.TC_greyLayout);
        purple = findViewById(R.id.TC_purpleLayout);
        lime = findViewById(R.id.TC_limeLayout);

        indigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(indigo, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {

                        changeToTheme(CityApplication.THEME_INDIGO);
                    }
                });
            }
        });
        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(blue, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {

                        changeToTheme(CityApplication.THEME_BLUE);
                    }
                });
            }
        });
        blueGray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(indigo, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {

                        changeToTheme(CityApplication.THEME_BLUE_GRAY);
                    }
                });
            }
        });
        teal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(teal, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {

                        changeToTheme(CityApplication.THEME_TEAL);
                    }
                });
            }
        });
        orange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(orange, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {

                        changeToTheme(CityApplication.THEME_ORANGE);
                    }
                });
            }
        });
        pink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(pink, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {

                        changeToTheme(CityApplication.THEME_PINK);
                    }
                });
            }
        });
        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(red, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {

                        changeToTheme(CityApplication.THEME_RED);
                    }
                });
            }
        });
        cyan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(cyan, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {

                        changeToTheme(CityApplication.THEME_CYAN);
                    }
                });
            }
        });
        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(green, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {

                        changeToTheme(CityApplication.THEME_GREEN);
                    }
                });
            }
        });
        amber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(amber, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {

                        changeToTheme(CityApplication.THEME_AMBER);
                    }
                });
            }
        });
        brown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(brown, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {

                        changeToTheme(CityApplication.THEME_BROWN);
                    }
                });
            }
        });
        grey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(grey, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {

                        changeToTheme(CityApplication.THEME_GREY);
                    }
                });
            }
        });
        lime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(lime, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {

                        changeToTheme(CityApplication.THEME_LIME);
                    }
                });
            }
        });
        purple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(cyan, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {

                        changeToTheme(CityApplication.THEME_PURPLE);
                    }
                });
            }
        });

    }

    boolean themeChanged;

    private void changeToTheme(int theme) {
        SharedUtil.setThemeSelection(ctx, theme);
        themeChanged = true;
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        Log.e("ThemeSelectorActivity", "%%% onBackPressed, themeChanged: " + themeChanged);
        if (themeChanged) {
            setResult(RESULT_OK);
        } else {
            setResult(RESULT_CANCELED);
        }
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_theme_selector, menu);
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
