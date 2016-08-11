package com.boha.library.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.util.Util;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Created by aubreyM on 14/12/17.
 */
public class StatementRecyclerAdapter extends RecyclerView.Adapter<StatementRecyclerAdapter.FileViewHolder> {

    public interface StatementAdapterListener {
        void onStatementClicked(int position);

    }

    private StatementAdapterListener mListener;
    private List<String> fileNames;
    private Context ctx;
    private final Pattern pattern = Pattern.compile("-");
    private final Pattern pattern2 = Pattern.compile("/");

    public StatementRecyclerAdapter(List<String> fileNames, Context ctx, StatementAdapterListener listener) {
        this.fileNames = fileNames;
        this.mListener = listener;
        this.ctx = ctx;
    }

    @Override
    public FileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.statement_item, parent, false);
        return new FileViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final FileViewHolder holder, final int position) {

        final String p = fileNames.get(position);
        String[] parts = pattern2.split(p);
        String full = parts[parts.length -1];
        String[] pieces = pattern.split(full);
        String month = pieces[2];
        int m = Integer.parseInt(month);
        if (m < 10) {
            month = "0" + month;
        }
        holder.txtFileName.setText(pieces[0] + " " + pieces[1] + "-" + month);
        holder.txtNumber.setText("" + (position + 1));

        String[] data = pattern.split(p);
        DateTime dateTime = new DateTime(Integer.parseInt(data[1]),Integer.parseInt(data[2]),1,0,0);
        holder.txtDate.setText(sdf.format(dateTime.toDate()));



        holder.main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onStatementClicked(position);
            }
        });
        Util.scaleDownAndUp(holder.main,200);

    }
    @Override
    public int getItemCount() {
        return fileNames == null ? 0 : fileNames.size();
    }

    public class FileViewHolder extends RecyclerView.ViewHolder {
        protected TextView  txtNumber, txtFileName, txtDate;
        protected ImageView icon;
        protected View main;


        public FileViewHolder(View itemView) {
            super(itemView);
            txtNumber = (TextView) itemView.findViewById(R.id.SI_number);
            txtFileName = (TextView) itemView.findViewById(R.id.SI_filename);
            txtDate = (TextView) itemView.findViewById(R.id.SI_date);
            main =  itemView.findViewById(R.id.SI_main);
        }

    }
    static final Locale d = Locale.getDefault();
    static final SimpleDateFormat sdf = new SimpleDateFormat("MMMM, yyyy", d);

    static final String LOG = StatementRecyclerAdapter.class.getSimpleName();
}
