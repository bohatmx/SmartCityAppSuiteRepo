package com.boha.library.jsonreader;

import android.content.Context;
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
import java.util.Date;

/**
 * Created by Nkululeko on 2016/09/08.
 */
public class AlertsReadAdapter extends RecyclerView.Adapter<AlertsReadAdapter.MyViewHolder> {

    NewsListListener listener;
    ArrayList<AlertsFeedItem> alertsFeedItems;
    Context context;
    public AlertsReadAdapter(Context context, ArrayList<AlertsFeedItem> alertsFeedItems, NewsListListener listener) {
        this.alertsFeedItems = alertsFeedItems;
        this.context = context;
        this.listener = listener;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custum_row_alert_item,parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }



    private static final String TEXT = "text/html", UTF = "UTF-8";
    private Date date = new Date();
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {



        final AlertsFeedItem current = alertsFeedItems.get(position);
        //Collections.reverse(feedItems);


      /*  Collections.sort(alertsFeedItems, new Comparator<AlertsFeedItem>() {
            @Override
            public int compare(AlertsFeedItem alertFeedItem, AlertsFeedItem t1) {
                    *//*if (feedItem.getPubDate() == null || t1.getPubDate() == null)
                        return 0;*//*
                return alertFeedItem.getPubDate().compareToIgnoreCase(t1.getPubDate());
            }
        });*/
        if (current.getTitle().isEmpty() || current.getTitle()== null){
            holder.Title.setText("No Title");
        } else {
            holder.Title.setText(current.getTitle());
        }


     //   holder.webViewDescription.loadData(current.description, TEXT, UTF);

        //  holder.Description.setText(current.getDescription());
        if (alertsFeedItems == null) {

        }
        holder.Date.setText(current.getPubDate().substring(0, Math.min(current.getPubDate().length(), 25)));
        if (current.getThumbnailUrl().isEmpty() || current.getThumbnailUrl() == null){
            holder.Thumbnail.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.news));

        } else {
            Picasso.with(context).load(current.getThumbnailUrl()).into(holder.Thumbnail);
        }
        //  holder.readMore.setText("Read More");
        //   holder.MoreIMG.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.more));


    }

    @Override
    public int getItemCount() {

        return alertsFeedItems == null ? 0 : alertsFeedItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Title, Description, Date, readMore, readLess;
         TextView noAlert;
        ImageView Thumbnail, MoreIMG;
        CardView cardView;
        WebView webViewDescription;
        public MyViewHolder(View itemView) {
            super(itemView);
            Title = (TextView) itemView.findViewById(R.id.title_text);
            noAlert = (TextView) itemView.findViewById(R.id.txtEmptyAlerts);
            noAlert.setVisibility(View.GONE);

           /* Title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  //  iListener.onItemClicked();
                    listener.onNewsClicked();
                }
            });*/
            webViewDescription = (WebView) itemView.findViewById(R.id.description_text);
            webViewDescription.setVisibility(View.GONE);

            //  Description = (TextView) itemView.findViewById(R.id.description_text);
            Date = (TextView) itemView.findViewById(R.id.date_text);

            Thumbnail = (ImageView) itemView.findViewById(R.id.thumb_img);
            /*Thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   // iListener.onItemClicked();
                    listener.onNewsClicked();
                }
            });*/

            cardView = (CardView) itemView.findViewById(R.id.cardView);

            readMore = (TextView) itemView.findViewById(R.id.more_text);
            readMore.setVisibility(View.GONE);
            readMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    webViewDescription.setVisibility(View.VISIBLE);
                    readLess.setVisibility(View.VISIBLE);
                    readMore.setVisibility(View.GONE);
                }
            });
            readLess = (TextView) itemView.findViewById(R.id.less_text);
            readLess.setVisibility(View.GONE);
            readLess.setVisibility(View.GONE);
            readLess.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    readMore.setVisibility(View.VISIBLE);
                    webViewDescription.setVisibility(View.GONE);
                    readLess.setVisibility(View.GONE);


                }
            });
            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, FullDetailActivity.class);
                    intent.putExtra("newsTitle", alertsFeedItems.get(getAdapterPosition()).getTitle());
                 //   intent.putExtra("newsArticle", alertsFeedItems.get(getAdapterPosition()).getDescription());
                    intent.putExtra("newsImage", alertsFeedItems.get(getAdapterPosition()).getThumbnailUrl());
                    context.startActivity(intent);
                }
            });*/
            //  readMore.setVisibility(View.GONE);
            //MoreIMG = (ImageView) itemView.findViewById(R.id.more_img);
            // MoreIMG.setVisibility(View.GONE);

        }
    }
    ItemListener iListener;

    public interface NewsListListener {
        //   public void onNewsClicked(FeedItem feedItem);
        public void onNewsClicked();
        //    void onMoreClicked();
    }

    public interface ItemListener {
        void onItemClicked();

    }
}
