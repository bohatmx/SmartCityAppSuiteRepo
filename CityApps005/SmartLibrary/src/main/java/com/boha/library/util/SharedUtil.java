package com.boha.library.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.boha.library.dto.MunicipalityDTO;
import com.boha.library.dto.MunicipalityStaffDTO;
import com.boha.library.dto.ProfileInfoDTO;
import com.google.gson.Gson;

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
        Log.i(LOG, "#### GCM id retrieved: " + j);
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
        Log.i(LOG, "#### profile retrieved: " + json);
        return gson.fromJson(json, ProfileInfoDTO.class);
    }
    static Gson gson = new Gson();
    static final String LANGUAGE_INDEX = "langIndex",
            ID = "id", MUNICIPALITY = "muni", MUNI_STAFF = "muniStaff",
            PROFILE = "profile", GCM = "gcm";

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
        Log.i(LOG, "#### muni retrieved: " + j);
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
        Log.i(LOG, "#### muni staff retrieved: " + j);
        return gson.fromJson(j, MunicipalityStaffDTO.class);
    }
    static final String LOG = SharedUtil.class.getSimpleName();
}
