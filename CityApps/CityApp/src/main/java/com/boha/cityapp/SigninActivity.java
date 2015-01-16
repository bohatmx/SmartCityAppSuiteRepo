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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.boha.library.activities.MainPagerActivity;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.Util;

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
    Button btn, btnVisitor;
    EditText editID, editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("SigninActivity", "#### onCreate");
        setContentView(R.layout.activity_signin);
        ctx = getApplicationContext();
        activity = this;

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
            Intent i = new Intent(this, MainPagerActivity.class);
            startActivity(i);

        }
    }

    private void setFields() {
        btn = (Button) findViewById(R.id.SIGNIN_btnUser);
        btnVisitor = (Button) findViewById(R.id.SIGNIN_btnVisitor);

        editID = (EditText) findViewById(R.id.SIGNIN_editUserID);
        editPassword = (EditText) findViewById(R.id.SIGNIN_editPIN);
        progressBar = (ProgressBar) findViewById(R.id.SIGNIN_progress);
        heroImage = (ImageView) findViewById(R.id.SIGNIN_heroImage);
        imgLanguage = (ImageView) findViewById(R.id.SIGNIN_language);
        txtName = (TextView) findViewById(R.id.SIGNIN_cityName);
        handle = findViewById(R.id.SIGNIN_handle);
        progressBar.setVisibility(View.GONE);

        int h = Util.getWindowHeight(this);
        Log.w("SigninActivity", "## window height: " + h);
        heroImage.getLayoutParams().height = h / 2;

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(btn, 200, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        sendSignIn();
                    }
                });
            }
        });
        btnVisitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(btn, 200, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                       Util.showToast(ctx,getString(R.string.under_cons));
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
                        heroImage.setImageDrawable(Util.getNextImage(ctx));
                    }
                });

            }
        }, 1000, 5000);

    }

    private void sendSignIn() {

        if (editID.getText().toString().isEmpty()) {
            Util.showErrorToast(ctx, getString(R.string.enter_id));
            return;
        }
        if (editPassword.getText().toString().isEmpty()) {
            Util.showErrorToast(ctx, getString(R.string.enter_pswd));
            return;
        }
        //TODO - remove after API available
        //SharedUtil.setID(ctx, 111);
        //SharedUtil.setName(ctx, "User: " + editID.getText().toString());

        Intent i = new Intent(this, MainPagerActivity.class);
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
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
