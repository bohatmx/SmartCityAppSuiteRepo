package com.boha.library.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by aubreyM on 15/01/13.
 */
public class SharedUtil {
    public static void setLanguageIndex(Context ctx, int languageIndex) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);

        SharedPreferences.Editor ed = sp.edit();
        ed.putInt(LANGUAGE_INDEX, languageIndex);
        ed.commit();

        Log.w("SharedUtil", "#### language index saved: " + languageIndex);

    }
    public static int getLanguageIndex(Context ctx) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        int j = sp.getInt(LANGUAGE_INDEX, -1);
        Log.i("SharedUtil", "#### language index retrieved: " + j);
        return j;
    }
    public static void setID(Context ctx, int id) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);

        SharedPreferences.Editor ed = sp.edit();
        ed.putInt(ID, id);
        ed.commit();

        Log.w("SharedUtil", "#### id  saved: " + id);

    }
    public static int getID(Context ctx) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        int j = sp.getInt(ID, 0);
        Log.i("SharedUtil", "#### id retrieved: " + j);
        return j;
    }
    public static void setName(Context ctx, String name) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);

        SharedPreferences.Editor ed = sp.edit();
        ed.putString(NAME, name);
        ed.commit();

        Log.w("SharedUtil", "#### name  saved: " + name);

    }
    public static String getName(Context ctx) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        String j = sp.getString(NAME, null);
        Log.i("SharedUtil", "#### name retrieved: " + j);
        return j;
    }
    static final String LANGUAGE_INDEX = "langIndex", ID = "id", NAME = "name";
}
