package com.boha.library.util;

/**
 * Created by aubreyM on 2014/10/12.
 */

/**
 * Created by aubreyM on 2014/05/11.
 */

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.boha.library.transfer.RequestDTO;
import com.boha.library.transfer.ResponseDTO;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;


public class GCMUtil {
    public interface  GCMUtilListener {
        public void onDeviceRegistered(String id);
        public void onGCMError();
    }
    static Context ctx;
    static GCMUtilListener gcmUtilListener;
    static String registrationID, msg;
    static final String LOG = "GCMUtil";
    static GoogleCloudMessaging gcm;

    public static void startGCMRegistration(Context context, GCMUtilListener listener) {
        ctx = context;
        gcmUtilListener = listener;
        new GCMTask().execute();
    }
    public static final String GCM_SENDER_ID = "635788281460";
    static Integer ret = Integer.valueOf(0);

    static class GCMTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            Log.d(LOG, "... startin GCM registration");
            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(ctx);
                }
                registrationID = gcm.register(GCM_SENDER_ID);
                msg = "Device registered, registration ID = \n" + registrationID;

                RequestDTO w = new RequestDTO(RequestDTO.SEND_GCM_REGISTRATION);
                w.setGcmRegistrationID(registrationID);

                ret = 0;
                NetUtil.sendRequest(ctx, w, new NetUtil.NetUtilListener() {
                    @Override
                    public void onResponse(ResponseDTO response) {
                        if (response.getStatusCode() == 0) {
                            SharedUtil.storeRegistrationId(ctx, registrationID);
                            Log.w(LOG, "############ Device registered on server GCM regime");
                        } else {
                            ret = 9;
                        }
                    }

                    @Override
                    public void onError(String message) {
                        Log.e(LOG, message);
                        ret = 9;
                    }

                    @Override
                    public void onWebSocketClose() {

                    }
                });

            } catch (IOException e) {
                ret = 9;
            }

            return ret;
        }

        @Override
        protected void onPostExecute(Integer result) {
            Log.i(LOG, "onPostExecute... ending GCM registration");
            if (result > 0) {
                gcmUtilListener.onGCMError();
                //Util.showErrorToast(ctx, "Bad GCM Registration attempt");
                Log.e(LOG, "Bad GCM Registration attempt");
                return;
            }
            gcmUtilListener.onDeviceRegistered(registrationID);
            Log.i(LOG, "onPostExecute GCM device registered OK");
        }

    }




    public static final int SHOW_GOOGLE_PLAY_DIALOG = 1, GOOGLE_PLAY_ERROR = 2, OK = 3;







}
