package com.boha.citylibrary.util;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.client.methods.HttpRequestBase;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLDecoder;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import khandroid.ext.apache.http.util.ByteArrayBuffer;

/**
 * Created by aubreyM on 15/01/21.
 */
public class CommsUtil {

    public interface CommsListener {
        public void onStringResponse(String response);
        public void onError(String message);
    }

    public static final String APP_URL = "http://41.160.126.146/esbapi/V1/",
                NEWS_CATEGORIES = "newsCategories", LOGIN = "userlogin";
    static final String LOG = CommsUtil.class.getSimpleName();
    static CommsListener commsListener;
    static String suff, user, pwd, response;
    static Context ctx;
    static boolean isResponseZipped;

    public static void sendRequest(Context c, String suffix, String userID, String passwd, boolean
            zipped, CommsListener listener) {
        commsListener = listener;
        suff = suffix;
        user = userID;
        pwd = passwd;
        commsListener = listener;
        ctx = c;
        isResponseZipped = zipped;
        new Task().execute();
    }

    private static class Task extends AsyncTask<Void, Void, Integer> {
        @Override
        protected Integer doInBackground(Void... params) {
            try {
                //ClientFormLogin.authenticate(APP_URL+suff,user,pwd);
                if (isResponseZipped) {
                    response = getDataCompressed(ctx, suff, user, pwd, commsListener);
                } else {
                    response = getData(ctx, suff, user, pwd, commsListener);
                }
                Log.i(LOG,"### server response: " + response);
            } catch (CommsException e) {
                e.printStackTrace();
                return 9;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (result > 0) {
                commsListener.onError("Error getting data");
            } else {
                commsListener.onStringResponse(response);
            }
        }
    }
    //http://41.160.126.146/esbapi/V2/userlogin?username=7406190168080&password=vatawa&latitude=-29.859701442126745&longitude=31.014404296875
    public static String getZippedData(Context c, String suffix,
                                       String userName, String passwd, CommsListener listener)
            throws CommsException {
        String request = "http://41.160.126.146/esbapi/V2/userlogin?username=7406190168080&password=vatawa&latitude=-29.859701442126745&longitude=31.014404296875";
        String x;
        try {
            x = URLDecoder.decode(request, "UTF-8");
            Log.d(LOG, "getZippedData: sending request: .......\n" + x);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        HttpURLConnection con = null;
        URL url;
        String response = null;
        InputStream is = null;
        Gson gson = new Gson();

        try {
            Authenticator.setDefault(new CustomAuthenticator());
            url = new URL(request);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent","Mozilla/5.0 ( compatible ) ");
            con.setRequestProperty("Accept","*/*");

            // Start the query
            con.connect();
            is = con.getInputStream();
            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(is));
            @SuppressWarnings("unused")
            ZipEntry entry = null;
            ByteArrayBuffer bab = new ByteArrayBuffer(2048);
            while ((entry = zis.getNextEntry()) != null) {
                int size = 0;
                byte[] buffer = new byte[2048];
                while ((size = zis.read(buffer, 0, buffer.length)) != -1) {
                    bab.append(buffer, 0, size);
                }

            }
            response = new String(bab.toByteArray());

            int code = con.getResponseCode();
            Log.d(LOG, "### (getZipped) HTTP response code: " + code
                    + " msg: " + con.getResponseMessage());
        } catch (IOException e) {
            Log.e(LOG, "Houston, we have a problem - ......", e);
            Log.d(LOG, "Request in error: \n" + request);
            throw new CommsException();
        } catch (Exception e) {
            throw new CommsException();
        } finally {
            try {
                is.close();
            } catch (Exception e) {
                // Log.e(LOG, "Unable to close input stream, but no prob!");
            }
        }
        return response;
    }
    private static String getData(Context c, String suffix,
                                  String userName, String passwd, CommsListener listener)
            throws CommsException {
        commsListener = listener;
        ctx = c;
        user = userName;
        pwd = passwd;
        String uri = APP_URL + suffix;
//        user = "7406190168080";
//        pwd = "vatawa";
//        uri = "http://41.160.126.146/esbapi/V2/userlogin?latitude=-29.859701442126745&longitude=31.014404296875";
        StringBuilder sb = new StringBuilder();
        String response;
        try {
            Authenticator.setDefault(new CustomAuthenticator());
            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("User-Agent","Mozilla/5.0 ( compatible ) ");
            con.setRequestProperty("Accept","*/*");

            Log.w(LOG,"** url: " + url.toString());
            Log.e(LOG,"## httpCode: " + con.getResponseCode() + " message: " + con.getResponseMessage());

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            in.close();
            response = sb.toString();

            Log.i(LOG, "### server response: \n" + response);

        } catch (MalformedURLException e) {
            Log.e(LOG,"Malformed URL: ", e);
            throw new CommsException();
        } catch (IOException e) {
            Log.e(LOG, "IOException", e);
            throw new CommsException();
        }

        return sb.toString();
    }
    private static String getDataCompressed(Context c, String suffix,
                                  String userName, String passwd, CommsListener listener)
            throws CommsException {
        commsListener = listener;
        ctx = c;
        user = userName;
        pwd = passwd;
        String uri = APP_URL + suffix;
        uri = "http://41.160.126.146/esbapi/V2/userlogin?username=7406190168080&password=vatawa&latitude=-29.859701442126745&longitude=31.014404296875";
        StringBuilder sb = new StringBuilder();
        String response;
        try {
            //Authenticator.setDefault(new CustomAuthenticator());
            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("User-Agent","Mozilla/5.0 ( compatible ) ");
            con.setRequestProperty("Accept","*/*");
            con.setRequestProperty("Accept-Encoding", "");
            con.setRequestProperty("Content-Type", "text/plain; charset=utf-8");
            String s = userName + ":" + passwd;
            byte[] basic = Base64.encode(s.getBytes(), Base64.NO_WRAP);
            con.setRequestProperty("Authorization", "Basic " + basic);
            con.setInstanceFollowRedirects(false);
            con.setDoOutput(false);
            con.connect();

            Log.w(LOG,"### url: " + url.toString());
            Log.w(LOG,"### httpCode: " + con.getResponseCode() + " message: " + con.getResponseMessage());

//            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//            String line;
//            while ((line = in.readLine()) != null) {
//                sb.append(line);
//            }
//            in.close();
//            response = sb.toString();
            InputStream is = con.getInputStream();
            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(is));
            @SuppressWarnings("unused")
            ZipEntry entry = null;
            ByteArrayBuffer bab = new ByteArrayBuffer(2048);
            while ((entry = zis.getNextEntry()) != null) {
                int size = 0;
                byte[] buffer = new byte[2048];
                while ((size = zis.read(buffer, 0, buffer.length)) != -1) {
                    bab.append(buffer, 0, size);
                }

            }
            response = new String(bab.toByteArray());

            Log.i(LOG, "### server response: \n" + response);

        } catch (MalformedURLException e) {
            Log.e(LOG,"Malformed URL: ", e);
            throw new CommsException();
        } catch (IOException e) {
            Log.e(LOG, "IOException", e);
            throw new CommsException();
        }

        return sb.toString();
    }
    public static class CustomAuthenticator extends Authenticator {

        protected PasswordAuthentication getPasswordAuthentication() {
            String prompt = getRequestingPrompt();
            String hostname = getRequestingHost();
            InetAddress ipaddr = getRequestingSite();
            int port = getRequestingPort();

            StringBuilder sb = new StringBuilder();
            sb.append("\n##\n####################################\n");
            sb.append("Prompt: ").append(prompt).append("\n");
            sb.append("hostname: ").append(hostname).append("\n");
            sb.append("ipaddr: ").append(ipaddr).append("\n");
            sb.append("port: ").append(port).append("\n");
            sb.append("####################################");
            Log.w(LOG,"#### trying to authenticate ....");

            // Return the information (a data holder that is used by Authenticator)
            char[] arr = pwd.toCharArray();
            PasswordAuthentication auth = new PasswordAuthentication(user, arr);
            return auth;

        }

    }
    private static void addAuthHeader(HttpRequestBase http, String username, String password) throws UnsupportedEncodingException {
        String encoded = Base64.encodeToString((username + ":" + password).getBytes("UTF-8"), Base64.NO_WRAP);
        http.addHeader("AUTHORIZATION", "Basic " + encoded);
    }
}
//root password
//^tyh#eDs1