package com.boha.library.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.dto.AlertDTO;
import com.boha.library.dto.AlertTypeDTO;
import com.boha.library.util.Statics;
import com.boha.library.util.Util;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

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
    static final String LOG = AlertListAdapter.class.getSimpleName();

    public AlertListAdapter(Context context, int textViewResourceId,
                            List<AlertDTO> list,AlertListListener listener) {
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
            item.mainView = convertView.findViewById(R.id.AI_main);


            convertView.setTag(item);
        } else {
            item = (ViewHolderItem) convertView.getTag();
        }

        final AlertDTO p = mList.get(position);
        item.txtColor.setText("" + (position + 1));
        item.txtType.setText(p.getAlertType().getAlertTypeName());
        item.txtDate.setText(sdfDate.format(p.getUpdated()));
        item.txtTime.setText(sdfTime.format(p.getUpdated()));
        item.txtDesc.setText(p.getDescription());
        item.position = position;
        switch (p.getAlertType().getColor()) {
            case AlertTypeDTO.GREEN:
                item.txtColor.setBackground(ctx.getResources().getDrawable(R.drawable.xgreen_oval_small));
                break;
            case AlertTypeDTO.AMBER:
                item.txtColor.setBackground(ctx.getResources().getDrawable(R.drawable.xamber_oval_small));
                break;
            case AlertTypeDTO.RED:
                item.txtColor.setBackground(ctx.getResources().getDrawable(R.drawable.xred_oval_small));
                break;
        }

        item.txtColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onAlertClicked(position);
            }
        });
        item.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onAlertClicked(position);
            }
        });
        Statics.setRomanFontLight(ctx,item.txtDesc);
        //get random image if available ...
        if (p.getAlertImageList() != null && !p.getAlertImageList().isEmpty()) {
            item.image.setVisibility(View.VISIBLE);
            if (p.getAlertImageList().size() == 1) {
                String url = Util.getAlertImageURL(p.getAlertImageList().get(0));
                setImage(url,item.image);
            } else {
                int index = random.nextInt(p.getAlertImageList().size() - 1);
                String url = Util.getAlertImageURL(p.getAlertImageList().get(index));
                setImage(url,item.image);
            }

        } else {
            item.image.setVisibility(View.GONE);
        }
        return (convertView);
    }

    private void setImage(String url, final ImageView  image) {
        ImageLoader.getInstance().displayImage(url, image, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                image.setImageDrawable(ctx.getResources().getDrawable(R.drawable.under_construction));
                image.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {

            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
    }
    public interface AlertListListener {
        public void onAlertClicked(int position);
    }
    static final Random random = new Random(System.currentTimeMillis());
    static final Locale loc = Locale.getDefault();
    static final SimpleDateFormat sdfDate = new SimpleDateFormat("EEE dd MMM yyyy", loc);
    static final SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm", loc);
}
