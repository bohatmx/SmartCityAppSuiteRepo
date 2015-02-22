package com.boha.library.util;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.boha.library.transfer.ResponseDTO;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by aubreyM on 15/01/31.
 */
public class CacheUtil {
    public interface CacheRetrievalListener {
        public void onCacheRetrieved(ResponseDTO response);
        public void onError();
    }
    public interface CacheListener {
        public void onDataCached();
        public void onError();
    }
    static Context ctx;
    static int dataType;
    public static final int CACHE_LOGIN = 1, CACHE_ALERTS = 2;
    public static final String JSON_DATA = "file.json", JSON_ALERTS = "alrts.json";
    static ResponseDTO response;
    static CacheListener cacheListener;
    static CacheRetrievalListener cacheRetrievalListener;

    public static void cacheLoginData(Context context, ResponseDTO w, CacheListener listener) {
        cacheListener = listener;
        response = w;
        ctx = context;
        dataType = CACHE_LOGIN;
        new CacheTask().execute();
    }
    public static void getCacheLoginData(Context context, CacheRetrievalListener listener) {
        cacheRetrievalListener = listener;
        ctx = context;
        dataType = CACHE_LOGIN;
        new CacheRetrieveTask().execute();
    }
    public static void cacheAlertData(Context context, ResponseDTO w, CacheListener listener) {
        cacheListener = listener;
        response = w;
        ctx = context;
        dataType = CACHE_ALERTS;
        new CacheTask().execute();
    }
    public static void getCacheAlertData(Context context, CacheRetrievalListener listener) {
        cacheRetrievalListener = listener;
        ctx = context;
        dataType = CACHE_ALERTS;
        new CacheRetrieveTask().execute();
    }

    static class CacheTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            String json = null;
            File file = null;
            FileOutputStream outputStream;
            try {
                switch (dataType) {
                    case CACHE_LOGIN:
                        json = gson.toJson(response);
                        outputStream = ctx.openFileOutput(JSON_DATA, Context.MODE_PRIVATE);
                        write(outputStream, json);
                        file = ctx.getFileStreamPath(JSON_DATA);
                        if (file != null) {
                            Log.e(LOG, "Login cache written, path: " + file.getAbsolutePath() +
                                    " - length: " + file.length());
                        }
                        break;
                    case CACHE_ALERTS:
                        json = gson.toJson(response);
                        outputStream = ctx.openFileOutput(JSON_ALERTS, Context.MODE_PRIVATE);
                        write(outputStream, json);
                        file = ctx.getFileStreamPath(JSON_ALERTS);
                        if (file != null) {
                            Log.e(LOG, "Alert cache written, path: " + file.getAbsolutePath() +
                                    " - length: " + file.length());
                        }
                        break;

                    default:
                        Log.e(LOG, "######### NOTHING done ...");
                        break;

                }

            } catch (IOException e) {
                Log.e(LOG, "Failed to cache data", e);
                return 9;
            }
            return 0;
        }

        private void write(FileOutputStream outputStream, String json) throws IOException {
            outputStream.write(json.getBytes());
            outputStream.close();
        }

        @Override
        protected void onPostExecute(Integer v) {
            if (cacheListener != null) {
                if (v > 0) {
                    cacheListener.onError();
                } else
                    cacheListener.onDataCached();
            }


        }
    }

    static class CacheRetrieveTask extends AsyncTask<Void, Void, ResponseDTO> {

        private ResponseDTO getData(FileInputStream stream) throws IOException {
            String json = getStringFromInputStream(stream);
            ResponseDTO response = gson.fromJson(json, ResponseDTO.class);
            return response;
        }

        @Override
        protected ResponseDTO doInBackground(Void... voids) {
            ResponseDTO response = new ResponseDTO();
            FileInputStream stream;
            try {
                switch (dataType) {


                    case CACHE_LOGIN:
                        stream = ctx.openFileInput(JSON_DATA);
                        response = getData(stream);
                        Log.i(LOG, "++ login cache retrieved");
                        break;

                    case CACHE_ALERTS:
                        stream = ctx.openFileInput(JSON_ALERTS);
                        response = getData(stream);
                        Log.i(LOG, "++ alert cache retrieved");
                        break;


                }
                response.setStatusCode(0);

            } catch (FileNotFoundException e) {
                Log.d(LOG,"#### cache file not found - returning a new response object, type = " + dataType);

            } catch (IOException e) {
                Log.v(LOG, "------------ Failed to retrieve cache", e);
                response = null;
            }

            return response;
        }

        @Override
        protected void onPostExecute(ResponseDTO result) {
            if (cacheRetrievalListener != null) {
                if (result == null) {
                    cacheRetrievalListener.onError();
                } else {
                    cacheRetrievalListener.onCacheRetrieved(result);
                }
            }

        }
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

    static final String LOG = CacheUtil.class.getSimpleName();
    static final Gson gson = new Gson();
}
