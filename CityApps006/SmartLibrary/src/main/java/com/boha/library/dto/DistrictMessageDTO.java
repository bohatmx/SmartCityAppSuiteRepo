package com.boha.library.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Nkululeko on 2017/01/16.
 */

public class DistrictMessageDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer districtMessageID;
    private String message, districtName;
    private Long messageDate;
    private Long messageExpiryDate;
    private Integer districtID;
    private List<MessagePhotoDTO> messagePhotoList;
    private DistrictMessageDTO replyTo;

    public DistrictMessageDTO() {
    }


    public DistrictMessageDTO getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(DistrictMessageDTO replyTo) {
        this.replyTo = replyTo;
    }

    public List<MessagePhotoDTO> getMessagePhotoList() {
        return messagePhotoList;
    }

    public void setMessagePhotoList(List<MessagePhotoDTO> messagePhotoList) {
        this.messagePhotoList = messagePhotoList;
    }


    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public Integer getDistrictMessageID() {
        return districtMessageID;
    }

    public void setDistrictMessageID(Integer districtMessageID) {
        this.districtMessageID = districtMessageID;
    }


    public String getMessage() {
        return message;
    }


    public void setMessage(String message) {
        this.message = message;
    }

    public Long getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(Long messageDate) {
        this.messageDate = messageDate;
    }

    public Long getMessageExpiryDate() {
        return messageExpiryDate;
    }

    public void setMessageExpiryDate(Long messageExpiryDate) {
        this.messageExpiryDate = messageExpiryDate;
    }

    public Integer getDistrictID() {
        return districtID;
    }

    public void setDistrictID(Integer districtID) {
        this.districtID = districtID;
    }
}
