package com.boha.library.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.util.Statics;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class StatementAdapter extends ArrayAdapter<String> {

    private final LayoutInflater mInflater;
    private final int mLayoutRes;
    private List<String> mList;
    private Context ctx;
    static final String LOG = StatementAdapter.class.getSimpleName();

    public StatementAdapter(Context context, int textViewResourceId,
                            List<String> list) {
        super(context, textViewResourceId, list);
        this.mLayoutRes = textViewResourceId;
        mList = list;
        ctx = context;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    View view;


    static class ViewHolderItem {
        protected TextView  txtNumber, txtFileName, txtDate;

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
            item.txtFileName = (TextView) convertView.findViewById(R.id.SI_filename);
            item.txtNumber = (TextView) convertView.findViewById(R.id.SI_number);
            item.txtDate = (TextView) convertView.findViewById(R.id.SI_date);


            convertView.setTag(item);
        } else {
            item = (ViewHolderItem) convertView.getTag();
        }

        final String p = mList.get(position);
        item.txtNumber.setText("" + (position + 1));
        try {
            int i = p.lastIndexOf("/");
            String name = p.substring(i + 1);
            i = name.lastIndexOf(".");
            name = name.substring(0, i);
            item.txtFileName.setText(name);
            i = name.indexOf("_");
            String ys = name.substring(i + 1, i + 5);
            i = name.lastIndexOf("_");
            String ms = name.substring(i + 1);
            DateTime dateTime = new DateTime(Integer.parseInt(ys),Integer.parseInt(ms),1,0,0);
            item.txtDate.setText(sdf.format(dateTime.toDate()));
        } catch (Exception e) {
            item.txtDate.setText("Unavailable date");
        }

        int rand = random.nextInt(mList.size() - 1);
        switch (rand) {
            case 0:
                item.txtNumber.setBackground(ctx.getResources().getDrawable(R.drawable.xgreen_oval_small));
                break;
            case 1:
                item.txtNumber.setBackground(ctx.getResources().getDrawable(R.drawable.xamber_oval_small));
                break;
            case 2:
                item.txtNumber.setBackground(ctx.getResources().getDrawable(R.drawable.xred_oval_small));
                break;
            case 3:
                item.txtNumber.setBackground(ctx.getResources().getDrawable(R.drawable.xblack_oval_small));
                break;
            case 4:
                item.txtNumber.setBackground(ctx.getResources().getDrawable(R.drawable.xblue_oval_small));
                break;
            case 5:
                item.txtNumber.setBackground(ctx.getResources().getDrawable(R.drawable.xgrey_oval_small));
                break;
            case 6:
                item.txtNumber.setBackground(ctx.getResources().getDrawable(R.drawable.xgreen_oval_small));
                break;
            case 7:
                item.txtNumber.setBackground(ctx.getResources().getDrawable(R.drawable.xblack_oval_small));
                break;
            case 8:
                item.txtNumber.setBackground(ctx.getResources().getDrawable(R.drawable.xred_oval_small));
                break;
            case 9:
                item.txtNumber.setBackground(ctx.getResources().getDrawable(R.drawable.xblue_oval_small));
                break;
            case 10:
                item.txtNumber.setBackground(ctx.getResources().getDrawable(R.drawable.xamber_oval_small));
                break;
            case 11:
                item.txtNumber.setBackground(ctx.getResources().getDrawable(R.drawable.xred_oval_small));
                break;
        }


        Statics.setRobotoFontLight(ctx, item.txtFileName);

        return (convertView);
    }


    static final Random random = new Random(System.currentTimeMillis());
    static final Locale d = Locale.getDefault();
    static final SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", d);
}
