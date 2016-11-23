package com.boha.library.jsonreader;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.boha.library.util.Util;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

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
    public ImageView image;
    public TextView title;
    LayoutInflater inflater;

    public NFeedItems feedItems;
    RecyclerView recyclerView;
    public static final String URL = "http://icsmnewsdev.oneconnectgroup.com/et/news/json/News.json";

    public  NewsRead(Context context, RecyclerView recyclerView){
        this.context = context;
        this.recyclerView = recyclerView;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if (feedItems.getFeedItems()/*.size() >1*/ != null) {
                Log.i(LOG, "feedItems: " + feedItems.getFeedItems().size());
            NewsReadAdapter adapter = new NewsReadAdapter(context, feedItems.getFeedItems(), new NewsReadAdapter.NewsListListener() {
                @Override
                public void onNewsClicked() {

                }
            });
            recyclerView.setAdapter(adapter);
        } else {
            Log.i(LOG, "news feedItems is null" );
            Util.showSnackBar(recyclerView, "No headlines to display", "Dismiss", Color.parseColor("RED"));

        }

    }
    Snackbar snackbar;




    @Override
    protected Void doInBackground(Void... params) {
        getFeedItems(URL);
        return null;
    }
    public void getFeedItems(String url) {
        Log.i(LOG, "getFeedItems");
        InputStream is = null;
        String json = null;

        feedItems = new NFeedItems();

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
            feedItems = gson.fromJson(json,NFeedItems.class);
        } catch (UnsupportedEncodingException e) {
            Log.e(LOG, e.getMessage());
        }catch (ClientProtocolException e) {
            Log.e(LOG, e.getMessage());
        } catch (Exception e) {
            Log.e(LOG, e.getMessage());
        }


    }

    static final Gson gson = new Gson();
    public static final String LOG = NewsRead.class.getSimpleName();
}
