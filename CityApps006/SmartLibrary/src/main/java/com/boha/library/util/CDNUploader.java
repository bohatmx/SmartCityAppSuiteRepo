package com.boha.library.util;

import android.content.Context;
import android.util.Log;

import com.boha.library.dto.AlertImageDTO;
import com.boha.library.dto.ComplaintImageDTO;
import com.boha.library.dto.ImageInterface;
import com.boha.library.dto.MunicipalityImageDTO;
import com.boha.library.dto.NewsArticleImageDTO;
import com.boha.library.dto.ProfileImageDTO;
import com.boha.library.dto.StaffImageDTO;
import com.boha.library.transfer.PhotoUploadDTO;
import com.boha.library.transfer.RequestDTO;
import com.boha.library.transfer.ResponseDTO;
import com.cloudinary.Cloudinary;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by aubreyM on 15/06/08.
 */
public class CDNUploader {
    public interface CDNUploaderListener {
        void onFilesUploaded(List<ImageInterface> okUploads, List<ImageInterface> failedUploads);
        void onError(String message);
    }
    public interface SingleUploadListener {
        void onFileUploaded(ImageInterface image);
        void onError(ImageInterface image);
    }
    static CDNUploaderListener mListener;
    static final String LOG = CDNUploader.class.getSimpleName();
    static final String
            API_KEY = "397571984789619",
            API_SECRET = "2RBq1clEHC5X_0eQlNP-K3yhA8U",
            CLOUD_NAME = "bohatmx";


    private static String getPublicID(final Context ctx,PhotoUploadDTO dto) {
        StringBuilder sb = new StringBuilder();
        sb.append("sc").append(SharedUtil.getMunicipality(ctx).getMunicipalityID());
        sb.append("/p").append(dto.getMunicipalityID());

        return sb.toString();
    }

    static int index;
    static List<ImageInterface> okList, badList;

    public static void uploadImages(final Context ctx, final List<ImageInterface> imageList,  boolean start,
                             final CDNUploaderListener uploaderListener) {

        List<ImageInterface> list = new ArrayList<>();

        if (start) {
            index = 0;
            okList = new ArrayList<>();
            badList = new ArrayList<>();
        }
        uploadImage(ctx, imageList.get(index), new SingleUploadListener() {

            @Override
            public void onFileUploaded(ImageInterface image) {
                index++;
                okList.add(image);
                if (index < imageList.size()) {
                    uploadImages(ctx, imageList, false, uploaderListener);
                } else {
                    uploaderListener.onFilesUploaded(okList,badList);
                }
            }

            @Override
            public void onError(ImageInterface image) {
                badList.add(image);
                Log.w(LOG,"Failed image in badList, index; " + index);
                index++;
                if (index < imageList.size()) {
                    uploadImages(ctx,imageList, false, uploaderListener);
                } else {
                    uploaderListener.onFilesUploaded(okList,badList);
                }
            }
        });

    }

    private static void uploadImage(final Context ctx, final ImageInterface image,
                                   final SingleUploadListener listener) {

        Log.d(LOG, "### starting thread to upload one image");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final long start = System.currentTimeMillis();
                Map config = new HashMap();
                config.put("cloud_name", CLOUD_NAME);
                config.put("api_key", API_KEY);
                config.put("api_secret", API_SECRET);
//                config.put("public_id", getPublicID(ctx,dto));

                Cloudinary cloudinary = new Cloudinary(config);
                File file = image.getFile();
                Map map = null;
                try {
                    Log.d(LOG, "##### starting cloudinary upload...: " + image.getFile().getAbsolutePath());
                    map = cloudinary.uploader().upload(file, config);
                } catch (Exception e) {
                    Log.e(LOG, "-- CDN Failed: " + e.getMessage(), e);
                    listener.onError(image);
                    return;
                }

                long end = System.currentTimeMillis();
                Log.i(LOG, "+++++ photo uploaded: "+ map.get("url") + " elapsed: "
                        + Util.getElapsed(start, end) + " seconds");

                image.setUrl((String) map.get("url"));
                image.setSecureUrl((String) map.get("secure_url"));
                image.setSignature((String) map.get("signature"));
                image.seteTag((String) map.get("etag"));
                image.setHeight((int) map.get("height"));
                image.setWidth((int) map.get("width"));
                image.setBytes((int) map.get("bytes"));

                RequestDTO w = new RequestDTO(RequestDTO.ADD_PHOTO);
                w.setPhotoUpload(new PhotoUploadDTO());
                if (image instanceof AlertImageDTO) {
                    w.getPhotoUpload().setAlertImage((AlertImageDTO) image);
                }
                if (image instanceof ComplaintImageDTO) {
                    w.getPhotoUpload().setComplaintImage((ComplaintImageDTO) image);
                }
                if (image instanceof MunicipalityImageDTO) {
                    w.getPhotoUpload().setMunicipalityImage((MunicipalityImageDTO) image);
                }
                if (image instanceof ProfileImageDTO) {
                    w.getPhotoUpload().setProfileImage((ProfileImageDTO) image);
                }
                if (image instanceof StaffImageDTO) {
                    w.getPhotoUpload().setStaffImage((StaffImageDTO) image);
                }
                if (image instanceof NewsArticleImageDTO) {
                    w.getPhotoUpload().setNewsArticleImage((NewsArticleImageDTO) image);
                }

                NetUtil.sendRequest(ctx, w, new NetUtil.NetUtilListener() {
                    @Override
                    public void onResponse(ResponseDTO response) {
                        Log.i(LOG, "#### yebo!! photo metadata sent to server");
                        listener.onFileUploaded(image);
                    }

                    @Override
                    public void onError(String message) {
                        Log.e(LOG, message);
                        listener.onError(image);
                    }

                    @Override
                    public void onWebSocketClose() {

                    }
                });

            }
        });

        thread.start();
    }
}
