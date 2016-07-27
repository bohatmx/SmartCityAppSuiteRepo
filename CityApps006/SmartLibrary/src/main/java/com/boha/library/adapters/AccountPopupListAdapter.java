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
import com.boha.library.dto.AccountDTO;
import com.boha.library.dto.ComplaintCategoryDTO;
import com.boha.library.util.Statics;
import com.boha.library.util.Util;

import java.util.List;

/**
 * Created by Nkululeko on 2016/06/29.
 */
public class AccountPopupListAdapter extends ArrayAdapter<AccountDTO> {


    private final LayoutInflater mInflater;
    private final int mLayoutRes;
    private List<AccountDTO> mList;
    private Context ctx;
    private int primaryColorDark;
    static final String LOG = ComplaintCategoryPopupListAdapter.class.getSimpleName();

    public AccountPopupListAdapter(Context context, int textViewResourceId,
                                             List<AccountDTO> list, int primaryColorDark) {
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


        final AccountDTO p = mList.get(position);
        item.txtString.setText(p.getAccountNumber());

        Util.setComplaintCategoryIcon(p.getAccountNumber(),item.image, ctx);

        item.image.setColorFilter(primaryColorDark, PorterDuff.Mode.SRC_IN);
          setIcon(item.txtString.getText().toString(), item.image, ctx);
        Statics.setRobotoFontLight(ctx, item.txtString);
        return (convertView);
    }

    public static void setIcon(String p, ImageView image, Context ctx) {
        image.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.elogo));
    }
}
