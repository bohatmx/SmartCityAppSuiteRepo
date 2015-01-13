package com.boha.library.activities;

import android.app.Application;
import android.util.Log;

import com.boha.library.util.LocaleUtil;
import com.boha.library.util.SharedUtil;

/**
 * Created by aubreyM on 15/01/15.
 */
public class CityApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        StringBuilder sb = new StringBuilder();
        sb.append("\n\n\n#######################################\n");
        sb.append("#######################################\n");
        sb.append("###\n");
        sb.append("###  City App has started\n");
        sb.append("###\n");
        sb.append("#######################################\n\n");

        Log.d(LOG, sb.toString());

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
