package com.boha.citylibrary.activities;

import android.app.Application;
import android.util.Log;

import com.boha.citylibrary.R;
import com.boha.citylibrary.util.LocaleUtil;
import com.boha.citylibrary.util.SharedUtil;
import com.boha.citylibrary.util.Statics;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp.StethoInterceptor;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.L;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.squareup.okhttp.OkHttpClient;

import org.acra.ReportField;
import org.acra.annotation.ReportsCrashes;

import java.io.File;

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
public class CityApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        StringBuilder sb = new StringBuilder();
        sb.append("\n\n\n#######################################\n");
        sb.append("#######################################\n");
        sb.append("###\n");
        sb.append("###  SmartCity App has started\n");
        sb.append("###\n");
        sb.append("#######################################\n\n");

        Log.d(LOG, sb.toString());
        //ACRA.init(this);
        //ACRA.getErrorReporter().putCustomData("companyStaffID", SharedUtil.getID());
        //Log.w(LOG, "###### ACRA initialised");

        DisplayImageOptions defaultOptions =
                new DisplayImageOptions.Builder()
                        .cacheInMemory(true)
                        .cacheOnDisk(true)
                        .showImageOnFail(getApplicationContext().getResources().getDrawable(R.drawable.banner1))
                        .showImageOnLoading(getApplicationContext().getResources().getDrawable(R.drawable.banner1))
                        .build();

        File cacheDir = StorageUtils.getCacheDirectory(this, true);
        Log.d(LOG, "## onCreate, ImageLoader cacheDir, files: " + cacheDir.listFiles().length);
        //
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .denyCacheImageMultipleSizesInMemory()
                .diskCache(new UnlimitedDiscCache(cacheDir))
                .memoryCache(new LruMemoryCache(12 * 1024 * 1024))
                .defaultDisplayImageOptions(defaultOptions)
                .build();

        ImageLoader.getInstance().init(config);
        L.writeDebugLogs(false);
        L.writeLogs(false);

        Log.w(LOG, "###### ImageLoaderConfiguration has been initialised");

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build());

        OkHttpClient client = new OkHttpClient();
        client.networkInterceptors().add(new StethoInterceptor());

        Log.w(LOG, "###### Stetho debug helper initialised");

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
    static final String LOG = CityApplication.class.getSimpleName();
}
