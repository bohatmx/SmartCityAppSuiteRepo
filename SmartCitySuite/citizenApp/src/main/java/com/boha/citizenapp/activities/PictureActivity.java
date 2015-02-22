package com.boha.citizenapp.activities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boha.citizenapp.R;
import com.boha.citylibrary.dto.AlertDTO;
import com.boha.citylibrary.services.PhotoUploadService;
import com.boha.citylibrary.transfer.PhotoUploadDTO;
import com.boha.citylibrary.transfer.ResponseDTO;
import com.boha.citylibrary.util.ImageUtil;
import com.boha.citylibrary.util.PhotoCacheUtil;
import com.boha.citylibrary.util.Util;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aubreyM on 2014/04/21.
 */
public class PictureActivity extends ActionBarActivity implements LocationListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    LocationRequest mLocationRequest;
    GoogleApiClient googleApiClient;
    LinearLayout imageContainerLayout;
    LayoutInflater inflater;
    TextView txtAlertType;
    View projectLayout;
    AlertDTO alert;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG, "### onCreate............");
        ctx = getApplicationContext();
        inflater = getLayoutInflater();
        setContentView(R.layout.camera);
        setFields();


        alert = (AlertDTO) getIntent().getSerializableExtra("alert");
        txtAlertType.setText(alert.getAlertType().getAlertTypeNmae());
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        setTitle(getString(R.string.alert_pics));
        Util.showToast(ctx, "Please take pictures for the alert");
        dispatchTakePictureIntent();

    }

    boolean mRequestingLocationUpdates;

    @Override
    public void onResume() {
        Log.d(LOG, "@@@ onResume...........");
        super.onResume();
        if (googleApiClient.isConnected() && !mRequestingLocationUpdates) {
            startLocationUpdates();
        }

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.e(LOG, "%%%%%%%%%%%% onRestoreInstanceState" + savedInstanceState);
        type = savedInstanceState.getInt("type", 0);
        alert = (AlertDTO) savedInstanceState.getSerializable("alert");
        String path = savedInstanceState.getString("photoFile");
        if (path != null) {
            photoFile = new File(path);
        }
        double lat = savedInstanceState.getDouble("latitude");
        double lng = savedInstanceState.getDouble("longitude");
        float acc = savedInstanceState.getFloat("accuracy");
        location = new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(lat);
        location.setLongitude(lng);
        location.setAccuracy(acc);
        Log.w(LOG, "### saved location accuracy: " + acc);
        super.onRestoreInstanceState(savedInstanceState);
    }

    ImageView imgCamera;
    Button btnStart;


    private void setFields() {
        activity = this;
        txtAlertType = (TextView) findViewById(R.id.CAM_alertTypeName);
        projectLayout = findViewById(R.id.CAM_typeLayout);
        imageContainerLayout = (LinearLayout) findViewById(R.id.CAM_imageContainer);

        imgCamera = (ImageView) findViewById(R.id.CAM_imgCamera);
        imgCamera.setVisibility(View.GONE);


        imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(imgCamera, 200, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {

                        dispatchTakePictureIntent();
                    }
                });

            }
        });
        imageContainerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode,
                                 final Intent data) {
        Log.e(LOG, "##### onActivityResult requestCode: " + requestCode + " resultCode: " + resultCode);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        switch (requestCode) {
            case CAPTURE_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                    if (resultCode == Activity.RESULT_OK) {
                        if (photoFile != null) {
                            Log.e(LOG, "++ hopefully photo file has a length: " + photoFile.length());

                            new PhotoTask().execute();
                        }
                    }
                    pictureChanged = true;

                }
                break;
            case REQUEST_VIDEO_CAPTURE:

                break;
        }
    }

    @Override
    public void onLocationChanged(Location loc) {
        Log.d(LOG, "## onLocationChanged accuracy = " + loc.getAccuracy());

        if (this.location == null) {
            this.location = loc;
        }
        if (loc.getAccuracy() <= ACCURACY_THRESHOLD) {
            this.location = loc;
        }
    }

    private void uploadPhotos() {
        Log.e(LOG, "### uploadPhotos, the accuracy: " + location.getAccuracy());
        mService.uploadCachedPhotos(new PhotoUploadService.UploadListener() {
            @Override
            public void onUploadsComplete(int count) {
                Log.e(LOG, "### onUploadsComplete: " + count);
            }
        });

    }

    @Override
    public void onStart() {
        Log.i(LOG,
                "## onStart - locationClient connecting ... ");
        if (googleApiClient != null) {
            if (location == null) {
                googleApiClient.connect();
            }
        }
        Log.i(LOG, "## onStart Bind to PhotoUploadService");
        Intent intent = new Intent(this, PhotoUploadService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();

        if (googleApiClient != null) {
            googleApiClient.disconnect();
            Log.e(LOG, "### onStop - locationClient disconnecting ");
        }
        Log.e(LOG, "## onStop unBind from PhotoUploadService");
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }

    }

    Location location;
    static final float ACCURACY_THRESHOLD = 25;
    ActionBarActivity activity;

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(LOG,
                "+++  onConnected() -  requestLocationUpdates ...");
        location = LocationServices.FusedLocationApi.getLastLocation(
                googleApiClient);
        if (location != null) {
        }

        Log.w(LOG, "## requesting location updates ....");
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setFastestInterval(2000);
        startLocationUpdates();
//

    }

    protected void startLocationUpdates() {
        if (googleApiClient.isConnected()) {
            mRequestingLocationUpdates = true;
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    googleApiClient, mLocationRequest, this);
        }
    }

    protected void stopLocationUpdates() {
        if (googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    googleApiClient, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
//        ACRA.getErrorReporter().handleSilentException(new PMException(
//                "Google LocationClient onConnectionFailed: " + connectionResult.getErrorCode()));
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.e(LOG, "Fuck!", ex);
                Util.showErrorToast(ctx, getString(R.string.photo_err));
                return;
            }

            if (photoFile != null) {
                Log.w(LOG, "dispatchTakePictureIntent - start pic intent");
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, CAPTURE_IMAGE);
            }
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name

        String imageFileName = "pic" + System.currentTimeMillis();
        switch (type) {

        }
        File root;
        if (Util.hasStorage(true)) {
            Log.i(LOG, "###### get file from getExternalStoragePublicDirectory");
            root = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES);
        } else {
            Log.i(LOG, "###### get file from getDataDirectory");
            root = Environment.getDataDirectory();
        }
        File pics = new File(root, "monitor_app");
        if (!pics.exists()) {
            pics.mkdir();
        }

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                pics      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // getMenuInflater().inflate(R.menu.camera, menu);
        mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

//        if (item.getItemId() == R.id.menu_video) {
//            dispatchTakeVideoIntent();
//            return true;
//        }
//        if (item.getItemId() == R.id.menu_gallery) {
//            //Intent i = new Intent(this, PictureRecyclerGridActivity.class);
//            //startActivity(i);
//            Util.showToast(ctx, ctx.getString(R.string.under_cons));
//            return true;
//        }
//        if (item.getItemId() == R.id.menu_camera) {
//            dispatchTakePictureIntent();
//            return true;
//        }

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    public void onBackPressed() {

        if (isUploaded) {
            Log.d(LOG, "onBackPressed ... picture uploaded");
            ResponseDTO r = new ResponseDTO();
            //r.setSiteImageFileNameList(currentSessionPhotos);
            Intent i = new Intent();
            i.putExtra("response", r);
            setResult(RESULT_OK, i);
        } else {
            Log.d(LOG, "onBackPressed ... cancelled");
            setResult(RESULT_CANCELED);
        }
        finish();
    }

    @Override
    public void onSaveInstanceState(Bundle b) {
        Log.e(LOG, "############################## onSaveInstanceState");
        b.putInt("type", type);
        if (currentThumbFile != null) {
            b.putString("thumbPath", currentThumbFile.getAbsolutePath());
        }
        if (photoFile != null) {
            b.putString("photoFile", photoFile.getAbsolutePath());
        }

        if (location != null) {
            b.putDouble("latitude", location.getLatitude());
            b.putDouble("longitude", location.getLongitude());
            b.putFloat("accuracy", location.getAccuracy());
        }

        super.onSaveInstanceState(b);
    }


    class PhotoTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            Log.w(LOG, "## PhotoTask starting doInBackground, file length: " + photoFile.length());
            pictureChanged = false;
            ExifInterface exif = null;
            if (photoFile == null || photoFile.length() == 0) {
                Log.e(LOG, "----- photoFile is null or length 0, exiting");
                return 99;
            }
            fileUri = Uri.fromFile(photoFile);
            if (fileUri != null) {
                try {
                    exif = new ExifInterface(photoFile.getAbsolutePath());
                    String orient = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
                    Log.i(LOG, "@@@@@@@@@@@@@@@@@@@@@@ Orientation says: " + orient);
                    float rotate = 0f;
                    if (orient.equalsIgnoreCase("6")) {
                        rotate = 90f;
                        Log.i(LOG, "@@@@@ picture, rotate = " + rotate);
                    }
                    try {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 2;
                        Bitmap bm = BitmapFactory.decodeFile(photoFile.getAbsolutePath(), options);
                        getLog(bm, "Raw Camera- sample size = 2");
                        Matrix matrixThumbnail = new Matrix();
                        matrixThumbnail.postScale(0.4f, 0.4f);
                        Bitmap thumb = Bitmap.createBitmap
                                (bm, 0, 0, bm.getWidth(),
                                        bm.getHeight(), matrixThumbnail, true);
                        getLog(thumb, "Thumb");

                        //append date and gps coords to bitmap
                        //fullBm = ImageUtil.drawTextToBitmap(ctx,fullBm,location);
                        //thumb = ImageUtil.drawTextToBitmap(ctx,thumb,location);

                        currentThumbFile = ImageUtil.getFileFromBitmap(thumb, "t" + System.currentTimeMillis() + ".jpg");
                        bitmapForScreen = ImageUtil.getBitmapFromUri(ctx, Uri.fromFile(currentThumbFile));

                        thumbUri = Uri.fromFile(currentThumbFile);
                        //write exif data
//                        Util.writeLocationToExif(currentThumbFile.getAbsolutePath(), location);
                        boolean del = photoFile.delete();
                        Log.i(LOG, "## Thumbnail file length: " + currentThumbFile.length()
                                + " main image file deleted: " + del);
                    } catch (Exception e) {
                        Log.e(LOG, "$&*%$! Fuck it! unable to process bitmap", e);
                        return 9;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    return 1;
                }

            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (result > 0) {
                Util.showErrorToast(ctx, ctx.getResources().getString(R.string.photo_err));
                return;
            }
            if (thumbUri != null) {
                pictureChanged = true;
                try {
                    isUploaded = true;
                    currentSessionPhotos.add(Uri.fromFile(currentThumbFile).toString());
                    addImageToScroller();
                    final PhotoUploadDTO p = new PhotoUploadDTO();
//                    p.setAlertID(alert.getAlertID());
//                    p.setAlertTypeName(alert.getAlertType().getAlertTypeNmae());
//                    p.setCityID(alert.getCityID());
//                    p.setImageFilePath(currentThumbFile.getAbsolutePath());
//                    p.setThumbFilePath(currentThumbFile.getAbsolutePath());
//                    p.setLatitude(location.getLatitude());
//                    p.setLongitude(location.getLongitude());
//                    p.setTime(new Date().getTime());
//                    p.setDateTaken(new Date());

                    PhotoCacheUtil.cachePhoto(ctx, p, new PhotoCacheUtil.PhotoCacheListener() {
                        @Override
                        public void onFileDataDeserialized(ResponseDTO response) {

                        }

                        @Override
                        public void onDataCached() {
//                            Log.i(LOG, "### photo cached OK for alertID: " + p.getAlertID()
//                                    + " type: " + alert.getAlertType().getAlertTypeNmae());
                        }

                        @Override
                        public void onError() {

                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void addImageToScroller() {
        Log.i(LOG, "## addImageToScroller");
        if (currentSessionPhotos.size() == 1) {
            imageContainerLayout.removeAllViews();
        }
        View v = inflater.inflate(R.layout.scroller_image_template, null);
        ImageView img = (ImageView) v.findViewById(R.id.image);
        TextView num = (TextView) v.findViewById(R.id.number);
        num.setText("" + currentSessionPhotos.size());
        Uri uri = Uri.fromFile(currentThumbFile);
        ImageLoader.getInstance().displayImage(uri.toString(), img);
        imageContainerLayout.addView(v, 0);

        imgCamera.setVisibility(View.VISIBLE);
        uploadPhotos();


    }

    List<String> currentSessionPhotos = new ArrayList<>();

    private void getLog(Bitmap bm, String which) {
        if (bm == null) return;
        Log.e(LOG, which + " - bitmap: width: "
                + bm.getWidth() + " height: "
                + bm.getHeight() + " rowBytes: "
                + bm.getRowBytes());
    }


    boolean mBound;
    PhotoUploadService mService;

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            Log.w(LOG, "## PhotoUploadService ServiceConnection onServiceConnected");
            PhotoUploadService.LocalBinder binder = (PhotoUploadService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
            mService.uploadCachedPhotos(new PhotoUploadService.UploadListener() {
                @Override
                public void onUploadsComplete(int count) {
                    Log.w(LOG, "$$$ onUploadsComplete, list: " + count);
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.w(LOG, "## PhotoUploadService onServiceDisconnected");
            mBound = false;
        }
    };

    String mCurrentPhotoPath;

    File photoFile;
    boolean isUploaded;
    static final int REQUEST_VIDEO_CAPTURE = 1;

    File currentThumbFile;
    Uri thumbUri;
    static final String LOG = PictureActivity.class.getSimpleName();

    Menu mMenu;
    int type;

    boolean pictureChanged;
    Context ctx;
    Uri fileUri;
    public static final int CAPTURE_IMAGE = 9908;

    Bitmap bitmapForScreen;


    private interface CacheListener {
        public void onCachingDone();
    }


    public void addAlertPicture(final CacheListener listener) {
        Log.w(LOG, "**** addProjectPicture");
        final PhotoUploadDTO dto = getObject();
//        dto.setAlertID(alert.getAlertID());
//        dto.setThumbFilePath(currentThumbFile.getAbsolutePath());

        PhotoCacheUtil.cachePhoto(ctx, dto, new PhotoCacheUtil.PhotoCacheListener() {
            @Override
            public void onFileDataDeserialized(ResponseDTO response) {

            }

            @Override
            public void onDataCached() {
                Log.w(LOG, "### photo has been cached");
                listener.onCachingDone();
            }

            @Override
            public void onError() {
                Util.showErrorToast(ctx, getString(R.string.photo_err));
            }
        });
    }

    private PhotoUploadDTO getObject() {
        PhotoUploadDTO dto = new PhotoUploadDTO();
//        dto.setAlertID(alert.getAlertID());
//        dto.setThumbFilePath(currentThumbFile.getAbsolutePath());
//        dto.setDateTaken(new Date());
//        dto.setLatitude(location.getLatitude());
//        dto.setLongitude(location.getLongitude());
//        dto.setAccuracy(location.getAccuracy());
//        dto.setTime(new Date().getTime());
        return dto;
    }


}