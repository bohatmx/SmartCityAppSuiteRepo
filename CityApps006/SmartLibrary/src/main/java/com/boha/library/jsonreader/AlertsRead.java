package com.boha.library.jsonreader;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.boha.library.R;
import com.boha.library.rssreader.AlertReadRssAdapter;
import com.boha.library.rssreader.FeedItem;
import com.boha.library.util.Util;

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
public class AlertsRead extends AsyncTask<Void,Void,Void>{
    Context context;
    ProgressDialog progressDialog;

    public ArrayList<AlertsFeedItems> alertsFeedItems;
    RecyclerView recyclerView;
    public AlertsRead(Context context, RecyclerView recyclerView){
        this.context = context;
        this.recyclerView = recyclerView;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
    }

    @Override
    protected Void doInBackground(Void... voids) {
        String url = "http://icsmnewsdev.oneconnectgroup.com/et/alerts/json/Alerts.json";
        JSONObject json = getJSONFromUrl(url);

        parseJson(json);
        return null;
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
        if (alertsFeedItems != null) {
            Log.i(LOG, "" + alertsFeedItems.size());

            AlertsReadAdapter adapter = new AlertsReadAdapter(context, alertsFeedItems, new AlertsReadAdapter.NewsListListener() {
                @Override
                public void onNewsClicked() {

                }
            });

            recyclerView.setAdapter(adapter);
        } else {
            Log.i(LOG, "alertsFeedItems is null" );
        }

    }
    Snackbar snackbar;

    public static final String LOG = AlertsRead.class.getSimpleName();


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
        try {

             if (json.has("feedItems")) {
            JSONArray posts = json.getJSONArray("feedItems");

            alertsFeedItems = new ArrayList<>();

            for (int i = 0; i < posts.length(); i++) {
                JSONObject post = (JSONObject) posts.getJSONObject(i);
                AlertsFeedItems item = new AlertsFeedItems();
                item.setCategory(post.getString("category"));
                Log.i(LOG, post.getString("category"));
                item.setExpiryDate(post.getString("expiryDate"));
                Log.i(LOG, post.getString("expiryDate"));
                item.setGuid(post.getString("guid"));
                Log.i(LOG, post.getString("guid"));
                item.setThumbnailUrl(post.getString("thumbnailUrl"));
                Log.i(LOG, post.getString("thumbnailUrl"));
                item.setTitle(post.getString("title"));
                Log.i(LOG, post.getString("title"));
                if (post.has("latitude")) {
                    item.setLatitude(post.getString("latitude"));
                    Log.i(LOG, post.getString("latitude"));

                } else {
                    Log.i(LOG, "no latitude");
                }
                if (post.has("longitude")) {
                    item.setLongitude(post.getString("longitude"));
                    Log.i(LOG, post.getString("longitude"));
                } else {
                    Log.i(LOG, "no longitude");
                }

                item.setPubDate(post.getString("pubDate"));
                Log.i(LOG, post.getString("pubDate"));


                alertsFeedItems.add(item);
                Log.i("itemCategory: ", item.getCategory());
                Log.i("itemExpiryDate: ", item.getExpiryDate());
                Log.i("itemGuid: ", item.getGuid());
                if (item.getLatitude() != null) {
                    Log.i("itemLatitude: ", item.getLatitude());
                } else {
                    Log.i("itemLatitude: ", "was not included with this item");
                }
                if (item.getLongitude() != null) {
                    Log.i("itemLongitude: ", item.getLongitude());
                } else {
                    Log.i("itemLongitude: ", "was not included with this item");
                }
                Log.i("itemPubDate: ", item.getPubDate());
                Log.i("itemThumbnailUrl: ", item.getThumbnailUrl());
                Log.i("itemTitle: ", item.getTitle());
                Log.i(LOG, "alerts feed items: " + alertsFeedItems.size());
            }
        } else {
                 Log.i(LOG, "no alerts from alerts feed");
                 snackbar = Util.showSnackBar(recyclerView, context.getString(R.string.no_alerts), "Dismiss",
                         Color.parseColor("Cyan"));
             }

        } catch (JSONException e){
            Log.e(LOG, e.getMessage());
        }
    }
}
