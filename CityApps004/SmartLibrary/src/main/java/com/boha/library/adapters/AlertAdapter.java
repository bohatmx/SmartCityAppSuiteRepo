package com.boha.library.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.dto.AlertDTO;
import com.boha.library.dto.AlertTypeDTO;
import com.boha.library.util.Statics;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by aubreyM on 14/12/17.
 */
public class AlertAdapter extends RecyclerView.Adapter<AlertAdapter.AlertHolder> {

    public interface AlertListener {
        public void onAlertClicked(int position);
    }
    private AlertListener listener;
    private List<AlertDTO> alertList;
    private Context ctx;

    public AlertAdapter(List<AlertDTO> list,
                        Context context, AlertListener listener) {
        this.alertList = list;
        this.ctx = context;
        this.listener = listener;
    }

    @Override
    public AlertHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.alert_item, parent, false);
        return new AlertHolder(v);
    }

    @Override
    public void onBindViewHolder(final AlertHolder holder, final int position) {

        final AlertDTO p = alertList.get(position);
        holder.txtColor.setText("" + (position + 1));
        holder.txtType.setText(p.getAlertType().getAlertTypeName());
        holder.txtDate.setText(sdfDate.format(p.getUpdated()));
        holder.txtTime.setText(sdfTime.format(p.getUpdated()));
        holder.txtDesc.setText(p.getDescription());
        holder.position = position;
        switch (p.getAlertType().getColor()) {
            case AlertTypeDTO.GREEN:
                holder.txtColor.setBackground(ctx.getResources().getDrawable(R.drawable.xgreen_oval_small));
                break;
            case AlertTypeDTO.AMBER:
                holder.txtColor.setBackground(ctx.getResources().getDrawable(R.drawable.xamber_oval_small));
                break;
            case AlertTypeDTO.RED:
                holder.txtColor.setBackground(ctx.getResources().getDrawable(R.drawable.xred_oval_small));
                break;
        }

        holder.txtColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onAlertClicked(position);
            }
        });
        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onAlertClicked(position);
            }
        });
        Statics.setRobotoFontLight(ctx,holder.txtDesc);

    }

    @Override
    public int getItemCount() {
        return alertList == null ? 0 : alertList.size();
    }

    static final Locale loc = Locale.getDefault();
    static final SimpleDateFormat sdfDate = new SimpleDateFormat("EEE dd MMM yyyy", loc);
    static final SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm", loc);

    public class AlertHolder extends RecyclerView.ViewHolder  {
        protected ImageView image;
        protected TextView txtType, txtColor, txtDate, txtDesc, txtTime;
        protected int position;
        protected View mainView;


        public AlertHolder(View itemView) {
            super(itemView);
            txtType = (TextView) itemView.findViewById(R.id.AI_type);
            txtDesc = (TextView) itemView.findViewById(R.id.AI_desc);
            txtColor = (TextView) itemView.findViewById(R.id.AI_color);
            txtDate = (TextView) itemView.findViewById(R.id.AI_date);
            txtTime = (TextView) itemView.findViewById(R.id.AI_time);
            mainView = itemView.findViewById(R.id.AI_main);
        }

    }

    static final String LOG = AlertAdapter.class.getSimpleName();
}
