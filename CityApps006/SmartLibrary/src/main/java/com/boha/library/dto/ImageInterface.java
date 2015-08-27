package com.boha.library.dto;

import java.io.File;

/**
 * Created by aubreyM on 15/03/02.
 */
public interface ImageInterface {
    File getFile();
    void setUrl(String url);
    void setSecureUrl(String url);
    void setSignature(String signature);
    void seteTag(String eTag);
    void setHeight(Integer height);
    void setWidth(Integer width);
    void setBytes(Integer bytes);
    void setDateUploaded(Long date);
}
