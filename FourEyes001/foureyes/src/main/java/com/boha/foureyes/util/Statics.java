package com.boha.foureyes.util;


import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;


public class Statics {


    /**
     * SmartCity Production Server
     */
//    public static final String WEBSOCKET_URL = "ws://62.210.248.238:7070/sc/";
//    public static final String URL = "http://62.210.248.238:7070/sc/";
//    public static final String IMAGE_URL = "http://62.210.248.238:7070/";
//

    /*
     * REMOTE APP_URL - bohamaker back end - production
     */
    public
    static final String WEBSOCKET_URL = "ws://195.154.47.229:7070/sc/";
    public static final String URL = "http://195.154.47.229:7070/sc/";
    public static final String IMAGE_URL = "http://195.154.47.229:7070/";
//    public static final String WEBSOCKET_URL = "ws://bohamaker.com:3030/mp/";
//    public static final String URL = "http://bohamaker.com:3030/mp/";
//    public static final String IMAGE_URL = "http://bohamaker.com:3030/";

    /*
        LOCAL DEV - PECANWOOD
     */
//    public static final String WEBSOCKET_URL = "ws://192.168.1.33:8080/sc/";
//    public static final String URL = "http://192.168.1.33:8080/sc/";
//    public static final String IMAGE_URL = "http://192.168.1.33:8080/";

    public static final String GATEWAY_SERVLET = "smart?";
    public static final String GATEWAY_SOCKET = "wssmart";

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

}
