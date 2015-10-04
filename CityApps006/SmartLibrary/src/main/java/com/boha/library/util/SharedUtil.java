package com.boha.library.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.boha.library.R;
import com.boha.library.dto.CreditCard;
import com.boha.library.dto.GcmDeviceDTO;
import com.boha.library.dto.MunicipalityDTO;
import com.boha.library.dto.MunicipalityStaffDTO;
import com.boha.library.dto.ProfileInfoDTO;
import com.boha.library.dto.UserDTO;
import com.google.gson.Gson;

import java.util.Random;

/**
 * Created by aubreyM on 15/01/13.
 */
public class SharedUtil {


    public static final int
            CITIZEN_WITH_ACCOUNT = 1,
            CITIZEN_NO_ACCOUNT = 2,
            TOURIST_VISITOR = 3;
    public static void setUserType(Context ctx, int userType) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);

        SharedPreferences.Editor ed = sp.edit();
        ed.putInt("userType",userType);
        ed.commit();

        Log.w(LOG, "#### userType saved: " + userType);

    }
    public static int getUserType(Context ctx) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);

        int j = sp.getInt("userType", CITIZEN_WITH_ACCOUNT);
        Log.i(LOG, "#### userType retrieved: " + j);
        return j;
    }
    public static void setCityImages(Context ctx, CityImages images) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);

        SharedPreferences.Editor ed = sp.edit();
        String json = gson.toJson(images);
        ed.putString(CITY_IMAGES, json);
        ed.commit();

        Log.w(LOG, "#### cityImages saved: " + json);

    }
    public static CityImages getCityImages(Context ctx) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        String json = sp.getString(CITY_IMAGES, null);
        if (json == null) {
            Log.e(LOG, "#### cityImages NOT retrieved");
            return null;
        }
        CityImages cityImages = gson.fromJson(json, CityImages.class);
//        Log.i(LOG, "#### cityImages retrieved: " + cityImages.getImageResourceIDs().length);
        return cityImages;
    }

    static int lastIndex;
    public static Bitmap getRandomCityImage(Context ctx) {
        CityImages cityImages = getCityImages(ctx);
        int index = random.nextInt(cityImages.getImageResourceIDs().length - 1);
//        Log.w(LOG,"getRandomCityImage index: " + index + " lastIndex: " + lastIndex);
        if (lastIndex != 0) {
            while (index != lastIndex) {
                index = random.nextInt(cityImages.getImageResourceIDs().length - 1);
            }
        }
        int id = cityImages.getImageResourceIDs() [index];
        Drawable drawable = ContextCompat.getDrawable(ctx, id);
        BitmapDrawable bd = (BitmapDrawable)drawable;
        Bitmap k = getResizedBitmap(bd.getBitmap(), 560, 380);
        return k;
    }
    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {

        int width = bm.getWidth();
        int height = bm.getHeight();

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

// recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);

        return resizedBitmap;
    }
    static Random random = new Random(System.currentTimeMillis());
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
        Log.d(LOG, "#### profile retrieved");
        return gson.fromJson(json, ProfileInfoDTO.class);
    }
    static Gson gson = new Gson();
    static final String LANGUAGE_INDEX = "langIndex",
            VISA_CREDIT_CARD = "visacredCard",
            MASTERCARD_CREDIT_CARD = "mccredCard",
            SID_CARD = "sidCard",
            LAST_PAYMENT_TYPE = "lastType",
            THEME = "theme", CITY_IMAGES = "cityImages", ADDRESS = "address",
            ID = "id", MUNICIPALITY = "muni", MUNI_STAFF = "muniStaff",
            GCMDEVICE = "GCMDEVICE",
            USER = "user", PROFILE = "profile", GCM = "gcm", SLIDING_TAB_COUNT = "slidingTabs";

    public static void saveGCMDevice( Context ctx,  GcmDeviceDTO dto) {


        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        String x = gson.toJson(dto);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(GCMDEVICE, x);
        ed.commit();
        Log.e("SharedUtil", "%%%%% Device saved in SharedPreferences");
    }


    public static GcmDeviceDTO getGCMDevice( Context ctx) {

        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        String adm = sp.getString(GCMDEVICE, null);
        GcmDeviceDTO co = null;
        if (adm != null) {
            co = gson.fromJson(adm, GcmDeviceDTO.class);

        }
        return co;
    }

    public static void saveAddress(Context ctx, ResidentialAddress m) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);

        SharedPreferences.Editor ed = sp.edit();
        String s = gson.toJson(m);
        ed.putString(ADDRESS, s);
        ed.commit();

        Log.w(LOG, "#### address  saved: " + s);

    }
    public static ResidentialAddress getAddress(Context ctx) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        String j = sp.getString(ADDRESS, null);
        if (j == null) {
            return null;
        }
        return gson.fromJson(j, ResidentialAddress.class);
    }
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

    public static void saveCreditCard(Context ctx, CreditCard creditCard) {
        if (creditCard.getCardType().equalsIgnoreCase(ctx.getString(R.string.visa))) {
            saveVisaCard(ctx,creditCard);
            savePaymentType(ctx, SharedUtil.VISA);
        } else {
            saveMasterCard(ctx,creditCard);
            savePaymentType(ctx, SharedUtil.MASTERCARD);
        }

    }
    private static void saveVisaCard(Context ctx, CreditCard creditCard) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);

        String json = gson.toJson(creditCard);

        SharedPreferences.Editor ed = sp.edit();
        ed.putString(VISA_CREDIT_CARD, json);
        ed.commit();

        Log.w(LOG, "#### visa creditCard  saved: " + json);

    }
    public static CreditCard getVisaCard(Context ctx) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        String json = sp.getString(VISA_CREDIT_CARD, null);

        if (json == null) {
            Log.e(LOG, "#### creditCard NOT retrieved");
            return null;
        }
        return gson.fromJson(json, CreditCard.class);
    }
    private static void saveMasterCard(Context ctx, CreditCard creditCard) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);

        String json = gson.toJson(creditCard);

        SharedPreferences.Editor ed = sp.edit();
        ed.putString(MASTERCARD_CREDIT_CARD, json);
        ed.commit();

        Log.w(LOG, "#### visa creditCard  saved: " + json);

    }
    public static CreditCard getMasterCard(Context ctx) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        String json = sp.getString(MASTERCARD_CREDIT_CARD, null);

        if (json == null) {
            Log.e(LOG, "#### MC creditCard NOT retrieved");
            return null;
        }
        return gson.fromJson(json, CreditCard.class);
    }
    public static void savePaymentType(Context ctx, int type) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);

        SharedPreferences.Editor ed = sp.edit();
        ed.putInt(LAST_PAYMENT_TYPE, type);
        ed.commit();

        Log.w(LOG, "#### last payment  saved: " + type);

    }
    public static int getLastPaymentType(Context ctx) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        int type = sp.getInt(LAST_PAYMENT_TYPE, 0);
        return type;
    }
    public static final int VISA = 1, MASTERCARD = 2, SID = 3;

    public static final int MAX_SLIDING_TAB_VIEWS = 200;
    static final String LOG = SharedUtil.class.getSimpleName();
}
