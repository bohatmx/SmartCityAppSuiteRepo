package com.boha.library.util;

import android.content.Context;
import android.util.Log;

import com.boha.library.transfer.RequestDTO;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by aubreyM on 15/06/20.
 */
public class RequestCache {

    public interface RequestCacheListener {
        void onRequestAdded();

        void onRequestsFound(RequestList list);

        void onError();

        void onRequestsRemoved();
    }

    static final Gson GSON = new Gson();
    static final String LOG = RequestCache.class.getSimpleName();
    public static final String JSON_REQUESTS = "requests.json";

    public static void updateRequests(final Context ctx, final RequestCacheListener listener) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RequestList requestList;
                FileInputStream stream;
                try {
                    stream = ctx.openFileInput(JSON_REQUESTS);
                    String json = getStringFromInputStream(stream);
                    requestList = GSON.fromJson(json, RequestList.class);
                    Log.i(LOG, "++ request cache retrieved for sync update. entries: " + requestList.getRequestList().size());
                } catch (IOException e) {
                    Log.e(LOG, "No cache file exists. starting new cache");
                    requestList = new RequestList();
                }

                //update requests
                for (RequestDTO z : requestList.getRequestList()) {
                    z.setLastSyncAttemptDate(new Date().getTime());
                }

                //write cache back
                FileOutputStream outputStream;
                String json = GSON.toJson(requestList);
                try {
                    outputStream = ctx.openFileOutput(JSON_REQUESTS, Context.MODE_PRIVATE);
                    write(outputStream, json);
                    Log.e(LOG, "request cache sync updated, entries: " + requestList.getRequestList().size());
                    if (listener != null)
                        listener.onRequestAdded();

                } catch (IOException e) {
                    Log.e(LOG, "File failed", e);
                    if (listener != null)
                        listener.onError();
                }
            }
        });
        thread.start();
    }

    public static void addRequest(final Context ctx, final RequestDTO request, final RequestCacheListener listener) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RequestList requestList;
                FileInputStream stream;
                try {
                    stream = ctx.openFileInput(JSON_REQUESTS);
                    String json = getStringFromInputStream(stream);
                    requestList = GSON.fromJson(json, RequestList.class);
                    Log.i(LOG, "++ request cache retrieved. entries: " + requestList.getRequestList().size());
                } catch (IOException e) {
                    Log.e(LOG, "No cache file exists. starting new cache");
                    requestList = new RequestList();
                }

                //add new request
                requestList.getRequestList().add(request);

                //write cache back
                FileOutputStream outputStream;
                String json = GSON.toJson(requestList);
                try {
                    outputStream = ctx.openFileOutput(JSON_REQUESTS, Context.MODE_PRIVATE);
                    write(outputStream, json);
                    Log.e(LOG, "request cache written");
                    if (listener != null)
                        listener.onRequestAdded();

                } catch (IOException e) {
                    Log.e(LOG, "File failed", e);
                    if (listener != null)
                        listener.onError();
                }
            }
        });
        thread.start();
    }

    public static void getRequests(final Context ctx, final RequestCacheListener listener) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RequestList requestList;
                FileInputStream stream;
                try {
                    stream = ctx.openFileInput(JSON_REQUESTS);
                    String json = getStringFromInputStream(stream);
                    requestList = GSON.fromJson(json, RequestList.class);
                    Log.i(LOG, "++ request cache retrieved. entries: " + requestList.getRequestList().size());
                    listener.onRequestsFound(requestList);
                } catch (IOException e) {
                    Log.e(LOG, "No cache file exists. returning empty list");
                    requestList = new RequestList();
                    listener.onRequestsFound(requestList);
                }

            }
        });
        thread.start();
    }

    public static void removeRequests(final Context ctx, final RequestCacheListener listener) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RequestList requestList;
                FileInputStream stream;
                try {
                    stream = ctx.openFileInput(JSON_REQUESTS);
                    String json = getStringFromInputStream(stream);
                    requestList = GSON.fromJson(json, RequestList.class);
                    Log.i(LOG, "++ request cache retrieved. entries: " + requestList.getRequestList().size());
                } catch (IOException e) {
                    Log.e(LOG, "No cache file exists. starting new cache");
                    requestList = new RequestList();
                }

                //add new request
                requestList.setRequestList(new ArrayList<RequestDTO>());

                //write cache back
                FileOutputStream outputStream;
                String json = GSON.toJson(requestList);
                try {
                    outputStream = ctx.openFileOutput(JSON_REQUESTS, Context.MODE_PRIVATE);
                    write(outputStream, json);
                    Log.e(LOG, "request cache emptied");
                    if (listener != null)
                        listener.onRequestsRemoved();

                } catch (IOException e) {
                    Log.e(LOG, "File failed", e);
                    if (listener != null)
                        listener.onError();
                }
            }
        });
        thread.start();
    }

    private static void write(FileOutputStream outputStream, String json) throws IOException {
        outputStream.write(json.getBytes());
        outputStream.close();
    }

    private static String getStringFromInputStream(InputStream is) throws IOException {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } finally {
            if (br != null) {
                br.close();
            }
        }
        String json = sb.toString();
        return json;

    }
}
