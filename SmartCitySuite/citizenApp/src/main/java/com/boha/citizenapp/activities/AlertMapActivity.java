package com.boha.citizenapp.activities;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.boha.citizenapp.R;
import com.boha.citylibrary.dto.AlertDTO;
import com.boha.citylibrary.dto.AlertTypeDTO;
import com.boha.citylibrary.transfer.ResponseDTO;
import com.boha.citylibrary.util.Statics;
import com.boha.citylibrary.util.Util;
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

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AlertMapActivity extends ActionBarActivity {

    GoogleMap googleMap;
    GoogleApiClient mGoogleApiClient;
    LocationRequest locationRequest;
    Location location;
    Context ctx;
    DisplayMetrics displayMetrics;
    List<Marker> markers = new ArrayList<Marker>();
    static final String LOG = AlertMapActivity.class.getSimpleName();
    boolean mResolvingError;
    static final long ONE_MINUTE = 1000 * 60;
    static final long FIVE_MINUTES = 1000 * 60 * 5;
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    // Unique tag for the error dialog fragment
    private static final String DIALOG_ERROR = "dialog_error";

    int index;
    TextView text, txtCount;
    View topLayout;
    ProgressBar progressBar;
    LayoutInflater inflater;
    static final Locale loc = Locale.getDefault();
    static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    List<AlertDTO> alertList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.w(LOG, "#### onCreate");
        super.onCreate(savedInstanceState);
        ctx = getApplicationContext();
        setContentView(R.layout.activity_maps);
        inflater = getLayoutInflater();

        ResponseDTO r = (ResponseDTO) getIntent().getSerializableExtra("alertList");
        if (r != null)
            alertList = r.getAlertList();
        alert = (AlertDTO) getIntent().getSerializableExtra("alert");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        text = (TextView) findViewById(R.id.text);
        txtCount = (TextView) findViewById(R.id.count);
        txtCount.setText("0");
        text.setText(getString(R.string.active_alerts));
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        Statics.setRobotoFontBold(ctx, text);

        topLayout = findViewById(R.id.top);

        googleMap = mapFragment.getMap();
        if (googleMap == null) {
            Util.showToast(ctx, getString(R.string.map_not_avail));
            finish();
            return;
        }
        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay()
                .getMetrics(displayMetrics);

        setGoogleMap();
//        MunicipalityDTO municipality = SharedUtil.getMunicipality(ctx);
//        ActionBar actionBar = getSupportActionBar();
//        Util.setCustomActionBar(ctx,
//                actionBar,
//                municipality.getMunicipalityName(),
//                ctx.getResources().getDrawable(com.boha.citizenapp.R.drawable.logo));

    }

    private void setGoogleMap() {
        googleMap.setMyLocationEnabled(true);
        googleMap.setBuildingsEnabled(true);
        location = googleMap.getMyLocation();

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                LatLng latLng = marker.getPosition();
                showPopup(latLng.latitude, latLng.longitude, marker.getTitle());

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

    static final DecimalFormat df = new DecimalFormat("###,##0.00");

    private void setAlertMarkers() {
        googleMap.clear();
        LatLng point = null;
        int index = 0, count = 0, randomIndex = 0;

        for (AlertDTO alert : alertList) {
            if (alert.getLatitude() == null) continue;
            LatLng pnt = new LatLng(alert.getLatitude(), alert.getLongitude());
            point = pnt;
            BitmapDescriptor desc = BitmapDescriptorFactory.fromResource(R.drawable.dot_black);
            Short color = null;
            View dot = null;
            TextView txtNumber;
            if (alert.getAlertType().getColor() != null) {
                switch (alert.getAlertType().getColor()) {
                    case AlertTypeDTO.RED:
                        dot = inflater.inflate(com.boha.citylibrary.R.layout.dot_red, null);
                        txtNumber = (TextView) dot.findViewById(com.boha.citylibrary.R.id.DOT_text);
                        txtNumber.setText("" + (index + 1));
                        desc = BitmapDescriptorFactory.fromBitmap(Util.createBitmapFromView(ctx, dot, displayMetrics));
                        break;
                    case AlertTypeDTO.GREEN:
                        dot = inflater.inflate(com.boha.citylibrary.R.layout.dot_green, null);
                        txtNumber = (TextView) dot.findViewById(com.boha.citylibrary.R.id.DOT_text);
                        txtNumber.setText("" + (index + 1));
                        desc = BitmapDescriptorFactory.fromBitmap(Util.createBitmapFromView(ctx, dot, displayMetrics));
                        break;
                    case AlertTypeDTO.AMBER:
                        dot = inflater.inflate(com.boha.citylibrary.R.layout.dot_amber, null);
                        txtNumber = (TextView) dot.findViewById(com.boha.citylibrary.R.id.DOT_text);
                        txtNumber.setText("" + (index + 1));
                        desc = BitmapDescriptorFactory.fromBitmap(Util.createBitmapFromView(ctx, dot, displayMetrics));
                        break;
                }

            }
            Marker m =
                    googleMap.addMarker(new MarkerOptions()
                            .title("" + alert.getAlertID().intValue())
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
        BitmapDescriptor desc = null;
        View dot;
        TextView txtNumber;
        switch (alert.getAlertType().getColor()) {
            case AlertTypeDTO.RED:
                dot = inflater.inflate(com.boha.citylibrary.R.layout.dot_red, null);
                txtNumber = (TextView) dot.findViewById(com.boha.citylibrary.R.id.DOT_text);
                txtNumber.setText("" + (alert.getIndex() + 1));
                desc = BitmapDescriptorFactory.fromBitmap(Util.createBitmapFromView(ctx, dot, displayMetrics));
                break;
            case AlertTypeDTO.GREEN:
                dot = inflater.inflate(com.boha.citylibrary.R.layout.dot_green, null);
                txtNumber = (TextView) dot.findViewById(com.boha.citylibrary.R.id.DOT_text);
                txtNumber.setText("" + (alert.getIndex() + 1));
                desc = BitmapDescriptorFactory.fromBitmap(Util.createBitmapFromView(ctx, dot, displayMetrics));
                break;
            case AlertTypeDTO.AMBER:
                dot = inflater.inflate(com.boha.citylibrary.R.layout.dot_amber, null);
                txtNumber = (TextView) dot.findViewById(com.boha.citylibrary.R.id.DOT_text);
                txtNumber.setText("" + (alert.getIndex() + 1));
                desc = BitmapDescriptorFactory.fromBitmap(Util.createBitmapFromView(ctx, dot, displayMetrics));
                break;
            default:
                dot = inflater.inflate(com.boha.citylibrary.R.layout.dot_amber, null);
                txtNumber = (TextView) dot.findViewById(com.boha.citylibrary.R.id.DOT_text);
                txtNumber.setText("" + (alert.getIndex() + 1));
                desc = BitmapDescriptorFactory.fromBitmap(Util.createBitmapFromView(ctx, dot, displayMetrics));
                break;
        }
        Marker m =
                googleMap.addMarker(new MarkerOptions()
                        .title(alert.getAlertType().getAlertTypeName())
                        .icon(desc)
                        .snippet(alert.getDescription())
                        .position(pnt));
        markers.add(m);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pnt, 1.0f));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(12.0f));
    }

    List<String> list;

    private void showPopup(final double lat, final double lng, final String title) {
        list = new ArrayList<>();
        list.add(getString(R.string.directions));
        list.add(getString(R.string.pictures));

        Util.showPopupBasicWithHeroImage(ctx, this, list, topLayout,
                "Actions",
                new Util.UtilPopupListener() {
                    @Override
                    public void onItemSelected(int index) {
                        if (list.get(index).equalsIgnoreCase(ctx.getString(R.string.directions))) {
                            startDirectionsMap(lat, lng);
                        }

                        if (list.get(index).equalsIgnoreCase(ctx.getString(R.string.pictures))) {
                            isGallery = true;
                            Integer id = Integer.parseInt(title);
                            int j = 0;
                            for (AlertDTO a : alertList) {
                                if (a.getAlertID().intValue() == id.intValue()) {
                                    break;
                                }
                                j++;
                            }

                            startGallery(alertList.get(j));
                        }
                    }
                });


    }

    boolean isStatusReport, isGallery;

    private void startGallery(AlertDTO alert) {


        Intent i = new Intent(ctx, AlertPictureGridActivity.class);
        i.putExtra("alert", alert);
        startActivity(i);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
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
