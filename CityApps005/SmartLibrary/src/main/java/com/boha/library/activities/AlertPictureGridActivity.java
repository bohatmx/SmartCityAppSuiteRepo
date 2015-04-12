package com.boha.library.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.adapters.AlertPictureAdapter;
import com.boha.library.dto.AlertDTO;
import com.boha.library.dto.AlertImageDTO;
import com.boha.library.dto.MunicipalityDTO;
import com.boha.library.util.DividerItemDecoration;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.Util;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class AlertPictureGridActivity extends ActionBarActivity {

    Context ctx;
    AlertPictureAdapter adapter;
    List<AlertImageDTO> photoList;
    int lastIndex;
    AlertDTO alert;
    ImageView icon;
    TextView txtAlertType, txtDate, txtTime, txtAddress, txtFab, txtCount;
    View addressLayout, topLayout;
    boolean isOpen;
    int logo;
    public static int ALERT = 1, COMPLAINT = 2, MUNICIPALITY = 3;
    static final String LOG = AlertPictureGridActivity.class.getSimpleName();
    static final Locale LOCALE = Locale.getDefault();
    static final SimpleDateFormat sdfDate = new SimpleDateFormat("EEEE dd MMMM yyyy", LOCALE);
    static final SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm", LOCALE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_alert_gallery);
        ctx = getApplicationContext();
        setFields();

        alert = (AlertDTO) getIntent().getSerializableExtra("alert");
        logo = getIntent().getIntExtra("logo", R.drawable.ic_action_globe);
        photoList = alert.getAlertImageList();
        txtAlertType.setText(alert.getAlertType().getAlertTypeName());
        txtDate.setText(sdfDate.format(alert.getUpdated()));
        txtTime.setText(sdfTime.format(alert.getUpdated()));


        Util.flashSeveralTimes(txtFab, 300, 3, null) ;
        MunicipalityDTO municipality = SharedUtil.getMunicipality(getApplicationContext());
        ActionBar actionBar = getSupportActionBar();
        if (logo != 0) {
            Drawable d = ctx.getResources().getDrawable(logo);
            Util.setCustomActionBar(ctx,
                    actionBar,
                    municipality.getMunicipalityName(), d);
            getSupportActionBar().setTitle("");
        } else {
            getSupportActionBar().setTitle(municipality.getMunicipalityName());
        }


        setGrid();
        //Track AlertPictureGridActivity
        CityApplication ca = (CityApplication) getApplication();
        Tracker t = ca.getTracker(
                CityApplication.TrackerName.APP_TRACKER);
        t.setScreenName(AlertPictureGridActivity.class.getSimpleName());
        t.send(new HitBuilders.ScreenViewBuilder().build());
        //

    }

    private void setFields() {
        topLayout = findViewById(R.id.TOPG_titleLayout);
        addressLayout = findViewById(R.id.TOPG_addressLayout);
        addressLayout.setVisibility(View.GONE);
        txtAddress = (TextView) findViewById(R.id.TOPG_address);
        txtCount = (TextView) findViewById(R.id.TOPG_count);
        txtFab = (TextView) findViewById(R.id.TOPG_fab);
        txtAlertType = (TextView) findViewById(R.id.TOPG_title);
        txtDate = (TextView) findViewById(R.id.TOPG_date);
        txtTime = (TextView) findViewById(R.id.TOPG_time);
        icon = (ImageView) findViewById(R.id.TOPG_icon);
        list = (RecyclerView) findViewById(R.id.PG_recyclerView);

        txtAlertType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(txtAlertType, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        if (isOpen) {
                            isOpen = false;
                            Util.collapse(addressLayout, 300, null);
                        } else {
                            new GeoTask().execute();
                        }
                    }
                });
            }
        });
        txtFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(txtFab, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        if (isOpen) {
                            isOpen = false;
                            Util.collapse(addressLayout, 300, null);
                        } else {
                            new GeoTask().execute();
                        }
                    }
                });
            }
        });
//        icon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Util.flashOnce(icon, 300, new Util.UtilAnimationListener() {
//                    @Override
//                    public void onAnimationEnded() {
//                        Intent intent = new Intent(ctx, AlertMapActivity.class);
//                        intent.putExtra("complaint", alert);
//                        startActivity(intent);
//                    }
//                });
//            }
//        });
    }


    RecyclerView list;

    private void setGrid() {
        txtCount.setText("" + photoList.size());
        LinearLayoutManager lm = new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false);

        lm.setSmoothScrollbarEnabled(true);
        list.setLayoutManager(lm);

        list.setItemAnimator(new DefaultItemAnimator());
        list.addItemDecoration(new DividerItemDecoration(ctx, RecyclerView.HORIZONTAL));

        adapter = new AlertPictureAdapter(photoList,
                ctx, new AlertPictureAdapter.PictureListener() {
            @Override
            public void onPictureClicked(int position) {
                Log.e(LOG, "Picture clicked..., position = " + position);
                lastIndex = position;
                Intent i = new Intent(getApplicationContext(), FullPhotoActivity.class);
                i.putExtra("alert", alert);
                i.putExtra("index", position);
                i.putExtra("type", 1);
//                startActivity(i);
            }
        });

        list.setAdapter(adapter);
        if (lastIndex < photoList.size()) {
            list.getLayoutManager().scrollToPosition(lastIndex);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_alert_picture_grid, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onPause() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        super.onPause();
    }

    Address address;

    class GeoTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            Log.e(LOG, "### start GeoTask doInBackground");
            Geocoder geocoder = new Geocoder(ctx);
            try {
                List<Address> list = geocoder.getFromLocation(
                        alert.getLatitude(), alert.getLongitude(), 1);
                if (list != null && !list.isEmpty()) {
                    address = list.get(0);
                } else {
                    return 9;
                }
            } catch (IOException e) {
                Log.e(LOG, "Impossible to connect to Geocoder", e);
                return 9;
            }
            return 0;
        }

        @Override
        public void onPostExecute(Integer result) {
            if (result == 0) {
                StringBuilder sb = new StringBuilder();
                //mAddresslines madmin - kzn, mfeat - klaarwater, msubm - durban metro
                int maxIndex = address.getMaxAddressLineIndex();
                int count = maxIndex + 1;
                for (int i = 0; i < count; i++) {
                    sb.append(address.getAddressLine(i));
                    if (i < (count - 1)) {
                        sb.append(", ");
                    }
                }
                txtAddress.setText(sb.toString());
            } else {
                txtAddress.setText(getString(R.string.address_not_found));
            }
            addressLayout.setVisibility(View.VISIBLE);
            Util.expand(addressLayout, 300, null);
            isOpen = true;
        }

    }
}
