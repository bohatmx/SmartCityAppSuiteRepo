package com.boha.library.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.dto.AlertDTO;
import com.boha.library.dto.AlertTypeDTO;
import com.boha.library.util.Statics;
import com.boha.library.util.Util;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class AlertListAdapter extends ArrayAdapter<AlertDTO> {

    AlertListListener listener;

    private final LayoutInflater mInflater;
    private final int mLayoutRes;
    private List<AlertDTO> mList;
    private Context ctx;
    private static final String TEXT = "text/html", UTF = "UTF-8";
    static final String LOG = AlertListAdapter.class.getSimpleName();

    public AlertListAdapter(Context context, int textViewResourceId,
                            List<AlertDTO> list, AlertListListener listener) {
        super(context, textViewResourceId, list);
        this.mLayoutRes = textViewResourceId;
        this.listener = listener;
        mList = list;
        ctx = context;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    View view;


    static class ViewHolderItem {
        protected ImageView image;
        protected TextView txtType, txtColor, txtDate, txtDesc, txtTime;
        protected int position;
        protected View mainView;
        protected WebView webView;
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
            item.txtType = (TextView) convertView.findViewById(R.id.AI_type);
            item.txtDesc = (TextView) convertView.findViewById(R.id.AI_desc);
            item.txtColor = (TextView) convertView.findViewById(R.id.AI_color);
            item.txtDate = (TextView) convertView.findViewById(R.id.AI_date);
            item.txtTime = (TextView) convertView.findViewById(R.id.AI_time);
            item.image = (ImageView) convertView.findViewById(R.id.AI_image);
            item.webView = (WebView) convertView.findViewById(R.id.AI_webView);
            item.mainView = convertView.findViewById(R.id.AI_main);


            convertView.setTag(item);
        } else {
            item = (ViewHolderItem) convertView.getTag();
        }

        final AlertDTO p = mList.get(position);
        item.txtColor.setText("" + (position + 1));
        item.txtType.setVisibility(View.GONE);
        item.txtDate.setText(sdfDate.format(p.getUpdated()));
        item.txtTime.setText(sdfTime.format(p.getUpdated()));
        item.txtDesc.setText(p.getDescription());
        item.position = position;
        switch (p.getAlertType().getColor()) {
            case AlertTypeDTO.GREEN:
                item.txtColor.setBackground(ContextCompat.getDrawable(ctx,R.drawable.xgreen_oval_small));
                break;
            case AlertTypeDTO.AMBER:
                item.txtColor.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xamber_oval_small));
                break;
            case AlertTypeDTO.RED:
                item.txtColor.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xred_oval_small));
                break;
        }

        if (p.getAlertData() != null) {
            item.webView.setVisibility(View.VISIBLE);
            item.webView.loadData(p.getAlertData(), TEXT, UTF);
        } else {
            item.webView.setVisibility(View.GONE);
        }
        //get first image if available ...
        if (p.getAlertImageList() != null && !p.getAlertImageList().isEmpty()) {
            item.image.setVisibility(View.VISIBLE);
            String url = p.getAlertImageList().get(0).getUrl();
            Picasso.with(ctx).load(url).into(item.image);
        } else {
                item.image.setVisibility(View.GONE);
        }
        Statics.setRobotoFontLight(ctx,item.txtDesc);
        Util.scaleDownAndUp(convertView,300);
        return (convertView);
    }

    public interface AlertListListener {
        public void onAlertClicked(int position);
    }

    static final Random random = new Random(System.currentTimeMillis());
    static final Locale loc = Locale.getDefault();
    static final SimpleDateFormat sdfDate = new SimpleDateFormat("EEE dd MMM yyyy", loc);
    static final SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm", loc);
}
