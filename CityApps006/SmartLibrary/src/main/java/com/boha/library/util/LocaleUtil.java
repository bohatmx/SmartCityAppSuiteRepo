package com.boha.library.util;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

import java.util.Locale;

public class LocaleUtil {

    public static void setLocale(Context ctx, int newLocale) {
        Locale appLoc = Locale.getDefault();
        switch (newLocale) {
            case ENGLISH:
                appLoc = new Locale(ENGLISH_LOCALE);
                break;
            case AFRIKAANS:
                appLoc = new Locale(AFRIKAANS_LOCALE);
                break;
            case ZULU:
                appLoc = new Locale(ZULU_LOCALE);
                break;
            case SETSWANA:
                appLoc = new Locale(SETSWANA_LOCALE);
                break;
            case XHOSA:
                appLoc = new Locale(XHOSA_LOCALE);
                break;
            case XITSONGA:
                appLoc = new Locale(XITSONGA_LOCALE);
                break;

            default:
                appLoc = new Locale(ENGLISH_LOCALE);
                break;
        }

        Locale.setDefault(appLoc);
        Configuration appConfig = new Configuration();
        appConfig.locale = appLoc;
        ctx.getResources()
                .updateConfiguration(appConfig,
                        ctx.getResources().getDisplayMetrics());

        Log.e(LOG, "...App Locale changed to: " + appLoc.getDisplayLanguage());
    }

    static final String LOG = LocaleUtil.class.getSimpleName();

    public static final int ENGLISH = 1, AFRIKAANS = 2, ZULU = 3, SETSWANA = 4,
            XHOSA = 5, XITSONGA = 6;

    public static final String
            ENGLISH_LOCALE = "en",
            AFRIKAANS_LOCALE = "af",
            ZULU_LOCALE = "zu",
            SETSWANA_LOCALE = "ts",
            XHOSA_LOCALE = "xh",
            XITSONGA_LOCALE = "xi";
}
