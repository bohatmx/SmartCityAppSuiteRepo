package com.boha.library.util;

/**
 * Created by aubreyM on 14/11/22.
 */

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FileDownloader {
    private static final int  MEGABYTE = 1024 * 1024;
    public interface FileDownloaderListener {
        public void onFileDownloaded(File file);
        public void onError();
    }
    static int mYear, mMonth;
    static String accountNumber;
    static FileDownloaderListener listener;
    static File pdfFile;
    static Context ctx;
    static String URLString;
    static URL url;

    public static void downloadStatementPDF(Context c,String acc, int year, int month,
        FileDownloaderListener mFileDownloaderListener){
        mYear = year;
        mMonth = month;
        accountNumber = acc;
        ctx = c;
        listener = mFileDownloaderListener;
        File directory = Environment.getExternalStorageDirectory();
        File myDir = new File(directory, "smartCity");
        if (!myDir.exists()) {
            myDir.mkdir();
        }
        StringBuilder x = new StringBuilder();
        x.append(accountNumber).append("_").append(mYear).append("_")
                .append(mMonth).append(".pdf");
        String fileName = x.toString();
        pdfFile = new File(myDir,fileName);
        String u = Util.getStatementURL(ctx,accountNumber,mYear,mMonth);
        try {
            url = new URL(u);
            new DownTask().execute();
        } catch (MalformedURLException e) {
            listener.onError();
        }
    }
    public static void downloadStatementPDF(Context c,
                                            String accountNumber,
                                            String fileName,
                                            FileDownloaderListener mFileDownloaderListener){

        ctx = c;
        listener = mFileDownloaderListener;
        File directory = Environment.getExternalStorageDirectory();
        File myDir = new File(directory, "smartCity");
        if (!myDir.exists()) {
            myDir.mkdir();
        }
        pdfFile = new File(myDir,fileName);
        StringBuilder sb = Util.getStartURL(SharedUtil.getMunicipality(ctx).getMunicipalityID());
        sb.append("/documents/account_").append(accountNumber).append("/");
        sb.append(fileName);
        try {
            url = new URL(sb.toString());
            Log.w("+++++ FileDownloader","### downloading " + url);
            new DownTask().execute();
        } catch (MalformedURLException e) {
            listener.onError();
        }

    }
    static class DownTask extends AsyncTask<Void, Void, Integer> {


        @Override
        protected Integer doInBackground(Void... params) {
            try {
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                FileOutputStream fileOutputStream = new FileOutputStream(pdfFile);
                int totalSize = urlConnection.getContentLength();
                System.out.println("##### downloaded content size of pdf: " + totalSize);

                byte[] buffer = new byte[MEGABYTE];
                int bufferLength = 0;
                while((bufferLength = inputStream.read(buffer))>0 ){
                    fileOutputStream.write(buffer, 0, bufferLength);
                }
                fileOutputStream.close();
                Log.d("FileDownloader","##### pdf file: " + pdfFile.getAbsolutePath()
                        + " length: " + pdfFile.length());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return 9;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return 9;
            } catch (IOException e) {
                e.printStackTrace();
                return 9;
            }
            return 0;
        }
        @Override
        public void onPostExecute(Integer res) {
            if (res > 0) {
               listener.onError();
                return;
            }
            listener.onFileDownloaded(pdfFile);
        }
    }

}
