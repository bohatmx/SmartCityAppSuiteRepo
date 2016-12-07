package com.boha.library.jsonreader;

import com.boha.library.rssreader.FeedItem;

import java.util.ArrayList;

/**
 * Created by Nkululeko on 2016/09/08.
 */
public class NewsFeeditems {

    ArrayList<FeedItem> feedItems;

    public ArrayList<FeedItem> getFeedItems() {
        return feedItems;
    }

    public void setFeedItems(ArrayList<FeedItem> feedItems) {
        this.feedItems = feedItems;
    }
}
