package com.boha.citizenapp.ethekwini.services;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.boha.citizenapp.ethekwini.R;
import com.boha.citizenapp.ethekwini.activities.MainDrawerActivity;
import com.boha.library.dto.AlertDTO;
import com.boha.library.util.GCMUtil;
import com.boha.library.util.SharedUtil;
import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;

public class GcmIntentService extends GCMBaseIntentService {
	public static final int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;

	public GcmIntentService() {
		super(GCMUtil.GCM_SENDER_ID);
	}

	@Override
	protected void onError(Context arg0, String arg1) {
		Log.i(TAG, "onError ... " + arg1);

	}

	@Override
	protected void onMessage(Context arg0, Intent intent) {
		Log.w(TAG, "onMessage ..:..gcm message here... " + intent.getExtras().toString());
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		String messageType = gcm.getMessageType(intent);
		Log.d(TAG, "GCM messageType = " + messageType);
		if (!extras.isEmpty()) { 
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
					.equals(messageType)) {
				Log.e(TAG, "GoogleCloudMessaging - MESSAGE_TYPE_SEND_ERROR");
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
					.equals(messageType)) {
				Log.e(TAG, "GoogleCloudMessaging - MESSAGE_TYPE_SEND_ERROR");
				
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
					.equals(messageType)) {
				sendNotification(intent);
			}
		}
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		GcmBroadcastReceiver.completeWakefulIntent(intent);

	}

	@Override
	protected void onRegistered(Context arg0, String arg1) {
		Log.i(TAG, "onRegistered ... " + arg1);

	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {
		Log.i(TAG, "onUnRegistered ... " + arg1);
	}
	Gson gson = new Gson();
	private void sendNotification(Intent msgIntent) {
		
		mNotificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);
		String message = msgIntent.getExtras().getString("message");

		Intent resultIntent = new Intent(this, MainDrawerActivity.class);
		resultIntent.putExtra("message", message);
		


        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainDrawerActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);
		AlertDTO alert = null;
		try {
			alert = gson.fromJson(message,AlertDTO.class);
			message = alert.getDescription();
		} catch (Exception e) {}


		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setContentIntent(resultPendingIntent)
//                .addAction(R.drawable.logo, SharedUtil.getMunicipality(getApplicationContext()).getMunicipalityName(), resultPendingIntent)
                .setSmallIcon(R.drawable.logo)
				.setContentTitle("Message from " + SharedUtil.getMunicipality(getApplicationContext()).getMunicipalityName())
				.setContentText(message);

		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
		Log.e(TAG,"Notification sent: " + message);
	}
	
	static final String TAG = GcmIntentService.class.getSimpleName();

}