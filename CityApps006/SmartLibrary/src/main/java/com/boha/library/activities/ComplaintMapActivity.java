package com.boha.library.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
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
import android.widget.Toast;

import com.boha.library.R;
import com.boha.library.dto.ComplaintDTO;
import com.boha.library.dto.ComplaintTypeDTO;
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
import com.google.android.gms.maps.OnMapReadyCallback;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ComplaintMapActivity extends AppCompatActivity implements OnMapReadyCallback/*,
        GoogleMap.OnInfoWindowClickListener*/{

    GoogleMap googleMap;
    GoogleApiClient mGoogleApiClient;
    LocationRequest locationRequest;
    Location location;
    Context ctx;
    DisplayMetrics displayMetrics;
    List<Marker> markers = new ArrayList<Marker>();
    static final String LOG = ComplaintMapActivity.class.getSimpleName();
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
    TextView addr1, addr2, dist, dur, txtTitle;
    LayoutInflater inflater;
    static final Locale loc = Locale.getDefault();
    static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    List<ComplaintDTO> complaintList;
    ComplaintDTO complaint;
    Activity activity;
    int logo, primaryColorDark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.w(LOG, "#### onCreate");
        super.onCreate(savedInstanceState);
        ctx = getApplicationContext();
        activity = this;
        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay()
                .getMetrics(displayMetrics);

        ThemeChooser.setTheme(this);
        setContentView(R.layout.activity_maps);
        inflater = getLayoutInflater();
        setFields();
        ResponseDTO r = (ResponseDTO) getIntent().getSerializableExtra("complaintList");
        primaryColorDark = getIntent().getIntExtra("primaryColorDark", R.color.blue_gray_800);
        if (r != null) {
            complaintList = r.getComplaintList();
            txtCount.setText("" + complaintList.size());
        }
        complaint = (ComplaintDTO) getIntent().getSerializableExtra("complaint");
        if (complaint != null) {
            txtCount.setText("1");
            text.setText(complaint.getReferenceNumber());
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.MAP_map);
        mapFragment.getMapAsync(this);



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
        //Track ComplaintMapActivity
//        CityApplication ca = (CityApplication) getApplication();
//        Tracker t = ca.getTracker(
//                CityApplication.TrackerName.APP_TRACKER);
//        t.setScreenName(ComplaintMapActivity.class.getSimpleName());
//        t.send(new HitBuilders.ScreenViewBuilder().build());
        //
    }

    Marker marker;
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        //googleMap.setOnInfoWindowClickListener(this);
        setGoogleMap();
    }

    public static final int REQ_PERMISSION = 114;
    private void setGoogleMap() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(LOG, "onConnected: Requesting location permission");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQ_PERMISSION);
            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.setBuildingsEnabled(true);
        location = googleMap.getMyLocation();

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker m) {
                Log.i(LOG, "onMarkerClicked");
              //  complaint = markerMap.get(m);
                marker = m;
                LatLng latLng = marker.getPosition();


              /*  Integer id = Integer.parseInt(marker.getTitle());
                if (complaintList != null) {
                    for (ComplaintDTO x : complaintList) {
                        if (x.getComplaintID().intValue() == id.intValue()) {
                            complaint = x;
                            break;
                        }
                    }
                }*/
                showPopup(latLng.latitude, latLng.longitude/*, marker.getTitle()*/);

                return true;
            }
        });
        if (complaintList != null) {
            setComplaintMarkers();
        }
        if (complaint != null) {
            setOneMarker();
        }

    }

    /*private GoogleMap.InfoWindowAdapter infoWindowAdapter = new GoogleMap.InfoWindowAdapter() {
        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        int position;
        @Override
        public View getInfoContents(Marker marker) {
           // position = complaintList.size();
           final ComplaintDTO c = complaintList.get(3);
            View v = getLayoutInflater().inflate(R.layout.complaint_map_info_window, null);
            TextView number  = (TextView) v.findViewById(R.id.CI_reference);
            TextView referenceLabel = (TextView) v.findViewById(R.id.CI_referenceLabel);
            TextView date = (TextView) v.findViewById(R.id.CI_date);
            TextView complaintName = (TextView) v.findViewById(R.id.complaint);
            TextView complaintType = (TextView) v.findViewById(R.id.type);
            complaintType.setVisibility(View.GONE);

            number.setText(c.getReferenceNumber());
            complaintName.setText(c.getCategory() + "-" + c.getSubCategory());
           // complaintType.setText(c.getSubCategory());
            date.setText(sdfDate.format(new Date(c.getComplaintDate())));
            if (c.getLatitude() != null){
            Log.i(LOG, "LATITUDE: " + c.getLatitude() + "LONGITUDE: " + c.getLongitude());
            showPopup(c.getLatitude(), c.getLongitude());
            }
          *//*  final ImageView direction = (ImageView) v.findViewById(R.id.direction);
            final ImageView picture = (ImageView) v.findViewById(R.id.picture);
            final ImageView distance = (ImageView) v.findViewById(R.id.distance);


            direction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.flashOnce(txtCount, 300, new Util.UtilAnimationListener() {
                        @Override
                        public void onAnimationEnded() {

                            startDirectionsMap(c.getLatitude(), c.getLongitude());
                        }
                    });
                }
            });

            picture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.flashOnce(picture, 300, new Util.UtilAnimationListener() {
                        @Override
                        public void onAnimationEnded() {

                        }
                    });
                }
            });

            distance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.flashOnce(distance, 300, new Util.UtilAnimationListener() {
                        @Override
                        public void onAnimationEnded() {

                        }
                    });
                }
            });
*//*
            return  v;
        }
    };*/
    static final SimpleDateFormat sdfDate = new SimpleDateFormat("EEEE dd MMMM yyyy HH:mm", loc);

    private void setComplaintMarkers() {
        googleMap.clear();
        LatLng point = null;
        int index = 0;

        for (ComplaintDTO comp : complaintList) {
            if (comp.getLatitude() == null) continue;
            LatLng pnt = new LatLng(comp.getLatitude(), comp.getLongitude());
            point = pnt;
            BitmapDescriptor desc = null;
            Short color = null;
            View dot = inflater.inflate(R.layout.dot_red, null);
            TextView txtNumber = (TextView) dot.findViewById(R.id.DOT_text);
            txtNumber.setText("" + (index + 1));
            //desc = BitmapDescriptorFactory.fromResource(R.drawable.ic_place_black_48dp);
            desc = BitmapDescriptorFactory.fromBitmap(Util.createBitmapFromView(ctx, dot, displayMetrics));

            Marker m =
                    googleMap.addMarker(new MarkerOptions()
                            .title("" + comp.getReferenceNumber())
                            .icon(desc)
                            .position(pnt));
            markers.add(m);
            index++;

        }
        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                //ensure that all markers in bounds

                if (markers != null && !markers.isEmpty()) {
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    for (Marker marker : markers) {
                        builder.include(marker.getPosition());
                    }

                    LatLngBounds bounds = builder.build();
                    int padding = 60; // offset from edges of the map in pixels
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

                    txtCount.setText("" + markers.size());
//                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 1.0f));
                    googleMap.animateCamera(cu);
                }
            }
        });

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                return false;
            }
        });

    }


    private void setOneMarker() {
        if (complaint.getLatitude() == null) {
            return;
        }
        LatLng pnt = new LatLng(complaint.getLatitude(), complaint.getLongitude());
        BitmapDescriptor desc = null;
        View dot;
        TextView txtNumber;
        switch (complaint.getComplaintType().getColor()) {
            case ComplaintTypeDTO.RED:
                dot = inflater.inflate(R.layout.dot_red, null);
                txtNumber = (TextView) dot.findViewById(R.id.DOT_text);
                txtNumber.setText("" + (complaint.getIndex() + 1));
                desc = BitmapDescriptorFactory.fromBitmap(Util.createBitmapFromView(ctx, dot, displayMetrics));
                break;
            case ComplaintTypeDTO.GREEN:
                dot = inflater.inflate(R.layout.dot_green, null);
                txtNumber = (TextView) dot.findViewById(R.id.DOT_text);
                txtNumber.setText("" + (complaint.getIndex() + 1));
                desc = BitmapDescriptorFactory.fromBitmap(Util.createBitmapFromView(ctx, dot, displayMetrics));
                break;
            case ComplaintTypeDTO.AMBER:
                dot = inflater.inflate(R.layout.dot_amber, null);
                txtNumber = (TextView) dot.findViewById(R.id.DOT_text);
                txtNumber.setText("" + (complaint.getIndex() + 1));
                desc = BitmapDescriptorFactory.fromBitmap(Util.createBitmapFromView(ctx, dot, displayMetrics));
                break;
            default:
                dot = inflater.inflate(R.layout.dot_amber, null);
                txtNumber = (TextView) dot.findViewById(R.id.DOT_text);
                txtNumber.setText("" + (complaint.getIndex() + 1));
                desc = BitmapDescriptorFactory.fromBitmap(Util.createBitmapFromView(ctx, dot, displayMetrics));
                break;
        }
        Marker m =
                googleMap.addMarker(new MarkerOptions()
                        .title("" + complaint.getComplaintID().intValue())
                        .icon(desc)
//                        .snippet(complaint.getRemarks())
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
        text.setText(getString(R.string.complaints_around_me));
        Statics.setRobotoFontLight(ctx, text);
        mapInfo = findViewById(R.id.MAP_info);
        mapInfo.setVisibility(View.GONE);
        addr1 = (TextView) mapInfo.findViewById(R.id.MAP_addressFrom);
        addr2 = (TextView) mapInfo.findViewById(R.id.MAP_addressTo);
        dist = (TextView) mapInfo.findViewById(R.id.MAP_distance);
        dur = (TextView) mapInfo.findViewById(R.id.MAP_duration);
        txtTitle = (TextView) findViewById(R.id.MAP_text);
        txtTitle.setText(getString(R.string.map_complaints));
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

    private void showPopup(final double lat, final double lng/*, final String title*/) {
        list = new ArrayList<>();
        list.add(getString(R.string.directions));
        list.add(getString(R.string.compl_pics));
        list.add(getString(R.string.get_distance));
        ImageView dummy = new ImageView(ctx);
        if (complaint != null) {
            if (complaint.getComplaintImageList() != null && !complaint.getComplaintImageList().isEmpty()) {
                String url = complaint.getComplaintImageList().get(0).getUrl();
                Picasso.with(ctx).load(url).into(dummy);

            } else {
                list.remove(1);
                Util.showPopupList(ctx, activity, list, topLayout, complaint.getComplaintType().getComplaintTypeName(),
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

    private void startGallery(ComplaintDTO complaint) {


        Intent i = new Intent(ctx, AlertPictureGridActivity.class);
        i.putExtra("complaint", complaint);
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
