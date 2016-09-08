package com.boha.library.jsonreader;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.boha.library.rssreader.AlertReadRss;
import com.boha.library.rssreader.FeedItem;
import com.boha.library.rssreader.ReadRssAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import khandroid.ext.apache.http.HttpEntity;
import khandroid.ext.apache.http.HttpResponse;
import khandroid.ext.apache.http.client.ClientProtocolException;
import khandroid.ext.apache.http.client.methods.HttpPost;
import khandroid.ext.apache.http.impl.client.DefaultHttpClient;

/**
 * Created by Nkululeko on 2016/09/08.
 */
public class NewsRead extends AsyncTask<Void,Void,Void> {
    Context context;
    ProgressDialog progressDialog;
    public ImageView image;
    public TextView title;

    public ArrayList<NewsFeedItems> newsFeedItems;
    RecyclerView recyclerView;
    public  NewsRead(Context context, RecyclerView recyclerView){
        this.context = context;
        this.recyclerView = recyclerView;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
    }

    @Override
    protected void onPreExecute() {
        progressDialog.show();
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        progressDialog.dismiss();
        NewsReadAdapter adapter = new NewsReadAdapter(context, newsFeedItems, new NewsReadAdapter.NewsListListener() {
            @Override
            public void onNewsClicked() {

            }
        });
        recyclerView.setAdapter(adapter);
        // adapter.notifyDataSetChanged();
    }




    @Override
    protected Void doInBackground(Void... params) {
        String url = "http://icsmnewsdev.oneconnectgroup.com/et/news/json/News.json";
        JSONObject json = getJSONFromUrl(url);

        parseJson(json);
        return null;
    }
    public JSONObject getJSONFromUrl(String url) {
        Log.i(LOG, "getJSONFromUrl");
        InputStream is = null;
        JSONObject jObj = null;
        String json = null;

        try{
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
        } catch (UnsupportedEncodingException e) {
            Log.e(LOG, e.getMessage());
        }catch (ClientProtocolException e) {
            Log.e(LOG, e.getMessage());
        } catch (IOException e) {
            Log.e(LOG, e.getMessage());
        }

        try{
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());

        }
        return jObj;
    }

    public void parseJson(JSONObject json) {
        Log.i(LOG, "ParseJson");
        try{
            JSONArray posts = json.getJSONArray("feedItems");

            newsFeedItems = new ArrayList<>();

            for (int i = 0; i < posts.length(); i++) {
                JSONObject post = (JSONObject) posts.getJSONObject(i);
                NewsFeedItems item = new NewsFeedItems();
                item.setDescription(post.getString("description"));
                Log.i(LOG, post.getString("description"));
                item.setExpiryDate(post.getString("expiryDate"));
                Log.i(LOG, post.getString("expiryDate"));
                item.setGuid(post.getString("guid"));
                Log.i(LOG, post.getString("guid"));
                item.setLink(post.getString("link"));
                Log.i(LOG, post.getString("link"));
                item.setPubDate(post.getString("pubDate"));
                Log.i(LOG, post.getString("pubDate"));
                item.setThumbnailUrl(post.getString("thumbnailUrl"));
                Log.i(LOG, post.getString("thumbnailUrl"));
                item.setTitle(post.getString("title"));
                Log.i(LOG, post.getString("title"));

                newsFeedItems.add(item);
                Log.i("itemDescription: ", item.getDescription());
                Log.i("itemExpiryDate: ", item.getExpiryDate());
                Log.i("itemGuid: ", item.getGuid());
                Log.i("itemLink: ", item.getLink());
                Log.i("itemPubDate: ", item.getPubDate());
                Log.i("itemThumbnailUrl: ", item.getThumbnailUrl());
                Log.i("itemTitle: ", item.getTitle());
            }

        } catch (JSONException e){
            Log.e(LOG, e.getMessage());
        }
    }


    public static final String LOG = NewsRead.class.getSimpleName();
}
