package com.boha.citizenapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.boha.citizenapp.R;
import com.boha.citylibrary.util.Statics;

import java.util.List;

public class PopupListAdapter extends ArrayAdapter<String> {


    private final LayoutInflater mInflater;
    private final int mLayoutRes;
    private List<String> mList;
    private Context ctx;
    private String title;
    private boolean showAlternateIcon;
    static final String LOG = PopupListAdapter.class.getSimpleName();

    public PopupListAdapter(Context context, int textViewResourceId,
                            List<String> list, boolean showAlternateIcon) {
        super(context, textViewResourceId, list);
        this.mLayoutRes = textViewResourceId;
        mList = list;
        this.showAlternateIcon = showAlternateIcon;
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
        if (showAlternateIcon) {
            item.image.setImageDrawable(ctx.getResources().getDrawable(R.drawable.xblue_oval_smaller));
        } else {
            item.image.setImageDrawable(ctx.getResources().getDrawable(android.R.drawable.ic_input_add));
        }
        final String p = mList.get(position);
        item.txtString.setText(p);
        Statics.setRobotoFontLight(ctx, item.txtString);
        return (convertView);
    }

}
