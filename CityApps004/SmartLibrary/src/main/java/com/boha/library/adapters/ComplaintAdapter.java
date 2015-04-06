package com.boha.library.adapters;

import android.content.Context;
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
import java.util.List;
import java.util.Locale;

/**
 * Created by aubreyM on 14/12/17.
 */
public class ComplaintAdapter extends RecyclerView.Adapter<ComplaintAdapter.ComplaintHolder> {

    public interface ComplaintListener {
        public void onComplaintSelected(int position);
    }

    private ComplaintListener listener;
    private List<ComplaintDTO> complaintList;
    private Context ctx;


    public ComplaintAdapter(List<ComplaintDTO> list,
                            Context context, ComplaintListener listener) {
        this.complaintList = list;
        this.ctx = context;
        this.listener = listener;
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
        holder.txtComplaintType.setText(p.getComplaintType().getComplaintTypeName());
        holder.txtDate.setText(sdfDate.format(p.getComplaintDate()));
        holder.txtComment.setText(sdfTime.format(p.getRemarks()));
        holder.txtRef.setText(p.getReferenceNumber());
        holder.position = position;
        holder.detailsView.setVisibility(View.GONE);

        if (holder.isDetailViewOpen) {
            Util.expand(holder.detailsView, 200, null);
        } else {
            Util.collapse(holder.detailsView, 100, null);

        }

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

        holder.txtColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(holder.txtColor, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        if (holder.isDetailViewOpen) {
                            Util.collapse(holder.detailsView, 300, null);
                            holder.isDetailViewOpen = false;
                        } else {
                            Util.expand(holder.detailsView, 300, null);
                            holder.isDetailViewOpen = true;
                        }
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
    static final SimpleDateFormat sdfDate = new SimpleDateFormat("EEE dd MMM yyyy", loc);
    static final SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm", loc);

    public class ComplaintHolder extends RecyclerView.ViewHolder {
        protected ImageView image;
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
        }

    }

    static final String LOG = ComplaintAdapter.class.getSimpleName();
}
