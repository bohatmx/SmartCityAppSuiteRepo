package com.boha.library.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.activities.CityApplication;
import com.boha.library.dto.FreqQuestionTypeDTO;
import com.boha.library.rssreader.FaqTest;
import com.boha.library.util.Statics;

import java.util.ArrayList;
import java.util.List;

public class FaqTypeAdapter extends ArrayAdapter<FreqQuestionTypeDTO> {

    private final LayoutInflater mInflater;
    private final int mLayoutRes;
    private List<FreqQuestionTypeDTO> mList;
    ArrayList <FaqTest> arrayList;
    List<FaqTest> faqStringsList;

    private Context ctx;
    static final String LOG = FaqTypeAdapter.class.getSimpleName();
    FaqTypeListListener listener;
    String [] FAQ;
    private int darkColor;


    public FaqTypeAdapter(Context context, int textViewResourceId, int darkColor,
                          List<FreqQuestionTypeDTO> list, FaqTypeListListener listener) {
        super(context, textViewResourceId, list);
        this.mLayoutRes = textViewResourceId;
        this.darkColor = darkColor;
        //this.arrayList = new ArrayList<FaqTest>();
        mList = list;
        ctx = context;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listener = listener;
    }


    View view;

    public interface FaqTypeListListener {
        public void onFaqTypeClicked(int position);
    }

    public static class ViewHolderItem {
        protected TextView txtFaqType, txtNumber;
        protected View mainView;
        protected ImageView FTYPE_ICON;
    }

    @Override
    public long getItemId(int position) {
        return position;
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
            item.txtNumber = (TextView) convertView.findViewById(R.id.FTYPE_number);
            item.txtFaqType = (TextView) convertView.findViewById(R.id.FTYPE_faqType);
            item.mainView = (convertView).findViewById(R.id.FAQTYPE_main);
          //  item.FTYPE_ICON = (ImageView) convertView.findViewById(R.id.FTYPE_ICON);

            convertView.setTag(item);
        } else {
            item = (ViewHolderItem) convertView.getTag();
        }


        final FreqQuestionTypeDTO p = mList.get(position);
        //final FaqStrings fs = faqStringsList.get(position);
        item.txtFaqType.setText(p.getFaqTypeName());
        item.txtNumber.setText("" + (position + 1));

        // http://icsmnewsdev.oneconnectgroup.com/et/faq/AccountsPayments.html
        // http://icsmnewsdev.oneconnectgroup.com/et/faq/WaterSanitation.html
        // http://icsmnewsdev.oneconnectgroup.com/et/faq/CleansingSolidWaste.html
        // http://icsmnewsdev.oneconnectgroup.com/et/faq/RatesTaxes.html
        // http://icsmnewsdev.oneconnectgroup.com/et/faq/BuildingPlans.html
        // http://icsmnewsdev.oneconnectgroup.com/et/faq/Electricity.html
        // http://icsmnewsdev.oneconnectgroup.com/et/faq/SocialServices.html
        // http://icsmnewsdev.oneconnectgroup.com/et/faq/Health.html
        // http://icsmnewsdev.oneconnectgroup.com/et/faq/MetroPolice.html



        Statics.setRobotoFontLight(ctx, item.txtFaqType);

        switch(position) {
            case CityApplication.THEME_INDIGO:
/*
                item.txtFaqType.setText("Accounts & Payments");
*/
                item.txtNumber.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xindigo_oval_small));
                break;
            case CityApplication.THEME_GREEN:
/*
                item.txtFaqType.setText("Water & Sanitation");
*/
                item.txtNumber.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xgreen_oval_small));
                break;
            case CityApplication.THEME_BROWN:
/*
                item.txtFaqType.setText("Cleansing & Solid Waste");
*/
                item.txtNumber.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xbrown_oval_small));
                break;
            case CityApplication.THEME_AMBER:
                /*item.txtFaqType.setText("Rates & Taxes");*/
                item.txtNumber.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xamber_oval_small));
                break;
            case CityApplication.THEME_PURPLE:
                /*item.txtFaqType.setText("Building Plans");*/
                item.txtNumber.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xpurple_oval_small));
                break;
            case CityApplication.THEME_LIME:
                /*item.txtFaqType.setText("Electricity");*/
                item.txtNumber.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xlime_oval_small));
                break;
            case CityApplication.THEME_GREY:
                /*item.txtFaqType.setText("Social Services");*/
                item.txtNumber.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xgrey_oval_small));
                break;
            case CityApplication.THEME_BLUE:
                /*item.txtFaqType.setText("Health");*/
                item.txtNumber.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xblue_oval_small));
                break;
            case CityApplication.THEME_BLUE_GRAY:
               /* item.txtFaqType.setText("Metro Police");*/
                item.txtNumber.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xblue_gray_oval_small));
                break;
            case CityApplication.THEME_TEAL:
                item.txtNumber.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xteal_oval_small));
                break;
            case CityApplication.THEME_CYAN:
                item.txtNumber.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xcyan_oval_small));
                break;
            case CityApplication.THEME_ORANGE:
                item.txtNumber.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xorange_oval_small));
                break;
            case CityApplication.THEME_PINK:
                item.txtNumber.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xpink_oval_small));
                break;
            case CityApplication.THEME_RED:
                item.txtNumber.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xred_oval_small));
                break;
        }
       // item.txtNumber.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xindigo_oval_small));
        item.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onFaqTypeClicked(position);
            }

        });
        return (convertView);


}
    int ok, checkTheme;



}
