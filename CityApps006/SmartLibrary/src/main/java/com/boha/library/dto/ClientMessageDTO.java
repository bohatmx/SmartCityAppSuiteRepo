package com.boha.library.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Nkululeko on 2017/01/16.
 */

public class ClientMessageDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer clientMessageID;
    private String message, email;
    private Long messageDate;
    private Long messageExpiryDate;
    private Integer profileInfoID;
    private List<MessagePhotoDTO> messagePhotoList;
    private ClientMessageDTO replyTo;

    public ClientMessageDTO() {
    }

    public ClientMessageDTO getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(ClientMessageDTO replyTo) {
        this.replyTo = replyTo;
    }

    public List<MessagePhotoDTO> getMessagePhotoList() {
        return messagePhotoList;
    }

    public void setMessagePhotoList(List<MessagePhotoDTO> messagePhotoList) {
        this.messagePhotoList = messagePhotoList;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getClientMessageID() {
        return clientMessageID;
    }

    public void setClientMessageID(Integer clientMessageID) {
        this.clientMessageID = clientMessageID;
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

    public Integer getProfileInfoID() {
        return profileInfoID;
    }

    public void setProfileInfoID(Integer profileInfoID) {
        this.profileInfoID = profileInfoID;
    }
}
