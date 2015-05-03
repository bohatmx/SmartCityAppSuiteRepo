package com.boha.library.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.Serializable;
import java.util.Random;

/**
 * Created by aubreyM on 15/05/03.
 */
public class CityImages implements Serializable{

    private int[] imageResourceIDs;

    public int[] getImageResourceIDs() {
        return imageResourceIDs;
    }

    public void setImageResourceIDs(int[] imageResourceIDs) {
        this.imageResourceIDs = imageResourceIDs;
    }
    static int lastIndex;
    static final int IMAGE_COUNT_MAX = 30;
    static final Random RANDOM = new Random(System.currentTimeMillis());
    

    public  Drawable getImage(Context ctx) {
        if (ctx == null) {
            return null;
        }
        int index = RANDOM.nextInt(IMAGE_COUNT_MAX - 1);
        if (index == lastIndex) {
            index = RANDOM.nextInt(IMAGE_COUNT_MAX - 1);
        }
        Drawable p = null;
        try {
            switch (index) {
                case 0:
                    p = ctx.getResources().getDrawable(imageResourceIDs[0]);
                    break;
                case 1:
                    p = ctx.getResources().getDrawable(imageResourceIDs[1]);
                    break;
                case 2:
                    p = ctx.getResources().getDrawable(imageResourceIDs[2]);
                    break;
                case 3:
                    p = ctx.getResources().getDrawable(imageResourceIDs[3]);
                    break;
                case 4:
                    p = ctx.getResources().getDrawable(imageResourceIDs[4]);
                    break;

                case 5:
                    p = ctx.getResources().getDrawable(imageResourceIDs[5]);
                    break;
                case 6:
                    p = ctx.getResources().getDrawable(imageResourceIDs[6]);
                    break;
                case 7:
                    p = ctx.getResources().getDrawable(imageResourceIDs[7]);
                    break;
                case 8:
                    p = ctx.getResources().getDrawable(imageResourceIDs[8]);
                    break;
                case 9:
                    p = ctx.getResources().getDrawable(imageResourceIDs[9]);
                    break;

                case 10:
                    p = ctx.getResources().getDrawable(imageResourceIDs[10]);
                    break;
                case 11:
                    p = ctx.getResources().getDrawable(imageResourceIDs[11]);
                    break;
                case 12:
                    p = ctx.getResources().getDrawable(imageResourceIDs[12]);
                    break;
                case 13:
                    p = ctx.getResources().getDrawable(imageResourceIDs[13]);
                    break;
                case 14:
                    p = ctx.getResources().getDrawable(imageResourceIDs[14]);
                    break;

                case 15:
                    p = ctx.getResources().getDrawable(imageResourceIDs[15]);
                    break;
                case 16:
                    p = ctx.getResources().getDrawable(imageResourceIDs[16]);
                    break;
                case 17:
                    p = ctx.getResources().getDrawable(imageResourceIDs[17]);
                    break;
                case 18:
                    p = ctx.getResources().getDrawable(imageResourceIDs[18]);
                    break;
                case 19:
                    p = ctx.getResources().getDrawable(imageResourceIDs[19]);
                    break;

                case 20:
                    p = ctx.getResources().getDrawable(imageResourceIDs[20]);
                    break;
                case 21:
                    p = ctx.getResources().getDrawable(imageResourceIDs[21]);
                    break;
                case 22:
                    p = ctx.getResources().getDrawable(imageResourceIDs[22]);
                    break;
                case 23:
                    p = ctx.getResources().getDrawable(imageResourceIDs[23]);
                    break;
                case 24:
                    p = ctx.getResources().getDrawable(imageResourceIDs[24]);
                    break;

                case 25:
                    p = ctx.getResources().getDrawable(imageResourceIDs[25]);
                    break;
                case 26:
                    p = ctx.getResources().getDrawable(imageResourceIDs[26]);
                    break;
                case 27:
                    p = ctx.getResources().getDrawable(imageResourceIDs[27]);
                    break;
                case 28:
                    p = ctx.getResources().getDrawable(imageResourceIDs[28]);
                    break;
                case 29:
                    p = ctx.getResources().getDrawable(imageResourceIDs[29]);
                    break;

                default:
                    p = ctx.getResources().getDrawable(imageResourceIDs[30]);
                    break;

            }

            lastIndex = index;

        } catch (OutOfMemoryError e) {
            Log.e("SplashActivity", "Error loading bitmap: " + index);
            return ctx.getResources().getDrawable(imageResourceIDs[28]);
        } catch (Exception e) {
        }
        return p;
    }

}
