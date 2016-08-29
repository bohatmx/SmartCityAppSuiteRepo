package com.boha.library.rssreader;

import android.content.Context;
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

/**
 * Created by Nkululeko on 2016/08/24.
 */
public class ReadRssAdapter extends RecyclerView.Adapter<ReadRssAdapter.MyViewHolder> {

    NewsListListener listener;
    ArrayList<FeedItem> feedItems;
    Context context;
    public ReadRssAdapter(Context context, ArrayList<FeedItem> feedItems, NewsListListener listener) {
        this.feedItems = feedItems;
        this.context = context;
        this.listener = listener;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custum_row_news_item,parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    private static final String TEXT = "text/html", UTF = "UTF-8";
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final FeedItem current = feedItems.get(position);
        holder.Title.setText(current.getTitle());
        holder.webViewDescription.loadData(current.description, TEXT, UTF);

      //  holder.Description.setText(current.getDescription());
        holder.Date.setText(current.getPubDate());
        Picasso.with(context).load(current.getThumbnailUrl()).into(holder.Thumbnail);
        holder.Title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onNewsClicked(current.hashCode());
            }
        });
        holder.Thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onNewsClicked(current.hashCode());
            }
        });
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
            webViewDescription = (WebView) itemView.findViewById(R.id.description_text);
          //  Description = (TextView) itemView.findViewById(R.id.description_text);
            Date = (TextView) itemView.findViewById(R.id.date_text);
            Thumbnail = (ImageView) itemView.findViewById(R.id.thumb_img);

            cardView = (CardView) itemView.findViewById(R.id.cardView);
          //  readMore = (TextView) itemView.findViewById(R.id.more_txt);
          //  readMore.setVisibility(View.GONE);
            //MoreIMG = (ImageView) itemView.findViewById(R.id.more_img);
           // MoreIMG.setVisibility(View.GONE);

        }
    }

    public interface NewsListListener {
     //   public void onNewsClicked(FeedItem feedItem);
     public void onNewsClicked(int position);
    }
}
