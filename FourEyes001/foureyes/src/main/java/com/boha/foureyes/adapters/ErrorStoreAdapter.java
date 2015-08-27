package com.boha.foureyes.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.boha.foureyes.R;
import com.boha.foureyes.dto.ErrorStoreDTO;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;


public class ErrorStoreAdapter extends ArrayAdapter<ErrorStoreDTO> {

    private final LayoutInflater mInflater;
    private final int mLayoutRes;
    private List<ErrorStoreDTO> mList;
    private Context ctx;

    public ErrorStoreAdapter(Context context, int textViewResourceId,
                             List<ErrorStoreDTO> list) {
        super(context, textViewResourceId, list);
        this.mLayoutRes = textViewResourceId;
        mList = list;
        ctx = context;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mInflater.inflate(mLayoutRes, parent, false);

        if (convertView == null) {
            view = mInflater.inflate(mLayoutRes, parent, false);
        } else {
            view = convertView;
        }

        ImageView logo = (ImageView) view
                .findViewById(R.id.ERR_image);
        TextView origin = (TextView) view
                .findViewById(R.id.ERR_origin);
        TextView message = (TextView) view
                .findViewById(R.id.ERR_message);
        TextView date = (TextView) view
                .findViewById(R.id.ERR_date);
        ImageView image = (ImageView) view.findViewById(R.id.ERR_image);

        final ErrorStoreDTO p = mList.get(position);

        origin.setTextColor(ctx.getResources().getColor(R.color.black));
        if (p.getStatusCode() > 99) {
            logo.setImageDrawable(ContextCompat.getDrawable(ctx,R.drawable.ic_action_cancel));
            message.setTextColor(ctx.getResources().getColor(R.color.red_800));
            date.setTextColor(ctx.getResources().getColor(R.color.orange_600));
        } else {
            logo.setImageDrawable(ContextCompat.getDrawable(ctx,R.drawable.ic_action_tick));
            message.setTextColor(ctx.getResources().getColor(R.color.black));
            date.setTextColor(ctx.getResources().getColor(R.color.blue_400));
        }


        origin.setText(p.getOrigin());
        message.setText(p.getMessage());
        date.setText(sdf.format(p.getDateOccured()));
        animateView(view);


        return (view);
    }

    private void animateView(final View view) {

    }

    static final Locale loc = Locale.getDefault();
    static final SimpleDateFormat sdf = new SimpleDateFormat("EE dd-MM-yyyy HH:mm:ss", loc);
}
