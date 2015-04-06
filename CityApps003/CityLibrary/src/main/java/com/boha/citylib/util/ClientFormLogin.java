package com.boha.citylib.util;

/**
 * Created by aubreyM on 15/01/29.
 */

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;

/**
 * A example that demonstrates how HttpClient APIs can be used to perform
 * form-based logon.
 */
public class ClientFormLogin {

    public static void authenticate(String url, String userName, String password) throws Exception {
        try {
            HttpClient client = new DefaultHttpClient();

            AuthScope as = new AuthScope("localhost", 443);
            UsernamePasswordCredentials upc = new UsernamePasswordCredentials(
                    userName, password);

            ((AbstractHttpClient) client).getCredentialsProvider()
                    .setCredentials(as, upc);

            BasicHttpContext localContext = new BasicHttpContext();

            BasicScheme basicAuth = new BasicScheme();
            localContext.setAttribute("preemptive-auth", basicAuth);

            HttpHost targetHost = new HttpHost("41.160.126.146", 443, "http");

            HttpGet httpget = new HttpGet(url);
            httpget.setHeader("Content-Type", "application/gzip");

            HttpResponse response = client.execute(targetHost, httpget,
                    localContext);

            HttpEntity entity = response.getEntity();
            Object content = EntityUtils.toString(entity);

            Log.d(MY_APP_TAG, "OK: " + content.toString());

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(MY_APP_TAG, "Error: " + e.getMessage());
        }
    }
    static final String MY_APP_TAG = ClientFormLogin.class.getSimpleName();
}