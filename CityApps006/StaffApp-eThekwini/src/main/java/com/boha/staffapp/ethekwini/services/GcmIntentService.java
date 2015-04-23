package com.boha.staffapp.ethekwini.services;


import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.boha.library.util.GCMUtil;
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
				//It's a regular GCM message, do some work.
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
		
//		mNotificationManager = (NotificationManager) this
//				.getSystemService(Context.NOTIFICATION_SERVICE);
//		String message = msgIntent.getExtras().getString("message");
//
//		Intent resultIntent = new Intent(this, MainDrawerActivity.class);
//		resultIntent.putExtra("notifier", message);
//
//
//
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//        stackBuilder.addParentStack(MainDrawerActivity.class);
//        stackBuilder.addNextIntent(resultIntent);
//
//        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//
//		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
//                .setContentIntent(resultPendingIntent)
//                .addAction(R.drawable.ic_plusone_medium_off_client, "More", resultPendingIntent)
//                .setSmallIcon(R.drawable.common_ic_googleplayservices)
//				.setContentTitle("Message")
//				.setContentText("contentText");
//
//		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	}
	
	static final String TAG = "GcmIntentService";

}