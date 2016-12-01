package com.boha.citizenapp.ethekwini.jsonreader;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.boha.library.R;

import java.util.ArrayList;

/**
 * Created by Nkululeko on 2016/11/18.
 */

public class NoAlertsAdapter extends RecyclerView.Adapter<NoAlertsAdapter.MyViewHolder> {

    Context ctx;
    ArrayList<AlertsFeedItem> alertsFeedItems;

    public NoAlertsAdapter(Context context, ArrayList<AlertsFeedItem> alertsFeedItems) {
        this.alertsFeedItems = alertsFeedItems;
        this.ctx = context;
        }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(ctx).inflate(R.layout.no_alert,parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final AlertsFeedItem current = alertsFeedItems.get(position);
        if (current.getCategory().isEmpty() || current.getCategory() == null) {
            holder.noAlert.setText("No Alerts available");
        }
    }

    @Override
    public int getItemCount() {
        return alertsFeedItems == null ? 0 : alertsFeedItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView noAlert;
        public MyViewHolder(View itemView) {
            super(itemView);
            noAlert = (TextView) itemView.findViewById(R.id.txtEmptyAlert1);



        }
    }
}
