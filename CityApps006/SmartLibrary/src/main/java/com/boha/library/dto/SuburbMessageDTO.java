package com.boha.library.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Nkululeko on 2017/01/16.
 */

public class SuburbMessageDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer suburbMessageID;
    private String message, suburbName;
    private Long messageDate;
    private Long messageExpiryDate;
    private Integer suburbID;
    private List<MessagePhotoDTO> messagePhotoList;
    private SuburbMessageDTO replyTo;

    public SuburbMessageDTO() {
    }

    public SuburbMessageDTO getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(SuburbMessageDTO replyTo) {
        this.replyTo = replyTo;
    }

    public List<MessagePhotoDTO> getMessagePhotoList() {
        return messagePhotoList;
    }

    public void setMessagePhotoList(List<MessagePhotoDTO> messagePhotoList) {
        this.messagePhotoList = messagePhotoList;
    }

    public Integer getSuburbID() {
        return suburbID;
    }

    public void setSuburbID(Integer suburbID) {
        this.suburbID = suburbID;
    }

    public String getSuburbName() {
        return suburbName;
    }

    public void setSuburbName(String suburbName) {
        this.suburbName = suburbName;
    }

    public Integer getSuburbMessageID() {
        return suburbMessageID;
    }

    public void setSuburbMessageID(Integer suburbMessageID) {
        this.suburbMessageID = suburbMessageID;
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
}
