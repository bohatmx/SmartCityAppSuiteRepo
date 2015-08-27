package com.boha.library.util;

import android.app.Activity;
import android.util.Log;

import com.boha.library.R;
import com.boha.library.activities.CityApplication;

public class ThemeChooser {


    /**
     * Set the theme of the activity, according to the configuration.
     */
    public static void setTheme(Activity activity) {

        int theme = SharedUtil.getThemeSelection(activity);

        switch (theme) {

            case CityApplication.THEME_BLUE:
                activity.setTheme(R.style.BlueThemeOne);
                break;
            case CityApplication.THEME_INDIGO:
                activity.setTheme(R.style.IndigoTheme);
                break;
            case CityApplication.THEME_RED:
                activity.setTheme(R.style.RedTheme);
                break;
            case CityApplication.THEME_TEAL:
                activity.setTheme(R.style.TealTheme);
                break;
            case CityApplication.THEME_BLUE_GRAY:
                activity.setTheme(R.style.BlueGrayTheme);
                break;
            case CityApplication.THEME_ORANGE:
                activity.setTheme(R.style.OrangeTheme);
                break;
            case CityApplication.THEME_PINK:
                activity.setTheme(R.style.PinkTheme);
                break;
            case CityApplication.THEME_CYAN:
                activity.setTheme(R.style.CyanTheme);
                break;
            case CityApplication.THEME_GREEN:
                activity.setTheme(R.style.GreenTheme);
                break;
            case CityApplication.THEME_GREY:
                activity.setTheme(R.style.GreyTheme);
                break;
            case CityApplication.THEME_LIGHT_GREEN:
                activity.setTheme(R.style.LightGreenTheme);
                break;
            case CityApplication.THEME_LIME:
                activity.setTheme(R.style.LimeTheme);
                break;
            case CityApplication.THEME_PURPLE:
                activity.setTheme(R.style.PurpleTheme);
                break;
            case CityApplication.THEME_AMBER:
                activity.setTheme(R.style.AmberTheme);
                break;
            case CityApplication.THEME_BROWN:
                activity.setTheme(R.style.BrownTheme);
                break;
            default:
                Log.d("ThemeChooser", "### no theme selected, none set");
                break;
        }
    }
}