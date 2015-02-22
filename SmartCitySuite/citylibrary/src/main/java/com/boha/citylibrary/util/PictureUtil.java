package com.boha.citylibrary.util;

import android.content.Context;
import android.util.Log;

import com.boha.citylibrary.transfer.PhotoUploadDTO;
import com.boha.citylibrary.transfer.ResponseDTO;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PictureUtil {

    public static void uploadImage(PhotoUploadDTO dto,
                                   Context ctx, final PhotoUploadDTO.PhotoUploadedListener listener) {

        File imageFile = null;
        if (dto.getMunicipalityImage() != null) {
            imageFile = new File(dto.getMunicipalityImage().getLocalFilepath());
        }
        if (dto.getStaffImage() != null) {
            imageFile = new File(dto.getStaffImage().getLocalFilepath());
        }
        if (dto.getComplaintImage() != null) {
            imageFile = new File(dto.getComplaintImage().getLocalFilepath());
        }
        if (dto.getNewsArticleImage() != null) {
            imageFile = new File(dto.getNewsArticleImage().getLocalFilepath());
        }
        if (dto.getAlertImage() != null) {
            imageFile = new File(dto.getAlertImage().getLocalFilepath());
        }

        if (imageFile == null) {
            listener.onPhotoUploadFailed();
            return;
        }
        Log.w(LOG, "*** File to be uploaded - length: " + imageFile.length() + " - " + imageFile.getAbsolutePath());
        List<File> files = new ArrayList<File>();
        if (imageFile.exists()) {
            files.add(imageFile);
            //set ...
            ImageUpload.upload(dto, files, ctx,
                    new ImageUpload.ImageUploadListener() {
                        @Override
                        public void onUploadError() {
                            listener.onPhotoUploadFailed();
                            Log.e(LOG,
                                    "Error uploading - onUploadError");
                        }

                        @Override
                        public void onImageUploaded(ResponseDTO response) {

                            if (response.getStatusCode() == 0) {
                                listener.onPhotoUploaded();
                            } else {
                                Log.e(LOG,
                                        "Error uploading - "
                                                + response.getMessage()
                                );
                            }
                        }
                    }
            );
        }
    }

    private static final String LOG = PictureUtil.class.getSimpleName();


}
