package com.boha.library.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.FileDownloader;

import java.io.File;

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
        Log.w(LOG,"##### onHandleIntent");
        if (intent != null) {
            response = (ResponseDTO) intent.getSerializableExtra("response");
            accountNumber = intent.getStringExtra("accountNumber");
            if (response != null && !response.getPdfFileNameList().isEmpty()) {
                controlDownloads();
            } else {
                Log.e(LOG,"*** no files available for download");
            }


        }
    }
    int index = 0, count = 0;
    private void controlDownloads() {
        if (index < response.getPdfFileNameList().size()) {
            performDownload(response.getPdfFileNameList().get(index));
        } else {
            Log.i(LOG,"++++ statement files downloaded for acct: " + accountNumber + ": " + count);
        }

    }
    private void performDownload(final String fileName) {
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
                Log.e(LOG,"--- failed. statement download: " + fileName);
                index++;
                controlDownloads();
            }
        });


    }
    static final String LOG = StatementService.class.getSimpleName();
}
