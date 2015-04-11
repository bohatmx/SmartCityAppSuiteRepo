package com.boha.library.dto;

import java.io.Serializable;

/**
 * Created by aubreyM on 15/04/11.
 */
public class ComplaintFollowerDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer complaintFollowerID;
    private Long dateFollowed;
    private String comment;
    private Integer profileInfoID;
    private Integer complaintID;

    public Integer getComplaintFollowerID() {
        return complaintFollowerID;
    }

    public void setComplaintFollowerID(Integer complaintFollowerID) {
        this.complaintFollowerID = complaintFollowerID;
    }

    public Long getDateFollowed() {
        return dateFollowed;
    }

    public void setDateFollowed(Long dateFollowed) {
        this.dateFollowed = dateFollowed;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getProfileInfoID() {
        return profileInfoID;
    }

    public void setProfileInfoID(Integer profileInfoID) {
        this.profileInfoID = profileInfoID;
    }

    public Integer getComplaintID() {
        return complaintID;
    }

    public void setComplaintID(Integer complaintID) {
        this.complaintID = complaintID;
    }
}
