package com.boha.library.util;


import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

/**
 * Container class to hold server URL, fonts etc
 */

public class Statics {

    /*
     * REMOTE APP_URL - Zebula back end - local laptop
     */

//    public static final String WEBSOCKET_URL = "ws://192.168.2.45:8080/sc/";
//    public static final String URL = "http://192.168.2.45:8080/sc/";
//    public static final String IMAGE_URL = "http://192.168.2.45:8080/";

    //http://munimobileapp.oneconnectgroup.com:7070/
    //http://195.154.47.229:7070
    /**
     * SmartCity Production Server
     */
    public static final String WEBSOCKET_URL = "ws://195.154.47.229:7070/sc/";
    public static final String URL = "http://195.154.47.229:7070/sc/";
    public static final String IMAGE_URL = "http://195.154.47.229:7070/";
//


    /*
        LOCAL DEV - PECANWOOD
     */
//    public static final String WEBSOCKET_URL = "ws://192.168.1.33:8080/sc/";
//    public static final String URL = "http://192.168.1.33:8080/sc/";
//    public static final String IMAGE_URL = "http://192.168.1.33:8080/";

    public static final String GATEWAY_SERVLET = "smart?";
    public static final String GATEWAY_SOCKET = "wssmart";
    public static final String CACHED_REQUESTS_SOCKET = "wsrequests";
    public static final String CACHED_REQUESTS_SERVLET = "cachedRequests?";

    public static final String INVITE_DESTINATION = "https://play.google.com/store/apps/details?id=";
    public static final String INVITE_EXEC = INVITE_DESTINATION + "com.boha.monitor.exec";
    public static final String INVITE_OPERATIONS_MGR = INVITE_DESTINATION + "com.boha.monitor.operations";
    public static final String INVITE_PROJECT_MGR = INVITE_DESTINATION + "com.boha.monitor.pmanager";
    public static final String INVITE_SITE_MGR = INVITE_DESTINATION + "com.boha.monitor.site";

    public static final String CRASH_REPORTS_URL = URL + "crash?";
    public static final String GOOGLE_DISTANCE_MATRIX_URL = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=";


    public static void setRomanFontLight(Context ctx, TextView txt) {
        Typeface font = Typeface.createFromAsset(ctx.getAssets(),
                "fonts/Neuton-Light.ttf");
        txt.setTypeface(font);
    }

    public static void setRobotoFontBoldCondensed(Context ctx, TextView txt) {
        Typeface font = Typeface.createFromAsset(ctx.getAssets(),
                "fonts/Roboto-BoldCondensed.ttf");
        txt.setTypeface(font);
    }

    public static void setRobotoFontRegular(Context ctx, TextView txt) {
        Typeface font = Typeface.createFromAsset(ctx.getAssets(),
                "fonts/Roboto-Regular.ttf");
        txt.setTypeface(font);
    }

    public static void setRobotoFontLight(Context ctx, TextView txt) {
        Typeface font = Typeface.createFromAsset(ctx.getAssets(),
                "fonts/Roboto-Light.ttf");
        txt.setTypeface(font);
    }

    public static void setRobotoFontBold(Context ctx, TextView txt) {
        Typeface font = Typeface.createFromAsset(ctx.getAssets(),
                "fonts/Roboto-Bold.ttf");
        txt.setTypeface(font);
    }

    public static void setRobotoItalic(Context ctx, TextView txt) {
        Typeface font = Typeface.createFromAsset(ctx.getAssets(),
                "fonts/Roboto-Italic.ttf");
        txt.setTypeface(font);
    }

    public static void setRobotoRegular(Context ctx, TextView txt) {
        Typeface font = Typeface.createFromAsset(ctx.getAssets(),
                "fonts/Roboto-Regular.ttf");
        txt.setTypeface(font);
    }

    public static void setNeutonExtraBold(Context ctx, TextView txt) {
        Typeface font = Typeface.createFromAsset(ctx.getAssets(),
                "fonts/Neuton-SC-Extrabold.ttf");
        txt.setTypeface(font);
    }
}
