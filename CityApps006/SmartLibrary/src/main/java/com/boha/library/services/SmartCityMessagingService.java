package com.boha.library.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.boha.library.R;
import com.boha.library.activities.NotificationActivity;
import com.boha.library.dto.ClientMessageDTO;
import com.boha.library.dto.DistrictMessageDTO;
import com.boha.library.dto.MessageInterface;
import com.boha.library.dto.SuburbMessageDTO;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.util.Map;

import static android.R.attr.data;

public class SmartCityMessagingService extends FirebaseMessagingService {
    public SmartCityMessagingService() {
    }

    public static final String TAG = SmartCityMessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "=========================================\n" +
                "onMessageReceived: from: " + remoteMessage.getFrom()
                + " to:" + remoteMessage.getTo() + " collapseKey: " + remoteMessage.getCollapseKey());
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
//
        try {
            Map<String, String> map = remoteMessage.getData();
            if (map.get("districtName") != null) {
                DistrictMessageDTO dm = new DistrictMessageDTO();
                dm.setDistrictName(map.get("districtName"));
                dm.setMessage(map.get("message"));
                dm.setMessageDate(Long.parseLong(map.get("messageDate")));
              //  dm.setMessageExpiryDate(Long.parseLong(map.get("messageExpiryDate")));
                sendNotification((MessageInterface) dm);
            }
            if (map.get("suburbName") != null) {
                SuburbMessageDTO sm = new SuburbMessageDTO();
                sm.setSuburbID(Integer.parseInt(map.get("suburbID")));
                sm.setSuburbName(map.get("suburbName"));
                sm.setMessage(map.get("message"));
                sm.setMessageDate(Long.parseLong(map.get("messageDate")));
             //   sm.setMessageExpiryDate(Long.parseLong(map.get("messageExpiryDate")));
                sendNotification((MessageInterface) sm);
            }
            if (map.get("message"/*"email"*/) != null) {
                ClientMessageDTO dm = new ClientMessageDTO();
                dm.setMessage(map.get("message"));
                dm.setEmail(map.get("email"));
                dm.setProfileInfoID(Integer.parseInt(map.get("profileInfoID")));
                dm.setMessageDate(Long.parseLong(map.get("messageDate")));
           //     dm.setMessageExpiryDate(Long.parseLong(map.get("messageExpiryDate")));


                sendNotification((MessageInterface) dm);
            }

            Log.d(TAG, "onMessageReceived: " + gson.toJson(data));



        } catch (Exception e) {
            Log.e(TAG, "onMessageReceived: ", e);
        }
    }

    private void sendNotification(MessageInterface  message) {

        StringBuilder sb = new StringBuilder();
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_action_bell)
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setContentTitle(message.getTitle())
                        .setContentText(message.getMessage());

        Intent resultIntent = new Intent(this, NotificationActivity.class);
        if (message instanceof DistrictMessageDTO) {
            resultIntent.putExtra("districtMessage", (DistrictMessageDTO)message);
        }
        if (message instanceof SuburbMessageDTO) {
            resultIntent.putExtra("suburbMessage", (SuburbMessageDTO)message);
        }
        if (message instanceof ClientMessageDTO) {
            resultIntent.putExtra("clientMessage", (ClientMessageDTO)message);
        }

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        getApplicationContext(),
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        int mNotificationId = 001;
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
        Log.e(TAG, "sendNotification: notification has been sent");


    }

    static final Gson gson = new Gson();
}
