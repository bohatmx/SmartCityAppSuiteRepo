package com.boha.library.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.dto.AlertDTO;
import com.boha.library.dto.AlertTypeDTO;
import com.boha.library.util.Statics;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by aubreyM on 14/12/17.
 */
public class AlertRecyclerAdapter extends RecyclerView.Adapter<AlertRecyclerAdapter.AlertViewHolder> {

    public interface AlertAdapterListener {
        void onAlertClicked(AlertDTO alert);

    }

    private AlertAdapterListener mListener;
    private List<AlertDTO> alerts;
    private Context ctx;

    public AlertRecyclerAdapter(List<AlertDTO> alerts, Context ctx, AlertAdapterListener listener) {
        this.alerts = alerts;
        this.mListener = listener;
        this.ctx = ctx;
    }

    @Override
    public AlertViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.alert_item, parent, false);
        return new AlertViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final AlertViewHolder holder, final int position) {

        final AlertDTO p = alerts.get(position);

        holder.txtColor.setText("" + (position + 1));
        holder.txtType.setVisibility(View.GONE);
        holder.txtDate.setText(sdfDate.format(p.getUpdated()));
        holder.txtTime.setText(sdfTime.format(p.getUpdated()));
        holder.txtDesc.setText(p.getDescription());
        holder.position = position;
        if (p.getAlertType() != null) {
            switch (p.getAlertType().getColor()) {
                case AlertTypeDTO.GREEN:
                    holder.txtColor.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xgreen_oval_small));
                    break;
                case AlertTypeDTO.AMBER:
                    holder.txtColor.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xamber_oval_small));
                    break;
                case AlertTypeDTO.RED:
                    holder.txtColor.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xred_oval_small));
                    break;
            }
        }
        holder.txtColor.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xindigo_oval_small));
//
//       switch(position) {
//            case CityApplication.THEME_INDIGO:
//                holder.txtColor.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xindigo_oval_small));
//                break;
//            case CityApplication.THEME_GREEN:
//                holder.txtColor.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xgreen_oval_small));
//                break;
//            case CityApplication.THEME_BROWN:
//                holder.txtColor.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xbrown_oval_small));
//                break;
//            case CityApplication.THEME_AMBER:
//                holder.txtColor.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xamber_oval_small));
//                break;
//            case CityApplication.THEME_PURPLE:
//                holder.txtColor.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xpurple_oval_small));
//                break;
//            case CityApplication.THEME_LIME:
//                holder.txtColor.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xlime_oval_small));
//                break;
//            case CityApplication.THEME_GREY:
//                holder.txtColor.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xgrey_oval_small));
//                break;
//            case CityApplication.THEME_BLUE:
//                holder.txtColor.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xblue_oval_small));
//                break;
//            case CityApplication.THEME_BLUE_GRAY:
//                holder.txtColor.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xblue_gray_oval_small));
//                break;
//            case CityApplication.THEME_TEAL:
//                holder.txtColor.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xteal_oval_small));
//                break;
//            case CityApplication.THEME_CYAN:
//                holder.txtColor.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xcyan_oval_small));
//                break;
//            case CityApplication.THEME_ORANGE:
//                holder.txtColor.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xorange_oval_small));
//                break;
//            case CityApplication.THEME_PINK:
//                holder.txtColor.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xpink_oval_small));
//                break;
//            case CityApplication.THEME_RED:
//                holder.txtColor.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xred_oval_small));
//                break;
//        }

        if (p.getAlertData() != null) {
            holder.webView.setVisibility(View.VISIBLE);
            holder.webView.loadData(p.getAlertData(), TEXT, UTF);
        } else {
            holder.webView.setVisibility(View.GONE);
        }
        //get first image if available ...
        if (p.getAlertImageList() != null && !p.getAlertImageList().isEmpty()) {
            holder.image.setVisibility(View.VISIBLE);
            String url = p.getAlertImageList().get(0).getUrl();
            Picasso.with(ctx).load(url).into(holder.image);
        } else {
            holder.image.setVisibility(View.GONE);
        }
        Statics.setRobotoFontLight(ctx,holder.txtDesc);
//        Util.scaleDownAndUp(holder.mainView,300);

        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAlertClicked(p);
            }
        });

    }
    public  void setComplaintCategoryIcon(String p, ImageView image) {
        if (p.equalsIgnoreCase("Water")) {
            Drawable d = ContextCompat.getDrawable(ctx, R.drawable.zwater);
            d.setColorFilter(new
                    PorterDuffColorFilter(Color.parseColor("BLACK"), PorterDuff.Mode.MULTIPLY));
            image.setImageDrawable(d);
        }
        if (p.equalsIgnoreCase("Pollution")) {
            image.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.zpollution));
            Drawable d = ContextCompat.getDrawable(ctx, R.drawable.zpollution);
            d.setColorFilter(new
                    PorterDuffColorFilter(Color.parseColor("BLACK"), PorterDuff.Mode.MULTIPLY));
            image.setImageDrawable(d);
        }
        if (p.equalsIgnoreCase("Traffic")) {
            Drawable d = ContextCompat.getDrawable(ctx, R.drawable.ztraffic);
            d.setColorFilter(new
                    PorterDuffColorFilter(Color.parseColor("BLACK"), PorterDuff.Mode.MULTIPLY));
            image.setImageDrawable(d);
        }
        if (p.equalsIgnoreCase("Road")) {
            Drawable d = ContextCompat.getDrawable(ctx, R.drawable.zroad);
            d.setColorFilter(new
                    PorterDuffColorFilter(Color.parseColor("BLACK"), PorterDuff.Mode.MULTIPLY));
            image.setImageDrawable(d);
        }
        if (p.equalsIgnoreCase("Waste Water")) {
            Drawable d = ContextCompat.getDrawable(ctx, R.drawable.zwaste_water);
            d.setColorFilter(new
                    PorterDuffColorFilter(Color.parseColor("BLACK"), PorterDuff.Mode.MULTIPLY));
            image.setImageDrawable(d);
        }

    }
    @Override
    public int getItemCount() {
        return alerts == null ? 0 : alerts.size();
    }

    public class AlertViewHolder extends RecyclerView.ViewHolder {
        protected ImageView image;
        protected TextView txtType, txtColor, txtDate, txtDesc, txtTime;
        protected int position;
        protected View mainView;
        protected WebView webView;


        public AlertViewHolder(View item) {
            super(item);
            txtType = (TextView) item.findViewById(R.id.AI_type);
            txtDesc = (TextView) item.findViewById(R.id.AI_desc);
            txtColor = (TextView) item.findViewById(R.id.AI_color);
            txtDate = (TextView) item.findViewById(R.id.AI_date);
            txtTime = (TextView) item.findViewById(R.id.AI_time);
            image = (ImageView) item.findViewById(R.id.AI_image);
            webView = (WebView) item.findViewById(R.id.AI_webView);
            mainView = item.findViewById(R.id.AI_main);
        }

    }

    static final String LOG = AlertRecyclerAdapter.class.getSimpleName();
    static final Locale loc = Locale.getDefault();
    static final SimpleDateFormat sdfDate = new SimpleDateFormat("EEE dd MMM yyyy", loc);
    static final SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm", loc);
    private static final String TEXT = "text/html", UTF = "UTF-8";
}
