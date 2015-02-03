package com.boha.library.util;


import com.boha.library.dto.PhotoUploadDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aubreyM on 2014/10/15.
 */
public class PhotoCache implements Serializable {
    private List<PhotoUploadDTO> photoUploadList = new ArrayList<>();

    public List<PhotoUploadDTO> getPhotoUploadList() {
        return photoUploadList;
    }

    public void setPhotoUploadList(List<PhotoUploadDTO> photoUploadList) {
        this.photoUploadList = photoUploadList;
    }
}
