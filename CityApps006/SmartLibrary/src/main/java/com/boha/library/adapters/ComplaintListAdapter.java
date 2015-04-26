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
import com.boha.library.dto.ComplaintDTO;
import com.boha.library.dto.ComplaintTypeDTO;
import com.boha.library.util.Util;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class ComplaintListAdapter extends ArrayAdapter<ComplaintDTO> {

    CmplaintListListener listener;

    private final LayoutInflater mInflater;
    private final int mLayoutRes;
    private List<ComplaintDTO> mList;
    private Context ctx;
    static final String LOG = ComplaintListAdapter.class.getSimpleName();

    public ComplaintListAdapter(Context context, int textViewResourceId,
                                List<ComplaintDTO> list, CmplaintListListener listener) {
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
        protected ImageView image, iconDetails, iconFollow, iconCamera, iconRoll;
        protected TextView txtComplaintType, txtColor, txtDate, txtComment, txtAddress, txtRef;
        protected View detailsView;
        protected int position;
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
            item.txtComplaintType = (TextView) convertView.findViewById(R.id.CI_complaintType);
            item.txtComment = (TextView) convertView.findViewById(R.id.CI_comment);
            item.txtColor = (TextView) convertView.findViewById(R.id.CI_color);
            item.txtDate = (TextView) convertView.findViewById(R.id.CI_date);
            item.txtAddress = (TextView) convertView.findViewById(R.id.CI_address);
            item.txtRef = (TextView) convertView.findViewById(R.id.CI_reference);

            item.detailsView = convertView.findViewById(R.id.CI_detailsView);
            item.iconDetails = (ImageView)convertView.findViewById(R.id.CI_iconDetail);
            item.iconFollow = (ImageView)convertView.findViewById(R.id.CI_iconFollow);
            item.iconCamera = (ImageView)convertView.findViewById(R.id.CI_iconCamera);
            item.iconRoll = (ImageView)convertView.findViewById(R.id.CI_iconRoll);


            convertView.setTag(item);
        } else {
            item = (ViewHolderItem) convertView.getTag();
        }

        final ComplaintDTO p = mList.get(position);
        item.txtColor.setText("" + (position + 1));
        if (p.getComplaintType() == null) {
            item.txtComplaintType.setText("Type unavailable");
        } else {
            item.txtComplaintType.setText(p.getComplaintType().getComplaintTypeName());
        }
        item.txtDate.setText(sdfDate.format(new Date(p.getComplaintDate())));
        item.txtComment.setText(p.getRemarks());
        item.txtRef.setText(p.getReferenceNumber());
        item.position = position;


        if (p.getComplaintType() != null) {
            switch (p.getComplaintType().getColor()) {
                case ComplaintTypeDTO.GREEN:
                    item.txtColor.setBackground(ctx.getResources().getDrawable(R.drawable.xgreen_oval_small));
                    break;
                case ComplaintTypeDTO.AMBER:
                    item.txtColor.setBackground(ctx.getResources().getDrawable(R.drawable.xamber_oval_small));
                    break;
                case ComplaintTypeDTO.RED:
                    item.txtColor.setBackground(ctx.getResources().getDrawable(R.drawable.xred_oval_small));
                    break;
            }
        }
        item.iconFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onFollowRequested(p);
            }
        });
        item.iconDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onStatusRequested(p);
            }
        });
        item.iconCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCameraRequested(p);
            }
        });
        item.iconRoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onImagesRequested(p);
            }
        });

        Util.scaleUp(convertView,500);
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
    public interface CmplaintListListener {
        void onFollowRequested(ComplaintDTO complaint);
        void onStatusRequested(ComplaintDTO complaint);
        void onCameraRequested(ComplaintDTO complaint);
        void onImagesRequested(ComplaintDTO complaint);
    }
    static final Random random = new Random(System.currentTimeMillis());
    static final Locale loc = Locale.getDefault();
    static final SimpleDateFormat sdfDate = new SimpleDateFormat("EEE dd MMM yyyy", loc);
    static final SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm", loc);
}
