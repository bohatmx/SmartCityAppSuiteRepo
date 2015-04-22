package com.boha.library.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.dto.AlertTypeDTO;
import com.boha.library.dto.NewsArticleDTO;
import com.boha.library.util.Statics;
import com.boha.library.util.Util;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class NewsListAdapter extends ArrayAdapter<NewsArticleDTO> {

    NewsListListener listener;

    private final LayoutInflater mInflater;
    private final int mLayoutRes;
    private List<NewsArticleDTO> mList;
    private Context ctx;
    static final String LOG = NewsListAdapter.class.getSimpleName();

    public NewsListAdapter(Context context, int textViewResourceId,
                           List<NewsArticleDTO> list, NewsListListener listener) {
        super(context, textViewResourceId, list);
        this.mLayoutRes = textViewResourceId;
        this.listener = listener;
        mList = list;
        ctx = context;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    View view;


    static class ViewHolderItem {
        protected ImageView image;
        protected TextView txtType, txtColor, txtDate, txtDesc, txtTime;
        protected int position;
        protected View mainView;
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
            item.txtType = (TextView) convertView.findViewById(R.id.NEWS_type);
            item.txtDesc = (TextView) convertView.findViewById(R.id.NEWS_desc);
            item.txtColor = (TextView) convertView.findViewById(R.id.NEWS_color);
            item.txtDate = (TextView) convertView.findViewById(R.id.NEWS_date);
            item.txtTime = (TextView) convertView.findViewById(R.id.NEWS_time);
            item.image = (ImageView) convertView.findViewById(R.id.NEWS_image);
            item.mainView = convertView.findViewById(R.id.NEWS_main);


            convertView.setTag(item);
        } else {
            item = (ViewHolderItem) convertView.getTag();
        }

        final NewsArticleDTO p = mList.get(position);
        item.txtColor.setText("" + (position + 1));
        item.txtType.setText(p.getNewsArticleType().getNewsArticleTypeName());
        item.txtDate.setText(sdfDate.format(p.getNewsDate()));
        item.txtTime.setText(sdfTime.format(p.getNewsDate()));
        item.txtDesc.setText(p.getNewsText());
        item.position = position;
        switch (p.getNewsArticleType().getColor()) {
            case AlertTypeDTO.GREEN:
                item.txtColor.setBackground(ctx.getResources().getDrawable(R.drawable.xgreen_oval_small));
                break;
            case AlertTypeDTO.AMBER:
                item.txtColor.setBackground(ctx.getResources().getDrawable(R.drawable.xamber_oval_small));
                break;
            case AlertTypeDTO.RED:
                item.txtColor.setBackground(ctx.getResources().getDrawable(R.drawable.xred_oval_small));
                break;
        }

        item.txtColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onNewsClicked(position);
            }
        });
        item.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onNewsClicked(position);
            }
        });
        Statics.setRomanFontLight(ctx,item.txtDesc);
        //get random image if available ...
        if (p.getNewsArticleImageList() != null && !p.getNewsArticleImageList().isEmpty()) {
            item.image.setVisibility(View.VISIBLE);
            if (p.getNewsArticleImageList().size() == 1) {
                String url = Util.getNewsImageURL(p.getNewsArticleImageList().get(0));
                ImageLoader.getInstance().displayImage(url, item.image);
            } else {
                int index = random.nextInt(p.getNewsArticleImageList().size() - 1);
                String url = Util.getNewsImageURL(p.getNewsArticleImageList().get(index));
                ImageLoader.getInstance().displayImage(url, item.image);
            }

        } else {
            item.image.setVisibility(View.GONE);
        }
        return (convertView);
    }


    public interface NewsListListener {
        public void onNewsClicked(int position);
    }
    static final Random random = new Random(System.currentTimeMillis());
    static final Locale loc = Locale.getDefault();
    static final SimpleDateFormat sdfDate = new SimpleDateFormat("EEE dd MMM yyyy", loc);
    static final SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm", loc);
}
