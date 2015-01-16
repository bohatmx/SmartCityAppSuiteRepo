package com.boha.library.util;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.boha.cityapps.R;

/**
 * Created by aubreyM on 15/01/13.
 */
public class TrafficLightUtil {

    public static void setGreen(Context ctx, View view) {
        final TextView green = (TextView) view.findViewById(R.id.TRAFF_green);
        final TextView red = (TextView) view.findViewById(R.id.TRAFF_red);
        final TextView yellow = (TextView) view.findViewById(R.id.TRAFF_yellow);

        green.setBackground(ctx.getResources().getDrawable(R.drawable.xgreen_oval_large));
        red.setBackground(ctx.getResources().getDrawable(R.drawable.xred_oval_small));
        yellow.setBackground(ctx.getResources().getDrawable(R.drawable.xorange_oval_small));
    }
    public static void setRed(Context ctx, View view) {
        final TextView green = (TextView) view.findViewById(R.id.TRAFF_green);
        final TextView red = (TextView) view.findViewById(R.id.TRAFF_red);
        final TextView yellow = (TextView) view.findViewById(R.id.TRAFF_yellow);

        green.setBackground(ctx.getResources().getDrawable(R.drawable.xgreen_oval_small));
        red.setBackground(ctx.getResources().getDrawable(R.drawable.xred_oval_large));
        yellow.setBackground(ctx.getResources().getDrawable(R.drawable.xorange_oval_small));
    }
    public static void setYellow(Context ctx, View view) {
        final TextView green = (TextView) view.findViewById(R.id.TRAFF_green);
        final TextView red = (TextView) view.findViewById(R.id.TRAFF_red);
        final TextView yellow = (TextView) view.findViewById(R.id.TRAFF_yellow);

        green.setBackground(ctx.getResources().getDrawable(R.drawable.xgreen_oval_small));
        red.setBackground(ctx.getResources().getDrawable(R.drawable.xred_oval_small));
        yellow.setBackground(ctx.getResources().getDrawable(R.drawable.xorange_oval_large));
    }
    private static void setOthers() {

    }
}
