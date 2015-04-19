package com.boha.foureyes.adapters;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.boha.foureyes.R;
import com.boha.foureyes.dto.SummaryDTO;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class DashboardAdapter extends ArrayAdapter<SummaryDTO> {

    private final LayoutInflater mInflater;
    private final int mLayoutRes;
    private List<SummaryDTO> mList;
    private Context ctx;

    public DashboardAdapter(Context context, int textViewResourceId,
                            List<SummaryDTO> list) {
        super(context, textViewResourceId, list);
        this.mLayoutRes = textViewResourceId;
        mList = list;
        ctx = context;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    View view;


    static class ViewHolderItem {
        TextView txtMuni, txtStaff, txtCitizens, txtComplaints, txtAlerts, txtAlertImages;
        TextView txtComplaintImages;
        ImageView heroImage;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolderItem vhItem;
        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutRes, null);
            vhItem = new ViewHolderItem();
            vhItem.heroImage = (ImageView)convertView.findViewById(R.id.MD_hero);
            vhItem.txtMuni = (TextView) convertView
                    .findViewById(R.id.MD_muni);
            vhItem.txtStaff = (TextView) convertView
                    .findViewById(R.id.MD_muniStaff);
            vhItem.txtCitizens = (TextView) convertView
                    .findViewById(R.id.MD_muniCitizens);
            vhItem.txtComplaints = (TextView) convertView
                    .findViewById(R.id.MD_muniComplaints);

            vhItem.txtAlerts = (TextView) convertView
                    .findViewById(R.id.MD_muniAlerts);

            vhItem.txtComplaintImages = (TextView) convertView
                    .findViewById(R.id.MD_compImages);
            vhItem.txtAlertImages = (TextView) convertView
                    .findViewById(R.id.MD_alertImages);

            convertView.setTag(vhItem);
        } else {
            vhItem = (ViewHolderItem) convertView.getTag();
        }

        final SummaryDTO p = mList.get(position);
        vhItem.txtAlertImages.setText(df.format(p.getNumberOfAlertImages()));
        vhItem.txtMuni.setText(p.getMunicipalityName());
        vhItem.txtStaff.setText(df.format(p.getNumberOfStaff()));

        vhItem.txtComplaintImages.setText(df.format(p.getNumberOfComplaintImages()));
        vhItem.txtCitizens.setText(df.format(p.getNumberOfCitizens()));
        vhItem.txtComplaints.setText(df.format(p.getNumberOfComplaints()));
        vhItem.txtAlerts.setText(df.format(p.getNumberOFAlerts()));

        vhItem.heroImage.setImageDrawable(ctx.getResources().getDrawable(p.getImageResource()));


        return (convertView);
    }

    public void animateView(final View view) {
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(view, "scaleX", 0.5f);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(view, "scaleY", 0.5f);

        ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(view, "scaleX", 1.0f);
        ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(view, "scaleY", 1.0f);
        scaleDownX.setDuration(1000);
        scaleDownY.setDuration(1000);

        final AnimatorSet scaleDown = new AnimatorSet();
        scaleDown.play(scaleUpX).with(scaleUpY);

        scaleDown.start();

    }

    static final DecimalFormat df = new DecimalFormat("###,###,###,###,###,###,###,###,###,###,###,###,###");
    static final Locale x = Locale.getDefault();
    static final SimpleDateFormat y = new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss", x);
}
