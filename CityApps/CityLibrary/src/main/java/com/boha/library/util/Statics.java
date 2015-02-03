package com.boha.library.util;


import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;


public class Statics {

    /*
     * REMOTE APP_URL - bohamaker back end - production
     */

    //
//    public static final String WEBSOCKET_URL = "ws://68.169.60.130:3030/sc/";
//    public static final String URL = "http://68.169.60.130:3030/sc/";
//    public static final String IMAGE_URL = "http://68.169.60.130:3030/";

    public static final String WEBSOCKET_URL = "ws://bohamaker.com:3030/sc/";
    public static final String URL = "http://bohamaker.com:3030/sc/";
    public static final String IMAGE_URL = "http://bohamaker.com:3030/";

//    public static final String WEBSOCKET_URL = "ws://192.168.1.111:8055/sc/";
//    public static final String URL = "http://192.168.1.111:8055/sc/";
//    public static final String IMAGE_URL = "http://192.168.1.111:8055/";


    public static final String GATEWAY_SERVLET = "smart?";
    public static final String GATEWAY_SOCKET = "wssmart";



    //google cloud http://mggolf-303.appspot.com/golf?JSON={requestType:38,golfGroupID:21}
    //public static final String APP_URL = "http://mggolf-303.appspot.com/";


//    public static final String PDF_URL = "http://192.168.1.111:8080/monitor_documents/";
//
    public static final String INVITE_DESTINATION = "https://play.google.com/store/apps/details?id=";
    public static final String INVITE_EXEC = INVITE_DESTINATION + "com.boha.monitor.exec";
    public static final String INVITE_OPERATIONS_MGR = INVITE_DESTINATION + "com.boha.monitor.operations";
    public static final String INVITE_PROJECT_MGR = INVITE_DESTINATION + "com.boha.monitor.pmanager";
    public static final String INVITE_SITE_MGR = INVITE_DESTINATION + "com.boha.monitor.site";

    public static final String UPLOAD_URL_REQUEST = "uploadUrl?";
    public static final String CRASH_REPORTS_URL = URL + "crash?";



    public static void setDroidFontBold(Context ctx, TextView txt) {
        Typeface font = Typeface.createFromAsset(ctx.getAssets(),
                "DroidSerif-Bold");
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
