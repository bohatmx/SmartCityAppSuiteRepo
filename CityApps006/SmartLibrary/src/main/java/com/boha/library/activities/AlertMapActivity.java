package com.boha.library.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.dto.AlertDTO;
import com.boha.library.dto.AlertTypeDTO;
import com.boha.library.dto.MunicipalityDTO;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.DistanceUtil;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.Statics;
import com.boha.library.util.ThemeChooser;
import com.boha.library.util.Util;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AlertMapActivity extends AppCompatActivity {

    GoogleMap googleMap;
    GoogleApiClient mGoogleApiClient;
    LocationRequest locationRequest;
    Location location;
    Context ctx;
    DisplayMetrics displayMetrics;
    List<Marker> markers = new ArrayList<>();
    static final String LOG = AlertMapActivity.class.getSimpleName();
    boolean mResolvingError;
    static final long ONE_MINUTE = 1000 * 60;
    static final long FIVE_MINUTES = 1000 * 60 * 5;
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    // Unique tag for the error dialog fragment
    private static final String DIALOG_ERROR = "dialog_error";

    int index;
    TextView text, txtCount;
    ImageView iconCollapse;
    View topLayout, mapInfo;
    TextView addr1, addr2, dist, dur, title;
    LayoutInflater inflater;
    static final Locale loc = Locale.getDefault();
    static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    List<AlertDTO> alertList;
    Activity activity;
    int logo, primaryColorDark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.w(LOG, "#### onCreate");
        super.onCreate(savedInstanceState);
        ctx = getApplicationContext();
        activity = this;
        ThemeChooser.setTheme(this);
        setContentView(R.layout.activity_maps);
        inflater = getLayoutInflater();
        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay()
                .getMetrics(displayMetrics);

        setFields();
        ResponseDTO r = (ResponseDTO) getIntent().getSerializableExtra("alertList");
        primaryColorDark = getIntent().getIntExtra("primaryColorDark", R.color.blue_gray_800);
        if (r != null) {
            alertList = r.getAlertList();
            txtCount.setText("" + alertList.size());
        }
        alert = (AlertDTO) getIntent().getSerializableExtra("alert");
        if (alert != null) {
            txtCount.setText("1");
            text.setText(alert.getDescription());
            title.setText(alert.getDescription());
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.MAP_map);

        try {
            googleMap = mapFragment.getMap();
            if (googleMap == null) {
                Util.showToast(ctx, getString(R.string.map_not_avail));
                finish();
                return;
            }
            setGoogleMap();
        } catch (Exception e) {
            Log.e(LOG, "Map failed", e);
            finish();
            return;
        }



        MunicipalityDTO municipality = SharedUtil.getMunicipality(ctx);
        logo = getIntent().getIntExtra("logo", 0);
        if (logo != 0) {
            Drawable d = ctx.getResources().getDrawable(logo);
            Util.setCustomActionBar(ctx,
                    getSupportActionBar(),
                    municipality.getMunicipalityName(), d, logo);
            getSupportActionBar().setTitle("");
        } else {
            getSupportActionBar().setTitle(municipality.getMunicipalityName());
        }
        //Track AlertMapActivity
        CityApplication ca = (CityApplication) getApplication();
        Tracker t = ca.getTracker(
                CityApplication.TrackerName.APP_TRACKER);
        t.setScreenName(AlertMapActivity.class.getSimpleName());
        t.send(new HitBuilders.ScreenViewBuilder().build());
        //
    }

    Marker marker;

    private void setGoogleMap() {
        googleMap.setMyLocationEnabled(true);
        googleMap.setBuildingsEnabled(true);
        location = googleMap.getMyLocation();

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker m) {
                marker = m;
                LatLng latLng = marker.getPosition();
                Integer id = Integer.parseInt(marker.getTitle());
                if (alertList != null) {
                    for (AlertDTO x : alertList) {
                        if (x.getAlertID().intValue() == id.intValue()) {
                            alert = x;
                            title.setText(alert.getDescription());
                            break;
                        }
                    }
                }
                showPopup(latLng.latitude, latLng.longitude, marker.getSnippet());

                return true;
            }
        });
        if (alertList != null) {
            setAlertMarkers();
        }
        if (alert != null) {
            setOneMarker();
        }

    }


    private void setAlertMarkers() {
        googleMap.clear();
        int index = 0, count = 0, randomIndex = 0;

        for (AlertDTO alert : alertList) {
            if (alert.getLatitude() == null) continue;
            LatLng pnt = new LatLng(alert.getLatitude(), alert.getLongitude());
            BitmapDescriptor desc = null;
            Short color = null;
            View dot;
            TextView txtNumber;
            if (alert.getAlertType().getColor() != null) {
                switch (alert.getAlertType().getColor()) {
                    case AlertTypeDTO.RED:
                        dot = inflater.inflate(com.boha.library.R.layout.dot_red, null);
                        txtNumber = (TextView) dot.findViewById(com.boha.library.R.id.DOT_text);
                        txtNumber.setText("" + (index + 1));
                        desc = BitmapDescriptorFactory.fromBitmap(Util.createBitmapFromView(ctx, dot, displayMetrics));
                        break;
                    case AlertTypeDTO.GREEN:
                        dot = inflater.inflate(com.boha.library.R.layout.dot_green, null);
                        txtNumber = (TextView) dot.findViewById(com.boha.library.R.id.DOT_text);
                        txtNumber.setText("" + (index + 1));
                        desc = BitmapDescriptorFactory.fromBitmap(Util.createBitmapFromView(ctx, dot, displayMetrics));
                        break;
                    case AlertTypeDTO.AMBER:
                        dot = inflater.inflate(com.boha.library.R.layout.dot_amber, null);
                        txtNumber = (TextView) dot.findViewById(com.boha.library.R.id.DOT_text);
                        txtNumber.setText("" + (index + 1));
                        desc = BitmapDescriptorFactory.fromBitmap(Util.createBitmapFromView(ctx, dot, displayMetrics));
                        break;
                }

            }
            Marker m =
                    googleMap.addMarker(new MarkerOptions()
                            .title("" + alert.getAlertID())
                            .icon(desc)
                            .snippet(alert.getDescription())
                            .position(pnt));
            markers.add(m);
            index++;
            count++;
        }
        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                //ensure that all markers in bounds

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (Marker marker : markers) {
                    builder.include(marker.getPosition());
                }

                LatLngBounds bounds = builder.build();
                int padding = 60; // offset from edges of the map in pixels
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

                txtCount.setText("" + markers.size());
                //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 1.0f));
                googleMap.animateCamera(cu);
            }
        });

    }

    AlertDTO alert;

    private void setOneMarker() {
        if (alert.getLatitude() == null) {
            return;
        }
        LatLng pnt = new LatLng(alert.getLatitude(), alert.getLongitude());
        BitmapDescriptor desc;
        View dot;
        TextView txtNumber;
        switch (alert.getAlertType().getColor()) {
            case AlertTypeDTO.RED:
                dot = inflater.inflate(com.boha.library.R.layout.dot_red, null);
                txtNumber = (TextView) dot.findViewById(com.boha.library.R.id.DOT_text);
                txtNumber.setText("" + (alert.getIndex() + 1));
                desc = BitmapDescriptorFactory.fromBitmap(Util.createBitmapFromView(ctx, dot, displayMetrics));
                break;
            case AlertTypeDTO.GREEN:
                dot = inflater.inflate(com.boha.library.R.layout.dot_green, null);
                txtNumber = (TextView) dot.findViewById(com.boha.library.R.id.DOT_text);
                txtNumber.setText("" + (alert.getIndex() + 1));
                desc = BitmapDescriptorFactory.fromBitmap(Util.createBitmapFromView(ctx, dot, displayMetrics));
                break;
            case AlertTypeDTO.AMBER:
                dot = inflater.inflate(com.boha.library.R.layout.dot_amber, null);
                txtNumber = (TextView) dot.findViewById(com.boha.library.R.id.DOT_text);
                txtNumber.setText("" + (alert.getIndex() + 1));
                desc = BitmapDescriptorFactory.fromBitmap(Util.createBitmapFromView(ctx, dot, displayMetrics));
                break;
            default:
                dot = inflater.inflate(com.boha.library.R.layout.dot_amber, null);
                txtNumber = (TextView) dot.findViewById(com.boha.library.R.id.DOT_text);
                txtNumber.setText("" + (alert.getIndex() + 1));
                desc = BitmapDescriptorFactory.fromBitmap(Util.createBitmapFromView(ctx, dot, displayMetrics));
                break;
        }
        Marker m =
                googleMap.addMarker(new MarkerOptions()
                        .title("" + alert.getAlertID())
                        .icon(desc)
                        .snippet(alert.getDescription())
                        .position(pnt));
        markers.add(m);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pnt, 1.0f));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(13.0f));
    }

    private void setFields() {
        text = (TextView) findViewById(R.id.MAP_text);
        iconCollapse = (ImageView) findViewById(R.id.MAP_iconCollapse);
        txtCount = (TextView) findViewById(R.id.MAP_count);
        txtCount.setText("0");
        text.setText(getString(R.string.active_alerts));
        Statics.setRobotoFontLight(ctx, text);
        mapInfo = findViewById(R.id.MAP_info);
        mapInfo.setVisibility(View.GONE);
        addr1 = (TextView) mapInfo.findViewById(R.id.MAP_addressFrom);
        addr2 = (TextView) mapInfo.findViewById(R.id.MAP_addressTo);
        dist = (TextView) mapInfo.findViewById(R.id.MAP_distance);
        dur = (TextView) mapInfo.findViewById(R.id.MAP_duration);
        title = (TextView) mapInfo.findViewById(R.id.MAP_distanceTitle);

        Statics.setRobotoFontBold(ctx, text);
        iconCollapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(iconCollapse, 200, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        Util.collapse(mapInfo, 500, null);
                    }
                });
            }
        });

        topLayout = findViewById(R.id.MAP_top);

    }

    List<String> list;

    private void showPopup(final double lat, final double lng, final String title) {
        list = new ArrayList<>();
        list.add(getString(R.string.directions));
        list.add(getString(R.string.pictures));
        list.add(getString(R.string.get_distance));
        ImageView dummy = new ImageView(ctx);
        if (alert != null) {
            if (alert.getAlertImageList() != null && !alert.getAlertImageList().isEmpty()) {
                String url = Util.getAlertImageURL(alert.getAlertImageList().get(0));
                Picasso.with(ctx).load(url).into(dummy);

            } else {
                list.remove(1);
                Util.showPopupList(ctx, activity, list, topLayout, alert.getTitle(),
                        primaryColorDark, new Util.UtilPopupListener() {
                            @Override
                            public void onItemSelected(int index, ListPopupWindow window) {
                                if (list.get(index).equalsIgnoreCase(ctx.getString(R.string.directions))) {
                                    startDirectionsMap(lat, lng);
                                }
                                if (list.get(index).equalsIgnoreCase(ctx.getString(R.string.get_distance))) {
                                    getDistance(lat, lng);
                                }
                            }
                        });
            }
        }


    }

    boolean isStatusReport, isGallery;

    private void startGallery(AlertDTO alert) {

        Intent i = new Intent(ctx, AlertPictureGridActivity.class);
        i.putExtra("alert", alert);
        i.putExtra("logo", logo);
        startActivity(i);
    }

    static final DecimalFormat df = new DecimalFormat("###,###,###,##0.00");

    private void getDistance(double lat, double lng) {
        location = googleMap.getMyLocation();
        if (location == null) {
            Util.showErrorToast(ctx, getString(R.string.loc_unavailable));
            return;
        }
        DistanceUtil.getDistance(location.getLatitude(), location.getLongitude(), lat, lng, new DistanceUtil.DistanceListener() {
            @Override
            public void onDistanceAcquired(String fromAddress,
                                           String toAddress,
                                           double distance,
                                           int duration) {
                addr1.setText(fromAddress);
                addr2.setText(toAddress);
                dist.setText(df.format(distance));
                dur.setText("" + duration);
                Util.expand(mapInfo, 1000, null);

            }

            @Override
            public void onError(String message) {
                Util.showErrorToast(ctx, message);
            }
        });
    }

    private void startDirectionsMap(double lat, double lng) {
        location = googleMap.getMyLocation();
        if (location == null) {
            Util.showErrorToast(ctx, "My location is not available");
            return;
        }
        Log.i(LOG, "startDirectionsMap ..........");
        String url = "http://maps.google.com/maps?saddr="
                + location.getLatitude() + "," + location.getLongitude()
                + "&daddr=" + lat + "," + lng + "&mode=driving";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(LOG, "################ onStart .... ");

    }

    @Override
    protected void onStop() {
        Log.w(LOG, "############## onStop stopping google service clients");

        super.onStop();
    }


    @Override
    public void onBackPressed() {
        Log.e(LOG, "######## onBackPressed");

        finish();
    }

    @Override
    public void onPause() {
        //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        super.onPause();
    }

    Address address;
    TextView txtAddress;

    class GeoTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            Geocoder geocoder = new Geocoder(ctx);
            try {
                List<Address> list = geocoder.getFromLocation(
                        location.getLatitude(), location.getLongitude(), 1);
                if (list != null && list.size() > 0) {
                    address = list.get(0);
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
                txtAddress.setText(address.toString());
            } else {
                txtAddress.setText(getString(R.string.address_not_found));
            }
        }

    }
}
