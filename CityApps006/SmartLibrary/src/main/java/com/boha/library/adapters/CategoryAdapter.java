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
        holder.categoryName.setText(p.getComplaintCategoryName().trim());
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

        if (p.equalsIgnoreCase("Pollution ")) {
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

        if (p.equalsIgnoreCase("Waste Water ")) {
            Drawable d = ContextCompat.getDrawable(ctx, R.drawable.zwaste_water);
            d.setColorFilter(new
                    PorterDuffColorFilter(Color.parseColor("BLACK"), PorterDuff.Mode.MULTIPLY));
            image.setImageDrawable(d);
        }

        if (p.equalsIgnoreCase("Electricity")) {
            Drawable d = ContextCompat.getDrawable(ctx, R.drawable.zpower_failure);
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
