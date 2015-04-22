package com.boha.library.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.dto.FaqTypeDTO;
import com.boha.library.util.Statics;

import java.util.List;

public class FaqTypeAdapter extends ArrayAdapter<FaqTypeDTO> {

    private final LayoutInflater mInflater;
    private final int mLayoutRes;
    private List<FaqTypeDTO> mList;
    private Context ctx;
    static final String LOG = FaqTypeAdapter.class.getSimpleName();

    public FaqTypeAdapter(Context context, int textViewResourceId,
                          List<FaqTypeDTO> list) {
        super(context, textViewResourceId, list);
        this.mLayoutRes = textViewResourceId;
        mList = list;
        ctx = context;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    View view;


    static class ViewHolderItem {
        protected TextView txtFaqType, txtNumber;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(final int position, View convertView, ViewGroup parent) {
        ViewHolderItem item;
        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutRes, null);
            item = new ViewHolderItem();
            item.txtNumber = (TextView) convertView.findViewById(R.id.FTYPE_number);
            item.txtFaqType = (TextView) convertView.findViewById(R.id.FTYPE_faqType);
            convertView.setTag(item);
        } else {
            item = (ViewHolderItem) convertView.getTag();
        }

        final FaqTypeDTO p = mList.get(position);
        item.txtNumber.setText("" + (position + 1));
        item.txtFaqType.setText(p.getFaqTypeName());
        Statics.setRobotoFontLight(ctx,item.txtFaqType);

        return (convertView);
    }

}
