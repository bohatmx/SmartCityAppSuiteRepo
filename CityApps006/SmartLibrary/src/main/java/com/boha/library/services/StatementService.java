package com.boha.library.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.FileDownloader;

import java.io.File;
import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class StatementService extends IntentService {

    public StatementService() {
        super("StatementService");
    }

    ResponseDTO response;
    String accountNumber;

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.w(LOG, "##### onHandleIntent");
        if (intent != null) {
            response = (ResponseDTO) intent.getSerializableExtra("response");
            accountNumber = intent.getStringExtra("accountNumber");
            fileNameList = response.getPdfFileNameList();
            if (fileNameList != null && !fileNameList.isEmpty()) {
                controlDownloads();
            } else {
                Log.e(LOG, "*** no files available for download");
            }

        } else {
            controlDownloads();
        }
    }

    List<String> fileNameList;
    int index = 0, count = 0;
    StatementListener statementListener;

    private void controlDownloads() {
        if (index < fileNameList.size()) {
            performDownload(fileNameList.get(index));
        } else {
            if (statementListener != null) {
                statementListener.onDownloadsComplete(count);
            }
            Log.i(LOG, "++++ statement files downloaded for acct: " + accountNumber + ": " + count);
        }

    }

    public void downloadPDFs(String accountNumber, List<String> fileNames,
                             StatementListener listener) {
        this.accountNumber = accountNumber;
        fileNameList = fileNames;
        statementListener = listener;
        count = 0;
        index = 0;

        controlDownloads();


    }

    public interface StatementListener {
        void onDownloadsComplete(int count);
    }

    private void performDownload(final String fileName) {
        Log.i(LOG, "++++ performDownload: " + fileName);
                 FileDownloader.downloadStatementPDF(getApplicationContext(), accountNumber, fileName,
                         new FileDownloader.FileDownloaderListener() {
                             @Override
                             public void onFileDownloaded(File file) {
                                 index++;
                                 count++;
                                 controlDownloads();
                             }

                             @Override
                             public void onError() {
                                 Log.e(LOG, "--- failed. statement download: " + fileName);
                                 index++;
                                 controlDownloads();
                             }
                         });


    }

    public class LocalBinder extends Binder {
        public StatementService getService() {
            Log.e(LOG, "LocalBinder getService");
            return StatementService.this;
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.w(LOG, "IBinder onBind - returning binder");
        return mBinder;
    }

    private final IBinder mBinder = new LocalBinder();
    static final String LOG = StatementService.class.getSimpleName();
}
