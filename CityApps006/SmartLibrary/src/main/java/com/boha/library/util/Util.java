package com.boha.library.util;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Environment;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.boha.library.R;
import com.boha.library.activities.MuniContactsActivity;
import com.boha.library.adapters.PopupListAdapter;
import com.boha.library.dto.AlertImageDTO;
import com.boha.library.dto.ComplaintImageDTO;
import com.boha.library.dto.NewsArticleImageDTO;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Toolbox class - all sorts of useful static methods to be found here
 * Created by aubreyM on 15/04/03.
 */
public class Util {
    public interface UtilAnimationListener {
        public void onAnimationEnded();
    }

    public static Bitmap createBitmapFromView(Context context, View view, DisplayMetrics displayMetrics) {
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels,
                displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(),
                view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    public static void setCustomActionBarNoAction(final Context ctx,
                                          ActionBar actionBar, String text, Drawable image) {
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater inflator = (LayoutInflater)
                ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.action_bar_logo, null);
        TextView txt = (TextView) v.findViewById(R.id.ACTION_BAR_text);
        final ImageView logo = (ImageView) v.findViewById(R.id.ACTION_BAR_logo);
        txt.setText(text);
        //
        logo.setImageDrawable(image);
        actionBar.setCustomView(v);
        actionBar.setTitle("");


    }

    public static void setCustomActionBar(final Context ctx,
                                          ActionBar actionBar, String text, Drawable image, final int logoInt) {
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater inflator = (LayoutInflater)
                ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.action_bar_logo, null);
        TextView txt = (TextView) v.findViewById(R.id.ACTION_BAR_text);
        final ImageView logo = (ImageView) v.findViewById(R.id.ACTION_BAR_logo);
        txt.setText(text);
        //
        logo.setImageDrawable(image);
        actionBar.setCustomView(v);
        actionBar.setTitle("");
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(logo, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        Intent w = new Intent(ctx, MuniContactsActivity.class);
                        w.putExtra("logo",logoInt);
                        w.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        ctx.startActivity(w);
                    }
                });
            }
        });

    }


    public static String getStatementURL(Context ctx, String accountNumber, int year, int month) {
        StringBuilder sb = getStartURL(SharedUtil.getMunicipality(ctx).getMunicipalityID());
        sb.append("/documents/");
        sb.append("account_")
                .append(accountNumber).append("/")
                .append(accountNumber).append("_")
                .append(year).append("_").append(month).append(".pdf");
        Log.d("Util","Statement URL: " + sb.toString());
        return sb.toString();
    }

    public static String getAlertImageURL(AlertImageDTO p) {
        StringBuilder sb = getStartURL(p.getMunicipalityID());
        sb.append("/alerts/alert")
                .append(p.getAlertID()).append("/")
                .append(p.getFileName());
        Log.i("Util", "Loading alert image: " + sb.toString());
        return sb.toString();
    }
    public static String getNewsImageURL(NewsArticleImageDTO p) {
        StringBuilder sb = getStartURL(p.getMunicipalityID());
        sb.append("/news/");
        sb.append("/news")
                .append(p.getNewsArticleID()).append("/")
                .append(p.getFileName());
        Log.i("Util", "Loading news image: " + sb.toString());
        return sb.toString();
    }

    public static String getComplaintImageURL(ComplaintImageDTO p) {
        StringBuilder sb = getStartURL(p.getMunicipalityID());
        sb.append("/complaints/");
        sb.append("complaint")
                .append(p.getComplaintID()).append("/")
                .append(p.getFileName());
        Log.i("Util", "Loading complaint image: " + sb.toString());
        return sb.toString();
    }

    public static StringBuilder getStartURL(Integer municipalityID) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Statics.IMAGE_URL).append("smartcity_images/")
                .append("municipality").append(municipalityID);
        return stringBuilder;
    }

    public static double getElapsed(long start, long end) {
        BigDecimal m = new BigDecimal(end - start).divide(new BigDecimal(1000));
        return m.doubleValue();
    }


    public static void flashOnce(View view, long duration, final UtilAnimationListener listener) {
        try {
            ObjectAnimator an = ObjectAnimator.ofFloat(view, "alpha", 0, 1);
            an.setRepeatMode(ObjectAnimator.REVERSE);
            an.setDuration(duration);
            an.setInterpolator(new AccelerateDecelerateInterpolator());
            an.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (listener != null)
                        listener.onAnimationEnded();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            an.start();
        } catch (Exception e) {
            if (listener != null) {
                listener.onAnimationEnded();
            }
        }

    }


    public static void collapse(final View view, int duration, final UtilAnimationListener listener) {
        int finalHeight = view.getHeight();

        ValueAnimator mAnimator = slideAnimator(view, finalHeight, 0);
        mAnimator.setDuration(duration);
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
                if (listener != null)
                    listener.onAnimationEnded();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mAnimator.start();
    }

    public static void expand(View view, int duration, final UtilAnimationListener listener) {
        view.setVisibility(View.VISIBLE);

        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(widthSpec, heightSpec);

        ValueAnimator mAnimator = slideAnimator(view, 0, view.getMeasuredHeight());
        mAnimator.setDuration(duration);
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (listener != null) listener.onAnimationEnded();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mAnimator.start();

    }
    public static void expand(View view, int duration, int height,final UtilAnimationListener listener) {
        view.setVisibility(View.VISIBLE);

        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(widthSpec, heightSpec);

        ValueAnimator mAnimator = slideAnimator(view, 0, view.getMeasuredHeight());
        mAnimator.setDuration(duration);
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (listener != null) listener.onAnimationEnded();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mAnimator.start();

    }

    private static ValueAnimator slideAnimator(final View view, int start, int end) {

        ValueAnimator animator = ValueAnimator.ofInt(start, end);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = value;
                view.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    public static void showPopupBasicWithHeroImage(Context ctx, Activity act,
                                                   List<String> list,
                                                   View anchorView, String caption, final UtilPopupListener listener) {
        final ListPopupWindow pop = new ListPopupWindow(act);
        LayoutInflater inf = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inf.inflate(R.layout.hero_image_popup, null);
        TextView txt = (TextView) v.findViewById(R.id.HERO_caption);
        if (caption != null) {
            txt.setText(caption);
        } else {
            txt.setVisibility(View.GONE);
        }
        ImageView img = (ImageView) v.findViewById(R.id.HERO_image);
        img.setImageDrawable(getRandomBackgroundImage(ctx));

        pop.setPromptView(v);
        pop.setPromptPosition(ListPopupWindow.POSITION_PROMPT_ABOVE);
        pop.setAdapter(new PopupListAdapter(ctx, R.layout.xspinner_item,
                list, false));
        pop.setAnchorView(anchorView);
        pop.setHorizontalOffset(getPopupHorizontalOffset(act));
        pop.setModal(true);
        pop.setWidth(getPopupWidth(act));
        pop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pop.dismiss();
                if (listener != null) {
                    listener.onItemSelected(position);
                }
            }
        });
        pop.show();
    }

    public interface UtilPopupListener {
        public void onItemSelected(int index);
    }

    public static int getPopupWidth(Activity activity) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
        Double d = Double.valueOf("" + width);
        Double e = d / 1.5;
        return e.intValue();
    }

    public static int getPopupHorizontalOffset(Activity activity) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
        Double d = Double.valueOf("" + width);
        Double e = d / 15;
        return e.intValue();
    }

    private static List<Drawable> images, banners;
    private static int lastIndex, lastBannerIndex;

    private static void loadBanners(Context ctx) {
        banners = new ArrayList<>();
        banners.add(ctx.getResources().getDrawable(R.drawable.banner1));
        banners.add(ctx.getResources().getDrawable(R.drawable.banner2));
        banners.add(ctx.getResources().getDrawable(R.drawable.banner3));
        banners.add(ctx.getResources().getDrawable(R.drawable.banner4));


    }

    static public boolean hasStorage(boolean requireWriteAccess) {
        String state = Environment.getExternalStorageState();
        Log.w("Util", "--------- disk storage state is: " + state);

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            if (requireWriteAccess) {
                boolean writable = checkFsWritable();
                Log.i("Util", "************ storage is writable: " + writable);
                return writable;
            } else {
                return true;
            }
        } else if (!requireWriteAccess && Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    private static boolean checkFsWritable() {
        // Create a temporary file to see whether a volume is really writeable.
        // It's important not to put it in the root directory which may have a
        // limit on the number of files.
        String directoryName = Environment.getExternalStorageDirectory().toString() + "/DCIM";
        File directory = new File(directoryName);
        if (!directory.isDirectory()) {
            if (!directory.mkdirs()) {
                return false;
            }
        }
        return directory.canWrite();
    }

    static int index;
    public static int getWindowHeight(Activity activity) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        return height;
    }

    public static Drawable getRandomBanner(Context ctx) {
        if (banners == null) {
            loadBanners(ctx);
        }
        int index = random.nextInt(4);
        if (index == lastBannerIndex) {
            getRandomBanner(ctx);
        }
        lastBannerIndex = index;
        return banners.get(index);
    }

    static Random random = new Random(System.currentTimeMillis());

    public static void showErrorToast(Context ctx, String caption) {
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.toast_error, null);
        TextView txt = (TextView) view.findViewById(R.id.MONTOAST_text);
        TextView ind = (TextView) view.findViewById(R.id.MONTOAST_indicator);
        txt.setText(caption);
        Toast customtoast = new Toast(ctx);

        customtoast.setView(view);
        customtoast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        customtoast.setDuration(Toast.LENGTH_LONG);
        customtoast.show();
    }

    public static void showToast(Context ctx, String caption) {
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.toast, null);
        TextView txt = (TextView) view.findViewById(R.id.MONTOAST_text);
        Statics.setRobotoFontLight(ctx, txt);
        TextView ind = (TextView) view.findViewById(R.id.MONTOAST_indicator);
        txt.setText(caption);
        Toast customtoast = new Toast(ctx);

        customtoast.setView(view);
        customtoast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        customtoast.setDuration(Toast.LENGTH_SHORT);
        customtoast.show();
    }

    public static void flashSeveralTimes(final View view,
                                         final long duration, final int max,
                                         final UtilAnimationListener listener) {
        try {
            final ObjectAnimator an = ObjectAnimator.ofFloat(view, "alpha", 0, 1);
            an.setRepeatMode(ObjectAnimator.REVERSE);
            an.setDuration(duration);
            an.setInterpolator(new AccelerateDecelerateInterpolator());
            an.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    count++;
                    if (count > max) {
                        count = 0;
                        an.cancel();
                        if (listener != null)
                            listener.onAnimationEnded();
                        return;
                    }
                    flashSeveralTimes(view, duration, max, listener);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            an.start();
        } catch (Exception e) {
            if (listener != null) {
                listener.onAnimationEnded();
            }
        }

    }

    public static void preen(View view, final long duration,
                             final UtilAnimationListener listener) {
        try {
            final ObjectAnimator an = ObjectAnimator.ofFloat(view, "alpha", 1, 1);
            an.setDuration(duration);
            an.setInterpolator(new AccelerateDecelerateInterpolator());
            an.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    listener.onAnimationEnded();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            an.start();
        } catch (Exception e) {
            if (listener != null) {
                listener.onAnimationEnded();
            }
        }

    }

    static int count, cityIndex, bIndex;
    public static Drawable getRandomBackgroundImage(Context ctx) {
        bIndex = random.nextInt(14);
        switch (bIndex) {
            case 0:
                return ctx.getResources().getDrawable(R.drawable.back1);
            case 1:
                return ctx.getResources().getDrawable(R.drawable.back2);
            case 2:
                return ctx.getResources().getDrawable(R.drawable.back3);
            case 3:
                return ctx.getResources().getDrawable(R.drawable.back4);
            case 4:
                return ctx.getResources().getDrawable(R.drawable.back5);
            case 5:
                return ctx.getResources().getDrawable(R.drawable.back6);
            case 6:
                return ctx.getResources().getDrawable(R.drawable.back7);
            case 7:
                return ctx.getResources().getDrawable(R.drawable.back8);
            case 8:
                return ctx.getResources().getDrawable(R.drawable.back9);
            case 9:
                return ctx.getResources().getDrawable(R.drawable.back10);
            case 10:
                return ctx.getResources().getDrawable(R.drawable.back11);
            case 11:
                return ctx.getResources().getDrawable(R.drawable.back12);
            case 12:
                return ctx.getResources().getDrawable(R.drawable.back13);
            case 13:
                return ctx.getResources().getDrawable(R.drawable.back14);
            case 14:
                return ctx.getResources().getDrawable(R.drawable.back15);

        }
        return ctx.getResources().getDrawable(R.drawable.back11);
    }
    public static Drawable getRandomCityImage(final Context ctx) {
        cityIndex = random.nextInt(21);
        switch (cityIndex) {
            case 0:
                return ctx.getResources().getDrawable(R.drawable.city1);
            case 1:
                return ctx.getResources().getDrawable(R.drawable.city2);
            case 2:
                return ctx.getResources().getDrawable(R.drawable.city3);
            case 3:
                return ctx.getResources().getDrawable(R.drawable.city4);
            case 4:
                return ctx.getResources().getDrawable(R.drawable.city5);
            case 5:
                return ctx.getResources().getDrawable(R.drawable.city6);
            case 6:
                return ctx.getResources().getDrawable(R.drawable.city7);
            case 7:
                return ctx.getResources().getDrawable(R.drawable.city8);
            case 8:
                return ctx.getResources().getDrawable(R.drawable.city9);
            case 9:
                return ctx.getResources().getDrawable(R.drawable.city10);
            case 10:
                return ctx.getResources().getDrawable(R.drawable.city11);
            case 11:
                return ctx.getResources().getDrawable(R.drawable.city12);
            case 12:
                return ctx.getResources().getDrawable(R.drawable.city13);
            case 13:
                return ctx.getResources().getDrawable(R.drawable.city14);
            case 14:
                return ctx.getResources().getDrawable(R.drawable.city15);
            case 15:
                return ctx.getResources().getDrawable(R.drawable.city16);
            case 16:
                return ctx.getResources().getDrawable(R.drawable.city17);
            case 17:
                return ctx.getResources().getDrawable(R.drawable.city18);
            case 18:
                return ctx.getResources().getDrawable(R.drawable.city19);
            case 19:
                return ctx.getResources().getDrawable(R.drawable.city20);
            case 20:
                return ctx.getResources().getDrawable(R.drawable.city21);
            case 21:
                return ctx.getResources().getDrawable(R.drawable.city22);

        }
        return ctx.getResources().getDrawable(R.drawable.city22);
    }
    public static void checkGPS(final Context ctx) {
        LocationManager lm = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            // Build the alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
            builder.setTitle("Location Services Not Active");
            builder.setMessage("Please enable Location Services and GPS");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Show location settings when the user acknowledges the alert dialog
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    ctx.startActivity(intent);
                }
            });
            Dialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }
    }
    public static interface GPSCheckListener {
        public void onGPSon();
    }

    public static File writeToFile(Context ctx, String data, String fileName)  throws Exception{

        File dir  = new File(ctx.getExternalFilesDir(null), fileName);
        File file = new File(dir, fileName);
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(file);
            stream.write(data.getBytes());
        } finally {
            stream.close();
        }
        Log.e("Util","## pdf file: " + file.getAbsolutePath() + " length: " + file.length());
        return file;
    }
}
