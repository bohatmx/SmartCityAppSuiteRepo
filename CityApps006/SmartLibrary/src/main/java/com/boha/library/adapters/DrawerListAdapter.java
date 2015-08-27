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

import java.util.List;

public class DrawerListAdapter extends ArrayAdapter<String> {


    private final LayoutInflater mInflater;
    private final int mLayoutRes;
    private List<String> mList;
    private Context ctx;
    private int primaryColorDark;
    static final String LOG = DrawerListAdapter.class.getSimpleName();

    public DrawerListAdapter(Context context, int textViewResourceId,
                             List<String> list, int primaryColorDark) {
        super(context, textViewResourceId, list);
        this.mLayoutRes = textViewResourceId;
        this.primaryColorDark = primaryColorDark;
        mList = list;
        ctx = context;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    View view;


    static class ViewHolderItem {
        TextView txtString, txtCount;
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
                    .findViewById(R.id.DI_txtTitle);
            item.txtCount = (TextView) convertView
                    .findViewById(R.id.DI_txtCount);

            item.image = (ImageView) convertView
                    .findViewById(R.id.DI_image);

            convertView.setTag(item);
        } else {
            item = (ViewHolderItem) convertView.getTag();
        }

        item.txtCount.setVisibility(View.GONE);
        final String p = mList.get(position);
        item.txtString.setText(p);
        if (p.equalsIgnoreCase(ctx.getString(R.string.payments))) {
            item.image.setImageDrawable(ContextCompat.getDrawable(
                    ctx,android.R.drawable.ic_popup_reminder));
        }
        if (p.equalsIgnoreCase(ctx.getString(R.string.city_alerts))) {
            item.image.setImageDrawable(ContextCompat.getDrawable(
                    ctx,android.R.drawable.ic_dialog_alert));
        }
        if (p.equalsIgnoreCase(ctx.getString(R.string.my_complaints))) {
            item.image.setImageDrawable(ContextCompat.getDrawable(
                    ctx,android.R.drawable.ic_btn_speak_now));
        }
        if (p.equalsIgnoreCase(ctx.getString(R.string.my_accounts))) {
            item.image.setImageDrawable(ContextCompat.getDrawable(
                    ctx,android.R.drawable.ic_dialog_info));
        }
        if (p.equalsIgnoreCase(ctx.getString(R.string.city_gallery))) {
            item.image.setImageDrawable(ContextCompat.getDrawable(
                    ctx,android.R.drawable.ic_menu_gallery));
        }
        if (p.equalsIgnoreCase(ctx.getString(R.string.complaints_around_me))) {
            item.image.setImageDrawable(ContextCompat.getDrawable(
                    ctx,android.R.drawable.ic_dialog_map));
        }
        if (p.equalsIgnoreCase(ctx.getString(R.string.faq))) {
            item.image.setImageDrawable(ContextCompat.getDrawable(
                    ctx,android.R.drawable.ic_menu_help));
        }
        if (p.equalsIgnoreCase(ctx.getString(R.string.city_news))) {
            item.image.setImageDrawable(ContextCompat.getDrawable(
                    ctx,android.R.drawable.ic_menu_info_details));
        }
        if (p.equalsIgnoreCase(ctx.getString(R.string.make_complaint))) {
            item.image.setImageDrawable(ContextCompat.getDrawable(
                    ctx,android.R.drawable.ic_menu_edit));
        }

        if (primaryColorDark != 0) {
            item.image.setColorFilter(primaryColorDark, PorterDuff.Mode.SRC_IN);
        }

       // Statics.setRobotoFontLight(ctx, item.txtString);
        return (convertView);
    }

}
