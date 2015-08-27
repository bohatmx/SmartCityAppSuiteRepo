package com.boha.library.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import java.io.Serializable;

/**
 * Created by aubreyM on 15/05/03.
 */
public class CityImages implements Serializable {

    private int[] imageResourceIDs;

    public int[] getImageResourceIDs() {
        return imageResourceIDs;
    }

    public void setImageResourceIDs(int[] imageResourceIDs) {
        this.imageResourceIDs = imageResourceIDs;
    }

    public Drawable getImage(Context ctx, int index) {
        if (ctx == null) {
            return null;
        }
        Drawable p = ContextCompat.getDrawable(ctx, imageResourceIDs[index]);
        return p;
    }

}
