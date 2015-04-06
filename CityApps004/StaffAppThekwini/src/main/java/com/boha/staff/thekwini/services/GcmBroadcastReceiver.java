package com.boha.staff.thekwini.services;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
    	Log.e(TAG, "################# onReceive - GCM message coming in ...");
        ComponentName comp = new ComponentName(context.getPackageName(),
                GcmIntentService.class.getName());
        
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }
    
    static final String TAG = "GcmBroadcastReceiver";
}