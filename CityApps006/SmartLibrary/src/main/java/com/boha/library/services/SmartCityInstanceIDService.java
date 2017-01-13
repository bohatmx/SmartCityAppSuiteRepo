package com.boha.library.services;

import android.os.Build;
import android.util.Log;

import com.boha.library.dto.GcmDeviceDTO;
import com.boha.library.transfer.RequestDTO;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.NetUtil;
import com.boha.library.util.SharedUtil;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;

/**
 * Created by aubreymalabie on 10/8/16.
 */

public class SmartCityInstanceIDService extends FirebaseInstanceIdService {

    public static final String TAG = SmartCityInstanceIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.w(TAG, "onTokenRefresh - Refreshed FCM token: " + refreshedToken);

        saveToken(refreshedToken);
    }

    private void saveToken(String token) {
        Log.w(TAG, ".............saveToken: " + token);

        String oldToken = SharedUtil.getRegistrationID(getApplicationContext());
        SharedUtil.storeRegistrationId(getApplicationContext(), token);

        GcmDeviceDTO gcmDevice = new GcmDeviceDTO();
        gcmDevice.setManufacturer(Build.MANUFACTURER);
        gcmDevice.setModel(Build.MODEL);
        gcmDevice.setSerialNumber(Build.SERIAL);
        gcmDevice.setAndroidVersion(Build.VERSION.RELEASE);
        gcmDevice.setGcmRegistrationID(token);
        SharedUtil.saveGCMDevice(this,gcmDevice);

        if (oldToken != null) {
            sendTokenToServer(oldToken, token);
        }


    }

    /**
     * Persist registration to third-party servers.
     * <p/>
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     */
    private void sendTokenToServer(String oldToken, String newToken) {
        RequestDTO w = new RequestDTO(RequestDTO.UPDATE_FCM_TOKEN);
        w.setOldToken(oldToken);
        w.setNewToken(newToken);

        NetUtil.sendRequest(getApplicationContext(), w, new NetUtil.NetUtilListener() {
            @Override
            public void onResponse(final ResponseDTO response) {
                if (response.getStatusCode() == 0) {
                    Log.w(TAG, "############ Device registered on SmartCity Server FCM regime");
                }
            }

            @Override
            public void onError(final String message) {
                Log.e(TAG, "############ Device failed to register on server GCM regime\n" + message);
            }

            @Override
            public void onWebSocketClose() {
            }
        });

    }

    /**
     * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
     *
     * @param token GCM token
     * @throws IOException if unable to reach the GCM PubSub service
     */
    // [START subscribe_topics]
    private void subscribeTopics(String token) throws IOException {
//
//        if (SharedUtil.getMonitor(getApplicationContext()) != null) {
//            for (String topic : MONITOR_TOPICS) {
//                GcmPubSub pubSub = GcmPubSub.getInstance(this);
//                pubSub.subscribe(token, "/topics/" + topic, null);
//            }
//        }
//        if (SharedUtil.getCompanyStaff(getApplicationContext()) != null) {
//            for (String topic : STAFF_TOPICS) {
//                GcmPubSub pubSub = GcmPubSub.getInstance(this);
//                pubSub.subscribe(token, "/topics/" + topic, null);
//            }
//        }
//        Log.e(LOG, "############ subscribeTopics: Topics on GCM");
//    }
    }
// [END subscribe_topics]


    public static final String MESSAGING = "main";

}
