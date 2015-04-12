package com.boha.library.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.dto.ComplaintDTO;
import com.boha.library.dto.ComplaintTypeDTO;
import com.boha.library.util.Util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by aubreyM on 14/12/17.
 */
public class ComplaintAdapter extends RecyclerView.Adapter<ComplaintAdapter.ComplaintHolder> {

    public interface ComplaintListener {
        public void onFollowRequested(ComplaintDTO complaint);
        public void onDetailsRequested(ComplaintDTO complaint);
        public void onCameraRequested(ComplaintDTO complaint);
        public void onImagesRequested(ComplaintDTO complaint);
    }

    private ComplaintListener listener;
    private List<ComplaintDTO> complaintList;
    private Context ctx;
    private int themeDarkColor = R.color.steel_blue;


    public ComplaintAdapter(Context context,List<ComplaintDTO> list, int themeDarkColor,
                            ComplaintListener listener) {
        this.complaintList = list;
        this.ctx = context;
        this.listener = listener;
        this.themeDarkColor = themeDarkColor;
    }

    @Override
    public ComplaintHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.complaint_item, parent, false);
        return new ComplaintHolder(v);
    }

    @Override
    public void onBindViewHolder(final ComplaintHolder holder, final int position) {

        final ComplaintDTO p = complaintList.get(position);
        holder.txtColor.setText("" + (position + 1));
        if (p.getComplaintType() == null) {
            holder.txtComplaintType.setText("Type unavailable");
        } else {
            holder.txtComplaintType.setText(p.getComplaintType().getComplaintTypeName());
        }
        holder.txtDate.setText(sdfDate.format(new Date(p.getComplaintDate())));
        holder.txtComment.setText(p.getRemarks());
        holder.txtRef.setText(p.getReferenceNumber());
        holder.position = position;


        if (p.getComplaintType() != null) {
            switch (p.getComplaintType().getColor()) {
                case ComplaintTypeDTO.GREEN:
                    holder.txtColor.setBackground(ctx.getResources().getDrawable(R.drawable.xgreen_oval_small));
                    break;
                case ComplaintTypeDTO.AMBER:
                    holder.txtColor.setBackground(ctx.getResources().getDrawable(R.drawable.xamber_oval_small));
                    break;
                case ComplaintTypeDTO.RED:
                    holder.txtColor.setBackground(ctx.getResources().getDrawable(R.drawable.xred_oval_small));
                    break;
            }
        }
        holder.iconFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(holder.iconFollow, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        listener.onFollowRequested(p);
                    }
                });
            }
        });
        holder.iconDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(holder.iconDetails, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        listener.onDetailsRequested(p);
                    }
                });
            }
        });
        holder.iconCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(holder.iconCamera, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        listener.onCameraRequested(p);
                    }
                });
            }
        });
        holder.iconRoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(holder.iconRoll, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        listener.onImagesRequested(p);
                    }
                });
            }
        });



    }

    @Override
    public int getItemCount() {
        return complaintList == null ? 0 : complaintList.size();
    }

    static final Locale loc = Locale.getDefault();
    static final SimpleDateFormat sdfDate = new SimpleDateFormat("EEEE dd MMMM yyyy HH:mm", loc);
    static final SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm", loc);

    public class ComplaintHolder extends RecyclerView.ViewHolder {
        protected ImageView image, iconDetails, iconFollow, iconCamera, iconRoll;
        protected TextView txtComplaintType, txtColor, txtDate, txtComment, txtAddress, txtRef;
        protected View detailsView;
        protected int position;
        private boolean isDetailViewOpen;


        public ComplaintHolder(View itemView) {
            super(itemView);
            txtComplaintType = (TextView) itemView.findViewById(R.id.CI_complaintType);
            txtComment = (TextView) itemView.findViewById(R.id.CI_comment);
            txtColor = (TextView) itemView.findViewById(R.id.CI_color);
            txtDate = (TextView) itemView.findViewById(R.id.CI_date);
            txtAddress = (TextView) itemView.findViewById(R.id.CI_address);
            txtRef = (TextView) itemView.findViewById(R.id.CI_reference);
            detailsView = itemView.findViewById(R.id.CI_detailsView);
            iconDetails = (ImageView)itemView.findViewById(R.id.CI_iconDetail);
            iconFollow = (ImageView)itemView.findViewById(R.id.CI_iconFollow);
            iconCamera = (ImageView)itemView.findViewById(R.id.CI_iconCamera);
            iconRoll = (ImageView)itemView.findViewById(R.id.CI_iconRoll);

            if (themeDarkColor != 0) {
                iconDetails.setColorFilter(themeDarkColor, PorterDuff.Mode.SRC_IN);
                iconFollow.setColorFilter(themeDarkColor, PorterDuff.Mode.SRC_IN);
                iconCamera.setColorFilter(themeDarkColor, PorterDuff.Mode.SRC_IN);
                iconRoll.setColorFilter(themeDarkColor, PorterDuff.Mode.SRC_IN);
            }

            if (themeDarkColor != 0) {

            }
        }

    }

    static final String LOG = ComplaintAdapter.class.getSimpleName();
}
