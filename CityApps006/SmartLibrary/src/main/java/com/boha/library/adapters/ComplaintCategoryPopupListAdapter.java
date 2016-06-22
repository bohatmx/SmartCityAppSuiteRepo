package com.boha.library.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.dto.ComplaintCategoryDTO;
import com.boha.library.util.Statics;
import com.boha.library.util.Util;

import java.util.List;

public class ComplaintCategoryPopupListAdapter extends ArrayAdapter<ComplaintCategoryDTO> {


    private final LayoutInflater mInflater;
    private final int mLayoutRes;
    private List<ComplaintCategoryDTO> mList;
    private Context ctx;
    private int primaryColorDark;
    static final String LOG = ComplaintCategoryPopupListAdapter.class.getSimpleName();

    public ComplaintCategoryPopupListAdapter(Context context, int textViewResourceId,
                                             List<ComplaintCategoryDTO> list, int primaryColorDark) {
        super(context, textViewResourceId, list);
        this.mLayoutRes = textViewResourceId;
        mList = list;
        this.primaryColorDark = primaryColorDark;
        ctx = context;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    View view;


    static class ViewHolderItem {
        TextView txtString;
        ImageView image;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        ViewHolderItem item;
        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutRes, null);
            item = new ViewHolderItem();
            item.txtString = (TextView) convertView
                    .findViewById(R.id.text1);

            item.image = (ImageView) convertView
                    .findViewById(R.id.image1);

            convertView.setTag(item);
        } else {
            item = (ViewHolderItem) convertView.getTag();
        }


        final ComplaintCategoryDTO p = mList.get(position);
        item.txtString.setText(p.getComplaintCategoryName());

        Util.setComplaintCategoryIcon(p.getComplaintCategoryName(),item.image, ctx);

        item.image.setColorFilter(primaryColorDark, PorterDuff.Mode.SRC_IN);
      //  setIcon(item.txtString.getText().toString(), item.image, ctx);
        Statics.setRobotoFontLight(ctx, item.txtString);
        return (convertView);
    }

    public static void setIcon(String p, ImageView image, Context ctx) {
        if (p.equalsIgnoreCase("Water")) {
            image.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.zwater));
        }
        if (p.equalsIgnoreCase("Burst Pipe")) {
            image.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.zburst_pipe));
        }
        if (p.equalsIgnoreCase("Overgrown Area")) {
            image.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.zovergrown_area));
        }
        if (p.equalsIgnoreCase("Power Failure")) {
            image.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.zpower_failure));
        }
        if (p.equalsIgnoreCase("Potholes")) {
            image.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.zpotholes));
        }
        if (p.equalsIgnoreCase("High Bill")) {
            image.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.zhigh_bill));
        }
        if (p.equalsIgnoreCase("Traffic Light Management")) {
            image.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.ztraffic_light_management));
        }
        if (p.equalsIgnoreCase("Blocked Drains")) {
            image.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.zblocked_drains));
        }
        if (p.equalsIgnoreCase("Manhole Covers")) {
            image.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.zmanhole_covers));
        }
        if (p.equalsIgnoreCase("Illegal Dumping")) {
            image.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.zillegal_dumping));
        }
        if (p.equalsIgnoreCase("Sanitation")) {
            image.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.zsanitation));
        }
        if (p.equalsIgnoreCase("Nuisance Reporting")) {
            image.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.znuisance_reporting));
        }
        if (p.equalsIgnoreCase("Uncategorized Complaint")) {
            image.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.ic_action_bell));
        }

    }

}
