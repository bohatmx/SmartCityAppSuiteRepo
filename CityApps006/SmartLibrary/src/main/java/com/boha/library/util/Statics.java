package com.boha.library.util;


import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

/**
 * Container class to hold server URL, fonts etc
 */

public class Statics {

    /*
     * PECANWOOD BACKEND APP_URL'S - pecanwood back end endpoint on laptop
     */

    public static final String PREFIX = "http://icsmsdev1.oneconnectgroup.com:8585";
    //public static final String PREFIX = "http://10.0.0.102:8080";
    public static final String URL = PREFIX + "/sc/";
    public static final String IMAGE_URL = PREFIX + "/";


    /**
     * SmartCity Production Server
     */


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

    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";

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
