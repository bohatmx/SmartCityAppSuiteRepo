package com.boha.library.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.boha.library.activities.CityApplication;
import com.boha.library.activities.ThemeSelectorActivity;
import com.boha.library.dto.FreqQuestionTypeDTO;
import com.boha.library.util.FaqStrings;
import com.boha.library.util.Statics;
import com.boha.library.R;
import java.util.List;

public class FaqTypeAdapter extends ArrayAdapter<FreqQuestionTypeDTO> {

    private final LayoutInflater mInflater;
    private final int mLayoutRes;
    private List<FreqQuestionTypeDTO> mList;
    private Context ctx;
    static final String LOG = FaqTypeAdapter.class.getSimpleName();
    FaqTypeListListener listener;
    private int darkColor;

    public FaqTypeAdapter(Context context, int textViewResourceId, int darkColor,
                          List<FreqQuestionTypeDTO> list, FaqTypeListListener listener) {
        super(context, textViewResourceId, list);
        this.mLayoutRes = textViewResourceId;
        this.darkColor = darkColor;
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
        item.txtNumber.setText("" + (position + 1));

        item.txtFaqType.setText(p.getFaqTypeName());


        Statics.setRobotoFontLight(ctx, item.txtFaqType);

        switch(position) {
            case CityApplication.THEME_INDIGO:
                item.txtNumber.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xindigo_oval_small));
                break;
            case CityApplication.THEME_GREEN:
                item.txtNumber.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xgreen_oval_small));
                break;
            case CityApplication.THEME_BROWN:
                item.txtNumber.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xbrown_oval_small));
                break;
            case CityApplication.THEME_AMBER:
                item.txtNumber.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xamber_oval_small));
                break;
            case CityApplication.THEME_PURPLE:
                item.txtNumber.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xpurple_oval_small));
                break;
            case CityApplication.THEME_LIME:
                item.txtNumber.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xlime_oval_small));
                break;
            case CityApplication.THEME_GREY:
                item.txtNumber.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xgrey_oval_small));
                break;
            case CityApplication.THEME_BLUE:
                item.txtNumber.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xblue_oval_small));
                break;
            case CityApplication.THEME_BLUE_GRAY:
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
