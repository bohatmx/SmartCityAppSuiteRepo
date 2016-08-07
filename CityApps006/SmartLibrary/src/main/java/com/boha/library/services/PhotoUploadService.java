package com.boha.library.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.boha.library.dto.AlertImageDTO;
import com.boha.library.dto.ComplaintImageDTO;
import com.boha.library.dto.ImageInterface;
import com.boha.library.transfer.PhotoUploadDTO;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.CDNUploader;
import com.boha.library.util.PhotoCacheUtil;
import com.boha.library.util.Util;
import com.boha.library.util.WebCheck;
import com.boha.library.util.WebCheckResult;

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
        onHandleIntent(null);
    }

    private static void getLog(ResponseDTO cache) {
        StringBuilder sb = new StringBuilder();
        sb.append("## Photos currently in the cache: ")
                .append(cache.getPhotoUploadList().size()).append("\n");
        for (PhotoUploadDTO p : cache.getPhotoUploadList()) {
            if (p.getAlertImage() != null) {
                if (p.getAlertImage().getDateTaken() == null) p.getAlertImage().setDateTaken(new Date().getTime());
                sb.append("+++ Alert: ").append(p.getAlertImage().getDateTaken().toString());
//                        .append(" lat: ").append(p.getAlertImage().getLatitude());
//                sb.append(" lng: ").append(p.getAlertImage().getLatitude());
                if (p.getDateUploaded() != null)
                    sb.append(" ").append(sdf.format(p.getDateUploaded())).append("\n");
                else
                    sb.append(" NOT UPLOADED\n");
            }
            if (p.getComplaintImage() != null) {
                if (p.getComplaintImage().getDateTaken() == null) p.getComplaintImage().setDateTaken(new Date().getTime());
                sb.append("+++ Complaint: ").append(p.getComplaintImage().getDateTaken().toString());
//                        .append(" lat: ").append(p.getAlertImage().getLatitude());
//                sb.append(" lng: ").append(p.getComplaintImage().getLatitude());
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
        WebCheckResult wcr = WebCheck.checkNetworkAvailability(getApplicationContext());
        if (wcr.isNetworkUnavailable()) {
            Log.d(LOG,"--- no network available, photoService quittin");
            return;
        }
        Log.w(LOG, "## onHandleIntent .... starting service");
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
                executeUpload(list);
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

    static List<PhotoUploadDTO> list;
    static final int MAX_RETRIES = 3;
    int retryCount;

    private void executeUpload(final List<PhotoUploadDTO> dtoList) {
        Log.d(LOG, "###### executeUpload ....list: " + dtoList.size());
        final long start = System.currentTimeMillis();

        final List<ImageInterface> list = new ArrayList<>();
        for (PhotoUploadDTO dto: dtoList) {
            ImageInterface image = null;
            if (dto.getAlertImage() != null) {
                image = dto.getAlertImage();
            }
            if (dto.getComplaintImage() != null) {
                image = dto.getComplaintImage();
            }
            if (dto.getMunicipalityImage() != null) {
                image = dto.getMunicipalityImage();
            }
            if (dto.getProfileImage() != null) {
                image = dto.getProfileImage();
            }
            if (dto.getStaffImage() != null) {
                image = dto.getStaffImage();
            }
            if (dto.getNewsArticleImage() != null) {
                image = dto.getNewsArticleImage();
            }
            list.add(image);
        }

        CDNUploader.uploadImages(getApplicationContext(), list, true, new CDNUploader.CDNUploaderListener() {
            @Override
            public void onFilesUploaded(List<ImageInterface> okList,List<ImageInterface> badList) {
                long end = System.currentTimeMillis();
                Log.i(LOG, "---- photos uploaded: " + okList.size()
                        + " failed: " + badList.size() +
                        ", elapsed: "
                        + Util.getElapsed(start, end) + " seconds");
                if (okList.size() == list.size()) {
                    PhotoCacheUtil.clearCache(getApplicationContext());
                } else {
                    clearCache(okList);
                }
                //broadcast to activities
                LocalBroadcastManager bm = LocalBroadcastManager.getInstance(getApplicationContext());
                Intent m = new Intent(BROADCAST_UPLOADED);
                bm.sendBroadcast(m);

            }

            @Override
            public void onError(String message) {
                Log.w(LOG, "--- onPhotoUploadFailed -  " + message);
            }
        });

    }
public static final String BROADCAST_UPLOADED = "com.boha.uploaded";
    private  void clearCache(List<ImageInterface> list) {
        for (ImageInterface i: list) {
            if (i instanceof ComplaintImageDTO) {
                ComplaintImageDTO x = (ComplaintImageDTO)i;
                PhotoUploadDTO photoUpload  = new PhotoUploadDTO();
                photoUpload.setComplaintImage(x);
                photoUpload.setDateUploaded(new Date().getTime());
                PhotoCacheUtil.removeUploadedPhoto(getApplicationContext(), photoUpload);
            }
            if (i instanceof AlertImageDTO) {
                AlertImageDTO x = (AlertImageDTO)i;
                PhotoUploadDTO photoUpload  = new PhotoUploadDTO();
                photoUpload.setAlertImage(x);
                photoUpload.setDateUploaded(new Date().getTime());
                PhotoCacheUtil.removeUploadedPhoto(getApplicationContext(), photoUpload);
            }
        }
    }
    private static
    List<PhotoUploadDTO> failedUploads = new ArrayList<>();
    static final String LOG = PhotoUploadService.class.getSimpleName();

    public class LocalBinder extends Binder {
        public PhotoUploadService getService() {
            Log.e(LOG, "LocalBinder getService");
            return PhotoUploadService.this;
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.w(LOG, "IBinder onBind - returning binder");
        return mBinder;
    }

    private final IBinder mBinder = new LocalBinder();

}
