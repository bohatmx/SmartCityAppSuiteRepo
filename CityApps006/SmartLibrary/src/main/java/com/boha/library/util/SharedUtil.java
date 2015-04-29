package com.boha.library.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.boha.library.dto.MunicipalityDTO;
import com.boha.library.dto.MunicipalityStaffDTO;
import com.boha.library.dto.ProfileInfoDTO;
import com.boha.library.dto.UserDTO;
import com.google.gson.Gson;

/**
 * Created by aubreyM on 15/01/13.
 */
public class SharedUtil {

    public static void setThemeSelection(Context ctx, int theme) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);

        SharedPreferences.Editor ed = sp.edit();
        ed.putInt(THEME, theme);
        ed.commit();

        Log.w(LOG, "#### theme saved: " + theme);

    }
    public static int getThemeSelection(Context ctx) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        int j = sp.getInt(THEME, -1);
        Log.i(LOG, "#### theme retrieved: " + j);
        return j;
    }

    public static void setLanguageIndex(Context ctx, int languageIndex) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);

        SharedPreferences.Editor ed = sp.edit();
        ed.putInt(LANGUAGE_INDEX, languageIndex);
        ed.commit();

        Log.w(LOG, "#### language index saved: " + languageIndex);

    }
    public static int getLanguageIndex(Context ctx) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        int j = sp.getInt(LANGUAGE_INDEX, -1);
        Log.i(LOG, "#### language index retrieved: " + j);
        return j;
    }




    public static void setID(Context ctx, String id) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);

        SharedPreferences.Editor ed = sp.edit();
        ed.putString(ID, id);
        ed.commit();

        Log.w(LOG, "#### id  saved: " + id);

    }
    public static String getID(Context ctx) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        String j = sp.getString(ID, null);
        if (j == null) {
            Log.i(LOG, "#### id NOT retrieved: ");
            return null;
        }
        Log.i(LOG, "#### id retrieved: " + j);
        return j;
    }

    public static void storeRegistrationId(Context ctx, String id) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);

        SharedPreferences.Editor ed = sp.edit();
        ed.putString(GCM, id);
        ed.commit();

        Log.w(LOG, "#### GCM registrationid  saved: " + id);

    }
    public static String getRegistrationID(Context ctx) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        String j = sp.getString(GCM, null);
        if (j == null) {
            Log.i(LOG, "#### GCM id NOT retrieved: ");
            return null;
        }
//        Log.i(LOG, "#### GCM id retrieved: " + j);
        return j;
    }

    public static void saveProfile(Context ctx, ProfileInfoDTO profileInfo) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);

        String json = gson.toJson(profileInfo);

        SharedPreferences.Editor ed = sp.edit();
        ed.putString(PROFILE, json);
        ed.commit();

        Log.w(LOG, "#### profile  saved: " + json);

    }
    public static ProfileInfoDTO getProfile(Context ctx) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        String json = sp.getString(PROFILE, null);

        if (json == null) {
            Log.e(LOG, "#### profile NOT retrieved");
            return null;
        }
//        Log.i(LOG, "#### profile retrieved: " + json);
        return gson.fromJson(json, ProfileInfoDTO.class);
    }
    static Gson gson = new Gson();
    static final String LANGUAGE_INDEX = "langIndex",
            THEME = "theme",
            ID = "id", MUNICIPALITY = "muni", MUNI_STAFF = "muniStaff",
            USER = "user", PROFILE = "profile", GCM = "gcm", SLIDING_TAB_COUNT = "slidingTabs";

    public static void saveMunicipality(Context ctx, MunicipalityDTO m) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);

        SharedPreferences.Editor ed = sp.edit();
        String s = gson.toJson(m);
        ed.putString(MUNICIPALITY, s);
        ed.commit();

        Log.w(LOG, "#### municipality  saved: " + s);

    }
    public static MunicipalityDTO getMunicipality(Context ctx) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        String j = sp.getString(MUNICIPALITY, null);
        if (j == null) {
            return null;
        }
//        Log.i(LOG, "#### muni retrieved: " + j);
        return gson.fromJson(j, MunicipalityDTO.class);
    }

    public static void saveMunicipalityStaff(Context ctx, MunicipalityStaffDTO m) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);

        SharedPreferences.Editor ed = sp.edit();
        String s = gson.toJson(m);
        ed.putString(MUNI_STAFF, s);
        ed.commit();

        Log.w(LOG, "#### municipality staff saved: " + s);

    }
    public static MunicipalityStaffDTO getMunicipalityStaff(Context ctx) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        String j = sp.getString(MUNI_STAFF, null);
        if (j == null) {
            return null;
        }
//        Log.i(LOG, "#### muni staff retrieved: " + j);
        return gson.fromJson(j, MunicipalityStaffDTO.class);
    }

    public static void incrementSlidingMenuCount(Context ctx) {
        int count = getSlidingMenuCount(ctx);
        if (count > MAX_SLIDING_TAB_VIEWS) {
            return;
        }
        count++;

        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);

        SharedPreferences.Editor ed = sp.edit();
        ed.putInt(SLIDING_TAB_COUNT, count);
        ed.commit();


    }
    public static int getSlidingMenuCount(Context ctx) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        int j = sp.getInt(SLIDING_TAB_COUNT, 0);
        return j;
    }
    public static void saveUser(Context ctx, UserDTO user) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);

        String json = gson.toJson(user);

        SharedPreferences.Editor ed = sp.edit();
        ed.putString(USER, json);
        ed.commit();

        Log.w(LOG, "#### user  saved: " + json);

    }
    public static UserDTO getUser(Context ctx) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        String json = sp.getString(USER, null);

        if (json == null) {
            Log.e(LOG, "#### user NOT retrieved");
            return null;
        }
        return gson.fromJson(json, UserDTO.class);
    }
    public static final int MAX_SLIDING_TAB_VIEWS = 36;
    static final String LOG = SharedUtil.class.getSimpleName();
}
