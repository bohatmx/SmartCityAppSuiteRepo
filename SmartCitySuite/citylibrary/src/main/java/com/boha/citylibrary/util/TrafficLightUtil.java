package com.boha.citylibrary.util;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.boha.citylibrary.R;


/**
 * Created by aubreyM on 15/01/13.
 */
public class TrafficLightUtil {

    private static void enableGreen(Context ctx, View green) {
        green.setBackground(ctx.getResources().getDrawable(R.drawable.xgreen_oval_large));
        green.setEnabled(true);
        green.setAlpha(1.0f);
    }

    private static void disableGreen(Context ctx, View green) {
        green.setBackground(ctx.getResources().getDrawable(R.drawable.xgreen_oval_small));
        green.setEnabled(false);
        green.setAlpha(0.4f);
    }

    private static void enableAmber(Context ctx, View amber) {
        amber.setBackground(ctx.getResources().getDrawable(R.drawable.xamber_oval_large));
        amber.setEnabled(true);
        amber.setAlpha(1.0f);
    }

    private static void disableAmber(Context ctx, View amber) {
        amber.setBackground(ctx.getResources().getDrawable(R.drawable.xamber_oval_small));
        amber.setEnabled(false);
        amber.setAlpha(0.4f);
    }

    private static void enableRed(Context ctx, View red) {
        red.setBackground(ctx.getResources().getDrawable(R.drawable.xred_oval_large));
        red.setEnabled(true);
        red.setAlpha(1.0f);
    }

    private static void disableRed(Context ctx, View red) {
        red.setBackground(ctx.getResources().getDrawable(R.drawable.xred_oval_small));
        red.setEnabled(false);
        red.setAlpha(0.4f);
    }

    public static void setGreen(Context ctx, View view) {
        final TextView green = (TextView) view.findViewById(R.id.TRAFF_green);
        final TextView red = (TextView) view.findViewById(R.id.TRAFF_red);
        final TextView amber = (TextView) view.findViewById(R.id.TRAFF_amber);

        enableGreen(ctx, green);
        disableAmber(ctx, amber);
        disableRed(ctx, red);

    }

    public static void setRed(Context ctx, View view) {
        final TextView green = (TextView) view.findViewById(R.id.TRAFF_green);
        final TextView red = (TextView) view.findViewById(R.id.TRAFF_red);
        final TextView amber = (TextView) view.findViewById(R.id.TRAFF_amber);

        enableRed(ctx, red);
        disableAmber(ctx, amber);
        disableGreen(ctx, green);
    }

    public static void setAmber(Context ctx, View view) {
        final TextView green = (TextView) view.findViewById(R.id.TRAFF_green);
        final TextView red = (TextView) view.findViewById(R.id.TRAFF_red);
        final TextView amber = (TextView) view.findViewById(R.id.TRAFF_amber);

        enableAmber(ctx, amber);
        disableGreen(ctx, green);
        disableRed(ctx, red);
    }

    private static void setOthers() {

    }
}
