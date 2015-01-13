package com.boha.library.util;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
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

import com.boha.cityapps.R;
import com.boha.library.adapters.PopupListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by aubreyM on 15/01/13.
 */
public class Util {

    public static Drawable getRandomImage(Context ctx) {
        if (images == null) {
            loadList(ctx);
        }
        int index = random.nextInt(15);
        if (index == lastIndex) {
            getRandomImage(ctx);
        }
        lastIndex = index;
        return images.get(index);
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

    private static List<Drawable> images, banners;
    private static int lastIndex, lastBannerIndex;

    private static void loadList(Context ctx) {
        images = new ArrayList<>();
        images.add(ctx.getResources().getDrawable(R.drawable.durban2));
        images.add(ctx.getResources().getDrawable(R.drawable.durban3));
        images.add(ctx.getResources().getDrawable(R.drawable.durban4));
        images.add(ctx.getResources().getDrawable(R.drawable.durban5));

        images.add(ctx.getResources().getDrawable(R.drawable.durban6));
        images.add(ctx.getResources().getDrawable(R.drawable.durban7));
        images.add(ctx.getResources().getDrawable(R.drawable.durban8));
        images.add(ctx.getResources().getDrawable(R.drawable.durban9));

        images.add(ctx.getResources().getDrawable(R.drawable.durban11));
        images.add(ctx.getResources().getDrawable(R.drawable.durban12));
        images.add(ctx.getResources().getDrawable(R.drawable.durban13));
        images.add(ctx.getResources().getDrawable(R.drawable.durban14));

        images.add(ctx.getResources().getDrawable(R.drawable.durban15));
        images.add(ctx.getResources().getDrawable(R.drawable.durban16));
        images.add(ctx.getResources().getDrawable(R.drawable.durban17));

    }
    private static void loadBanners(Context ctx) {
        banners = new ArrayList<>();
        banners.add(ctx.getResources().getDrawable(R.drawable.banner1));
        banners.add(ctx.getResources().getDrawable(R.drawable.banner2));
        banners.add(ctx.getResources().getDrawable(R.drawable.banner3));
        banners.add(ctx.getResources().getDrawable(R.drawable.banner4));


    }

    static Random random = new Random(System.currentTimeMillis());
    public static int getWindowWidth(Activity activity) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
        return width;
    }

    public static int getWindowHeight(Activity activity) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        return height;
    }

    public static void showErrorToast(Context ctx, String caption) {
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.toast, null);
        TextView txt = (TextView) view.findViewById(R.id.MONTOAST_text);
        TextView ind = (TextView) view.findViewById(R.id.MONTOAST_indicator);
        ind.setText("E");
        Statics.setRobotoFontLight(ctx, txt);
        ind.setBackground(ctx.getResources().getDrawable(R.drawable.xred_oval_small));
        txt.setTextColor(ctx.getResources().getColor(R.color.absa_red));
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
        ind.setText("M");
        ind.setBackground(ctx.getResources().getDrawable(R.drawable.xblue_oval_small));
        txt.setTextColor(ctx.getResources().getColor(R.color.blue));
        txt.setText(caption);
        Toast customtoast = new Toast(ctx);

        customtoast.setView(view);
        customtoast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        customtoast.setDuration(Toast.LENGTH_SHORT);
        customtoast.show();
    }

    public static View getHeroView(Context ctx, String caption) {
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.hero_image, null);
        ImageView img = (ImageView) v.findViewById(R.id.HERO_image);
        TextView txt = (TextView) v.findViewById(R.id.HERO_caption);
        img.setImageDrawable(getRandomImage(ctx));
        txt.setText(caption);
        return v;
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
        img.setImageDrawable(getRandomBanner(ctx));

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


    public static void flashOnce(View view, long duration, final UtilAnimationListener listener) {
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

    }


    public static void flashSeveralTimes(final View view,
                                         final long duration, final int max,
                                         final UtilAnimationListener listener) {
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

    }

    static int count;

    public interface UtilAnimationListener {
        public void onAnimationEnded();
    }
}
