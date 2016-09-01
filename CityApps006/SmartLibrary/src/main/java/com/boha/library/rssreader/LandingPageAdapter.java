package com.boha.library.rssreader;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.boha.library.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Nkululeko on 2016/08/30.
 */
public class LandingPageAdapter extends RecyclerView.Adapter<LandingPageAdapter.MyViewHolder> {

    NewsListListener listener;
    ArrayList<FeedItem> feedItems;
    Context context;
    public LandingPageAdapter(Context context, ArrayList<FeedItem> feedItems, NewsListListener listener) {
        this.feedItems = feedItems;
        this.context = context;
        this.listener = listener;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.landing_page_row_news_itm,parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    private static final String TEXT = "text/html", UTF = "UTF-8";
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final FeedItem current = feedItems.get(position);
        /*Collections.reverse(feedItems);*/

        holder.Title.setText(current.getTitle());
        holder.webViewDescription.loadData(current.description, TEXT, UTF);

        //  holder.Description.setText(current.getDescription());
        holder.Date.setText(current.getPubDate().substring(0, Math.min(current.getPubDate().length(), 16)));
        if (current.getThumbnailUrl().isEmpty()){
            holder.Thumbnail.setImageDrawable(ContextCompat.getDrawable( context, R.drawable.news));

        } else {
            Picasso.with(context).load(current.getThumbnailUrl()).into(holder.Thumbnail);
        }
        //  holder.readMore.setText("Read More");
        //   holder.MoreIMG.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.more));


    }

    @Override
    public int getItemCount() {

        return feedItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Title, Description, Date, readMore;
        ImageView Thumbnail, MoreIMG;
        CardView cardView;
        WebView webViewDescription;
        public MyViewHolder(View itemView) {
            super(itemView);
            Title = (TextView) itemView.findViewById(R.id.title_text);
            Title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onNewsClicked();
                }
            });
            webViewDescription = (WebView) itemView.findViewById(R.id.description_text);

            //  Description = (TextView) itemView.findViewById(R.id.description_text);
            Date = (TextView) itemView.findViewById(R.id.date_text);
            Thumbnail = (ImageView) itemView.findViewById(R.id.thumb_img);
            /*Thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onNewsClicked();
                }
            });*/

            cardView = (CardView) itemView.findViewById(R.id.cardView);
            //  readMore = (TextView) itemView.findViewById(R.id.more_txt);
            //  readMore.setVisibility(View.GONE);
            //MoreIMG = (ImageView) itemView.findViewById(R.id.more_img);
            // MoreIMG.setVisibility(View.GONE);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, FullArticleViewActivity.class);
                   intent.putExtra("newsTitle", feedItems.get(getAdapterPosition()).getTitle());
                    intent.putExtra("newsArticle", feedItems.get(getAdapterPosition()).getDescription());
                    intent.putExtra("newsImage", feedItems.get(getAdapterPosition()).getThumbnailUrl());
                    context.startActivity(intent);
                }
            });

        }
    }

    public interface NewsListListener {
        //   public void onNewsClicked(FeedItem feedItem);
        public void onNewsClicked();
    }
}
