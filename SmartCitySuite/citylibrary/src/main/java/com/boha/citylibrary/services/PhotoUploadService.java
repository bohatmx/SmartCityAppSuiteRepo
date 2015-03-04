package com.boha.citylibrary.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.boha.citylibrary.transfer.PhotoUploadDTO;
import com.boha.citylibrary.transfer.ResponseDTO;
import com.boha.citylibrary.util.PhotoCacheUtil;
import com.boha.citylibrary.util.PictureUtil;
import com.boha.citylibrary.util.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Manages the uploading of photos from a list held in cache. Uploads each photo
 * and notifies any activity bound to it on completion. Otherwise these cached photos
 * are uploaded in a silent process not visible to the user.
 * <p/>
 * It may be started by a startService call or may be bound to an activity via the
 * IBinder interface.
 * <p/>
 * Entry points: onHandleIntent, uploadCachedPhotos
 */
public class PhotoUploadService extends IntentService {

    public PhotoUploadService() {
        super("PhotoUploadService");
    }

    public interface UploadListener {
        public void onUploadsComplete(int count);
    }

    UploadListener uploadListener;
    int count;

    public int getCount() {
        return uploadedList.size();
    }
    public void uploadCachedPhotos(UploadListener listener) {
        uploadListener = listener;
        Log.d(LOG, "#### uploadCachedPhotos, getting cached photos - will start uploads");
        PhotoCacheUtil.getCachedPhotos(getApplicationContext(), new PhotoCacheUtil.PhotoCacheListener() {
            @Override
            public void onFileDataDeserialized(ResponseDTO response) {
                Log.d(LOG, "##### cached photo list returned: " + response.getPhotoUploadList().size());
                list = response.getPhotoUploadList();
                if (list.isEmpty()) {
                    Log.w(LOG, "--- no cached photos for upload");
                    if (uploadListener != null)
                        uploadListener.onUploadsComplete(0);
                    return;
                }
                getLog(response);
                onHandleIntent(null);
            }

            @Override
            public void onDataCached() {

            }

            @Override
            public void onError() {
                Log.e(LOG, "### getCachedPhotos onError ");
            }
        });
    }

    private static void getLog(ResponseDTO cache) {
        StringBuilder sb = new StringBuilder();
        sb.append("## Photos currently in the cache: ")
                .append(cache.getPhotoUploadList().size()).append("\n");
        for (PhotoUploadDTO p : cache.getPhotoUploadList()) {
            if (p.getAlertImage() != null) {
                if (p.getAlertImage().getDateTaken() == null) p.getAlertImage().setDateTaken(new Date());
                sb.append("+++ Alert: ").append(p.getAlertImage().getDateTaken().toString()).append(" lat: ").append(p.getAlertImage().getLatitude());
                sb.append(" lng: ").append(p.getAlertImage().getLatitude());
                if (p.getDateUploaded() != null)
                    sb.append(" ").append(sdf.format(p.getDateUploaded())).append("\n");
                else
                    sb.append(" NOT UPLOADED\n");
            }
            if (p.getComplaintImage() != null) {
                if (p.getComplaintImage().getDateTaken() == null) p.getComplaintImage().setDateTaken(new Date());
                sb.append("+++ Complaint: ").append(p.getComplaintImage().getDateTaken().toString()).append(" lat: ").append(p.getAlertImage().getLatitude());
                sb.append(" lng: ").append(p.getComplaintImage().getLatitude());
                if (p.getDateUploaded() != null)
                    sb.append(" ").append(sdf.format(p.getDateUploaded())).append("\n");
                else
                    sb.append(" NOT UPLOADED\n");
            }

        }
        Log.w(LOG, sb.toString());
    }

    List<PhotoUploadDTO> uploadedList = new ArrayList<>();

    static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.w(LOG, "## onHandleIntent .... starting service");
        if (list == null) {
            uploadCachedPhotos(uploadListener);
            return;
        }
        retryCount = 0;
        controlImageUploads();

    }

    static List<PhotoUploadDTO> list;
    int index;

    private void controlImageUploads() {
        if (index < list.size()) {
            executeUpload(list.get(index));
        } else {
            Log.i(LOG,"*** image uploads complete: "+uploadedList.size()+", checking for failed uploads");
            if (uploadListener != null) {
                uploadListener.onUploadsComplete(uploadedList.size());
                if (!failedUploads.isEmpty()) {
                    Log.e(LOG, "###### we have failedUploads: " + failedUploads.size());
                    retryCount++;
                    if (retryCount < MAX_RETRIES) {
                        Log.w(LOG, "***** retrying failed upload, retryCount: " + retryCount);
                        list = failedUploads;
                        uploadedList.clear();
                        failedUploads.clear();
                        index = 0;
                        controlImageUploads();
                    }
                }
            }
        }

    }

    static final int MAX_RETRIES = 3;
    int retryCount;


    private void executeUpload(final PhotoUploadDTO dto) {
        final long start = System.currentTimeMillis();
        PictureUtil.uploadImage(dto, getApplicationContext(), new PhotoUploadDTO.PhotoUploadedListener() {
            @Override
            public void onPhotoUploaded() {
                long end = System.currentTimeMillis();
                Log.i(LOG, "---- photo uploaded, elapsed: " + Util.getElapsed(start, end) + " seconds");
                dto.setDateUploaded(new Date());
                uploadedList.add(dto);
                PhotoCacheUtil.removeUploadedPhoto(getApplicationContext(), dto);
                index++;
                controlImageUploads();
            }

            @Override
            public void onPhotoUploadFailed() {
                Log.e(LOG, "------<< onPhotoUploadFailed - check and tell someone");
                failedUploads.add(dto);
                index++;
                controlImageUploads();
            }
        });
    }

    List<PhotoUploadDTO> failedUploads = new ArrayList<>();
    static final String LOG = PhotoUploadService.class.getSimpleName();

    public class LocalBinder extends Binder {
        public PhotoUploadService getService() {
            Log.e(LOG,"LocalBinder getService");
            return PhotoUploadService.this;
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.w(LOG,"IBinder onBind - returning binder");
        return mBinder;
    }

    private final IBinder mBinder = new LocalBinder();

}
