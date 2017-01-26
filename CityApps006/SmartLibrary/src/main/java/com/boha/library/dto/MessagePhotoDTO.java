package com.boha.library.dto;

import java.io.Serializable;

/**
 * Created by Nkululeko on 2017/01/16.
 */

public class MessagePhotoDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer messagePhotoID;
    private Long date;
    private String url;
    private Integer clientMessageID;
    private Integer suburbMessageID;
    private Integer districtMessageID;

    public MessagePhotoDTO() {
    }

    public Integer getMessagePhotoID() {
        return messagePhotoID;
    }

    public void setMessagePhotoID(Integer messagePhotoID) {
        this.messagePhotoID = messagePhotoID;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Integer getClientMessageID() {
        return clientMessageID;
    }

    public void setClientMessageID(Integer clientMessageID) {
        this.clientMessageID = clientMessageID;
    }

    public Integer getSuburbMessageID() {
        return suburbMessageID;
    }

    public void setSuburbMessageID(Integer suburbMessageID) {
        this.suburbMessageID = suburbMessageID;
    }

    public Integer getDistrictMessageID() {
        return districtMessageID;
    }

    public void setDistrictMessageID(Integer districtMessageID) {
        this.districtMessageID = districtMessageID;
    }
}
