package com.boha.library.activities;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.StrictMode;
import android.util.Log;

import com.boha.library.R;
import com.boha.library.dto.MunicipalityDTO;
import com.boha.library.util.LocaleUtil;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.Statics;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.L;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.annotation.ReportsCrashes;

import java.io.File;
import java.util.HashMap;

/**
 * Created by aubreyM on 15/02/21.
 */
@ReportsCrashes(
        formKey = "",
        formUri = Statics.CRASH_REPORTS_URL,
        customReportContent = {ReportField.APP_VERSION_NAME, ReportField.APP_VERSION_CODE,
                ReportField.ANDROID_VERSION, ReportField.PHONE_MODEL, ReportField.BRAND, ReportField.STACK_TRACE,
                ReportField.PACKAGE_NAME,
                ReportField.CUSTOM_DATA,
                ReportField.LOGCAT},
        socketTimeout = 10000
)
/**
 * The main Application for the SmartCity suite of apps. Initializes several
 * needed services, such as image loading and cache configuration, analytics
 * setup and ACRA to trap exceptions.
 */
public class CityApplication extends Application {
    public enum TrackerName {
        APP_TRACKER, // Tracker used only in this app.
        GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg: roll-up tracking.
        ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a company.
    }

    public static final String PROPERTY_ID = "UA-53661372-3";
    HashMap<TrackerName, Tracker> mTrackers = new HashMap<>();

    public synchronized Tracker getTracker(TrackerName trackerId) {
        if (!mTrackers.containsKey(trackerId)) {

            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(PROPERTY_ID)
                    : (trackerId == TrackerName.GLOBAL_TRACKER) ? analytics.newTracker(R.xml.global_tracker)
                    : analytics.newTracker(R.xml.ecommerce_tracker);
            mTrackers.put(trackerId, t);

        }
        return mTrackers.get(trackerId);
    }

    public static RefWatcher getRefWatcher(Context context) {
        CityApplication application = (CityApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    private RefWatcher refWatcher;


    @Override
    public void onCreate() {
        super.onCreate();
        StringBuilder sb = new StringBuilder();
        sb.append("\n\n#######################################\n");
        sb.append("#######################################\n");
        sb.append("###\n");
        sb.append("###  SmartCity App has started\n");
        sb.append("###\n");
        sb.append("#######################################\n\n");

        Log.d(LOG, sb.toString());
        refWatcher = LeakCanary.install(this);
        MunicipalityDTO m = SharedUtil.getMunicipality(getApplicationContext());
        boolean isDebuggable = 0 != (getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE);
        if (!isDebuggable) {
            StrictMode.enableDefaults();
            Log.e(LOG, "###### StrictMode defaults enabled");
            ACRA.init(this);
            if (m != null) {
                ACRA.getErrorReporter().putCustomData("municipalityID", "" + m.getMunicipalityID());
            }
            Log.e(LOG, "###### ACRA initialised. Exceptions will be grabbed and sent.");
        } else {
            Log.d(LOG, "###### ACRA not initialised. Running in release mode");
        }


        DisplayImageOptions defaultOptions =
                new DisplayImageOptions.Builder()
                        .cacheInMemory(true)
                        .cacheOnDisk(true)
                        .showImageOnFail(getApplicationContext().getResources().getDrawable(R.drawable.under_construction))
                        .showImageOnLoading(getApplicationContext().getResources().getDrawable(R.drawable.under_construction2))
                        .build();

        File cacheDir = StorageUtils.getCacheDirectory(this, true);
        Log.e(LOG, "## onCreate, ImageLoader cacheDir, files: " + cacheDir.listFiles().length);
        //
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .denyCacheImageMultipleSizesInMemory()
                .diskCache(new UnlimitedDiskCache(cacheDir))
                .memoryCache(new LruMemoryCache(12 * 1024 * 1024))
                .defaultDisplayImageOptions(defaultOptions)
                .build();

        ImageLoader.getInstance().init(config);
        L.writeDebugLogs(false);
        L.writeLogs(false);

        Log.w(LOG, "###### ImageLoaderConfiguration has been initialised");


        int index = SharedUtil.getLanguageIndex(getApplicationContext());
        setLocale(index);

    }


    private void setLocale(int index) {
        switch (index) {
            case 0:
                LocaleUtil.setLocale(getApplicationContext(), LocaleUtil.ENGLISH);
                break;
            case 1:
                LocaleUtil.setLocale(getApplicationContext(), LocaleUtil.AFRIKAANS);
                break;
            case 2:
                LocaleUtil.setLocale(getApplicationContext(), LocaleUtil.ZULU);
                break;
            case 3:
                LocaleUtil.setLocale(getApplicationContext(), LocaleUtil.XHOSA);
                break;
            case 4:
                LocaleUtil.setLocale(getApplicationContext(), LocaleUtil.XITSONGA);
                break;
            case 5:
                LocaleUtil.setLocale(getApplicationContext(), LocaleUtil.SETSWANA);
                break;
        }
    }

    public final static int THEME_BLUE = 20;
    public final static int THEME_INDIGO = 1;
    public final static int THEME_RED = 2,
            THEME_TEAL = 3,
            THEME_BLUE_GRAY = 4,
            THEME_ORANGE = 5,
            THEME_PINK = 6,
            THEME_CYAN = 7,
            THEME_GREEN = 8,
            THEME_LIGHT_GREEN = 9,
            THEME_LIME = 10,
            THEME_AMBER = 11,
            THEME_GREY = 12,
            THEME_BROWN = 14,
            THEME_PURPLE = 15;

    static final String LOG = CityApplication.class.getSimpleName();
}
