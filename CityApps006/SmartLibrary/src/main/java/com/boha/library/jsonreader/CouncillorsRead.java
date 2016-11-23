package com.boha.library.jsonreader;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import khandroid.ext.apache.http.HttpEntity;
import khandroid.ext.apache.http.HttpResponse;
import khandroid.ext.apache.http.client.ClientProtocolException;
import khandroid.ext.apache.http.client.methods.HttpPost;
import khandroid.ext.apache.http.impl.client.DefaultHttpClient;

/**
 * Created by Nkululeko on 2016/11/19.
 */

public class CouncillorsRead extends AsyncTask<Void,Void,Void> {

    Context context;
    ArrayList<CouncillorsFeedItem> councillorsFeedItems;
    RecyclerView recyclerView;
    public static final String URL = "http://icsmnewsdev.oneconnectgroup.com/et/info/councillors/councillors.json";
    public CouncillorsRead(Context context, RecyclerView recyclerView){
        this.context = context;
        this.recyclerView = recyclerView;

    }

    @Override
    protected Void doInBackground(Void... params) {
        getJSONFromUrl(URL);
        return null;
    }

    CouncillorFeedItems councillors;

    public void getJSONFromUrl(String url) {
        Log.i(LOG, "getFeedItems");
        InputStream is = null;
        JSONObject jObj = null;
        String json = null;
        councillors = new CouncillorFeedItems();

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
                sb.append(line /*+ "\n"*/);
            }

            is.close();
            json = sb.toString();
            councillors = gson.fromJson(json,CouncillorFeedItems.class);
            Log.e(LOG, "getFeedItems: " + json );
        } catch (UnsupportedEncodingException e) {
            Log.e(LOG, e.getMessage());
        }catch (ClientProtocolException e) {
            Log.e(LOG, e.getMessage());
        } catch (IOException e) {
            Log.e(LOG, e.getMessage());
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (councillors.getCouncillorsFeedItems()!= null) {
            Log.i(LOG, "onPostExecute: feedItems:" + councillors.getCouncillorsFeedItems().size());

            CouncillorsReadAdapter adapter = new CouncillorsReadAdapter(context, councillors.councillorsFeedItems);
            recyclerView.setAdapter(adapter);
        } else {
            Log.i(LOG, "councillorsFeedItems is null" );
            //setup no councillors adapter, to show no councillors were found
          //  NoAlertsAdapter noAlertsAdapter = new NoAlertsAdapter(/*context*//*, feeditems.feedItems*/);
          //  recyclerView.setAdapter(noAlertsAdapter);

            // View v = getLayoutInflater().inflate(R.layout.complaint_map_info_window, null);
            // Util.showSnackBar(recyclerView, "No alerts to display", "Dismiss", Color.parseColor("RED"));
        }
    }

    static final Gson gson = new Gson();
    public static final String LOG = CouncillorsRead.class.getSimpleName();
}
