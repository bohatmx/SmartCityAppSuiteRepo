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
import com.boha.library.dto.ComplaintCategoryDTO;
import com.boha.library.util.Statics;
import com.boha.library.util.Util;

import java.util.List;

/**
 * Created by aubreyM on 14/12/17.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryyViewHolder> {

    public interface CategoryAdapterListener {
        void onCategoryClicked(ComplaintCategoryDTO category);

    }

    private CategoryAdapterListener mListener;
    private List<ComplaintCategoryDTO> categories;
    private Context ctx;

    public CategoryAdapter(List<ComplaintCategoryDTO> categories, Context ctx, CategoryAdapterListener listener) {
        this.categories = categories;
        this.mListener = listener;
        this.ctx = ctx;
    }

    @Override
    public CategoryyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_item, parent, false);
        return new CategoryyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final CategoryyViewHolder holder, final int position) {

        final ComplaintCategoryDTO p = categories.get(position);
        holder.categoryName.setText(p.getComplaintCategoryName());
        setComplaintCategoryIcon(p.getComplaintCategoryName(),holder.icon );

        holder.main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCategoryClicked(p);
            }
        });

        Statics.setRobotoFontLight(ctx,holder.categoryName);
        Util.scaleDownAndUp(holder.main, 200);

    }
    public  void setComplaintCategoryIcon(String p, ImageView image) {
        if (p.equalsIgnoreCase("Water")) {
            Drawable d = ContextCompat.getDrawable(ctx, R.drawable.zwater);
            d.setColorFilter(new
                    PorterDuffColorFilter(Color.parseColor("BLACK"), PorterDuff.Mode.MULTIPLY));
            image.setImageDrawable(d);
        }
        if (p.equalsIgnoreCase("Burst")) {
            image.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.zburst_pipe));
            Drawable d = ContextCompat.getDrawable(ctx, R.drawable.zburst_pipe);
            d.setColorFilter(new
                    PorterDuffColorFilter(Color.parseColor("BLACK"), PorterDuff.Mode.MULTIPLY));
            image.setImageDrawable(d);
        }
        if (p.equalsIgnoreCase("Leaking Pipe")) {
            image.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.zleaking_pipe));
            Drawable d = ContextCompat.getDrawable(ctx, R.drawable.zleaking_pipe);
            d.setColorFilter(new
                    PorterDuffColorFilter(Color.parseColor("BLACK"), PorterDuff.Mode.MULTIPLY));
            image.setImageDrawable(d);
        }
        if (p.equalsIgnoreCase("No Water")) {
            image.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.zno_water));
            Drawable d = ContextCompat.getDrawable(ctx, R.drawable.zno_water);
            d.setColorFilter(new
                    PorterDuffColorFilter(Color.parseColor("BLACK"), PorterDuff.Mode.MULTIPLY));
            image.setImageDrawable(d);
        }
        if (p.equalsIgnoreCase("Water Pressure Problem")) {
            image.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.zwater_pressure));
            Drawable d = ContextCompat.getDrawable(ctx, R.drawable.zwater_pressure);
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
        if (p.equalsIgnoreCase("Air")) {
            image.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.zair_polluttion));
            Drawable d = ContextCompat.getDrawable(ctx, R.drawable.zair_polluttion);
            d.setColorFilter(new
                    PorterDuffColorFilter(Color.parseColor("BLACK"), PorterDuff.Mode.MULTIPLY));
            image.setImageDrawable(d);
        }
        if (p.equalsIgnoreCase("Beach")) {
            image.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.zbeach_pollution));
            Drawable d = ContextCompat.getDrawable(ctx, R.drawable.zbeach_pollution);
            d.setColorFilter(new
                    PorterDuffColorFilter(Color.parseColor("BLACK"), PorterDuff.Mode.MULTIPLY));
            image.setImageDrawable(d);
        }
        if (p.equalsIgnoreCase("Land")) {
            image.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.zland_pollution));
            Drawable d = ContextCompat.getDrawable(ctx, R.drawable.zland_pollution);
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
        if (p.equalsIgnoreCase("All Lights Out of Order")) {
            image.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.zall_lights));
            Drawable d = ContextCompat.getDrawable(ctx, R.drawable.zall_lights);
            d.setColorFilter(new
                    PorterDuffColorFilter(Color.parseColor("BLACK"), PorterDuff.Mode.MULTIPLY));
            image.setImageDrawable(d);
        }
        if (p.equalsIgnoreCase("Flashing")) {
            image.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.zflashing));
            Drawable d = ContextCompat.getDrawable(ctx, R.drawable.zflashing);
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
        if (p.equalsIgnoreCase("Missing ManHole cover")) {
            image.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.zmanhole_covers));
            Drawable d = ContextCompat.getDrawable(ctx, R.drawable.zmanhole_covers);
            d.setColorFilter(new
                    PorterDuffColorFilter(Color.parseColor("BLACK"), PorterDuff.Mode.MULTIPLY));
            image.setImageDrawable(d);
        }
        if (p.equalsIgnoreCase("Over flowing")) {
            image.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.zoverflow));
            Drawable d = ContextCompat.getDrawable(ctx, R.drawable.zoverflow);
            d.setColorFilter(new
                    PorterDuffColorFilter(Color.parseColor("BLACK"), PorterDuff.Mode.MULTIPLY));
            image.setImageDrawable(d);
        }
        if (p.equalsIgnoreCase("Pot Hole")) {
            image.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.zpothole));
            Drawable d = ContextCompat.getDrawable(ctx, R.drawable.zpothole);
            d.setColorFilter(new
                    PorterDuffColorFilter(Color.parseColor("BLACK"), PorterDuff.Mode.MULTIPLY));
            image.setImageDrawable(d);
        }
        if (p.equalsIgnoreCase("Sink Hole")) {
            image.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.zsinkhole));
            Drawable d = ContextCompat.getDrawable(ctx, R.drawable.zsinkhole);
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
        if (p.equalsIgnoreCase("Manhole Cover")) {
            image.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.zmanhole_cover));
            Drawable d = ContextCompat.getDrawable(ctx, R.drawable.zmanhole_cover);
            d.setColorFilter(new
                    PorterDuffColorFilter(Color.parseColor("BLACK"), PorterDuff.Mode.MULTIPLY));
            image.setImageDrawable(d);
        }
        if (p.equalsIgnoreCase("Over Flaw")) {
            image.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.zoverflow));
            Drawable d = ContextCompat.getDrawable(ctx, R.drawable.zoverflow);
            d.setColorFilter(new
                    PorterDuffColorFilter(Color.parseColor("BLACK"), PorterDuff.Mode.MULTIPLY));
            image.setImageDrawable(d);
        }

    }
    @Override
    public int getItemCount() {
        return categories == null ? 0 : categories.size();
    }

    public class CategoryyViewHolder extends RecyclerView.ViewHolder {
        protected TextView categoryName;
        protected ImageView icon;
        protected View main;


        public CategoryyViewHolder(View itemView) {
            super(itemView);
            categoryName = (TextView) itemView.findViewById(R.id.name);
            icon = (ImageView) itemView.findViewById(R.id.icon);
            main =  itemView.findViewById(R.id.main);
        }

    }

    static final String LOG = CategoryAdapter.class.getSimpleName();
}
