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
import android.widget.ImageView;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.dto.ComplaintTypeDTO;
import com.boha.library.util.Util;

import java.util.List;

/**
 * Created by aubreyM on 14/12/17.
 */
public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.ComplaintTypeViewHolder> {

    public interface ComplaintTypeListener {
        void onComplaintTypeClicked(ComplaintTypeDTO complaintType);

    }

    private ComplaintTypeListener mListener;
    private List<ComplaintTypeDTO> complaintTypes;
    private Context ctx;

    public SubCategoryAdapter(List<ComplaintTypeDTO> complaintTypes, Context ctx, ComplaintTypeListener listener) {
        this.complaintTypes = complaintTypes;
        this.mListener = listener;
        this.ctx = ctx;
    }

    @Override
    public ComplaintTypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sub_category_item, parent, false);
        return new ComplaintTypeViewHolder(v);
    }

    String BURST_PIPE = "Burst Pipe";
    @Override
    public void onBindViewHolder(final ComplaintTypeViewHolder holder, final int position) {

        final ComplaintTypeDTO p = complaintTypes.get(position);
        holder.complaintType.setText(p.getComplaintTypeName());
        setIcon(p.getComplaintTypeName(),holder.icon, ctx);

       if (p.getComplaintTypeName().matches("Burst"))  {
           holder.complaintType.setText(BURST_PIPE);
       }
        holder.main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onComplaintTypeClicked(p);
            }
        });
        Util.scaleDownAndUp(holder.main, 300);
    }

    @Override
    public int getItemCount() {
        return complaintTypes == null ? 0 : complaintTypes.size();
    }

    public class ComplaintTypeViewHolder extends RecyclerView.ViewHolder {
        protected TextView complaintType;
        protected ImageView icon;
        protected View main;


        public ComplaintTypeViewHolder(View itemView) {
            super(itemView);
            complaintType = (TextView) itemView.findViewById(R.id.name);
            icon = (ImageView) itemView.findViewById(R.id.icon);
            main =  itemView.findViewById(R.id.main);
        }

    }
    private static void setDisplayName(String displayName,/*, ImageView image,*/ Context ctx) {
        if (displayName == "Burst") {
            displayName.replace("Burst", "Burst Pipe");
        }
    }
    public static void setIcon(String p, ImageView image, Context ctx) {
        if (p.equalsIgnoreCase("Water")) {
            Drawable d = ContextCompat.getDrawable(ctx, R.drawable.zwater);
            d.setColorFilter(new
                    PorterDuffColorFilter(Color.parseColor("BLACK"), PorterDuff.Mode.MULTIPLY));
            image.setImageDrawable(d);
        }
        if (p.equalsIgnoreCase("Burst Pipe")) {
            Drawable d = ContextCompat.getDrawable(ctx, R.drawable.zburst_pipe);
            d.setColorFilter(new
                    PorterDuffColorFilter(Color.parseColor("BLACK"), PorterDuff.Mode.MULTIPLY));
            image.setImageDrawable(d);
        }
        if (p.equalsIgnoreCase("Burst")) {
            p = "Burst Pipe";
            Drawable d = ContextCompat.getDrawable(ctx, R.drawable.zburst_pipe);
            d.setColorFilter(new
                    PorterDuffColorFilter(Color.parseColor("BLACK"), PorterDuff.Mode.MULTIPLY));
            image.setImageDrawable(d);
        }
        if (p.equalsIgnoreCase("Leaking Pipe")) {
            Drawable d = ContextCompat.getDrawable(ctx, R.drawable.zleaking_pipe);
            d.setColorFilter(new
                    PorterDuffColorFilter(Color.parseColor("BLACK"), PorterDuff.Mode.MULTIPLY));
            image.setImageDrawable(d);
        }
        if (p.equalsIgnoreCase("No Water")) {
            Drawable d = ContextCompat.getDrawable(ctx, R.drawable.zno_water);
            d.setColorFilter(new
                    PorterDuffColorFilter(Color.parseColor("BLACK"), PorterDuff.Mode.MULTIPLY));
            image.setImageDrawable(d);
        }
        if (p.equalsIgnoreCase("Water Pressure Problem")) {
            Drawable d = ContextCompat.getDrawable(ctx, R.drawable.zwater_pressure);
            d.setColorFilter(new
                    PorterDuffColorFilter(Color.parseColor("BLACK"), PorterDuff.Mode.MULTIPLY));
            image.setImageDrawable(d);
        }
        if (p.equalsIgnoreCase("Air")) {
            Drawable d = ContextCompat.getDrawable(ctx, R.drawable.zair_polluttion);
            d.setColorFilter(new
                    PorterDuffColorFilter(Color.parseColor("BLACK"), PorterDuff.Mode.MULTIPLY));
            image.setImageDrawable(d);
        }
        if (p.equalsIgnoreCase("Beach")) {
            Drawable d = ContextCompat.getDrawable(ctx, R.drawable.zbeach_pollution);
            d.setColorFilter(new
                    PorterDuffColorFilter(Color.parseColor("BLACK"), PorterDuff.Mode.MULTIPLY));
            image.setImageDrawable(d);
        }
        if (p.equalsIgnoreCase("Land")) {
            Drawable d = ContextCompat.getDrawable(ctx, R.drawable.zland_pollution);
            d.setColorFilter(new
                    PorterDuffColorFilter(Color.parseColor("BLACK"), PorterDuff.Mode.MULTIPLY));
            image.setImageDrawable(d);
        }
        if (p.equalsIgnoreCase("Burst Pipe")) {
            Drawable d = ContextCompat.getDrawable(ctx, R.drawable.zburst_pipe);
            d.setColorFilter(new
                    PorterDuffColorFilter(Color.parseColor("BLACK"), PorterDuff.Mode.MULTIPLY));
            image.setImageDrawable(d);
        }
        if (p.equalsIgnoreCase("Missing Manhole Cover")) {
            Drawable d = ContextCompat.getDrawable(ctx, R.drawable.zmanhole_covers);
            d.setColorFilter(new
                    PorterDuffColorFilter(Color.parseColor("BLACK"), PorterDuff.Mode.MULTIPLY));
            image.setImageDrawable(d);
        }
        if (p.equalsIgnoreCase("Road Overflowing")) {
            Drawable d = ContextCompat.getDrawable(ctx, R.drawable.zoverflow);
            d.setColorFilter(new
                    PorterDuffColorFilter(Color.parseColor("BLACK"), PorterDuff.Mode.MULTIPLY));
            image.setImageDrawable(d);
        }
        if (p.equalsIgnoreCase("Pothole")) {
            Drawable d = ContextCompat.getDrawable(ctx, R.drawable.zpothole);
            d.setColorFilter(new
                    PorterDuffColorFilter(Color.parseColor("BLACK"), PorterDuff.Mode.MULTIPLY));
            image.setImageDrawable(d);
        }
        if (p.equalsIgnoreCase("Sinkhole")) {
            Drawable d = ContextCompat.getDrawable(ctx, R.drawable.zsinkhole);
            d.setColorFilter(new
                    PorterDuffColorFilter(Color.parseColor("BLACK"), PorterDuff.Mode.MULTIPLY));
            image.setImageDrawable(d);
        }
        if (p.equalsIgnoreCase("All Lights Out of Order")) {
            Drawable d = ContextCompat.getDrawable(ctx, R.drawable.zall_lights);
            d.setColorFilter(new
                    PorterDuffColorFilter(Color.parseColor("BLACK"), PorterDuff.Mode.MULTIPLY));
            image.setImageDrawable(d);
        }
        if (p.equalsIgnoreCase("Flashing")) {
            Drawable d = ContextCompat.getDrawable(ctx, R.drawable.zflashing);
            d.setColorFilter(new
                    PorterDuffColorFilter(Color.parseColor("BLACK"), PorterDuff.Mode.MULTIPLY));
            image.setImageDrawable(d);
        }
        if (p.equalsIgnoreCase("Manhole Cover ")) {
            Drawable d = ContextCompat.getDrawable(ctx, R.drawable.zmanhole_cover);
            d.setColorFilter(new
                    PorterDuffColorFilter(Color.parseColor("BLACK"), PorterDuff.Mode.MULTIPLY));
            image.setImageDrawable(d);
        }
        if (p.equalsIgnoreCase("Overflowing"/*"Over Flaw"*/)) {
            Drawable d = ContextCompat.getDrawable(ctx, R.drawable.zoverflow);
            d.setColorFilter(new
                    PorterDuffColorFilter(Color.parseColor("BLACK"), PorterDuff.Mode.MULTIPLY));
            image.setImageDrawable(d);
        }
        if (p.equalsIgnoreCase("Overgrown Area")) {
            Drawable d = ContextCompat.getDrawable(ctx, R.drawable.zovergrown_area);
            d.setColorFilter(new
                    PorterDuffColorFilter(Color.parseColor("BLACK"), PorterDuff.Mode.MULTIPLY));
            image.setImageDrawable(d);
        }
        if (p.equalsIgnoreCase("Power Failure")) {
            Drawable d = ContextCompat.getDrawable(ctx, R.drawable.zpower_failure);
            d.setColorFilter(new
                    PorterDuffColorFilter(Color.parseColor("BLACK"), PorterDuff.Mode.MULTIPLY));
            image.setImageDrawable(d);
        }
        if (p.equalsIgnoreCase("Potholes")) {
            Drawable d = ContextCompat.getDrawable(ctx, R.drawable.zpotholes);
            d.setColorFilter(new
                    PorterDuffColorFilter(Color.parseColor("BROWN"), PorterDuff.Mode.MULTIPLY));
            image.setImageDrawable(d);
        }
        if (p.equalsIgnoreCase("High Bill")) {
            Drawable d = ContextCompat.getDrawable(ctx, R.drawable.zhigh_bill);
            d.setColorFilter(new
                    PorterDuffColorFilter(Color.parseColor("RED"), PorterDuff.Mode.MULTIPLY));
            image.setImageDrawable(d);
        }
        if (p.equalsIgnoreCase("Traffic Light Management")) {
            Drawable d = ContextCompat.getDrawable(ctx, R.drawable.ztraffic_light_management);
            d.setColorFilter(new
                    PorterDuffColorFilter(Color.parseColor("BLACK"), PorterDuff.Mode.MULTIPLY));
            image.setImageDrawable(d);
        }
        if (p.equalsIgnoreCase("Blocked Drains")) {
            Drawable d = ContextCompat.getDrawable(ctx, R.drawable.zblocked_drains);
            d.setColorFilter(new
                    PorterDuffColorFilter(Color.parseColor("BLACK"), PorterDuff.Mode.MULTIPLY));
            image.setImageDrawable(d);
        }
        if (p.equalsIgnoreCase("Manhole Covers")) {
            Drawable d = ContextCompat.getDrawable(ctx, R.drawable.zmanhole_covers);
            d.setColorFilter(new
                    PorterDuffColorFilter(Color.parseColor("RED"), PorterDuff.Mode.MULTIPLY));
            image.setImageDrawable(d);
        }
        if (p.equalsIgnoreCase("Illegal Dumping")) {
            Drawable d = ContextCompat.getDrawable(ctx, R.drawable.zillegal_dumping);
            d.setColorFilter(new
                    PorterDuffColorFilter(Color.parseColor("BLACK"), PorterDuff.Mode.MULTIPLY));
            image.setImageDrawable(d);
        }
        if (p.equalsIgnoreCase("Sanitation")) {
            Drawable d = ContextCompat.getDrawable(ctx, R.drawable.zsanitation);
            d.setColorFilter(new
                    PorterDuffColorFilter(Color.parseColor("BLACK"), PorterDuff.Mode.MULTIPLY));
            image.setImageDrawable(d);
        }
        if (p.equalsIgnoreCase("Nuisance Reporting")) {
            Drawable d = ContextCompat.getDrawable(ctx, R.drawable.znuisance_reporting);
            d.setColorFilter(new
                    PorterDuffColorFilter(Color.parseColor("BLACK"), PorterDuff.Mode.MULTIPLY));
            image.setImageDrawable(d);
        }
        if (p.equalsIgnoreCase("No Supply (multiple customers)")) {
            Drawable d = ContextCompat.getDrawable(ctx, R.drawable.power_failure_all);
            d.setColorFilter(new
                    PorterDuffColorFilter(Color.parseColor("BLACK"), PorterDuff.Mode.MULTIPLY));
            image.setImageDrawable(d);
        }
        if (p.equalsIgnoreCase("No Supply (only affecting me)")) {
            Drawable d = ContextCompat.getDrawable(ctx, R.drawable.home_shortage);
            d.setColorFilter(new
                    PorterDuffColorFilter(Color.parseColor("BLACK"), PorterDuff.Mode.MULTIPLY));
            image.setImageDrawable(d);
        }
        if (p.equalsIgnoreCase("Dangerous Situation")) {
            Drawable d = ContextCompat.getDrawable(ctx, R.drawable.danger);
            d.setColorFilter(new
                    PorterDuffColorFilter(Color.parseColor("BLACK"), PorterDuff.Mode.MULTIPLY));
            image.setImageDrawable(d);
        }
        if (p.equalsIgnoreCase("Street Light")) {
            Drawable d = ContextCompat.getDrawable(ctx, R.drawable.streetlight32);
            d.setColorFilter(new
                    PorterDuffColorFilter(Color.parseColor("BLACK"), PorterDuff.Mode.MULTIPLY));
            image.setImageDrawable(d);
        }

        if (p.equalsIgnoreCase("Uncategorized Complaint")) {
            image.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.ic_action_bell));
        }

    }
    static final String LOG = SubCategoryAdapter.class.getSimpleName();
}
