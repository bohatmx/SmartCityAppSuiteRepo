package com.boha.library.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.dto.ComplaintDTO;
import com.boha.library.dto.ComplaintTypeDTO;
import com.boha.library.dto.ComplaintUpdateStatusDTO;
import com.boha.library.util.Util;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class ComplaintListAdapter extends ArrayAdapter<ComplaintDTO> {

    ComplaintListListener listener;

    private final LayoutInflater mInflater;
    private final int mLayoutRes;
    private List<ComplaintDTO> mList;
    private Context ctx;
    private int darkColor, type;
    public static final int MY_COMPLAINTS = 1, AROUND_ME = 2;
    private ArrayList<ComplaintDTO> arrayList;
    static final String LOG = ComplaintListAdapter.class.getSimpleName();

    public ComplaintListAdapter(Context context, int textViewResourceId,
                                int darkColor, int type,
                                List<ComplaintDTO> list, ComplaintListListener listener) {
        super(context, textViewResourceId, list);
        this.mLayoutRes = textViewResourceId;
        this.listener = listener;
        this.darkColor = darkColor;
        mList = list;
        this.type = type;
        ctx = context;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.arrayList = new ArrayList<ComplaintDTO>();
        this.arrayList.addAll(mList);
    }

    View view;

    static class ViewHolderItem {
        protected ImageView image, iconDetails, iconFollow, iconCamera, iconRoll;
        protected TextView txtComplaintType, txtColor, txtDate,
                txtComment, txtAddress, txtRef, txtStatusDate, txtRemarks, txtAck;
        protected View detailsView, followBox, cameraBox, statusBox;
        protected LinearLayout statusLayout;
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
         final ViewHolderItem item;
        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutRes, null);
            item = new ViewHolderItem();
            item.txtComplaintType = (TextView) convertView.findViewById(R.id.CI_complaintType);
            item.txtComment = (TextView) convertView.findViewById(R.id.CI_comment);
            item.txtColor = (TextView) convertView.findViewById(R.id.CI_color);
            item.txtDate = (TextView) convertView.findViewById(R.id.CI_date);
            item.txtAddress = (TextView) convertView.findViewById(R.id.CI_address);
            item.txtRef = (TextView) convertView.findViewById(R.id.CI_reference);
            item.statusLayout = (LinearLayout) convertView.findViewById(R.id.CI_statusLayout);

            item.detailsView = convertView.findViewById(R.id.CI_detailsView);
            item.iconDetails = (ImageView)convertView.findViewById(R.id.CI_iconDetail);
            item.iconFollow = (ImageView)convertView.findViewById(R.id.CI_iconFollow);
            item.iconCamera = (ImageView)convertView.findViewById(R.id.CI_iconCamera);
            item.iconRoll = (ImageView)convertView.findViewById(R.id.CI_iconRoll);
            item.image = (ImageView)convertView.findViewById(R.id.CI_image);

            item.txtStatusDate = (TextView) convertView.findViewById(R.id.SL_statusDate);
            item.txtAck = (TextView) convertView.findViewById(R.id.SL_ack);
            item.txtRemarks = (TextView) convertView.findViewById(R.id.SL_statusText);

            item.followBox = convertView.findViewById(R.id.iconBoxFollow);
            item.cameraBox = convertView.findViewById(R.id.iconBoxCamera);
            item.statusBox = convertView.findViewById(R.id.iconBoxStatus);

            convertView.setTag(item);
        } else {
            item = (ViewHolderItem) convertView.getTag();
        }

        final ComplaintDTO p = mList.get(position);
        item.txtColor.setText("" + (position + 1));
        if (p.getComplaintType() == null) {
            item.txtComplaintType.setText("Complaint name unavailable");
        } else {
            item.txtComplaintType.setText(
                    p.getComplaintType().getCategoryName() + " - " +
                    p.getComplaintType().getComplaintTypeName());
        }
        if (p.getComplaintDate() != null) {
            item.txtDate.setText(sdfDate.format(new Date(p.getComplaintDate())));
        } else {
            item.txtDate.setText("Date Unavailable");
        }
        item.txtComment.setText(p.getRemarks());
        item.txtRef.setText(p.getReferenceNumber());
        item.position = position;
        if (p.getAddress() != null) {
            item.txtAddress.setText(p.getAddress());
        } else {
            item.txtAddress.setText("");
        }

        if (p.getComplaintType() != null) {
            if (p.getComplaintType().getColor() != null) {
                switch (p.getComplaintType().getColor()) {
                    case ComplaintTypeDTO.GREEN:
                        item.txtColor.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xgreen_oval_small));
                        break;
                    case ComplaintTypeDTO.AMBER:
                        item.txtColor.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xamber_oval_small));
                        break;
                    case ComplaintTypeDTO.RED:
                        item.txtColor.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xred_oval_small));
                        break;
                    default:
                         item.txtColor.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xred_oval_small));
                        break;
                }
            } else {
                item.txtColor.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xred_oval_small));
          }
        }

        if (p.getComplaintImageList() != null && !p.getComplaintImageList().isEmpty()) {
            item.image.setVisibility(View.VISIBLE);
            String url = p.getComplaintImageList().get(0).getUrl();
            Picasso.with(ctx).load(url).into(item.image);
        } else {
            item.image.setVisibility(View.GONE);
        }

        item.iconFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(item.iconFollow, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        listener.onComplaintFollowRequested(p);
                    }
                });
            }
        });
        item.iconDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(item.iconDetails, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {

                        listener.onComplaintStatusRequested(p, position);
                    }
                });
            }
        });
        item.iconCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(item.iconCamera, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        listener.onComplaintCameraRequested(p);
                    }
                });
            }
        });
        item.iconRoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(item.iconRoll, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        listener.onComplaintImagesRequested(p);
                    }
                });
            }
        });

        if (p.getComplaintUpdateStatusList() != null && !p.getComplaintUpdateStatusList().isEmpty()) {
            item.statusLayout.setVisibility(View.VISIBLE);
            ComplaintUpdateStatusDTO y = p.getComplaintUpdateStatusList().get(0);
            item.txtStatusDate.setText(sdfDate.format(new Date(y.getDateUpdated())));
            item.txtRemarks.setText(y.getRemarks());
            item.txtAck.setText(y.getStatus());

        } else {
            item.statusLayout.setVisibility(View.GONE);
        }

        item.txtColor.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xindigo_oval_small));
        item.iconCamera.setColorFilter(darkColor, PorterDuff.Mode.SRC_IN);
        item.iconDetails.setColorFilter(darkColor, PorterDuff.Mode.SRC_IN);
        item.iconFollow.setColorFilter(darkColor, PorterDuff.Mode.SRC_IN);
        item.iconRoll.setColorFilter(darkColor, PorterDuff.Mode.SRC_IN);
        item.iconRoll.setVisibility(View.GONE);
        switch (type) {
            case MY_COMPLAINTS:
                 item.followBox.setVisibility(View.GONE);
                 item.cameraBox.setVisibility(View.VISIBLE);
                break;
            case AROUND_ME:
                item.followBox.setVisibility(View.VISIBLE);
                item.cameraBox.setVisibility(View.GONE);
                break;

        }
        Util.scaleDownAndUp(convertView, 300);
        return (convertView);
    }

    public void setDrawableColor(int index){

    }
    int checkTheme;

    // filter method
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        mList.clear();
        if (charText.length() == 0) {
            mList.addAll(arrayList);
        }
        else {
            for (ComplaintDTO c : arrayList){
              if (c.getComplaintType().getComplaintTypeName()
                        .toLowerCase(Locale.getDefault()).contains(charText)){
                    mList.add(c);
                }
            }
        }
        notifyDataSetChanged();
    }




    public interface ComplaintListListener {
        void onComplaintFollowRequested(ComplaintDTO complaint);
        void onComplaintStatusRequested(ComplaintDTO complaint, int position);
        void onComplaintCameraRequested(ComplaintDTO complaint);
        void onComplaintImagesRequested(ComplaintDTO complaint);
    }
    static final Random random = new Random(System.currentTimeMillis());
    static final Locale loc = Locale.getDefault();
    static final SimpleDateFormat sdfDate = new SimpleDateFormat("EEEE dd MMMM yyyy HH:mm", loc);
    static final SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm", loc);
}
