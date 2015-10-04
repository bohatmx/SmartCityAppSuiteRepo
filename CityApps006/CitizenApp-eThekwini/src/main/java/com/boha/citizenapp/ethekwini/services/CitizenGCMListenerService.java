package com.boha.citizenapp.ethekwini.services;

/**
 * Created by aubreyM on 15/08/16.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.boha.citizenapp.ethekwini.R;
import com.boha.citizenapp.ethekwini.activities.CitizenDrawerActivity;
import com.boha.library.activities.CardPaymentsActivity;
import com.boha.library.activities.SIDPaymentsActivity;
import com.boha.library.dto.CardResponseDTO;
import com.boha.library.dto.SIDResponseDTO;
import com.boha.library.util.CacheUtil;
import com.google.android.gms.gcm.GcmListenerService;
import com.google.gson.Gson;

public class CitizenGCMListenerService extends GcmListenerService {

    private static final Gson GSON = new Gson();
    private static final String TAG = "CitGCMListenerService";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        Log.i(TAG, "######onMessageReceived, data: " + data.toString());
        String message = data.getString("message");
        if (message != null) {
            Log.d(TAG, "** GCM message From: " + from);
            Log.d(TAG, "Message: " + message);
            sendNotification(message);
            return;
        }
        message = data.getString("SIDResponse");
        if (message != null) {
            SIDResponseDTO m = GSON.fromJson(message, SIDResponseDTO.class);
            Log.d(TAG, "** GCM SID response message From: " + from);
            Log.d(TAG, "SIDResponse: " + message);
            CacheUtil.addSIDResponse(getApplicationContext(), m, null);
            sendNotification(m);
            return;
        }
        message = data.getString("cardResponse");
        if (message != null) {
            CardResponseDTO m = GSON.fromJson(message, CardResponseDTO.class);
            Log.d(TAG, "** GCM Card response message From: " + from);
            Log.d(TAG, "cardResponse: " + message);
            CacheUtil.addCardResponse(getApplicationContext(), m, null);
            sendNotification(m);
            return;
        }


    }
    private void sendNotification(CardResponseDTO cardResponse) {
        Intent intent = new Intent(this, CardPaymentsActivity.class);
        intent.putExtra("cardResponse",cardResponse);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                LOCATION_REQUEST_CODE, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Message from Card Payments")
                .setContentText(cardResponse.getOutcome())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
    private void sendNotification(SIDResponseDTO sidResponse) {
        Intent intent = new Intent(this, SIDPaymentsActivity.class);
        intent.putExtra("SIDResponse",sidResponse);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                LOCATION_REQUEST_CODE, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String name = "unknown";
        if (sidResponse.getBank() != null) {
            name = sidResponse.getBank();
        }

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Message from " + name)
                .setContentText(sidResponse.getStatus())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    static final int LOCATION_REQUEST_CODE = 7763;
    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String message) {
        Intent intent = new Intent(this, CitizenDrawerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Incoming message")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}