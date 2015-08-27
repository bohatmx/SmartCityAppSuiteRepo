package com.boha.library.util;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.boha.library.transfer.ResponseDTO;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by aubreyM on 15/a01/31.
 */
public class CacheUtil {
    public interface CacheRetrievalListener {
        public void onCacheRetrieved(ResponseDTO response);

        public void onError();
    }

    public interface FAQCacheRetrievalListener {
        public void onCacheRetrieved(FaqStrings faqStrings);

        public void onError();
    }

    public interface CacheListener {
        public void onDataCached();

        public void onError();
    }

    static Context ctx;
    static int dataType;
    public static final int CACHE_LOGIN = 1, CACHE_ALERTS = 2, CACHE_NEWS = 3, CACHE_FAQ = 4;
    public static final String JSON_DATA = "file.json", JSON_ALERTS = "alerts.json",
            JSON_NEWS = "news.json", JSON_FAQ = "faq.json";
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

    static FAQCacheRetrievalListener faqCacheRetrievalListener;

    public static void getCachedFAQ(Context context, FAQCacheRetrievalListener listener) {
        faqCacheRetrievalListener = listener;
        ctx = context;
        dataType = CACHE_FAQ;
        new FAQCacheRetrieveTask().execute();
    }

    static FaqStrings faqStrings;

    public static void cacheAlertData(Context context, ResponseDTO w, CacheListener listener) {
        cacheListener = listener;
        response = w;
        ctx = context;
        dataType = CACHE_ALERTS;
        new CacheTask().execute();
    }

    public static void cacheNewsData(Context context, ResponseDTO w, CacheListener listener) {
        cacheListener = listener;
        response = w;
        ctx = context;
        dataType = CACHE_NEWS;
        new CacheTask().execute();
    }

    public static void cacheFAQ(Context context, FaqStrings w, CacheListener listener) {
        cacheListener = listener;
        faqStrings = w;
        ctx = context;
        dataType = CACHE_FAQ;
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
                    case CACHE_NEWS:
                        json = gson.toJson(response);
                        outputStream = ctx.openFileOutput(JSON_NEWS, Context.MODE_PRIVATE);
                        write(outputStream, json);
                        file = ctx.getFileStreamPath(JSON_NEWS);
                        if (file != null) {
                            Log.e(LOG, "News cache written, path: " + file.getAbsolutePath() +
                                    " - length: " + file.length());
                        }
                        break;
                    case CACHE_FAQ:
                        json = gson.toJson(faqStrings);
                        outputStream = ctx.openFileOutput(JSON_FAQ, Context.MODE_PRIVATE);
                        write(outputStream, json);
                        file = ctx.getFileStreamPath(JSON_FAQ);
                        if (file != null) {
                            Log.e(LOG, "FAQ cache written, path: " + file.getAbsolutePath() +
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
            ResponseDTO response = null;
            try {
                response = gson.fromJson(json, ResponseDTO.class);
            } catch (JsonSyntaxException e) {
                Log.e(LOG, "-- JSON Error: " + e.getMessage());
                throw new IOException(e.getMessage());
            }
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
                Log.d(LOG, "#### cache file not found - returning a new response object, type = " + dataType);

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

    static class FAQCacheRetrieveTask extends AsyncTask<Void, Void, FaqStrings> {

        private FaqStrings getData(FileInputStream stream) throws IOException {
            String json = getStringFromInputStream(stream);
            Log.e(LOG, "++ faq json: " + json);
            FaqStrings faqStrings = null;
            try {
                faqStrings = gson.fromJson(json, FaqStrings.class);
            } catch (JsonSyntaxException e) {
                Log.e(LOG, "-- JSON Error: " + e.getMessage());
                throw new IOException(e.getMessage());
            }
            return faqStrings;
        }

        @Override
        protected FaqStrings doInBackground(Void... voids) {
            Log.w(LOG, "### doInBackground FAQCacheRetrieveTask");
            FaqStrings faqStrings = new FaqStrings();
            FileInputStream stream;
            try {
                switch (dataType) {
                    case CACHE_FAQ:
                        stream = ctx.openFileInput(JSON_FAQ);
                        faqStrings = getData(stream);
                        Log.i(LOG, "++ faq cache retrieved:");
                        break;
                }

            } catch (FileNotFoundException e) {
                Log.e(LOG, "#### faq cache file not found - returning a new response object, type = " + dataType);

            } catch (IOException e) {
                Log.e(LOG, "------------ Failed to retrieve cache", e);
                faqStrings = null;
            }
            return faqStrings;
        }

        @Override
        public void onPostExecute(FaqStrings result) {
            Log.i(LOG, "+++ onPostExecute result: " + result);
            if (faqCacheRetrievalListener != null) {
                if (result == null) {
                    faqCacheRetrievalListener.onError();
                } else {
                    faqCacheRetrievalListener.onCacheRetrieved(result);
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
