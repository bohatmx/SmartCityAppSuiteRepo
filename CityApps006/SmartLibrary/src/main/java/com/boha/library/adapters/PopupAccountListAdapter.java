package com.boha.library.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.dto.AccountDTO;
import com.boha.library.util.Statics;

import java.util.List;

/**
 * Created by Nkululeko on 2016/08/18.
 */
public class PopupAccountListAdapter extends ArrayAdapter<AccountDTO> {


    private final LayoutInflater mInflater;
    private final int mLayoutRes;
    private List<AccountDTO> mList;
    private Context ctx;
    private int primaryColorDark;
    static final String LOG = PopupComplaintCategoryListAdapter.class.getSimpleName();

    public PopupAccountListAdapter(Context context, int textViewResourceId,
                                             List<AccountDTO> list/*, int primaryColorDark*/) {
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
        item.txtString.setText(p.getAccountNumber() + " " +
                p.getCustomerAccountName().substring(0, Math.min(p.getCustomerAccountName().length(), 20)));

        Drawable d = ContextCompat.getDrawable(ctx, R.drawable.account_2x);
        d.setColorFilter(new
                PorterDuffColorFilter(Color.parseColor("BLACK"), PorterDuff.Mode.MULTIPLY));
        item.image.setImageDrawable(d);

       // item.image.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.accounts_statement));
        //item.image.setColorFilter(primaryColorDark, PorterDuff.Mode.SRC_IN);
        Statics.setRobotoFontLight(ctx, item.txtString);
        return (convertView);
    }

}
