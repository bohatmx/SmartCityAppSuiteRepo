package com.boha.testservicesapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;


public class MainActivity extends ActionBarActivity {

    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = (WebView)findViewById(R.id.webView);



        new TaskN().execute();
    }

    class TaskN extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            basicAuthDemo();
            return null;
        }
    }

    private static final String HOST_NAME = "41.160.126.146";
    private static final String USER_NAME = "7406190168080";
    private static final String PASSWORD = "vatawa";
    public static final String URL = "http://41.160.126.146/esbapi/V2/",
            NEWS_CATEGORIES = "newsCategories", LOGIN = "userlogin";

    private void basicAuthDemo() {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        try {
            httpclient.getCredentialsProvider().setCredentials(
                    new AuthScope(HOST_NAME, 443),
                    new UsernamePasswordCredentials(USER_NAME, PASSWORD));

            HttpGet httpget = new HttpGet(URL + LOGIN);

            Log.w(LOG,"### executing request\n" + httpget.getRequestLine());
            HttpResponse response = httpclient.execute(httpget);
            final HttpEntity entity = response.getEntity();
            final String resp = EntityUtils.toString(entity);
           Log.w(LOG, "## Status Line:\n" + response.getStatusLine().getReasonPhrase()
           + " status code: " + response.getStatusLine().getStatusCode());
            if (entity != null) {
                Log.d(LOG,"Response content length: " + entity.getContentLength());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                            webView.loadData(resp, "text/html", "UTF-8");

                    }
                });

                Log.e(LOG, "Entity to String\n" + resp);
            }
        } catch (Exception e) {
            Log.e(LOG,"--- FUCK!!", e);
        } finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    static final String LOG = MainActivity.class.getSimpleName();
}
