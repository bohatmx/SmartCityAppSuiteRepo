package com.boha.library.services;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import com.boha.library.util.Statics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class AddressIntentService extends IntentService {

    public AddressIntentService() {
        super("AddressIntentService");
    }
         public static final String TAG = AddressIntentService.class.getSimpleName();
    @Override
    protected void onHandleIntent(Intent intent) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String errorMessage = "";

        // Get the location passed to this service through an extra.
        Location location = intent.getParcelableExtra(
                Statics.LOCATION_DATA_EXTRA);

        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    1);
        } catch (IOException ioException) {
            errorMessage = "Service not available";
            Log.e(TAG, errorMessage, ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            errorMessage = "Invalid coordinates";
            Log.e(TAG, errorMessage + ". " +
                    "Latitude = " + location.getLatitude() +
                    ", Longitude = " +
                    location.getLongitude(), illegalArgumentException);
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size()  == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = "Address not found";
                Log.e(TAG, errorMessage);
            }
            deliverResultToReceiver(Statics.FAILURE_RESULT, errorMessage);
        } else {
            Address address = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<>();
            StringBuilder sb = new StringBuilder();
            sb.append("############# Geocoded Address\n");
            for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
                sb.append(address.getAddressLine(i)).append("\n");
            }
            sb.append("###################################");
            Log.i(TAG, "Address found: \n" + sb.toString());
            deliverResultToReceiver(Statics.SUCCESS_RESULT,
                    TextUtils.join(System.getProperty("line.separator"),
                            addressFragments));
        }
    }
    protected ResultReceiver mReceiver;

    private void deliverResultToReceiver(int resultCode, String message) {
        Log.d(TAG, "deliverResultToReceiver: " + message);
        mReceiver = new ResultReceiver(new Handler());
        Bundle bundle = new Bundle();
        bundle.putString(Statics.RESULT_DATA_KEY, message);
        mReceiver.send(resultCode, bundle);
    }
}



